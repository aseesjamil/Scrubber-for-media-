package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scrubberai.presentation.viewmodel.HomeViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(onNavigateDuplicates: () -> Unit, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Scrubber AI") }) }) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .fillMaxSize()
            .padding(16.dp)) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total: ${state.totalStorageReadable}")
                    Text("Used: ${state.usedStorageReadable}")
                    Text("Free: ${state.freeStorageReadable}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { viewModel.scanDuplicates() }) { Text("Scan Duplicates") }
                Button(onClick = { viewModel.scanLargeFiles() }) { Text("Scan Large Files") }
                Button(onClick = { viewModel.scanJunkFiles() }) { Text("Scan Junk Files") }
                Button(onClick = { viewModel.scanApkFiles() }) { Text("Scan APK Files") }
                Button(onClick = { viewModel.scanEmptyFolders() }) { Text("Empty Folder Scan") }
                Button(onClick = { viewModel.fullCleanup() }) { Text("Full Cleanup Scan") }
                Button(onClick = onNavigateDuplicates) { Text("Open Duplicates Screen") }
            }
        }
    }
}
