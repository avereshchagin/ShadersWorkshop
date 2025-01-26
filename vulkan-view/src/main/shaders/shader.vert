#version 450

// Colour passed to the fragment shader
layout(location = 0) out vec3 fragColor;

// Uniform buffer containing an MVP matrix.
// Currently the vulkan backend only sets the rotation matix
// required to handle device rotation.
layout(binding = 0) uniform UniformBufferObject {
    mat4 MVP;
    vec4 color;
    vec4 bgColor;
    vec2 size;
} ubo;

//vec2 positions[4] = vec2[](
//vec2(-1.0, 1.0),
//vec2(1.0, 1.0),
//vec2(-1.0, -1.0),
//vec2(1.0, -1.0)
//);

vec2 positions[6] = vec2[](
    vec2(-1.0, -1.0),
    vec2(1.0, -1.0),
    vec2(1.0, 1.0),
    vec2(-1.0, -1.0),
    vec2(1.0, 1.0),
    vec2(-1.0, 1.0)
);

void main() {
    gl_Position = ubo.MVP * vec4(positions[gl_VertexIndex], 0.0, 1.0);
    fragColor = vec3(1.0, 1.0, 1.0);
}