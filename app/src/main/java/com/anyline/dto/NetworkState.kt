package com.anyline.dto

import androidx.annotation.Keep
import androidx.annotation.StringRes
import com.anyline.R


@Keep
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

@Keep
enum class ErrorType(@StringRes val resource: Int) {

    //api error type
    EOFException(R.string.eofException),
    IOException(R.string.ioException),
    InternetConnection(R.string.internetConnectionException),
    SSLHandShake(R.string.sslException),
    Authorization(R.string.authorization),
    HttpException(R.string.generic_error),
    Undefine(R.string.generic_error)
}

data class NetworkState(
    var status: Status,
    var type: ErrorType = ErrorType.Undefine,
    var tag: String = "",
    var msg: String = "",
    var code: Int = 200
) {

    companion object {
        fun loaded(tag: String) = NetworkState(Status.SUCCESS, tag = tag)
        fun loading(tag: String) = NetworkState(Status.RUNNING, tag = tag)
        public fun error(type: ErrorType, tag: String, msg: String = "", code: Int = 200) =
            NetworkState(status = Status.FAILED, type = type, tag = tag, msg = msg, code = code)
    }
}