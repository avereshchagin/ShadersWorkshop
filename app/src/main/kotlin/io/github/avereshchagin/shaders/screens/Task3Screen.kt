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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import org.intellij.lang.annotations.Language

@Language("AGSL")
private const val shader = """
    uniform shader texture;
    
    half4 colorBlue = half4(0.1, 0.6, 0.93, 1.0);
    half4 colorPink = half4(0.85, 0.14, 0.75, 1.0);
    
    half4 main(float2 cord) {
        return colorPink;
    }
"""

@Composable
private fun ShaderContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val runtimeShader = remember {
        RuntimeShader(shader)
    }

    Box(
        modifier = modifier
            .clipToBounds()
            .graphicsLayer {
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
fun Task3Screen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ShaderContainer {
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
        }
    }
}