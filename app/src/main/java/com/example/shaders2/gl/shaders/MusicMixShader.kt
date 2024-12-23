package com.vk.music.view.vkmix.gl.shaders

import android.opengl.GLES20
import android.util.Size
import androidx.compose.ui.graphics.Color

internal class MusicMixShader(
    vertexShader: Int,
    fragmentShader: Int
) : SimpleShader(vertexShader, fragmentShader) {

    private val textureSizeHandle = GLES20.glGetUniformLocation(mProgram, "u_TextureSize")
    private val colorHandle = GLES20.glGetUniformLocation(mProgram, "u_Color")
    private val bgColorHandle = GLES20.glGetUniformLocation(mProgram, "u_BgColor")

    var color: Color = Color.Black
    var bgColor: Color = Color.Black

    override fun bindExtraHandles(viewportSize: Size) {
        GLES20.glUniform2f(
            textureSizeHandle,
            viewportSize.width.toFloat(),
            viewportSize.height.toFloat(),
        )

        GLES20.glUniform3f(
            colorHandle,
            color.red,
            color.green,
            color.blue,
        )

        GLES20.glUniform3f(
            bgColorHandle,
            bgColor.red,
            bgColor.green,
            bgColor.blue,
        )
    }
}
