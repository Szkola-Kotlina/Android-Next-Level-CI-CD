package com.akjaw.test.refactor.fruit

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

    companion object {
        private val FRUITS = listOf(Fruit(name = "Apple"), Fruit(name = "Banana"), Fruit(name = "Cherry"))
        private val FRUITS_WITH_FILTER = listOf(Fruit(name = "Apple"), Fruit(name = "Banana"))
    }

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeFruitApi: FakeFruitApi
    private lateinit var systemUnderTest: FruitListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeFruitApi = FakeFruitApi()
        systemUnderTest = FruitListViewModel(fakeFruitApi)
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

    @Test
    fun `Initialize updates the state with fruits fetched from the API`() {
        fakeFruitApi.fruits = FRUITS

        systemUnderTest.initialize()

        systemUnderTest.fruits.value shouldBe FRUITS
    }

    @Test
    fun `Filtering by name updates the state to include only matching case insensitive fruit names`() {
        fakeFruitApi.fruits = FRUITS
        systemUnderTest.initialize()

        systemUnderTest.filterByName("a")

        systemUnderTest.fruits.value shouldBe FRUITS_WITH_FILTER
    }

    @Test
    fun `Removing the name filter updates the state with the original list`() {
        fakeFruitApi.fruits = FRUITS
        systemUnderTest.initialize()
        systemUnderTest.filterByName("a")

        systemUnderTest.filterByName("")

        systemUnderTest.fruits.value shouldBe FRUITS
    }

    @Test
    fun `Initializing again correctly applies existing name filter`() {
        fakeFruitApi.fruits = FRUITS
        systemUnderTest.initialize()
        systemUnderTest.filterByName("a")

        systemUnderTest.initialize()

        systemUnderTest.fruits.value shouldBe FRUITS_WITH_FILTER
    }

    // TODO Other test some kind of selection tracker? where we can more easily extract logic classes?
}

class FakeFruitApi : FruitApi {

    var fruits: List<Fruit> = emptyList()

    override suspend fun getFruits(): List<Fruit> = fruits
}