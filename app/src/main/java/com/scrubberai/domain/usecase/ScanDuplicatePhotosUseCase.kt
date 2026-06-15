package com.scrubberai.domain.usecase

import com.scrubberai.data.scanner.DuplicatePhotoScanner
import com.scrubberai.domain.model.DuplicateGroup
import javax.inject.Inject

class ScanDuplicatePhotosUseCase @Inject constructor(
    private val scanner: DuplicatePhotoScanner
) {
    suspend fun execute(): List<DuplicateGroup> = scanner.scanDuplicates()
}
