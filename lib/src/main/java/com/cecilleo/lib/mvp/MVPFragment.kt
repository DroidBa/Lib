package com.cecilleo.lib.mvp

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.cecilleo.lib.base.AbsFragment

abstract class MVPFragment<VB : ViewBinding, V : MVPView?, P : MVPPresenter<V>> : AbsFragment<VB>(),
    MVPView {
  lateinit var presenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter = createPresenter()
    lifecycle.addObserver(presenter)
    super.onCreate(savedInstanceState)
  }

  abstract fun createPresenter(): P
}