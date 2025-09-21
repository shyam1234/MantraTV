package com.malviya.mantra.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource

import com.malviya.mantra.R
import com.malviya.mantra.ui.screen.GreetingScreen
import com.malviya.mantra.ui.theme.MantraTheme
import com.malviya.mantra.ui.viewmodel.ChantViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val chantViewModel: ChantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        // Initialize audio manager
        chantViewModel.initializeAudioManager(this)
        
        // Initialize remote config service
        chantViewModel.initializeRemoteConfig()

        //enableEdgeToEdge()
        setContent {
            MantraTheme {
                GreetingScreen( name = stringResource(R.string.greeting),chantViewModel)
            }
        }
    }
}

