package com.example.androidboardgames

data class NumbersUiState(
    val state: NumbersGameStatus= NumbersGameStatus.LOADING,
    var myRandomNumbers: List<Int> = listOf(),
    val guessedNumbers: ArrayList<Int> = arrayListOf(),
    var isGameWrong:Boolean = false,
    var finalGameScore: Int = 0,
    var isGameWon: Boolean = false,
    var isGameOver: Boolean = false,
    var isGameReset: Boolean = false
)