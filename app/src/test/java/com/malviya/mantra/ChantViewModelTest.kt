package com.malviya.mantra


import com.malviya.mantra.ui.constants.AppConstants
import com.malviya.mantra.ui.viewmodel.ChantViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class ChantViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ChantViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ChantViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `incrementCount increases count`() = runTest {
        val initial = viewModel.count.value
        viewModel.incrementCount(true)
        assertEquals(initial + 1, viewModel.count.value)
    }

    @Test
    fun `decrementCount decreases count but not below zero`() = runTest {
        viewModel.decrementCount(true)
        assertEquals(0, viewModel.count.value)
        viewModel.incrementCount(true)
        viewModel.decrementCount(true)
        assertEquals(0, viewModel.count.value)
    }

    @Test
    fun `mala completes after 108 counts and logs are updated`() = runTest {
        repeat(AppConstants.ONE_MALA_ROUND_COUNT) { viewModel.incrementCount(true) }
        assertEquals(0, viewModel.count.value)
        assertEquals(1, viewModel.malaNumber.value)
        assertEquals(1, viewModel.chantLogs.value.size)
    }

    @Test
    fun `autoChant toggles state`() = runTest {
        assertFalse(viewModel.isAutoChanting.value)
        viewModel.toggleAutoChant()
        assertTrue(viewModel.isAutoChanting.value)
        viewModel.toggleAutoChant()
        assertFalse(viewModel.isAutoChanting.value)
    }

    @Test
    fun `manual increment does not work during autoChant`() = runTest {
        viewModel.toggleAutoChant()
        val before = viewModel.count.value
        viewModel.incrementCount(true)
        assertEquals(before, viewModel.count.value)
    }

    @Test
    fun `getCircleColor sets correct feedback and color`() = runTest {
        viewModel.getCircleColor(2000)
        assertTrue(viewModel.chantFeedback.value is ChantViewModel.ChantFeedback.VeryFast)
        viewModel.getCircleColor(4000)
        assertTrue(viewModel.chantFeedback.value is ChantViewModel.ChantFeedback.Good)
        viewModel.getCircleColor(7000)
        assertTrue(viewModel.chantFeedback.value is ChantViewModel.ChantFeedback.Slow)
    }

    @Test
    fun `convertMillisToReadableTime formats correctly`() {
        assertEquals("01:01:01 hr", viewModel.convertMillisToReadableTime(3661000))
        assertEquals("01:01 min", viewModel.convertMillisToReadableTime(61000))
        assertEquals("59 sec", viewModel.convertMillisToReadableTime(59000))
    }
}