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
import org.junit.Ignore
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class FruitListViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeFruitApi: FakeFruitApi
    private lateinit var fruitListViewModel: FruitListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeFruitApi = FakeFruitApi()
        fruitListViewModel = FruitListViewModel(fakeFruitApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    @Ignore
    fun apiCall() = runTest(testDispatcher) {
        val fruits = KtorFruitApi().getFruits()
        fruits shouldHaveSize 39
    }

    // TODO Other test some kind of selection tracker?
}

class FakeFruitApi : FruitApi {

    var fruits: List<Fruit> = emptyList()

    override suspend fun getFruits(): List<Fruit> = fruits
}