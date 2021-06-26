package com.anyline.dto

import androidx.annotation.Keep


@Keep
data class User(
    @Keep val id : String,
    @Keep val node_id : String,
    @Keep val login : String,
    @Keep val type : String,
    @Keep val avatar_url : String,
    @Keep val url : String,
    @Keep val html_url : String,
    @Keep val site_admin : Boolean
)