package com.cecilleo.lib.base

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import com.cecilleo.lib.util.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class AbsActivity : AppCompatActivity(), CoroutineScope by MainScope() {

  var isVisibleToUser = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ActivityUtil.instance.saveActivity(this)
    setContentView(getLayoutResId())
    initView()
    initData()
    grayWindow(false)
  }

  abstract fun getLayoutResId(): Int
  abstract fun initView()
  abstract fun initData()

  override fun onWindowFocusChanged(hasFocus: Boolean) {
    super.onWindowFocusChanged(hasFocus)
    isVisibleToUser = hasFocus
  }

  override fun onDestroy() {
    EventBus.getDefault().unregister(this)
    ActivityUtil.instance.removeActivity(this)
    super.onDestroy()
    cancel()
  }
  private fun grayWindow(boolean: Boolean) {
    if (boolean) {
      val decorView = window.decorView
      val paint = Paint()
      val cm = ColorMatrix()
      cm.setSaturation(0f)
      paint.colorFilter = ColorMatrixColorFilter(cm)
      decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }
  }


}