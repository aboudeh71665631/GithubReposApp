package com.am.githubrepos.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.am.githubrepos.data.model.GithubRepository
import retrofit2.HttpException
import java.io.IOException

private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val reposService: ReposService
) : PagingSource<Int, GithubRepository>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubRepository> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            val response = reposService.getAllRepos(params.loadSize, position)
            if (!response.isSuccessful) {
                throw HttpException(response)
                //TODO Handle the forbidden error and display a message, and maybe some other Http errors
            }
            val repos = response.body()!!
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (ex: IOException) {
            Log.e("IOException", "${ex.stackTrace}")
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            Log.e("HttpException", "${ex.stackTrace}")
            LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubRepository>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition((anchorPosition))?.prevKey?.plus(1)
                ?: state.closestPageToPosition((anchorPosition))?.nextKey?.minus(1)
        }
    }
}