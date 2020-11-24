package com.cecilleo.core.wx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import com.cecilleo.core.base.util.AppUtil
import com.cecilleo.core.base.util.ToastUtils
import com.cecilleo.core.base.util.bmpToByteArray
import com.cecilleo.core.base.util.imageZoom
import com.cecilleo.core.wx.WxError.AUTH_DENIED
import com.cecilleo.core.wx.WxError.CANCEL
import com.cecilleo.core.wx.WxError.FAILED
import com.cecilleo.core.wx.WxError.NOINSTALL
import com.cecilleo.core.wx.WxError.UNSUPPORTED
import com.tencent.mm.opensdk.modelbase.BaseResp.ErrCode
import com.tencent.mm.opensdk.modelmsg.SendAuth.Req
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXImageObject
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode.MAIN
import java.io.ByteArrayOutputStream
import java.lang.ref.WeakReference

var WXID = ""

class WXHelper(private val mBuilder: WXBuilder) {
  private var wxAPI: IWXAPI
  private var weakContext: WeakReference<Context> = WeakReference<Context>(mBuilder.context)

  companion object {
    fun create(context: Context = AppUtil.getAppContext()): WXBuilder {
      return WXBuilder(context)
    }
  }

  init {
    wxAPI = WXAPIFactory.createWXAPI(weakContext.get(), mBuilder.wxID, true)
    wxAPI.registerApp(mBuilder.wxID)
    WXID = mBuilder.wxID
  }

  fun login() {
    if (checkNoInstall()) return
    registerEvt()
    val req = Req()
    req.scope = "snsapi_userinfo"
    req.state = System.currentTimeMillis().toString()
    wxAPI.sendReq(req)
  }

  @Subscribe(threadMode = MAIN)
  fun onEventWxBean(wxLogin: WxLogin) {
    when (wxLogin.errCode) {
      ErrCode.ERR_OK -> mBuilder.wxCB?.onSuccess(wxLogin.code)
      ErrCode.ERR_USER_CANCEL -> mBuilder.wxCB?.onFailed(CANCEL)
      ErrCode.ERR_AUTH_DENIED -> mBuilder.wxCB?.onFailed(AUTH_DENIED)
      ErrCode.ERR_SENT_FAILED -> mBuilder.wxCB?.onFailed(FAILED)
      ErrCode.ERR_UNSUPPORT -> mBuilder.wxCB?.onFailed(UNSUPPORTED)
      else -> mBuilder.wxCB?.onFailed(FAILED)
    }
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this)
    }
  }

  @Subscribe(threadMode = MAIN)
  fun onEventWxShare(wxShare: WxShare) {
    when (wxShare.errCode) {
      ErrCode.ERR_OK -> mBuilder.wxCB?.onSuccess(wxShare.code)
      ErrCode.ERR_USER_CANCEL -> mBuilder.wxCB?.onFailed(CANCEL)
      ErrCode.ERR_AUTH_DENIED -> mBuilder.wxCB?.onFailed(AUTH_DENIED)
      ErrCode.ERR_SENT_FAILED -> mBuilder.wxCB?.onFailed(FAILED)
      ErrCode.ERR_UNSUPPORT -> mBuilder.wxCB?.onFailed(UNSUPPORTED)
      else -> mBuilder.wxCB?.onFailed(FAILED)
    }
    if (EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().unregister(this)
    }
  }

  fun shareImgToChat(bitmap: Bitmap?, context: Application) {
    if (checkNoInstall()) return
    registerEvt()
    bitmap?.let {
      val imgObj = WXImageObject(bitmap)
      val msg = WXMediaMessage()
      msg.mediaObject = imgObj
      val thumbBitmap: Bitmap = imageZoom(bitmap, 25.00)
      msg.thumbData = bmpToByteArray(thumbBitmap, true)
      val req = SendMessageToWX.Req()
      req.transaction = System.currentTimeMillis().toString()
      req.message = msg
      req.scene = SendMessageToWX.Req.WXSceneSession
      wxAPI.sendReq(req)
    } ?: kotlin.run { ToastUtils.showShortToast("请注意分享图片为空") }
  }

  private fun checkNoInstall(): Boolean {
    if (!wxAPI.isWXAppInstalled) {
      mBuilder.wxCB?.onFailed(NOINSTALL)
      return true
    }
    return false
  }

  private fun registerEvt() {
    if (!EventBus.getDefault().isRegistered(this)) {
      EventBus.getDefault().register(this)
    }
  }
}

class WXBuilder(var context: Context) {
  var wxID: String = ""
  var wxCB: WxLoginCallback? = null

  fun setWxAppID(id: String): WXBuilder {
    wxID = id
    WXID = id
    return this
  }

  fun setOnLoginWxCallback(cb: WxLoginCallback): WXBuilder {
    wxCB = cb
    return this
  }

  fun build(): WXHelper {
    return WXHelper(this)
  }
}

interface WxLoginCallback {
  fun onSuccess(code: String)
  fun onFailed(error: WxError)
}
