package io.github.suadocowboy.blueengine;

import io.github.suadocowboy.blueengine.core.*;
import io.github.suadocowboy.blueengine.core.graphics.Mesh;
import io.github.suadocowboy.blueengine.core.shader.ShaderProgram;
import io.github.suadocowboy.blueengine.core.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TestGame implements IGameLogic {
    private final String title = "BlueEngine - Our First Colored Cube!";
    private final Window window;
    private long lastSecond;
    private final ShaderProgram shaderProgram;
    private int ticks = 0;
    private final double tickRate = 20.0;
    private final Entity entity;
    private float scaleIncremental = 1;

    public TestGame() throws Exception {
        window = new Window(this, 400, 300, title, NULL);
        window.setClearColor(new Vector4f(0.2f, 0.5f, 0.7f, 1.0f));
        window.setvSync(false);

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(Utils.readFile(getClass().getClassLoader().getResource("shaders/vertex.vert").getPath()));
        shaderProgram.createFragmentShader(Utils.readFile(getClass().getClassLoader().getResource("shaders/fragment.frag").getPath()));
        shaderProgram.link();
        shaderProgram.bind();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("worldMatrix");

        entity = createCubeEntity();

        lastSecond = System.currentTimeMillis();
    }

    private Entity createCubeEntity() {
        float[] positions = new float[] {
                // VO
                -0.5f,  0.5f,  0.5f,
                // V1
                -0.5f, -0.5f,  0.5f,
                // V2
                0.5f, -0.5f,  0.5f,
                // V3
                0.5f,  0.5f,  0.5f,
                // V4
                -0.5f,  0.5f, -0.5f,
                // V5
                0.5f,  0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
        };

        int[] indices = new int[] {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                4, 0, 3, 5, 4, 3,
                // Right face
                3, 2, 7, 5, 3, 7,
                // Left face
                6, 1, 0, 6, 0, 4,
                // Bottom face
                2, 1, 6, 2, 6, 7,
                // Back face
                7, 6, 4, 7, 4, 5,
        };

        Mesh rectangle = new Mesh(positions, indices, shaderProgram);

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        rectangle.createAttribute(1, 3, colors);
        return new Entity(rectangle);
    }

    @Override
    public void keyCallback(int key, int scancode, int action, int mods) {
    }

    @Override
    public void update() {
    }

    @Override
    public void updateInTick() {
        ticks++;

        float rotation = entity.getRotation().x + 1.5f;
        if (rotation > 360)
            rotation = 0;

        entity.setRotation(rotation, rotation, rotation);

        float scale = entity.getScale();
        if (scale > 2.00f || scale < 0.05f)
            scaleIncremental *= -1;

        entity.setScale(scale+0.05f*scaleIncremental);

        Vector3f position = entity.getPosition();
        if (window.isKeyPressed(GLFW_KEY_W))
            entity.setPosition(position.x, position.y, position.z+0.1f);
        if (window.isKeyPressed(GLFW_KEY_S))
            entity.setPosition(position.x, position.y, position.z-0.1f);

        if (window.isKeyPressed(GLFW_KEY_R)) {
            entity.setScale(1);
            entity.setPosition(0, 0, 0);
            entity.setRotation(0, 0, 0);
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

        shaderProgram.setUniform("projectionMatrix", window.getProjectionMatrix());

        Matrix4f worldMatrix = window.transformation.getWorldMatrix(
                entity.getPosition(),
                entity.getRotation(),
                entity.getScale()
        );

        shaderProgram.setUniform("worldMatrix", worldMatrix);
        entity.getMesh().draw();

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
        entity.getMesh().terminate();
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
