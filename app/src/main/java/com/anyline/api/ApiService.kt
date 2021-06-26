package com.anyline.api


import com.anyline.dto.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/users")
    suspend fun users(
        @Query("q") query   : String = "",
        @Query("per_page") pageSize : Int = 25,
        @Query("page") page : Int = 1
    ) : BaseResponse

}