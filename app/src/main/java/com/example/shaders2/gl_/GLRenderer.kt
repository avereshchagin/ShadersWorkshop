package com.example.shaders2.gl_

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer : GLSurfaceView.Renderer {

    @Volatile
    private var viewportSize = Size(0,0)

    private var source: MenuTextureSource? = null
    private var sizeWasChanged = true

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1.0f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        viewportSize = Size(width, height)
//        source?.onViewportSizeChanged(viewportSize)
        sizeWasChanged = true
        createMainPipelineIfNeeded()
    }

    override fun onDrawFrame(p0: GL10?) {
        if (viewportSize.width == 0 || viewportSize.height == 0) {
            return
        }

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT or GLES20.GL_COLOR_BUFFER_BIT)

        createMainPipelineIfNeeded()

        if (viewportSize.width == 0 || viewportSize.height == 0) {
            return
        }

        var forceDraw = sizeWasChanged

        source?.draw(-1, viewportSize)

        sizeWasChanged = false
    }

    private fun createMainPipelineIfNeeded() {
        if (source == null) {
            source = MenuTextureSource(viewportSize)
        }
    }
}
