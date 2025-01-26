package com.example.shaders2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.gl2.GLRenderer
import com.example.shaders2.gl2.ShaderGlSurfaceView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenGL() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("OpenGL title")
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val assets = LocalContext.current.assets
            val renderer = remember {
                GLRenderer(assets)
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    ShaderGlSurfaceView(context, renderer)
                },
                update = {
                }
            )
        }
    }
}
