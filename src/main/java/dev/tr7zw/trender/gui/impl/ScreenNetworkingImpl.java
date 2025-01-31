//package dev.tr7zw.trender.gui.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//import java.util.WeakHashMap;
//import java.util.concurrent.Executor;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.mojang.serialization.DataResult;
//import com.mojang.serialization.Decoder;
//import com.mojang.serialization.Encoder;
//import com.mojang.serialization.Lifecycle;
//
//import dev.tr7zw.trender.gui.SyncedGuiDescription;
//import dev.tr7zw.trender.gui.networking.NetworkSide;
//import dev.tr7zw.trender.gui.networking.ScreenNetworking;
//import net.minecraft.core.RegistryAccess;
//import net.minecraft.nbt.NbtAccounter;
//import net.minecraft.nbt.NbtOps;
//import net.minecraft.nbt.Tag;
//import net.minecraft.network.RegistryFriendlyByteBuf;
//import net.minecraft.network.codec.ByteBufCodecs;
//import net.minecraft.network.codec.StreamCodec;
//import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
//import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
//import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//import net.minecraft.resources.RegistryOps;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//
//public class ScreenNetworkingImpl implements ScreenNetworking {
//    // Matches the one used in PacketCodecs.codec() etc
//    private static final long MAX_NBT_SIZE = 0x200000L;
//
//    public record ScreenMessage(int syncId, ResourceLocation message, Tag nbt) implements CustomPacketPayload {
//
//        public static final Type<ScreenMessage> ID = new Type<>(LibGuiCommon.id("screen_message"));
//        public static final StreamCodec<RegistryFriendlyByteBuf, ScreenMessage> CODEC = StreamCodec.composite(
//                ByteBufCodecs.INT, ScreenMessage::syncId, ResourceLocation.STREAM_CODEC, ScreenMessage::message,
//                ByteBufCodecs.tagCodec(() -> NbtAccounter.create(MAX_NBT_SIZE)), ScreenMessage::nbt,
//                ScreenMessage::new);
//
//        @Override
//        public Type<? extends CustomPacketPayload> type() {
//            return ID;
//        }
//    }
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenNetworkingImpl.class);
//    private static final Map<SyncedGuiDescription, ScreenNetworkingImpl> instanceCache = new WeakHashMap<>();
//
//    private final Map<ResourceLocation, ReceiverData<?>> receivers = new HashMap<>();
//    private final SyncedGuiDescription description;
//    private final NetworkSide side;
//
//    private ScreenNetworkingImpl(SyncedGuiDescription description, NetworkSide side) {
//        this.description = description;
//        this.side = side;
//    }
//
//    private static RegistryOps<Tag> getRegistryOps(RegistryAccess registryManager) {
//        return registryManager.createSerializationContext(NbtOps.INSTANCE);
//    }
//
//    @Override
//    public <D> void receive(ResourceLocation message, Decoder<D> decoder, MessageReceiver<D> receiver) {
//        Objects.requireNonNull(message, "message");
//        Objects.requireNonNull(decoder, "decoder");
//        Objects.requireNonNull(receiver, "receiver");
//
//        if (!receivers.containsKey(message)) {
//            receivers.put(message, new ReceiverData<>(decoder, receiver));
//        } else {
//            throw new IllegalStateException("Message " + message + " on side " + side + " already registered");
//        }
//    }
//
//    @Override
//    public <D> void send(ResourceLocation message, Encoder<D> encoder, D data) {
//        Objects.requireNonNull(message, "message");
//        Objects.requireNonNull(encoder, "encoder");
//
//        var ops = getRegistryOps(description.getWorld().registryAccess());
//        Tag encoded = encoder.encodeStart(ops, data).getOrThrow();
//        ScreenMessage packet = new ScreenMessage(description.containerId, message, encoded);
//        if (side == NetworkSide.SERVER) {
//            description.getPacketSender().accept(new ClientboundCustomPayloadPacket(packet));
//        } else {
//            description.getPacketSender().accept(new ServerboundCustomPayloadPacket(packet));
//        }
//    }
//
//    public static void init() {
//        // TODO
//        //        PayloadTypeRegistry.playS2C().register(ScreenMessage.ID, ScreenMessage.CODEC);
//        //        PayloadTypeRegistry.playC2S().register(ScreenMessage.ID, ScreenMessage.CODEC);
//        //        ServerPlayNetworking.registerGlobalReceiver(ScreenMessage.ID, (payload, context) -> {
//        //            handle(context.player().server, context.player(), payload);
//        //        });
//    }
//
//    public static void handle(Executor executor, Player player, ScreenMessage packet) {
//        AbstractContainerMenu screenHandler = player.containerMenu;
//
//        if (!(screenHandler instanceof SyncedGuiDescription)) {
//            LOGGER.error("Received message packet for screen handler {} which is not a SyncedGuiDescription",
//                    screenHandler);
//            return;
//        } else if (packet.syncId() != screenHandler.containerId) {
//            LOGGER.error("Received message for sync ID {}, current sync ID: {}", packet.syncId(),
//                    screenHandler.containerId);
//            return;
//        }
//
//        ScreenNetworkingImpl networking = instanceCache.get(screenHandler);
//
//        if (networking != null) {
//            ReceiverData<?> receiverData = networking.receivers.get(packet.message());
//            if (receiverData != null) {
//                processMessage(executor, player, packet, screenHandler, receiverData);
//            } else {
//                LOGGER.error("Message {} not registered for {} on side {}", packet.message(), screenHandler,
//                        networking.side);
//            }
//        } else {
//            LOGGER.warn("GUI description {} does not use networking", screenHandler);
//        }
//    }
//
//    private static <D> void processMessage(Executor executor, Player player, ScreenMessage packet,
//            AbstractContainerMenu description, ReceiverData<D> receiverData) {
//        var ops = getRegistryOps(player.registryAccess());
//        var result = receiverData.decoder().parse(ops, packet.nbt());
//
//        switch (result) {
//        case DataResult.Success(D data, Lifecycle lifecycle) -> executor.execute(() -> {
//            try {
//                receiverData.receiver().onMessage(data);
//            } catch (Exception e) {
//                LOGGER.error("Error handling screen message {} for {}", packet.message(), description, e);
//            }
//        });
//
//        case DataResult.Error<D> error ->
//            LOGGER.error("Could not parse screen message {}: {}", packet.message(), error.message());
//        }
//    }
//
//    public static ScreenNetworking of(SyncedGuiDescription description, NetworkSide networkSide) {
//        Objects.requireNonNull(description, "description");
//        Objects.requireNonNull(networkSide, "networkSide");
//
//        if (description.getNetworkSide() == networkSide) {
//            return instanceCache.computeIfAbsent(description, it -> new ScreenNetworkingImpl(description, networkSide));
//        } else {
//            return DummyNetworking.INSTANCE;
//        }
//    }
//
//    private record ReceiverData<D>(Decoder<D> decoder, MessageReceiver<D> receiver) {
//    }
//
//    private static final class DummyNetworking extends ScreenNetworkingImpl {
//        static final DummyNetworking INSTANCE = new DummyNetworking();
//
//        private DummyNetworking() {
//            super(null, null);
//        }
//
//        @Override
//        public <D> void receive(ResourceLocation message, Decoder<D> decoder, MessageReceiver<D> receiver) {
//            // NO-OP
//        }
//
//        @Override
//        public <D> void send(ResourceLocation message, Encoder<D> encoder, D data) {
//            // NO-OP
//        }
//    }
//}
