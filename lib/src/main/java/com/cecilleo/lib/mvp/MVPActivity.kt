package com.cecilleo.lib.mvp

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.cecilleo.lib.base.AbsActivity

abstract class MVPActivity<V : MVPView?, P : MVPPresenter<V>> : AbsActivity(), MVPView {
  lateinit var presenter: P

  override fun onCreate(savedInstanceState: Bundle?) {
    presenter = createPresenter()
//    lifecycle.addObserver(presenter)

    super.onCreate(savedInstanceState)
  }

  protected abstract fun createPresenter(): P
}