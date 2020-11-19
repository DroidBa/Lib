package com.cecilleo.core.base.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.cecilleo.core.base.util.view.LogView

fun Activity.setLog(log: String = "") {
  if (log.isEmpty()) {
    return
  }
  val decView = this.window.decorView.findViewById<ViewGroup>(android.R.id.content)
  val view = getLogTextView(this, log)
  if (view.isShown) return
  decView.addView(view)
}

fun getLogTextView(context: Context, logTip: String): View {
  return LogView(context, tips = logTip)
}