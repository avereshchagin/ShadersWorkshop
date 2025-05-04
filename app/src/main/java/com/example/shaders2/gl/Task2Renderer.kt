package com.example.shaders2.gl

import org.intellij.lang.annotations.Language

class Task2Renderer : AbstractShaderGLRenderer() {

    @Language("GLSL")
    override val fragmentShader = """
        void main() {
            gl_FragColor = vec4(vec3(0.0), 1.0);
        }
    """.trimIndent()

    override fun initUniforms(programId: Int) {
    }

    override fun updateUniforms() {
    }
}
