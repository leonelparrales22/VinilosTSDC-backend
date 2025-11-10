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
import androidx.compose.ui.res.painterResource
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
                Spacer(modifier = Modifier.height(8.dp))
                // Imagen circular con placeholder
                Card(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(40.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEDEDED))
                ) {
                    AsyncImage(
                        model = artist.image,
                        contentDescription = "Foto del artista",
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = android.R.drawable.ic_menu_report_image),
                        error = painterResource(id = android.R.drawable.ic_menu_report_image)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Spacer(modifier = Modifier.height(16.dp))
                // Fila: número de álbumes y favorito
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${artist.albums?.size ?: 0} Álbumes", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Lista de álbumes
                artist.albums?.forEach { album ->
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
                            Text("\u266B ${album.name} (Año ${album.releaseDate.take(4)})", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
