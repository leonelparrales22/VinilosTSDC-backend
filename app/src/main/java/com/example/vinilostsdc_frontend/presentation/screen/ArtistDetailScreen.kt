package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vinilostsdc_frontend.presentation.viewmodel.ArtistViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.ArtistViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: Int,
    onBack: () -> Unit,
    viewModel: ArtistViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ArtistViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(artistId) {
        viewModel.getArtistById(artistId)
    }

    val artist = uiState.selectedArtist

    Scaffold(
        containerColor = Color(0xFFF8F4E6),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = artist?.name ?: "Artista",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
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
        if (artist == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("No se encontró el artista")
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
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.size(120.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    AsyncImage(
                        model = artist.image,
                        contentDescription = "Foto del artista",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "URL de imagen: ${artist.image}",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
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
                        Text(artist.description, fontWeight = FontWeight.Medium)
                    }
                }
                if (artist.birthDate != null) {
                    Spacer(modifier = Modifier.height(8.dp))
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
                            Text("Fecha de nacimiento: ${artist.birthDate}", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
