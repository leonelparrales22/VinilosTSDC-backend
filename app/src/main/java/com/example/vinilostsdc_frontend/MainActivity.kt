package com.example.vinilostsdc_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vinilostsdc_frontend.presentation.screen.AlbumListScreen
import com.example.vinilostsdc_frontend.presentation.screen.ArtistListScreen
import com.example.vinilostsdc_frontend.presentation.screen.CollectorListScreen
import com.example.vinilostsdc_frontend.presentation.screen.VisitanteMenuScreen
import com.example.vinilostsdc_frontend.presentation.screen.RoleSelectionScreen
import com.example.vinilostsdc_frontend.ui.theme.VinilosTSDCfrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VinilosTSDCfrontendTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {
                    composable("main") {
                        RoleSelectionScreen(
                            onVisitante = { navController.navigate("visitante") },
                            onColeccionista = { /* Pendiente de desarrollar */ }
                        )
                    }
                    composable("visitante") {
                        VisitanteMenuScreen(navController)
                    }
                    composable("albums") {
                        AlbumListScreen(
                            onBack = { navController.popBackStack() },
                            onAlbumClick = { album ->
                                // TODO: Navigate to album detail
                            }
                        )
                    }
                    composable("artists") {
                        ArtistListScreen(
                            onBack = { navController.popBackStack() },
                            onArtistClick = { artist ->
                                // TODO: Navigate to artist detail
                            }
                        )
                    }
                    composable("collectors") {
                        CollectorListScreen(
                            onBack = { navController.popBackStack() },
                            onCollectorClick = { collector ->
                                // TODO: Navigate to collector detail
                            }
                        )
                    }
                }
            }
        }
    }
}