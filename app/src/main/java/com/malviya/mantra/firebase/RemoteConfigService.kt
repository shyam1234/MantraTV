package com.malviya.mantra.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.ktx.Firebase
import com.malviya.mantra.R
import timber.log.Timber

class RemoteConfigService() {
    // Get a Remote Config object instance and set the minimum fetch interval to allow for frequent refreshes:
    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    init {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600 // 1 hour
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
    }

    fun fetchAndActivate(onComplete: (Boolean) -> Unit) {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Remote config fetched successfully")
                    onComplete(true)
                } else {
                    Timber.e(task.exception, "Error fetching remote config")
                    onComplete(false)
                }
            }
    }

    fun getBackgroundImageUrl(): String {
        return remoteConfig.getString("background_image_url")
    }

    fun isBackgroundImageEnabled(): Boolean {
        return remoteConfig.getBoolean("background_image_enabled")
    }

    fun getBackgroundOpacity(): Float {
        return remoteConfig.getDouble("background_opacity").toFloat()
    }

    fun getFlashMessage(): String {
        return remoteConfig.getString("flash_message")
    }
} 