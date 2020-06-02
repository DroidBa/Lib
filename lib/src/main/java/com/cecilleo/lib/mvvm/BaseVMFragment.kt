package com.cecilleo.lib.mvvm

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.cecilleo.lib.base.AbsFragment
import com.cecilleo.lib.model.BaseViewModel

abstract class BaseVMFragment<VB : ViewBinding, VM : BaseViewModel> : AbsFragment<VB>() {
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