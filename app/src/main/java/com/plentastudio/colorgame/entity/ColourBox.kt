package com.plentastudio.colorgame.entity

data class ColourBox(
    var id: Long = 0L,
    var color: String? = null,
    var r: Int? = null,
    var g: Int? = null,
    var b: Int? = null,
    var isCorrect: Boolean = false
)