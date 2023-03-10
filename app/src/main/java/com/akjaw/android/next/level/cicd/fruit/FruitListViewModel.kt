package com.akjaw.android.next.level.cicd.fruit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.akjaw.android.next.level.cicd.fruit.model.Fruit
import com.akjaw.android.next.level.cicd.fruit.model.Nutritions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FruitListViewModelFactory : ViewModelProvider.Factory {

    private val api = FruitApiProvider.get()
    private val favoriteRepository = FavoriteRepository()
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FruitApi::class.java, FavoriteRepository::class.java)
            .newInstance(api, favoriteRepository)
    }
}

class FruitListViewModel(
    private val fruitApi: FruitApi,
    private val favoriteRepository: FavoriteRepository = FavoriteRepository(),
) : ViewModel() {

    enum class SortType {
        CARBOHYDRATES,
        PROTEIN,
        FAT,
        CALORIES,
        SUGAR,
        NO_SORTING,
    }

    private val originalFruits: MutableStateFlow<List<FruitSchema>> = MutableStateFlow(emptyList())
    private val currentSearchQuery: MutableStateFlow<String> = MutableStateFlow("")
    private val currentNutritionSort: MutableStateFlow<SortType> = MutableStateFlow(SortType.NO_SORTING)
    private val favoriteFruitIds: StateFlow<List<Int>> = favoriteRepository.favoriteFruitIds
    val fruits: StateFlow<List<Fruit>> =
        combine(
            originalFruits,
            currentSearchQuery,
            currentNutritionSort,
            favoriteFruitIds,
            ::transform,
        ).stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun initialize() = viewModelScope.launch {
        originalFruits.value = fruitApi.getFruits()
    }

    fun sortByNutrition(nutrition: SortType) {
        currentNutritionSort.value = nutrition
    }

    fun filterByName(searchQuery: String) {
        currentSearchQuery.value = searchQuery
    }

    fun updateFavorite(fruitId: Int) {
        favoriteRepository.updateFavorite(fruitId)
    }

    private fun transform(
        originalFruits: List<FruitSchema>,
        currentSearchQuery: String,
        currentNutritionSort: SortType,
        favorites: List<Int>
    ): List<Fruit> = originalFruits
        .filter { it.name.contains(currentSearchQuery, ignoreCase = true) }
        .map { schema -> convert(schema, favorites.contains(schema.id)) }
        .sort(currentNutritionSort)

    private fun convert(schema: FruitSchema, isFavorited: Boolean): Fruit =
        Fruit(
            name = schema.name,
            id = schema.id,
            nutritions = Nutritions(
                schema.nutritions.carbohydrates,
                schema.nutritions.protein,
                schema.nutritions.fat,
                schema.nutritions.calories,
                schema.nutritions.sugar,
            ),
            isFavorited = isFavorited
        )

    private fun List<Fruit>.sort(
        sortType: SortType,
    ): List<Fruit> = when (sortType) {
        SortType.CARBOHYDRATES -> sortedBy { it.nutritions.carbohydrates }
        SortType.PROTEIN -> sortedBy { it.nutritions.protein }
        SortType.FAT -> sortedBy { it.nutritions.fat }
        SortType.CALORIES -> sortedBy { it.nutritions.calories }
        SortType.SUGAR -> sortedBy { it.nutritions.sugar }
        SortType.NO_SORTING -> sortedBy { it.name }
            .sortedBy { it.isFavorited.not() }
    }
}
