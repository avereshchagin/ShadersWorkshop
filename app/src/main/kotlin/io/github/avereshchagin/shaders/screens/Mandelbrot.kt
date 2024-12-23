package io.github.avereshchagin.shaders.screens

import android.opengl.GLSurfaceView
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import io.github.avereshchagin.shaders.R
import io.github.avereshchagin.shaders.gl.MandelbrotRenderer

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

            val heightPx = LocalContext.current.resources.displayMetrics.heightPixels

            var offset by remember { mutableStateOf(Offset.Zero) }
            var scale by remember { mutableFloatStateOf(1f) }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            offset = Offset(
                                offset.x + pan.x / heightPx.toFloat(),
                                offset.y - pan.y / heightPx.toFloat(),
                            )
                            scale /= zoom
                        }
                    },
                factory = { context ->
                    GLSurfaceView(context).apply {
                        setEGLContextClientVersion(2)
                        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
                        preserveEGLContextOnPause = true
                        keepScreenOn = true

                        setRenderer(renderer)
                        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    }
                },
                update = { view ->
                    renderer.center = offset
                    renderer.scale = scale
                    view.requestRender()
                }
            )
        }
    }
}
