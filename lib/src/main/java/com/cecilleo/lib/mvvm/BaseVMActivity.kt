package com.cecilleo.lib.mvvm

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.cecilleo.lib.base.AbsActivity
import com.cecilleo.lib.model.BaseViewModel

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