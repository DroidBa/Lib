package com.cecilleo.core.base.mvp

import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * Created by Vincent on 2016/11/24.
 */
class MVPRxManager {

  private var mSubscription2Stop: CompositeSubscription? = null

  private var mSubscription2Destroy: CompositeSubscription? = null

  @Synchronized
  fun onCreate() {
    if (mSubscription2Destroy != null) {
      throw IllegalStateException("onCreate called multiple times")
    }
    mSubscription2Destroy = CompositeSubscription()
  }

  @Synchronized
  fun onStart() {
    if (mSubscription2Stop != null) {
      throw IllegalStateException("onStart called multiple times")
    }
    mSubscription2Stop = CompositeSubscription()
  }

  @Synchronized
  fun addUtilStop(subscription: Subscription): Boolean {
    if (mSubscription2Stop == null) {
      throw IllegalStateException("addUtilStop should be called between onStart and onStop")
    }
    mSubscription2Stop!!.add(subscription)
    return true
  }

  @Synchronized
  fun addUtilDestroy(subscription: Subscription): Boolean {
    if (mSubscription2Destroy == null) {
      throw IllegalStateException(
          "addUtilDestroy should be called between onCreate and onDestroy")
    }
    mSubscription2Destroy!!.add(subscription)
    return true
  }

  @Synchronized
  fun remove(subscription: Subscription) {
    if (mSubscription2Stop == null && mSubscription2Destroy == null) {
      throw IllegalStateException("remove should not be called after onDestroy")
    }
    if (mSubscription2Stop != null) {
      mSubscription2Stop!!.remove(subscription)
    }
    if (mSubscription2Destroy != null) {
      mSubscription2Destroy!!.remove(subscription)
    }
  }

  @Synchronized
  fun onStop() {
    if (mSubscription2Stop == null) {
      throw IllegalStateException("onStop called multiple times or onStart not called")
    }
    mSubscription2Stop!!.clear()
    mSubscription2Stop = null
  }

  @Synchronized
  fun onDestroy() {
    if (mSubscription2Destroy == null) {
      throw IllegalStateException("onDestroy called multiple times or onCreate not called")
    }
    mSubscription2Destroy!!.clear()
    mSubscription2Destroy = null
  }
}
