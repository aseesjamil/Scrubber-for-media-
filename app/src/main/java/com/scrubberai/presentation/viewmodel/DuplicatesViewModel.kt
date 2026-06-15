package com.scrubberai.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.scrubberai.data.repository.FileRepository
import com.scrubberai.data.scanner.DuplicatePhotoScanner
import com.scrubberai.domain.model.DuplicateGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DuplicatesUiState(
    val isScanning: Boolean = false,
    val duplicateGroups: List<DuplicateGroup> = emptyList(),
    val totalSpaceToSave: Long = 0L,
    val selectedFilesCount: Int = 0
)

@HiltViewModel
class DuplicatesViewModel @Inject constructor(
    application: Application,
    private val repository: FileRepository,
    private val photoScanner: DuplicatePhotoScanner
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DuplicatesUiState())
    val uiState: StateFlow<DuplicatesUiState> = _uiState

    fun scanForDuplicatePhotos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true)
            try {
                val groups = photoScanner.scanDuplicates()
                val totalSpace = groups.sumOf { it.getSpaceSavings() }
                _uiState.value = _uiState.value.copy(
                    duplicateGroups = groups,
                    totalSpaceToSave = totalSpace,
                    isScanning = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isScanning = false)
            }
        }
    }

    fun toggleGroupSelection(groupIndex: Int) {
        viewModelScope.launch {
            val groups = _uiState.value.duplicateGroups.toMutableList()
            val group = groups[groupIndex]
            val selected = group.files.count { it.isSelected }
            val newGroup = group.copy(
                files = group.files.mapIndexed { index, file ->
                    if (index > 0) file.copy(isSelected = !file.isSelected) else file
                }
            )
            groups[groupIndex] = newGroup
            _uiState.value = _uiState.value.copy(
                duplicateGroups = groups,
                selectedFilesCount = groups.sumOf { g -> g.files.count { it.isSelected } }
            )
        }
    }

    fun deleteSelectedDuplicates() {
        viewModelScope.launch {
            val filesToDelete = _uiState.value.duplicateGroups
                .flatMap { it.files.filter { f -> f.isSelected } }
                .map { java.io.File(it.path) }

            var deletedCount = 0
            filesToDelete.forEach { file ->
                if (repository.moveToRecycleBin(file)) {
                    deletedCount++
                }
            }

            val remaining = _uiState.value.duplicateGroups
                .map { group ->
                    group.copy(files = group.files.filter { !it.isSelected })
                }
                .filter { it.files.size > 1 }

            _uiState.value = _uiState.value.copy(
                duplicateGroups = remaining,
                selectedFilesCount = 0,
                totalSpaceToSave = remaining.sumOf { it.getSpaceSavings() }
            )
        }
    }
}
