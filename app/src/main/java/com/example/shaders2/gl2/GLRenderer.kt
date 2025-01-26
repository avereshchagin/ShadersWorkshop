package com.example.shaders2.gl2

import android.content.res.AssetManager
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer(private val assets: AssetManager) : GLSurfaceView.Renderer {

    private var viewportSize = Size(0,0)

    private var positionAttributeLocation: Int = 0

    private val quadVertices by lazy {
        floatArrayOf(
            -1f, 1f,
            1f, 1f,
            -1f, -1f,
            1f, -1f
        )
    }
    private val bytesPerFloat = 4
    private val verticesData by lazy {
        ByteBuffer.allocateDirect(quadVertices.size * bytesPerFloat)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().also {
                it.put(quadVertices)
            }
    }
    private val positionComponentCount = 2

    @Volatile
    private lateinit var shader: GLShaderProgram

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
        GLES20.glEnable(GL10.GL_BLEND)

        shader = GLShaderProgram.createShaderProgram(
            assets,
            "mandelbrot_gl.vert",
            "mandelbrot_gl.frag",
        )

        positionAttributeLocation = GLES20.glGetAttribLocation(shader.programId, "aPosition")

        verticesData.position(0)
        GLES20.glVertexAttribPointer(
            positionAttributeLocation,
            positionComponentCount,
            GLES20.GL_FLOAT,
            false,
            0,
            verticesData
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        viewportSize = Size(width, height)
    }

    override fun onDrawFrame(p0: GL10?) {
        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUseProgram(shader.programId)
        GLES20.glEnableVertexAttribArray(positionAttributeLocation)
        GLES20.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(positionAttributeLocation)
    }
}
