package com.example.shaders2.screens

import android.graphics.RuntimeShader
import android.os.SystemClock
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ShaderBrush
import kotlinx.coroutines.delay
import org.intellij.lang.annotations.Language

@Language("AGSL")
val agslShaderSrc = """
    uniform float2 iResolution;
    uniform float iTime;
    
    half3 colorBlue = half3(0.1, 0.6, 0.93);
    half3 colorPink = half3(0.85, 0.14, 0.75);

    float GLASS_REFRACTION = 0.125;

    float2 glass(float2 uv) {
        float stripesCount = 17.0;
        float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
        uv.x += xShift * GLASS_REFRACTION;
        return uv;
    }
    
    half4 main(float2 fragCoord) {
        float2 uv = fragCoord / iResolution.xy;
        uv = glass(uv);
        
        float dist = abs(uv.y - cos(uv.x * 3.14 * 2.0 + iTime) / 3.0);
        
        half3 col = mix(colorPink, colorBlue, smoothstep(0.4, 0.6, dist));

        return half4(col, 1.0);
    }
""".trimIndent()

@Composable
fun AGSL() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val shader = remember {
                RuntimeShader(agslShaderSrc)
            }
            val brush = remember(shader) {
                ShaderBrush(shader)
            }

            var time by remember {
                mutableFloatStateOf(0f)
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(10)
                    time = SystemClock.elapsedRealtime() / 1000f
                }
            }

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f),
            ) {
                shader.setFloatUniform("iResolution", size.width, size.height)
                shader.setFloatUniform("iTime", time)
                drawRect(brush, blendMode = BlendMode.Multiply)
            }
        }
    }
}