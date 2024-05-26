package com.example.getty_images_app.domain.model

sealed class HtmlParseResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : HtmlParseResult<T>()
    data class Error(val exception: Exception) : HtmlParseResult<Nothing>()
}