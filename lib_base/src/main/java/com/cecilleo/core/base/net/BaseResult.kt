package com.cecilleo.core.base.net

sealed class BaseResult<out T : Any?> {

  data class Success<out T : Any?>(val data: T) : BaseResult<T>()
  data class Error(val exception: BaseException) : BaseResult<Nothing>()

  override fun toString(): String {
    return when (this) {
      is Success<*> -> "Success[data=$data]"
      is Error -> "Error[exception=$exception]"
    }
  }
}