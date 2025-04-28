package com.escodro.alkaa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEditable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.escodro.shared.MainView
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Step
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep
import kotlin.apply

@RunWith(AllureAndroidJUnit4::class)
class TasksTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    @DisplayName("Create task with category")
    fun createTaskWithCategory() {
        val taskName = "Task for create task testing"
        val taskCategory = "Work"

        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Create task $taskName with category $taskCategory")
            onNodeWithContentDescription("Add task").performClick()
            onAllNodes(hasText("Task", substring = true)).get(2).performTextInput(taskName)
            onAllNodes(hasText(taskCategory, substring = true)).get(1).performClick()
            onNodeWithText("Add").performClick()
            onNodeWithText(taskName).assertIsDisplayed()
            onNodeWithText(taskCategory).performClick()
            onNodeWithText(taskName).assertIsDisplayed()
            onNodeWithText("Personal").performClick()
            onNodeWithText(taskName).assertDoesNotExist()
            onNodeWithText(taskCategory).performClick()
            completeTask(taskName)
        }
    }

    @Test
    @DisplayName("Create task with no category")
    fun createTaskWithoutCategory() {
        val taskName = "Task with no category"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Create task $taskName with no category")
            addTask(taskName)
            onNodeWithText(taskName).assertIsDisplayed()
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()
            onNodeWithContentDescription("Search").performTextInput(taskName)
            onNodeWithText("Tasks").performClick()
            completeTask(taskName)

        }
    }

    @Test
    @DisplayName("Add category to task")
    fun addCategoryToTaskWithoutCategory() {
        val taskName = "Task with no category"
        val taskCategory = "Personal"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Add category $taskCategory to task $taskName")
            addTask(taskName)
            onNodeWithText(taskName).assertIsDisplayed()
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()
            onNodeWithContentDescription("Search").performTextInput(taskName)
            onAllNodesWithText(taskName).get(1).performClick()
            selectTaskCategory(taskCategory)
            onNodeWithText("Tasks").performClick()
            onNodeWithText("Work").performClick()
            onNodeWithText(taskName).assertIsNotDisplayed()
            onNodeWithText("Personal").performClick()
            onNodeWithText(taskName).assertIsDisplayed()
            completeTask(taskName)
        }
    }

    @Test
    @DisplayName("Create task with empty name")
    fun cannotCreateEmptyTask() {
        composeRule.setContent { MainView() }

        composeRule.apply {
            Allure.step("Create task with empty name")
            onNodeWithContentDescription("Add task").performClick()
            onNodeWithText("Add").performClick()
            onNodeWithText("Wow! All tasks are completed!").assertIsDisplayed()

            Allure.step("Search task with empty name")
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()
            onNodeWithContentDescription("Search").performTextInput("")
            onNodeWithText("taskName").assertIsNotDisplayed()
        }
    }

    @Test
    @DisplayName("Create task without description")
    fun createTaskWoDescription() {
        val taskName = "Task without description"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Create task $taskName without description")
            addTask(taskName)
            onNodeWithText(taskName).assertIsDisplayed()
            completeTask(taskName)
        }
    }

    @Test
    @DisplayName("Create task with description")
    fun createTaskWithDescription() {
        val taskName = "task with description"
        val description = "Task with description"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Create task $taskName with description $description")
            addTask(taskName)
            onNodeWithText(taskName).performClick()
            fillTaskDescription(description)
            onNodeWithContentDescription("Back").performClick()
            onNodeWithText(taskName).performClick()
            onNodeWithContentDescription("Description").assertTextEquals(description)
            onNodeWithContentDescription("Back").performClick()
            onNodeWithText(taskName).assertIsDisplayed()
            completeTask(taskName)
        }
    }



    @Test
    @DisplayName("Complete task")
    fun completeTask() {
        val taskName = "Task for complete"
        composeRule.setContent { MainView() }
        composeRule.apply {
            Allure.step("Complete task $taskName")
            addTask(taskName)
            completeTask(taskName)
            onNodeWithText("Task completed").assertIsDisplayed()
            onNodeWithText(taskName).assertIsNotDisplayed()
            onNodeWithContentDescription("Search", useUnmergedTree = true).performClick()
            onNodeWithText(taskName).assertIsDisplayed()
        }
    }

    @Step("Add task {name}")
    fun addTask(name: String) {
        Allure.step("Create task $name")
        composeRule.onNodeWithContentDescription("Add task").performClick()
        composeRule.onAllNodes(hasText("Task", substring = true)).get(2).performTextInput(name)
        composeRule.onNodeWithText("Add").performClick()
    }

    @Step("Fill task description {description}")
    fun fillTaskDescription(description: String) {

        Allure.step("Add description to task")
        composeRule.onNodeWithContentDescription("Description").performClick()
        composeRule. onNodeWithContentDescription("Description").performTextInput(description)

    }

    @Step("Select task category {categoryName}")
    fun selectTaskCategory(categoryName: String) {
        Allure.step("Select task category")
        composeRule.onNodeWithText(categoryName).performClick()
    }

    @Step("Complete task {name}")
    fun completeTask(name: String) {
        Allure.step("Complete task $name")
        composeRule.onNodeWithText(name).onChild().performClick()
    }

}