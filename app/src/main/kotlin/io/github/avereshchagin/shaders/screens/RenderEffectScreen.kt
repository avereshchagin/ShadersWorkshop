package io.github.avereshchagin.shaders.screens

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FloatState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val LensDistortion = """
    uniform shader texture;
    
    uniform float2 iResolution;
    uniform float iAlpha;
    layout(color) uniform half4 iBgColor;
    
    vec2 lens_distortion(vec2 r, float a) {
        return r * (1.0 - a * dot(r, r));   
    }

    vec2 zoom_point(vec2 uv, vec2 center, float zoom) {
        return (uv - center) / zoom + center;
    }

    half4 main(float2 cord) {
        float2 uv = cord / iResolution.xy;
        float2 center = float2(0.5, 0.5);
        
        vec2 distortion = lens_distortion(uv - center, iAlpha);
        vec2 zoomed = zoom_point(uv + distortion, center, 2.0);
        
        if (zoomed.x > 0.0 && zoomed.y > 0.0 && zoomed.x < 1.0 && zoomed.y < 1.0) {
            return texture.eval(zoomed * iResolution.xy);
        } else {
            return iBgColor;
        }
    }
"""

@Composable
private fun ShaderContainer(
    modifier: Modifier = Modifier,
    sliderPosition: FloatState,
    content: @Composable BoxScope.() -> Unit,
) {
    val runtimeShader = remember {
        RuntimeShader(LensDistortion)
    }
    val bgColor = MaterialTheme.colorScheme.background
    Box(
        modifier = modifier
            .clipToBounds()
            .graphicsLayer {
                runtimeShader.setFloatUniform("iResolution", size.width, size.height)
                runtimeShader.setFloatUniform("iAlpha", sliderPosition.floatValue)
                runtimeShader.setColorUniform("iBgColor", bgColor.toArgb())
                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(
                        runtimeShader, "texture"
                    )
                    .asComposeRenderEffect()
            },
        content = content
    )
}

@Composable
fun RenderEffectScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val sliderPositionState = remember {
                mutableFloatStateOf(0f)
            }
            var sliderPosition by sliderPositionState

            ShaderContainer(
                sliderPosition = sliderPositionState
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineLarge,
                        text = "RenderEffect"
                    )

                    Text(
                        modifier = Modifier
                            .height(300.dp)
                            .verticalScroll(rememberScrollState()),
                        text = LoremIpsum().values.first(),
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                            }
                        ) {
                            Text("OK")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }

            Slider(
                modifier = Modifier.padding(16.dp),
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = -4f .. 4f,
            )
        }
    }
}