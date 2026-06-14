package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StorageCategory(
    val name: String,
    val size: Long,
    val percentage: Float
)

data class StorageAnalysisUiState(
    val categories: List<StorageCategory> = emptyList(),
    val totalSize: Long = 0L,
    val isLoading: Boolean = false
)

@Composable
fun StorageAnalysisScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf(StorageAnalysisUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Storage Analysis") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                item {
                    StorageChartCard(categories = state.categories)
                }

                items(state.categories.size) { index ->
                    StorageCategoryRow(state.categories[index])
                }
            }
        }
    }
}

@Composable
fun StorageChartCard(categories: List<StorageCategory>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Storage Breakdown", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))
            // Chart would be drawn here
            Text("[Chart visualization]")
        }
    }
}

@Composable
fun StorageCategoryRow(category: StorageCategory) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(category.name, style = MaterialTheme.typography.titleSmall)
                LinearProgressIndicator(
                    progress = category.percentage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(top = 4.dp)
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    formatBytes(category.size),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    "${(category.percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
