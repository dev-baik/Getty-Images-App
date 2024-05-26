package com.example.getty_images_app.presentation.main.ui.list.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.example.getty_images_app.databinding.ItemMainBottomBinding
import com.example.getty_images_app.domain.model.MainRecyclerViewItem
import com.example.getty_images_app.presentation.main.ui.list.MainRecyclerViewAdapter

class MainBottomViewHolder(
    private val binding: ItemMainBottomBinding,
    private val mainBottomListener: MainRecyclerViewAdapter.MainBottomListener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MainRecyclerViewItem.PageNumber) {
        binding.item = item
        binding.listener = mainBottomListener
        setClickListener()
    }

    private fun setClickListener() {
        binding.etPageNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mainBottomListener.goToSetPage(binding.etPageNumber.text.toString().toInt())
                true
            } else {
                false
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            mainBottomListener: MainRecyclerViewAdapter.MainBottomListener
        ): MainBottomViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMainBottomBinding.inflate(inflater, parent, false)
            return MainBottomViewHolder(binding, mainBottomListener)
        }
    }
}