package com.cecilleo.core.wx

data class WxLogin(
  var code: String,
  var errCode: Int,
  var errStr: String
)

data class WxShare(
  var code: String,
  var errCode: Int,
  var errStr: String
)