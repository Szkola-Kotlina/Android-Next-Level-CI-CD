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
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.test.swipeUp
import com.akjaw.android.next.level.cicd.MainActivity
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

@SmokeTest
internal class FruitListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        FruitApiProvider.fruitApi = FakeFruitApi(FakeFruitApi.FRUITS)
    }

    @Test
    fun favoriteFruitsIsPlacedOnTop() {
        composeTestRule.onRoot()
            .performTouchInput {
                swipeUp(800f, 0f)
            }

        addFruitToFavorite("Dragonfruit")
        composeTestRule.onRoot()
            .performTouchInput {
                swipeDown(200f, 900f)
                swipeDown(200f, 900f)
            }

        composeTestRule.waitUntil {
            composeTestRule
                .onAllNodesWithText("Apple")
                .fetchSemanticsNodes().size == 1
        }

        composeTestRule.onNodeWithText("Apple").assertIsDisplayed()
        composeTestRule.onNodeWithText("Dragonfruit").assertIsDisplayed()
    }

    @Test
    fun favoriteFruitsIsNotOnTopWhenSorting() {
        addFruitToFavorite("Avocado")

        composeTestRule.onNodeWithContentDescription("Sort").performClick()
        composeTestRule.onNodeWithText("Fat").performClick()

        composeTestRule.onNodeWithText("Avocado").assertIsNotDisplayed()
    }

    @Test
    fun searchWorks() {
        composeTestRule.onNodeWithText("Search for fruit").performTextInput("BERRY")

        composeTestRule.onNodeWithText("Blackberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Blueberry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cranberry").assertIsDisplayed()
    }

    private fun addFruitToFavorite(name: String) {
        composeTestRule.onAllNodesWithContentDescription("Add to favorite")
            .filterToOne(hasAnySibling(hasText(name))).performClick()
    }

    private fun removeFruitFromFavorite(name: String) {
        composeTestRule.onAllNodesWithContentDescription("Remove from favorite")
            .filterToOne(hasAnySibling(hasText(name))).performClick()
    }
}
