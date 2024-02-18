package engine.test;

import engine.core.IGameLogic;
import engine.core.Engine;
import engine.core.TickTime;
import engine.core.Window;
import engine.core.util.Utils;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Game implements IGameLogic {
    private final String title = "BlueEngine - Hello World Window";
    private final Window window;
    private long lastSecond;
    int ticks = 0;

    public Game() {
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

        if (window.isKeyPressed(GLFW_KEY_S)) { // it's changing 5 ticks a second
            window.setClearColor(Utils.randomGLColor());
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSecond >= 1000) {
            double ticksPerSecond = (double) ticks / ((double) (currentTime - lastSecond) / 1000.0);
            window.setTitle(title + " - TPS: " + ticks);
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
        TickTime tickTime = new TickTime(5.0); // 5 ticks per second

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

    public static void main(String[] args) {
        Engine.init();

        Game game = new Game();

        game.run();

        game.terminate();
        Engine.terminate();
    }
}
