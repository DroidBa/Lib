package com.cecilleo.baseexample

import android.app.Application
import com.cecilleo.core.base.cache.Storage
import com.cecilleo.core.base.cache.StoreBuilder
import com.cecilleo.core.base.util.AppUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    AppUtil.init(this)
    initStore()
    startKoin {
      androidContext(this@App)
      modules(emptyList())
    }
  }

  private fun initStore() {
    StoreBuilder.configure(50 * 1024L * 1024)
        .setCacheDirectory(this, Storage.PREFER_EXTERNAL)
        .setVersion(BuildConfig.VERSION_CODE)
        .initialize()
  }
}