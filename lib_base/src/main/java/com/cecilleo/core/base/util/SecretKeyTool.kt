package com.cecilleo.core.base.util

import android.text.TextUtils
import android.util.Base64
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * 将图片转换成Base64编码的字符串
 */
fun fileToBase64(path: String?): String? {
  if (TextUtils.isEmpty(path)) {
    return null
  }
  var `is`: InputStream? = null
  var data: ByteArray? = null
  var result: String? = null
  try {
    `is` = FileInputStream(path)
    //创建一个字符流大小的数组。
    data = ByteArray(`is`.available())
    //写入数组
    `is`.read(data)
    //用默认的编码格式进行编码
    result = Base64.encodeToString(data, Base64.DEFAULT)
  } catch (e: Exception) {
    e.printStackTrace()
  } finally {
    if (null != `is`) {
      try {
        `is`.close()
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }
  return result
}

fun base64ToByteArray(str: String): ByteArray? {
  return Base64.decode(str, Base64.DEFAULT)
}