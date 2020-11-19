package com.cecilleo.core.wx.wxapi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cecilleo.core.wx.WXID
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth.Resp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.cecilleo.core.wx.WxLogin
import com.cecilleo.core.wx.WxShare
import org.greenrobot.eventbus.EventBus

class WXEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
  private lateinit var wxAPI: IWXAPI

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    try {
      wxAPI = WXAPIFactory.createWXAPI(this, WXID, false)
      wxAPI.handleIntent(intent, this)
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    try {
      setIntent(intent)
      wxAPI.handleIntent(intent, this)
    } catch (e: java.lang.Exception) {
      e.printStackTrace()
    }
  }

  override fun onResp(baseResp: BaseResp?) {
    if (baseResp == null) {
      finish()
      return
    }
    if (baseResp.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) { //分享
      val weiXinShare =
        WxShare(
            baseResp.errCode.toString(),
            baseResp.errCode, if (baseResp.errStr == null) "登录已取消" else baseResp.errStr)
      EventBus.getDefault().post(weiXinShare)
    } else if (baseResp.type == ConstantsAPI.COMMAND_SENDAUTH) { //登录
      val weiXin = WxLogin(
          if ((baseResp as Resp).code == null) "" else (baseResp as Resp).code,
          baseResp.errCode, if (baseResp.errStr == null) "登录已取消" else baseResp.errStr)
      EventBus.getDefault().post(weiXin)
    }
    finish()
  }

  override fun onReq(req: BaseReq?) {

  }
}