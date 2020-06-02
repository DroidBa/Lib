package com.cecilleo.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.cecilleo.lib.util.ActivityUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

abstract class AbsActivity<VB : ViewBinding> : AppCompatActivity(), CoroutineScope by MainScope() {

  var isVisibleToUser = false
  lateinit var viewBinding: VB

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    EventBus.getDefault().register(this)
    ActivityUtil.instance.saveActivity(this)
    val type = javaClass.genericSuperclass
    if (type is ParameterizedType) {
      val clazz = type.actualTypeArguments[0] as Class<VB>
      val method = clazz.getMethod("inflate", LayoutInflater::class.java)
      viewBinding = method.invoke(null, layoutInflater) as VB
      setContentView(viewBinding.root)
    }
    initView()
    initData()
  }

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

  @Subscribe open fun onEvent(evt: String) {}
}