package com.cecilleo.core.base.util

import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.Signature
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun getSignMd5Str(packageManager: PackageManager, packageName: String): String? {
  try {
    val packageInfo = if (VERSION.SDK_INT >= VERSION_CODES.P) {
      packageManager.getPackageInfo(
          packageName, PackageManager.GET_SIGNING_CERTIFICATES)
    } else {
      packageManager.getPackageInfo(
          packageName, PackageManager.GET_SIGNATURES)
    }

    val sign: Signature
    sign = if (VERSION.SDK_INT >= VERSION_CODES.P) {
      val signstwo = packageInfo.signingInfo.apkContentsSigners
      signstwo[0]
    } else {
      val signs: Array<Signature> = packageInfo.signatures
      signs[0]
    }
    return encryptionMD5(sign.toByteArray())
  } catch (e: NameNotFoundException) {
    e.printStackTrace()
  }
  return ""
}

fun encryptionMD5(byteStr: ByteArray): String? {
  var messageDigest: MessageDigest
  val md5StrBuff = StringBuffer()
  try {
    messageDigest = MessageDigest.getInstance("MD5")
    messageDigest.reset()
    messageDigest.update(byteStr)
    val byteArray: ByteArray = messageDigest.digest()
    for (i in byteArray.indices) {
      if (Integer.toHexString(0xFF and byteArray[i].toInt()).length == 1) {
        md5StrBuff.append("0").append(Integer.toHexString(0xFF and byteArray[i]
            .toInt()))
      } else {
        md5StrBuff.append(Integer.toHexString(0xFF and byteArray[i].toInt()))
      }
    }
  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  }
  return md5StrBuff.toString()
}