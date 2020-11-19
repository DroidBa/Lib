package com.cecilleo.core.base.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.Bitmap.Config.RGB_565
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

fun getImagePathWithCompress(bm: Bitmap, context: Context, sizeInKB: Double):String? {
  val bitmap = imageZoom(bm, sizeInKB)
 return saveBitmap(bitmap, context)
}

fun saveBitmap(bm: Bitmap, context: Context): String? {
  val f = File(FileUtil.getImageDownloadDir(context),
      System.currentTimeMillis().toString() + ".png")
  if (f.exists()) {
    f.delete()
  }
  try {
    val out = FileOutputStream(f)
    bm.compress(PNG, 80, out)
    out.flush()
    out.close()
  } catch (e: FileNotFoundException) {
    e.printStackTrace()
  } catch (e: IOException) {
    e.printStackTrace()
  }
  return f.path
}

fun imageZoom(bitMap: Bitmap, sizeInKB: Double): Bitmap {
  //图片允许最大空间   单位：KB
  //        double maxSize = 25.00;
  //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
  var bitMap = bitMap
  val baos = ByteArrayOutputStream()
  bitMap.compress(JPEG, 100, baos)
  val b = baos.toByteArray()
  //将字节换成KB
  val mid = b.size / 1024.toDouble()
  //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
  if (mid > sizeInKB) {
    //获取bitmap大小 是允许最大大小的多少倍
    val i = mid / sizeInKB
    //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，
    // 压缩后也达到了最大大小占用空间的大小）
    bitMap = zoomImage(bitMap, bitMap.width / Math.sqrt(i), bitMap.height / Math.sqrt(i))
  }
  return bitMap
}

fun zoomImage(bitmap: Bitmap, newWidth: Double, newHeight: Double): Bitmap {
  // 获取这个图片的宽和高
  val width = bitmap.width.toFloat()
  val height = bitmap.height.toFloat()
  // 创建操作图片用的matrix对象
  val matrix = Matrix()
  // 计算宽高缩放率
  val scaleWidth = newWidth.toFloat() / width
  val scaleHeight = newHeight.toFloat() / height
  // 缩放图片动作
  matrix.postScale(scaleWidth, scaleHeight)
  return Bitmap.createBitmap(bitmap, 0, 0, width.toInt(), height.toInt(), matrix, true)
}

fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
  var i: Int
  var j: Int
  if (bmp.height > bmp.width) {
    i = bmp.width
    j = bmp.width
  } else {
    i = bmp.height
    j = bmp.height
  }
  val localBitmap = Bitmap.createBitmap(i, j, RGB_565)
  val localCanvas = Canvas(localBitmap)
  while (true) {
    localCanvas.drawBitmap(bmp, Rect(0, 0, i, j), Rect(0, 0, i, j), null)
    if (needRecycle) bmp.recycle()
    val localByteArrayOutputStream = ByteArrayOutputStream()
    localBitmap.compress(JPEG, 100, localByteArrayOutputStream)
    localBitmap.recycle()
    val arrayOfByte = localByteArrayOutputStream.toByteArray()
    try {
      localByteArrayOutputStream.close()
      return arrayOfByte
    } catch (e: Exception) {
      // F.out(e);
    }
    i = bmp.height
    j = bmp.height
  }
}