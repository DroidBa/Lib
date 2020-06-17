package com.cecilleo.lib.util

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

class DensityUtil private constructor() {
  companion object {
    @JvmStatic
    fun px2sp(context: Context, pxValue: Float): Float {
      return pxValue / context.resources.displayMetrics.scaledDensity
    }

    @JvmStatic
    fun sp2px(context: Context, spValue: Int): Int {
      return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue.toFloat(),
          context.resources.displayMetrics).toInt()
    }

    @JvmStatic
    fun dp2px(context: Context, dipValue: Int): Int {
      val scale = context.resources.displayMetrics.density
      return (dipValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun dp2px(dipValue: Int): Int {
      val scale =
        Resources.getSystem().displayMetrics.density
      return (dipValue * scale + 0.5f).toInt()
    }

    @JvmStatic
    fun px2dp(context: Context, pxValue: Float): Int {
      val scale = context.resources.displayMetrics.density
      return (pxValue / scale + 0.5f).toInt()
    }
  }

  init {
    throw UnsupportedOperationException("DensityUtil cannot be instantiated")
  }
}