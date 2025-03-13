package com.example.shaders2.screens

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

@Language("AGSL")
const val Source = """
    uniform shader composable;
    
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
        half4 color = composable.eval(uv * iResolution.xy);
        return color;
    }
"""

@Language("AGSL")
const val Source2 = """
    uniform shader composable;
    
    uniform float2 iResolution;
    uniform float iTime;
    
    half4 main(float2 cord) {
        float2 uv = cord / iResolution.xy;
        uv.y = uv.y + cos(uv.x * 3.14 * 4.0) * iTime;
        half4 color = composable.eval(uv * iResolution.xy);
        return color;
    }
"""

@Composable
private fun ShaderContainer(
    modifier: Modifier = Modifier,
    stripes: State<Float>,
    content: @Composable BoxScope.() -> Unit,
) {
    val runtimeShader = remember {
        RuntimeShader(Source2)
    }
    Box(
        modifier
            .graphicsLayer {
                runtimeShader.setFloatUniform("iResolution", size.width, size.height)
//                runtimeShader.setFloatUniform("stripesCount", stripes.value)
                runtimeShader.setFloatUniform("iTime", stripes.value)
                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(
                        runtimeShader, "composable"
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

                    Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse eget orci ac lorem blandit tempus eu quis sem. Sed id elit ac ex luctus malesuada lacinia iaculis enim. Morbi ullamcorper nisl at molestie bibendum. Nam lorem tellus, sodales dictum fringilla eu, pharetra at ante. Phasellus egestas facilisis pretium. Phasellus id eros in urna finibus fermentum at ac leo. Sed volutpat odio dolor, eget volutpat nibh egestas sit amet. Morbi vulputate vehicula neque, eu pharetra libero. Pellentesque id diam a ex feugiat ultricies. Vivamus non enim nunc. Proin sagittis purus nulla, a ultrices nunc egestas nec. Nullam dui risus, sodales quis aliquet at, consequat eget sapien. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum orci urna, consequat sit amet tincidunt et, finibus sed odio.")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                stripes.targetState = 0.03f
                            }
                        ) {
                            Text("OK")
                        }

                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                stripes.targetState = 0f
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