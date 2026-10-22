package com.malviya.mantra.ui.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ChantSpeedsTest {

    @Test
    fun nearest_snapsToASupportedSpeed() {
        assertEquals(1.0f, ChantSpeeds.nearest(0.9f))
        assertEquals(1.0f, ChantSpeeds.nearest(1.1f))
        assertEquals(2.0f, ChantSpeeds.nearest(9f))
        assertEquals(0.5f, ChantSpeeds.nearest(0.1f))
    }

    @Test
    fun slowerAndFaster_stayInsideTheRange() {
        assertEquals(0.75f, ChantSpeeds.slower(1.0f))
        assertEquals(1.25f, ChantSpeeds.faster(1.0f))
        assertEquals(ChantSpeeds.min, ChantSpeeds.slower(ChantSpeeds.min))
        assertEquals(ChantSpeeds.max, ChantSpeeds.faster(ChantSpeeds.max))
    }

    @Test
    fun format_keepsQuarterStepsReadable() {
        assertEquals("1.0", ChantSpeeds.format(1.0f))
        assertEquals("1.25", ChantSpeeds.format(1.25f))
        assertEquals("0.5", ChantSpeeds.format(0.5f))
        assertEquals("1.75", ChantSpeeds.format(1.75f))
    }
}
