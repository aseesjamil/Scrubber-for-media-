package com.scrubberai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.scrubberai.data.local.entity.FileEntity
import com.scrubberai.data.local.entity.RecycleBinEntry

@Dao
interface FileDao {

    @Insert
    suspend fun insertFile(entity: FileEntity): Long

    @Query("SELECT * FROM files WHERE hash = :hash")
    suspend fun getFilesByHash(hash: String): List<FileEntity>

    @Query("SELECT * FROM files WHERE size >= :minSize ORDER BY size DESC")
    suspend fun getLargeFiles(minSize: Long): List<FileEntity>

    @Insert
    suspend fun insertRecycleEntry(entry: RecycleBinEntry): Long

    @Query("SELECT * FROM recycle_bin ORDER BY deletedAt DESC")
    suspend fun getRecycleEntries(): List<RecycleBinEntry>

    @Query("DELETE FROM recycle_bin WHERE deletedAt <= :threshold")
    suspend fun deleteOldRecycleEntries(threshold: Long)

    @Query("DELETE FROM files")
    suspend fun clearFiles()
}
