package io.github.avereshchagin.shaders.gl

import android.opengl.GLES20
import android.os.SystemClock
import androidx.compose.ui.geometry.Offset
import org.intellij.lang.annotations.Language

class MandelbrotRenderer : AbstractShaderGLRenderer() {

    var scale: Float = 1f
    var center: Offset = Offset(0.5f, 0.5f)

    private val startTimeMs = SystemClock.elapsedRealtime()

    private var resolutionUniform: Int = 0
    private var timeUniform: Int = 0
    private var centerUniform: Int = 0
    private var scaleUniform: Int = 0

    @Language("GLSL")
    override val fragmentShader = """
        precision highp float;

        uniform vec2 iResolution;
        uniform float iTime;
        uniform vec2 iCenter;
        uniform float iScale;

        void main() {
//            vec2 p = vec2(-.745,.186) + 3.*(gl_FragCoord.xy/iResolution.y-.5) * scale;
//            vec2 p = vec2(-.745,.186) + 3.*(gl_FragCoord.xy/iResolution.y-.5)*pow(.01,1.+cos(.2*iTime));

            vec2 p = 3.0 * ((gl_FragCoord.xy - 0.5 * iResolution.xy - iCenter * iResolution.xy) / iResolution.y) * iScale;

//            vec2 uv = (gl_FragCoord.xy) / iResolution.y - 0.5;
//            vec2 origin = vec2(0.5) + iCenter;
//            vec2 p = 3.0 * iScale * (uv - origin) + origin;

            float n = 0.0;
            vec2 z = vec2(0.0);
            
//            if (gl_FragCoord.x < 1.0 && gl_FragCoord.y < 1.0) {
//                for (; n < 128.0; n++) {
//                    z = vec2( z.x*z.x - z.y*z.y, 2.*z.x*z.y ) + p;
//                }
//            }
            
            n = 0.0;
            z = vec2(0.0);
            
            if (mod(gl_FragCoord.x, 2.) < 1.) {
                for (; n < 128.0 && dot(z,z) < 1e4; n++) {
                    z = vec2( z.x*z.x - z.y*z.y, 2.*z.x*z.y ) + p;
                }
            } else {
                while (n < 128.0) {
                    if (z.x * z.x + z.y * z.y > 1e4) {
                        break;
                    }
                    z = vec2( z.x*z.x - z.y*z.y, 2.*z.x*z.y ) + p;
                    n++;
                }
            }

            float sn = n - log2(log2(dot(z,z)));
            gl_FragColor = vec4(.5 + .5*cos( vec3(3,4,11) + .05*sn ), 1.0);

            vec2 resolution = iResolution.xy;
        }
    """.trimIndent()

    override fun initUniforms(programId: Int) {
        resolutionUniform = GLES20.glGetUniformLocation(programId, "iResolution")
        timeUniform = GLES20.glGetUniformLocation(programId, "iTime")
        centerUniform = GLES20.glGetUniformLocation(programId, "iCenter")
        scaleUniform = GLES20.glGetUniformLocation(programId, "iScale")
    }

    override fun updateUniforms() {
        GLES20.glUniform2f(resolutionUniform, viewportSize.width.toFloat(), viewportSize.height.toFloat())

        val timeDelta = (SystemClock.elapsedRealtime() - startTimeMs) / 1000.0f
        GLES20.glUniform1f(timeUniform, timeDelta)

        GLES20.glUniform2f(centerUniform, center.x, center.y)
        GLES20.glUniform1f(scaleUniform, scale)
    }
}
