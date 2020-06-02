package com.cecilleo.baseexample

import com.cecilleo.baseexample.databinding.ActivityExmapleBinding
import com.cecilleo.lib.mvp.MVPActivity

class MainActivity : MVPActivity<ActivityExmapleBinding, TestView, TestPresenter>(), TestView {

  override fun createPresenter(): TestPresenter {
    return TestPresenter(this)
  }

  override fun initView() {
  }

  override fun initData() {
  }

  override fun showProgress() {
  }

  override fun showEmpty() {
  }

  override fun showError() {
  }

  override fun hideProgress() {
  }
}
