package com.example.shaders2.gl

import android.content.res.AssetManager
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.SystemClock
import android.util.Log
import android.util.Size
import androidx.compose.ui.graphics.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLRenderer(
    private val assets: AssetManager,
    private val pathToFrag: String,
) : GLSurfaceView.Renderer {

    private var viewportSize = Size(0,0)

    private var positionAttribute: Int = 0
    private var resolutionUniform: Int = 0
    private var timeUniform: Int = 0

    private val startTimeMs = SystemClock.elapsedRealtime()

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

    fun setValue(name: String, color: Color) {

    }

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

        positionAttribute = GLES20.glGetAttribLocation(shader.programId, "aPosition")
        resolutionUniform = GLES20.glGetUniformLocation(shader.programId, "iResolution")
        timeUniform = GLES20.glGetUniformLocation(shader.programId, "iTime")

        verticesData.position(0)
        GLES20.glVertexAttribPointer(
            positionAttribute,
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
        val start = SystemClock.elapsedRealtime()

        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUseProgram(shader.programId)

        GLES20.glUniform2f(resolutionUniform, viewportSize.width.toFloat(), viewportSize.height.toFloat())

        val timeDelta = (SystemClock.elapsedRealtime() - startTimeMs) / 1000.0f
        GLES20.glUniform1f(timeUniform, timeDelta)

//        Log.i("GL", "draw $timeDelta ${Thread.currentThread()}")

        GLES20.glEnableVertexAttribArray(positionAttribute)
        GLES20.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4)
        GLES20.glDisableVertexAttribArray(positionAttribute)

        Log.i("OpenGL", "Draw time ${SystemClock.elapsedRealtime() - start} ms")
    }
}
