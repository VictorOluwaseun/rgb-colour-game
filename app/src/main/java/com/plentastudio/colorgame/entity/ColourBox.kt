package com.plentastudio.colorgame.entity

data class ColourBox(
    var id: Long = 0L,
    var color: String? = null,
    var r: String,
    var g: String,
    var b: String,
    var isCorrect: Boolean = false
)