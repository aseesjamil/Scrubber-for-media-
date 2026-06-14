package com.scrubberai.di

import android.content.Context
import androidx.room.Room
import com.scrubberai.data.local.database.ScrubberDatabase
import com.scrubberai.data.local.dao.FileDao
import com.scrubberai.data.repository.FileRepository
import com.scrubberai.utils.RecycleBinManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ScrubberDatabase {
        return Room.databaseBuilder(context, ScrubberDatabase::class.java, "scrubber_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFileDao(db: ScrubberDatabase): FileDao = db.fileDao()

    @Provides
    @Singleton
    fun provideRecycleBinManager(@ApplicationContext context: Context): RecycleBinManager = RecycleBinManager(context)

    @Provides
    @Singleton
    fun provideFileRepository(fileDao: FileDao, recycleBinManager: RecycleBinManager): FileRepository {
        return FileRepository(fileDao, recycleBinManager)
    }
}
