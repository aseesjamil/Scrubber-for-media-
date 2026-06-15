package com.scrubberai.domain.usecase

import com.scrubberai.data.repository.FileRepository
import java.io.File
import javax.inject.Inject

class DeleteFilesUseCase @Inject constructor(
    private val repository: FileRepository
) {
    suspend fun execute(files: List<File>): Int {
        var deletedCount = 0
        files.forEach { file ->
            if (repository.moveToRecycleBin(file)) {
                deletedCount++
            }
        }
        return deletedCount
    }
}
