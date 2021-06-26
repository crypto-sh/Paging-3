package com.anyline.repository

import androidx.paging.PagingData
import com.anyline.dto.ResponseData
import com.anyline.dto.User
import kotlinx.coroutines.flow.Flow


interface GithubRepository {

    fun getUsers(query: String, pageSize : Int) : Flow<PagingData<User>>

    fun getUser(user : String) : ResponseData<User>

}