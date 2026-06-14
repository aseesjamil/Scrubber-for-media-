package com.scrubberai.data.scanner

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.scrubberai.domain.model.ScannableItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class LargeFileScanner(private val context: Context) {

    suspend fun scanLargeFiles(minSizeBytes: Long): List<ScannableItem> = withContext(Dispatchers.IO) {
        val files = mutableListOf<ScannableItem>()

        // Scan images
        files.addAll(queryMediaStore(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, minSizeBytes))

        // Scan videos
        files.addAll(queryMediaStore(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, minSizeBytes))

        // Scan audio
        files.addAll(queryMediaStore(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, minSizeBytes))

        // Scan other files (documents, etc)
        files.addAll(scanFilesystem(minSizeBytes))

        return@withContext files.sortedByDescending { it.size }
    }

    private fun queryMediaStore(uri: Uri, minSize: Long): List<ScannableItem> {
        val items = mutableListOf<ScannableItem>()
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.MIME_TYPE
        )
        val selection = "${MediaStore.MediaColumns.SIZE} >= ?"
        val selectionArgs = arrayOf(minSize.toString())

        context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use { cursor ->
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                items.add(
                    ScannableItem(
                        path = cursor.getString(dataColumn),
                        displayName = cursor.getString(nameColumn),
                        size = cursor.getLong(sizeColumn),
                        mimeType = cursor.getString(mimeColumn)
                    )
                )
            }
        }

        return items
    }

    private fun scanFilesystem(minSize: Long): List<ScannableItem> {
        val items = mutableListOf<ScannableItem>()
        val root = File("/storage/emulated/0")
        scanDirectory(root, minSize, items)
        return items
    }

    private fun scanDirectory(dir: File, minSize: Long, items: MutableList<ScannableItem>) {
        try {
            dir.listFiles()?.forEach { file ->
                if (file.isFile && file.length() >= minSize) {
                    items.add(
                        ScannableItem(
                            path = file.absolutePath,
                            displayName = file.name,
                            size = file.length(),
                            mimeType = getMimeType(file)
                        )
                    )
                } else if (file.isDirectory && !isSystemFolder(file)) {
                    scanDirectory(file, minSize, items)
                }
            }
        } catch (e: Exception) {
            // Skip inaccessible directories
        }
    }

    private fun getMimeType(file: File): String? {
        val extension = file.extension.lowercase()
        return when (extension) {
            "pdf" -> "application/pdf"
            "zip" -> "application/zip"
            "apk" -> "application/vnd.android.package-archive"
            else -> null
        }
    }

    private fun isSystemFolder(file: File): Boolean {
        val path = file.absolutePath
        return path.contains("/Android/") || path.contains("/.")
    }
}
