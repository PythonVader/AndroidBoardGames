package com.example.androidboardgames

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidboardgames.data.ShadifySetPairPositions
import com.example.androidboardgames.network.ShadifyMathApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates
import kotlin.random.Random

class MainViewModel: ViewModel(){
    private var _status = MutableStateFlow(ShadifyApiStatus.LOADING)
    val status = _status.asStateFlow()

    private var _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private var _memoryGameAdapted = MutableStateFlow(arrayListOf(""))
    val memoryGameAdapted = _memoryGameAdapted.asStateFlow()

    private var _isAnswerCorrect = MutableStateFlow(false)
    val isAnswerCorrect = _isAnswerCorrect.asStateFlow()

    private var memoryCardsGuessed = arrayListOf<String>()

    private var _answerReset = MutableStateFlow(false)
    val answerReset = _answerReset.asStateFlow()

    private var _answerChecker by Delegates.observable(listOf("")){ property, oldValue, newValue ->
        println("Cards Guessed are --> $newValue")
        if (newValue.size>3){
            checkAnswerMemory(newValue.take(3))
        }
    }

    private val myRandomNumbers = List(25) { Random.nextInt() }

    private fun checkAnswerMemory(stringList: List<String>) {
        when(stringList){
            listOf("a","a","a") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "a" }.first())
            listOf("b","b","b") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "b" }.first())
            listOf("c","c","c") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "c" }.first())
            listOf("d","d","d") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "d" }.first())
            listOf("e","e","e") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "e" }.first())
            listOf("f","f","f") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "f" }.first())
            listOf("g","g","g") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "g" }.first())
            listOf("h","h","h") -> updateGuessedCardsList(_mainUiState.value.memoryGame.pairPositions.filter { it.value == "h" }.first())
            else -> resetGuessedCardsListandFlip()
        }
    }

    private fun resetGuessedCardsListandFlip() {
        memoryCardsGuessed = arrayListOf()
        _answerReset.value = true
    }
    fun cardsReset(){
        _answerReset.value = false
    }

    private val _correctlyGuessedCardsList= MutableStateFlow(arrayListOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false))
    val correctlyGuessedCardsList: StateFlow<ArrayList<Boolean>> = _correctlyGuessedCardsList.asStateFlow()
    private fun updateGuessedCardsList(guessedCardsPositions: ShadifySetPairPositions) {
        val positionInt: ArrayList<Int> = arrayListOf()
        guessedCardsPositions.positions.forEach {
            val indexAt = when(it.toList()){
                listOf(1,1) -> 1
                listOf(1,2) -> 2
                listOf(1,3) -> 3
                listOf(1,4) -> 4
                listOf(1,5) -> 5
                listOf(1,6) -> 6
                listOf(2,1) -> 7
                listOf(2,2) -> 8
                listOf(2,3) -> 9
                listOf(2,4) -> 10
                listOf(2,5) -> 11
                listOf(2,6) -> 12
                listOf(3,1) -> 13
                listOf(3,2) -> 14
                listOf(3,3) -> 15
                listOf(3,4) -> 16
                listOf(3,5) -> 17
                listOf(3,6) -> 18
                listOf(4,1) -> 19
                listOf(4,2) -> 20
                listOf(4,3) -> 21
                listOf(4,4) -> 22
                listOf(4,5) -> 23
                else -> 0
            }
            println("iNDEX rETURNED at $indexAt")
            positionInt.add(indexAt)
        }
        println("items are at positions Should be ${guessedCardsPositions.positions.forEach { it.forEach { println(it) } }} ${positionInt.forEach { println(it) }}")

        positionInt.forEach { int ->
            if (int == 0){
                correctlyGuessedCardsList.value[24] = true

            }else{
                correctlyGuessedCardsList.value[int] = true
            }
        }
        resetGuessedCardsList()
    }

    private fun resetGuessedCardsList() {
        memoryCardsGuessed = arrayListOf()
    }


    init {
        getShadifyMathEquation()
        getShadifyMemoryGame()
    }
    fun cardFlip(cardName: String){
        memoryCardsGuessed.add(cardName)
        _answerChecker = memoryCardsGuessed
    }
    private fun getShadifyMathEquation(){
        _status.value = ShadifyApiStatus.LOADING
        try {
            viewModelScope.launch {
                val listResult = ShadifyMathApi.retrofitService.getMultiplicationEquation()
                _mainUiState.value.mathEquation = listResult
                _status.value = ShadifyApiStatus.DONE
            }
        } catch (e:Exception){
            _status.value = ShadifyApiStatus.ERROR
            println(e)
        }

    }

    private fun getShadifyMemoryGame(){
        _status.value = ShadifyApiStatus.LOADING
        try {
            viewModelScope.launch {
                runBlocking {
                    val listResult = ShadifyMathApi.retrofitService.getMemoryGame()
                    _mainUiState.value.memoryGame = listResult
                    _mainUiState.value.memoryGame.grid.forEach {
                        it.forEach { element ->
                            _memoryGameAdapted.value.add(element)
                        }
                    }
                    _memoryGameAdapted.value.forEach { println(it) }
                    _status.value = ShadifyApiStatus.DONE
                    Log.d("MEMORYGAME RESULT", "${_mainUiState.value.memoryGame.pairPositions}")
                    println("MEMORYGAME --------->${_mainUiState.value.memoryGame.grid.size} &&&& the width of it is ${_mainUiState.value.memoryGame.width}")
                }
            }

        } catch (e:Exception){
            _status.value = ShadifyApiStatus.ERROR
        }

    }


    fun checkAnswer(answer: String){
        if(answer == _mainUiState.value.mathEquation.answer){
            _isAnswerCorrect.value = true
        }
        else return
    }
}
