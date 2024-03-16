package io.github.suadocowboy.blueengine;

import io.github.suadocowboy.blueengine.core.IGameLogic;
import io.github.suadocowboy.blueengine.core.Engine;
import io.github.suadocowboy.blueengine.core.TickTime;
import io.github.suadocowboy.blueengine.core.Window;
import io.github.suadocowboy.blueengine.core.util.Utils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TestGame implements IGameLogic {
    private final String title = "BlueEngine - Hello World Window";
    private final Window window;
    private long lastSecond;
    int ticks = 0;
    double tickRate = 20.0;

    public TestGame() {
        window = new Window(this, 400, 300, title, NULL);
        window.setClearColor(Utils.randomGLColor());
        window.setvSync(false);

        lastSecond = System.currentTimeMillis();
    }

    @Override
    public void keyCallback(int key, int scancode, int action, int mods) {

    }

    @Override
    public void update() {
        if (window.isKeyPressed(GLFW_KEY_W)) { // it's changing alot of times in only 1 second, but the changes only appear after 5 ticks a second.
            window.setClearColor(Utils.randomGLColor());
        }

    }

    @Override
    public void updateInTick() {
        ticks++;

        if (window.isKeyPressed(GLFW_KEY_S)) {
            window.setClearColor(Utils.randomGLColor());
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSecond >= 1000) {
            int ticksPerSecond = (int) ((double) ticks / ((double) (currentTime - lastSecond) / 1000.0));
            window.setTitle(title + " - TPS: " + ticksPerSecond);
            ticks = 0;
            lastSecond = currentTime;
        }
    }

    @Override
    public void draw() {
        window.clear();
    }

    @Override
    public void run() {
        TickTime tickTime = new TickTime(tickRate); // 5 ticks per second

        while (!window.shouldClose()) {
            update();
            if (tickTime.shouldTick()) {
                updateInTick();
                draw();
            }
        }
    }

    @Override
    public void framebufferSizeCallback() {

    }

    @Override
    public void terminate() {
        window.terminate();
    }

    @BeforeAll
    public static void init() {
        Engine.init();
    }

    @Test
    public void testGame() {
        run();
        terminate();
    }

    @AfterAll
    public static void terminateEngine() {
        Engine.terminate();
    }
}
