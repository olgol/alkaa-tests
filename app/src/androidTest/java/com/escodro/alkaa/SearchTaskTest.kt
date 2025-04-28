package com.escodro.alkaa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.escodro.shared.MainView
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Step
import io.qameta.allure.kotlin.junit4.DisplayName
import io.qameta.allure.kotlin.junit4.Tag
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.apply

@RunWith(AllureAndroidJUnit4::class)
class SearchTaskTest {
    @get:Rule
    val composeRule = createComposeRule()


    @Test
    @Tag("Regress")
    @DisplayName("Searching existing task")
    fun searchTask() {
        val taskName = "Task for Search"
        val taskCategory = "Personal"
        composeRule.setContent { MainView() }
        composeRule.apply {
            addTask(taskName)
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()

            Allure.step("Search task $taskName")
            onNodeWithContentDescription("Search").performTextInput(taskName)
            onAllNodesWithText(taskName).filter(! isEditable()).get(0).assertIsDisplayed()

            Allure.step("Complete task $taskName")
            onNodeWithText("Tasks").performClick()
            onNodeWithText(taskName).assertIsDisplayed()
            onNodeWithText(taskName).onChild().performClick()
        }
    }

    @Test
    @Tag("Regress")
    @DisplayName("Searching non-existing task")
    fun searchNonExistentTask() {
        val taskName = "Task is not exist"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Search task 'taskName'")
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()
            onNodeWithContentDescription("Search").performTextInput(taskName)
            onNodeWithText("No tasks found").assertIsDisplayed()
        }
    }

    @Step("Add task {name}")
    fun addTask(name: String) {
        Allure.step("Create task $name")
        composeRule.onNodeWithContentDescription("Add task").performClick()
        composeRule.onAllNodes(hasText("Task", substring = true)).get(2).performTextInput(name)
        composeRule.onNodeWithText("Add").performClick()
    }
}