package com.example.shaders2.gl2

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import com.example.shaders2.gl.GLTextureView

@SuppressLint("ViewConstructor")
class ShaderGlSurfaceView(
    context: Context,
    renderer: Renderer,
) : GLSurfaceView(
    context, null
) {

    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        preserveEGLContextOnPause = true

        setRenderer(renderer)
    }

    override fun setRenderer(renderer: Renderer) {
        super.setRenderer(renderer)
//        renderMode = GLTextureView.RENDERMODE_WHEN_DIRTY
    }

    override fun onResume() {
        super.onResume()
        requestRender()
    }
}
