package com.plentastudio.colorgame.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plentastudio.colorgame.databinding.HeaderColorGameBinding
import com.plentastudio.colorgame.databinding.ListItemColorGameBinding
import com.plentastudio.colorgame.entity.ColourBox
import com.plentastudio.colorgame.pojo.DataItem


private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ColourGameAdapter(val clickListener: ColorGameListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ColourDiffCallback()) {
    private val TAG = ColourGameAdapter::class.java.simpleName

    fun addHeaderAndColorItem(list: List<ColourBox>?) {
        val items = when (list) {
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + list.map { DataItem.ColorBoxItem(it) }
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
                val colourItem = getItem(position) as DataItem.ColorBoxItem
                holder.bind(colourItem.colourBox, clickListener)
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

        fun bind() {
            binding.clHeader
            binding.btnNewColours
            binding.btnEasy
            binding.btnHard
        }

        companion object {
            fun from(parent: ViewGroup): ColorHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = HeaderColorGameBinding.inflate(layoutInflater, parent, false)
                return ColorHeaderViewHolder(view)
            }
        }
    }

    class ColorItemViewHolder private constructor(val binding: ListItemColorGameBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: ColourBox, clickListener: ColorGameListener) {
            binding.btnColorBox.setColorFilter(Color.rgb(color.r, color.g, color.b))
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ColorItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = ListItemColorGameBinding.inflate(layoutInflater, parent, false)
                return ColorItemViewHolder(view)
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