package com.plentastudio.colorgame.ui

import android.graphics.Color
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plentastudio.colorgame.entity.ColourBox
import com.plentastudio.colorgame.utils.Constants

class ColourGameViewModel : ViewModel() {
    private val TAG = ColourGameViewModel::class.java.simpleName

    private var _squaresNum = MutableLiveData<Int>()
    val squaresNum: LiveData<Int>
        get() = _squaresNum

    private var _colours = MutableLiveData<List<ColourBox>>()
    val colours: LiveData<List<ColourBox>>
        get() = _colours

    private var _selectedColor = MutableLiveData<ColourBox>()
    val selectedColor: LiveData<ColourBox>
        get() = _selectedColor

    init {
        _squaresNum.value = Constants.HARD.getInt()
        _colours.value = squaresNum.value?.let { generateRandomColours() }
        _selectedColor.value = colours.value?.let { colorPicker() }
    }

    private fun generateRandomColours(): List<ColourBox> {
        var colours = arrayListOf<ColourBox>()
        for (c in 1..squaresNum.value!!) {
            colours.add(generateColours())
        }
        Log.d(TAG, "Colours==>$colours")
        return colours
    }

    private fun generateColours(): ColourBox {
        val range = (-1..256)
        val red = range.random()
        val green = range.random()
        val blue = range.random()
//        val colour = intArrayOf(red, green, blue)
        val rgbColors = String.format("#%02x%02x%02x", red, green, blue)
//        val rgbColors = Color.rgb(red, green, blue)
        ColourBox().apply {
            id = System.currentTimeMillis()
            color = rgbColors
            r = red
            g = green
            b = blue
            isCorrect = false
            return this
        }
    }

    private fun colorPicker(): ColourBox {
        val colourArr = mutableListOf<ColourBox>()
        colourArr.addAll(colours.value!!)
        val range = (0 until squaresNum.value!!)
        val randomIndex = range.random()
        Log.d(TAG, "Range==> $range")
        val selectedColor = colourArr[randomIndex]
        selectedColor.isCorrect = true
        colourArr[randomIndex] = selectedColor
        _colours.value = colourArr
        return selectedColor
    }

    fun starGame() {
        _colours.value = squaresNum.value?.let { generateRandomColours() }
        _selectedColor.value = colours.value?.let { colorPicker() }
    }
}