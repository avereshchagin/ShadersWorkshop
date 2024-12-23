package com.example.shaders2.agsl

import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language

@Language("AGSL")
val agslShaderSrc = """
    uniform float2 iResolution;
    uniform float3 iColor;
    
    float GLASS_REFRACTION = 0.125;
    
    vec2 glass(vec2 uv) {
        float stripesCount = 17.0;
        float xShift = fract(uv.x * stripesCount) - 0.5;
        uv.x += xShift * GLASS_REFRACTION;
        return uv;
    }
    
    float4 main(float2 fragCoord) {
        vec2 center = vec2(0.5, 0.5);
        float rx = 0.4;
        float ry = 0.4;
        
        float2 uv = fragCoord / iResolution;
        uv = glass(uv);
        
        float d = (uv.x - center.x) * (uv.x - center.x) / (rx * rx) +
            (uv.y - center.y) * (uv.y - center.y) / (ry * ry);
            
        return mix(vec4(iColor, 1.0), vec4(0.0, 0.0, 0.0, 0.0), d);
    }
""".trimIndent()

@Composable
internal fun AgslShaderImage(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val shader = remember {
        RuntimeShader(agslShaderSrc)
    }
    val brush = remember(shader) {
        ShaderBrush(shader)
    }

    Box(
        modifier = modifier
            .drawBehind {
                shader.setFloatUniform("iResolution", size.width, size.height)
                shader.setFloatUniform("iColor", color.red, color.green, color.blue)
                drawRect(brush, blendMode = BlendMode.Multiply)
            }
    )
}