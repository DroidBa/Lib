package com.cecilleo.lib.util

import android.os.Handler
import android.os.Looper

object UiThreadUtil {
  private val mHandler = Handler(Looper.getMainLooper())

  val isMainThread: Boolean
    get() = Looper.myLooper() == Looper.getMainLooper()

  fun runOnUiThread(runnable: Runnable?) {
    if (runnable != null) {
      mHandler.post(runnable)
    }
  }

  fun removeRunnable(runnable: Runnable?) {
    if (runnable != null) {
      mHandler.removeCallbacks(runnable)
    }
  }

  fun postDelay(runnable: Runnable?, delayMillis: Long): Boolean {
    var result = false
    if (runnable != null) {
      result = mHandler.postDelayed(runnable, delayMillis)
    }
    return result
  }

  fun postRunnable(runnable: Runnable?) {
    if (runnable != null) {
      mHandler.post(runnable)
    }
  }
}
