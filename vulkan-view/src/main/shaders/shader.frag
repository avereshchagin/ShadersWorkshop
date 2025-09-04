#version 450

layout(location = 0) in vec3 fragColor;

layout(location = 0) out vec4 outColor;

layout(binding = 0) uniform UniformBufferObject {
    mat4 MVP;
    vec4 color;
    vec4 bgColor;
    vec2 size;
    float iTime;
} ubo;

vec3 colorBlack = vec3(0.0, 0.0, 0.0);
vec2 center = vec2(0.5, 0.5);
float radius = 0.4;

float GLASS_REFRACTION = 0.125;

vec3 pal(float t) {
    vec3 a = vec3(0.5,0.5,0.5);
    vec3 b = vec3(0.5,0.5,0.5);
    vec3 c = vec3(1.0,1.0,1.0);
    vec3 d = vec3(0.0,0.33,0.67);
    return a + b*cos(6.28318*(c*t+d));
}

vec2 glass(vec2 uv) {
    float stripesCount = (cos(ubo.iTime) + 1.0) * 10.0;
    float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
    uv.x += xShift * GLASS_REFRACTION;
    return uv;
}

void main() {
    vec2 uv = gl_FragCoord.xy / ubo.size.xy;
    uv = glass(uv);
    float dist = distance(uv, center);
    vec3 color = pal((cos(ubo.iTime / 5.0) + 1.0) / 2.0);
    outColor = mix(vec4(color, 1.0), ubo.bgColor, smoothstep(radius / 2.0, radius, dist));
}
