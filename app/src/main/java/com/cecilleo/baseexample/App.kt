package com.cecilleo.baseexample

import android.app.Application
import com.cecilleo.core.base.BaseApplication
import com.cecilleo.core.base.cache.Storage
import com.cecilleo.core.base.cache.StoreBuilder
import com.cecilleo.core.base.util.AppUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class App : BaseApplication() {
  override fun initOtherLib() {
    AppUtil.init(this)
  }

  override fun getModules(): List<Module> {
    return emptyList()
  }
}