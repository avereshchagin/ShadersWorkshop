precision highp float;

uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;
uniform vec2 u_TextureSize;

float M_PI_F = 3.1415927;

void main() {
    vec2 uv = vec2(gl_FragCoord.xy / u_TextureSize.xy);

    float dist = length(uv - 0.5);

    float bd = 8.0;
    float bq = 4.0;
    float br = max(0.0, dist / 30.0);

    vec3 col = texture2D(u_Texture, uv).rgb;

    if (br > 0.0) {
        float twopi = 2.0 * M_PI_F;
        float step = twopi / bd;
        for (float a = 0.0; a < twopi; a += step) {
            for (float i = 1.0 / bq; i <= 1.0; i += 1.0 / bq) {
                vec2 coord = (uv + vec2(cos(a), sin(a)) * br * i);
                col += texture2D(u_Texture , coord).rgb;
            }
        }
        col /= (bq * bd) + 1.0;
    }

    gl_FragColor = vec4(col, 1.0);
}