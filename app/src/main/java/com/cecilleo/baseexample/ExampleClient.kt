package com.cecilleo.baseexample

import com.cecilleo.lib.net.BaseRetrofitClient
import com.cecilleo.lib.net.NetworkUtil
import com.cecilleo.lib.util.AppUtil
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File

object ExampleClient : BaseRetrofitClient() {
  private val cookieJar by lazy {
    PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(AppUtil.context))
  }

  override fun handleBuilder(builder: OkHttpClient.Builder) {
    val httpCacheDirectory = File(AppUtil.context.cacheDir, "responses")
    val cacheSize = 10 * 1024 * 1024L // 10 MiB
    val cache = Cache(httpCacheDirectory, cacheSize)
    builder.cache(cache)
        .cookieJar(cookieJar)
        .addInterceptor(ExampleInterceptor())
  }
}

class ExampleInterceptor : Interceptor {
  override fun intercept(chain: Chain): Response {
    var request = chain.request()
    if (!NetworkUtil.isNetworkAvailable(AppUtil.context)) {
      request = request.newBuilder()
          .cacheControl(CacheControl.FORCE_CACHE)
          .build()
    }
    val response = chain.proceed(request)
    if (!NetworkUtil.isNetworkAvailable(AppUtil.context)) {
      val maxAge = 60 * 60
      response.newBuilder()
          .removeHeader("Pragma")
          .header("Cache-Control", "public, max-age=$maxAge")
          .build()
    } else {
      val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
      response.newBuilder()
          .removeHeader("Pragma")
          .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
          .build()
    }
    return response
  }
}
