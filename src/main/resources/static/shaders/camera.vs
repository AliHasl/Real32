#version 300 es
layout (location = 0) in vec3 aVertexPosition;
layout (location = 1) in vec3 aNormals;

out vec3 FragPos;
out vec3 Normals;

uniform mat4 uModelViewMatrix;
uniform mat4 uProjectionMatrix;
void main(void)
{
    gl_Position = uProjectionMatrix * uModelViewMatrix * vec4(aVertexPosition, 1.0f);
    FragPos = vec3(vec4(aVertexPosition, 1.0f) * uModelViewMatrix);
    Normals = mat3(transpose(inverse(uModelViewMatrix))) * aNormals;
}