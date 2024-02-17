package engine.core;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Engine {
    private Window window;
    private IGameLogic gameLogic;

    Engine() {
        init();
    }

    private void init() {
        System.out.println("LWJGL Version: " + Version.getVersion());

        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    public Window getWindow() {
        return window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setGameLogic(IGameLogic gameLogic) {
        this.gameLogic = gameLogic;

        window.setKeyCallback(gameLogic);
        window.setFramebufferSizeCallback(gameLogic);
    }

    public void run() {
        while (!window.shouldClose()) {
            gameLogic.update();
            gameLogic.draw(window);
            window.clear();
        }
    }

    /**
     * terminate GLFW and free the error callback
     */
    public void terminate() {
        window.terminate();

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}