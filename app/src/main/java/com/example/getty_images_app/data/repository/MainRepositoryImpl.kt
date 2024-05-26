package com.example.getty_images_app.data.repository

import com.example.getty_images_app.data.source.main.MainDataSource
import com.example.getty_images_app.data.source.mapper.MainMapper.toMainDomain
import com.example.getty_images_app.domain.model.HtmlParseResult
import com.example.getty_images_app.domain.model.MainList
import com.example.getty_images_app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainDataSource: MainDataSource,
) : MainRepository {

    override fun getMainList(pageNumber: Int): Flow<HtmlParseResult<MainList>> {
        return mainDataSource.getMainList(pageNumber).map { it.toMainDomain() }
    }
}