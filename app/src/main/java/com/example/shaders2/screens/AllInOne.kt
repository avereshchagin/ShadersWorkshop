package com.example.shaders2.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.agsl.AgslShaderImage
import com.example.shaders2.gl.AnimationView
import io.github.avereshchagin.vulkan.VulkanSurfaceView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllInOne() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
                    text = "AGSL",
                )

                AgslShaderImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    color = color,
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    text = "OpenGL",
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
                    text = "Vulkan",
                )

                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f),
                    factory = { context ->
                        VulkanSurfaceView(context)
                    },
                    update = {
                        it.setColor(color.red, color.green, color.blue)
                        it.setBgColor(bgColor.red, bgColor.green, bgColor.blue)
                        it.redraw()
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
