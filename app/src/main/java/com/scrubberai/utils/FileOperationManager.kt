package com.scrubberai.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class FileOperationManager(private val context: Context) {

    suspend fun deleteFile(file: File): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                deleteFileScoped(file)
            } else {
                file.delete()
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun deleteFileScoped(file: File): Boolean {
        return try {
            val uri = getUriForFile(file)
            context.contentResolver.delete(uri, null, null) > 0
        } catch (e: Exception) {
            file.delete()
        }
    }

    private fun getUriForFile(file: File): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        } else {
            Uri.fromFile(file)
        }
    }

    suspend fun deleteMultipleFiles(files: List<File>): Int = withContext(Dispatchers.IO) {
        var deletedCount = 0
        files.forEach { file ->
            if (deleteFile(file)) {
                deletedCount++
            }
        }
        return@withContext deletedCount
    }
}
