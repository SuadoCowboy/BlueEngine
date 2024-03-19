package io.github.suadocowboy.blueengine.core;

import org.joml.Vector2i;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.opengl.GL11;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private CharSequence title;

    private Vector2i size = new Vector2i(1,1);

    private long window;

    private boolean vSync;

    private Vector4f clearColor = new Vector4f(0,0,0,0);

    IGameLogic gameLogic;

    /**
     *
     * @param width width of window
     * @param height height of window
     * @param title title of window
     * @param monitor monitor to use. NULL for primary monitor
     */
    public Window(IGameLogic gameLogic, int width, int height, CharSequence title, long monitor) {
        this.gameLogic = gameLogic;

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        size.x = width;
        size.y = height;
        window = glfwCreateWindow(width, height, title, monitor, NULL);
        if ( window == NULL ) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        centerScreen();


        glfwSetKeyCallback(window, this::keyCallback);
        glfwSetFramebufferSizeCallback(window, this::framebufferSizeCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Make the window visible
        glfwShowWindow(window);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();
    }

    public void setPosition(Vector2i position) {
        setPosition(position.x, position.y);
    }

    public void setPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    /**
     * centers the window on the screen
     */
    public void centerScreen() {
        // Get the thread stack and push a new frame
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            setPosition(
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically
    }

    /**
     * clears the window framebuffer
     */
    public void clear() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
    }

    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * free the window callbacks and destroy the window
     */
    public void terminate() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    private void keyCallback(long glfwWindow, int key, int scancode, int action, int mods) {
        gameLogic.keyCallback(key, scancode, action, mods);
    }

    /**
     * DO NOT forget to put updateViewport at the end of your framebuffer size callback!
     */
    public void framebufferSizeCallback(long glfwWindow, int width, int height) {
        updateViewport();
        clear();
        gameLogic.framebufferSizeCallback();
    }

    public boolean isKeyPressed(int key) {
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public void setShouldClose(boolean shouldClose) {
        glfwSetWindowShouldClose(window, shouldClose);
    }

    public Vector2i getSize() {
        return size;
    }

    /**
     * updates window size and viewport
     */
    public void setSize(int x, int y) {
        glfwSetWindowSize(window, x, y);
        updateViewport(); // also sets the new size
    }

    /**
     * must be called whenever window size changes
     */
    public void updateViewport() {
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            size.x = pWidth.get(0);
            size.y = pHeight.get(0);
        } // the stack frame is popped automatically

        GL11.glViewport(0, 0, size.x, size.y);
    }

    public void setSize(Vector2i size) {
        setSize(size.x, size.y);
    }

    public boolean isvSync() {
        return vSync;
    }

    public void setvSync(boolean vSync) {
        this.vSync = vSync;
        if (vSync)
            glfwSwapInterval(1);
        else
            glfwSwapInterval(0);
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
        glfwSetWindowTitle(window, title);
    }

    public Vector4f getClearColor() {
        return clearColor;
    }

    public void setClearColor(float r, float g, float b, float a) {
        this.clearColor.x = r;
        this.clearColor.y = g;
        this.clearColor.z = b;
        this.clearColor.w = a;

        GL11.glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
    }

    public void setClearColor(Vector4f clearColor) {
        setClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
    }
}
