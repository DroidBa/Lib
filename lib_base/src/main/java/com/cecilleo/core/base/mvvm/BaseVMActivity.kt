package com.cecilleo.core.base.mvvm

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.cecilleo.core.base.base.AbsActivity
import com.cecilleo.core.base.model.BaseViewModel

abstract class BaseVMActivity<VB : ViewBinding, VM : BaseViewModel> : AbsActivity<VB>() {

  lateinit var mViewModel: VM

  override fun onCreate(savedInstanceState: Bundle?) {
    mViewModel = initVM()
    startObserve()
    super.onCreate(savedInstanceState)
  }

  abstract fun initVM(): VM
  abstract fun startObserve()
}