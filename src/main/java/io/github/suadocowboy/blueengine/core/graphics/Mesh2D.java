package io.github.suadocowboy.blueengine.core.graphics;

import io.github.suadocowboy.blueengine.core.shader.ShaderProgram;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

public class Mesh2D {

    private final int vaoId;

    private final int positionsVboId;
    private final int indicesVboId;
    private final int indicesLength;
    private final List<Integer> VBOs;
    private final ShaderProgram shader;

    private final Vector3f position;
    private float scale;
    private final Vector3f rotation;

    public Mesh2D(float[] positions, int[] indices, ShaderProgram shader) {
        indicesLength = indices.length;

        VBOs = new ArrayList<>();

        this.shader = shader;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        positionsVboId = glGenBuffers();
        indicesVboId = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, positionsVboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);

        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);

        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Vector3f(0, 0, 0);
    }

    /**
     * we create a new vertex buffer object for each attribute...
     * @param index attribute index(the first VBO starts with 0 but you should start with index 1 because 0 is already used in the constructor of this class for the position attribute)
     * @param lineLength for example: if it's coordinates: x1,y1,z1, x2,y2,z2 -> the lineLength is 3
     */
    public void createAttribute(int index, int lineLength, float[] value) {
        glBindVertexArray(vaoId);

        int vboId = glGenBuffers();
        VBOs.add(vboId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, value, GL_STATIC_DRAW);
        glVertexAttribPointer(index, lineLength, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(index);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    /**
     * we create a new vertex buffer object for each attribute...
     * @param index attribute index(the first VBO starts with 0 and the second with 1, then 2, 3, 4...)
     * @param lineLength for example: if it's coordinates: x1,y1,z1, x2,y2,z2 -> the lineLength is 3
     * @param unsigned if it's meant to be GL_UNSIGNED_INT or GL_INT
     */
    public void createAttribute(int index, int lineLength, int[] value, boolean unsigned) {
        glBindVertexArray(vaoId);

        int vboId = glGenBuffers();
        VBOs.add(vboId);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);

        glBufferData(GL_ARRAY_BUFFER, value, GL_STATIC_DRAW);
        glVertexAttribPointer(index, lineLength, unsigned? GL_UNSIGNED_INT : GL_INT, false, 0, 0);
        glEnableVertexAttribArray(index);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    public void setPosition(Vector3f pos) {
        this.position.x = pos.x;
        this.position.y = pos.y;
        this.position.z = pos.z;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }

    public void setRotation(Vector3f rot) {
        this.rotation.x = rot.x;
        this.rotation.y = rot.y;
        this.rotation.z = rot.z;
    }

    public void draw() {
        shader.bind();
        glBindVertexArray(vaoId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);

        glDrawElements(GL_TRIANGLES, indicesLength, GL_UNSIGNED_INT, 0);
    }

    public void terminate() {
        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(positionsVboId);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(indicesVboId);

        glDeleteBuffers(VBOs.stream().mapToInt(i->i).toArray());

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}