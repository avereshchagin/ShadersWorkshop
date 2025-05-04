package com.example.shaders2.gl

import android.opengl.GLES20
import android.os.SystemClock
import org.intellij.lang.annotations.Language

class SpotRenderer : AbstractShaderGLRenderer() {

    private val startTimeMs = SystemClock.elapsedRealtime()

    private var resolutionUniform: Int = 0
    private var timeUniform: Int = 0

    @Language("GLSL")
    override val fragmentShader = """
        precision highp float;
        
        uniform vec2 iResolution;
        uniform float iTime;
        
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
            float stripesCount = (cos(iTime) + 1.0) * 10.0;
            float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
            uv.x += xShift * GLASS_REFRACTION;
            return uv;
        }
        
        void main() {
            vec2 uv = gl_FragCoord.xy / iResolution.xy;
            uv = glass(uv);
            float dist = distance(uv, center);
            vec3 color = pal((cos(iTime / 5.0) + 1.0) / 2.0);
            vec3 col = mix(color, colorBlack, smoothstep(radius / 2.0, radius, dist));
            gl_FragColor = vec4(col, 1.0);
        }
    """.trimIndent()

    override fun initUniforms(programId: Int) {
        resolutionUniform = GLES20.glGetUniformLocation(programId, "iResolution")
        timeUniform = GLES20.glGetUniformLocation(programId, "iTime")
    }

    override fun updateUniforms() {
        GLES20.glUniform2f(resolutionUniform, viewportSize.width.toFloat(), viewportSize.height.toFloat())

        val timeDelta = (SystemClock.elapsedRealtime() - startTimeMs) / 1000.0f
        GLES20.glUniform1f(timeUniform, timeDelta)
    }
}
