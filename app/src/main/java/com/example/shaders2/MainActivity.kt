package com.example.shaders2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shaders2.agsl.AgslShaderImage
import com.example.shaders2.gl.AnimationView
import com.example.shaders2.screens.AGSL
import com.example.shaders2.screens.AllInOne
import com.example.shaders2.screens.Capabilities
import com.example.shaders2.screens.OpenGL
import com.example.shaders2.screens.Vulkan
import com.example.shaders2.ui.theme.Shaders2Theme
import io.github.avereshchagin.vulkan.VulkanSurfaceView

private enum class NavRoute(val route: String, @StringRes val title: Int) {
    Capabilities("capabilities", R.string.nav_capabilities),
    AllInOne("all-in-one", R.string.nav_allinone),
    OpenGL("opengl", R.string.nav_opengl),
    Vulkan("vulkan", R.string.nav_vulkan),
    AGSL("agsl", R.string.nav_agsl),
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Shaders2Theme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "main",
                    enterTransition = { EnterTransition.None },
                    exitTransition = { ExitTransition.None },
                ) {
                    composable("main") {
                        MainScreen(
                            onNavigateTo = { navController.navigate(it.route) },
                        )
                    }
                    composable(NavRoute.Capabilities.route) { Capabilities() }
                    composable(NavRoute.AllInOne.route) { AllInOne() }
                    composable(NavRoute.OpenGL.route) { OpenGL() }
                    composable(NavRoute.Vulkan.route) { Vulkan() }
                    composable(NavRoute.AGSL.route) { AGSL() }
                }
            }
        }
    }
}

@Composable
private fun MainScreen(
    onNavigateTo: (NavRoute) -> Unit,
) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 60.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(NavRoute.entries) {
                    Button(onClick = { onNavigateTo(it) }) {
                        Text(
                            text = stringResource(it.title),
                            style = MaterialTheme.typography.headlineLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }
}
