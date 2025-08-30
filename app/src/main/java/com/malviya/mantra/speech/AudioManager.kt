package com.malviya.mantra.speech

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import java.util.*

/**
 * Audio manager for handling Text-to-Speech functionality
 * 
 * Manages TTS initialization, playback, and word synchronization
 * for the mantra chanting feature.
 */
class AudioManager(private val context: Context) {
    
    private var textToSpeech: TextToSpeech? = null
    
    // State flows for audio management
    private val _isAudioPlaying = MutableStateFlow(false)
    val isAudioPlaying: StateFlow<Boolean> = _isAudioPlaying.asStateFlow()
    
    private val _currentWordIndex = MutableStateFlow(-1)
    val currentWordIndex: StateFlow<Int> = _currentWordIndex.asStateFlow()
    
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady.asStateFlow()
    
    // Mantra words for synchronization
    private val mantraWords = listOf(
        "हरे", "कृष्ण,", "हरे", "कृष्ण,", "कृष्ण", "कृष्ण,", "हरे", "हरे|",
        "हरे", "राम,", "हरे", "राम,", "राम", "राम,", "हरे", "हरे||"
    )
    private val mantraWordsForPronouns = listOf(
    "Huh-ray", "Krish-naa", "Huh-ray", "Krish-naa",
    "Krish-naa", "Krish-naa", "Huh-ray", "Huh-ray",
    "Huh-ray", "Raam", "Huh-ray", "Raam",
    "Raam", "Raam", "Huh-ray", "Huh-ray")
    
    private var currentWordIndexInternal = 0
    private var isLooping = false
    
    init {
        initializeTts()
    }
    
    /**
     * Initializes Text-to-Speech engine
     */
    private fun initializeTts() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Configure TTS settings for better audio quality
                textToSpeech?.setPitch(0.8f) // Slightly lower pitch
                textToSpeech?.setSpeechRate(1.0f) // Slower speech rate for clarity
                
                val result = textToSpeech?.setLanguage(Locale("hi", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Fallback to English if Hindi is not available
                    val englishResult = textToSpeech?.setLanguage(Locale.ENGLISH)
                    if (englishResult == TextToSpeech.LANG_MISSING_DATA || englishResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Timber.e("TTS Language not supported")
                    } else {
                        setupTtsListener()
                        _isTtsReady.value = true
                    }
                } else {
                    setupTtsListener()
                    _isTtsReady.value = true
                }
            } else {
                Timber.e("TTS initialization failed")
            }
        }
    }

    /**
     * Sets up TTS utterance progress listener for word synchronization
     */
    private fun setupTtsListener() {
        textToSpeech?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                Timber.d("TTS started: $utteranceId")
            }
            
            override fun onDone(utteranceId: String?) {
                Timber.d("TTS completed: $utteranceId")
                if (isLooping && _isAudioPlaying.value) {
                    // Add delay before moving to next word to control speed
                    android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                        // Move to next word or restart if at end
                        currentWordIndexInternal++
                        if (currentWordIndexInternal >= mantraWords.size) {
                            currentWordIndexInternal = 0
                        }
                        _currentWordIndex.value = currentWordIndexInternal
                        speakCurrentWord()
                    }, 0) // 2 second delay between words for slower highlighting
                }
            }
            
            override fun onError(utteranceId: String?) {
                Timber.e("TTS error: $utteranceId")
                _isAudioPlaying.value = false
            }
        })
    }
    
    /**
     * Starts audio playback with looping
     */
    fun startAudio() {
        Timber.d("startAudio called, TTS ready: ${_isTtsReady.value}")
        if (!_isTtsReady.value) {
            Timber.w("TTS not ready, cannot start audio")
            return
        }
        
        _isAudioPlaying.value = true
        isLooping = true
        currentWordIndexInternal = 0
        _currentWordIndex.value = 0
        Timber.d("Starting audio playback with word: ${mantraWords[0]}")
        speakCurrentWord()
    }
    
    /**
     * Stops audio playback
     */
    fun stopAudio() {
        _isAudioPlaying.value = false
        isLooping = false
        _currentWordIndex.value = -1
        textToSpeech?.stop()
    }
    
    /**
     * Speaks the current word in the mantra
     */
    private fun speakCurrentWord() {
        if (!_isAudioPlaying.value || currentWordIndexInternal >= mantraWords.size) return
        
        val word = mantraWordsForPronouns[currentWordIndexInternal]
        val utteranceId = "word_$currentWordIndexInternal"
        
        Timber.d("Speaking word: '$word' at index: $currentWordIndexInternal")
        
        val result = textToSpeech?.speak(
            word,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )
        
        if (result == TextToSpeech.ERROR) {
            Timber.e("Failed to speak word: $word")
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
     * Test method to verify TTS is working
     */
    fun testTts() {
        if (_isTtsReady.value) {
            Timber.d("Testing TTS with word: 'Krishna'")
            textToSpeech?.speak("Krishna", TextToSpeech.QUEUE_FLUSH, null, "test_utterance")
        } else {
            Timber.w("TTS not ready for testing")
        }
    }
    
    /**
     * Releases TTS resources
     */
    fun release() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
} 