package com.scrubberai.domain.usecase

import com.scrubberai.data.scanner.LargeFileScanner
import com.scrubberai.domain.model.ScannableItem
import javax.inject.Inject

class ScanLargeFilesUseCase @Inject constructor(
    private val scanner: LargeFileScanner
) {
    suspend fun execute(minSizeBytes: Long): List<ScannableItem> = scanner.scanLargeFiles(minSizeBytes)
}
