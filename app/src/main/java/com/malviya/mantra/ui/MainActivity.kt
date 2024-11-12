package com.malviya.mantra.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.FirebaseAnalytics
import com.malviya.mantra.R
import com.malviya.mantra.screen.GreetingScreen
import com.malviya.mantra.ui.theme.MantraTheme
import com.malviya.mantra.utils.AnalyticsManager
import timber.log.Timber

class MainActivity : ComponentActivity() {
    // Initialize AnalyticsManager with Firebase instances
    private var analyticsManager: AnalyticsManager = AnalyticsManager(
        FirebaseAnalytics.getInstance(this),
        // FirebaseCrashlytics.getInstance()
    )
    private val chantViewModel: ChantViewModel by viewModels {
        ChantingViewModelFactory(analyticsManager)
    }

    init {
        analyticsManager.logEnterPageloaded("chant home page")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        var chantLoadTrace = analyticsManager.startTrace("chant_load")

        //enableEdgeToEdge()
        setContent {
            MantraTheme {
                GreetingScreen( name = stringResource(R.string.greeting),chantViewModel)
            }
        }

        chantLoadTrace.let { analyticsManager.stopTrace(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        analyticsManager.logExitPageloaded("chant home page")
    }
}

