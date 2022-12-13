package com.akjaw.test.refactor.fruit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.akjaw.test.refactor.fruit.model.Fruit

@Composable
fun FruitListScreen(viewModel: FruitListViewModel) {
    val fruits by viewModel.fruits.collectAsState()

    LaunchedEffect(null) {
        viewModel.initialize()
    }

    FruitListScreenContent(
        fruits = fruits,
        filterByName = viewModel::filterByName,
        sortByNutrition = viewModel::sortByNutrition,
        addToFavorite = viewModel::addToFavorite,
    )
}

@Composable
private fun FruitListScreenContent(
    fruits: List<Fruit>,
    filterByName: (String) -> Unit,
    sortByNutrition: (FruitListViewModel.Sorting) -> Unit,
    addToFavorite: (Int) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TopActions(
            filterByName = filterByName,
            sortByNutrition = sortByNutrition,
        )
        FruitList(
            fruits,
            addToFavorite,
        )
    }
}

@Composable
private fun TopActions(
    filterByName: (String) -> Unit,
    sortByNutrition: (FruitListViewModel.Sorting) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    var isDropDownShown by remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier,
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
                filterByName(newValue)
            },
            label = { Text("Name") }
        )
        Box {
            Icon(
                imageVector = Icons.Filled.Sort,
                contentDescription = "Sort",
                modifier = Modifier.clickable { isDropDownShown = true }
            )
            DropdownMenu(expanded = isDropDownShown, onDismissRequest = { isDropDownShown = false }) {
                Column {
                    Text("No sorting", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.NO_SORTING) })
                    Text("Carbohydrates", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.CARBOHYDRATES) })
                    Text("Protein", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.PROTEIN) })
                    Text("Fat", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.FAT) })
                    Text("Calories", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.CALORIES) })
                    Text("Sugar", Modifier.clickable { sortByNutrition(FruitListViewModel.Sorting.SUGAR) })
                }
            }
        }
    }
}

@Composable
private fun FruitList(
    fruits: List<Fruit>,
    addToFavorite: (Int) -> Unit
) {
    val state = rememberLazyListState()
    LaunchedEffect(fruits) {
        state.scrollToItem(0)
    }
    LazyColumn(Modifier.fillMaxHeight(), state = state) {
        items(items = fruits, key = { it.id }) { fruit ->
            val isFavorited = remember(fruit) { fruit.isFavorited }
            FruitItem(
                fruit = fruit,
                isFavorited = isFavorited,
                onFavoriteClick = { addToFavorite(fruit.id) }
            )
        }
    }
}

@Composable
private fun FruitItem(fruit: Fruit, isFavorited: Boolean, onFavoriteClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        backgroundColor = Color(0xFFF0F0F0),
    ) {
        Column(Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(fruit.name, style = MaterialTheme.typography.h5)
                if (isFavorited) {
                    Icon(Icons.Filled.Star, "Favorited")
                } else {
                    Icon(
                        imageVector = Icons.Filled.StarBorder,
                        contentDescription = "Add to favorite",
                        modifier = Modifier.clickable(onClick = onFavoriteClick)
                    )
                }
            }
            NutritionRow {
                Text("Calories: ${fruit.nutritions.calories}")
            }
            NutritionRow {
                Text("Carbohydrates: ${fruit.nutritions.carbohydrates}")
                Text("Sugar: ${fruit.nutritions.sugar}")
            }
            NutritionRow {
                Text("Protein: ${fruit.nutritions.protein}")
                Text("Fat: ${fruit.nutritions.fat}")
            }
        }
    }
}

@Composable
private fun NutritionRow(content: @Composable () -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        content()
    }
}
