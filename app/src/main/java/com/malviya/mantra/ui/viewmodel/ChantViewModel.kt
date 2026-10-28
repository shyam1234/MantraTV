package com.malviya.mantra.ui.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malviya.mantra.firebase.RemoteConfigService
import com.malviya.mantra.firebase.logAutoChant
import com.malviya.mantra.firebase.logManualChant
import com.malviya.mantra.firebase.logSampurnaMala
import com.malviya.mantra.speech.AudioManager
import com.malviya.mantra.ui.screen.ChantLog
import com.malviya.mantra.ui.settings.ChantSpeedStore
import com.malviya.mantra.ui.settings.ChantSpeeds
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
import kotlin.properties.Delegates

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

    private var malaCompletedTime by Delegates.notNull<Long>()

    /** Remote configuration service for dynamic app configuration */
    val remoteConfigService: StateFlow<RemoteConfigService?> = MutableStateFlow(null)

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

    // ==================== AUDIO STATE FLOWS ====================
    
    /** Audio manager for MP3 playback functionality */
    private var audioManager: AudioManager? = null
    
    /** Whether audio is currently playing */
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()
    
    /** Current highlighted word index in the mantra */
    private val _currentWordIndex = MutableStateFlow(-1)
    val currentWordIndex: StateFlow<Int> = _currentWordIndex.asStateFlow()
    
    /** Whether audio is ready */
    private val _isAudioReady = MutableStateFlow(false)
    val isAudioReady: StateFlow<Boolean> = _isAudioReady.asStateFlow()

    /** Saved auto-chant playback speed. 1.0 is the recorded pace. */
    private val _chantSpeed = MutableStateFlow(ChantSpeeds.DEFAULT)
    val chantSpeed: StateFlow<Float> = _chantSpeed.asStateFlow()

    private var appContext: Context? = null

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
     * Initializes the remote config service
     * This should be called from the main application, not during unit tests
     */
    fun initializeRemoteConfig() {
        if (remoteConfigService.value == null) {
            (remoteConfigService as MutableStateFlow).value = RemoteConfigService()
        }
    }

    /**
     * Initializes the audio manager with context
     * 
     * @param context Application context for audio initialization
     */
    fun initializeAudioManager(context: Context) {
        val applicationContext = context.applicationContext
        appContext = applicationContext
        val savedSpeed = ChantSpeedStore.read(applicationContext)
        _chantSpeed.value = savedSpeed
        if (audioManager == null) {
            audioManager = AudioManager(applicationContext, savedSpeed)
            
            // Set callback for audio completion to increment counter
            audioManager?.setOnAudioCompleteCallback {
                viewModelScope.launch {
                    Timber.d("Audio completion callback triggered, incrementing counter")
                    incrementCount()
                }
            }
            
            // Observe audio manager state flows
            viewModelScope.launch {
                audioManager?.isAudioPlaying?.collect { isPlaying ->
                    _isAudioPlaying.value = isPlaying
                }
            }
            viewModelScope.launch {
                audioManager?.currentWordIndex?.collect { wordIndex ->
                    _currentWordIndex.value = wordIndex
                }
            }
            viewModelScope.launch {
                audioManager?.isAudioReady?.collect { isReady ->
                    _isAudioReady.value = isReady
                }
            }
        }
    }

    /**
     * Toggles auto-chant functionality on/off
     * 
     * When enabled, starts audio playback with word highlighting and looping.
     * Counter increments each time audio completes one cycle.
     * Logs the auto-chant state change to Firebase Analytics.
     */
    fun toggleAutoChant() {
        Timber.d("toggleAutoChant")
        _isAutoChanting.value = !_isAutoChanting.value
        if (_isAutoChanting.value) {
            oneBidTAT = System.currentTimeMillis()
            audioManager?.startAudio()
            getCircleColor(expectedBeadMillis())
        } else {
            // Stop audio playback
            audioManager?.stopAudio()
        }
        logAutoChant(_isAutoChanting.value)
    }

    /**
     * Sets the auto-chant speed and stores it as the default for the next launch.
     */
    fun setChantSpeed(speed: Float) {
        val snapped = ChantSpeeds.nearest(speed)
        _chantSpeed.value = snapped
        audioManager?.setPlaybackSpeed(snapped)
        appContext?.let { ChantSpeedStore.write(it, snapped) }
        if (_isAutoChanting.value) {
            getCircleColor(expectedBeadMillis())
        } else if (_count.value > 0 && _oneBeadTimeForRendering.value > 0L) {
            getCircleColor(_oneBeadTimeForRendering.value)
        }
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
                    val elapsed = (System.currentTimeMillis() - oneBidTAT).coerceAtLeast(0L)
                    // Auto-chant uses the speed's real cycle. The gap between audio
                    // loops is not part of the chant, and it was marking 2× as slow.
                    _oneBeadTimeForRendering.value = if (_isAutoChanting.value) {
                        expectedBeadMillis()
                    } else {
                        elapsed
                    }
                    oneBidTAT = System.currentTimeMillis()
                    _count.value += 1
            }

            // Start time when count is 1 (first bead in mala)
            if (_count.value == 0) {
                startTime = System.currentTimeMillis()
                _oneBeadTimeForRendering.value = expectedBeadMillis()
            } else {
                getCircleColor(_oneBeadTimeForRendering.value)
            }

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
     * Length of one mantra at the recorded pace, before the speed control.
     */
    fun recordedCycleMillis(): Long {
        return audioManager?.cycleDurationMs()?.takeIf { it > 0L }
            ?: AppConstants.IDLE_TIME_FOR_ONE_BEAD
    }

    /**
     * How long one bead takes at the current auto-chant speed.
     * 2× is half the recording, so it is a fast chant, not the new "good" pace.
     */
    fun expectedBeadMillis(): Long {
        val speed = _chantSpeed.value.toDouble().coerceAtLeast(0.1)
        return (recordedCycleMillis() / speed).toLong().coerceAtLeast(1L)
    }

    /**
     * Colors the bead from how its time compares with the recorded chant.
     * Faster than the recording is red. The recording itself is green.
     * Slower than the recording is yellow.
     */
    fun getCircleColor(timeConsumedForOneBid: Long) {
        when (paceFeedback(timeConsumedForOneBid, recordedCycleMillis())) {
            ChantFeedback.VeryFast -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.VeryFast
            }
            ChantFeedback.Fast -> {
                _color.value = colorRed
                _chantFeedback.value = ChantFeedback.Fast
            }
            ChantFeedback.Good -> {
                _color.value = colorGreen
                _chantFeedback.value = ChantFeedback.Good
            }
            ChantFeedback.SlowThanUsual -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.SlowThanUsual
            }
            ChantFeedback.Slow -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.Slow
            }
            ChantFeedback.VerySlow -> {
                _color.value = colorYellow
                _chantFeedback.value = ChantFeedback.VerySlow
            }
            ChantFeedback.Begin -> Unit
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
     * Gets the mantra text for display
     * 
     * @return The complete mantra text string
     */
    fun getMantraText(): String {
        return audioManager?.getMantraText() ?: "हरे कृष्ण, हरे कृष्ण, कृष्ण कृष्ण, हरे हरे|\nहरे राम, हरे राम, राम राम, हरे हरे||"
    }

    /**
     * Called when the ViewModel is being cleared
     * Ensures auto-chant is stopped and audio resources are released
     */
    override fun onCleared() {
        super.onCleared()
        stopAutoChant()
        audioManager?.release()
        audioManager = null
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

/**
 * Compares one bead with the recorded chant. [recordedMs] is one cycle at 1×.
 * Half of that (2× speed) is very fast. The recording itself is a good pace.
 */
internal fun paceFeedback(timeMs: Long, recordedMs: Long): ChantViewModel.ChantFeedback {
    if (recordedMs <= 0L) return ChantViewModel.ChantFeedback.Begin
    val pace = AppConstants.IDLE_TIME_FOR_ONE_BEAD.toDouble()
    val ratio = timeMs.toDouble() / recordedMs.toDouble()
    return when {
        ratio <= AppConstants.VERY_FAST_THRESHOLD / pace -> ChantViewModel.ChantFeedback.VeryFast
        ratio <= AppConstants.FAST_THRESHOLD / pace -> ChantViewModel.ChantFeedback.Fast
        ratio <= GOOD_PACE_RATIO -> ChantViewModel.ChantFeedback.Good
        ratio <= AppConstants.SLOW_THAN_USUAL_THRESHOLD / pace -> ChantViewModel.ChantFeedback.SlowThanUsual
        ratio <= AppConstants.SLOW_THRESHOLD / pace -> ChantViewModel.ChantFeedback.Slow
        else -> ChantViewModel.ChantFeedback.VerySlow
    }
}

private const val GOOD_PACE_RATIO = 1.08