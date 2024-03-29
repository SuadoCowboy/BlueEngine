package io.github.suadocowboy.blueengine.core.util;

import org.joml.Vector4f;
import org.joml.Vector4i;

import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class Utils {
    static public final Random random = new Random();

    static public int randomInteger(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    static public float randomFloat(float min, float max) {
        return random.nextFloat(max - min) + min;
    }

    static public String readFile(String filePath) throws Exception {
        File file = new File(filePath);

        if (!file.isFile())
            throw new Exception("Error reading file: it does not exists or is not a file.");

        StringBuilder data = new StringBuilder();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine())
            data.append(scanner.nextLine()).append('\n');

        if (!data.isEmpty())
            data.deleteCharAt(data.length()-1);

        return data.toString();
    }

    static public Vector4i randomColor() {
        return new Vector4i(randomInteger(0, 255), randomInteger(0, 255), randomInteger(0, 255), 255);
    }

    static public Vector4f randomGLColor() {
        return new Vector4f(randomFloat(0.0f, 1.0f), randomFloat(0.0f, 1.0f), randomFloat(0.0f, 1.0f), 1.0f);
    }

    static public Vector4f rgbaToGLColor(int r, int g, int b, int a) {
        return new Vector4f( (float) r / 255, (float) g / 255, (float) b / 255, (float) a / 255);
    }

    static public Vector4f rgbaToGLColor(Vector4i color) {
        return rgbaToGLColor(color.x, color.y, color.z, color.w);
    }

    static public Vector4i GLColorToRgba(float r, float g, float b, float a) {
        return new Vector4i((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));
    }

    static public Vector4i GLColorToRgba(Vector4f color) {
        return GLColorToRgba(color.x, color.y, color.z, color.w);
    }
}
