package com.anyline.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.anyline.api.ApiService
import com.anyline.dto.User
import com.anyline.repository.dataSource.DataSourceUsers
import com.anyline.ui.viewModel.MainViewModelImpl
import kotlinx.coroutines.flow.Flow


class GithubRepositoryImpl(private val apiService: ApiService) : GithubRepository{

    override fun getUsers(query: String, pageSize: Int): Flow<PagingData<User>> {
        return Pager(
            PagingConfig(pageSize = MainViewModelImpl.PAGE_SIZE)
        ) {
            DataSourceUsers(query, MainViewModelImpl.PAGE_SIZE, apiService)
        }.flow
    }
}