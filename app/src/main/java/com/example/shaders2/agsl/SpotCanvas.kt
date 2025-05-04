package com.example.shaders2.agsl

import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val agslShaderSrc = """
    uniform float2 iResolution;
    uniform float iTime;
    layout(color) uniform half4 iBgColor;
    
    float3 colorBlack = float3(0.0, 0.0, 0.0);
    float2 center = float2(0.5, 0.5);
    float radius = 0.4;
    
    float GLASS_REFRACTION = 0.125;
    
    half3 pal(float t) {
        half3 a = half3(0.5,0.5,0.5);
        half3 b = half3(0.5,0.5,0.5);
        half3 c = half3(1.0,1.0,1.0);
        half3 d = half3(0.0,0.33,0.67);
        return a + b*cos(6.28318*(c*t+d));
    }
    
    float2 glass(float2 uv) {
        float stripesCount = (cos(iTime) + 1.0) * 10.0;
        float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
        uv.x += xShift * GLASS_REFRACTION;
        return uv;
    }
    
    half4 main(float2 fragCoord) {
        float2 uv = fragCoord.xy / iResolution.xy;
        uv = glass(uv);
        float dist = distance(uv, center);
        half4 color = half4(pal((cos(iTime / 5.0) + 1.0) / 2.0), 1.0);
        return mix(color, iBgColor, smoothstep(radius / 2.0, radius, dist));
    }
""".trimIndent()

@RequiresApi(33)
@Composable
internal fun SpotCanvas(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
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
                withFrameMillis {
                    time = it / 1000f
                }
            }
        }

        val bgColor = MaterialTheme.colorScheme.background

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
        ) {
            shader.setFloatUniform("iResolution", size.width, size.height)
            shader.setFloatUniform("iTime", time)
            shader.setColorUniform("iBgColor", bgColor.toArgb())
            drawRect(brush)
        }
    }
}
