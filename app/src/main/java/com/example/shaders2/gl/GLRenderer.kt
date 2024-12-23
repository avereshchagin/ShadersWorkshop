package com.vk.music.view.vkmix.gl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import com.vk.music.view.vkmix.gl.textures.GLTextureOutput
import com.vk.music.view.vkmix.gl.textures.GLTextureSource
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {

    @Volatile
    private var viewportSize = Size(0,0)

    private var sourceFactories: List<GLTextureSource.Factory>? = null
    private var outputFactory: GLTextureOutput.Factory? = null
    private var mainPipeline: GLPipeline? = null

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        viewportSize = Size(width, height)
        mainPipeline?.onViewportSizeChanged(viewportSize)
        createMainPipelineIfNeeded()
    }

    override fun onDrawFrame(p0: GL10?) {
        if (viewportSize.width == 0 || viewportSize.height == 0) {
            return
        }

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        createMainPipelineIfNeeded()

        mainPipeline?.draw()
    }

    internal fun setPipelineQueue(sourceFactories: List<GLTextureSource.Factory>) {
        this.sourceFactories = sourceFactories
        sourceFactories.map { it.create(viewportSize) }
    }

    internal fun setOutput(outputFactory: GLTextureOutput.Factory) {
        this.outputFactory = outputFactory
    }

    fun invalidate() {
        mainPipeline?.invalidate()
    }

    private fun createMainPipelineIfNeeded() {
        if (mainPipeline == null) {
            sourceFactories?.let {
                mainPipeline = GLPipeline(
                    sourcesQueue = it.map {
                        it.create(viewportSize)
                    },
                    output = outputFactory!!.create(),
                    viewportSize = viewportSize
                )
            }
        }
    }
}
