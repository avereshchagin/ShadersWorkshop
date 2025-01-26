package com.example.shaders2.gl2

import android.content.res.AssetManager
import android.opengl.GLES10
import android.opengl.GLES20
import android.util.Log
import java.io.ByteArrayOutputStream
import javax.microedition.khronos.opengles.GL10

class GLShaderProgram(
    val programId: Int
) {

    companion object {

        fun createShaderProgram(
            assets: AssetManager,
            pathToVert: String,
            pathToFrag: String,
        ): GLShaderProgram {
            val programId = GLES20.glCreateProgram()

            val vertShader = createAndVerifyShader(loadCodeFromResources(assets, pathToVert), GLES20.GL_VERTEX_SHADER)
            val fragShader = createAndVerifyShader(loadCodeFromResources(assets, pathToFrag), GLES20.GL_FRAGMENT_SHADER)

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

            return GLShaderProgram(programId)
        }

        fun createAndVerifyShader(shaderCode: String, shaderType: Int): Int {
            val version = GLES10.glGetString(GL10.GL_VERSION)
            log("opengl version: $version")

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

        private fun loadCodeFromResources(assets: AssetManager, filename: String): String {
            val inputStream = assets.open(filename)
            val outputStream = ByteArrayOutputStream()

            var i = inputStream.read()
            while (i != -1) {
                outputStream.write(i)
                i = inputStream.read()
            }

            inputStream.close()

            return outputStream.toString()
        }

        private fun log(message: String) {
            Log.i("GL", message)
        }
    }
}