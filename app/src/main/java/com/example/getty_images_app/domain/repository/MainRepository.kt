package com.example.getty_images_app.domain.repository

import com.example.getty_images_app.domain.model.HtmlParseResult
import com.example.getty_images_app.domain.model.MainList
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    fun getMainList(pageNumber: Int): Flow<HtmlParseResult<MainList>>
}