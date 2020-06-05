package com.cecilleo.baseexample

import com.cecilleo.lib.net.BaseResponse

class NBAResponse<T>(var code: Int, data: T) : BaseResponse<T>(
    code, "", data) {
  override fun isSuccess(): Boolean {
    return code == 0
  }

  override fun getResult(): T {
    return data
  }

  override fun getError(): String {
    return code.toString()
  }
}