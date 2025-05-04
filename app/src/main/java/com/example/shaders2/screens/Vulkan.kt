package com.example.shaders2.screens

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.example.shaders2.R
import io.github.avereshchagin.vulkan.VulkanSurfaceView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Vulkan() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.nav_vulkan))
                }
            )
        },
    ) { innerPadding ->

        val color = Color.Red
        val bgColor = Color.Black

        AndroidView(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .aspectRatio(3f),
            factory = { context ->
                VulkanSurfaceView(context)
            },
            update = {
                it.setColor(color.red, color.green, color.blue)
                it.setBgColor(bgColor.red, bgColor.green, bgColor.blue)
                it.redraw()
            }
        )
    }
}