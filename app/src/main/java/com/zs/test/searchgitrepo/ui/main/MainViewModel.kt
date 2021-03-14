package com.zs.test.searchgitrepo.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zs.test.searchgitrepo.R
import com.zs.test.searchgitrepo.data.ResourceHelper
import com.zs.test.searchgitrepo.data.common.SingleLiveEvent
import com.zs.test.searchgitrepo.data.network.RetrofitManager
import com.zs.test.searchgitrepo.data.network.api.GitService
import com.zs.test.searchgitrepo.data.network.entity.SearchResult
import com.zs.test.searchgitrepo.data.network.entity.SearchResultError
import com.zs.test.searchgitrepo.ui.main.model.RepositoryAdapter
import com.zs.test.searchgitrepo.ui.main.model.RepositoryItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    companion object {
        private const val PER_PAGE = 30
        private const val LOAD_MORE_THRESHOLD = 10
    }

    val errorMessage = MutableLiveData<String>("")
    val query = MutableLiveData<String>("")
    var page = 0
    var isLastPage = false

    private val listData = ArrayList<RepositoryItemViewModel>()
    val isProgress = MutableLiveData<Boolean>(false)

    lateinit var adapter: RepositoryAdapter

    var searchingJob: Job? = null

    val toastEvent = SingleLiveEvent<String>()

    private val gitService by lazy {
        RetrofitManager.createService(GitService::class.java)
    }

    fun initAdapter(): RepositoryAdapter {
        adapter = RepositoryAdapter(listData)
        return adapter
    }

    private fun hideErrorMessage() {
        errorMessage.postValue("")
    }

    private fun showErrorMessage(message: String) {
        errorMessage.postValue(message)
    }

    fun onClickSearch() {
        hideErrorMessage()
        page = 0
        listData.clear()
        adapter.notifyDataSetChanged()

        query.value.let {
            if (it == null || it.isEmpty()) {
                showErrorMessage(ResourceHelper.getString(R.string.error_query_empty))
            } else {
                isProgress.value = true
                search(it, page)
            }
        }
    }

    private fun search(query: String, page: Int) {
        isLastPage = false
        searchingJob = viewModelScope.launch (Dispatchers.IO) {
            try {
                val result = gitService.getRepos(query, page, PER_PAGE)
                onReceivedResult(result)
            } catch (e: HttpException) {
                onReceivedError(RetrofitManager.parseError(e.response()))
            }
        }
    }

    private fun searchMore(): Boolean {
        if (isLastPage) {
            return false
        }
        if (searchingJob?.isActive == true) {
            return false
        }

        query.value?.also {
            search(it, ++page)
        }
        return true
    }

    fun onRecyclerViewScrolled(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager?: return
        val visibleItemCount = recyclerView.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if((totalItemCount - visibleItemCount) <= (firstVisibleItem + LOAD_MORE_THRESHOLD)) {
            if (searchMore()) {
                Log.d(
                    "SearchMore",
                    "totalItemCount : $totalItemCount, searchMoreIndex : ${firstVisibleItem + LOAD_MORE_THRESHOLD}"
                )
            }
        }
    }

    suspend fun onReceivedResult(result: SearchResult) {
        withContext(Dispatchers.Main) {
            isProgress.value = false
            if (checkEmptyResult(result)) return@withContext
            if (checkLastPage(result)) {
                toastEvent.value = ResourceHelper.getString(R.string.message_last_page)
            }
            showResult(result)
        }
    }

    private fun showResult(result: SearchResult) {
        result.items.map { item ->
            RepositoryItemViewModel(item)
        }.apply {
            listData.addAll(this)
            if (page == 0) {
                adapter.notifyDataSetChanged()
            } else {
                adapter.notifyItemRangeInserted(listData.size - size, size)
            }
        }
    }

    suspend fun onReceivedError(error: SearchResultError) {
        withContext(Dispatchers.Main) {
            isProgress.value = false
            showErrorMessage(error.getErrorMessage())
        }
    }

    private fun checkEmptyResult(result: SearchResult): Boolean {
        if (result.items.isEmpty()) {
            if (page == 0) {
                showErrorMessage(ResourceHelper.getString(R.string.error_result_empty))
            } else {
                toastEvent.value = ResourceHelper.getString(R.string.message_last_page)
            }
            return true
        }
        return false
    }

    private fun checkLastPage(result: SearchResult): Boolean {
        if (result.items.size < PER_PAGE) {
            isLastPage = true
        }
        return isLastPage
    }
}