package com.scrubberai.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.scrubberai.data.repository.FileRepository
import com.scrubberai.data.scanner.JunkFileScanner
import com.scrubberai.domain.model.ScannableItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JunkFilesUiState(
    val isScanning: Boolean = false,
    val files: List<ScannableItem> = emptyList(),
    val selectedCount: Int = 0,
    val totalSpaceSelected: Long = 0L
)

@HiltViewModel
class JunkFilesViewModel @Inject constructor(
    application: Application,
    private val repository: FileRepository,
    private val scanner: JunkFileScanner
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(JunkFilesUiState())
    val uiState: StateFlow<JunkFilesUiState> = _uiState

    fun scanJunkFiles() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            try {
                val files = scanner.scanJunkFiles()
                _uiState.value = _uiState.value.copy(
                    files = files,
                    isScanning = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isScanning = false)
            }
        }
    }

    fun toggleFileSelection(filePath: String) {
        viewModelScope.launch {
            val updatedFiles = _uiState.value.files.map { file ->
                if (file.path == filePath) file.copy(hash = if (file.hash == null) "selected" else null) else file
            }
            val selected = updatedFiles.count { it.hash == "selected" }
            val totalSpace = updatedFiles.filter { it.hash == "selected" }.sumOf { it.size }
            _uiState.value = _uiState.value.copy(
                files = updatedFiles,
                selectedCount = selected,
                totalSpaceSelected = totalSpace
            )
        }
    }

    fun deleteSelectedFiles() {
        viewModelScope.launch {
            val filesToDelete = _uiState.value.files
                .filter { it.hash == "selected" }
                .map { java.io.File(it.path) }

            filesToDelete.forEach { repository.moveToRecycleBin(it) }

            val remaining = _uiState.value.files.filter { it.hash != "selected" }
            _uiState.value = _uiState.value.copy(
                files = remaining,
                selectedCount = 0,
                totalSpaceSelected = 0L
            )
        }
    }
}
