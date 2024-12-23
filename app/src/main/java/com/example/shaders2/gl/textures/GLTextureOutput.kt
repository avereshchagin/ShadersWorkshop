package com.vk.music.view.vkmix.gl.textures

import android.util.Size

internal interface GLTextureOutput {

    interface Factory {

        fun create(): GLTextureOutput
    }

    fun draw(inputTexture: Int, viewportSize: Size)
}
