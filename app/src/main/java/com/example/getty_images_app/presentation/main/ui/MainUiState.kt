package com.example.getty_images_app.presentation.main.ui

sealed class MainUiState<out T>(val _data: T?) {
    object Loading : MainUiState<Nothing>(_data = null)
    data class Error(val error: Throwable) : MainUiState<Nothing>(_data = null)
    data class Success<out R>(val data: R) : MainUiState<R>(_data = data)
}