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

data class JunkFileItem(
    val path: String,
    val name: String,
    val type: String,
    val size: Long,
    val isSelected: Boolean = false
)

data class JunkFilesUiState(
    val isScanning: Boolean = false,
    val files: List<JunkFileItem> = emptyList(),
    val totalSpace: Long = 0L,
    val selectedCount: Int = 0
)

@Composable
fun JunkFilesScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf(JunkFilesUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Junk Files") },
                actions = {
                    if (state.selectedCount > 0) {
                        Button(onClick = { /* Clean action */ }) {
                            Text("Clean (${state.selectedCount})")
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
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Scanning for junk files...")
                        }
                    }
                }
            } else if (state.files.isEmpty()) {
                item {
                    Text("No junk files found")
                }
            } else {
                items(state.files) { item ->
                    JunkFileItemRow(item) { selected ->
                        state = state.copy(
                            files = state.files.map {
                                if (it.path == item.path) it.copy(isSelected = selected) else it
                            },
                            selectedCount = if (selected) state.selectedCount + 1 else state.selectedCount - 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun JunkFileItemRow(item: JunkFileItem, onSelect: (Boolean) -> Unit) {
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
                Row {
                    Text("${item.type} • ", style = MaterialTheme.typography.bodySmall)
                    Text(formatBytes(item.size), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
