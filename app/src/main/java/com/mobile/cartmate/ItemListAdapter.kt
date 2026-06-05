package com.mobile.cartmate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mobile.cartmate.data.ItemEntity
import com.mobile.cartmate.databinding.ItemLayoutBinding

class ItemListAdapter: ListAdapter<ItemEntity, ItemListAdapter.ItemViewHolder>(diffUtil) {
    val TAG = "ItemListAdapter"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemListAdapter.ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvItemName.text = item.product
        holder.binding.imageView.setImageResource(item.imageResId)
        holder.binding.tvItemCount.text = item.count.toString()
        holder.binding.tvItemPrice.text = item.price.toString()
    }

    inner class ItemViewHolder(val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                clickListener?.let {
                    it(bindingAdapterPosition)
                }
            }
            binding.root.setOnLongClickListener {
                var result = false
                longClickListener?.let {
                    result = it(bindingAdapterPosition)
                }
                result
            }
        }
    }
    var clickListener: ((pos: Int) -> Unit)? = null
    var longClickListener: ((pos: Int) -> Boolean)? = null

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ItemEntity>(){
            override fun areItemsTheSame(
                oldItem: ItemEntity,
                newItem: ItemEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ItemEntity,
                newItem: ItemEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}