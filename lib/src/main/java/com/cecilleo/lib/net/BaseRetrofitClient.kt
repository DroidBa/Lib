package com.cecilleo.lib.net

import com.cecilleo.lib.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient {
  companion object {
    private const val TIME_OUT = 10
  }

  private val client: OkHttpClient
    get() {
      val builder = OkHttpClient.Builder()
      val logging = HttpLoggingInterceptor()
      if (BuildConfig.DEBUG) {
        logging.level = HttpLoggingInterceptor.Level.BODY
      } else {
        logging.level = HttpLoggingInterceptor.Level.BASIC
      }
      builder.addInterceptor(logging)
          .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
      handleBuilder(builder)
      return builder.build()
    }

  protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

  fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
    return Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .baseUrl(baseUrl)
        .build().create(serviceClass)
  }
}
