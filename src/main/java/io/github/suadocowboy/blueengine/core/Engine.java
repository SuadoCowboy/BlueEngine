package io.github.suadocowboy.blueengine.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {

    static public void init() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    /**
     * terminate GLFW and free the error callback
     */
    static public void terminate() {
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}
