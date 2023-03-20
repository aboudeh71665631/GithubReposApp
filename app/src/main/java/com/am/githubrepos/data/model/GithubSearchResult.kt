package com.am.githubrepos.data.model

import com.google.gson.annotations.SerializedName

data class GithubSearchResult(
    @SerializedName("total_count")
    val totalCount: Int,
    @SerializedName("items")
    val githubRepos: List<GithubRepository>
)