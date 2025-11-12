package com.example.vinilostsdc_frontend.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vinilostsdc_frontend.data.model.Collector
import com.example.vinilostsdc_frontend.presentation.viewmodel.CollectorViewModel
import com.example.vinilostsdc_frontend.presentation.viewmodel.CollectorViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectorListScreen(
    onBack: () -> Unit,
    onCollectorClick: (Collector) -> Unit,
    viewModel: CollectorViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = CollectorViewModelFactory())
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCollectors()
    }

    Scaffold(
        containerColor = Color(0xFFF8F4E6),
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Coleccionistas",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            var searchText by remember { mutableStateOf("") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Nombre coleccionista") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { /* Implementar búsqueda si es necesario */ },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEDEDED))
                ) {
                    Text("Buscar", color = Color.Black)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(16.dp)
                            )
                            Button(
                                onClick = {
                                    viewModel.clearError()
                                    viewModel.getCollectors()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                            ) {
                                Text("Reintentar", color = Color.White)
                            }
                        }
                    }
                    uiState.collectors.isEmpty() -> {
                        Text(
                            text = "No hay coleccionistas disponibles",
                            modifier = Modifier.align(Alignment.Center),
                            color = Color.Black
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.collectors.filter { it.name.contains(searchText, ignoreCase = true) }) { collector ->
                                CollectorItem(
                                    collector = collector,
                                    onClick = { onCollectorClick(collector) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CollectorItem(
    collector: Collector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDEDED)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = collector.name,
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Persona",
                tint = Color(0xFF3F51B5),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}