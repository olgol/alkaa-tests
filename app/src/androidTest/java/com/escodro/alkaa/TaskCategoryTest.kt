package com.escodro.alkaa

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import com.escodro.shared.MainView
import io.qameta.allure.android.runners.AllureAndroidJUnit4
import io.qameta.allure.kotlin.Allure
import io.qameta.allure.kotlin.Step
import io.qameta.allure.kotlin.junit4.DisplayName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AllureAndroidJUnit4::class)
class TaskCategoryTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    @DisplayName("Create category with existing name")
    fun createExistingCategory() {
        val categoryName = "Personal"

        composeRule.setContent { MainView() }
        composeRule.onNodeWithText("Categories").performClick()

        if (!isCategoryExist(categoryName)) {
            createCategory(categoryName)
            composeRule.onNodeWithText(categoryName).assertExists()
        }

        composeRule.onNodeWithText(categoryName).performClick()
        createCategory(categoryName)
        composeRule.onNodeWithText("Wow! All tasks are completed!").assertIsDisplayed()
    }

    @Test
    @DisplayName("Creating new category")
    fun newCategoryCreate() {
        val categoryName = "Personal"
        composeRule.setContent { MainView() }
        composeRule.onNodeWithText("Categories").performClick()

        if (isCategoryExist(categoryName)) {
            composeRule.onNodeWithText(categoryName).performClick()
            deleteCategory(categoryName)
        }

        createCategory(categoryName)
        composeRule.onNodeWithText(categoryName).assertExists()
    }

    @Test
    @DisplayName("Update existing category")
    fun updateExistingCategory() {
        val oldCategoryName = "Personal"
        val newCategoryName = "Shared"

        composeRule.setContent { MainView() }
        composeRule.onNodeWithText("Categories").performClick()

        if (!isCategoryExist(oldCategoryName)) {
            createCategory(oldCategoryName)
            composeRule.onNodeWithText(oldCategoryName).assertExists()
        }

        composeRule.onNodeWithText(oldCategoryName).performClick()
        updateCategory(newCategoryName)
        composeRule.onNodeWithText(newCategoryName).assertExists()
    }

    @Test
    @DisplayName("Delete category")
    fun checkDeleteCategory() {
        val categoryName = "Personal"
        composeRule.setContent { MainView() }
        composeRule.onNodeWithText("Categories").performClick()

        if (!isCategoryExist(categoryName)) {
            createCategory(categoryName)
            composeRule.onNodeWithText(categoryName).assertExists()
        }

        composeRule.apply {
            onNodeWithText(categoryName).performClick()
            deleteCategory(categoryName)
            onNodeWithText(categoryName).assertDoesNotExist()
        }
    }

    @Step("Check {categoryName} exist")
    fun isCategoryExist(categoryName: String): Boolean {
       return (composeRule.onNodeWithText(categoryName).isDisplayed())
    }

    @Step("Create category with name {categoryName}")
    fun createCategory(categoryName: String) {
        Allure.step("Create category $categoryName")
        composeRule.onNodeWithContentDescription("Add category").performClick()
        composeRule.onNodeWithText("Category").performTextInput(categoryName)
        composeRule.onNodeWithText("Save").performClick()
    }

    @Step("Delete category with {name}")
    fun deleteCategory(categoryName: String) {
        Allure.step("Remove category $categoryName")
        composeRule.onNodeWithContentDescription("Remove").performClick()
        composeRule.onNodeWithText("Remove").performClick()
    }

    @Step("Update category name to {newCategoryName}")
    fun updateCategory(newCategoryName: String) {
        Allure.step("Update category name to $newCategoryName")
        composeRule.onNodeWithText("Category").performTextReplacement(newCategoryName)
        composeRule.onNodeWithText("Save").performClick()
    }
}