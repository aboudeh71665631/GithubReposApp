package com.am.githubrepos.data.remote

import com.am.githubrepos.data.Constants
import com.am.githubrepos.data.model.GithubRepository
import com.am.githubrepos.data.model.GithubSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ReposService {
    @GET(Constants.ORG_ALL_REPOS_QUERY)
    suspend fun getAllRepos(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<GithubRepository>>

    suspend fun getSearchRepos(
        @Query("q") query: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<GithubSearchResult>

    @GET
    suspend fun getSingleRepo(@Url url: String): Response<GithubRepository>
}