package com.scrubberai.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.scrubberai.data.repository.FileRepository
import com.scrubberai.utils.FileUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import javax.inject.Inject

data class HomeUiState(
    val totalStorageReadable: String = "--",
    val usedStorageReadable: String = "--",
    val freeStorageReadable: String = "--",
    val storagePercentage: Int = 0,
    val photosCount: Int = 0,
    val videosCount: Int = 0,
    val scannedFiles: Int = 0
)

class HomeViewModel @Inject constructor(
    application: Application,
    private val repository: FileRepository
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        refreshStorageInfo()
    }

    fun refreshStorageInfo() {
        viewModelScope.launch {
            val info = FileUtils.getStorageInfo(getApplication())
            _uiState.value = _uiState.value.copy(
                totalStorageReadable = info.totalReadable,
                usedStorageReadable = info.usedReadable,
                freeStorageReadable = info.freeReadable,
                storagePercentage = info.percentUsed,
                photosCount = info.photosCount,
                videosCount = info.videosCount
            )
        }
    }

    fun scanDuplicates() {
        viewModelScope.launch {
            // Kick off background work - left as stub for WorkManager integration
        }
    }

    fun scanLargeFiles() {
        viewModelScope.launch {
            // Stub
        }
    }

    fun scanJunkFiles() {
        viewModelScope.launch {
            // Stub
        }
    }

    fun scanApkFiles() {
        viewModelScope.launch {
            // Stub
        }
    }

    fun scanEmptyFolders() {
        viewModelScope.launch {
            // Stub
        }
    }

    fun fullCleanup() {
        viewModelScope.launch {
            // Stub
        }
    }

    private fun humanReadableByteCountSI(bytes: Long): String {
        var b = bytes
        if (b < 1000) return "$b B"
        val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
        var value = b.toDouble()
        while (value >= 1000 && ci.current().toString().isNotEmpty()) {
            value /= 1000
            ci.next()
        }
        return "${"%.1f".format(value)} ${ci.current()}B"
    }
}
