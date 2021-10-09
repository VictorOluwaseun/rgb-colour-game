package com.plentastudio.colorgame.entity

data class ColourBox(
    var id: Long = 0L,
    var color: String? = null,
    var r: Int,
    var g: Int,
    var b: Int,
    var isCorrect: Boolean = false
)