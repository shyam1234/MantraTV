package com.malviya.mantra.firebase

import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.malviya.mantra.ui.constants.AppConstants

/**
 * Firebase Analytics event tracking for MantraTV app
 * 
 * This file contains functions for logging user interactions and app events
 * to Firebase Analytics for insights and user behavior analysis.
 */

object FirebaseEvent {
    const val EVENT_AUTO_CHANT = AppConstants.EVENT_AUTO_CHANT
    const val EVENT_MANUAL_CHANT = AppConstants.EVENT_MANUAL_CHANT
    const val EVENT_SAMPURNA_MALA = AppConstants.EVENT_SAMPURNA_MALA
    const val KEY_AUTO_CHANT_ENABLED = AppConstants.KEY_AUTO_CHANT_ENABLED
    const val KEY_MANUAL_CHANT_TYPE = AppConstants.KEY_MANUAL_CHANT_TYPE
    const val KEY_MALA_NUMBER = AppConstants.KEY_MALA_NUMBER
    const val KEY_TIME_TAKEN = AppConstants.KEY_TIME_TAKEN
    const val KEY_DEVICE_MODEL = AppConstants.KEY_DEVICE_MODEL
}

/**
 * Logs auto-chant toggle events to Firebase Analytics
 * 
 * @param isAutoChantEnabled True if auto-chant was enabled, false if disabled
 * 
 * Tracks when users enable or disable the auto-chant feature,
 * including device information for analytics insights.
 */
fun logAutoChant(isAutoChantEnabled: Boolean){
    try {
        val params = Bundle().apply {
            putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
            putBoolean(FirebaseEvent.KEY_AUTO_CHANT_ENABLED, isAutoChantEnabled)
        }
        Firebase.analytics.logEvent(FirebaseEvent.EVENT_AUTO_CHANT, params)
    } catch (e: Exception) {
        // Silently ignore Firebase errors during testing
    }
}

/**
 * Logs manual chant actions to Firebase Analytics
 * 
 * @param manualChantType Type of manual chant action ("incrementCount" or "decrementCount")
 * 
 * Tracks when users manually increment or decrement the bead count,
 * including device information for analytics insights.
 */
fun logManualChant(manualChantType: String){
    try {
        val params = Bundle().apply {
            putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
            putString(FirebaseEvent.KEY_MANUAL_CHANT_TYPE, manualChantType)
        }
        Firebase.analytics.logEvent(FirebaseEvent.EVENT_MANUAL_CHANT, params)
    } catch (e: Exception) {
        // Silently ignore Firebase errors during testing
    }
}

/**
 * Logs completed mala rounds to Firebase Analytics
 * 
 * @param malaNumber The total number of completed mala rounds
 * @param timeTaken Time taken to complete the mala in minutes
 * 
 * Tracks when users complete a full mala round (108 beads),
 * including timing information for performance analysis.
 */
fun logSampurnaMala(malaNumber: Int, timeTaken: Long){
    try {
        val params = Bundle().apply {
            putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
            putInt(FirebaseEvent.KEY_MALA_NUMBER, malaNumber)
            putLong(FirebaseEvent.KEY_TIME_TAKEN, timeTaken)
        }
        Firebase.analytics.logEvent(FirebaseEvent.EVENT_SAMPURNA_MALA, params)
    } catch (e: Exception) {
        // Silently ignore Firebase errors during testing
    }
}



