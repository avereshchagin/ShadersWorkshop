package com.example.shaders2.capabilities

import android.opengl.EGL14.EGL_CONTEXT_CLIENT_VERSION
import android.opengl.GLES10
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.opengles.GL10


class CapabilitiesProvider {

    fun getGlVersion(): String {
        val egl = EGLContext.getEGL() as EGL10
        val eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)

        val mConfigSpec = intArrayOf(
            EGL10.EGL_RED_SIZE, 8,
            EGL10.EGL_GREEN_SIZE, 8,
            EGL10.EGL_BLUE_SIZE, 8,
            EGL10.EGL_ALPHA_SIZE, 8,
            EGL10.EGL_DEPTH_SIZE, 16,
            EGL10.EGL_STENCIL_SIZE, 0,
            EGL10.EGL_NONE
        )

        egl.eglInitialize(eglDisplay, intArrayOf(0, 0))

        val num_config = IntArray(1)
        require(
            egl.eglChooseConfig(
                eglDisplay, mConfigSpec, null, 0,
                num_config
            )
        ) { "eglChooseConfig failed" }

        val numConfigs = num_config[0]

        require(numConfigs > 0) { "No configs match configSpec" }

        val configs = arrayOfNulls<EGLConfig>(numConfigs)
        require(
            egl.eglChooseConfig(
                eglDisplay, mConfigSpec, configs, numConfigs,
                num_config
            )
        ) { "eglChooseConfig#2 failed" }

        val context = egl.eglCreateContext(
            eglDisplay,
            configs[0],
            EGL10.EGL_NO_CONTEXT,
            intArrayOf(EGL_CONTEXT_CLIENT_VERSION, 1, EGL10.EGL_NONE)
        )

        val version = GLES10.glGetString(GL10.GL_VERSION)

        egl.eglDestroyContext(eglDisplay, context)

        return version
    }
}
