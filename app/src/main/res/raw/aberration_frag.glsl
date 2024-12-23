precision highp float;

uniform sampler2D u_Texture;
varying vec2 v_TexCoordinate;
uniform vec2 u_TextureSize;

void main() {
    vec2 uv = vec2(gl_FragCoord.xy / u_TextureSize.xy);

    float yshift = uv.y - 0.5;
    yshift *= yshift;
    yshift = min(0.05, yshift);
    float xshift = (uv.x - 0.5) * yshift;
    uv.x -= xshift;
    float abShift = xshift * 0.5;
    vec2 uvr = uv;
    uvr.x -= abShift;
    vec2 uvb = uv;
    uvb.x += abShift;

    vec3 col = vec3(
        texture2D(u_Texture, uvr).x,
        texture2D(u_Texture, uv).y,
        texture2D(u_Texture, uvb).z
    );

    gl_FragColor = vec4(col, 1.0);
}