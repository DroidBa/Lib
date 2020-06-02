package com.cecilleo.lib.net

class NBAResponse<T>(var code: Int, data: T) : BaseResponse<T>(
    code, "", data) {
  override fun isSuccess(): Boolean {
    return code == 0
  }

  override fun getResult(): T {
    return data
  }
}