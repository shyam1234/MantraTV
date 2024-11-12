package com.malviya.mantra

import android.app.Application
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase Analytics
        val firebaseAnalytics = Firebase.analytics
    }
}