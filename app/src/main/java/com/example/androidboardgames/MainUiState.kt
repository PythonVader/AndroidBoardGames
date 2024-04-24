package com.example.androidboardgames

import com.example.androidboardgames.data.ShadifyMath
import com.example.androidboardgames.data.ShadifyMemory
import com.example.androidboardgames.data.ShadifySetPairPositions

data class MainUiState(
    var mathEquation: ShadifyMath = ShadifyMath(first = 0, second = 0, operator = "", expression = "", answer = ""),
    var memoryGame: ShadifyMemory = ShadifyMemory(2,3,4,8, arrayOf(arrayOf("a","b"), arrayOf("c")), pairPositions = arrayOf(ShadifySetPairPositions("a",
        arrayListOf(arrayOf(1,3,4))
    )))
)