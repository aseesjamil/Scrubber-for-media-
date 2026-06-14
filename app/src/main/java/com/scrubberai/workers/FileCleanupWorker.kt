package com.scrubberai.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class FileCleanupWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        // Background cleanup tasks
        return Result.success()
    }
}
