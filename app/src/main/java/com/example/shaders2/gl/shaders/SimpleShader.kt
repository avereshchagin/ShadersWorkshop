package com.vk.music.view.vkmix.gl.shaders

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Size
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

open class SimpleShader(
    vertexShader: Int,
    fragmentShader: Int
) : GLShader {

    var inputTexture: Int = -1

    protected val mProgram = GLES20.glCreateProgram()

    private val vertexStride = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private var vertexBuffer = floatArrayOf(
        // x, y, z
        -1.0f, -1.0f, 0.0f, // point 0
        -1.0f,  1.0f, 0.0f, // point 1
         1.0f,  1.0f, 0.0f, // point 2
         1.0f, -1.0f, 0.0f, // point 3
                            // see drawOrder 0, 1, 2, 0, 2, 3
    ).toFloatBuffer()

    private val textureCoordsBuffer: FloatBuffer = floatArrayOf(
        0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        1.0f, 1.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
    ).toFloatBuffer()

    private val positionHandle: Int
    private val matrixHandle: Int
    private val textureUniformHandle: Int
    private val textureCoordHandle: Int

    private val drawOrderBuffer: ShortBuffer = ByteBuffer.allocateDirect(drawOrder.size * 2).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(drawOrder)
            position(0)
        }
    }

    init {
        GLES20.glAttachShader(mProgram, vertexShader)
        GLES20.glAttachShader(mProgram, fragmentShader)
        GLES20.glLinkProgram(mProgram)

        matrixHandle = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix")
        textureUniformHandle = GLES20.glGetUniformLocation(mProgram, "u_Texture")

        positionHandle = GLES20.glGetAttribLocation(mProgram, "a_Position")
        textureCoordHandle = GLES20.glGetAttribLocation(mProgram, "a_TexCoordinate")
    }

    open fun bindExtraHandles(viewportSize: Size) {}

    fun draw(viewportSize: Size) {
        GLES20.glUseProgram(mProgram)

        bindExtraHandles(viewportSize)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTexture)
        /* Логично было бы здесь передавать GLES20.GL_TEXTURE0, но так не работает :) */
        GLES20.glUniform1i(textureUniformHandle, 0)

        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, identityMatrix, 0)

        GLES20.glVertexAttribPointer(
            positionHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        GLES20.glVertexAttribPointer(
            textureCoordHandle,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            vertexStride,
            textureCoordsBuffer
        )

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(textureCoordHandle)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            drawOrder.size,
            GLES20.GL_UNSIGNED_SHORT,
            drawOrderBuffer
        )

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(textureCoordHandle)
    }

    protected fun FloatArray.toFloatBuffer() =
        ByteBuffer.allocateDirect(size * 4).let {
            it.order(ByteOrder.nativeOrder())
            it.asFloatBuffer().apply {
                put(this@toFloatBuffer)
                position(0)
            }
        }

    companion object {

        // Number of coordinates per vertex in this array
        protected const val COORDS_PER_VERTEX = 3

        protected val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3)

        private val identityMatrix = FloatArray(16).apply {
            Matrix.setIdentityM(this, 0)
            // Mirror upside-down
            Matrix.scaleM(this, 0, 1f, -1f, 0f)
        }
    }
}
