package com.malviya.mantra.ui.constants

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized constants and configurable values for the MantraTV app
 */
object AppConstants {
    
    // ==================== MALA & CHANTING CONSTANTS ====================
    const val ONE_MALA_ROUND_COUNT: Int = 108
    const val IDLE_TIME_FOR_ONE_BEAD: Long = 4800L // milliseconds
    
    // ==================== ANIMATION CONSTANTS ====================
    const val MALA_ROTATION_ANIMATION_DURATION: Int = 500 // milliseconds
    const val MARQUEE_VELOCITY: Int = 50 // dp
    const val MARQUEE_ITERATIONS: Int = Int.MAX_VALUE
    
    // ==================== TIMING CONSTANTS ====================
    const val VERY_FAST_THRESHOLD: Long = 2999L // milliseconds
    const val FAST_THRESHOLD: Long = 3500L // milliseconds
    const val GOOD_THRESHOLD: Long = 4900L // milliseconds
    const val SLOW_THAN_USUAL_THRESHOLD: Long = 6000L // milliseconds
    const val SLOW_THRESHOLD: Long = 8000L // milliseconds
    
    // ==================== FIREBASE ANALYTICS CONSTANTS ====================
    const val EVENT_AUTO_CHANT = "auto_chant"
    const val EVENT_MANUAL_CHANT = "manual_chant"
    const val EVENT_SAMPURNA_MALA = "sampurna_mala"
    const val KEY_AUTO_CHANT_ENABLED = "auto_chant_enabled"
    const val KEY_MANUAL_CHANT_TYPE = "manual_chant_type"
    const val KEY_MALA_NUMBER = "mala_number"
    const val KEY_TIME_TAKEN = "time_taken"
    const val KEY_DEVICE_MODEL = "device_model"
    
    // ==================== DIMENSIONS ====================
    object Dimensions {
        // Spacing
        val SPACING_TINY = 1.dp
        val SPACING_SMALL = 2.dp
        val SPACING_MEDIUM = 8.dp
        val SPACING_LARGE = 10.dp
        val SPACING_XLARGE = 16.dp
        val SPACING_XXLARGE = 18.dp
        val SPACING_XXXLARGE = 32.dp
        
        // Sizes
        val SIZE_TINY = 4.dp
        val SIZE_SMALL = 8.dp
        val SIZE_MEDIUM = 140.dp
        val SIZE_LARGE = 150.dp
        val SIZE_XLARGE = 280.dp
        val SIZE_XXLARGE = 300.dp
        
        // Circle and bead sizes
        val CIRCLE_MAIN_SIZE = 140.dp
        val CIRCLE_BORDER_SIZE = 150.dp
        val BEAD_RADIUS = 8.dp
        val BEAD_OUTLINE_WIDTH = 1.dp
        val CENTER_POINT_RADIUS = 4.dp
        val SMALL_CIRCLE_RADIUS = 3.dp
        
        // Mala visualization
        val MALA_CONTAINER_SIZE = 300.dp
        val MALA_CANVAS_SIZE = 280.dp
        val MALA_RADIUS_FACTOR = 2.5f // size.minDimension / 2.5f
        val MALA_CENTER_POINT_RADIUS = 4.dp
    }
    
    // ==================== TYPOGRAPHY ====================
    object Typography {
        // Font sizes
        val FONT_SIZE_TINY = 8.sp
        val FONT_SIZE_SMALL = 18.sp
        val FONT_SIZE_MEDIUM = 20.sp
        val FONT_SIZE_LARGE = 24.sp
        val FONT_SIZE_XLARGE = 28.sp
        val FONT_SIZE_XXLARGE = 34.sp
        val FONT_SIZE_XXXLARGE = 38.sp
        val FONT_SIZE_COUNTER = 44.sp
        
        // Line heights
        val LINE_HEIGHT_MANTRA = 80.sp
        val LINE_HEIGHT_DEFAULT = 24.sp
        val LINE_HEIGHT_TITLE = 28.sp
        val LINE_HEIGHT_CAPTION = 16.sp
        
        // Letter spacing
        val LETTER_SPACING_DEFAULT = 0.5.sp
        val LETTER_SPACING_TITLE = 0.sp
    }
    
    // ==================== LAYOUT CONSTANTS ====================
    object Layout {
        val FILL_MAX_SIZE = 1f
        val FILL_MAX_WIDTH = 1f
        val CENTER_ALIGNMENT = 0.5f
        val CIRCLE_RADIUS_FACTOR = 0.5f // size.minDimension / 2
    }
    
    // ==================== TIME FORMATTING ====================
    object TimeFormat {
        const val SECONDS_IN_MINUTE = 60
        const val SECONDS_IN_HOUR = 3600
        const val MILLISECONDS_IN_SECOND = 1000
        const val MILLISECONDS_IN_MINUTE = 60000
        
        const val TIME_FORMAT_HOURS = "%02d:%02d:%02d"
        const val TIME_FORMAT_MINUTES = "%02d:%02d"
        const val TIME_FORMAT_SECONDS = "%02d"
        
        const val SUFFIX_HOURS = " hr"
        const val SUFFIX_MINUTES = " min"
        const val SUFFIX_SECONDS = " sec"
    }
    
    // ==================== UI BEHAVIOR ====================
    object UI {
        const val MAX_LINES_FLASH_MESSAGE = 1
        const val WATERMARK_SEPARATOR = "\t\t"
        const val WATERMARK_PREFIX = "v"
    }
} 