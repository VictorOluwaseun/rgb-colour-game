package com.plentastudio.colorgame.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plentastudio.colorgame.R
import com.plentastudio.colorgame.databinding.HeaderColorGameBinding
import com.plentastudio.colorgame.databinding.ListItemColorGameBinding
import com.plentastudio.colorgame.entity.ColourBox
import com.plentastudio.colorgame.extension.toggleSelection
import com.plentastudio.colorgame.pojo.DataItem
import com.plentastudio.colorgame.ui.ColourGameViewModel
import com.plentastudio.colorgame.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ColourGameAdapter(val clickListener: ColorGameListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ColourDiffCallback()) {
    private val TAG = ColourGameAdapter::class.java.simpleName

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    lateinit var appActivity: Activity
    lateinit var viewModel: ColourGameViewModel
    lateinit var lifecycle: LifecycleOwner

    var selectedRGB: ColourBox? = null

    fun addHeaderAndColorItem(list: List<ColourBox>?, isRight: Boolean = false) {
        adapterScope.launch {
            selectedRGB = list?.find { BooleanUtils.isTrue(it.isCorrect) }
            val items = when (list) {
                null -> listOf(DataItem.Header(isRight))
                else -> listOf(DataItem.Header(isRight)) + list.map { DataItem.ColorBoxItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
                Log.d(TAG, "Adapter $items")
//                Log.d(TAG, "Adapter $selectedRGB")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> ColorHeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ColorItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ColorItemViewHolder -> {
                val colourBoxItem = getItem(position) as DataItem.ColorBoxItem
                holder.bind(viewModel, colourBoxItem.colourBox, clickListener)
            }
            is ColorHeaderViewHolder -> {
                val header = getItem(position) as DataItem.Header
                holder.bind(appActivity, viewModel, lifecycle, selectedRGB!!, header.isRight)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ColorBoxItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ColorHeaderViewHolder private constructor(val binding: HeaderColorGameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(appContext: Context, viewModel: ColourGameViewModel, lifecycle: LifecycleOwner, selectedText: ColourBox, isRight: Boolean) {
//            if (isRight){
//            } else{
//                viewModel._isCorrect.value = false
//            }

            binding.viewModel = viewModel
            binding.lifecycleOwner = lifecycle
            viewModel.isCorrect.observe(binding.lifecycleOwner!!, {
                if (BooleanUtils.isFalse(it)){
                    binding.btnNewColours.text = appContext.getText(R.string.new_colours)
                    binding.clHeader.setBackgroundColor(appContext.getColor(R.color.steel_blue))
                    return@observe
                }
                try {
                    binding.clHeader.setBackgroundColor(Color.parseColor(selectedText.color))
                } catch (err: IllegalArgumentException){
                    viewModel.squaresNum.value?.let { it1 -> viewModel.starGame(it1) }
                }
                binding.btnNewColours.text = "Play Again?"
            })


//            binding.btnNewColours
//            binding.btnEasy

            binding.btnHard.setOnClickListener {
                it.toggleSelection(it, binding.btnEasy)
                viewModel.starGame()
            }

            binding.btnEasy.setOnClickListener {
                it.toggleSelection(it, binding.btnHard)
                viewModel.starGame(Constants.EASY.getInt())
            }
//            binding.tvRgbColour.text = appContext.getString(R.string.rgb, selectedText.r, selectedText.g, selectedText.b)
            binding.tvRgbColour.text = "RGB(${selectedText.r.toString()}, ${selectedText.g.toString()}, ${selectedText.b.toString()})"
        }

        companion object {
            fun from(parent: ViewGroup): ColorHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderColorGameBinding.inflate(layoutInflater, parent, false)
                return ColorHeaderViewHolder(binding)
            }
        }
    }

    class ColorItemViewHolder private constructor(val binding: ListItemColorGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: ColourGameViewModel, colourBox: ColourBox, clickListener: ColorGameListener) {
            binding.colourBox = colourBox
            try {
                binding.btnColorBox.setColorFilter(Color.parseColor(colourBox.color)) //, PorterDuff.Mode.SRC_ATOP throws an IllegalArgumentException exception.
            }catch (e: IllegalArgumentException){
                viewModel.starGame()
            }
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ColorItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemColorGameBinding.inflate(layoutInflater, parent, false)
                return ColorItemViewHolder(binding)
            }
        }
    }

}

class ColourDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}

class ColorGameListener(val clickListener: (colorId: Long) -> Unit) {
    fun onClick(color: ColourBox) = clickListener(color.id)
}