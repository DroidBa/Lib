package com.cecilleo.lib.mvvm

import android.os.Bundle
import android.view.View
import com.cecilleo.lib.base.AbsFragment
import com.cecilleo.lib.model.BaseViewModel

abstract class BaseVMFragment<VM : BaseViewModel> : AbsFragment() {
  private lateinit var mViewModel: VM

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    mViewModel = initVM()
    initView()
    initData()
    startObserve()
    super.onViewCreated(view, savedInstanceState)
  }

  abstract fun initVM(): VM
  abstract fun initView()
  abstract fun initData()
  abstract fun startObserve()
}