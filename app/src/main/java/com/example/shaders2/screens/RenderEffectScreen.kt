package com.example.shaders2.screens

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val Source = """
    uniform shader texture;
    
    uniform float2 iResolution;
    uniform float stripesCount;
    
    float GLASS_REFRACTION = 0.125;
    
    float2 glass(float2 uv) {
        float xShift = fract((uv.x - 0.5) * stripesCount + 0.5) - 0.5;
        uv.x += xShift * GLASS_REFRACTION;
        return uv;
    }

    half4 main(float2 cord) {
        float2 uv = cord / iResolution.xy;
        uv = glass(uv);
        half4 color = texture.eval(uv * iResolution.xy);
        return color;
    }
"""

@Language("AGSL")
const val Source2 = """
    uniform shader texture;
    
    uniform float2 iResolution;
    uniform float iTime;
    
    half4 main(float2 cord) {
        float2 uv = cord / iResolution.xy;
        uv.y = uv.y + cos(uv.x * 3.14 * 4.0) * iTime;
        half4 color = texture.eval(uv * iResolution.xy);
        return color;
    }
"""

@Language("AGSL")
const val LensDistortion = """
    uniform shader texture;
    
    uniform float2 iResolution;
    uniform float iTime;
    
    vec2 lens_distortion(vec2 r, float a) {
        return r * (1.0 - a * dot(r, r));   
    }

    vec2 zoom_point(vec2 uv, vec2 center, float zoom) {
        return (uv - center) / zoom + center;
    }

    half4 main(float2 cord) {
        float2 uv = cord / iResolution.xy;
        float2 center = float2(0.5, 0.5);
        
        vec2 distortion = lens_distortion(uv - center, -2.5);
        vec2 zoomed = zoom_point(uv + distortion, center, 2.0);
        
        if (zoomed.x > 0.0 && zoomed.y > 0.0 && zoomed.x < 1.0 && zoomed.y < 1.0) {
            return texture.eval(zoomed * iResolution.xy);
        } else {
            return half4(1.0);
        }
    }
"""

@Composable
private fun ShaderContainer(
    modifier: Modifier = Modifier,
    stripes: State<Float>,
    content: @Composable BoxScope.() -> Unit,
) {
    val runtimeShader = remember {
        RuntimeShader(LensDistortion)
    }
    Box(
        modifier = modifier
            .graphicsLayer {
                runtimeShader.setFloatUniform("iResolution", size.width, size.height)
//                runtimeShader.setFloatUniform("stripesCount", stripes.value)
                runtimeShader.setFloatUniform("iTime", stripes.value)
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val stripes = remember {
                MutableTransitionState(0.0f)
            }
            val transition = rememberTransition(stripes)
            val s = transition.animateFloat(
                transitionSpec = {
                    tween(durationMillis = 2000)
                }
            ) { it }
            ShaderContainer(
                stripes = s
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
//                                stripes.targetState = 0.03f
                            }
                        ) {
                            Text("OK")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
//                                stripes.targetState = 0f
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}