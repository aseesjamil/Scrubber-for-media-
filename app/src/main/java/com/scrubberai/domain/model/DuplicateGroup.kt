package com.scrubberai.domain.model

data class DuplicateGroup(
    val hash: String,
    val files: List<FileInfo>,
    val totalSize: Long
) {
    fun getSpaceSavings(): Long = totalSize * (files.size - 1)
}

data class FileInfo(
    val path: String,
    val name: String,
    val size: Long,
    val mimeType: String?,
    val lastModified: Long,
    val isSelected: Boolean = false
)
