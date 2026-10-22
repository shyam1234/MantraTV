package com.malviya.mantra.ui.components

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScreenFitTest {

    @Test
    fun phone_usesTightPaddingAndReadableType() {
        val fit = screenFitFor(widthDp = 384, heightDp = 853, isTelevision = false)

        assertEquals(FormFactor.Phone, fit.formFactor)
        assertEquals(12f, fit.horizontalPadding.value, 0.1f)
        assertEquals(8f, fit.verticalPadding.value, 0.1f)
        assertEquals(20f, fit.mantraFontSize.value, 0.1f)
        assertTrue(fit.horizontalPadding.value * 2 < 384)
    }

    @Test
    fun television_keepsContentInsideOverscan() {
        val fit = screenFitFor(widthDp = 961, heightDp = 541, isTelevision = true)

        assertEquals(FormFactor.Tv, fit.formFactor)
        assertTrue(fit.horizontalPadding.value >= 48f)
        assertTrue(fit.verticalPadding.value >= 27f)
        assertTrue(fit.horizontalPadding.value * 2 < 961)
        assertTrue(fit.verticalPadding.value * 2 < 541)
        assertTrue(fit.mantraFontSize.value >= 20f)
    }

    @Test
    fun tablet_isLargerThanPhoneWithoutTvOverscan() {
        val phone = screenFitFor(widthDp = 384, heightDp = 853, isTelevision = false)
        val tablet = screenFitFor(widthDp = 800, heightDp = 1280, isTelevision = false)

        assertEquals(FormFactor.Tablet, tablet.formFactor)
        assertTrue(tablet.mantraFontSize.value > phone.mantraFontSize.value)
        assertTrue(tablet.horizontalPadding.value < 48f)
    }

    @Test
    fun shortPhone_usesCompactSpacing() {
        val fit = screenFitFor(widthDp = 800, heightDp = 360, isTelevision = false)

        assertEquals(FormFactor.Phone, fit.formFactor)
        assertTrue(fit.compact)
        assertEquals(4f, fit.verticalPadding.value, 0.1f)
        assertTrue(fit.mantraFontSize.value < 20f)
    }
}
