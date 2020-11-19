package com.cecilleo.core.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelChildren
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

abstract class AbsFragment<VB : ViewBinding> : Fragment(), CoroutineScope by MainScope() {
  var mIsFragmentVisible = false
  private var mNeedDispatch = true
  private var mIsFirstTimeVisible = true
  private var mIsInvisibleBeforeUserLeave = false
  var mRootView: View? = null
  protected var mViewBinding: VB? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    EventBus.getDefault().register(this)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    val type = javaClass.genericSuperclass
    val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<VB>
    val method = clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java,
        Boolean::class.java)
    mViewBinding = method.invoke(null, layoutInflater, container, false) as VB
    return mViewBinding!!.root
  }

  @Subscribe open fun onEvent(evt: String) {}
  protected open fun onFirstTimeVisible() {}
  protected open fun onFragmentVisible() {}
  protected open fun onFragmentInvisible() {}

  override fun onDestroy() {
    coroutineContext.cancelChildren()
    EventBus.getDefault().unregister(this)
    super.onDestroy()
  }

  override fun onStart() {
    super.onStart()
    if (!mIsInvisibleBeforeUserLeave && !isHidden && userVisibleHint) {
      if (parentFragment != null && !parentFragment!!.isHidden
          || parentFragment == null) {
        mNeedDispatch = false
        dispatchFragmentVisibility(true)
      }
    }
  }

  override fun onResume() {
    super.onResume()
    if (!mIsFirstTimeVisible) {
      if (!mIsFragmentVisible && !mIsInvisibleBeforeUserLeave && !isHidden && userVisibleHint) {
        mNeedDispatch = false
        dispatchFragmentVisibility(true)
      }
    }
  }

  override fun onPause() {
    super.onPause()
    if (mIsFragmentVisible && !isHidden && userVisibleHint) {
      mNeedDispatch = false
      mIsInvisibleBeforeUserLeave = false
      dispatchFragmentVisibility(false)
    } else {
      mIsInvisibleBeforeUserLeave = true
    }
  }

  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
    super.setUserVisibleHint(isVisibleToUser)
    if (isResumed) {
      if (!mIsFragmentVisible && isVisibleToUser) {
        dispatchFragmentVisibility(true)
      } else if (mIsFragmentVisible && !isVisibleToUser) {
        dispatchFragmentVisibility(false)
      }
    } else if (isVisibleToUser) {
      mIsInvisibleBeforeUserLeave = false
    }
  }

  override fun onHiddenChanged(hidden: Boolean) {
    super.onHiddenChanged(hidden)
    if (isResumed) {
      dispatchFragmentVisibility(!hidden)
    }
  }

  override fun onDestroyView() {
    mViewBinding = null
    if (mRootView != null && mRootView!!.parent != null) {
      (mRootView!!.parent as ViewGroup).removeView(mRootView)
    }
    super.onDestroyView()
  }

  /**
   * 分发Fragment可见性包括嵌套的Fragment,统一子Fragment可见性
   */
  open fun dispatchFragmentVisibility(visible: Boolean) {
    mIsFragmentVisible = visible
    if (!mNeedDispatch) {
      mNeedDispatch = true
    } else {
      val fragmentManager = childFragmentManager
      if (fragmentManager != null) {
        val childFragments =
          fragmentManager.fragments
        if (childFragments != null) {
          for (childFragment in childFragments) {
            if (childFragment is AbsFragment<*>
                && !childFragment.isHidden()
                && childFragment.getUserVisibleHint()) {
              childFragment.dispatchFragmentVisibility(visible)
            }
          }
        }
      }
    }
    if (visible) {
      if (mIsFirstTimeVisible) {
        mIsFirstTimeVisible = false
        //onFirstTimeVisible()
//      } else {
        onFragmentVisible()
      }
//    } else {
//      onFragmentInvisible()
    }
  }
}