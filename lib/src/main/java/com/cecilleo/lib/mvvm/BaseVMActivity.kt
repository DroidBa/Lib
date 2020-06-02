package com.cecilleo.lib.mvvm

import android.os.Bundle
import com.cecilleo.lib.base.AbsActivity
import com.cecilleo.lib.model.BaseViewModel

abstract class BaseVMActivity<VM : BaseViewModel> : AbsActivity() {

  lateinit var mViewModel: VM

  override fun onCreate(savedInstanceState: Bundle?) {
    mViewModel = initVM()
    startObserve()
    super.onCreate(savedInstanceState)
  }

  abstract fun initVM(): VM
  abstract fun startObserve()
}