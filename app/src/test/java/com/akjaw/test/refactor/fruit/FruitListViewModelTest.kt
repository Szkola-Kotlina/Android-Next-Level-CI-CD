package com.akjaw.test.refactor.fruit

import app.cash.turbine.test
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FruitListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fruitListViewModel: FruitListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fruitListViewModel = FruitListViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun aa() = runTest(testDispatcher) {
        fruitListViewModel.initialize()
        fruitListViewModel.fruits.test {
            awaitItem() shouldHaveSize 39
        }
    }
}