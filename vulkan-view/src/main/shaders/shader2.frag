#version 450

layout(location = 0) out vec4 outColor;

layout(binding = 0) uniform UniformBufferObject {
    vec2 iResolution;
    float iTime;
} ubo;

void main() {
    vec2 uv = gl_FragCoord.xy / ubo.iResolution;
    outColor = vec4(uv.x, 0.0, 0.0, 1.0);
}
