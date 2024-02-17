package engine.test;

import engine.core.IGameLogic;
import engine.core.Launcher;
import engine.core.Window;
import engine.core.util.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game implements IGameLogic {
    private Window window;
    private final String title = "BlueEngine - Hello World Window";
    private double previousTime;
    private int frameCount;

    public void init() {
        window = new Window(350, 300, title, NULL);
        window.setClearColor(Utils.randomGLColor());
        window.setvSync(false);

        window.setKeyCallback(this);
        window.setFramebufferSizeCallback(this);

        previousTime = glfwGetTime();
    }

    @Override
    public void keyCallback(long glfwWindow, int key, int scancode, int action, int mods) {
        if (action == GLFW_RELEASE) {
            if (key == GLFW_KEY_ESCAPE)
                window.setShouldClose(true);

            else if (key == GLFW_KEY_W) {
                window.setClearColor(Utils.randomGLColor());
            }
        }
    }

    @Override
    public void update() {
        double currentTime = glfwGetTime();
        frameCount++;

        if (currentTime - previousTime >= 1.0) {
            window.setTitle(title + " - FPS: " + frameCount);
            frameCount = 0;
            previousTime = currentTime;
        }
    }

    @Override
    public void draw() {
        window.clear();
    }

    @Override
    public void run() {
        while (!window.shouldClose()) {
            update();
            draw();
        }
    }

    @Override
    public void framebufferSizeCallback(long glfwWindow, int width, int height) {
        window.updateViewport();
        window.clear();
    }

    @Override
    public Window getWindow() {
        return window;
    }

    public static void main(String[] args) {
        Launcher.run(new Game());
    }
}
