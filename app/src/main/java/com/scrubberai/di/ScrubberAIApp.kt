package com.scrubberai.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScrubberAIApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize any global components here if needed
    }
}
