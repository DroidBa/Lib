package com.cecilleo.lib.mvp

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import rx.Subscription

abstract class MVPPresenter<V : MVPView?> constructor(view: V) : LifecycleObserver,
    CoroutineScope by MainScope() {
  private var mRxManager: MVPRxManager = MVPRxManager()

  init {
    mRxManager.onCreate()
  }

  private val mView: V = view

  private var isViewAttached: Boolean = false

  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart(owner: LifecycleOwner) {
    Log.d("Leo", "The View(${mView?.javaClass?.simpleName}) is attached")
    mRxManager.onStart()
    isViewAttached = true
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop(owner: LifecycleOwner) {
    Log.d("Leo", "The View(${mView?.javaClass?.simpleName}) is not attached")
    mRxManager.onStop()
    isViewAttached = false
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy(owner: LifecycleOwner) {
    mRxManager.onDestroy()
  }

  protected fun addUtilStop(subscription: Subscription): Boolean {
    return mRxManager.addUtilStop(subscription)
  }

  protected fun addUtilDestroy(subscription: Subscription): Boolean {
    return mRxManager.addUtilDestroy(subscription)
  }

  protected fun remove(subscription: Subscription) {
    mRxManager.remove(subscription)
  }

  fun onDestroy() {
    coroutineContext.cancelChildren()
  }
}