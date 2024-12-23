package com.vk.music.view.vkmix.gl.shaders

import android.opengl.GLES20
import android.util.Size

internal class AberrationShader (
    vertexShader: Int,
    fragmentShader: Int
) : SimpleShader(vertexShader, fragmentShader) {

    private val textureSizeHandle = GLES20.glGetUniformLocation(mProgram, "u_TextureSize")

    override fun bindExtraHandles(viewportSize: Size) {
        GLES20.glUniform2f(textureSizeHandle, viewportSize.width.toFloat(), viewportSize.height.toFloat())
    }
}
