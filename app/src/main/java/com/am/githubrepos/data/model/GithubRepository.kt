package com.am.githubrepos.data.model

import com.google.gson.annotations.SerializedName

data class GithubRepository(
    val id: Int,
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("created_at")
    val creationDate: String,
    val owner: Owner,
    val stargazers_count: Int
)
