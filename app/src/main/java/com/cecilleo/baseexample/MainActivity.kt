package com.cecilleo.baseexample

import android.util.Log
import android.view.View.OnClickListener
import android.widget.Toast
import com.cecilleo.baseexample.databinding.ActivityExmapleBinding
import com.cecilleo.lib.cache.Store
import com.cecilleo.lib.mvp.MVPActivity
import com.cecilleo.lib.util.Prefs
import com.cecilleo.lib.util.ToastUtils
import java.lang.reflect.Proxy

class MainActivity : MVPActivity<ActivityExmapleBinding, TestView, TestPresenter>(), TestView {

  override fun createPresenter(): TestPresenter {
    return TestPresenter(this)
  }

  override fun initView() {
    viewBinding.result.setOnClickListener {
      ToastUtils.showShortToast("123123123")
    }
    HookSetOnClickListenerHelper.hook(this, viewBinding.result)

    val proxyClass =
      Proxy.newProxyInstance(this.javaClass.classLoader,
          arrayOf<Class<*>>(
              MyInterface::class.java)
      ) { proxy, method, args ->
        Log.d("Leo", ": ")
        Log.d("Leo", ":读取 ${Prefs.get().getInt("123", 0)} ")
        Log.d("Leo", ":读取 ${Store.get("store", String::class.java).execute()} ");
        null//执行被代理的对象的逻辑
      }
    (proxyClass as MyInterface).onClick()
  }

  override fun initData() {
    Prefs.get().save("123", 1)
    Store.put("store", "111").execute()
  }

  override fun showProgress() {
  }

  override fun showEmpty() {
  }

  override fun showError() {
  }

  override fun hideProgress() {
  }
}

interface MyInterface {
  fun onClick()
  fun onBack()
}
