package dev.tr7zw.trender.gui.client;

//? if >= 26.3 {

import org.lwjgl.sdl.*;
//? } else {

/*import org.lwjgl.glfw.*;

*///? }

// Keycodes in GLFW or SDL format, depending on the Minecraft version
public class KeyCodes {

    //? if >= 26.3 {

    public static final int ENTER = SDLScancode.SDL_SCANCODE_RETURN;
    public static final int KP_ENTER = SDLScancode.SDL_SCANCODE_KP_ENTER;
    public static final int SPACE = SDLScancode.SDL_SCANCODE_SPACE;
    public static final int MOD_CONTROL = SDLKeycode.SDL_KMOD_CTRL;
    public static final int RCONTROL = SDLScancode.SDL_SCANCODE_RCTRL;
    public static final int LCONTROL = SDLScancode.SDL_SCANCODE_LCTRL;
    public static final int MOD_SHIFT = SDLKeycode.SDL_KMOD_SHIFT;
    public static final int RSHIFT = SDLScancode.SDL_SCANCODE_RSHIFT;
    public static final int LSHIFT = SDLScancode.SDL_SCANCODE_LSHIFT;
    public static final int RIGHT = SDLScancode.SDL_SCANCODE_RIGHT;
    public static final int LEFT = SDLScancode.SDL_SCANCODE_LEFT;
    public static final int UP = SDLScancode.SDL_SCANCODE_UP;
    public static final int DOWN = SDLScancode.SDL_SCANCODE_DOWN;
    public static final int LALT = SDLScancode.SDL_SCANCODE_LALT;
    public static final int RALT = SDLScancode.SDL_SCANCODE_RALT;
    public static final int DELETE = SDLScancode.SDL_SCANCODE_DELETE;
    public static final int BACKSPACE = SDLScancode.SDL_SCANCODE_BACKSPACE;
    public static final int END = SDLScancode.SDL_SCANCODE_END;
    public static final int HOME = SDLScancode.SDL_SCANCODE_HOME;
    public static final int KEY_V = SDLScancode.SDL_SCANCODE_V;
    public static final int KEY_C = SDLScancode.SDL_SCANCODE_C;
    public static final int KEY_A = SDLScancode.SDL_SCANCODE_A;

    //? } else {

    /*public static final int ENTER = GLFW.GLFW_KEY_ENTER;
    public static final int KP_ENTER = GLFW.GLFW_KEY_KP_ENTER;
    public static final int SPACE = GLFW.GLFW_KEY_SPACE;
    public static final int MOD_CONTROL = GLFW.GLFW_MOD_CONTROL;
    public static final int RCONTROL = GLFW.GLFW_KEY_RIGHT_CONTROL;
    public static final int LCONTROL = GLFW.GLFW_KEY_LEFT_CONTROL;
    public static final int MOD_SHIFT = GLFW.GLFW_MOD_SHIFT;
    public static final int RSHIFT = GLFW.GLFW_KEY_RIGHT_SHIFT;
    public static final int LSHIFT = GLFW.GLFW_KEY_LEFT_SHIFT;
    public static final int RIGHT = GLFW.GLFW_KEY_RIGHT;
    public static final int LEFT = GLFW.GLFW_KEY_LEFT;
    public static final int UP = GLFW.GLFW_KEY_UP;
    public static final int DOWN = GLFW.GLFW_KEY_DOWN;
    public static final int LALT = GLFW.GLFW_KEY_LEFT_ALT;
    public static final int RALT = GLFW.GLFW_KEY_RIGHT_ALT;
    public static final int DELETE = GLFW.GLFW_KEY_DELETE;
    public static final int BACKSPACE = GLFW.GLFW_KEY_BACKSPACE;
    public static final int END = GLFW.GLFW_KEY_END;
    public static final int HOME = GLFW.GLFW_KEY_HOME;
    public static final int KEY_V = GLFW.GLFW_KEY_V;
    public static final int KEY_C = GLFW.GLFW_KEY_C;
    public static final int KEY_A = GLFW.GLFW_KEY_A;
    
    
    *///? }

}
