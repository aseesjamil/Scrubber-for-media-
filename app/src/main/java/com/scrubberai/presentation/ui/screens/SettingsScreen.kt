package com.scrubberai.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class SettingsUiState(
    val autoScanEnabled: Boolean = false,
    val darkModeEnabled: Boolean = false,
    val hashAlgorithm: String = "SHA-256",
    val binRetentionDays: Int = 30,
    val largeFileThresholdMb: Int = 50
)

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    var state by remember { mutableStateOf(SettingsUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Scan Settings", style = MaterialTheme.typography.titleMedium)
            }

            item {
                SwitchRow(
                    label = "Auto-Scan",
                    checked = state.autoScanEnabled
                ) { checked ->
                    state = state.copy(autoScanEnabled = checked)
                }
            }

            item {
                Divider()
            }

            item {
                Text("Display Settings", style = MaterialTheme.typography.titleMedium)
            }

            item {
                SwitchRow(
                    label = "Dark Mode",
                    checked = state.darkModeEnabled
                ) { checked ->
                    state = state.copy(darkModeEnabled = checked)
                }
            }

            item {
                Divider()
            }

            item {
                Text("Storage Settings", style = MaterialTheme.typography.titleMedium)
            }

            item {
                SliderSetting(
                    label = "Large File Threshold (MB)",
                    value = state.largeFileThresholdMb.toFloat(),
                    onValueChange = { state = state.copy(largeFileThresholdMb = it.toInt()) }
                )
            }

            item {
                SliderSetting(
                    label = "Recycle Bin Retention (Days)",
                    value = state.binRetentionDays.toFloat(),
                    onValueChange = { state = state.copy(binRetentionDays = it.toInt()) }
                )
            }
        }
    }
}

@Composable
fun SwitchRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SliderSetting(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(label)
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 10f..1000f,
            steps = 9
        )
        Text(value.toInt().toString(), style = MaterialTheme.typography.bodySmall)
    }
}
