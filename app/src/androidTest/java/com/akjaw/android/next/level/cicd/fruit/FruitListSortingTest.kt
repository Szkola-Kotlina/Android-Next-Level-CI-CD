package com.akjaw.android.next.level.cicd.fruit

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performKeyInput
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.test.printToString
import androidx.compose.ui.test.swipeUp
import com.akjaw.android.next.level.cicd.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class FruitListSortingTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        FruitApiProvider.fruitApi = FakeFruitApi(FakeFruitApi.FRUITS)
    }

    @Test
    fun sortingByCarbohydratesWorks() {
        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Carbohydrates").performClick()

        composeTestRule.onNodeWithText("Apricot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Blueberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Avocado").assertIsDisplayed()
    }

    @Test
    fun sortingByProteinWorks() {
        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Protein").performClick()

        composeTestRule.onNodeWithText("Blueberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cranberry").assertIsDisplayed()
    }

    @Test
    fun sortingByFatWorks() {
        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Fat").performClick()

        composeTestRule.onNodeWithText("Apricot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cranberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Banana").assertIsDisplayed()
    }

    @Test
    fun sortingByCaloriesWorks() {
        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Calories").performClick()

        composeTestRule.onNodeWithText("Apricot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Blueberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Blackberry").assertIsDisplayed()
    }

    @Test
    fun sortingBySugarWorks() {
        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Sugar").performClick()

        composeTestRule.onNodeWithText("Avocado").assertIsDisplayed()
        composeTestRule.onNodeWithText("Apricot").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cranberry").assertIsDisplayed()
    }
}
