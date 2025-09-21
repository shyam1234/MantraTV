package com.malviya.mantra.speech

import android.content.Context
import android.media.MediaPlayer
import com.malviya.mantra.ui.constants.AppConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Audio manager for handling MP3 playback with text synchronization
 * 
 * Manages MediaPlayer for audio playback and synchronizes word highlighting
 * based on a timing file (TTF) that contains timestamps and word indices.
 */
class AudioManager(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    private var timingData: List<TimingEntry> = emptyList()
    private var internalWordIndex = 0
    private var isLoopingAudio = false
    private var onAudioCompleteCallback: (() -> Unit)? = null
    
    // State flows for audio management
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()
    
    private val _currentWordIndex = MutableStateFlow(-1)
    val currentWordIndex: StateFlow<Int> = _currentWordIndex.asStateFlow()
    
    private val _isAudioReady = MutableStateFlow(false)
    val isAudioReady: StateFlow<Boolean> = _isAudioReady.asStateFlow()
    
    // Mantra words for display
    private val mantraWords = listOf(
        "हरे", "कृष्ण,", "हरे", "कृष्ण,", "कृष्ण", "कृष्ण,", "हरे", "हरे|",
        "हरे", "राम,", "हरे", "राम,", "राम", "राम,", "हरे", "हरे||"
    )
    
    /**
     * Data class representing a timing entry in the TTF file
     */
    data class TimingEntry(
        val timestamp: Long, // milliseconds
        val wordIndex: Int
    )
    
    init {
        initializeAudio()
    }
    
    /**
     * Initializes the audio player and loads timing data
     */
    private fun initializeAudio() {
        try {
            // Check if audio file exists
            val audioFile = "mantra_audio.mp3"
            val assetManager = context.assets
            val files = assetManager.list("")
            //Timber.d("Available assets: ${files?.joinToString(", ")}")
            
            if (files?.contains(audioFile) == true) {
                //Timber.d("Audio file found: $audioFile")
                // Load the MP3 file from assets
                val assetFileDescriptor = context.assets.openFd(audioFile)
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(assetFileDescriptor.fileDescriptor, assetFileDescriptor.startOffset, assetFileDescriptor.length)
                    prepareAsync()
                    
                    setOnPreparedListener {
                        //Timber.d("Audio prepared successfully")
                        _isAudioReady.value = true
                        loadTimingData()
                    }
                    
                    setOnCompletionListener {
                        //Timber.d("Audio completed $isLoopingAudio")
                        if (isLoopingAudio) {
                            // Call the callback to increment counter
                            //Timber.d("Calling audio completion callback to increment counter")
                            onAudioCompleteCallback?.invoke()
                            // Restart audio for next loop
                            restartAudio()
                        } else {
                            _isAudioPlaying.value = false
                            _currentWordIndex.value = -1
                        }
                    }
                    
                    setOnErrorListener { _, what, extra ->
                        //Timber.e("MediaPlayer error: what=$what, extra=$extra")
                        _isAudioReady.value = false
                        _isAudioPlaying.value = false
                        true
                    }
                }
                assetFileDescriptor.close()
            } else {
                //Timber.e("Audio file not found: $audioFile")
                // Create a test audio player for debugging
                createTestAudioPlayer()
            }
            
        } catch (e: Exception) {
            //Timber.e("Failed to initialize audio: ${e.message}")
            _isAudioReady.value = false
            // Create a test audio player for debugging
            createTestAudioPlayer()
        }
    }
    
    /**
     * Creates a test audio player for debugging when no audio file is available
     */
    private fun createTestAudioPlayer() {
        try {
            // Create a silent audio player for testing
            mediaPlayer = MediaPlayer().apply {
                setOnPreparedListener {
                    //Timber.d("Test audio player prepared")
                    _isAudioReady.value = true
                    loadTimingData()
                }
                
                setOnCompletionListener {
                    //Timber.d("Test audio completed")
                    if (isLooping) {
                        //Timber.d("Calling audio completion callback to increment counter")
                        onAudioCompleteCallback?.invoke()
                        restartAudio()
                    } else {
                        _isAudioPlaying.value = false
                        _currentWordIndex.value = -1
                    }
                }
                
                setOnErrorListener { _, what, extra ->
                    //Timber.e("Test MediaPlayer error: what=$what, extra=$extra")
                    _isAudioReady.value = false
                    _isAudioPlaying.value = false
                    true
                }
            }
            // Simulate audio preparation
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                _isAudioReady.value = true
                loadTimingData()
            }, 1000)
            
        } catch (e: Exception) {
            //Timber.e("Failed to create test audio player: ${e.message}")
        }
    }
    
    /**
     * Loads timing data from the TTF file
     */
    private fun loadTimingData() {
        try {
            //Timber.d("called loadTimingData")
            val inputStream = context.assets.open("mantra_timing.ttf")
            val reader = BufferedReader(InputStreamReader(inputStream))
            val timingList = mutableListOf<TimingEntry>()

            reader.use {
                it.forEachLine { line ->
                    if (line.isNotBlank() && !line.startsWith("#")) {
                        val parts = line.split(",")
                        if (parts.size >= 2) {
                            val timestamp = parts[0].trim().toLongOrNull()
                            val wordIndex = parts[1].trim().toIntOrNull()
                            if (timestamp != null && wordIndex != null) {
                                timingList.add(TimingEntry(timestamp, wordIndex))
                            }
                        }
                    }
                }
            }

            //Timber.d(">>>  ${timingList.size} timing entries")
            timingData = timingList.sortedBy { it.timestamp }
            //Timber.d("Loaded ${timingData.size } , ${timingData.last().wordIndex}, ${timingData.last().timestamp} timing entries")
        } catch (e: Exception) {
            //Timber.e("Failed to load timing data: ${e.message}")
            // Create default timing data if file is not found or error occurs
            createDefaultTimingData()
        }
    }


    /**
     * Creates default timing data if TTF file is not available
     */
    private fun createDefaultTimingData() {
        val defaultTiming = mutableListOf<TimingEntry>()
        val wordDuration = 450L //  ms per word
        
        for (i in mantraWords.indices) {
            defaultTiming.add(TimingEntry(i * wordDuration, i))
        }
        
        timingData = defaultTiming
        //Timber.d("Created default timing data with ${timingData.size} entries")
    }
    
    /**
     * Starts audio playback with looping
     */
    fun startAudio() {
        //Timber.d("startAudio ${_isAudioReady.value}")
        if (!_isAudioReady.value) {
            //Timber.w("Audio not ready, cannot start playback")
            return
        }

        try {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    _isAudioPlaying.value = true
                    isLoopingAudio = true
                    internalWordIndex = 0
                    _currentWordIndex.value = 0
                    
                    // Start monitoring playback position for word synchronization
                    startPositionMonitoring()
                    
                    //Timber.d("Audio playback started with looping")
                }
            } ?: run {
                // If no media player, simulate audio playback for testing
               // simulateAudioPlayback()
            }
        } catch (e: Exception) {
            //Timber.e("Failed to start audio: ${e.message}")
            // Fallback to simulation
           // simulateAudioPlayback()
        }
    }
    
    /**
     * Simulates audio playback for testing when no audio file is available
     */
    private fun simulateAudioPlayback() {
        _isAudioPlaying.value = true
        isLoopingAudio = true
        internalWordIndex = 0
        _currentWordIndex.value = 0
        
        //Timber.d("Simulating audio playback")
        
        // Simulate word progression
        Thread {
            var currentTime = 0L
            val totalDuration = AppConstants.IDLE_TIME_FOR_ONE_BEAD // ms for complete mantra
            
            while (_isAudioPlaying.value && isLoopingAudio) {
                try {
                    // Find current word based on time
                    val currentEntry = timingData.find { entry ->
                        currentTime >= entry.timestamp
                    }
                    //Timber.d(message = "index >>>> ${currentEntry?.wordIndex}, time >>>> $currentTime")
                    currentEntry?.let { entry ->
                        if (entry.wordIndex != internalWordIndex) {
                            internalWordIndex = entry.wordIndex
                            _currentWordIndex.value = entry.wordIndex
                            //Timber.d("Simulated highlighting word at index: $internalWordIndex, time: $currentTime")
                        }
                    }
                    
                    // Move to next word every 2 seconds
                    Thread.sleep(300)
                    currentTime += 300
                    
                    // Check if mantra is complete
                    if (currentTime >= totalDuration) {
                        //Timber.d("Simulated audio completed")
                        onAudioCompleteCallback?.invoke()
                        currentTime = 0
                        internalWordIndex = 0
                        _currentWordIndex.value = 0
                    }
                    
                } catch (e: Exception) {
                    //Timber.e("Error in simulated playback: ${e.message}")
                    break
                }
            }
        }.start()
    }
    
    /**
     * Sets callback to be called when audio completes one cycle
     */
    fun setOnAudioCompleteCallback(callback: () -> Unit) {
        onAudioCompleteCallback = callback
    }
    
    /**
     * Stops audio playback
     */
    fun stopAudio() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    player.seekTo(0)
                }
            }
            _isAudioPlaying.value = false
            isLoopingAudio = false
            _currentWordIndex.value = -1
            internalWordIndex = 0
            
            //Timber.d("Audio playback stopped")
        } catch (e: Exception) {
            //Timber.e("Failed to stop audio: ${e.message}")
        }
    }
    
    /**
     * Restarts audio from the beginning for next loop
     */
    private fun restartAudio() {
        try {
            mediaPlayer?.let { player ->
                player.seekTo(0)
                player.start()
                internalWordIndex = 0
                _currentWordIndex.value = 0
                //Timber.d("Audio restarted for next loop")
            }
        } catch (e: Exception) {
            //Timber.e("Failed to restart audio: ${e.message}")
        }
    }
    
    /**
     * Monitors playback position and updates word highlighting
     */
    private fun startPositionMonitoring() {
        Thread {
            while (_isAudioPlaying.value) {
                try {
                    val currentPosition = mediaPlayer?.currentPosition ?: 0
                    updateWordHighlighting(currentPosition)
                    Thread.sleep(100) // Check every 100ms
                  //  //Timber.d(message = "index >>>> $internalWordIndex, position >>>> $currentPosition")
                } catch (e: Exception) {
                    //Timber.e("Error in position monitoring: ${e.message}")
                    break
                }
            }
        }.start()
    }
    
    /**
     * Updates word highlighting based on current playback position
     */
    private fun updateWordHighlighting(currentPosition: Int) {
        if (timingData.isEmpty()) return

        // Binary search: find latest entry with timestamp <= currentPosition
        var low = 0
        var high = timingData.size - 1
        var idx = -1

        while (low <= high) {
            val mid = (low + high) ushr 1
            if (timingData[mid].timestamp <= currentPosition) {
                idx = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
        }

        val currentEntry = if (idx >= 0) timingData[idx] else null

        //Timber.d("updateWordHighlighting index >>>> ${currentEntry?.wordIndex}, time >>>> $currentPosition")

        currentEntry?.let { entry ->
            if (entry.wordIndex != internalWordIndex) {
                internalWordIndex = entry.wordIndex
                _currentWordIndex.value = entry.wordIndex
                //Timber.d("Highlighting word at index: $internalWordIndex, position: $currentPosition")
            }
        }
    }


    /**
     * Gets the complete mantra text
     */
    fun getMantraText(): String {
        return mantraWords.joinToString(" ")
    }
    
    /**
     * Gets the number of words in the mantra
     */
    fun getMantraWordCount(): Int {
        return mantraWords.size
    }
    
    /**
     * Releases audio resources
     */
    fun release() {
        try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
            mediaPlayer = null
            _isAudioPlaying.value = false
            _isAudioReady.value = false
            //Timber.d("Audio resources released")
        } catch (e: Exception) {
            //Timber.e("Error releasing audio resources: ${e.message}")
        }
    }
} 