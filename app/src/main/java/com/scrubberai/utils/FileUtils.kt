package com.scrubberai.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File

data class StorageInfo(
    val totalBytes: Long,
    val freeBytes: Long,
    val usedBytes: Long,
    val percentUsed: Int,
    val totalReadable: String,
    val freeReadable: String,
    val usedReadable: String,
    val photosCount: Int,
    val videosCount: Int
)

object FileUtils {

    fun getStorageInfo(context: Context): StorageInfo {
        val stat = android.os.StatFs(Environment.getExternalStorageDirectory().absolutePath)
        val blockSize = stat.blockSizeLong
        val total = stat.blockCountLong * blockSize
        val free = stat.availableBlocksLong * blockSize
        val used = total - free
        val percent = if (total == 0L) 0 else ((used * 100) / total).toInt()

        val photos = countMedia(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        val videos = countMedia(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)

        return StorageInfo(
            totalBytes = total,
            freeBytes = free,
            usedBytes = used,
            percentUsed = percent,
            totalReadable = readableFileSize(total),
            freeReadable = readableFileSize(free),
            usedReadable = readableFileSize(used),
            photosCount = photos,
            videosCount = videos
        )
    }

    private fun countMedia(context: Context, uri: Uri): Int {
        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
        val count = cursor?.count ?: 0
        cursor?.close()
        return count
    }

    private fun readableFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format("%.1f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }
}
