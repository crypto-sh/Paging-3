package com.anyline.repository.dataSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.anyline.api.ApiService
import com.anyline.dto.User


class DataSourceUsers(
    private val query: String,
    private val pageSize: Int,
    private val apiService: ApiService
) : PagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        val anchorPosition: Int = state.anchorPosition ?: return null
        val (_, prevKey, nextKey) = state.closestPageToPosition(anchorPosition) ?: return null
        if (prevKey != null) {
            return prevKey + 1
        }
        if (nextKey != null) {
            return nextKey - 1
        }
        return null
    }

    /**
     * Loading API for [PagingSource].
     *
     * Implement this method to trigger your async load (e.g. from database or network).
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val current: Int = params.key ?: 1
            val result = apiService.users(query, pageSize, current)
            LoadResult.Page(
                data = result.items,
                prevKey = null,
                nextKey = current + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}