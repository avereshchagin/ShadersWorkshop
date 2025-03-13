package com.example.shaders2.screens

import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.agsl.AgslShaderImage
import com.example.shaders2.gl.AnimationView
import com.example.shaders2.gl2.GLRenderer
import com.example.shaders2.gl2.GLRenderer2
import io.github.avereshchagin.vulkan.VulkanSurfaceView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurfaceTexture(modifier: Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .align(Alignment.Center)
            ) {
                val sliderState = remember {
                    SliderState()
                }

                var color by remember {
                    mutableStateOf(Color.Red)
                }
                val bgColor = MaterialTheme.colorScheme.background

                color = Color.hsv(sliderState.value * 360, 1f, 1f)

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = "TextureView",
                )

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    factory = { context ->
                        AnimationView(context)
                    },
                    update = {
                        it.color = color
                        it.bgColor = bgColor
                    }
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = "SurfaceView",
                )

                val assets = LocalContext.current.assets
                val renderer = remember {
                    GLRenderer2(assets, "refraction_gl.frag")
                }

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    factory = { context ->
                        GLSurfaceView(context).apply {
                            setEGLContextClientVersion(2)
                            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                            preserveEGLContextOnPause = true

                            setRenderer(renderer)
                            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

                            keepScreenOn = true
                        }
                    },
                    update = {
                        renderer.color = color
                        renderer.bgColor = bgColor
                        it.requestRender()
                    }
                )

                Slider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    state = sliderState,
                )
            }
        }
    }
}
