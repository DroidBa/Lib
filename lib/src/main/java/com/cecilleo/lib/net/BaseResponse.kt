package com.cecilleo.lib.net

open class BaseResponse<T>(var errorCode: Int, var errorMsg: String, var data: T) {
  open fun isSuccess(): Boolean {
    return errorCode == 0
  }

  open fun getResult(): T {
    return data
  }

  open fun getError(): String {
    return errorMsg
  }
}