package com.malviya.mantra.ui.settings

import java.util.Locale
import kotlin.math.abs

/**
 * Playback speeds for one mantra cycle. 1.0 matches the recorded chant.
 */
object ChantSpeeds {
    const val DEFAULT = 1.0f
    val steps: List<Float> = listOf(0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
    val min: Float = steps.first()
    val max: Float = steps.last()

    fun nearest(speed: Float): Float = steps.minBy { abs(it - speed) }

    fun slower(speed: Float): Float {
        val index = steps.indexOf(nearest(speed))
        return steps[(index - 1).coerceAtLeast(0)]
    }

    fun faster(speed: Float): Float {
        val index = steps.indexOf(nearest(speed))
        return steps[(index + 1).coerceAtMost(steps.lastIndex)]
    }

    fun format(speed: Float): String {
        val trimmed = String.format(Locale.US, "%.2f", nearest(speed))
            .trimEnd('0')
            .trimEnd('.')
        return if (trimmed.contains('.')) trimmed else "$trimmed.0"
    }
}
