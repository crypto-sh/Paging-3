package com.anyline.core

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.net.UnknownHostException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface BaseObserver {

    companion object {

        private val disposables = CompositeDisposable()

        private val ioThread   = Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors() * 2) - 4)

        val apiThreads: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 2)
    }

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    fun Disposable.toDisposable() = disposables.add(this)

    fun clearDisposable() {
        disposables.clear()
    }

    /**
     *
     * this observer type is used for api calling
     * as retrofit is not handling the dispatcher of the OkHttp we need to limit calling api in RxJava
     * so we are using 'Schedulers.computation()' which is similar to io but limited by the device capacity.
     *
     */
    fun <T> addExecutorThreads(observable: Single<T>, onSuccess: ((T) -> Unit)? = null, onError: ((Throwable) -> Unit)? = null) {
        addDisposable(observable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.from(apiThreads))
            .subscribe({ result ->
                onSuccess?.invoke(result)
            }, { throwable ->
                onError?.invoke(throwable)
                if (throwable !is UnknownHostException){
                    Timber.e(throwable)
                }
            }))
    }

}