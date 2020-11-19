package com.cecilleo.core.base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.cecilleo.core.base.cache.Storage
import com.cecilleo.core.base.cache.StoreBuilder
import com.cecilleo.core.base.util.AppUtil
import com.cecilleo.core.base.util.getSignMd5Str
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

abstract class BaseApplication : Application() {
  companion object {
    var instance: BaseApplication? = null
  }

  override fun attachBaseContext(base: Context?) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun onCreate() {
    super.onCreate()
    if (instance == null) {
      instance = this
      if (BuildConfig.DEBUG) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
        ARouter.openLog() // 打印日志
        ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
      }
      ARouter.init(this) // 尽可能早，推荐在Application中初始化

      AppUtil.init(this)
      initStore()
      startKoin {
        androidContext(this@BaseApplication)
        modules(getModules())
      }
      initOtherLib()
      Log.d("Leo", "This App MD5---->${getSignMd5Str(packageManager, packageName)} ")
    }
  }

  abstract fun initOtherLib()

  abstract fun getModules(): List<Module>

  private fun initStore() {
    StoreBuilder.configure(50 * 1024L * 1024)
        .setCacheDirectory(this, Storage.PREFER_EXTERNAL)
        .setVersion(BuildConfig.VERSION_CODE)
        .initialize()
  }
}