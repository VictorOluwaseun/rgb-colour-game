package com.plentastudio.colorgame.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plentastudio.colorgame.R
import com.plentastudio.colorgame.entity.ColourBox
import com.plentastudio.colorgame.pojo.DataItem


private const val  ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ColourGameAdapter(val clickListener: ColorGameListener): ListAdapter<DataItem, RecyclerView.ViewHolder>(ColourDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class ColorHeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): ColorHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.header_color_game, parent, false)
                return ColorHeaderViewHolder(view)
            }
        }
    }

    class ColorItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): ColorItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_color_game, parent, false)
               return ColorItemViewHolder(view)
            }
        }
    }

}

class ColourDiffCallback: DiffUtil.ItemCallback<DataItem>(){
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