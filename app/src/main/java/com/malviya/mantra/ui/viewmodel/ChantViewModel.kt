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

class ChantViewModel : ViewModel() {
    sealed class ChantFeedback {
        object Begin : ChantFeedback()
        object VeryFast : ChantFeedback()
        object Fast : ChantFeedback()
        object Good : ChantFeedback()
        object SlowThanUsual : ChantFeedback()
        object Slow : ChantFeedback()
        object VerySlow : ChantFeedback()
    }

    val remoteConfigService: StateFlow<RemoteConfigService> = MutableStateFlow(RemoteConfigService())

    private val _isAutoChanting = MutableStateFlow(false)
    val isAutoChanting: StateFlow<Boolean> = _isAutoChanting

    private val _chantFeedback = MutableStateFlow<ChantFeedback>(ChantFeedback.Begin)
    val chantFeedback: StateFlow<ChantFeedback> = _chantFeedback

    private val _color = MutableStateFlow(colorButtonGray)
    val color: StateFlow<Color> = _color

    private val _chantLogs = MutableStateFlow<List<ChantLog>>(emptyList())
    val chantLogs: StateFlow<List<ChantLog>> = _chantLogs

    private val _malaNumber = MutableStateFlow<Int>(0)
    val malaNumber: StateFlow<Int> = _malaNumber

    private val _oneBeadTimeForRendering = MutableStateFlow<Long>(0)
    val oneBeadTimeForRendering: StateFlow<Long> = _oneBeadTimeForRendering

    private val _count = MutableStateFlow<Int>(0)
    val count : StateFlow<Int> = _count

    private var startTime = System.currentTimeMillis()
    private var oneBidTAT = System.currentTimeMillis()
    private var totalTime = 0L

    private var chantJob: Job? = null

    fun toggleAutoChant() {
        _isAutoChanting.value = !_isAutoChanting.value
        if (_isAutoChanting.value) {
            startAutoChant()
        }
        logAutoChant(_isAutoChanting.value)
    }


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
                //---
                // Calculate time taken for the current mala and log it
                _malaNumber.value += 1
                val malaCompletedTime = System.currentTimeMillis() - startTime
                totalTime += malaCompletedTime
                _chantLogs.value += ChantLog(_malaNumber.value, malaCompletedTime, totalTime)
                logSampurnaMala(_malaNumber.value, (malaCompletedTime/AppConstants.TimeFormat.MILLISECONDS_IN_MINUTE))
            }else{
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

    fun decrementCount(isOnClicked : Boolean = false) {
        if(isOnClicked && _isAutoChanting.value) return
        if(isOnClicked) {
            logManualChant("decrementCount")
        }
        if (_count.value > 0) {
            _count.value -= 1
        }
    }

    fun getCircleColor(timeConsumedForOneBid: Long){
        when(timeConsumedForOneBid){
            in 0..AppConstants.VERY_FAST_THRESHOLD -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.VeryFast
            }  // very fast
            in (AppConstants.VERY_FAST_THRESHOLD + 1)..AppConstants.FAST_THRESHOLD -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.Fast
            }  // fast
            in (AppConstants.FAST_THRESHOLD + 1)..AppConstants.GOOD_THRESHOLD -> {
                _color.value = colorGreen
                _chantFeedback.value = ChantFeedback.Good
            }  // good
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
            }   // very slow
        }
    }

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

    override fun onCleared() {
        super.onCleared()
        stopAutoChant()
    }

    fun stopAutoChant() {
        chantJob?.cancel()
        chantJob = null
    }
}