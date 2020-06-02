package com.cecilleo.baseexample

import androidx.lifecycle.Observer
import com.cecilleo.lib.mvvm.BaseVMActivity
import kotlinx.android.synthetic.main.activity_exmaple.result
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MVVMExampleAc : BaseVMActivity<MVVMExampleVM>() {

  override fun initVM(): MVVMExampleVM = getViewModel()

  override fun startObserve() {
    mViewModel.text.observe(this, Observer {
      result.text = it.showError
    })
  }

  override fun getLayoutResId(): Int {
    return R.layout.activity_exmaple
  }

  override fun initView() {
  }

  override fun initData() {
    mViewModel.getTab()
  }
}