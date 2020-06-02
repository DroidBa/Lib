package com.cecilleo.lib.mvp

import android.os.Bundle
import com.cecilleo.lib.base.AbsActivity

abstract class MVPActivity<V : MVPView?, P : MVPPresenter<V>> : AbsActivity(), MVPView {
  lateinit var presenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter = createPresenter()
    lifecycle.addObserver(presenter)
    super.onCreate(savedInstanceState)
  }

  protected abstract fun createPresenter(): P
}