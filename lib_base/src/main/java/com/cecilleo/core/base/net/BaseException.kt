package com.cecilleo.core.base.net

class BaseException(errMsg: String, var errorCode: Int) : Exception(errMsg)