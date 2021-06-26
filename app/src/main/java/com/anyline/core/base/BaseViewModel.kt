package com.anyline.core.base

import android.os.Looper
import androidx.lifecycle.*
import com.anyline.core.BaseObserver
import com.anyline.dto.NetworkState
import com.anyline.utils.SingleLiveEvent


abstract class BaseViewModel : ViewModel(), BaseObserver, LifecycleObserver {

    fun isMainThread(): Boolean {
        return Looper.myLooper() == Looper.getMainLooper()
    }

    private val externalUrl: SingleLiveEvent<String> = SingleLiveEvent()

    fun getOpenUrlInExternalBrowser(): SingleLiveEvent<String> = externalUrl

    fun requestOpenUrlInExternalBrowser(url: String) {
        externalUrl.postValue(url)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreated() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
    }
}