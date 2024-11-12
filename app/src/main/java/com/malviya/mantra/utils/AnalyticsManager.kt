package com.malviya.mantra.utils

//import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.perf.FirebasePerformance
import com.google.firebase.perf.metrics.Trace
import getCurrentDateTime

class AnalyticsManager(
    private val firebaseAnalytics: FirebaseAnalytics,
//    private val crashlytics: FirebaseCrashlytics
) {

    // Method to log chant completion
    fun logSingleMalaComplete(chantId: String, duration: Long) {
        val bundle = Bundle().apply {
            putString("mala_number", chantId)
            putLong("duration", duration)
        }
        firebaseAnalytics.logEvent("single_mala_complete", bundle)
    }

    // Method to log chant completion
    fun logSampurnamalaComplete(chantId: String, duration: Long) {
        val bundle = Bundle().apply {
            putString("sampurnamala", chantId)
            putLong("duration", duration)
        }
        firebaseAnalytics.logEvent("sampurnamala_complete", bundle)
    }

    // Method to log settings changes
    fun logSettingsChange(setting: String, value: String) {
        val bundle = Bundle().apply {
            putString("setting", setting)
            putString("value", value)
        }
        firebaseAnalytics.logEvent("settings_change", bundle)
    }

    // Method to start a performance trace
    fun startTrace(traceName: String): Trace {
        return FirebasePerformance.getInstance().newTrace(traceName).apply { start() }
    }

    // Method to stop a performance trace
    fun stopTrace(trace: Trace) {
        trace.stop()
    }


    fun logEnterPageloaded(string: String) {
        val bundle = Bundle().apply {
            putString("enter_chant_page", string)
            putString("value", getCurrentDateTime())
        }
        firebaseAnalytics.logEvent("chant_home_page_enter", bundle)
    }

    fun logExitPageloaded(string: String) {
        val bundle = Bundle().apply {
            putString("exit_chant_page", string)
            putString("value", getCurrentDateTime())
        }
        firebaseAnalytics.logEvent("chant_home_page_exit", bundle)
    }

    // Method to log custom errors to Crashlytics
    fun reportError(errorMessage: String) {
//        crashlytics.log(errorMessage)
    }
}
