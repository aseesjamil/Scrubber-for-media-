package com.scrubberai.data.scanner

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.scrubberai.domain.model.DuplicateGroup
import com.scrubberai.domain.model.FileInfo
import com.scrubberai.utils.HashUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class DuplicatePhotoScanner(private val context: Context) {

    suspend fun scanDuplicates(): List<DuplicateGroup> = withContext(Dispatchers.IO) {
        val files = getMediaStoreImages()
        val groupedByHash = mutableMapOf<String, MutableList<FileInfo>>()

        for (file in files) {
            val hash = computeHash(File(file.path))
            groupedByHash.computeIfAbsent(hash) { mutableListOf() }.add(file)
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

    private fun getMediaStoreImages(): List<FileInfo> {
        val images = mutableListOf<FileInfo>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val dateColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)

            while (cursor.moveToNext()) {
                images.add(
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

        return images
    }

    private fun computeHash(file: File): String {
        return try {
            FileInputStream(file).use { HashUtils.sha256ForFile(it) }
        } catch (e: Exception) {
            ""
        }
    }
}
