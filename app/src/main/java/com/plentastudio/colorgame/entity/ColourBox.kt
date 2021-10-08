package com.plentastudio.colorgame.entity

data class ColourBox(
    var id: Long = 0L,
    var color: String? = null,
    var isCorrect: Boolean = false
)