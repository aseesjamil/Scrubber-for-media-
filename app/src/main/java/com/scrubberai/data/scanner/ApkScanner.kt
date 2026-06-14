package com.scrubberai.data.scanner

import android.content.Context
import android.content.pm.PackageManager
import com.scrubberai.domain.model.ScannableItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class ApkScanner(private val context: Context) {

    private val packageManager: PackageManager = context.packageManager

    suspend fun scanApkFiles(): List<ScannableItem> = withContext(Dispatchers.IO) {
        val apkFiles = mutableListOf<ScannableItem>()

        // Get installed packages
        val installedPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            .map { it.packageName }
            .toSet()

        // Scan Downloads and common locations
        val searchPaths = listOf(
            "/storage/emulated/0/Download",
            "/storage/emulated/0/Downloads",
            "/storage/emulated/0/DCIM",
            "/storage/emulated/0"
        )

        for (path in searchPaths) {
            val dir = File(path)
            if (dir.exists() && dir.isDirectory) {
                dir.walk().forEach { file ->
                    if (file.isFile && file.extension.lowercase() == "apk") {
                        val fileName = file.nameWithoutExtension
                        val isInstalled = installedPackages.any { it.contains(fileName, ignoreCase = true) }

                        if (!isInstalled) {
                            apkFiles.add(
                                ScannableItem(
                                    path = file.absolutePath,
                                    displayName = file.name,
                                    size = file.length(),
                                    mimeType = "application/vnd.android.package-archive"
                                )
                            )
                        }
                    }
                }
            }
        }

        return@withContext apkFiles.sortedByDescending { it.size }
    }
}
