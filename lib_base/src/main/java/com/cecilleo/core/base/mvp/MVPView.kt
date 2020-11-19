package com.cecilleo.core.base.mvp

interface MVPView {
  fun showProgress()
  fun showEmpty()
  fun showError()
  fun hideProgress()
}