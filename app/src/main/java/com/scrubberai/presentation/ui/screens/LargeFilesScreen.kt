package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class LargeFileUiState(
    val isScanning: Boolean = false,
    val files: List<LargeFileItem> = emptyList(),
    val totalSpace: Long = 0L,
    val selectedCount: Int = 0
)

data class LargeFileItem(
    val path: String,
    val name: String,
    val size: Long,
    val isSelected: Boolean = false
)

@Composable
fun LargeFilesScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf(LargeFileUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Large Files") },
                actions = {
                    if (state.selectedCount > 0) {
                        IconButton(onClick = { /* Delete action */ }) {
                            Icon(Icons.Default.Delete, "Delete")
                        }
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (state.isScanning) {
                item {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else {
                items(state.files) { item ->
                    FileItemRow(item) { selected ->
                        state = state.copy(
                            files = state.files.map {
                                if (it.path == item.path) it.copy(isSelected = selected) else it
                            },
                            selectedCount = state.files.count { it.isSelected }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FileItemRow(item: LargeFileItem, onSelect: (Boolean) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = item.isSelected, onCheckedChange = onSelect)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(item.name, style = MaterialTheme.typography.titleSmall)
                Text(
                    formatBytes(item.size),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun formatBytes(bytes: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB")
    var size = bytes.toDouble()
    var unitIndex = 0
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }
    return "%.2f %s".format(size, units[unitIndex])
}
