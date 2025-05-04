package com.example.shaders2.gl

import android.opengl.GLES20
import android.os.SystemClock
import org.intellij.lang.annotations.Language

class MandelbrotRenderer : AbstractShaderGLRenderer() {

    private val startTimeMs = SystemClock.elapsedRealtime()

    private var resolutionUniform: Int = 0
    private var timeUniform: Int = 0

    @Language("GLSL")
    override val fragmentShader = """
        precision highp float;
        
        uniform vec2 iResolution;
        uniform float iTime;
        
        void main() {
            vec2 p = vec2(-.745,.186) + 3.*(gl_FragCoord.xy/iResolution.y-.5)*pow(.01,1.+cos(.2*iTime));
            //vec2 p = vec2(-1.5, -2.0) + 4.0 * (gl_FragCoord.xy / iResolution.y);
        
            float n = 0.0;
            vec2 z = p * n;
        
            for( ; n < 128.0 && dot(z,z) < 1e4; n++ )
            z = vec2( z.x*z.x - z.y*z.y, 2.*z.x*z.y ) + p;
        
            float sn = n - log2(log2(dot(z,z)));
            gl_FragColor = vec4(.5 + .5*cos( vec3(3,4,11) + .05*sn ), 1.0);
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
