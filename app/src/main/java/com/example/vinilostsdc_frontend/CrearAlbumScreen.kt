package com.example.vinilostsdc_frontend.presentation.screen

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearAlbumScreen(
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: AlbumViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AlbumViewModelFactory())
) {
    var portada by remember { mutableStateOf(TextFieldValue()) }
    var nombre by remember { mutableStateOf(TextFieldValue()) }
    var fecha by remember { mutableStateOf(TextFieldValue()) }
    var generoExpanded by remember { mutableStateOf(false) }
    var generoSeleccionado by remember { mutableStateOf("") }
    val generos = listOf("Rock", "Pop", "Jazz", "Clasica", "Funk")
    var tracks by remember { mutableStateOf(TextFieldValue()) }
    var descripcion by remember { mutableStateOf(TextFieldValue()) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Observe album creation result
    LaunchedEffect(uiState.albumCreated) {
        if (uiState.albumCreated) {
            viewModel.clearAlbumCreated()
            onSave()
        }
    }

    // Show error message if any
    uiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            // You can show a snackbar or toast here
            viewModel.clearError()
        }
    }

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
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isCreatingAlbum
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del Álbum") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isCreatingAlbum
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha de Lanzamiento (YYYY-MM-DD)") },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreatingAlbum
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
                        .clickable { if (!uiState.isCreatingAlbum) generoExpanded = true },
                    readOnly = true,
                    enabled = !uiState.isCreatingAlbum
                )
                DropdownMenu(
                    expanded = generoExpanded && !uiState.isCreatingAlbum, 
                    onDismissRequest = { generoExpanded = false }
                ) {
                    generos.forEach { g ->
                        DropdownMenuItem(
                            text = { Text(g) }, 
                            onClick = { 
                                generoSeleccionado = g
                                generoExpanded = false 
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                enabled = !uiState.isCreatingAlbum
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (nombre.text.isNotBlank() && 
                        portada.text.isNotBlank() && 
                        fecha.text.isNotBlank() && 
                        generoSeleccionado.isNotBlank() && 
                        descripcion.text.isNotBlank()) {
                        
                        viewModel.createAlbum(
                            name = nombre.text,
                            cover = portada.text,
                            releaseDate = fecha.text,
                            description = descripcion.text,
                            genre = generoSeleccionado
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEEEEE)),
                enabled = !uiState.isCreatingAlbum && 
                         nombre.text.isNotBlank() && 
                         portada.text.isNotBlank() && 
                         fecha.text.isNotBlank() && 
                         generoSeleccionado.isNotBlank() && 
                         descripcion.text.isNotBlank()
            ) {
                if (uiState.isCreatingAlbum) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black
                    )
                } else {
                    Text(text = "Guardar", color = Color.Black)
                }
            }

            // Error message display
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}
