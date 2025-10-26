package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoleSelectionScreen(
    onVisitante: () -> Unit,
    onColeccionista: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabItems = listOf("Visitante", "Coleccionista")

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFF7F3EA)) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onVisitante()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Visitante"
                        )
                    },
                    label = { Text("Visitante") },
                    alwaysShowLabel = true
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onColeccionista()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = "Coleccionista"
                        )
                    },
                    label = { Text("Coleccionista") },
                    alwaysShowLabel = true
                )
            }
        },
        containerColor = Color(0xFFF7F3EA)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Vinilos",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black
            )
        }
    }
}
