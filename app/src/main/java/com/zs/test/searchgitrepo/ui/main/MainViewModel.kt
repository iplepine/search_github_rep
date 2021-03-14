package com.zs.test.searchgitrepo.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zs.test.searchgitrepo.R
import com.zs.test.searchgitrepo.data.ResourceHelper
import com.zs.test.searchgitrepo.data.network.RetrofitManager
import com.zs.test.searchgitrepo.data.network.api.GitService
import com.zs.test.searchgitrepo.data.network.entity.Repository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>("검색어를 입력하세요.")
    val query = MutableLiveData<String>("")
    var page = 0
    val list = ArrayList<Repository>()

    val gitService by lazy {
        RetrofitManager.createService(GitService::class.java)
    }

    private fun showErrorMessage(message: String) {
        errorMessage.postValue(message)
    }

    fun onClickSearch() {
        page = 0
        list.clear()

        viewModelScope.launch {
            query.value.let {
                if (it == null || it.isEmpty()) {
                    showErrorMessage(ResourceHelper.getString(R.string.error_query_empty))
                } else {

                }
            }
        }
    }

    private fun notifyDataChanged() {
    }

    suspend fun search(isAppend: Boolean) {
        if (isAppend) {
            page++
        } else {
            page = 0
            list.clear()
            notifyDataChanged()
        }

        query.value?.also {
            val result = gitService.getRepos(it, page)
        }
    }

    fun onSuccessResult() {

    }
}