package dev.tr7zw.trender.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;

import dev.tr7zw.transition.mc.EntityUtil;
import dev.tr7zw.transition.mc.LightingUtil;
import dev.tr7zw.transition.mc.MathUtil;
import dev.tr7zw.trender.gui.client.RenderContext;
import dev.tr7zw.trender.gui.widget.data.InputResult;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

//#if MC < 12102
//$$import com.mojang.blaze3d.systems.RenderSystem;
//#endif

@Setter
@Getter
public class WPlayerPreview extends WWidget {
    private int rotationX = 0;
    private int rotationY = 0;
    private boolean showBackground = false;

    public WPlayerPreview() {
        setSize(60, 90);
    }

    @Override
    public void paint(RenderContext context, int x, int y, int mouseX, int mouseY) {
        if (showBackground) {
            context.fill(x, y, x + getWidth(), y + getHeight(), 0xFF000000);
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            drawEntity(context, x + getWidth() / 2, y + getHeight() / 2, this.getWidth(), this.getHeight(), 40, rotationX, rotationY, mc.player, 0);
        }
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        rotationX -= deltaX;
        // FIXME: Breaks in 1.21.6, so disabled for now
        //#if MC < 12106
        //$$ rotationY -= deltaY;
        //#endif
        return InputResult.PROCESSED;
    }

    // Modified version from InventoryScreen
    private static void drawEntity(RenderContext context, int x, int y, int width, int height, int size, float lookX, float lookY, LivingEntity livingEntity,
            float delta) {
        float rotationModifier = 3;
        prepareViewMatrix(x, y);
        PoseStack matrixStack = new PoseStack();
        matrixStack.translate(x, y, 500.0D);
        matrixStack.scale((float) size, (float) size, (float) size);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        var quaternion = MathUtil.ZP.rotationDegrees(180.0F);
        var quaternion2 = MathUtil.XP.rotationDegrees(lookY * rotationModifier);
        quaternion.mul(quaternion2);
        matrixStack.mulPose(quaternion);
        matrixStack.translate(0.0D, -1, 0D);
        float yBodyRot = livingEntity.yBodyRot;
        float yRot = EntityUtil.getYRot(livingEntity);
        float yRotO = livingEntity.yRotO;
        float yBodyRotO = livingEntity.yBodyRotO;
        float xRot = EntityUtil.getXRot(livingEntity);
        float xRotO = livingEntity.xRotO;
        float yHeadRotO = livingEntity.yHeadRotO;
        float yHeadRot = livingEntity.yHeadRot;
        Vec3 vel = livingEntity.getDeltaMovement();
        livingEntity.yBodyRot = (180.0F + lookX * rotationModifier);
        EntityUtil.setYRot(livingEntity, (180.0F + lookX * rotationModifier));
        livingEntity.yBodyRotO = livingEntity.yBodyRot;
        livingEntity.yRotO = EntityUtil.getYRot(livingEntity);
        livingEntity.setDeltaMovement(Vec3.ZERO);
        EntityUtil.setXRot(livingEntity, 0);
        livingEntity.xRotO = EntityUtil.getXRot(livingEntity);
        livingEntity.yHeadRot = EntityUtil.getYRot(livingEntity);
        livingEntity.yHeadRotO = EntityUtil.getYRot(livingEntity);
        LightingUtil.prepareLightingEntity();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        MathUtil.conjugate(quaternion2);
        entityRenderDispatcher.overrideCameraOrientation(quaternion2);
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        //#if MC >= 12106
        var entityRenderer = entityRenderDispatcher.getRenderer(livingEntity);
        var entityRenderState = entityRenderer.createRenderState(livingEntity, 1.0F);
        entityRenderState.hitboxesRenderState = null;
        float o = livingEntity.getScale();
        var vector3f = new org.joml.Vector3f(0.0F, livingEntity.getBbHeight() / 2.0F + 0 * o, 0.0F);
        float p = (float) size / o;
        context.getGuiGraphics().submitEntityRenderState(entityRenderState, p, vector3f, quaternion, quaternion2, x - width, y - height, x+width, y+height);
        //#elseif MC >= 12102
        //$$ entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, delta, matrixStack, bufferSource, 15728880);
        //#elseif MC >= 11700
        //$$entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack, bufferSource, 15728880);
        //#else
        //$$ RenderSystem.runAsFancy(() -> {
        //$$    entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, delta, matrixStack, bufferSource, 15728880);
        //$$ });
        //#endif
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = yBodyRot;
        livingEntity.yBodyRotO = yBodyRotO;
        EntityUtil.setYRot(livingEntity, yRot);
        livingEntity.yRotO = yRotO;
        EntityUtil.setXRot(livingEntity, xRot);
        livingEntity.xRotO = xRotO;
        livingEntity.yHeadRotO = yHeadRotO;
        livingEntity.yHeadRot = yHeadRot;
        livingEntity.setDeltaMovement(vel);
        resetViewMatrix();
    }

    private static void resetViewMatrix() {
        //#if MC >= 12102
        // nothing
        //#elseif MC >= 11700
        //$$ RenderSystem.applyModelViewMatrix();
        //#else
        //$$ RenderSystem.popMatrix();
        //#endif
    }

    private static void prepareViewMatrix(double xpos, double ypos) {
        //#if MC >= 12102
        // nothing
        //#elseif MC >= 11700
        //$$ RenderSystem.applyModelViewMatrix();
        //#else
        //$$ RenderSystem.pushMatrix();
        //$$ RenderSystem.translatef((float)xpos, (float)ypos, 1050.0F);
        //$$ RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        //#endif
    }

}