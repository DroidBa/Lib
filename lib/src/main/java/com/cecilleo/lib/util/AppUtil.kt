package com.cecilleo.lib.util

import android.content.ContentResolver
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import kotlin.properties.Delegates

class AppUtil {
  companion object {
    private var context: Context by Delegates.notNull()

    fun init(ctx: Context) {
      context = ctx
    }

    fun getAppContext(): Context {
      return context
    }

    fun getRes(): Resources {
      return getAppContext().resources

    }

    fun getContentResolver(): ContentResolver {
      return getAppContext().contentResolver
    }

    fun getDimensionPixelSize(dimenResId: Int): Int {
      return getAppContext().resources.getDimensionPixelSize(dimenResId)
    }

    fun getStringFromRes(strResId: Int): String {
      return getAppContext().resources.getString(strResId)
    }

    fun getString(@StringRes strResId: Int, vararg formatArgs: Any): String {
      return getAppContext().resources.getString(strResId, *formatArgs)
    }

    fun getColorFromRes(@ColorRes colorResId: Int): Int {
      return ContextCompat.getColor(getAppContext(), colorResId)
    }

    fun getColorStateListFromRes(@ColorRes colorResId: Int): ColorStateList? {
      return ContextCompat.getColorStateList(getAppContext(), colorResId)
    }

    fun getSysService(name: String): Any? {
      return getAppContext().getSystemService(name)
    }

    fun getDrawableFromRes(@DrawableRes drawableRes: Int): Drawable? {
      return ContextCompat.getDrawable(getAppContext(), drawableRes)
    }

    fun getInteger(@IntegerRes integerRes: Int): Int {
      return getAppContext().resources.getInteger(integerRes)
    }

    fun getScreenWidth(): Int {
      val wm = getAppContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
      val outMetrics = DisplayMetrics()
      wm.defaultDisplay.getMetrics(outMetrics)
      return outMetrics.widthPixels
    }
  }
}