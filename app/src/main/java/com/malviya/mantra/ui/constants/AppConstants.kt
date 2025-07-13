package com.malviya.mantra.ui.constants

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Centralized constants and configurable values for the MantraTV app
 * 
 * This object contains all the constants used throughout the application,
 * organized by category for better maintainability and consistency.
 * 
 * Note: Use const val for compile-time constants (primitive types: Long, Boolean, Int, String, etc.)
 *       Don't use const with non-primitive types (Array, List, etc.)
 */
object AppConstants {
    
    // ==================== MALA & CHANTING CONSTANTS ====================
    /** Total number of beads in one complete mala round */
    const val ONE_MALA_ROUND_COUNT: Int = 108
    
    /** Time interval between auto-chant beads in milliseconds */
    const val IDLE_TIME_FOR_ONE_BEAD: Long = 4800L
    
    // ==================== ANIMATION CONSTANTS ====================
    /** Duration of mala rotation animation in milliseconds */
    const val MALA_ROTATION_ANIMATION_DURATION: Int = 500
    
    /** Speed of marquee text animation in dp */
    const val MARQUEE_VELOCITY: Int = 50
    
    /** Number of iterations for marquee animation (infinite) */
    const val MARQUEE_ITERATIONS: Int = Int.MAX_VALUE
    
    // ==================== TIMING CONSTANTS ====================
    /** Threshold for very fast chanting feedback (in milliseconds) */
    const val VERY_FAST_THRESHOLD: Long = 2999L
    
    /** Threshold for fast chanting feedback (in milliseconds) */
    const val FAST_THRESHOLD: Long = 3500L
    
    /** Threshold for good chanting speed (in milliseconds) */
    const val GOOD_THRESHOLD: Long = 4900L
    
    /** Threshold for slower than usual chanting (in milliseconds) */
    const val SLOW_THAN_USUAL_THRESHOLD: Long = 6000L
    
    /** Threshold for slow chanting feedback (in milliseconds) */
    const val SLOW_THRESHOLD: Long = 8000L
    
    // ==================== FIREBASE ANALYTICS CONSTANTS ====================
    /** Firebase Analytics event name for auto-chant toggle */
    const val EVENT_AUTO_CHANT = "auto_chant"
    
    /** Firebase Analytics event name for manual chant actions */
    const val EVENT_MANUAL_CHANT = "manual_chant"
    
    /** Firebase Analytics event name for completed mala rounds */
    const val EVENT_SAMPURNA_MALA = "sampurna_mala"
    
    /** Firebase Analytics parameter key for auto-chant status */
    const val KEY_AUTO_CHANT_ENABLED = "auto_chant_enabled"
    
    /** Firebase Analytics parameter key for manual chant type */
    const val KEY_MANUAL_CHANT_TYPE = "manual_chant_type"
    
    /** Firebase Analytics parameter key for mala number */
    const val KEY_MALA_NUMBER = "mala_number"
    
    /** Firebase Analytics parameter key for time taken */
    const val KEY_TIME_TAKEN = "time_taken"
    
    /** Firebase Analytics parameter key for device model */
    const val KEY_DEVICE_MODEL = "device_model"
    
    // ==================== DIMENSIONS ====================
    /** UI dimensions and spacing constants */
    object Dimensions {
        // ==================== SPACING ====================
        /** Minimal spacing (1dp) */
        val SPACING_TINY = 1.dp
        
        /** Small spacing (2dp) */
        val SPACING_SMALL = 2.dp
        
        /** Medium spacing (8dp) */
        val SPACING_MEDIUM = 8.dp
        
        /** Large spacing (10dp) */
        val SPACING_LARGE = 10.dp
        
        /** Extra large spacing (16dp) */
        val SPACING_XLARGE = 16.dp
        
        /** Double extra large spacing (18dp) */
        val SPACING_XXLARGE = 18.dp
        
        /** Triple extra large spacing (32dp) */
        val SPACING_XXXLARGE = 32.dp
        
        // ==================== SIZES ====================
        /** Tiny size (4dp) */
        val SIZE_TINY = 4.dp
        
        /** Small size (8dp) */
        val SIZE_SMALL = 8.dp
        
        /** Medium size (140dp) - Main circle size */
        val SIZE_MEDIUM = 140.dp
        
        /** Large size (150dp) - Circle border size */
        val SIZE_LARGE = 150.dp
        
        /** Extra large size (280dp) - Mala canvas size */
        val SIZE_XLARGE = 280.dp
        
        /** Double extra large size (300dp) - Mala container size */
        val SIZE_XXLARGE = 300.dp
        
        // ==================== CIRCLE AND BEAD SIZES ====================
        /** Main circle size for counter display (140dp) */
        val CIRCLE_MAIN_SIZE = 140.dp
        
        /** Border circle size for bead indicators (150dp) */
        val CIRCLE_BORDER_SIZE = 150.dp
        
        /** Radius of mala beads (8dp) */
        val BEAD_RADIUS = 8.dp
        
        /** Width of bead outline stroke (1dp) */
        val BEAD_OUTLINE_WIDTH = 1.dp
        
        /** Radius of center point in mala (4dp) */
        val CENTER_POINT_RADIUS = 4.dp
        
        /** Radius of small indicator circles (3dp) */
        val SMALL_CIRCLE_RADIUS = 3.dp
        
        // ==================== MALA VISUALIZATION ====================
        /** Container size for mala visualization (300dp) */
        val MALA_CONTAINER_SIZE = 300.dp
        
        /** Canvas size for mala drawing (280dp) */
        val MALA_CANVAS_SIZE = 280.dp
        
        /** Factor for mala radius calculation (size.minDimension / 2.5f) */
        val MALA_RADIUS_FACTOR = 2.5f
        
        /** Radius of center point in mala visualization (4dp) */
        val MALA_CENTER_POINT_RADIUS = 4.dp
    }
    
    // ==================== TYPOGRAPHY ====================
    /** Typography constants for consistent text styling */
    object Typography {
        // ==================== FONT SIZES ====================
        /** Tiny font size (8sp) - Watermark text */
        val FONT_SIZE_TINY = 8.sp
        
        /** Small font size (18sp) - Flash messages */
        val FONT_SIZE_SMALL = 18.sp
        
        /** Medium font size (20sp) - Feedback text */
        val FONT_SIZE_MEDIUM = 20.sp
        
        /** Large font size (24sp) - Logs and auto-chant button */
        val FONT_SIZE_LARGE = 24.sp
        
        /** Extra large font size (28sp) - Screen selector title */
        val FONT_SIZE_XLARGE = 28.sp
        
        /** Double extra large font size (34sp) - Increment/decrement buttons */
        val FONT_SIZE_XXLARGE = 34.sp
        
        /** Triple extra large font size (38sp) - Mantra text */
        val FONT_SIZE_XXXLARGE = 38.sp
        
        /** Counter font size (44sp) - Main counter display */
        val FONT_SIZE_COUNTER = 44.sp
        
        // ==================== LINE HEIGHTS ====================
        /** Line height for mantra text (80sp) */
        val LINE_HEIGHT_MANTRA = 80.sp
        
        /** Default line height (24sp) */
        val LINE_HEIGHT_DEFAULT = 24.sp
        
        /** Title line height (28sp) */
        val LINE_HEIGHT_TITLE = 28.sp
        
        /** Caption line height (16sp) */
        val LINE_HEIGHT_CAPTION = 16.sp
        
        // ==================== LETTER SPACING ====================
        /** Default letter spacing (0.5sp) */
        val LETTER_SPACING_DEFAULT = 0.5.sp
        
        /** Title letter spacing (0sp) */
        val LETTER_SPACING_TITLE = 0.sp
    }
    
    // ==================== LAYOUT CONSTANTS ====================
    /** Layout constants for positioning and sizing */
    object Layout {
        /** Fill factor for maximum size (1.0f) */
        val FILL_MAX_SIZE = 1f
        
        /** Fill factor for maximum width (1.0f) */
        val FILL_MAX_WIDTH = 1f
        
        /** Center alignment factor (0.5f) */
        val CENTER_ALIGNMENT = 0.5f
        
        /** Circle radius factor (0.5f) - size.minDimension / 2 */
        val CIRCLE_RADIUS_FACTOR = 0.5f
    }
    
    // ==================== TIME FORMATTING ====================
    /** Time formatting constants for readable time display */
    object TimeFormat {
        /** Number of seconds in a minute */
        const val SECONDS_IN_MINUTE = 60
        
        /** Number of seconds in an hour */
        const val SECONDS_IN_HOUR = 3600
        
        /** Number of milliseconds in a second */
        const val MILLISECONDS_IN_SECOND = 1000
        
        /** Number of milliseconds in a minute */
        const val MILLISECONDS_IN_MINUTE = 60000
        
        /** Time format string for hours:minutes:seconds */
        const val TIME_FORMAT_HOURS = "%02d:%02d:%02d"
        
        /** Time format string for minutes:seconds */
        const val TIME_FORMAT_MINUTES = "%02d:%02d"
        
        /** Time format string for seconds only */
        const val TIME_FORMAT_SECONDS = "%02d"
        
        /** Suffix for hours display */
        const val SUFFIX_HOURS = " hr"
        
        /** Suffix for minutes display */
        const val SUFFIX_MINUTES = " min"
        
        /** Suffix for seconds display */
        const val SUFFIX_SECONDS = " sec"
    }
    
    // ==================== UI BEHAVIOR ====================
    /** UI behavior constants for specific UI elements */
    object UI {
        /** Maximum number of lines for flash message */
        const val MAX_LINES_FLASH_MESSAGE = 1
        
        /** Separator used in watermark text */
        const val WATERMARK_SEPARATOR = "\t\t"
        
        /** Prefix for version number in watermark */
        const val WATERMARK_PREFIX = "v"
    }
} 