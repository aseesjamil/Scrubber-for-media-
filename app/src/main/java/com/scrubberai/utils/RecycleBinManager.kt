package com.scrubberai.utils

import android.content.Context
import java.io.File

class RecycleBinManager(private val context: Context) {

    private val binDir: File by lazy {
        File(context.filesDir, "recycle_bin").apply { if (!exists()) mkdirs() }
    }

    fun getBinDir(): File = binDir

    suspend fun moveToBin(file: File): Boolean {
        try {
            if (!file.exists()) return false
            val target = File(binDir, System.currentTimeMillis().toString() + "_" + file.name)
            file.copyTo(target, overwrite = false)
            file.delete()
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun restoreFromBin(binFile: File, destination: File): Boolean {
        return try {
            binFile.copyTo(destination, overwrite = false)
            binFile.delete()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun purgeOlderThan(thresholdMillis: Long) {
        val now = System.currentTimeMillis()
        binDir.listFiles()?.forEach { f ->
            if (now - f.lastModified() > thresholdMillis) {
                f.delete()
            }
        }
    }
}
