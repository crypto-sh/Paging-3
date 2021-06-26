package com.anyline.dto

import androidx.annotation.Keep
import java.util.*


@Keep
data class User(
    @Keep val id : String = "",
    @Keep val node_id : String = "",
    @Keep val login : String = "",
    @Keep val type : String = "",
    @Keep val avatar_url : String = "",
    @Keep val url : String = "",
    @Keep val html_url : String = "",
    @Keep val site_admin : Boolean = false,
    @Keep val name : String = "",
    @Keep val company : String = "",
    @Keep val blog : String = "",
    @Keep val location : String = "",
    @Keep val email : String = "",
    @Keep val bio : String? = "",
    @Keep val twitter_username : String? = "",
    @Keep val followers : Long = 0,
    @Keep val following : Long = 0,
    @Keep val created_at : Date = Date()
)