#version 330

out vec4 fragColor;

in vec3 ourColor;

void main()
{
    fragColor = vec4(ourColor, 1.0);
}