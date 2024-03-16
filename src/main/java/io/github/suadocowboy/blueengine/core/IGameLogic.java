package io.github.suadocowboy.blueengine.core;

public interface IGameLogic {
    void update();

    /**
     * should only be updated using tickRate logic
     */
    void updateInTick();
    void draw();
    void run();
    void terminate();

    /**
     * not recommended for the same key being pressed every frame as it does not check that way
     */
    void keyCallback(int key, int scancode, int action, int mods);

    /**
     * when window changes its size this function is called
     */
    void framebufferSizeCallback();
}
