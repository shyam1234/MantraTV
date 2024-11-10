package com.malviya.mantra.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.malviya.mantra.R
import com.malviya.mantra.screen.GreetingScreen
import com.malviya.mantra.ui.theme.MantraTheme
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val chantViewModel: ChantViewModel by viewModels()
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        //enableEdgeToEdge()
        setContent {
            MantraTheme {
                GreetingScreen( name = stringResource(R.string.greeting),chantViewModel)
            }
        }
    }

    private fun init() {
        Timber.plant(Timber.DebugTree())
        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics
        // Initialize Firebase Crashlytics
        //FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true

    }
}

