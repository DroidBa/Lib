package com.cecilleo.core.base.util

import android.text.TextUtils
import androidx.annotation.AnimRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cecilleo.core.base.R

class FragmentHelper {
  companion object {
    const val TAG = "FragmentHelper"

    @JvmStatic
    fun addFragWithoutAnim(fragMgr: FragmentManager, frag: Fragment?, containerId: Int,
      fragTag: String) {
      if (frag != null) {
        fragMgr.beginTransaction().add(containerId, frag, fragTag).commitAllowingStateLoss()
      }
    }

    @JvmStatic
    fun replaceFragWithoutAnim(fragMgr: FragmentManager, frag: Fragment?, containerId: Int,
      fragTag: String) {
      if (frag != null) {
        fragMgr.beginTransaction().replace(containerId, frag, fragTag).commitAllowingStateLoss()
      }
    }

    @JvmStatic
    fun findFragWithTag(fragMgr: FragmentManager, fragTag: String): Fragment? {
      return fragMgr.findFragmentByTag(fragTag)
    }

    @JvmStatic
    fun isFragHidden(fragMgr: FragmentManager, fragTag: String): Boolean {
      val frg = findFragWithTag(fragMgr, fragTag)
      return frg?.isHidden ?: false
    }

    @JvmStatic
    fun hideFrag(fragmentManager: FragmentManager?, fragment: Fragment?) {
      if (fragmentManager != null && fragment != null) {
        var fragment = fragment
        try {
          if (fragment.isAdded) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.hide(fragment)
            fragmentTransaction.commitAllowingStateLoss()
          }
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }

    @JvmStatic
    fun visibleFrag(fragmentManager: FragmentManager?, fragment: Fragment?) {
      if (fragmentManager != null && fragment != null) {
        var fragment = fragment
        try {
          if (fragment.isAdded) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.show(fragment)
            fragmentTransaction.commitAllowingStateLoss()
          }
        } catch (e: Exception) {
          e.printStackTrace()
        }
      }
    }

    @JvmStatic
    fun removeWithoutAnim(fragmentManager: FragmentManager?, fragment: Fragment?) {
      if (fragmentManager != null && fragment != null) {
        removeWithCustomAnim(fragmentManager, fragment, null, 0, 0, 0, 0)
      }
    }

    @JvmStatic
    fun fadeIn(fragmentManager: FragmentManager, containerViewId: Int, fragment: Fragment,
      tag: String?) {
      replaceWithCustomAnim(fragmentManager, containerViewId, fragment, tag, R.anim.fade_in, 0, 0,
          0)
    }

    @JvmStatic
    fun fadeOut(fragmentManager: FragmentManager, fragment: Fragment) {
      removeWithCustomAnim(fragmentManager, fragment, null, 0, R.anim.fade_out, 0, 0)
    }

    @JvmStatic
    fun fadeIn(fragmentManager: FragmentManager, containerViewId: Int, fragment: Fragment,
      tag: String?, @AnimRes anim: Int) {
      replaceWithCustomAnim(fragmentManager, containerViewId, fragment, tag, anim, 0, 0,
          0)
    }

    @JvmStatic
    fun fadeOut(fragmentManager: FragmentManager, fragment: Fragment, @AnimRes anim: Int) {
      removeWithCustomAnim(fragmentManager, fragment, null, 0, anim, 0, 0)
    }

    private fun replaceWithCustomAnim(fragmentManager: FragmentManager?, containerViewId: Int,
      fragment: Fragment?, tag: String?, enterAnimResId: Int, exitAnimResId: Int,
      popEnterAnimResId: Int, popExitAnimResId: Int) {
      try {
        if (fragmentManager != null && fragment != null) {
          val fragmentTransaction = fragmentManager.beginTransaction()
          fragmentTransaction.setCustomAnimations(enterAnimResId, exitAnimResId, popEnterAnimResId,
              popExitAnimResId)
          fragmentTransaction.replace(containerViewId, fragment, tag)
          fragmentTransaction.commitAllowingStateLoss()
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }

    }

    private fun removeWithCustomAnim(fragmentManager: FragmentManager?, fragment: Fragment?,
      tag: String?, enterAnimResId: Int, exitAnimResId: Int, popEnterAnimResId: Int,
      popExitAnimResId: Int): Boolean {
      var fragment = fragment
      var isRemove = false
      try {
        if (fragmentManager != null) {
          if (fragment == null && !TextUtils.isEmpty(tag)) {
            fragment = fragmentManager.findFragmentByTag(tag)
          }
          if (fragment != null) {
            if (fragment.isAdded) {
              val fragmentTransaction = fragmentManager.beginTransaction()
              fragmentTransaction.setCustomAnimations(enterAnimResId, exitAnimResId,
                  popEnterAnimResId, popExitAnimResId)
              fragmentTransaction.remove(fragment)
              fragmentTransaction.commitAllowingStateLoss()
              isRemove = true
            } else {
            }
          } else {
          }
        }
      } catch (e: Exception) {
        e.printStackTrace()
      }
      return isRemove
    }
  }
}