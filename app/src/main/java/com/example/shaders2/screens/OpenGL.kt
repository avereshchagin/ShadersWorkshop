package com.example.shaders2.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
                    ShaderGlSurfaceView(context, renderer).apply {
                        keepScreenOn = true
                    }
                },
                update = {
//                    nonce
                    it.requestRender()
                }
            )

            Text(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                text = "FPS 1293892849"
            )
        }
    }
}

//@Composable
//fun Sample() {
//
//    val bgColor = Color(1f, 0f, 0f, 1f)
//
//    Shader(
//        modifier = Modifier.fillMaxSize(),
//        code = "refraction_gl.frag",
//    ) {
//        setValue("bgColor", bgColor)
//    }
//}
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
//    code: String,
//    modifier: Modifier = Modifier,
//    onErrorPlaceholder: @Composable () -> Unit = {},
//    update: ShaderScope.() -> Unit = {},
//) {
//    val assets = LocalContext.current.assets
//
//    val scope = remember(assets) {
//        val renderer = GLRenderer(assets, code)
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
