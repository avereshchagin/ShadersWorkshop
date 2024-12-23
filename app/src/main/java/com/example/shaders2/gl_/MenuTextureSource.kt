package com.example.shaders2.gl_

import android.opengl.GLES20
import android.util.Size

/*
    uniform vec2 u_TextureSize;

    float GLASS_REFRACTION = 0.125;
    vec3 BACKGROUND_COLOR = vec3(0.0, 0.0, 0.0);

    vec2 glass(vec2 uv) {
        float stripesCount = 17.0;
        float xShift = fract(uv.x * stripesCount) - 0.5;
        uv.x += xShift * GLASS_REFRACTION;
        return uv;
    }

    vec3 mainImage(vec2 fragCoord) {
        vec2 uv = fragCoord / u_TextureSize.xy;
        uv = glass(uv);

        vec2 center = vec2(0.5, 0.5);
        float rx = 0.4;
        float ry = 0.4;

        float d = (uv.x - center.x) * (uv.x - center.x) / (rx * rx) +
            (uv.y - center.y) * (uv.y - center.y) / (ry * ry);

        vec3 col = vec3(0.5, 0.0, 0.0);// 0.5 + 0.5 * cos(iTime + vec3(0,2,4));

        return mix(col, BACKGROUND_COLOR, d);
    }
 */
private val glShaderSrc = """
    void main() {
        vec3 col = vec3(0.5, 0.0, 0.0);// mainImage(gl_FragCoord.xy);
        gl_FragColor = vec4(col, 1.0);
    }
""".trimIndent()

private val vertexShaderSrc = """
    void main() {
    }
""".trimIndent()


class MenuTextureSource(
    viewportSize: Size,
) {

    private val mainShader by lazy {
        SimpleShader(
            vertexShader = ShaderCreator.createShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderSrc,
            ),
            fragmentShader = ShaderCreator.createShader(
                GLES20.GL_FRAGMENT_SHADER,
                glShaderSrc,
            )
        )
    }

    fun draw(inputTexture: Int, viewportSize: Size) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glViewport(0, 0, viewportSize.width, viewportSize.height)

//        mainShader.inputTexture = inputTexture
        mainShader.draw(viewportSize)
    }

//    private val shader by lazy {
//        val handle = ShaderCreator.createShader(
//            GLES20.GL_FRAGMENT_SHADER,
//            glShaderSrc,
//        )
//        MenuShader(baseVertexShaderHandle, handle)
//    }
//
//    override fun draw(forceDraw: Boolean, inputTexture: Int): Boolean {
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, rbo.frameBufferId)
//        GLES20.glViewport(0, 0, viewportSize.width, viewportSize.height)
//
//        val shouldDraw = forceDraw
//
////        if (shouldDraw) {
////            shader.inputTexture = inputTexture
//            shader.draw(viewportSize)
////        }
//
//        return shouldDraw
//    }
}
