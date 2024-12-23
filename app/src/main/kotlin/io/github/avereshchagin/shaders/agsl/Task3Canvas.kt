package io.github.avereshchagin.shaders.agsl

import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ShaderBrush
import org.intellij.lang.annotations.Language

@Language("AGSL")
private val agslShaderSrc = """
    half4 main(float2 fragCoord) {
        return half4(half3(0.0), 1.0);
    }
""".trimIndent()

@RequiresApi(33)
@Composable
internal fun Task3Canvas(
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

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f),
        ) {
            drawRect(brush)
        }
    }
}
