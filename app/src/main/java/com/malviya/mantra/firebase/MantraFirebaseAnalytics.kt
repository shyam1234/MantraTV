package com.malviya.mantra.firebase

import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase


object FirebaseEvent {
    const val EVENT_AUTO_CHANT = "auto_chant"
    const val EVENT_MANUAL_CHANT = "manual_chant"
    const val EVENT_SAMPURNA_MALA= "sampurna_mala"
    const val KEY_AUTO_CHANT_ENABLED = "auto_chant_enabled"
    const val KEY_MANUAL_CHANT_TYPE = "manual_chant_type"
    const val KEY_MALA_NUMBER = "mala_number"
    const val KEY_TIME_TAKEN = "time_taken"
    const val KEY_DEVICE_MODEL = "device_model"
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



