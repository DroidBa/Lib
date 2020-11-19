package com.cecilleo.core.base.util

import android.app.Activity
import java.util.Stack

class ActivityUtil {
  private val activityStack: Stack<String> = Stack()
  private val mActivities: Stack<Activity> = Stack()
  fun saveActivity(activity: Activity?) {
    if (activity != null) {
      activityStack.add(activity.javaClass.simpleName)
      mActivities.add(activity)
    }
  }

  fun removeActivity(activity: Activity?) {
    if (activity != null) {
      activityStack.remove(activity.javaClass.simpleName)
      mActivities.remove(activity)
    }
  }

  fun hasActivity(activity: Activity): Boolean {
    return activityStack.contains(activity.javaClass.simpleName)
  }

  fun canProcessFav(activity: Activity): Boolean {
    return if (mActivities.size < 1) true else activity === mActivities[1]
  }

  fun hasHomeActivity(homeSimleName: String): Boolean {
    return activityStack.contains(homeSimleName)
  }

  val isOnlyOne: Boolean
    get() = activityStack.size == 1

  fun closeApp() {
    if (mActivities.size < 1) return
    for (activity in mActivities) {
      activity.finish()
    }
  }

  private object Holder {
    val INSTANCE = ActivityUtil()
  }

  companion object {
    @JvmStatic
    val instance: ActivityUtil by lazy { Holder.INSTANCE }
  }
}