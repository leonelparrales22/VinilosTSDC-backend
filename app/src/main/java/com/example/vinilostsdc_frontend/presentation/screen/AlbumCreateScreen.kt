package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.vinilostsdc_frontend.data.model.Track
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModelFactory
import kotlinx.coroutines.launch
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumCreateScreen(
    navController: NavHostController,
    viewModel: AlbumViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AlbumViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Album fields
    var coverUrl by rememberSaveable { mutableStateOf("") }
    var albumName by rememberSaveable { mutableStateOf("") }
    var releaseDate by rememberSaveable { mutableStateOf("") }
    var genre by rememberSaveable { mutableStateOf("") }
    var recordLabel by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    // Tracks
    var tracks by rememberSaveable { mutableStateOf(listOf<Track>()) }
    var newTrackName by rememberSaveable { mutableStateOf("") }
    var newTrackMin by rememberSaveable { mutableStateOf("") }
    var newTrackSec by rememberSaveable { mutableStateOf("") }

    // Date picker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Validation errors
    var nameError by rememberSaveable { mutableStateOf<String?>(null) }
    var coverError by rememberSaveable { mutableStateOf<String?>(null) }
    var releaseDateError by rememberSaveable { mutableStateOf<String?>(null) }
    var genreError by rememberSaveable { mutableStateOf<String?>(null) }
    var descriptionError by rememberSaveable { mutableStateOf<String?>(null) }

    // Validation functions
    fun isValidUrl(url: String): Boolean {
        return url.startsWith("http://") || url.startsWith("https://")
    }

    fun isValidDuration(duration: String): Boolean {
        val regex = Regex("""^\d{1,2}:\d{2}$""")
        return regex.matches(duration)
    }

    fun isValidDate(date: String): Boolean {
        val regex = Regex("""^\d{4}-\d{2}-\d{2}$""")
        return regex.matches(date)
    }

    fun validateFields(): Boolean {
        nameError = if (albumName.isBlank()) "El nombre es obligatorio" else null
        coverError = if (coverUrl.isBlank()) "La URL de portada es obligatoria" else if (!isValidUrl(coverUrl)) "URL inválida" else null
        releaseDateError = if (releaseDate.isBlank()) "La fecha de lanzamiento es obligatoria" else if (!isValidDate(releaseDate)) "Formato: YYYY-MM-DD" else null
        genreError = if (genre.isBlank()) "El género es obligatorio" else null
        descriptionError = if (description.isBlank()) "La descripción es obligatoria" else null
        return nameError == null && coverError == null && releaseDateError == null && genreError == null && descriptionError == null
    }

    // Handle album creation success
    LaunchedEffect(uiState.albumCreated) {
        if (uiState.albumCreated) {
            // After album created, add tracks if any
            val albumId = uiState.selectedAlbum?.id ?: return@LaunchedEffect
            tracks.forEach { track ->
                viewModel.addTrackToAlbum(albumId, track.name, track.duration)
            }
            // Navigate back or show success
            navController.popBackStack()
        }
    }

    // Handle track addition success
    LaunchedEffect(uiState.trackAdded) {
        if (uiState.trackAdded) {
            viewModel.clearTrackAdded()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8F4E6),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crear álbum",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.semantics { contentDescription = "Volver atrás" }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEDEDED)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Información del Álbum",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.semantics { contentDescription = "Sección de información del álbum" }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = coverUrl,
                    onValueChange = { if (it.length <= 500) { coverUrl = it; coverError = null } },
                    label = { Text("Enlace de portada (URL)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("coverUrlField")
                        .semantics { contentDescription = "Campo para ingresar la URL de la portada del álbum" },
                    singleLine = true,
                    isError = coverError != null,
                    supportingText = coverError?.let { { Text(it) } }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = albumName,
                    onValueChange = { if (it.length <= 100) { albumName = it; nameError = null } },
                    label = { Text("Nombre del álbum") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("albumNameField")
                        .semantics { contentDescription = "Campo para ingresar el nombre del álbum" },
                    singleLine = true,
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = releaseDate,
                    onValueChange = { }, // Read-only
                    label = { Text("Fecha de lanzamiento (YYYY-MM-DD)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("releaseDateField")
                        .semantics { contentDescription = "Campo para seleccionar la fecha de lanzamiento" },
                    singleLine = true,
                    readOnly = true,
                    isError = releaseDateError != null,
                    supportingText = releaseDateError?.let { { Text(it) } },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = genre,
                    onValueChange = { if (it.length <= 50) { genre = it; genreError = null } },
                    label = { Text("Género musical") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("genreField")
                        .semantics { contentDescription = "Campo para ingresar el género musical" },
                    singleLine = true,
                    isError = genreError != null,
                    supportingText = genreError?.let { { Text(it) } }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = recordLabel,
                    onValueChange = { if (it.length <= 100) recordLabel = it },
                    label = { Text("Sello discográfico (opcional)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("recordLabelField")
                        .semantics { contentDescription = "Campo opcional para ingresar el sello discográfico" },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = description,
                    onValueChange = { if (it.length <= 500) { description = it; descriptionError = null } },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("descriptionField")
                        .semantics { contentDescription = "Campo para ingresar la descripción del álbum" },
                    maxLines = 4,
                    isError = descriptionError != null,
                    supportingText = descriptionError?.let { { Text(it) } }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tracks del Álbum",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.semantics { contentDescription = "Sección de tracks del álbum" }
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Add track section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newTrackName,
                                onValueChange = { if (it.length <= 100) newTrackName = it },
                                label = { Text("Título") },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(64.dp)
                                    .testTag("trackNameField")
                                    .semantics { contentDescription = "Campo para ingresar el nombre del track" },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OutlinedTextField(
                                value = newTrackMin,
                                onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2) newTrackMin = it },
                                label = { Text("Min") },
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(64.dp)
                                    .testTag("trackMinField")
                                    .semantics { contentDescription = "Campo para ingresar los minutos de la duración" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Text(":", modifier = Modifier.padding(horizontal = 4.dp))
                            OutlinedTextField(
                                value = newTrackSec,
                                onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 2 && (it.toIntOrNull() ?: 0) <= 59) newTrackSec = it },
                                label = { Text("Seg") },
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(64.dp)
                                    .testTag("trackSecField")
                                    .semantics { contentDescription = "Campo para ingresar los segundos de la duración" },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (newTrackName.isNotBlank() && newTrackMin.isNotBlank() && newTrackSec.isNotBlank()) {
                                        val duration = "${newTrackMin.padStart(2, '0')}:${newTrackSec.padStart(2, '0')}"
                                        tracks = tracks + Track(name = newTrackName, duration = duration)
                                        newTrackName = ""
                                        newTrackMin = ""
                                        newTrackSec = ""
                                    }
                                },
                                modifier = Modifier.semantics { contentDescription = "Botón para agregar track" }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        // List of tracks
                        tracks.forEach { track ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = track.name,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "Duración: ${track.duration}",
                                            fontSize = 14.sp,
                                            color = Color.Gray
                                        )
                                    }
                                    IconButton(
                                        onClick = { tracks = tracks.filter { it != track } },
                                        modifier = Modifier.semantics { contentDescription = "Botón para eliminar track" }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (validateFields()) {
                            scope.launch {
                                viewModel.createAlbum(
                                    name = albumName,
                                    cover = coverUrl,
                                    releaseDate = releaseDate,
                                    description = description,
                                    genre = genre,
                                    recordLabel = recordLabel.takeIf { it.isNotBlank() }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(44.dp)
                        .testTag("saveButton")
                        .semantics { contentDescription = "Botón para guardar el álbum" },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEDED)),
                    enabled = !uiState.isCreatingAlbum
                ) {
                    if (uiState.isCreatingAlbum) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    } else {
                        Text("Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Error message
            uiState.errorMessage?.let { error ->
                item {
                    Text(
                        text = error,
                        color = Color.Red,
                        modifier = Modifier.semantics { contentDescription = "Mensaje de error" }
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        releaseDate = formatter.format(date)
                        releaseDateError = null
                    }
                    showDatePicker = false
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
