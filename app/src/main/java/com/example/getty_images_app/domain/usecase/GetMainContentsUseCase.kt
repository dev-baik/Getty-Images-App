package com.example.getty_images_app.domain.usecase

import com.example.getty_images_app.domain.model.HtmlParseResult
import com.example.getty_images_app.domain.model.MainList
import com.example.getty_images_app.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMainContentsUseCase @Inject constructor(
    private val mainRepository: MainRepository,
) {

    operator fun invoke(pageNumber: Int): Flow<HtmlParseResult<MainList>> =
        mainRepository.getMainList(pageNumber)
}