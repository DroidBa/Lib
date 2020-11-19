package com.cecilleo.core.base.mvvm

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.cecilleo.core.base.base.AbsFragment
import com.cecilleo.core.base.model.BaseViewModel

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : AbsFragment<VB>() {
  lateinit var mViewModel: VM

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    mViewModel = initVM()
    initView()
    startData()
    startObserve()
    super.onViewCreated(view, savedInstanceState)
  }

  abstract fun initVM(): VM
  abstract fun initView()
  abstract fun startData()
  abstract fun startObserve()
}