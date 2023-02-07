package com.akjaw.android.next.level.cicd.fruit

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@Serializable
data class FruitSchema(
    val name: String = "",
    val id: Int = -1,
    val nutritions: NutritionsSchema = NutritionsSchema()
)

@Serializable
data class NutritionsSchema(
    val carbohydrates: Float = 0.0f,
    val protein: Float = 0.0f,
    val fat: Float = 0.0f,
    val calories: Float = 0.0f,
    val sugar: Float = 0.0f,
)

interface FruitApi {

    suspend fun getFruits(): List<FruitSchema>
}

object FruitApiProvider {

    var fruitApi: FruitApi = KtorFruitApi()

    fun get(): FruitApi = fruitApi
}

class KtorFruitApi : FruitApi {

    private val client = HttpClient(CIO) {
        expectSuccess = true
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("Ktor", message)
                }
            }
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    override suspend fun getFruits(): List<FruitSchema> =
        try {
            val response = client.get("https://www.fruityvice.com/api/fruit/all")
            response.body()
        } catch (e: Exception) {
            Log.e("Ktor", e.stackTraceToString())
            emptyList()
        }
}

class FakeFruitApi(var fruits: List<FruitSchema> = emptyList()) : FruitApi {

    companion object {
        val FRUITS = listOf(
            FruitSchema(name="Apple", id=6, nutritions=NutritionsSchema(carbohydrates=11.4f, protein=0.3f, fat=0.4f, calories=52.0f, sugar=10.3f)),
            FruitSchema(name="Apricot", id=35, nutritions=NutritionsSchema(carbohydrates=3.9f, protein=0.5f, fat=0.1f, calories=15.0f, sugar=3.2f)),
            FruitSchema(name="Avocado", id=84, nutritions=NutritionsSchema(carbohydrates=8.53f, protein=2.0f, fat=14.66f, calories=160.0f, sugar=0.66f)),
            FruitSchema(name="Banana", id=1, nutritions=NutritionsSchema(carbohydrates=22.0f, protein=1.0f, fat=0.2f, calories=96.0f, sugar=17.2f)),
            FruitSchema(name="Blackberry", id=64, nutritions=NutritionsSchema(carbohydrates=9.0f, protein=1.3f, fat=0.4f, calories=40.0f, sugar=4.5f)),
            FruitSchema(name="Blueberry", id=33, nutritions=NutritionsSchema(carbohydrates=5.5f, protein=0.0f, fat=0.4f, calories=29.0f, sugar=5.4f)),
            FruitSchema(name="Cherry", id=9, nutritions=NutritionsSchema(carbohydrates=12.0f, protein=1.0f, fat=0.3f, calories=50.0f, sugar=8.0f)),
            FruitSchema(name="Cranberry", id=87, nutritions=NutritionsSchema(carbohydrates=12.2f, protein=0.4f, fat=0.1f, calories=46.0f, sugar=4.0f)),
            FruitSchema(name="Dragonfruit", id=80, nutritions=NutritionsSchema(carbohydrates=9.0f, protein=9.0f, fat=1.5f, calories=60.0f, sugar=8.0f)),
            FruitSchema(name="Durian", id=60, nutritions=NutritionsSchema(carbohydrates=27.1f, protein=1.5f, fat=5.3f, calories=147.0f, sugar=6.75f)),
        )
    }

    override suspend fun getFruits(): List<FruitSchema> = fruits
}

