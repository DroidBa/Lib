package com.cecilleo.baseexample

import androidx.lifecycle.Observer
import com.cecilleo.baseexample.databinding.ActivityExmapleBinding
import com.cecilleo.lib.mvvm.BaseVMActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MVVMExampleAc : BaseVMActivity<ActivityExmapleBinding, MVVMExampleVM>() {

  override fun initVM(): MVVMExampleVM = getViewModel()

  override fun startObserve() {
    mViewModel.text.observe(this, Observer {
      viewBinding.result.text = "123"
    })
  }

  override fun initView() {
  }

  override fun initData() {
    mViewModel.getTab()
  }
}