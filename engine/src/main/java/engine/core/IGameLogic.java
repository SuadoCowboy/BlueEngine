package engine.core;

public interface IGameLogic {
    void init();
    void update();
    void draw();
    void run();

    /**
     * not recommended for the same key being pressed every frame as it does not check that way
     */
    void keyCallback(long glfwWindow, int key, int scancode, int action, int mods);

    /**
     * when window changes its size this function is called
     */
    void framebufferSizeCallback(long glfwWindow, int width, int height);

    Window getWindow();
}
