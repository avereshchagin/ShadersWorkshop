uniform vec2 iResolution;

float GLASS_REFRACTION = 0.125;

vec2 glass(vec2 uv) {
    float stripesCount = 17.0;
    float xShift = fract(uv.x * stripesCount) - 0.5;
    uv.x += xShift * GLASS_REFRACTION;
    return uv;
}

vec4 mainImage(vec2 fragCoord) {
    vec4 color = vec4(1.0, 0.0, 0.0, 1.0);
    vec4 bgColor = vec4(1.0, 1.0, 1.0, 1.0);

    vec2 uv = fragCoord / iResolution;
    uv = glass(uv);

    vec2 center = vec2(0.5, 0.5);
    float rx = 0.4;
    float ry = 0.4;

    float d = (uv.x - center.x) * (uv.x - center.x) / (rx * rx) +
    (uv.y - center.y) * (uv.y - center.y) / (ry * ry);

    if (d < 0.9) {
        return mix(color, bgColor, d);
    } else {
        return bgColor;
    }
}

void main() {
    gl_FragColor = mainImage(gl_FragCoord.xy);
}
