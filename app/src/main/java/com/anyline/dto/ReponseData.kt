package com.anyline.dto

import androidx.annotation.Keep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.EOFException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException


@Keep
data class ResponseData<T>(
    @Keep val tag: String,
    @Keep val data: LiveData<T>,
    @Keep var status: MutableLiveData<NetworkState> = MutableLiveData()
) {
    fun loaded() {
        status.postValue(NetworkState.loaded(tag))
    }

    fun loading() {
        status.postValue(NetworkState.loading(tag))
    }

    fun handleError(t: Throwable) {
        this.loaded()
        when (t) {
            /**
             * server exception
             */
            is EOFException -> NetworkState.error(ErrorType.EOFException, tag = tag)
            is SSLHandshakeException -> NetworkState.error(ErrorType.SSLHandShake, tag = tag)
            is SocketTimeoutException,
            is UnknownHostException,
            is IOException -> NetworkState.error(ErrorType.InternetConnection, tag = tag)
            is HttpException -> when {
                t.code() == 401 -> NetworkState.error(ErrorType.Authorization, tag = tag)
                else -> NetworkState.error(ErrorType.HttpException, msg = getErrorMessage(t.response()?.errorBody()), tag = tag, code = t.code())
            }
            else -> {
                NetworkState.error(ErrorType.Undefine, tag = tag)
            }
        }
    }

    /**
     *
     * [getErrorMessage] is responsible for fetching error message from error body
     * which is happening in the response of api call.
     *
     */
    private fun getErrorMessage(errorBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(errorBody?.string() ?: "")
            when {
                jsonObject.has("error") -> {
                    val error = JSONObject(jsonObject.getString("error"))
                    error.getString("base")
                }
                else -> {
                    ""
                }
            }
        } catch (e: Exception) {
            ""
        }
    }
}
