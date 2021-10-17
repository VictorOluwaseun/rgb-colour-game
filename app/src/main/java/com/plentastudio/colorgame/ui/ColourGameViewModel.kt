package com.plentastudio.colorgame.ui

import android.graphics.Color
import android.util.Log
import android.widget.ArrayAdapter
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

    var _colours = MutableLiveData<List<ColourBox>>()
    val colours: LiveData<List<ColourBox>>
        get() = _colours

    private var _selectedColor = MutableLiveData<ColourBox>()
    val selectedColor: LiveData<ColourBox>
        get() = _selectedColor

    var _isCorrect = MutableLiveData<Boolean>()
    val isCorrect: LiveData<Boolean>
        get() = _isCorrect

    init {
        _squaresNum.value = Constants.HARD.getInt()
        _colours.value = generateRandomColours()
        _selectedColor.value = colorPicker()
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
            id = System.currentTimeMillis() + range.random()
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

    fun modifiedColors() {
        val newColours = colours.value as ArrayList
        for (i in 0 until newColours.size) {
            val newColor = newColours[i]
            newColor.color = selectedColor.value?.color
            newColor.id = selectedColor.value?.id!!
            newColours[i] = newColor
        }
        Log.d(TAG, newColours.toString())
        _colours.value = newColours
    }

    fun starGame(squaresNum: Int = Constants.HARD.getInt()) {
        Log.d(TAG, "Start Game")
        if (squaresNum != _squaresNum.value){
            _squaresNum.value = squaresNum
        }
        _colours.value = generateRandomColours()
        _selectedColor.value = colorPicker()
        _isCorrect.value = false
    }
}