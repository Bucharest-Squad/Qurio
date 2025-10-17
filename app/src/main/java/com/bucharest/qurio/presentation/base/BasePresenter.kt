package com.bucharest.qurio.presentation.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BasePresenter<V : BaseView> {

    private var _view: V? = null
    protected val view: V?
        get() = _view

    private var job = SupervisorJob()
    private var presenterScope = CoroutineScope(job + Dispatchers.Main)

    open fun attachView(view: V) {
        this._view = view
        if (job.isCancelled) {
            job = SupervisorJob()
            presenterScope = CoroutineScope(job + Dispatchers.Main)
        }
        onViewAttached()
    }

    open fun onViewAttached() {}

    open fun detachView() {
        job.cancel()
        _view = null
        onViewDetached()
    }

    open fun onViewDetached() {}

    protected fun executeIfViewAttached(action: V.() -> Unit) {
        _view?.let { action(it) }
    }

    protected fun <T> tryToExecute(
        execute: suspend () -> T,
        onSuccess: (suspend (T) -> Unit),
        onError: (suspend (Throwable) -> Unit),
        onStart: (suspend () -> Unit)? = null,
        onFinally: (suspend () -> Unit)? = null,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
    ): Job {
        return presenterScope.launch {
            try {
                onStart?.invoke()
                val result = withContext(dispatcher) {
                    execute()
                }
                onSuccess(result)
            } catch (throwable: Throwable) {
                onError(throwable)
            } finally {
                onFinally?.invoke()
            }
        }
    }
}