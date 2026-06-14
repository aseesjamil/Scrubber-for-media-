package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class RecycleBinItem(
    val id: Long,
    val originalPath: String,
    val displayName: String,
    val size: Long,
    val deletedAt: Long
)

data class RecycleBinUiState(
    val items: List<RecycleBinItem> = emptyList(),
    val totalSpace: Long = 0L
)

@Composable
fun RecycleBinScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf(RecycleBinUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recycle Bin") },
                actions = {
                    if (state.items.isNotEmpty()) {
                        TextButton(onClick = { /* Empty bin */ }) {
                            Text("Empty All")
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
            if (state.items.isEmpty()) {
                item {
                    Text("Recycle bin is empty")
                }
            } else {
                items(state.items) { item ->
                    RecycleBinItemRow(
                        item,
                        onRestore = { /* Restore */ },
                        onDelete = { /* Permanently delete */ }
                    )
                }
            }
        }
    }
}

@Composable
fun RecycleBinItemRow(
    item: RecycleBinItem,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(item.displayName, style = MaterialTheme.typography.titleSmall)
            Text(
                formatBytes(item.size),
                style = MaterialTheme.typography.bodySmall
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRestore,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Restore, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Restore")
                }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Delete, null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}
