package com.scrubberai.data.scanner

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class EmptyFolderInfo(
    val path: String,
    val displayName: String
)

class EmptyFolderScanner {

    suspend fun scanEmptyFolders(): List<EmptyFolderInfo> = withContext(Dispatchers.IO) {
        val emptyFolders = mutableListOf<EmptyFolderInfo>()
        val root = File("/storage/emulated/0")

        root.walk().forEach { file ->
            if (file.isDirectory && isEmpty(file) && !isSystemFolder(file)) {
                emptyFolders.add(
                    EmptyFolderInfo(
                        path = file.absolutePath,
                        displayName = file.name
                    )
                )
            }
        }

        return@withContext emptyFolders
    }

    private fun isEmpty(directory: File): Boolean {
        val files = directory.listFiles() ?: return true
        return files.isEmpty()
    }

    private fun isSystemFolder(file: File): Boolean {
        val path = file.absolutePath
        return path.contains("/Android/") || path.startsWith("/storage/emulated/0/.")
    }
}
