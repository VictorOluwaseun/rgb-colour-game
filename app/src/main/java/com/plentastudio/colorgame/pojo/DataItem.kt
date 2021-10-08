package com.plentastudio.colorgame.pojo

import com.plentastudio.colorgame.entity.ColourBox

sealed class DataItem {
    data class ColorBoxItem(val colourBox: ColourBox): DataItem() {
        override val id = colourBox.id
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }

    abstract val id: Long
}
