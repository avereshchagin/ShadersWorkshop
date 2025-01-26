package com.example.shaders2.screens

import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shaders2.capabilities.CapabilitiesProvider

@Composable
fun Capabilities() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .align(Alignment.Center)
            ) {
                val capabilitiesProvider = remember {
                    CapabilitiesProvider()
                }

                Text(text = "Model: ${Build.MANUFACTURER} ${Build.MODEL}")

                Text(text = "SDK: ${Build.VERSION.SDK_INT}")

//                Text(text = "OpenGL: ${capabilitiesProvider.getGlVersion()}")
            }
        }
    }
}
