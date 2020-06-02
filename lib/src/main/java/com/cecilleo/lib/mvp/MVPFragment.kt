package com.cecilleo.lib.mvp

import android.os.Bundle
import com.cecilleo.lib.base.AbsFragment

abstract class MVPFragment<V : MVPView?, P : MVPPresenter<V>> : AbsFragment(), MVPView {
  lateinit var presenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter = createPresenter()
    lifecycle.addObserver(presenter)
    super.onCreate(savedInstanceState)
  }

  abstract fun createPresenter(): P
}