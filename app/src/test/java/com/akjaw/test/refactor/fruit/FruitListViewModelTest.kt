package com.akjaw.test.refactor.fruit

import io.kotest.assertions.assertSoftly
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

    @Test
    fun `Sorting by an invalid nutrition does not update the state`() {
        fakeFruitApi.fruits = FRUITS
        systemUnderTest.initialize()

        systemUnderTest.sortByNutrition(-1)

        systemUnderTest.fruits.value shouldBe FRUITS
    }

    @Test
    fun `Sorting by CARBOHYDRATES updates the state correctly`() = nutritionSortingTestCase(
        nutrition = FruitListViewModel.CARBOHYDRATES,
        fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(carbohydrates = 10f)),
            Fruit(name = "Banana", nutritions = Nutritions(carbohydrates = 22f)),
            Fruit(name = "Cherry", nutritions = Nutritions(carbohydrates = 11f)),
        ),
        assertion = {
            shouldHaveSize(3)
            get(0).name shouldBe "Apple"
            get(1).name shouldBe "Cherry"
            get(2).name shouldBe "Banana"
        }
    )

    @Test
    fun `Sorting by PROTEIN updates the state correctly`() = nutritionSortingTestCase(
        nutrition = FruitListViewModel.PROTEIN,
        fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(protein = 10f)),
            Fruit(name = "Banana", nutritions = Nutritions(protein = 22f)),
            Fruit(name = "Cherry", nutritions = Nutritions(protein = 11f)),
        ),
        assertion = {
            shouldHaveSize(3)
            get(0).name shouldBe "Apple"
            get(1).name shouldBe "Cherry"
            get(2).name shouldBe "Banana"
        }
    )

    @Test
    fun `Sorting by FAT updates the state correctly`() = nutritionSortingTestCase(
        nutrition = FruitListViewModel.FAT,
        fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(fat = 10f)),
            Fruit(name = "Banana", nutritions = Nutritions(fat = 22f)),
            Fruit(name = "Cherry", nutritions = Nutritions(fat = 11f)),
        ),
        assertion = {
            shouldHaveSize(3)
            get(0).name shouldBe "Apple"
            get(1).name shouldBe "Cherry"
            get(2).name shouldBe "Banana"
        }
    )

    @Test
    fun `Sorting by CALORIES updates the state correctly`() = nutritionSortingTestCase(
        nutrition = FruitListViewModel.CALORIES,
        fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(calories = 10f)),
            Fruit(name = "Banana", nutritions = Nutritions(calories = 22f)),
            Fruit(name = "Cherry", nutritions = Nutritions(calories = 11f)),
        ),
        assertion = {
            shouldHaveSize(3)
            get(0).name shouldBe "Apple"
            get(1).name shouldBe "Cherry"
            get(2).name shouldBe "Banana"
        }
    )

    @Test
    fun `Sorting by SUGAR updates the state correctly`() = nutritionSortingTestCase(
        nutrition = FruitListViewModel.SUGAR,
        fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(sugar = 10f)),
            Fruit(name = "Banana", nutritions = Nutritions(sugar = 22f)),
            Fruit(name = "Cherry", nutritions = Nutritions(sugar = 11f)),
        ),
        assertion = {
            shouldHaveSize(3)
            get(0).name shouldBe "Apple"
            get(1).name shouldBe "Cherry"
            get(2).name shouldBe "Banana"
        }
    )

    @Test
    fun `Filtering after sorting keeps the correct sorting`() {
        fakeFruitApi.fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(carbohydrates = 3f)),
            Fruit(name = "Banana", nutritions = Nutritions(carbohydrates = 1f)),
            Fruit(name = "Cherry", nutritions = Nutritions(carbohydrates = 2f)),
        )
        systemUnderTest.initialize()
        systemUnderTest.sortByNutrition(FruitListViewModel.CARBOHYDRATES)

        systemUnderTest.filterByName("a")

        assertSoftly(systemUnderTest.fruits.value) {
            shouldHaveSize(2)
            get(0).name shouldBe "Banana"
            get(1).name shouldBe "Apple"
        }
    }

    @Test
    fun `Sorting after filtering keeps the correct filter`() {
        fakeFruitApi.fruits = listOf(
            Fruit(name = "Apple", nutritions = Nutritions(carbohydrates = 3f)),
            Fruit(name = "Banana", nutritions = Nutritions(carbohydrates = 1f)),
            Fruit(name = "Cherry", nutritions = Nutritions(carbohydrates = 2f)),
        )
        systemUnderTest.initialize()
        systemUnderTest.filterByName("a")

        systemUnderTest.sortByNutrition(FruitListViewModel.CARBOHYDRATES)

        assertSoftly(systemUnderTest.fruits.value) {
            shouldHaveSize(2)
            get(0).name shouldBe "Banana"
            get(1).name shouldBe "Apple"
        }
    }

    private fun nutritionSortingTestCase(
        nutrition: Int,
        fruits: List<Fruit>,
        assertion: List<Fruit>.() -> Unit
    ) {
        fakeFruitApi.fruits = fruits
        systemUnderTest.initialize()

        systemUnderTest.sortByNutrition(nutrition)

        assertSoftly(systemUnderTest.fruits.value) {
            assertion(systemUnderTest.fruits.value)
        }
    }

    // TODO test for concurrent access?
    // TODO split test class into multiple classes?
    // TODO Other test some kind of selection tracker? where we can more easily extract logic classes?
}

class FakeFruitApi : FruitApi {

    var fruits: List<Fruit> = emptyList()

    override suspend fun getFruits(): List<Fruit> = fruits
}