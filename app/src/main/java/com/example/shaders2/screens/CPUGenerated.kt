package com.example.shaders2.screens

import android.graphics.Bitmap
import android.graphics.Color
import android.os.SystemClock
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.floor
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CPUGenerated() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("CPU")
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Test(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f),
                color = androidx.compose.ui.graphics.Color.Blue
            )
        }
    }
}

class BitmapGenerator(
    private val stripesCount: Int = 27,
    private val glassRefraction: Float = 0.125f
) {

    companion object {
        private const val CENTER = 0.5f
        private const val RADIUS = 0.4f
    }

    suspend fun generate(width: Int, height: Int, color: Int): Bitmap = withContext(Dispatchers.Default) {
        Log.i("BITMAP", "w = $width, h = $height")
        val start = SystemClock.elapsedRealtime()
        val pixels = IntArray(width * height)
        for (x in 0..<width) {
            for (y in 0..<height) {
                pixels[x + y * width] = main(x, y, width, height, color)
            }
        }

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
            Log.i("BITMAP", "Generation time: ${SystemClock.elapsedRealtime() - start}")
        }
    }

    private fun fract(x: Float): Float = x - floor(x)

    private fun glass(x: Float): Float {
        val xShift: Float = fract(x * stripesCount) - 0.5f
        return x + xShift * glassRefraction
    }

    private fun main(x: Int, y: Int, width: Int, height: Int, color: Int): Int {
        val uvX = glass(x.toFloat() / width.toFloat())
        val uvY = y.toFloat() / height.toFloat()

        val d = (uvX - CENTER) * (uvX - CENTER) / (RADIUS * RADIUS) +
                (uvY - CENTER) * (uvY - CENTER) / (RADIUS * RADIUS)

        val alpha = ((1f - d.coerceIn(0f, 1f)) * 255).roundToInt()
        return Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }
}


@Composable
fun Test(
    modifier: Modifier = Modifier,
    color: androidx.compose.ui.graphics.Color
) {
    BoxWithConstraints(
        modifier = modifier
    ) {

        var bitmap: Bitmap? by remember {
            mutableStateOf(null)
        }

        val coroutineScope = rememberCoroutineScope()

        DisposableEffect(constraints.maxWidth, constraints.maxHeight, color) {
            val job = coroutineScope.launch {
                BitmapGenerator().generate(
                    constraints.maxWidth,
                    constraints.maxHeight,
                    color.toArgb()
                ).also {
                    if (isActive) {
                        bitmap = it
                    }
                }
            }

            onDispose {
                Log.i("BITMAP", "dispose")
                job.cancel()
                bitmap?.recycle()
                bitmap = null
            }
        }

        bitmap?.let {
            Image(
                modifier = Modifier.fillMaxSize(),
                bitmap = it.asImageBitmap(),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            )
        }
    }
}
