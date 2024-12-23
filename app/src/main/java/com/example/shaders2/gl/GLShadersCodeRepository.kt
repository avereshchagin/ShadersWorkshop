package com.vk.music.view.vkmix.gl

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.ByteArrayOutputStream
import java.lang.ref.Cleaner
import java.util.WeakHashMap
import java.util.concurrent.ConcurrentHashMap

class GLShadersCodeRepository(
    val context: Context
) {

    private val cache = ConcurrentHashMap<GLShaderId, MutableStateFlow<String>>()

    fun getShaderCode(id: GLShaderId) = cache[id] ?: run {
        val code = loadCodeFromResources(id)
        val stateFlow = MutableStateFlow(code)
        cache[id] = stateFlow
        stateFlow
    }

    fun setShaderCode(id: GLShaderId, code: String) {
        val stateFlow = cache[id] ?: MutableStateFlow(code).also {
            cache[id] = it
        }
        stateFlow.tryEmit(code)
    }

    private fun loadCodeFromResources(id: GLShaderId): String {
        val inputStream = context.resources.openRawResource(id.resId)
        val outputStream = ByteArrayOutputStream()

        var i = inputStream.read()
        while (i != -1) {
            outputStream.write(i)
            i = inputStream.read()
        }

        inputStream.close()

        return outputStream.toString()
    }
}
