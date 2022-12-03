package com.akjaw.test.refactor.fruit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class Fruit(
    val genus: String,
    val name: String,
    val id: String,
    val family: String,
    val order: String,
    val nutritions: Nutritions
)

@Serializable
data class Nutritions(
    val carbohydrates: String,
    val protein: String,
    val fat: String,
    val calories: String,
    val sugar: String,
)

class FruitListViewModel : ViewModel() {

    val fruits = MutableStateFlow(emptyList<Fruit>())

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                }
            )
        }
    }

    // TODO make it use ViewModelScope
    suspend fun initialize() {
        val response: List<Fruit> = client.get("https://www.fruityvice.com/api/fruit/all").body()
        fruits.value = response
    }
}