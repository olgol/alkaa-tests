package com.escodro.alkaa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.shared.MainView
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.junit4.DisplayName
import io.qameta.allure.kotlin.junit4.Tag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
class MoreTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    @Tag("Regress")
    @DisplayName("Check 'More' screen")
    fun checkMoreChapter() {
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Check screen 'More'")
            onNodeWithText("More").performClick()

            onNodeWithText("FEATURES").assertIsDisplayed()
            onNodeWithText("Task Tracker").assertIsDisplayed()

            onNodeWithText("SETTINGS").assertIsDisplayed()
            onNodeWithText("App theme").assertIsDisplayed()
            onNodeWithText("About").assertIsDisplayed()
            onNodeWithText("Open source licenses").assertIsDisplayed()
            onNodeWithText("Version").assertIsDisplayed()
        }
    }
}