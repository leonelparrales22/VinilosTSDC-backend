package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vinilostsdc_frontend.data.model.Collector
import com.example.vinilostsdc_frontend.data.model.Album
import com.example.vinilostsdc_frontend.presentation.viewmodel.CollectorViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.CollectorViewModelFactory
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModelFactory
import coil.compose.AsyncImage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.vector.rememberVectorPainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorDetailScreen(
    collectorId: Int,
    onBack: () -> Unit,
    viewModel: CollectorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = CollectorViewModelFactory()),
    albumViewModel: AlbumViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = AlbumViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val albumUiState by albumViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(collectorId) {
        viewModel.getCollectorById(context, collectorId)
        albumViewModel.getAlbums(context)
    }

    val collector = uiState.selectedCollector
    val albums = albumUiState.albums

    Scaffold(
        containerColor = Color(0xFFF8F4E6),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = collector?.name ?: "Coleccionista",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.Black,
                            modifier = Modifier.testTag("nav_back")
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEDEDED)
                )
            )
        }
    ) { paddingValues ->
        if (collector == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("No se encontró el coleccionista")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                // Imagen circular con placeholder
                Card(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
                ) {
                    AsyncImage(
                        model = collector.image,
                        contentDescription = "Foto del coleccionista",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = rememberVectorPainter(Icons.Default.Person),
                        error = rememberVectorPainter(Icons.Default.Person)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(16.dp))
                // Información del coleccionista
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Nombre",
                                tint = Color(0xFF3F51B5),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = collector.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                modifier = Modifier.testTag("collector_name")
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Teléfono",
                                tint = Color(0xFF3F51B5),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = collector.telephone,
                                fontSize = 14.sp,
                                modifier = Modifier.testTag("collector_telephone")
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email",
                                tint = Color(0xFF3F51B5),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = collector.email,
                                fontSize = 14.sp,
                                modifier = Modifier.testTag("collector_email")
                            )
                        }
                    }
                }

                // Listado de álbumes del coleccionista
                if (collector.collectorAlbums.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Álbumes:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF3F51B5),
                        modifier = Modifier.testTag("collector_albums_title")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(modifier = Modifier.testTag("collector_albums_list")) {
                        collector.collectorAlbums.forEach { albumRef ->
                            val albumName = albums.find { it.id == albumRef.id }?.name ?: "Álbum ID: ${albumRef.id}"
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED)),
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("$albumName", fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                    Icon(
                                        imageVector = Icons.Default.Album,
                                        contentDescription = "Disco",
                                        tint = Color(0xFF3F51B5),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}