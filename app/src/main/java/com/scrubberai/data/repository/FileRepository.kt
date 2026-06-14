package com.scrubberai.data.repository

import com.scrubberai.data.local.dao.FileDao
import com.scrubberai.data.local.entity.FileEntity
import com.scrubberai.utils.RecycleBinManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileRepository(
    private val fileDao: FileDao,
    private val recycleBinManager: RecycleBinManager
) {

    suspend fun saveScannedFile(file: FileEntity) {
        withContext(Dispatchers.IO) {
            fileDao.insertFile(file)
        }
    }

    suspend fun getFilesByHash(hash: String): List<FileEntity> = withContext(Dispatchers.IO) {
        fileDao.getFilesByHash(hash)
    }

    suspend fun moveToRecycleBin(file: File): Boolean = withContext(Dispatchers.IO) {
        recycleBinManager.moveToBin(file)
    }

    suspend fun getRecycleEntries() = withContext(Dispatchers.IO) {
        fileDao.getRecycleEntries()
    }

    suspend fun purgeOldEntries(retentionMillis: Long) = withContext(Dispatchers.IO) {
        val threshold = System.currentTimeMillis() - retentionMillis
        fileDao.deleteOldRecycleEntries(threshold)
    }
}
