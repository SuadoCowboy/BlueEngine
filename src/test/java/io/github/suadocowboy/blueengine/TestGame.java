package io.github.suadocowboy.blueengine;

import io.github.suadocowboy.blueengine.core.*;
import io.github.suadocowboy.blueengine.core.graphics.Mesh2D;
import io.github.suadocowboy.blueengine.core.shader.ShaderProgram;
import io.github.suadocowboy.blueengine.core.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class TestGame implements IGameLogic {
    private final String title = "BlueEngine - Our First Triangle!";
    private final Window window;
    private long lastSecond;
    private final ShaderProgram shaderProgram;
    private int ticks = 0;
    private final double tickRate = 20.0;

    private final List<Entity> entities;
    private int changeX, changeY, changeZ = 0;

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

        float[] positions = new float[]{
                -0.5f,  0.5f, -1.5f,
                -0.5f, -0.5f, -1.5f,
                 0.5f, -0.5f, -1.5f,
                 0.5f,  0.5f, -1.5f,
        };

        int[] indices = new int[]{
                0, 1, 3,
                3, 1, 2,
        };

        Mesh2D rectangle = new Mesh2D(positions, indices, shaderProgram);

        float[] colors = new float[]{
                0.5f, 0.0f, 0.0f,
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.5f,
        };

        rectangle.createAttribute(1, 3, colors);

        entities = new ArrayList<>();
        entities.add(new Entity(rectangle));

        lastSecond = System.currentTimeMillis();
    }

    @Override
    public void keyCallback(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_X && action == GLFW_PRESS) {
            changeX = changeX == 1? 0 : 1;
            System.out.println("X: " + changeX);
        }

        if (key == GLFW_KEY_Y && action == GLFW_PRESS) {
            changeY = changeY == 1? 0 : 1;
            System.out.println("Y: " + changeY);
        }

        if (key == GLFW_KEY_Z && action == GLFW_PRESS) {
            changeZ = changeZ == 1? 0 : 1;
            System.out.println("Z: " + changeZ);
        }
    }

    @Override
    public void update() {
    }

    @Override
    public void updateInTick() {
        ticks++;

        Entity entity = entities.get(0);

        Vector3f position = entity.getPosition();
        Vector3f rotation = entity.getRotation();

        if (window.isKeyPressed(GLFW_KEY_W)) {
            entity.setPosition(position.x + changeX * 0.1f, position.y + changeY * 0.1f, position.z + changeZ * 0.1f);
        }

        if (window.isKeyPressed(GLFW_KEY_S)) {
            entity.setPosition(position.x - changeX * 0.1f, position.y - changeY * 0.1f, position.z - changeZ * 0.1f);
        }

        if (window.isKeyPressed(GLFW_KEY_Q))
            entity.setScale(entity.getScale()-0.5f);

        if (window.isKeyPressed(GLFW_KEY_E))
            entity.setScale(entity.getScale()+0.5f);

        if (window.isKeyPressed(GLFW_KEY_A)) {
            entity.setRotation(rotation.x - changeX, rotation.y - changeY, rotation.z - changeZ);
        }

        if (window.isKeyPressed(GLFW_KEY_D)) {
            entity.setRotation(rotation.x + changeX, rotation.y + changeY, rotation.z + changeZ);
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

        for (Entity entity : entities) {
            Matrix4f worldMatrix = window.transformation.getWorldMatrix(
                    entity.getPosition(),
                    entity.getRotation(),
                    entity.getScale()
            );

            shaderProgram.setUniform("worldMatrix", worldMatrix);
            entity.getMesh().draw();
        }

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

        for (Entity entity : entities) {
            entity.getMesh().terminate();
        }

        entities.clear();

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
