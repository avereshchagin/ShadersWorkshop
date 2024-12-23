vec3 colorBlack = vec3(0.0, 0.0, 0.0);
vec3 colorRed = vec3(1.0, 0.0, 0.0);
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
    //float stripesCount = 17.0;
    float stripesCount = (cos(iTime) + 1.0) * 10.0;// (iTime - 1000.0) / 5.0;
    float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
    uv.x += xShift * GLASS_REFRACTION;// + 0.5;
    return uv;
}

void mainImage( out vec4 fragColor, in vec2 fragCoord )
{
    vec2 uv = fragCoord / iResolution.xy;

    uv = glass(uv);

    float dist = length(uv - center);

    vec3 color = pal((cos(iTime / 5.0) + 1.0) / 2.0);

    //vec3 col = mix(color, colorBlack, dist);

    //vec3 col = mix(color, colorBlack, step(radius, dist));

    vec3 col = mix(color, colorBlack, smoothstep(radius / 2.0, radius, dist));

    fragColor = vec4(col, 1.0);
}