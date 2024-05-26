package com.example.getty_images_app.presentation.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.getty_images_app.presentation.main.ui.MainUiState
import com.example.getty_images_app.domain.model.HtmlParseResult
import com.example.getty_images_app.domain.model.MainList
import com.example.getty_images_app.domain.model.MainRecyclerViewItem
import com.example.getty_images_app.domain.usecase.GetMainContentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getMainContentsUseCase: GetMainContentsUseCase,
) : ViewModel() {

    var previousPage = MutableLiveData<Int>()

    private val _currentPage = MutableLiveData<Int>(1)
    val currentPage: LiveData<Int> = _currentPage

    private val _mainUiState = MutableStateFlow<MainUiState<MainList>>(MainUiState.Loading)
    val mainUiState = _mainUiState.asStateFlow()

    fun getMainInfo(pageNumber: Int) {
        _mainUiState.value = MainUiState.Loading

        viewModelScope.launch {
            getMainContentsUseCase(pageNumber).collectLatest { htmlParseResult ->
                _mainUiState.value = when (htmlParseResult) {
                    is HtmlParseResult.Success -> MainUiState.Success(htmlParseResult.data.updateToCurrentPage())
                    is HtmlParseResult.Error -> MainUiState.Error(htmlParseResult.exception)
                }
            }
        }
    }

    private fun MainList.updateToCurrentPage(): MainList {
        val updatedContents = contents.map { item ->
            if (item is MainRecyclerViewItem.PageNumber) {
                item.copy(pageNumber = _currentPage.value ?: 1)
            } else item
        }
        return copy(contents = updatedContents)
    }

    fun decreasePageNumber() {
        _currentPage.value = _currentPage.value?.dec()
    }

    fun increasePageNumber() {
        _currentPage.value = _currentPage.value?.inc()
    }

    fun setPageNumber(pageNumber: Int) {
        _currentPage.value = pageNumber
    }
}