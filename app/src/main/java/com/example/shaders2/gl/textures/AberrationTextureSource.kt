package com.vk.music.view.vkmix.gl.textures

import android.opengl.GLES20
import android.util.Size
import com.vk.music.view.vkmix.gl.GLShaderId
import com.vk.music.view.vkmix.gl.GLShadersCodeRepository
import com.vk.music.view.vkmix.gl.shaders.BlurShader

internal class AberrationTextureSource(
    viewportSize: Size,
    shadersCodeRepositoryProvider: () -> GLShadersCodeRepository,
) : GLTextureSource(
    viewportSize,
    shadersCodeRepositoryProvider
) {

    class Factory(
        private val shadersCodeRepositoryProvider: () -> GLShadersCodeRepository
    ) : GLTextureSource.Factory {
        override fun create(viewportSize: Size) = AberrationTextureSource(
            viewportSize,
            shadersCodeRepositoryProvider
        )
    }

    private val aberrationShader by lazy {
        val handle = ShaderCreator.createShader(
            GLES20.GL_FRAGMENT_SHADER,
            shadersCodeRepositoryProvider().getShaderCode(GLShaderId.Aberration).value
        )
        BlurShader(baseVertexShaderHandle, handle)
    }

    override fun draw(forceDraw: Boolean, inputTexture: Int): Boolean {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, rbo.frameBufferId)
        GLES20.glViewport(0, 0, viewportSize.width, viewportSize.height)

        if (forceDraw) {
            aberrationShader.inputTexture = inputTexture
            aberrationShader.draw(viewportSize)
        }

        return forceDraw
    }
}
