package com.vk.music.view.vkmix.gl

import android.util.Size
import com.vk.music.view.vkmix.gl.textures.GLTextureOutput
import com.vk.music.view.vkmix.gl.textures.GLTextureSource

internal class GLPipeline(
    private val sourcesQueue: List<GLTextureSource>,
    private val output: GLTextureOutput,
    private var viewportSize: Size
) {

    private var sizeWasChanged = true

    fun onViewportSizeChanged(viewportSize: Size) {
        this.viewportSize = viewportSize
        for (source in sourcesQueue) {
            source.onViewportSizeChanged(viewportSize)
        }
        sizeWasChanged = true
    }

    fun draw() {
        if (viewportSize.width == 0 || viewportSize.height == 0) {
            return
        }

        var forceDraw = sizeWasChanged
        var inputTexture = -1

        for (source in sourcesQueue) {
            forceDraw = source.draw(forceDraw, inputTexture)
            inputTexture = source.textureId
        }

        output.draw(inputTexture, viewportSize)

        sizeWasChanged = false
    }

    fun invalidate() {
        sizeWasChanged = true
    }

    fun destroy() {
        for (source in sourcesQueue) {
            source.destroy()
        }
    }
}
