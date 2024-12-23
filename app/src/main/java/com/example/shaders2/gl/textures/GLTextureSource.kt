package com.vk.music.view.vkmix.gl.textures

import android.opengl.GLES20
import android.util.Size
import com.vk.music.view.vkmix.gl.GLRenderBuffer
import com.vk.music.view.vkmix.gl.GLShaderId
import com.vk.music.view.vkmix.gl.GLShadersCodeRepository

internal abstract class GLTextureSource(
    protected var viewportSize: Size,
    private val shadersCodeRepositoryProvider: () -> GLShadersCodeRepository
) {

    val textureId: Int
        get() = rbo.textureId

    protected var rbo: GLRenderBuffer
    protected val baseVertexShaderHandle by lazy {
        ShaderCreator.createShader(
            GLES20.GL_VERTEX_SHADER,
            shadersCodeRepositoryProvider().getShaderCode(GLShaderId.Vertex).value
        )
    }

    init {
        rbo = createRenderBuffer(viewportSize)
    }

    /**
     * @return true if texture was updated
     */
    abstract fun draw(forceDraw: Boolean, inputTexture: Int): Boolean

    open fun onViewportSizeChanged(viewportSize: Size) {
        this.viewportSize = viewportSize
        destroyRenderBuffer(rbo)
        rbo = createRenderBuffer(viewportSize)
    }

    open fun destroy() = destroyRenderBuffer(rbo)

    private fun createRenderBuffer(viewportSize: Size): GLRenderBuffer {
        val frameBuffer = IntArray(1)
        val textureOut = IntArray(1)
        val renderBuffer = IntArray(1)

        GLES20.glGenFramebuffers(1, frameBuffer, 0)
        GLES20.glGenTextures(1, textureOut, 0)
        GLES20.glGenRenderbuffers(1, renderBuffer, 0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0])

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureOut[0])
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            viewportSize.width,
            viewportSize.height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D,
            textureOut[0],
            0
        )

        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, renderBuffer[0])
        GLES20.glRenderbufferStorage(
            GLES20.GL_RENDERBUFFER,
            GLES20.GL_DEPTH_COMPONENT16,
            viewportSize.width,
            viewportSize.height
        )
        GLES20.glFramebufferRenderbuffer(
            GLES20.GL_FRAMEBUFFER,
            GLES20.GL_DEPTH_ATTACHMENT,
            GLES20.GL_RENDERBUFFER,
            renderBuffer[0]
        )

        return GLRenderBuffer(
            frameBuffer[0],
            renderBuffer[0],
            textureOut[0]
        )
    }

    private fun destroyRenderBuffer(renderBuffer: GLRenderBuffer) {
        GLES20.glDeleteFramebuffers(1, intArrayOf(renderBuffer.frameBufferId), 0)
        GLES20.glDeleteTextures(1, intArrayOf(renderBuffer.textureId), 0)
        GLES20.glDeleteRenderbuffers(1, intArrayOf(renderBuffer.renderBufferId), 0)
    }

    interface Factory {

        fun create(viewportSize: Size): GLTextureSource
    }
}
