#version 450

// Colour passed to the fragment shader
layout(location = 0) out vec3 fragColor;

// Uniform buffer containing an MVP matrix.
// Currently the vulkan backend only sets the rotation matix
// required to handle device rotation.
layout(binding = 0) uniform UniformBufferObject {
    mat4 MVP;
    vec2 size;
} ubo;

vec2 positions[6] = vec2[](
    vec2(-1.0, -1.0),
    vec2(1.0, -1.0),
    vec2(1.0, 1.0),
    vec2(-1.0, -1.0),
    vec2(1.0, 1.0),
    vec2(-1.0, 1.0)
);

vec3 colors[6] = vec3[](
    vec3(0.67, 0.1, 0.2),
    vec3(0.67, 0.1, 0.2),
    vec3(0.67, 0.1, 0.2),
    vec3(0.2, 0.1, 0.6),
    vec3(0.2, 0.1, 0.6),
    vec3(0.2, 0.1, 0.6)
);

void main() {
    gl_Position = vec4(positions[gl_VertexIndex], 0.0, 1.0);
    fragColor = vec3(1.0, 1.0, 1.0);
}