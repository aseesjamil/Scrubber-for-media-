package com.scrubberai.domain.model

data class ScanResult(
    val type: ScanType,
    val totalItemsFound: Int,
    val totalSpaceToFree: Long,
    val items: List<ScannableItem>,
    val scanDurationMs: Long
)

enum class ScanType {
    DUPLICATES,
    LARGE_FILES,
    JUNK_FILES,
    APK_FILES,
    EMPTY_FOLDERS
}

data class ScannableItem(
    val path: String,
    val displayName: String,
    val size: Long,
    val mimeType: String? = null,
    val hash: String? = null,
    val groupId: String? = null,
    val canDelete: Boolean = true
)
