package com.anyline.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.anyline.core.base.BaseViewModel
import com.anyline.dto.NetworkState
import com.anyline.dto.User
import kotlinx.coroutines.flow.Flow

abstract class MainViewModel : BaseViewModel() {

    abstract fun fetchUsers(query : String)

    abstract fun fetchUser(userName : String)

    abstract fun getUsers() : Flow<PagingData<User>>

    abstract fun getUser() : LiveData<User>

    abstract fun getNetworkStatus() : LiveData<NetworkState>

}



