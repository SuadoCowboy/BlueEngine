package io.github.suadocowboy.blueengine.core;

public interface IGameLogic {
    void update();

    /**
     * should only be updated using tick rate logic
     */
    void updateInTick();
    void draw();
    void run();
    void terminate();

    /**
     * should be used for when key is pressed ONCE and not if it's still being held every frame
     */
    void keyCallback(int key, int scancode, int action, int mods);

    /**
     * when window changes its size this function is called
     */
    void framebufferSizeCallback();
}
