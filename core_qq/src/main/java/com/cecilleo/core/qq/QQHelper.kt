package com.cecilleo.core.qq

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.cecilleo.core.qq.QQError.CANCEL
import com.cecilleo.core.qq.QQError.FAILED
import com.cecilleo.core.qq.QQError.NO_INSTALL
import com.tencent.connect.common.Constants
import com.tencent.connect.share.QQShare
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject
import java.io.File
import java.lang.ref.WeakReference

class QQHelper(private var mContext: Context, private var qqID: String,
  private var qqLoginCB: QQLoginCallBack? = null,
  private var qqShareCB: QQShareCallBack? = null) {
  private var mTencent: Tencent =
    Tencent.createInstance(qqID, mContext, "com.tencent.sportslive.fileprovider")
  private var weakContext: WeakReference<Context> = WeakReference<Context>(mContext)

  companion object {
    fun create(context: Context): QQBuilder {
      return QQBuilder(context)
    }
  }

  fun getAppID(): String = qqID

  /**
   * QQ登录
   */
  fun login(activity: Activity) {
    if (!mTencent.isQQInstalled(weakContext.get())) {
      qqLoginCB?.onFailed(NO_INSTALL)
      return
    }
    mTencent.login(activity, "all", mTencentListener, true)
  }

  /**
   * 分享纯图片到聊天
   */
  fun shareOnlyImgToChat(activity: Activity, localPath: String) {
    val params = Bundle()
    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE)
    params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, localPath)
    val file = File(localPath);
    if (file.length() >= 5 * 1024 * 1024) {
      mTencentListener.onError(UiError(Constants.ERROR_IMAGE_TOO_LARGE,
          Constants.MSG_SHARE_IMAGE_TOO_LARGE_ERROR, null))
      return
    }
    mTencent.shareToQQ(activity, params, mTencentListener)
  }

  /**
   * 分享图文到聊天
   */
  fun shareTxtImgToChat(activity: Activity, jumpURL: String, title: String, summary: String = "",
    imgURL: String = "") {
    val params = Bundle()
    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
    params.putString(QQShare.SHARE_TO_QQ_TITLE, title)
    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, jumpURL)
    if (summary.isNotEmpty()) params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary)
    if (imgURL.isNotEmpty()) params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgURL)
    mTencent.shareToQQ(activity, params, mTencentListener)
  }

  fun isSessionValid() = mTencent.isSessionValid

  private val mTencentListener: IUiListener = object : IUiListener {
    override fun onComplete(response: Any?) {
      qqShareCB?.onSuccess()
      qqLoginCB?.let {
        try {
          if (null == response || (response as JSONObject).length() == 0) {
            it.onFailed(FAILED)
          } else {
            response.get(Constants.PARAM_ACCESS_TOKEN)
            val token: String = response.get(Constants.PARAM_ACCESS_TOKEN) as String
            val expires: String = (response.get(Constants.PARAM_EXPIRES_IN) as Int).toString()
            val openId: String = response.get(Constants.PARAM_OPEN_ID) as String
            if (!TextUtils.isEmpty(token)
                && !TextUtils.isEmpty(expires)
                && !TextUtils.isEmpty(openId)) {
              mTencent.setAccessToken(token, expires)
              mTencent.openId = openId
              it.onSuccess(token, expires, openId)
            }
          }
        } catch (e: Exception) {
          it.onFailed(FAILED)
        }
      }
    }

    override fun onCancel() {
      qqShareCB?.onFailed(CANCEL)
      qqLoginCB?.onFailed(CANCEL)
    }

    override fun onWarning(p0: Int) {
    }

    override fun onError(e: UiError?) {
      qqShareCB?.onFailed(FAILED)
      qqLoginCB?.onFailed(FAILED)
    }
  }

  fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Tencent.onActivityResultData(requestCode, resultCode, data, mTencentListener)
  }

  fun logout(activity: Context) {
    mTencent.logout(activity)
  }
}

class QQBuilder(var context: Context) {
  private var qqLoginCB: QQLoginCallBack? = null
  private var qqShareCB: QQShareCallBack? = null
  private var qqID = ""

  fun setQQID(id: String): QQBuilder {
    qqID = id
    return this
  }

  fun setQQLoginCallback(cb: QQLoginCallBack): QQBuilder {
    qqLoginCB = cb
    return this
  }

  fun setQQShareCallback(cb: QQShareCallBack): QQBuilder {
    qqShareCB = cb
    return this
  }

  fun build(): QQHelper {
    return QQHelper(context, qqID, qqLoginCB, qqShareCB)
  }
}

enum class QQError {
  /**
   * 未安装微信报错
   */
  NO_INSTALL,
  CANCEL,
  FAILED
}

interface QQLoginCallBack {
  fun onSuccess(token: String, expires: String, openId: String)
  fun onFailed(error: QQError)
}

interface QQShareCallBack {
  fun onSuccess()
  fun onFailed(error: QQError)
}

