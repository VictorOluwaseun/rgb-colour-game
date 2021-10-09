package com.plentastudio.colorgame.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plentastudio.colorgame.databinding.HeaderColorGameBinding
import com.plentastudio.colorgame.databinding.ListItemColorGameBinding
import com.plentastudio.colorgame.entity.ColourBox
import com.plentastudio.colorgame.pojo.DataItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ColourGameAdapter(val clickListener: ColorGameListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ColourDiffCallback()) {
    private val TAG = ColourGameAdapter::class.java.simpleName

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndColorItem(list: List<ColourBox>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.ColorBoxItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
                Log.d(TAG, "Adapter $currentList")
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
                holder.bind(colourBoxItem.colourBox, clickListener)
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

//        fun bind() {
//            binding.clHeader
//            binding.btnNewColours
//            binding.btnEasy
//            binding.btnHard
//        }

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
        fun bind(colourBox: ColourBox, clickListener: ColorGameListener) {
            binding.colourBox = colourBox
            binding.btnColorBox.setColorFilter(Color.parseColor(colourBox.color)) //, PorterDuff.Mode.SRC_ATOP
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