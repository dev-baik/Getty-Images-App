package com.example.getty_images_app.data.source.main

import com.example.getty_images_app.data.model.main.MainDTO
import com.example.getty_images_app.data.model.main.MainResponse
import com.example.getty_images_app.domain.model.HtmlParseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor() : MainDataSource {

    override fun getMainList(pageNumber: Int): Flow<HtmlParseResult<MainResponse>> = flow {
        try {
            val document = fetchDocumentFromUrl(pageNumber)
            val mainList = parseImagesFromDocument(document)
            emit(HtmlParseResult.Success(MainResponse(mainList)))
        } catch (e: Exception) {
            emit(HtmlParseResult.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    private fun fetchDocumentFromUrl(pageNumber: Int): Document {
        val url = "https://www.gettyimages.com/search/2/image?alloweduse=availableforalluses&family=creative&phrase=collaboration&sort=mostpopular&page=$pageNumber"
        return Jsoup.connect(url).get()
    }

    private fun parseImagesFromDocument(document: Document): List<MainDTO> {
        return document.select("div[data-testid=galleryMosaicAsset]").map {
            val imageUrl = it.select("img").attr("src")
            val title = it.select("meta[itemprop=name]").attr("content")
            MainDTO(imageUrl, title)
        }
    }
}