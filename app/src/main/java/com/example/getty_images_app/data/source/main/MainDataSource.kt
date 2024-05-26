package com.example.getty_images_app.data.source.main

import com.example.getty_images_app.data.model.main.MainResponse
import com.example.getty_images_app.domain.model.HtmlParseResult
import kotlinx.coroutines.flow.Flow

interface MainDataSource {

    fun getMainList(pageNumber: Int): Flow<HtmlParseResult<MainResponse>>
}