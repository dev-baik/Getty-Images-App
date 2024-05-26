package com.example.getty_images_app.presentation.main.ui.list.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.getty_images_app.databinding.ItemMainListBinding
import com.example.getty_images_app.domain.model.MainRecyclerViewItem

class MainListViewHolder(
    private val binding: ItemMainListBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainRecyclerViewItem.MainItem) {
        binding.item = item
    }

    companion object {
        fun create(parent: ViewGroup): MainListViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMainListBinding.inflate(inflater, parent, false)
            return MainListViewHolder(binding)
        }
    }
}