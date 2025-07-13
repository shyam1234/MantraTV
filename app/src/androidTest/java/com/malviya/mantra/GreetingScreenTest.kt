package com.malviya.mantra

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.malviya.mantra.ui.screen.GreetingScreen
import com.malviya.mantra.ui.viewmodel.ChantViewModel
import org.junit.Rule
import org.junit.Test

class GreetingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun mantraText_isDisplayed() {
        val viewModel = ChantViewModel()
        composeTestRule.setContent {
            GreetingScreen("Test Mantra", viewModel)
        }
        composeTestRule.onNodeWithText("Test Mantra").assertIsDisplayed()
    }

    @Test
    fun incrementButton_increasesCount() {
        val viewModel = ChantViewModel()
        composeTestRule.setContent {
            GreetingScreen("Test", viewModel)
        }
        val plusButton = composeTestRule.onAllNodesWithText("+1").onFirst()
        plusButton.performClick()
        composeTestRule.onNodeWithText("1").assertIsDisplayed()
    }

    @Test
    fun decrementButton_decreasesCount() {
        val viewModel = ChantViewModel()
        composeTestRule.setContent {
            GreetingScreen("Test", viewModel)
        }
        val plusButton = composeTestRule.onAllNodesWithText("+1").onFirst()
        val minusButton = composeTestRule.onAllNodesWithText("-1").onFirst()
        plusButton.performClick()
        minusButton.performClick()
        composeTestRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun autoChantButton_togglesState() {
        val viewModel = ChantViewModel()
        composeTestRule.setContent {
            GreetingScreen("Test", viewModel)
        }
        val autoChantButton = composeTestRule.onAllNodesWithText("ऑटो जाप बंद").onFirst()
        autoChantButton.performClick()
        composeTestRule.onAllNodesWithText("ऑटो जाप चालू").onFirst().assertIsDisplayed()
    }
}