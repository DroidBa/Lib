package com.cecilleo.lib.mvp

interface MVPView {
  fun showProgress()
  fun showEmpty()
  fun showError()
  fun hideProgress()
}