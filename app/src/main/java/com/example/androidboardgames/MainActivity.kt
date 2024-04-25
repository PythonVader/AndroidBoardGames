package com.example.androidboardgames

import android.os.Bundle
import android.view.WindowId
import android.widget.GridLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidboardgames.data.ShadifyMath
import com.example.androidboardgames.ui.theme.AndroidBoardGamesTheme
enum class Screens {MAIN, MATH, MEMORY}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBoardGamesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentScreen by rememberSaveable {
                        mutableStateOf(Screens.MAIN)
                    }
                    when(currentScreen){
                        Screens.MAIN -> Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                Button(onClick = { currentScreen = Screens.MEMORY }) {
                                    Text(text = "To Memory Game")
                                }
                                Button(onClick = { currentScreen = Screens.MATH }) {
                                    Text(text = "To Math Game")
                                }
                            }
                        }
                        Screens.MEMORY -> MemoryGameScreen()
                        Screens.MATH -> MathGameScreen()
                    }

                }
            }
        }
    }
}

@Composable
fun MathGameScreen(mainViewModel: MainViewModel = viewModel(), modifier: Modifier = Modifier) {
    val shadifyUiState = mainViewModel.status.collectAsState()
    val mainUiState = mainViewModel.mainUiState.collectAsState()
    val isAnswerCorrect = mainViewModel.isAnswerCorrect.collectAsState()
    when(shadifyUiState.value){
        ShadifyApiStatus.DONE -> ResultScreen(mainUiState.value.mathEquation,{ mainViewModel.checkAnswer(
            it
        )}, isAnswerCorrect.value)

        ShadifyApiStatus.LOADING -> LoadingScreen()
        ShadifyApiStatus.ERROR -> ErrorScreen()
    }

}

@Composable
fun ResultScreen(shadifyMathResult: ShadifyMath, onSubmitAnswer: (String) -> Unit, isAnswerCorrect: Boolean) {
    var inputText by remember {
        mutableStateOf("")
    }
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = shadifyMathResult.expression)
        Spacer(modifier = Modifier.size(12.dp))
        TextField(value = inputText, onValueChange = {inputText = it} )
        Button(onClick = { onSubmitAnswer(inputText) }) {
            Text(text = "Submit")
        }
        if (isAnswerCorrect){
            Text(text = "Correct")
        }
    }
}

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = null)
    }
}

@Composable
fun ErrorScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Image(painter = painterResource(id = R.drawable.baseline_cloud_off_24), contentDescription = null)
    }
}

@Composable
fun MemoryGameScreen(mainViewModel: MainViewModel = viewModel()) {
    val mainUiState = mainViewModel.mainUiState.collectAsState()
    val shadifyUiState = mainViewModel.status.collectAsState()
    val guessedCardsList = mainViewModel.correctlyGuessedCardsList.collectAsState()
    val memoryGameAdapted = mainViewModel.memoryGameAdapted.collectAsState()
    val resetRotations = mainViewModel.answerReset.collectAsState()
    when(shadifyUiState.value){
        ShadifyApiStatus.DONE ->Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Match The Cards")
            LazyHorizontalGrid(rows = GridCells.Fixed(6)) {
                    items(24) {
                        MemoryCard(
                            cardContent = memoryGameAdapted.value[it],
                            onCardRotated = {
                                mainViewModel.cardFlip(it)
                            },
                            resetRotation = resetRotations.value,
                            isCardGuessedCorrectly = guessedCardsList.value[it],
                            onCardsReset = { mainViewModel.cardsReset() }
                        )
                    }
//                items(4) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[it][1],
//                        onCardRotated = {
//                            mainViewModel.cardFlip(it)
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(4) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[it][2],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(4) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[it][3],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(4) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[it][4],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(4) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[it][5],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }




//                items(mainUiState.value.memoryGame.grid[2].size) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[2][it],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(mainUiState.value.memoryGame.grid[3].size) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[3][it],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
//                items(mainUiState.value.memoryGame.grid[4].size) {
//                    MemoryCard(
//                        cardContent = mainUiState.value.memoryGame.grid[4][it],
//                        onCardRotated = {
//                            listOfCards.add(it)
//                            resetRotations = false
//                        },
//                        resetRotation = resetRotations
//                    )
//                }
            }
        }
        ShadifyApiStatus.LOADING -> LoadingScreen()
        ShadifyApiStatus.ERROR -> ErrorScreen()
    }
}

@Composable
fun MemoryCard(cardContent:String, onCardRotated: (String) -> Unit, isCardGuessedCorrectly:Boolean, resetRotation: Boolean, onCardsReset: () -> Unit) {
    var rotated by remember {
        mutableStateOf(false)
    }
//    LaunchedEffect(Unit){
//        if(isCardGuessedCorrectly){
//            rotated = true
//        }else if (resetRotation){
//            rotated = false
//            onCardsReset()
//        }
//    }

    val imageResource  = when(cardContent){
        "a" -> R.drawable.icons8_iron_man
        "b" -> R.drawable.icons8_joker_dc
        "c" -> R.drawable.icons8_neo
        "d" -> R.drawable.icons8_android
        "e" -> R.drawable.icons8_walter_white
        else -> R.drawable.icons8_luigi
    }
    Card(
        modifier = Modifier
            .size(120.dp, 380.dp)
            .padding(10.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        if (isCardGuessedCorrectly){
            FrontCardLayout(imageResource)
        } else if (resetRotation) {
            rotated = false
            BackCardLayout {
                rotated = true
                onCardRotated(cardContent)
            }
            onCardsReset()
        } else if (rotated){
            FrontCardLayout(imageResource)
        } else {
            BackCardLayout {
                rotated = true
                onCardRotated(cardContent)
            }
        }
    }
}
@Composable
fun FrontCardLayout(@DrawableRes painterRes: Int) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)) {
        Image(painter = painterResource(id = painterRes) , contentDescription = null)
    }
}
@Composable
fun BackCardLayout(onClicked: () -> Unit) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .clickable { onClicked() },) {
        Image(painter = painterResource(id = R.drawable.baseline_question_mark_24), contentDescription = null)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidBoardGamesTheme {
        MathGameScreen()
    }
}