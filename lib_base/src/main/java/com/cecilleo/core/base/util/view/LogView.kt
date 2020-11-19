package com.cecilleo.core.base.util.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.cecilleo.core.base.R

class LogView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, var tips: String = "") :
    FrameLayout(
        context, attrs, defStyleAttr) {
  init {
    inflate(context, R.layout.layout_log, this)
    if (tips.isNotEmpty()) {
      findViewById<TextView>(R.id.loadingText).text = tips
    }
  }
}