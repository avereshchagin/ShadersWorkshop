package com.example.shaders2.screens

import android.opengl.GLSurfaceView
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.R
import com.example.shaders2.gl.MandelbrotRenderer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Mandelbrot() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.nav_mandelbrot))
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val renderer = remember {
                MandelbrotRenderer()
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
                factory = { context ->
                    GLSurfaceView(context).apply {
                        setEGLContextClientVersion(2)
                        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                        preserveEGLContextOnPause = true
                        keepScreenOn = true

                        setRenderer(renderer)
                    }
                },
            )
        }
    }
}
