package com.scrubberai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.scrubberai.presentation.navigation.NavGraph
import com.scrubberai.presentation.ui.theme.ScrubberAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScrubberAITheme {
                NavGraph()
            }
        }
    }
}
