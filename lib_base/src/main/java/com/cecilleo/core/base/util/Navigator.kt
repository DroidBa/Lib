package com.cecilleo.core.base.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

/**
 * <P>Created by Vincent on 2018/2/1.</P>
 */

inline fun <reified T : Activity> Activity.gotoActivity(vararg params: Pair<String, Any?>) {
  var intent = createIntent(this, T::class.java, params)
  this.startActivity(intent)
}

inline fun <reified T : Activity> Activity.gotoActivityWithResult(requestCode: Int, vararg params: Pair<String, Any?>) {
  var intent = createIntent(this, T::class.java, params)
  this.startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Activity.gotoActivityWithFlag(
  vararg params: Pair<String, Any?>,
  flag: Int
) {
  var intent = createIntent(this, T::class.java, params)
  intent.addFlags(flag)
  this.startActivity(intent)
}

fun <T> createIntent(
  ctx: Context, clazz: Class<out T>, params: Array<out Pair<String, Any?>>
): Intent {
  val intent = Intent(ctx, clazz)
  if (params.isNotEmpty()) fillIntentArguments(intent, params)
  return intent
}

private fun fillIntentArguments(intent: Intent, params: Array<out Pair<String, Any?>>) {
  params.forEach {
    val value = it.second
    when (value) {
      null -> intent.putExtra(it.first, null as Serializable?)
      is Int -> intent.putExtra(it.first, value)
      is Long -> intent.putExtra(it.first, value)
      is CharSequence -> intent.putExtra(it.first, value)
      is String -> intent.putExtra(it.first, value)
      is Float -> intent.putExtra(it.first, value)
      is Double -> intent.putExtra(it.first, value)
      is Char -> intent.putExtra(it.first, value)
      is Short -> intent.putExtra(it.first, value)
      is Boolean -> intent.putExtra(it.first, value)
      is Serializable -> intent.putExtra(it.first, value)
      is Bundle -> intent.putExtra(it.first, value)
      is Parcelable -> intent.putExtra(it.first, value)
      is Array<*> -> when {
        value.isArrayOf<CharSequence>() -> intent.putExtra(it.first, value)
        value.isArrayOf<String>() -> intent.putExtra(it.first, value)
        value.isArrayOf<Parcelable>() -> intent.putExtra(it.first, value)
        else -> throw Exception(
            "Intent extra ${it.first} has wrong type ${value.javaClass.name}"
        )
      }
      is IntArray -> intent.putExtra(it.first, value)
      is LongArray -> intent.putExtra(it.first, value)
      is FloatArray -> intent.putExtra(it.first, value)
      is DoubleArray -> intent.putExtra(it.first, value)
      is CharArray -> intent.putExtra(it.first, value)
      is ShortArray -> intent.putExtra(it.first, value)
      is BooleanArray -> intent.putExtra(it.first, value)
      else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
    }
    return@forEach
  }
}