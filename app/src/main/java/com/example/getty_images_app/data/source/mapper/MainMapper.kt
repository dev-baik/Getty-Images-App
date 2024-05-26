package com.example.getty_images_app.data.source.mapper

import com.example.getty_images_app.data.model.main.MainDTO
import com.example.getty_images_app.data.model.main.MainResponse
import com.example.getty_images_app.domain.model.HtmlParseResult
import com.example.getty_images_app.domain.model.MainList
import com.example.getty_images_app.domain.model.MainRecyclerViewItem

object MainMapper {

    fun HtmlParseResult<MainResponse>.toMainDomain(): HtmlParseResult<MainList> {
        return when (this) {
            is HtmlParseResult.Success -> HtmlParseResult.Success(data.toMainList())
            is HtmlParseResult.Error -> HtmlParseResult.Error(exception)
        }
    }

    private fun MainResponse.toMainList(): MainList {
        val mainItems = contents.map { it.toItem() }
        val combinedList = mainItems + MainRecyclerViewItem.PageNumber(pageNumber = 1)
        return MainList(combinedList)
    }

    private fun MainDTO.toItem(): MainRecyclerViewItem.MainItem =
        MainRecyclerViewItem.MainItem(
            url = this.url,
            title = this.title
        )
}