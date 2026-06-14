package com.scrubberai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recycle_bin")
data class RecycleBinEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val originalPath: String,
    val binPath: String,
    val size: Long,
    val deletedAt: Long
)
