package com.am.githubrepos.ui.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.am.githubrepos.data.Constants
import com.am.githubrepos.data.ReposRepository
import com.am.githubrepos.data.model.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReposViewModel @Inject constructor(private val repo: ReposRepository) : ViewModel() {

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _singleRepoResponse = MutableLiveData<GithubRepository>()
    val singleRepoResponse: LiveData<GithubRepository>
        get() = _singleRepoResponse

    fun getData() = repo.getPagedRepos().cachedIn(viewModelScope)

    fun getSingleRepo(url: String) = viewModelScope.launch {
        repo.getSingleRepo(url).let { response ->
            if (response.isSuccessful) {
                _singleRepoResponse.postValue(response.body())
            } else {
                _error.postValue(Constants.INTERNAL_ERROR)
            }
        }
    }
}