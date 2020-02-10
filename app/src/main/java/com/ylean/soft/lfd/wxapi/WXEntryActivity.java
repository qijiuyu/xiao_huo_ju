package com.ylean.soft.lfd.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.base.Http;
import com.zxdc.utils.library.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI mWeixinAPI;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeixinAPI = WXAPIFactory.createWXAPI(this, Constant.WX_APPID, true);
        mWeixinAPI.handleIntent(this.getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWeixinAPI.handleIntent(intent, this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    //发送成功
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    if (sendResp != null) {
                        String code = sendResp.code;
                        getAccess_token(code);
                    }
                }else{
                    //分享成功
                    EventBus.getDefault().post(new EventBusType(EventStatus.SHARE_SUCCESS));
                    finish();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                finish();
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                //发送被拒绝
                break;
            default:
                finish();
                //发送返回
                break;
        }
    }


    private String openId,access_token;
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final String message=msg.obj.toString();
            switch (msg.what){
                //获取openid accessToken值用于后期操作
                case HandlerConstant.GET_WX_ACCESS_TOKEN_SUCCESS:
                     try {
                          final JSONObject jsonObject = new JSONObject(message);
                          openId = jsonObject.getString("openid").toString().trim();
                          access_token = jsonObject.getString("access_token").toString().trim();
                         //获取用户昵称和头像
                          getUserMsg();
                     } catch (JSONException e) {
                            e.printStackTrace();
                     }
                     break;
                //获取用户信息
                case HandlerConstant.GET_WX_USER_SUCCESS:
                     try {
                         final JSONObject jsonObject = new JSONObject(message);
                         String nickname = jsonObject.getString("nickname");
                         String headimgurl = jsonObject.getString("headimgurl");
                         String unionid=jsonObject.getString("unionid");
                         EventBus.getDefault().post(new EventBusType(EventStatus.WX_LOGIN,headimgurl+","+nickname+","+unionid));
                         finish();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                     break;
                 default:
                     break;
            }
        }
    };


    /**
     * 获取openid accessToken值用于后期操作
     * @param code
     */
    private void getAccess_token(final String code) {
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Constant.WX_APPID+"&secret="+ Constant.WX_APPSECRET+"&code="+code+"&grant_type=authorization_code";
        Http.getMonth(url,mHandler, HandlerConstant.GET_WX_ACCESS_TOKEN_SUCCESS);
    }


    /**
     *  获取微信的个人信息
     */
    private void getUserMsg() {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openId;
        Http.getMonth(url,mHandler, HandlerConstant.GET_WX_USER_SUCCESS);
    }

}
