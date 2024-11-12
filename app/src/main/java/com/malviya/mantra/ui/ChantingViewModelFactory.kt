package com.malviya.mantra.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.malviya.mantra.utils.AnalyticsManager

class ChantingViewModelFactory(
    private val analyticsManager: AnalyticsManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChantViewModel::class.java)) {
            return ChantViewModel(analyticsManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
