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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.capabilities.ThermalService
import com.example.shaders2.gl2.GLRenderer
import com.example.shaders2.gl2.ShaderGlSurfaceView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

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
                GLRenderer(assets, "")
            }

            var nonce by remember {
                mutableIntStateOf(0)
            }

//            LaunchedEffect(key1 = Unit) {
//                while (true) {
//                    nonce++
//                    delay(20)
//                }
//            }

            AndroidView(
                modifier = Modifier
                    .fillMaxSize(),
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
//                    nonce
                    it.requestRender()
                }
            )

            val context = LocalContext.current
            val thermalService = remember {
                ThermalService(context)
            }

            var temp by remember {
                mutableIntStateOf(0)
            }

//            val coroutineScope = rememberCoroutineScope()
            LaunchedEffect(Unit) {
                while (true) {
                    temp = thermalService.getCurrent()
                    delay(1000)
                }
            }

//            Text(
//                modifier = Modifier
//                    .align(Alignment.TopStart)
//                    .padding(16.dp),
//                text = (temp / 10.0).toString()
//            )
        }
    }
}

//@Composable
//fun PlaceholderImage() {
//
//}
//
//@Composable
//fun Sample(colorFlow: StateFlow<Color>) {
//
//
//
//    val color by colorFlow.collectAsState()
//
//    Shader(
//        modifier = Modifier.fillMaxSize(),
//        source = "refraction_gl.frag",
//        onErrorPlaceholder = {
//            PlaceholderImage()
//        },
//    ) {
//        setValue("color", color)
//    }
//}
//
//
//class ShaderScope(
//    val renderer: GLRenderer,
//) {
//
//    fun setValue(name: String, color: Color) {
//        renderer.setValue(name, color)
//    }
//}
//
//@Composable
//fun Shader(
//    source: String,
//    modifier: Modifier = Modifier,
//    onErrorPlaceholder: @Composable () -> Unit = {},
//    update: ShaderScope.() -> Unit = {},
//) {
//    val assets = LocalContext.current.assets
//
//    val scope = remember(assets) {
//        val renderer = GLRenderer(assets, source)
//        ShaderScope(renderer)
//    }
//
//    AndroidView(
//        modifier = modifier,
//        factory = { context ->
//            ShaderGlSurfaceView(context, scope.renderer).apply {
//                keepScreenOn = true
//            }
//        },
//        update = {
//            scope.update()
//            it.requestRender()
//        }
//    )
//}
