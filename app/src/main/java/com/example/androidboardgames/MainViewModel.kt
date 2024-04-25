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
import kotlin.properties.Delegates

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

    private val _correctlyGuessedCardsList= MutableStateFlow(arrayListOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false))
    val correctlyGuessedCardsList: StateFlow<ArrayList<Boolean>> = _correctlyGuessedCardsList.asStateFlow()
    private fun updateGuessedCardsList(guessedCardsPositions: ShadifySetPairPositions) {
        val positionInt: ArrayList<Int> = arrayListOf()
        guessedCardsPositions.positions.forEach {
            positionInt.add(it[0]*it[1])
        }
        println("items are at positions Should be ${guessedCardsPositions.positions.forEach { it.forEach { println(it) } }} ${positionInt.forEach { println(it) }}")

        positionInt.forEach { int ->
            correctlyGuessedCardsList.value[int] = true
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
        }

    }

    private fun getShadifyMemoryGame(){
        _status.value = ShadifyApiStatus.LOADING
        try {
            viewModelScope.launch {
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
