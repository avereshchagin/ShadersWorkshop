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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shaders2.screens.AGSL
import com.example.shaders2.screens.Capabilities
import com.example.shaders2.screens.Mandelbrot
import com.example.shaders2.screens.OpenGL
import com.example.shaders2.screens.RenderEffectScreen
import com.example.shaders2.screens.Vulkan
import com.example.shaders2.ui.theme.Shaders2Theme

private enum class NavRoute(val route: String, @StringRes val title: Int) {
    OpenGL("opengl", R.string.nav_opengl),
    AGSL("agsl", R.string.nav_agsl),
    RenderEffect("render_effect", R.string.nav_render_effect),
    Mandelbrot("mandelbrot", R.string.nav_mandelbrot),
//    CPU("CPU", R.string.nav_cpu),
//    AllInOne("all-in-one", R.string.nav_allinone),
    Capabilities("capabilities", R.string.nav_capabilities),
    Vulkan("vulkan", R.string.nav_vulkan),
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
                    composable(NavRoute.AGSL.route) { AGSL() }
                    composable(NavRoute.RenderEffect.route) { RenderEffectScreen() }
//                    composable(NavRoute.CPU.route) { CPUGenerated() }
//                    composable(NavRoute.AllInOne.route) { AllInOne() }
                    composable(NavRoute.OpenGL.route) { OpenGL() }
                    composable(NavRoute.Mandelbrot.route) { Mandelbrot() }
                    composable(NavRoute.Vulkan.route) { Vulkan() }
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
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        onClick = { onNavigateTo(it) },
                    ) {
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
