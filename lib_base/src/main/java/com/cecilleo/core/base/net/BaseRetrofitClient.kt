package com.cecilleo.core.base.net

import com.cecilleo.core.base.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.TimeUnit

abstract class BaseRetrofitClient @Throws() constructor() {

  companion object {
    const val TIME_OUT = 50
    val registryMap: MutableMap<String, BaseRetrofitClient> = HashMap()

    @Throws(InstantiationException::class,
        IllegalAccessException::class)
    open fun <T : BaseRetrofitClient> getInstance(clazz: Class<T>): T {
      val clazzName = clazz.name
      if (!registryMap.containsKey(clazzName)) {
        synchronized(registryMap) {
          if (!registryMap.containsKey(clazzName)) {
            return clazz.newInstance()
          }
        }
      }
      return registryMap[clazzName] as T
    }

    @Throws(ClassNotFoundException::class, InstantiationException::class,
        IllegalAccessException::class)
    open fun getInstance(clazzName: String): BaseRetrofitClient {
      if (!registryMap.containsKey(clazzName)) {
        val clazz: Class<out BaseRetrofitClient> = Class.forName(clazzName).asSubclass(
            BaseRetrofitClient::class.java)
        synchronized(registryMap) {
          if (!registryMap.containsKey(clazzName)) {
            return clazz.newInstance()
          }
        }
      }
      return registryMap[clazzName] as BaseRetrofitClient
    }

    @Throws(SecurityException::class, NoSuchMethodException::class, IllegalArgumentException::class,
        InvocationTargetException::class, InstantiationException::class,
        IllegalAccessException::class)
    open fun <T : BaseRetrofitClient> getInstance(clazz: Class<T>, parameterTypes: Array<Class<*>?>,
      initArgs: Array<Any?>?): T {
      val clazzName = clazz.name
      if (!registryMap.containsKey(clazzName)) {
        synchronized(registryMap) {
          if (!registryMap.containsKey(clazzName)) {
            val constructor: Constructor<T> = clazz.getConstructor(*parameterTypes)
            return constructor.newInstance(initArgs)
          }
        }
      }
      return registryMap[clazzName] as T
    }
  }

  val client: OkHttpClient by lazy {
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
    return@lazy builder.build()
  }

  val socketClient: OkHttpClient by lazy {
    val builder = OkHttpClient.Builder()
    val logging = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG) {
      logging.level = HttpLoggingInterceptor.Level.BODY
    } else {
      logging.level = HttpLoggingInterceptor.Level.BASIC
    }
    builder.addInterceptor(logging)
        .connectTimeout(3.toLong(), TimeUnit.SECONDS)
    handleBuilder(builder)
    return@lazy builder.build()
  }

  private var retrofit: Retrofit? = null

  protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

  protected abstract fun getBaseUrl(): String

  fun <S> getService(serviceClass: Class<S>): S {
    if (retrofit == null) {
      retrofit = Retrofit.Builder()
          .client(client)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
          .baseUrl(getBaseUrl())
          .build()
    }
    return retrofit!!.create(serviceClass)
  }

  internal class SingletonException(message: String) : java.lang.Exception(
      message) {
    companion object {
      private const val serialVersionUID = -8633183690442262445L
    }
  }

  init {
    val clazzName = this.javaClass.name
    if (registryMap.containsKey(clazzName)) {
      throw SingletonException(
          "Cannot construct instance for class $clazzName, since an instance already exists!")
    } else {
      synchronized(registryMap) {
        if (registryMap.containsKey(clazzName)) {
          throw  SingletonException(
              "Cannot construct instance for class $clazzName, since an instance already exists!")
        } else {
          registryMap.put(clazzName, this)
        }
      }
    }
  }

}
