package com.cecilleo.baseexample

import android.app.Application
import com.cecilleo.lib.cache.Storage
import com.cecilleo.lib.cache.StoreBuilder
import com.cecilleo.lib.util.AppUtil
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
  override fun onCreate() {
    super.onCreate()
    AppUtil.init(this)
    initStore()
    startKoin {
      androidContext(this@App)
      modules(appModule)
    }
  }

  private fun initStore() {
    StoreBuilder.configure(50 * 1024L * 1024)
        .setCacheDirectory(this, Storage.PREFER_EXTERNAL)
        .setVersion(BuildConfig.VERSION_CODE)
        .initialize()
  }
}