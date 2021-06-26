package com.anyline.dto

import androidx.annotation.Keep


@Keep
data class BaseResponse(
    @Keep val total_count: Long,
    @Keep val incomplete_results: Boolean,
    @Keep val items: List<User>
)

