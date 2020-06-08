package com.cecilleo.baseexample

import android.util.Log
import androidx.lifecycle.Observer
import com.cecilleo.baseexample.databinding.ActivityExmapleBinding
import com.cecilleo.lib.model.ErrorType.NONET
import com.cecilleo.lib.model.ErrorType.OTHERS
import com.cecilleo.lib.mvvm.BaseVMActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MVVMExampleAc : BaseVMActivity<ActivityExmapleBinding, MVVMExampleVM>() {

  override fun initVM(): MVVMExampleVM = getViewModel()

  override fun startObserve() {
    mViewModel.text.observe(this, Observer {
      if (it.showLoading) {
        Log.d("Leo", "ShowLoading: ");
        return@Observer
      }
      when (it.errorType) {
        null -> {
          viewBinding.result.text = "123"
        }
        OTHERS -> {

        }
        NONET -> {

        }
        else -> {

        }
      }
    })
  }

  override fun initView() {
  }

  override fun initData() {
    mViewModel.getTab()
  }
}