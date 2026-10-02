package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.RizzBottomNav
import com.example.ui.components.RizzTopBar
import com.example.ui.screens.AboutScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.GeneratorScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.theme.DarkBg
import com.example.ui.theme.RizzTheme
import com.example.ui.viewmodel.RizzViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RizzTheme {
                RizzApp()
            }
        }
    }
}

@Composable
fun RizzApp(
    viewModel: RizzViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    // Toast handler
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }
    }

    // Android back handler
    BackHandler(enabled = currentScreen != "home") {
        viewModel.navigateTo("home")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DarkBg,
        topBar = {
            RizzTopBar(
                onGenerateClick = { viewModel.navigateTo("generator") }
            )
        },
        bottomBar = {
            RizzBottomNav(
                currentScreen = currentScreen,
                favoritesCount = favorites.size,
                onNavigate = { viewModel.navigateTo(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Crossfade(
            targetState = currentScreen,
            modifier = Modifier.padding(innerPadding),
            label = "screen_transition"
        ) { screen ->
            when (screen) {
                "home" -> HomeScreen(
                    onNavigateToGenerator = { viewModel.navigateTo("generator") },
                    onSurpriseMe = { viewModel.surpriseMe() },
                    onApplyPreset = { style -> viewModel.applyPreset(style) }
                )
                "generator" -> GeneratorScreen(viewModel = viewModel)
                "favorites" -> FavoritesScreen(viewModel = viewModel)
                "history" -> HistoryScreen(viewModel = viewModel)
                "about" -> AboutScreen()
                else -> HomeScreen(
                    onNavigateToGenerator = { viewModel.navigateTo("generator") },
                    onSurpriseMe = { viewModel.surpriseMe() },
                    onApplyPreset = { style -> viewModel.applyPreset(style) }
                )
            }
        }
    }
}
