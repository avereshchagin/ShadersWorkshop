package com.example.shaders2.screens

import android.opengl.GLSurfaceView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.R
import com.example.shaders2.capabilities.ThermalService
import com.example.shaders2.gl.SpotRenderer
import com.example.shaders2.gl.Task2Renderer
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenGL() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.nav_opengl))
                }
            )
        },
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val renderer = remember {
                SpotRenderer()
            }

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
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
                },
            )

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                factory = { context ->
                    GLSurfaceView(context).apply {
                        setEGLContextClientVersion(2)
                        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                        preserveEGLContextOnPause = true

                        setRenderer(Task2Renderer())
                        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY

                        keepScreenOn = true
                    }
                },
                update = {
                },
            )

            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
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
                },
            )

            val context = LocalContext.current
            val thermalService = remember {
                ThermalService(context)
            }

            var temp by remember {
                mutableIntStateOf(0)
            }

            LaunchedEffect(Unit) {
                while (true) {
                    temp = thermalService.getCurrent()
                    delay(1000)
                }
            }
        }
    }
}
