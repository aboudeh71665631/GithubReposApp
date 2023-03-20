package com.am.githubrepos.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.am.githubrepos.data.remote.GithubPagingSource
import com.am.githubrepos.data.remote.ReposService
import javax.inject.Inject

class ReposRepository @Inject constructor(private val reposApi: ReposService) {

    suspend fun getAllRepos(perPage: Int, page: Int) = reposApi.getAllRepos(perPage, page)

    suspend fun getSingleRepo(url: String) = reposApi.getSingleRepo(url)

    fun getPagedRepos() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { GithubPagingSource(reposApi) }
    ).liveData
}