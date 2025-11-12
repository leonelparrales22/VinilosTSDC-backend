package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.ui.platform.LocalContext
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import coil.ImageLoader
import coil.request.ImageRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vinilostsdc_frontend.data.model.Album
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.AlbumViewModelFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailScreen(
    albumId: Int,
    onBack: () -> Unit,
    viewModel: AlbumViewModel = androidx.lifecycle.viewmodel.compose.viewModel<AlbumViewModel>(factory = AlbumViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(albumId) {
        viewModel.getAlbumById(albumId)
    }

    val album = uiState.selectedAlbum

    val context = LocalContext.current

    // Custom Coil ImageLoader with User-Agent interceptor for Wikimedia, optimized for memory
    val imageLoader = remember {
        val client = OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request().newBuilder()
                        .header("User-Agent", "Mozilla/5.0 (Android) Coil")
                        .build()
                    return chain.proceed(request)
                }
            })
            .build()
        ImageLoader.Builder(context)
            .okHttpClient(client)
            .memoryCachePolicy(coil.request.CachePolicy.DISABLED) // Disable memory cache to reduce memory usage
            .diskCachePolicy(coil.request.CachePolicy.ENABLED) // Keep disk cache for performance
            .build()
    }

    Scaffold(
        containerColor = Color(0xFFF8F4E6),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = album?.name ?: "Álbum",
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
        if (album == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("No se encontró el álbum")
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
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(album.cover)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = "Portada",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text("${album.tracks.size} tracks", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                album.tracks.forEach { track ->
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
                            Text("\u266B ${track.name} - ${track.duration}", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}
