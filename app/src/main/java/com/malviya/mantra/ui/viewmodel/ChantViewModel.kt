package com.malviya.mantra.ui.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malviya.mantra.firebase.RemoteConfigService
import com.malviya.mantra.firebase.logAutoChant
import com.malviya.mantra.firebase.logManualChant
import com.malviya.mantra.firebase.logSampurnaMala
import com.malviya.mantra.ui.screen.ChantLog
import com.malviya.mantra.ui.theme.colorButtonGray
import com.malviya.mantra.ui.theme.colorGreen
import com.malviya.mantra.ui.theme.colorRed
import com.malviya.mantra.ui.theme.colorYellow
import com.malviya.mantra.ui.constants.AppConstants
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

/**
 * ViewModel for managing chant-related state and business logic
 * 
 * This ViewModel handles:
 * - Chant counting and mala completion
 * - Auto-chant functionality
 * - Chant speed feedback
 * - Time tracking and logging
 * - Firebase analytics
 */
class ChantViewModel : ViewModel() {
    
    /**
     * Sealed class representing different chant speed feedback states
     * Used to provide user feedback based on chanting speed
     */
    sealed class ChantFeedback {
        /** Initial state when chanting begins */
        object Begin : ChantFeedback()
        
        /** User is chanting very fast */
        object VeryFast : ChantFeedback()
        
        /** User is chanting fast */
        object Fast : ChantFeedback()
        
        /** User is chanting at good speed */
        object Good : ChantFeedback()
        
        /** User is chanting slower than usual */
        object SlowThanUsual : ChantFeedback()
        
        /** User is chanting slow */
        object Slow : ChantFeedback()
        
        /** User is chanting very slow */
        object VerySlow : ChantFeedback()
    }

    // ==================== STATE FLOWS ====================
    
    /** Remote configuration service for dynamic app configuration */
    val remoteConfigService: StateFlow<RemoteConfigService> = MutableStateFlow(RemoteConfigService())

    /** Auto-chanting state - true when auto-chant is active */
    private val _isAutoChanting = MutableStateFlow(false)
    val isAutoChanting: StateFlow<Boolean> = _isAutoChanting

    /** Current chant speed feedback state */
    private val _chantFeedback = MutableStateFlow<ChantFeedback>(ChantFeedback.Begin)
    val chantFeedback: StateFlow<ChantFeedback> = _chantFeedback

    /** Current color for UI feedback based on chant speed */
    private val _color = MutableStateFlow(colorButtonGray)
    val color: StateFlow<Color> = _color

    /** List of completed mala rounds with timing information */
    private val _chantLogs = MutableStateFlow<List<ChantLog>>(emptyList())
    val chantLogs: StateFlow<List<ChantLog>> = _chantLogs

    /** Total number of completed mala rounds */
    private val _malaNumber = MutableStateFlow<Int>(0)
    val malaNumber: StateFlow<Int> = _malaNumber

    /** Time taken for the last bead (for rendering feedback) */
    private val _oneBeadTimeForRendering = MutableStateFlow<Long>(0)
    val oneBeadTimeForRendering: StateFlow<Long> = _oneBeadTimeForRendering

    /** Current bead count in the current mala round (0-108) */
    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count

    // ==================== PRIVATE VARIABLES ====================
    
    /** Start time of the current mala round */
    private var startTime = System.currentTimeMillis()
    
    /** Time of the last bead increment (TAT = Turn Around Time) */
    private var oneBidTAT = System.currentTimeMillis()
    
    /** Total time spent chanting across all mala rounds */
    private var totalTime = 0L

    /** Coroutine job for auto-chanting functionality */
    private var chantJob: Job? = null

    /**
     * Toggles auto-chant functionality on/off
     * 
     * When enabled, automatically increments the bead count at regular intervals.
     * Logs the auto-chant state change to Firebase Analytics.
     */
    fun toggleAutoChant() {
        _isAutoChanting.value = !_isAutoChanting.value
        if (_isAutoChanting.value) {
            startAutoChant()
        }
        logAutoChant(_isAutoChanting.value)
    }


    /**
     * Starts the auto-chant coroutine
     * 
     * Creates a coroutine that automatically increments the bead count
     * at the specified interval (IDLE_TIME_FOR_ONE_BEAD) while auto-chant is active.
     */
    private fun startAutoChant() {
        oneBidTAT = System.currentTimeMillis()
        chantJob?.cancel()
        chantJob = viewModelScope.launch {
            while (_isAutoChanting.value) {
                delay(AppConstants.IDLE_TIME_FOR_ONE_BEAD)
                incrementCount()
            }
        }
    }

    /**
     * Increments the bead count and handles mala completion
     * 
     * @param isOnClicked True if triggered by user interaction, false for auto-chant
     * 
     * When a mala round is completed (108 beads):
     * - Resets count to 0
     * - Logs the completed mala with timing information
     * - Updates total time and mala number
     * - Sends analytics to Firebase
     * 
     * For regular increments:
     * - Updates bead count
     * - Calculates time for the last bead
     * - Updates UI color based on chanting speed
     */
    fun incrementCount(isOnClicked : Boolean = false) {
        if(isOnClicked && _isAutoChanting.value) return
        if(isOnClicked) {
            logManualChant("incrementCount")
        }
        viewModelScope.launch {
            if (_count.value == AppConstants.ONE_MALA_ROUND_COUNT) {
                // Mala round is completed, reset count and log time
                _count.value = 0
                _color.value = Color.Gray
                _chantFeedback.value = ChantFeedback.Begin
                
                // Calculate time taken for the current mala and log it
                _malaNumber.value += 1
                val malaCompletedTime = System.currentTimeMillis() - startTime
                totalTime += malaCompletedTime
                _chantLogs.value += ChantLog(_malaNumber.value, malaCompletedTime, totalTime)
                logSampurnaMala(_malaNumber.value, (malaCompletedTime/AppConstants.TimeFormat.MILLISECONDS_IN_MINUTE))
            } else {
                _oneBeadTimeForRendering.value = (System.currentTimeMillis() - oneBidTAT).toLong()
                // Increment the bead count
                oneBidTAT = System.currentTimeMillis()
                _count.value += 1
            }

            // Start time when count is 1 (first bead in mala)
            if (_count.value == 0) {
                startTime = System.currentTimeMillis()
                _oneBeadTimeForRendering.value = AppConstants.IDLE_TIME_FOR_ONE_BEAD
            }

            getCircleColor(_oneBeadTimeForRendering.asStateFlow().value)

            Timber.d("one bead count: ${_count.value}, time: ${_oneBeadTimeForRendering.asStateFlow().value}")
        }
    }

    /**
     * Decrements the bead count
     * 
     * @param isOnClicked True if triggered by user interaction, false for auto-chant
     * 
     * Only allows decrement when count is greater than 0.
     * Logs manual decrement actions to Firebase Analytics.
     */
    fun decrementCount(isOnClicked : Boolean = false) {
        if(isOnClicked && _isAutoChanting.value) return
        if(isOnClicked) {
            logManualChant("decrementCount")
        }
        if (_count.value > 0) {
            _count.value -= 1
        }
    }

    /**
     * Updates UI color and feedback based on chanting speed
     * 
     * @param timeConsumedForOneBid Time taken for the last bead in milliseconds
     * 
     * Determines the appropriate color and feedback message based on timing thresholds:
     * - Very Fast (0-2999ms): Red color
     * - Fast (3000-3500ms): Red color  
     * - Good (3501-4900ms): Green color
     * - Slow Than Usual (4901-6000ms): Yellow color
     * - Slow (6001-8000ms): Yellow color
     * - Very Slow (8000ms+): Yellow color
     */
    fun getCircleColor(timeConsumedForOneBid: Long){
        when(timeConsumedForOneBid){
            in 0..AppConstants.VERY_FAST_THRESHOLD -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.VeryFast
            }
            in (AppConstants.VERY_FAST_THRESHOLD + 1)..AppConstants.FAST_THRESHOLD -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.Fast
            }
            in (AppConstants.FAST_THRESHOLD + 1)..AppConstants.GOOD_THRESHOLD -> {
                _color.value = colorGreen
                _chantFeedback.value = ChantFeedback.Good
            }
            in (AppConstants.GOOD_THRESHOLD + 1)..AppConstants.SLOW_THAN_USUAL_THRESHOLD -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.SlowThanUsual
            }
            in (AppConstants.SLOW_THAN_USUAL_THRESHOLD + 1)..AppConstants.SLOW_THRESHOLD -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.Slow
            }
            else -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.VerySlow
            }
        }
    }

    /**
     * Converts milliseconds to a human-readable time format
     * 
     * @param milliseconds Time duration in milliseconds
     * @return Formatted time string (e.g., "02:30 min", "01:23:45 hr", "30 sec")
     * 
     * Formats time based on duration:
     * - Hours: "HH:MM:SS hr"
     * - Minutes: "MM:SS min" 
     * - Seconds: "SS sec"
     */
    fun convertMillisToReadableTime(milliseconds: Long): String {
        val totalSeconds = milliseconds / AppConstants.TimeFormat.MILLISECONDS_IN_SECOND
        val hours = totalSeconds / AppConstants.TimeFormat.SECONDS_IN_HOUR
        val minutes = (totalSeconds % AppConstants.TimeFormat.SECONDS_IN_HOUR) / AppConstants.TimeFormat.SECONDS_IN_MINUTE
        val seconds = totalSeconds % AppConstants.TimeFormat.SECONDS_IN_MINUTE

        return when {
            hours > 0 -> String.format(Locale.US, AppConstants.TimeFormat.TIME_FORMAT_HOURS, hours, minutes, seconds) + AppConstants.TimeFormat.SUFFIX_HOURS
            minutes > 0 -> String.format(Locale.US, AppConstants.TimeFormat.TIME_FORMAT_MINUTES, minutes, seconds) + AppConstants.TimeFormat.SUFFIX_MINUTES
            else -> String.format(Locale.US, AppConstants.TimeFormat.TIME_FORMAT_SECONDS, seconds) + AppConstants.TimeFormat.SUFFIX_SECONDS
        }
    }

    /**
     * Called when the ViewModel is being cleared
     * Ensures auto-chant is stopped to prevent memory leaks
     */
    override fun onCleared() {
        super.onCleared()
        stopAutoChant()
    }

    /**
     * Stops the auto-chant functionality
     * Cancels the coroutine job and sets it to null
     */
    fun stopAutoChant() {
        chantJob?.cancel()
        chantJob = null
    }
}