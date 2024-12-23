package com.vk.music.view.vkmix.gl.textures

import android.opengl.GLES20
import android.util.Size
import com.vk.music.view.vkmix.gl.GLShaderId
import com.vk.music.view.vkmix.gl.GLShadersCodeRepository
import com.vk.music.view.vkmix.gl.shaders.SimpleShader

internal class DisplayOutput(
    private val shadersCodeRepository: GLShadersCodeRepository
) : GLTextureOutput {

    private val mainShader by lazy {
        SimpleShader(
            vertexShader = ShaderCreator.createShader(
                GLES20.GL_VERTEX_SHADER,
                shadersCodeRepository.getShaderCode(GLShaderId.Vertex).value
            ),
            fragmentShader = ShaderCreator.createShader(
                GLES20.GL_FRAGMENT_SHADER,
                shadersCodeRepository.getShaderCode(GLShaderId.Main).value
            )
        )
    }

    override fun draw(inputTexture: Int, viewportSize: Size) {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glViewport(0, 0, viewportSize.width, viewportSize.height)

        mainShader.inputTexture = inputTexture
        mainShader.draw(viewportSize)
    }

    class Factory(
        private val shadersCodeRepository: GLShadersCodeRepository
    ) : GLTextureOutput.Factory {
        override fun create() = DisplayOutput(shadersCodeRepository)

    }
}
