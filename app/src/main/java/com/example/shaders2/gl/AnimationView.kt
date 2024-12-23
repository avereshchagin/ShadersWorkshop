package com.example.shaders2.gl

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import androidx.compose.ui.graphics.Color
import com.vk.music.view.vkmix.gl.GLRenderer
import com.vk.music.view.vkmix.gl.GLShadersCodeRepository
import com.vk.music.view.vkmix.gl.textures.ColorsProvider
import com.vk.music.view.vkmix.gl.textures.DisplayOutput
import com.vk.music.view.vkmix.gl.textures.MusicMixTextureSource

class AnimationView(
    context: Context,
    attributeSet: AttributeSet? = null
) : GLTextureView(context, attributeSet) {

    var color: Color = Color.Black
        set(value) {
            field = value
            render.invalidate()
            requestRender()
        }

    var bgColor: Color = Color.Black
        set(value) {
            field = value
            render.invalidate()
            requestRender()
        }

    private val render: GLRenderer

    init {
        preserveEGLContextOnPause = true
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        val codeRepository = GLShadersCodeRepository(context)

        val texturePipelineQueue = listOf(
            MusicMixTextureSource.Factory(
                shadersCodeRepositoryProvider = { codeRepository },
                colorsProvider = object : ColorsProvider {

                    override val color: Color
                        get() = this@AnimationView.color

                    override val bgColor: Color
                        get() = this@AnimationView.bgColor
                }
            ),
//            BlurTextureSource.Factory(
//                shadersCodeRepositoryProvider = { codeRepository }
//            ),
//            AberrationTextureSource.Factory(
//                shadersCodeRepositoryProvider = { codeRepository }
//            ),
        )

        render = GLRenderer().apply {
            setPipelineQueue(texturePipelineQueue)
            setOutput(DisplayOutput.Factory(codeRepository))
        }

        setRenderer(render)
    }

    override fun setRenderer(renderer: GLSurfaceView.Renderer?) {
        super.setRenderer(renderer)
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun onResume() {
        super.onResume()
        requestRender()
    }
}