package com.cecilleo.lib.util

import android.view.View
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.actor

@ObsoleteCoroutinesApi
fun View.onClick(action: suspend (View) -> Unit) {
  val eventActor = GlobalScope.actor<View>(Dispatchers.Main) {
    for (event in channel) action(event)
  }
  setOnClickListener { eventActor.offer(it) }
}

fun View.gone() = run { this.visibility = View.GONE }

fun View.invisible() = run { this.visibility = View.INVISIBLE }

fun View.visible() = run { this.visibility = View.VISIBLE }

var View.isVisible: Boolean
  get() = visibility == View.VISIBLE
  set(value) = if (value) visible() else gone()

var View.isInvisible: Boolean
  get() = visibility == View.INVISIBLE
  set(value) = if (value) invisible() else visible()

var View.isGone: Boolean
  get() = visibility == View.GONE
  set(value) = if (value) gone() else visible()
