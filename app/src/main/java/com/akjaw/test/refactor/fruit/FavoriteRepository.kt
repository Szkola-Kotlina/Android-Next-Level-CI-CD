package com.akjaw.test.refactor.fruit

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteRepository {

    private val mutableFavoriteFruitIds: MutableStateFlow<List<Int>> = MutableStateFlow(emptyList<Int>())
    val favoriteFruitIds: StateFlow<List<Int>> = mutableFavoriteFruitIds

    fun addToFavorite(fruitId: Int) {
        if (mutableFavoriteFruitIds.value.contains(fruitId)) return
        mutableFavoriteFruitIds.value = mutableFavoriteFruitIds.value + fruitId
    }
}
