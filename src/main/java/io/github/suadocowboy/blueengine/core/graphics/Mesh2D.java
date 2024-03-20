package io.github.suadocowboy.blueengine.core.graphics;

import io.github.suadocowboy.blueengine.core.shader.ShaderProgram;

import static org.lwjgl.opengl.GL30.*;

public class Mesh2D {

    private final int vaoId;

    private final int vboId;
    private final int indicesVboId;
    private final int indicesLength;
    private final ShaderProgram shader;

    public Mesh2D(float[] positions, int[] indices, ShaderProgram shader) {
        indicesLength = indices.length;

        this.shader = shader;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        indicesVboId = glGenBuffers();

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indicesVboId);

        glBufferData(GL_ARRAY_BUFFER, positions, GL_STATIC_DRAW);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // position attribute
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);

        glBindVertexArray(0);
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
        glDeleteBuffers(vboId);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glDeleteBuffers(indicesVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}