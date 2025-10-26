package com.example.vinilostsdc_frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAlbumScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    var portada by remember { mutableStateOf(TextFieldValue()) }
    var nombre by remember { mutableStateOf(TextFieldValue()) }
    var fecha by remember { mutableStateOf(TextFieldValue()) }
    var generoExpanded by remember { mutableStateOf(false) }
    var generoSeleccionado by remember { mutableStateOf("") }
    val generos = listOf("Rock", "Pop", "Jazz", "Clasica", "Funk")
    var tracks by remember { mutableStateOf(TextFieldValue()) }
    var descripcion by remember { mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Crear álbum", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF424242))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = portada,
                onValueChange = { portada = it },
                label = { Text("Enlace de Portada (URL)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Álbum") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha de Lanzamiento") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* TODO: show date picker */ }) {
                    Icon(Icons.Default.CalendarToday, contentDescription = "Fecha")
                }
            }

            // Género musical (Dropdown)
            Box {
                OutlinedTextField(
                    value = generoSeleccionado,
                    onValueChange = { generoSeleccionado = it },
                    label = { Text("Género Musical") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { generoExpanded = true },
                    readOnly = true
                )
                DropdownMenu(expanded = generoExpanded, onDismissRequest = { generoExpanded = false }) {
                    generos.forEach { g ->
                        DropdownMenuItem(text = { Text(g) }, onClick = { generoSeleccionado = g; generoExpanded = false })
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = tracks,
                    onValueChange = { tracks = it },
                    label = { Text("Agregar Tracks") },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* TODO: open track picker or add action */ }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Agregar")
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE))
            ) {
                Text(text = "Guardar", color = Color.Black)
            }
        }
    }
}
