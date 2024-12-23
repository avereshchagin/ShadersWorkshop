package com.vk.music.view.vkmix.gl

import com.example.shaders2.R

enum class GLShaderId(val resId: Int) {
    Vertex(R.raw.vert),
    Main(R.raw.main_frag),
    Music(R.raw.music_mix_frag),
    Blur(R.raw.blur_frag),
    Aberration(R.raw.aberration_frag),
}
