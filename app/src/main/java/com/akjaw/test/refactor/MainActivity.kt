package com.akjaw.test.refactor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.akjaw.test.refactor.fruit.FruitListScreen
import com.akjaw.test.refactor.fruit.FruitListViewModel
import com.akjaw.test.refactor.fruit.FruitListViewModelFactory
import com.akjaw.test.refactor.ui.theme.TestRefactorTheme

// Either ViewModel crammed with everything  -> refactored to have better separation
// Having a logically dense class -> refactoring its internal logic but the surface API is the same
class MainActivity : ComponentActivity() {

    private val fruitListViewModel: FruitListViewModel by viewModels(factoryProducer = { FruitListViewModelFactory() })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestRefactorTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    FruitListScreen(fruitListViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TestRefactorTheme {
        Greeting("Android")
    }
}