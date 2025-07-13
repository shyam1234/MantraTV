package com.malviya.mantra.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource

import com.malviya.mantra.R
import com.malviya.mantra.ui.screen.GreetingScreen
import com.malviya.mantra.ui.screen.MalaScreen
import com.malviya.mantra.ui.screen.ScreenSelector
import com.malviya.mantra.ui.screen.ScreenType
import com.malviya.mantra.ui.theme.MantraTheme
import com.malviya.mantra.ui.viewmodel.ChantViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val chantViewModel: ChantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        //enableEdgeToEdge()
        setContent {
            MantraTheme {
                var selectedScreen by rememberSaveable { mutableStateOf<ScreenType?>(null) }
                
                when (selectedScreen) {
                    null -> ScreenSelector { selectedScreen = it }
                    ScreenType.Counter -> GreetingScreen(
                        name = stringResource(R.string.greeting),
                        chantViewModel
                    )
                    ScreenType.Mala -> MalaScreen(
                        name = stringResource(R.string.greeting),
                        chantViewModel
                    )
                }
            }
        }
    }
}

