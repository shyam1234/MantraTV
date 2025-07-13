package com.malviya.mantra.firebase

import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.malviya.mantra.ui.constants.AppConstants


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

fun logAutoChant(isAutoChantEnabled: Boolean){
    val params = Bundle().apply {
        putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
        putBoolean(FirebaseEvent.KEY_AUTO_CHANT_ENABLED,isAutoChantEnabled )
    }
    Firebase.analytics.logEvent(FirebaseEvent.EVENT_AUTO_CHANT, params)
}

fun logManualChant(manualChantType: String){
    val params = Bundle().apply {
        putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
        putString(FirebaseEvent.KEY_MANUAL_CHANT_TYPE,manualChantType )
    }
    Firebase.analytics.logEvent(FirebaseEvent.EVENT_MANUAL_CHANT, params)
}

fun logSampurnaMala(malaNumber: Int, timeTaken: Long){
    val params = Bundle().apply {
        putString(FirebaseEvent.KEY_DEVICE_MODEL, "${Build.MANUFACTURER} ${Build.MODEL}")
        putInt(FirebaseEvent.KEY_MALA_NUMBER, malaNumber )
        putLong(FirebaseEvent.KEY_TIME_TAKEN,  timeTaken)
    }
    Firebase.analytics.logEvent(FirebaseEvent.EVENT_SAMPURNA_MALA, params)
}



