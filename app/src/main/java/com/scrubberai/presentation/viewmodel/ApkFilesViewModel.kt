package com.scrubberai.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.scrubberai.data.repository.FileRepository
import com.scrubberai.data.scanner.ApkScanner
import com.scrubberai.domain.model.ScannableItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ApkFilesUiState(
    val isScanning: Boolean = false,
    val apkFiles: List<ScannableItem> = emptyList(),
    val selectedCount: Int = 0,
    val totalSpaceSelected: Long = 0L
)

@HiltViewModel
class ApkFilesViewModel @Inject constructor(
    application: Application,
    private val repository: FileRepository,
    private val scanner: ApkScanner
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(ApkFilesUiState())
    val uiState: StateFlow<ApkFilesUiState> = _uiState

    fun scanApkFiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            try {
                val apks = scanner.scanApkFiles()
                _uiState.value = _uiState.value.copy(
                    apkFiles = apks,
                    isScanning = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isScanning = false)
            }
        }
    }

    fun toggleApkSelection(filePath: String) {
        viewModelScope.launch {
            val updatedApks = _uiState.value.apkFiles.map { apk ->
                if (apk.path == filePath) apk.copy(hash = if (apk.hash == null) "selected" else null) else apk
            }
            val selected = updatedApks.count { it.hash == "selected" }
            val totalSpace = updatedApks.filter { it.hash == "selected" }.sumOf { it.size }
            _uiState.value = _uiState.value.copy(
                apkFiles = updatedApks,
                selectedCount = selected,
                totalSpaceSelected = totalSpace
            )
        }
    }

    fun deleteSelectedApks() {
        viewModelScope.launch {
            val apksToDelete = _uiState.value.apkFiles
                .filter { it.hash == "selected" }
                .map { java.io.File(it.path) }

            apksToDelete.forEach { repository.moveToRecycleBin(it) }

            val remaining = _uiState.value.apkFiles.filter { it.hash != "selected" }
            _uiState.value = _uiState.value.copy(
                apkFiles = remaining,
                selectedCount = 0,
                totalSpaceSelected = 0L
            )
        }
    }
}
