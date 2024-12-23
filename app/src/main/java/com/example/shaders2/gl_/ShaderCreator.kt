package com.example.shaders2.gl_

import android.opengl.GLES20
import android.util.Log
import java.nio.IntBuffer

object ShaderCreator {

    fun createShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also {
            GLES20.glShaderSource(it, shaderCode)
            GLES20.glCompileShader(it)
            val rvalue = IntBuffer.allocate(1)
            GLES20.glGetShaderiv(it, GLES20.GL_COMPILE_STATUS, rvalue)
            if (rvalue[0] == 0) {
                Log.e("createShader", GLES20.glGetShaderInfoLog(it))
            }
        }
    }
}
