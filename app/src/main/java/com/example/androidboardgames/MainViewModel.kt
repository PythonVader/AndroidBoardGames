package com.example.androidboardgames

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidboardgames.data.ShadifyMath
import com.example.androidboardgames.network.ShadifyMathApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.await
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

//    private val _answerChecker by Delegates.observable(listOf("")){ property, oldValue, newValue ->
//        if ()
//    }

    init {
        getShadifyMathEquation()
        getShadifyMemoryGame()
    }
    fun cardFlip(cardName: String){

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
