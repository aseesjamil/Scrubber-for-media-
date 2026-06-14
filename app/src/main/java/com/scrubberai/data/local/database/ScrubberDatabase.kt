package com.scrubberai.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scrubberai.data.local.dao.FileDao
import com.scrubberai.data.local.entity.FileEntity
import com.scrubberai.data.local.entity.RecycleBinEntry

@Database(entities = [FileEntity::class, RecycleBinEntry::class], version = 1)
abstract class ScrubberDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
}
