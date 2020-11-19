package com.cecilleo.lib.share

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import com.cecilleo.core.base.util.ToastUtils
import com.cecilleo.core.qq.QQError
import com.cecilleo.core.qq.QQHelper
import com.cecilleo.core.qq.QQError.NO_INSTALL
import com.cecilleo.core.qq.QQShareCallBack
import com.cecilleo.core.wx.WXHelper
import com.cecilleo.core.wx.WxError
import com.cecilleo.core.wx.WxError.FAILED
import com.cecilleo.core.wx.WxLoginCallback
import com.cecilleo.lib.share.ShareType.QQ_CHAT_IMG
import com.cecilleo.lib.share.ShareType.QQ_CHAT_TEXT
import com.cecilleo.lib.share.ShareType.WX_CHAT_IMG

class ShareEngine {
  private val qqHelper: QQHelper = createQQHelper()
  private var wxHelper: WXHelper = createWXHelper()

  init {
    val clazzName = this.javaClass.name
    if (registryMap.containsKey(clazzName)) {
      throw Exception(
          "无法为类 $clazzName 构造实例，因为实例已经存在!")
    } else {
      synchronized(registryMap) {
        if (registryMap.containsKey(clazzName)) {
          throw  Exception(
              "无法为类 $clazzName 构造实例，因为实例已经存在!")
        } else {
          registryMap.put(clazzName, this)
        }
      }
    }
  }

  private fun createQQHelper(): QQHelper {
    return QQHelper.create(context).setQQID("101888752")
        .setQQShareCallback(object : QQShareCallBack {
          override fun onSuccess() {
            ToastUtils.showLongToast("分享成功")
          }

          override fun onFailed(error: QQError) {
            if (error == NO_INSTALL) {
              ToastUtils.showLongToast("未安装QQ")
            } else {
              ToastUtils.showLongToast("分享失败")
            }
          }
        }).build()
  }

  private fun createWXHelper(): WXHelper {
    return WXHelper.create(context).setWxAppID("wxd48a21ba496b03c9")
        .setOnLoginWxCallback(object : WxLoginCallback {
          override fun onSuccess(code: String) {
            ToastUtils.showShortToast("分享成功")
          }

          override fun onFailed(error: WxError) {
            if (error == WxError.NOINSTALL) {
              ToastUtils.showLongToast("未安装微信")
            } else {
              ToastUtils.showLongToast("分享失败")
            }
          }
        }).build()
  }

  /**
   * ShareType -> 请参阅各类型说明
   */
  fun share(type: ShareType, activity: Activity, jumpURL: String = "", title: String = "",
    summary: String = "", imgURL: String = "", bitmap: Bitmap? = null) {
    when (type) {
      QQ_CHAT_TEXT -> {
        qqHelper.shareTxtImgToChat(activity, jumpURL, title, summary, imgURL)
      }
      QQ_CHAT_IMG -> {
        qqHelper.shareOnlyImgToChat(activity, imgURL)
      }
      WX_CHAT_IMG -> {
        wxHelper.shareImgToChat(bitmap)
      }
      else -> {

      }
    }
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    qqHelper.onActivityResult(requestCode, resultCode, data)
  }

  companion object {
    private var isInit = false
    private var instance: ShareEngine? = null
    private lateinit var context: Application
    private val registryMap: MutableMap<String, ShareEngine> = HashMap()
    fun getEngine(): ShareEngine {
      if (!isInit) throw Exception("请初始化分享引擎---->didn't call ShareEngine.initEngine()")
      if (null == instance) {
        synchronized(ShareEngine::class.java) {
          if (null == instance) {
            instance = ShareEngine()
          }
        }
      }
      return instance!!
    }

    fun initEngine(app: Application) {
      isInit = true
      context = app
    }
  }
}

enum class ShareType {
  /**必填项【activity,jumpURL,title】*/
  QQ_CHAT_TEXT,

  /**必填项【activity, imgURL】*/
  QQ_CHAT_IMG,

  QQ_ZONE,
  WX_CHAT_IMG,
  WX_FRI
}