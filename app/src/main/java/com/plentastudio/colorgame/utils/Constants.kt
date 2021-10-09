package com.plentastudio.colorgame.utils

import org.apache.commons.lang3.StringUtils

enum class Constants(private val value: String) {
    EASY("3"),
    HARD("6");

    fun getInt(): Int{
        return this.value.toInt()
    }
}