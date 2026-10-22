package com.malviya.mantra.ui.settings

import android.content.Context

/**
 * Saves the chosen chant speed so it is used the next time the app opens.
 */
object ChantSpeedStore {
    private const val PREFS_NAME = "mantra_settings"
    private const val KEY_SPEED = "chant_speed"

    fun read(context: Context): Float {
        val saved = context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getFloat(KEY_SPEED, ChantSpeeds.DEFAULT)
        return ChantSpeeds.nearest(saved)
    }

    fun write(context: Context, speed: Float) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putFloat(KEY_SPEED, ChantSpeeds.nearest(speed))
            .apply()
    }
}
