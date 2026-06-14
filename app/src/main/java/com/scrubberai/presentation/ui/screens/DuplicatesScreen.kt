package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun DuplicatesScreen(onBack: () -> Unit) {
    Scaffold(topBar = { TopAppBar(title = { Text("Duplicates") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            item { Text("Duplicate groups will appear here") }
        }
    }
}
