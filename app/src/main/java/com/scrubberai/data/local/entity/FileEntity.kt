package com.scrubberai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "files")
data class FileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val path: String,
    val displayName: String?,
    val size: Long,
    val mimeType: String?,
    val lastModified: Long,
    val hash: String?
)
