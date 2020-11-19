package com.cecilleo.core.wx

enum class WxError {
  /**
   * 未安装微信报错
   */
  NOINSTALL,

  /**
   * 用户取消
   */
  CANCEL,

  /**
   * 授权认证失败
   */
  AUTH_DENIED,

  /**
   * 通用失败
   */
  FAILED,

  /**
   * 微信版本不支持
   */
  UNSUPPORTED
}