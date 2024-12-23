package com.vk.music.view.vkmix.gl.textures

import android.opengl.GLES20
import android.util.Log
import android.util.Size
import androidx.compose.ui.graphics.Color
import com.vk.music.view.vkmix.gl.GLShaderId
import com.vk.music.view.vkmix.gl.GLShadersCodeRepository
import com.vk.music.view.vkmix.gl.shaders.MusicMixShader

internal interface ColorsProvider {
    val color: Color
    val bgColor: Color
}

internal class MusicMixTextureSource(
    viewportSize: Size,
    shadersCodeRepositoryProvider: () -> GLShadersCodeRepository,
    private val colorsProvider: ColorsProvider,
) : GLTextureSource(
    viewportSize,
    shadersCodeRepositoryProvider
) {

    private val musicShader by lazy {
        val handle = ShaderCreator.createShader(
            GLES20.GL_FRAGMENT_SHADER,
            shadersCodeRepositoryProvider().getShaderCode(GLShaderId.Music).value
        )
        MusicMixShader(baseVertexShaderHandle, handle)
    }

    override fun draw(forceDraw: Boolean, inputTexture: Int): Boolean {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, rbo.frameBufferId)
        GLES20.glViewport(0, 0, viewportSize.width, viewportSize.height)

        var shouldDraw = forceDraw

        if (shouldDraw) {
            musicShader.inputTexture = inputTexture
            musicShader.color = colorsProvider.color
            musicShader.bgColor = colorsProvider.bgColor
            musicShader.draw(viewportSize)
        }

        return shouldDraw
    }

    class Factory(
        private val shadersCodeRepositoryProvider: () -> GLShadersCodeRepository,
        private val colorsProvider: ColorsProvider,
    ) : GLTextureSource.Factory {
        override fun create(viewportSize: Size): GLTextureSource {
            return MusicMixTextureSource(
                viewportSize,
                shadersCodeRepositoryProvider,
                colorsProvider,
            )
        }
    }
}
