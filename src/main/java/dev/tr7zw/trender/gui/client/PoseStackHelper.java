package dev.tr7zw.trender.gui.client;

public interface PoseStackHelper {

    //#if MC >= 12106
    public org.joml.Matrix3x2fStack getPose();
    //#else
    //$$  public com.mojang.blaze3d.vertex.PoseStack getPose();
    //#endif

    public default void pushPose() {
        //#if MC >= 12106
        getPose().pushMatrix();
        //#else
        //$$ getPose().pushPose();
        //#endif
    }

    public default void popPose() {
        //#if MC >= 12106
        getPose().popMatrix();
        //#else
        //$$ getPose().popPose();
        //#endif
    }

    public default void translate(float x, float y) {
        //#if MC >= 12106
        getPose().translate(x, y);
        //#else
        //$$ getPose().translate(x, y, 0);
        //#endif
    }

    public default void scale(float x, float y) {
        //#if MC >= 12106
        getPose().scale(x, y);
        //#else
        //$$ getPose().scale(x, y, 1);
        //#endif
    }

    //#if MC >= 11904
    public default void rotate(org.joml.Quaternionf quaternion) {
        //#if MC >= 12106
        getPose().rotate(quaternion.angle());
        //#else
        //$$ getPose().mulPose(quaternion);
        //#endif
    }
    //#else
    //$$ public default void rotate(com.mojang.math.Quaternion quaternion) {
    //$$    getPose().mulPose(quaternion);
    //$$}
    //#endif
}
