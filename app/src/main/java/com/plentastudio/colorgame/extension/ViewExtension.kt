package com.plentastudio.colorgame.extension

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.plentastudio.colorgame.R

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun ImageView.setVectorColor(color: Int) =
    this.setColorFilter(color, PorterDuff.Mode.SRC_IN)

fun ImageView.setVectorColor(context: Context, color: Int) =
    this.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)

fun View.showIf(condition: Boolean) {
    visibility = if (condition) View.VISIBLE else View.GONE
}

fun View.showIfAndReturnConstraint(condition: Boolean): Boolean {
    val constraint = if (condition) View.VISIBLE else View.GONE
    visibility = constraint
    return condition
}

fun View.invisibleIf(condition: Boolean) {
    visibility = if (condition) View.INVISIBLE else View.VISIBLE
}

fun Button.toggleSelection(appContext: Context, selectedBtn: Button, deselected: Button) {
    deselected.setBackgroundColor(appContext.getColor(R.color.white))
    selectedBtn.setBackgroundColor(appContext.getColor(R.color.steel_blue))
}

fun View.toggleSelection(selectedBtn: View, deselectedBtn: View) {
//    deselected.setBackgroundColor(deselected.context.resources.getColor(R.color.white))
//    deselectedBtn.background.setColorFilter(
//        ContextCompat.getColor(this.context, R.color.white),
//        PorterDuff.Mode.MULTIPLY
//    )
//    selectedBtn.setBackgroundColor(selectedBtn.context.resources.getColor(R.color.steel_blue))
//    selectedBtn.background.setColorFilter(
//        ContextCompat.getColor(this.context, R.color.steel_blue),
//        PorterDuff.Mode.MULTIPLY
//    )

    if (Build.VERSION.SDK_INT >= 29) {
        selectedBtn.background.colorFilter = BlendModeColorFilter(
            selectedBtn.context.resources.getColor(R.color.steel_blue),
            BlendMode.MULTIPLY
        )
        deselectedBtn.background.colorFilter = BlendModeColorFilter(
            deselectedBtn.context.resources.getColor(R.color.white),
            BlendMode.MULTIPLY
        )
    } else {
        selectedBtn.background.setColorFilter(selectedBtn.context.resources.getColor(R.color.steel_blue), PorterDuff.Mode.MULTIPLY)
        deselectedBtn.background.setColorFilter(deselectedBtn.context.resources.getColor(R.color.white),PorterDuff.Mode.MULTIPLY)
    }
}