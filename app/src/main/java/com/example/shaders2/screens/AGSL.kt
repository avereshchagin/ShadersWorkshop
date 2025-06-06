package com.example.shaders2.screens

import android.graphics.Color
import android.graphics.RuntimeShader
import android.os.Build
import android.os.SystemClock
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import com.example.shaders2.R
import com.example.shaders2.agsl.SpotCanvas
import kotlinx.coroutines.delay
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AGSL() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.nav_agsl))
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                SpotCanvas()
            } else {
                Text(stringResource(R.string.agsl_not_supported))
            }
        }
    }
}
