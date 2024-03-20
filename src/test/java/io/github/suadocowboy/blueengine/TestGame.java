package io.github.suadocowboy.blueengine;

import io.github.suadocowboy.blueengine.core.Engine;
import io.github.suadocowboy.blueengine.core.IGameLogic;
import io.github.suadocowboy.blueengine.core.TickTimer;
import io.github.suadocowboy.blueengine.core.Window;
import io.github.suadocowboy.blueengine.core.graphics.Mesh2D;
import io.github.suadocowboy.blueengine.core.shader.ShaderProgram;
import io.github.suadocowboy.blueengine.core.util.Utils;
import org.joml.Vector4f;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TestGame implements IGameLogic {
    private final String title = "BlueEngine - Our First Triangle!";
    private final Window window;
    private long lastSecond;
    private final ShaderProgram shaderProgram;
    private final Mesh2D triangle;
    int ticks = 0;
    double tickRate = 20.0;

    public TestGame() throws Exception {
        window = new Window(this, 400, 300, title, NULL);
        window.setClearColor(new Vector4f(0.2f, 0.5f, 0.7f, 1.0f));
        window.setvSync(false);

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.readFile(getClass().getClassLoader().getResource("shaders/vertex.vert").getPath()));
        shaderProgram.createFragmentShader(Utils.readFile(getClass().getClassLoader().getResource("shaders/fragment.frag").getPath()));
        shaderProgram.link();
        shaderProgram.bind();

        float[] positions = new float[]{
                -0.5f,  0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f,  0.5f, 0.0f,
        };
        int[] indices = new int[]{
                0, 1, 3,
                3, 1, 2,
        };

        triangle = new Mesh2D(positions, indices, shaderProgram);

        lastSecond = System.currentTimeMillis();
    }

    @Override
    public void keyCallback(int key, int scancode, int action, int mods) {

    }

    @Override
    public void update() {
        if (window.isKeyPressed(GLFW_KEY_W)) // it's changing alot of times in only 1 second, but the changes only appear after x ticks a second.
            window.setClearColor(Utils.randomGLColor());
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

        triangle.draw();

        window.update();
    }

    @Override
    public void run() {
        TickTimer tickTimer = new TickTimer(tickRate);

        while (!window.shouldClose()) {
            update();
            if (tickTimer.shouldTick()) {
                updateInTick();
            }

            draw();
        }
    }

    @Override
    public void framebufferSizeCallback() {

    }

    @Override
    public void terminate() {
        shaderProgram.terminate();
        triangle.terminate();
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
