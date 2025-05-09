#version 450

// Input colour coming from the vertex shader
layout(location = 0) in vec3 fragColor;

// Output colour for the fragment
layout(location = 0) out vec4 outColor;

layout(binding = 0) uniform UniformBufferObject {
    mat4 MVP;
    vec4 color;
    vec4 bgColor;
    vec2 size;
} ubo;

float GLASS_REFRACTION = 0.125;

vec2 glass(vec2 uv) {
    float stripesCount = 17.0;
    float xShift = fract(uv.x * stripesCount) - 0.5;
    uv.x += xShift * GLASS_REFRACTION;
    return uv;
}

vec4 mainImage(vec2 fragCoord) {
    vec2 uv = fragCoord / ubo.size;
    uv = glass(uv);

    vec2 center = vec2(0.5, 0.5);
    float rx = 0.4;
    float ry = 0.4;

    float d = (uv.x - center.x) * (uv.x - center.x) / (rx * rx) +
    (uv.y - center.y) * (uv.y - center.y) / (ry * ry);

    if (d < 0.9) {
        return mix(ubo.color, ubo.bgColor, d);
    } else {
        return ubo.bgColor;
    }
}

void main() {
    outColor = mainImage(gl_FragCoord.xy);
}
