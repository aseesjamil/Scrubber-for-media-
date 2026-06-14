package com.scrubberai.data.scanner

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.scrubberai.domain.model.DuplicateGroup
import com.scrubberai.domain.model.FileInfo
import com.scrubberai.utils.HashUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class DuplicateVideoScanner(private val context: Context) {

    suspend fun scanDuplicates(): List<DuplicateGroup> = withContext(Dispatchers.IO) {
        val files = getMediaStoreVideos()
        val groupedByHash = mutableMapOf<String, MutableList<FileInfo>>()

        for (file in files) {
            val hash = computeHash(File(file.path))
            if (hash.isNotEmpty()) {
                groupedByHash.computeIfAbsent(hash) { mutableListOf() }.add(file)
            }
        }

        return@withContext groupedByHash
            .filter { it.value.size > 1 }
            .map { (hash, fileList) ->
                DuplicateGroup(
                    hash = hash,
                    files = fileList,
                    totalSize = fileList.sumOf { it.size }
                )
            }
    }

    private fun getMediaStoreVideos(): List<FileInfo> {
        val videos = mutableListOf<FileInfo>()
        val uri: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.DURATION
        )

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                videos.add(
                    FileInfo(
                        path = cursor.getString(dataColumn),
                        name = cursor.getString(nameColumn),
                        size = cursor.getLong(sizeColumn),
                        mimeType = cursor.getString(mimeColumn),
                        lastModified = cursor.getLong(dateColumn) * 1000
                    )
                )
            }
        }

        return videos
    }

    private fun computeHash(file: File): String {
        return try {
            FileInputStream(file).use { HashUtils.sha256ForFile(it) }
        } catch (e: Exception) {
            ""
        }
    }
}
