package engine.test;

import engine.core.IGameLogic;
import engine.core.Window;
import engine.core.util.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game implements IGameLogic {
    private Window window;
    private final String title = "BlueEngine - Hello World Window";
    private double previousTime;
    private int frameCount;

    @Override
    public void init() {
        window = new Window(350, 300, title, NULL);
        window.setClearColor(Utils.randomGLColor());
        window.setvSync(true);

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
    public void draw(Window window) {

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
}
