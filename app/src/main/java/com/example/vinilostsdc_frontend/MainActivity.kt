package com.example.vinilostsdc_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vinilostsdc_frontend.presentation.screen.AlbumListScreen
import com.example.vinilostsdc_frontend.presentation.screen.ArtistListScreen
import com.example.vinilostsdc_frontend.presentation.screen.CollectorListScreen
import com.example.vinilostsdc_frontend.presentation.screen.CrearAlbumScreen
import com.example.vinilostsdc_frontend.presentation.screen.VisitanteMenuScreen
import com.example.vinilostsdc_frontend.presentation.screen.RoleSelectionScreen
import com.example.vinilostsdc_frontend.presentation.screen.BlankScreen
import com.example.vinilostsdc_frontend.ui.theme.VinilosTSDCfrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VinilosTSDCfrontendTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "role_selection") {
                    composable("role_selection") {
                        RoleSelectionScreen(
                            onVisitante = { navController.navigate("main") },
                            onColeccionista = { navController.navigate("coleccionista") }
                        )
                    }
                    composable("main") {
                        VisitanteMenuScreen(navController)
                    }
                    composable("coleccionista") {
                        BlankScreen()
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
                    composable("crear_album") {
                        CrearAlbumScreen(
                            onBack = { navController.popBackStack() },
                            onSave = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}