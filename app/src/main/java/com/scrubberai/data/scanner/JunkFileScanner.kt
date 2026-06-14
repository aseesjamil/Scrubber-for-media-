package com.scrubberai.data.scanner

import android.content.Context
import com.scrubberai.domain.model.ScannableItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class JunkFileScanner(private val context: Context) {

    suspend fun scanJunkFiles(): List<ScannableItem> = withContext(Dispatchers.IO) {
        val junkFiles = mutableListOf<ScannableItem>()

        // Scan temp files
        junkFiles.addAll(scanForPattern("/storage/emulated/0", TEMP_PATTERNS))

        // Scan cache files
        junkFiles.addAll(scanCacheDirectories())

        // Scan log files
        junkFiles.addAll(scanForPattern("/storage/emulated/0", LOG_PATTERNS))

        return@withContext junkFiles
    }

    private fun scanForPattern(rootPath: String, patterns: List<String>): List<ScannableItem> {
        val items = mutableListOf<ScannableItem>()
        val root = File(rootPath)

        try {
            root.walk().forEach { file ->
                if (file.isFile) {
                    val name = file.name.lowercase()
                    for (pattern in patterns) {
                        if (name.matches(Regex(pattern))) {
                            items.add(
                                ScannableItem(
                                    path = file.absolutePath,
                                    displayName = file.name,
                                    size = file.length(),
                                    mimeType = "application/junk"
                                )
                            )
                            break
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Skip inaccessible files
        }

        return items
    }

    private fun scanCacheDirectories(): List<ScannableItem> {
        val items = mutableListOf<ScannableItem>()
        val cacheDirs = listOf(
            File("/storage/emulated/0/.cache"),
            File("/storage/emulated/0/Android/data/.cache")
        )

        cacheDirs.forEach { dir ->
            if (dir.exists() && dir.isDirectory) {
                dir.walk().forEach { file ->
                    if (file.isFile && file.length() > 0) {
                        items.add(
                            ScannableItem(
                                path = file.absolutePath,
                                displayName = file.name,
                                size = file.length(),
                                mimeType = "application/cache"
                            )
                        )
                    }
                }
            }
        }

        return items
    }

    companion object {
        private val TEMP_PATTERNS = listOf(
            ".*\\.tmp$",
            ".*\\.temp$",
            ".*_tmp.*",
            "~.*"
        )

        private val LOG_PATTERNS = listOf(
            ".*\\.log$",
            ".*crash.*",
            ".*debug.*"
        )
    }
}
