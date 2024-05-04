package com.example.androidboardgames

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androidboardgames.data.ShadifyMath
import com.example.androidboardgames.ui.theme.AndroidBoardGamesTheme

enum class Screens {MAIN, MATH, MEMORY, NUMBERS}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidBoardGamesTheme {
                val mainViewModel: MainViewModel = viewModel()
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
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically){
                                Button(onClick = { currentScreen = Screens.MEMORY }, modifier = Modifier.weight(1f)) {
                                    Text(text = "Memory Game", textAlign = TextAlign.Center)
                                }
                                Button(onClick = { currentScreen = Screens.MATH }, modifier = Modifier.weight(1f)) {
                                    Text(text = "Math Game", textAlign = TextAlign.Center)
                                }
                                Button(onClick = {
                                    currentScreen = Screens.NUMBERS
                                mainViewModel.startNumbersGame()
                                }, modifier = Modifier.weight(1f)) {
                                    Text(text = "Numbers Game", textAlign = TextAlign.Center)
                                }
                            }
                        }
                        Screens.MEMORY -> MemoryGameScreen()
                        Screens.MATH -> MathGameScreen()
                        Screens.NUMBERS -> NumbersGameScreen()
                    }

                }
            }
        }
    }
}

@Composable
fun NumbersGameScreen(mainViewModel: MainViewModel = viewModel()) {
    val numbersGameState = mainViewModel.numbersUiState.collectAsState()
    val timerValue = mainViewModel.timerValue.collectAsState()
    when(numbersGameState.value.state){
        NumbersGameStatus.LOADING -> LoadingScreen()
        NumbersGameStatus.STARTED ->  NumbersGameContent(
            isGameWrong = numbersGameState.value.isGameWrong,
            listOfRandomNumbers = numbersGameState.value.myRandomNumbers,
            onNumberAdded = {mainViewModel.addNumberGameGuess(it)},
            isGameWon = numbersGameState.value.isGameWon,
            isGameOver = numbersGameState.value.isGameOver,
            restartGame = {mainViewModel.resetGame()},
            onPlayAgain = {},
            isGameReset = numbersGameState.value.isGameReset,
            timerValue = timerValue.value,
            finalScore = numbersGameState.value.finalGameScore
        )
    }
}

@Composable
fun NumbersGameContent(isGameWrong: Boolean, listOfRandomNumbers: List<Int>, onNumberAdded: (Int) -> Unit, restartGame: () -> Unit, finalScore: Int, isGameWon: Boolean,isGameReset: Boolean, isGameOver: Boolean, onPlayAgain: () -> Unit, timerValue: Int) {
    Column(modifier = Modifier
        .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Choose Numbers Ascending")
            Card(modifier = Modifier.padding(12.dp)) {
                Text(text = timerValue.toString())
            }
        }
        Box(contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 22.dp, horizontal = 0.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NumbersBox(
                        listOfRandomNumbers[0],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[1],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[2],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[3],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[4],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NumbersBox(
                        listOfRandomNumbers[5],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[6],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[7],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[8],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[9],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NumbersBox(
                        listOfRandomNumbers[10],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[11],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[12],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[13],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[14],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NumbersBox(
                        listOfRandomNumbers[15],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[16],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[17],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[18],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[19],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    NumbersBox(
                        listOfRandomNumbers[20],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[21],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[22],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[23],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )
                    NumbersBox(
                        listOfRandomNumbers[24],
                        onNumberSelected = onNumberAdded,
                        isGameReset = isGameReset,
                        modifier = Modifier.weight(1f)
                    )

                }
            }
            if (isGameOver || isGameWon) {
                FinalScoreDialog(
                    result = if (isGameWon) "CONGRATULATIONS!" else "GAME OVER!",
                    score = finalScore,
                    onPlayAgain = { onPlayAgain() })
            }
            if (isGameWrong) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = null,
                    modifier = Modifier.size(280.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(45.dp), contentAlignment = Alignment.BottomCenter
                ) {
                    Button(onClick = { restartGame() }) {
                        Text(text = "Restart")
                    }
                }
            }
        }
    }

}

    @Composable
    fun NumbersBox(number: Int, onNumberSelected: (Int) -> Unit,isGameReset:Boolean, modifier: Modifier = Modifier) {
        var isNumberSelected by rememberSaveable {
            mutableStateOf(false)
        }
        LaunchedEffect(isGameReset){
            isNumberSelected = false
        }
        Box(
            modifier = modifier
                .background(Color.Transparent)
                .padding(vertical = 35.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = number.toString(), modifier = Modifier.clickable(enabled = !isNumberSelected) {
                isNumberSelected = true
                onNumberSelected(number)
            })
            if (isNumberSelected) {
                Canvas(
                    modifier = Modifier.size(100.dp),
                    onDraw = {
                        val strokeWidth = 8f
                        drawCircle(
                            color = Color.White,
                            radius = 80f,
                            style = Stroke(
                                width = strokeWidth
                            )
                        )
                    }
                )
            }

        }
    }

@Composable
fun MathGameScreen(mainViewModel: MainViewModel = viewModel()) {
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
    val shadifyUiState = mainViewModel.status.collectAsState()
    val guessedCardsList = mainViewModel.correctlyGuessedCardsList.collectAsState()
    val memoryGameAdapted = mainViewModel.memoryGameAdapted.collectAsState()
    val resetRotations = mainViewModel.answerReset.collectAsState()
    when(shadifyUiState.value){
        ShadifyApiStatus.DONE ->Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Match The Cards")
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 22.dp, horizontal = 0.dp), verticalArrangement = Arrangement.SpaceBetween){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[1],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[1],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[2],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[2],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[3],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[3],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[4],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[4],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[5],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[5],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[6],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[6],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[7],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[7],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[8],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[8],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[9],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[9],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[10],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[10],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[11],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[11],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[12],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[12],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[13],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[13],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[14],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[14],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[15],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[15],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[16],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[16],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[17],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[17],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[18],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[18],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[19],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[19],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[20],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[20],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[21],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[21],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[22],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[22],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value[23],
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[23],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                    MemoryCard(
                        cardContent = memoryGameAdapted.value.last(),
                        onCardRotated = {
                            mainViewModel.cardFlip(it)
                        },
                        resetRotation = resetRotations.value,
                        isCardGuessedCorrectly = guessedCardsList.value[24],
                        onCardsReset = { mainViewModel.cardsReset() },
                        modifier = Modifier
                            .weight(1f)
                    )
                }
            }

        }
        ShadifyApiStatus.LOADING -> LoadingScreen()
        ShadifyApiStatus.ERROR -> ErrorScreen()
    }
}

@Composable
fun MemoryCard(cardContent:String, onCardRotated: (String) -> Unit, isCardGuessedCorrectly:Boolean, resetRotation: Boolean, onCardsReset: () -> Unit, modifier: Modifier = Modifier) {
    var rotated by remember {
        mutableStateOf(false)
    }
    val rotation by animateFloatAsState(targetValue = if(rotated) 180f else 0f,
        animationSpec = tween(100), label = ""
    )

    val imageResource  = when(cardContent){
        "a" -> R.drawable.icons8_iron_man
        "b" -> R.drawable.icons8_joker_dc
        "c" -> R.drawable.icons8_neo
        "d" -> R.drawable.icons8_android
        "e" -> R.drawable.icons8_walter_white
        "f" -> R.drawable.icons8_homer_simpson
        "g" -> R.drawable.icons8_genie
        else -> R.drawable.icons8_luigi
    }
    Card(
        modifier = modifier
            .padding(10.dp)
            .graphicsLayer {
                rotationY = rotation
//                cameraDistance = 8 * density
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
        border = if(isCardGuessedCorrectly) BorderStroke(3.dp, Color.Green) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        if (isCardGuessedCorrectly){
            FrontCardLayout(imageResource)
        } else if (resetRotation) {
            rotated = false
            BackCardLayout(rotation = rotation, onClicked = {
                rotated = true
                onCardRotated(cardContent)
            })
            onCardsReset()
        } else if (rotated){
            FrontCardLayout(imageResource)
        } else {
            BackCardLayout(rotation = rotation, onClicked = {
                rotated = true
                onCardRotated(cardContent)
            })
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
fun BackCardLayout(onClicked: () -> Unit, rotation: Float) {
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .graphicsLayer {
            rotationY = rotation
        }
        .clickable { onClicked() },) {
        Image(painter = painterResource(id = R.drawable.baseline_question_mark_24), contentDescription = null)
    }
}

@Composable
private fun FinalScoreDialog(
    result:String,
    score: Int,
    onPlayAgain: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activity = (LocalContext.current as Activity)

    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onCloseRequest.
        },
        title = { Text(text =(result)) },
        text = { Text(text = "You Scored $score seconds") },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = {
                    activity.finish()
                }
            ) {
                Text(text = stringResource(R.string.exit))
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = stringResource(R.string.play_again))
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidBoardGamesTheme {
        MathGameScreen()
    }
}