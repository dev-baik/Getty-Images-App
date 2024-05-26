package com.example.getty_images_app.domain.model

sealed class MainRecyclerViewItem {
    data class MainItem(val url: String, val title: String) : MainRecyclerViewItem()
    data class PageNumber(val pageNumber: Int) : MainRecyclerViewItem()
}