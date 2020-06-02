package com.cecilleo.lib.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cecilleo.lib.util.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

abstract class AbsActivity : AppCompatActivity(), CoroutineScope by MainScope() {

  var isVisibleToUser = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ActivityUtil.instance.saveActivity(this)
    setContentView(getLayoutResId())
    initView()
    initData()
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
}