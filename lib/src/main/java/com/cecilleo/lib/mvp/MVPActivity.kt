package com.cecilleo.lib.mvp

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.cecilleo.lib.base.AbsActivity

abstract class MVPActivity<VB : ViewBinding, V : MVPView?, P : MVPPresenter<V>> : AbsActivity<VB>(),
    MVPView {
  lateinit var presenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter = createPresenter()
    lifecycle.addObserver(presenter)
    super.onCreate(savedInstanceState)
  }

  protected abstract fun createPresenter(): P
}