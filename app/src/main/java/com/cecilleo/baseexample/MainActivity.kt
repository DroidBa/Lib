package com.cecilleo.baseexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cecilleo.lib.mvp.MVPActivity
import com.cecilleo.lib.mvp.MVPView

class MainActivity : MVPActivity<TestView, TestPresenter>(), TestView {

  override fun createPresenter(): TestPresenter {
    return TestPresenter(this)
  }

  override fun getLayoutResId(): Int = R.layout.main_activity

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
