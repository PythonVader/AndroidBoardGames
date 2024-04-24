package com.example.androidboardgames.data

data class ShadifyMemory(
    val width: Int,
    val height: Int,
    val pairSize: Int,
    val totalPairs: Int,
    val grid: Array<Array<String>>,
    val pairPositions: Array<ShadifySetPairPositions>
)
