package com.anyline.api


import com.anyline.dto.BaseResponse
import com.anyline.dto.User
import io.reactivex.rxjava3.core.Observable

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun users(
        @Query("q") query   : String = "",
        @Query("per_page") pageSize : Int = 25,
        @Query("page") page : Int = 1
    ) : BaseResponse

    @GET("users/{user}")
    fun user(
        @Path("user") user : String
    ) : Observable<User>

}