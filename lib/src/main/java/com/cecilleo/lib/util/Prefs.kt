package com.cecilleo.lib.util

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Build.VERSION_CODES
import android.preference.PreferenceManager

class Prefs {
  internal constructor(context: Context?) {
    preferences = PreferenceManager.getDefaultSharedPreferences(context)
    editor = preferences.edit()
  }

  internal constructor(context: Context, name: String?, mode: Int) {
    preferences = context.getSharedPreferences(name, mode)
    editor = preferences.edit()
  }

  fun save(key: String?, value: Boolean) = editor.putBoolean(key, value).apply()

  fun clear() = editor.clear().apply()

  fun save(key: String?, value: String?) = editor.putString(key, value).apply()

  fun save(key: String?, value: Int) = editor.putInt(key, value).apply()

  fun save(key: String?, value: Float) = editor.putFloat(key, value).apply()

  fun save(key: String?, value: Long) = editor.putLong(key, value).apply()

  @TargetApi(VERSION_CODES.HONEYCOMB)
  fun save(key: String?, value: Set<String?>?) = editor.putStringSet(key, value).apply()

  fun getBoolean(key: String?, defValue: Boolean): Boolean = preferences.getBoolean(key, defValue)

  fun getString(key: String?, defValue: String?): String? = preferences.getString(key, defValue)

  fun getInt(key: String?, defValue: Int): Int = preferences.getInt(key, defValue)

  fun getFloat(key: String?, defValue: Float): Float = preferences.getFloat(key, defValue)

  fun getLong(key: String?, defValue: Long): Long = preferences.getLong(key, defValue)

  @TargetApi(
      VERSION_CODES.HONEYCOMB)
  fun getStringSet(key: String?, defValue: Set<String?>?): Set<String> =
    preferences.getStringSet(key, defValue)

  val all: Map<String, *>
    get() = preferences.all

  fun remove(key: String?) = editor.remove(key).apply()

  operator fun contains(key: String?): Boolean = preferences.contains(key)

  private class Builder(context: Context?, name: String?, mode: Int) {
    private val context: Context
    private val mode: Int
    private val name: String?
    fun build(): Prefs {
      return if (mode == -1 || name == null) Prefs(context) else Prefs(context, name, mode)
    }

    init {
      requireNotNull(context) { "Context must not be null." }
      this.context = context.applicationContext
      this.name = name
      this.mode = mode
    }
  }

  companion object {
    private var isInit = false
    private lateinit var singleton: Prefs
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: Editor
    fun get(context: Context? = null, name: String? = null, mode: Int = -1): Prefs {
      if (!isInit) {
        singleton = Builder(context ?: AppUtil.getAppContext(), name, mode).build()
        isInit = true
      }
      return singleton
    }
  }
}