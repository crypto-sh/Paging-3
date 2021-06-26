package com.anyline.ui.viewModel

import androidx.paging.PagingData
import com.anyline.core.base.BaseViewModel
import com.anyline.dto.User
import kotlinx.coroutines.flow.Flow

abstract class MainViewModel : BaseViewModel() {

    abstract fun fetchUsers(query : String)

    abstract fun getUsers() : Flow<PagingData<User>>

}



