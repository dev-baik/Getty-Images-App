package com.example.getty_images_app.presentation.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.getty_images_app.R
import com.example.getty_images_app.databinding.ActivityMainBinding
import com.example.getty_images_app.domain.model.MainList
import com.example.getty_images_app.presentation.main.ui.list.MainRecyclerViewAdapter
import com.example.getty_images_app.presentation.main.viewmodel.MainViewModel
import com.example.getty_images_app.presentation.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.jsoup.HttpStatusException
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    private lateinit var mainRecyclerViewAdapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initMainRecyclerView()
        setViewModelObserve()
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        btnRetry.setOnClickListener {
            viewModel.currentPage.value?.let {
                viewModel.getMainInfo(it)
            }
        }
    }

    private fun setViewModelObserve() = with(viewModel) {
        currentPage.observe(this@MainActivity) {
            viewModel.getMainInfo(it)
            hideKeyboard()
        }

        lifecycleScope.launch {
            mainUiState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { uiState ->
                    handleUiState(uiState)
                }
        }
    }

    private fun hideKeyboard() {
        if (currentFocus == null) return
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun handleUiState(uiState: MainUiState<MainList>) {
        when (uiState) {
            is MainUiState.Loading -> handleLoadingState()
            is MainUiState.Success -> handleSuccessState(uiState)
            is MainUiState.Error -> handleErrorState(uiState)
        }
    }

    private fun handleErrorState(uiState: MainUiState.Error) {
        binding.pbLoading.visibility = View.GONE

        when (uiState.error) {
            is HttpStatusException -> {
                showToast("원하시는 페이지를 찾을 수 없습니다.")
                viewModel.previousPage.value?.let { viewModel.setPageNumber(it) }
            }

            is UnknownHostException -> {
                binding.clWifiInstability.visibility = View.VISIBLE
            }

            else -> {
                binding.clWifiInstability.visibility = View.VISIBLE
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun handleSuccessState(uiState: MainUiState.Success<MainList>) {
        viewModel.previousPage.value = viewModel.currentPage.value
        mainRecyclerViewAdapter.submitList(uiState.data.contents)
        binding.pbLoading.visibility = View.GONE
        binding.clWifiInstability.visibility = View.GONE
    }

    private fun handleLoadingState() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun initMainRecyclerView() {
        mainRecyclerViewAdapter = MainRecyclerViewAdapter(
            mainBottomListener = object : MainRecyclerViewAdapter.MainBottomListener {
                override fun goToPreviousPage() {
                    viewModel.decreasePageNumber()
                }

                override fun goToNextPage() {
                    viewModel.increasePageNumber()
                }

                override fun goToSetPage(pageNumber: Int) {
                    viewModel.setPageNumber(pageNumber)
                }
            }
        )
        binding.rvMainList.adapter = mainRecyclerViewAdapter
        binding.rvMainList.layoutManager = createGridLayoutManager()
    }

    private fun createGridLayoutManager(): GridLayoutManager {
        return GridLayoutManager(this, 3).apply {
            spanSizeLookup = createSpanSizeLookup(spanCount)
        }
    }

    private fun createSpanSizeLookup(spanCount: Int): GridLayoutManager.SpanSizeLookup {
        return object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (mainRecyclerViewAdapter.getItemViewType(position)) {
                    R.layout.item_main_list -> 1
                    R.layout.item_main_bottom -> spanCount
                    else -> throw IllegalArgumentException("ViewType is not valid")
                }
            }
        }
    }
}