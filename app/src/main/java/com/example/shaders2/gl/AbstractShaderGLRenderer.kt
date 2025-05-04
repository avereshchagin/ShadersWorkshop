package com.example.shaders2.gl

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.SystemClock
import android.util.Log
import android.util.Size
import org.intellij.lang.annotations.Language
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

abstract class AbstractShaderGLRenderer : GLSurfaceView.Renderer {

    protected var viewportSize = Size(0,0)

    private var positionAttribute: Int = 0

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

    @Language("GLSL")
    private val vertexShader = """
        attribute vec4 aPosition;
        
        void main() {
            gl_Position = aPosition;
        }
    """.trimIndent()

    private var programId: Int = 0

    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        GLES20.glDisable(GL10.GL_DITHER)
        GLES20.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)
        GLES20.glEnable(GL10.GL_BLEND)

        programId = createShaderProgram(
            vertexShader,
            fragmentShader,
        )

        positionAttribute = GLES20.glGetAttribLocation(programId, "aPosition")
        initUniforms(programId)

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

        GLES20.glUseProgram(programId)

        updateUniforms()

        GLES20.glEnableVertexAttribArray(positionAttribute)
        GLES20.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, quadVertices.size)
        GLES20.glDisableVertexAttribArray(positionAttribute)

        Log.i("OpenGL", "Draw time ${SystemClock.elapsedRealtime() - start} ms")
    }

    private fun createShaderProgram(
        vertexShader: String,
        fragmentShader: String,
    ): Int {
        val programId = GLES20.glCreateProgram()

        val vertShader = createAndVerifyShader(vertexShader, GLES20.GL_VERTEX_SHADER)
        val fragShader = createAndVerifyShader(fragmentShader, GLES20.GL_FRAGMENT_SHADER)

        GLES20.glAttachShader(programId, fragShader)
        GLES20.glAttachShader(programId, vertShader)
        GLES20.glLinkProgram(programId)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] == 0) {
            val msg = "Linking of program failed. ${GLES20.glGetProgramInfoLog(programId)}"
            GLES20.glDeleteProgram(programId)
            throw IllegalStateException(msg)
        }

        GLES20.glDetachShader(programId, vertShader)
        GLES20.glDetachShader(programId, fragShader)
        GLES20.glDeleteShader(vertShader)
        GLES20.glDeleteShader(fragShader)

        return programId
    }

    private fun createAndVerifyShader(shaderCode: String, shaderType: Int): Int {
        val shaderId = GLES20.glCreateShader(shaderType)
        check(shaderId > 0) { "Unable to create shader!" }

        GLES20.glShaderSource(shaderId, shaderCode)
        GLES20.glCompileShader(shaderId)

        val compileStatusArray = IntArray(1)
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatusArray, 0)

        log(shaderCode)

        if (compileStatusArray.first() == 0) {
            log("$shaderCode \n : ${GLES20.glGetShaderInfoLog(shaderId)}")
            GLES20.glDeleteShader(shaderId)
            throw IllegalStateException("Shader compilation failed!")
        }

        return shaderId
    }

    protected abstract val fragmentShader: String

    protected abstract fun initUniforms(programId: Int)

    protected abstract fun updateUniforms()

    private fun log(message: String) {
        Log.i("GL", message)
    }
}
