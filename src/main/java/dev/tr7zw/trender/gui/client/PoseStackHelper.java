package dev.tr7zw.trender.gui.client;

public interface PoseStackHelper {

    //? if >= 1.21.6 {

    public org.joml.Matrix3x2fStack getPose();
    //? } else {
    /*
    public com.mojang.blaze3d.vertex.PoseStack getPose();
    *///? }

    public default void pushPose() {
        //? if >= 1.21.6 {

        getPose().pushMatrix();
        //? } else {
        /*
        getPose().pushPose();
        *///? }
    }

    public default void popPose() {
        //? if >= 1.21.6 {

        getPose().popMatrix();
        //? } else {
        /*
        getPose().popPose();
        *///? }
    }

    public default void translate(float x, float y) {
        //? if >= 1.21.6 {

        getPose().translate(x, y);
        //? } else {
        /*
        getPose().translate(x, y, 0);
        *///? }
    }

    public default void scale(float x, float y) {
        //? if >= 1.21.6 {

        getPose().scale(x, y);
        //? } else {
        /*
        getPose().scale(x, y, 1);
        *///? }
    }

    //? if >= 1.19.4 {

    public default void rotate(org.joml.Quaternionf quaternion) {
        //? if >= 1.21.6 {

        getPose().rotate(quaternion.angle());
        //? } else {
        /*
        getPose().mulPose(quaternion);
        *///? }
    }
    //? } else {
    /*
    public default void rotate(com.mojang.math.Quaternion quaternion) {
        getPose().mulPose(quaternion);
    }
    *///? }
}
