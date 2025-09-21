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
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(initial + 1, viewModel.count.value)
    }

    @Test
    fun `decrementCount decreases count but not below zero`() = runTest {
        viewModel.decrementCount(true)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(0, viewModel.count.value)
        viewModel.incrementCount(true)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.decrementCount(true)
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(0, viewModel.count.value)
    }

    @Test
    fun `mala completes after 108 counts and logs are updated`() = runTest {
        // Test the mala completion logic step by step
        repeat((AppConstants.ONE_MALA_ROUND_COUNT)) {
            viewModel.incrementCount(true)
            testDispatcher.scheduler.advanceUntilIdle()
        }
        // After 107 increments, count should be 107
        assertEquals((AppConstants.ONE_MALA_ROUND_COUNT), viewModel.count.value)
        
        // The 108th increment should complete the mala and reset count to 0
        viewModel.incrementCount(true)
        testDispatcher.scheduler.advanceUntilIdle()
        
        // After 108 increments, the count should be 0 (mala completed and reset)
        assertEquals(0, viewModel.count.value)
        assertEquals(1, viewModel.malaNumber.value)
        assertEquals(1, viewModel.chantLogs.value.size)
    }

    @Test
    fun `autoChant toggles state`() = runTest {
        assertFalse(viewModel.isAutoChanting.value)
        viewModel.toggleAutoChant()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.isAutoChanting.value)
        viewModel.toggleAutoChant()
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.isAutoChanting.value)
    }

    @Test
    fun `manual increment does not work during autoChant`() = runTest {
        viewModel.toggleAutoChant()
        testDispatcher.scheduler.advanceUntilIdle()
        val before = viewModel.count.value
        viewModel.incrementCount(true)
        testDispatcher.scheduler.advanceUntilIdle()
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