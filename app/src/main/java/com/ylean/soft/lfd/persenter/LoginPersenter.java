package com.ylean.soft.lfd.persenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2020/2/5.
 */
public class LoginPersenter {

    private LoginActivity activity;

    public LoginPersenter(LoginActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //验证码回执
                case HandlerConstant.GET_CODE_SUCCESS:
                    BaseBean baseBean= (BaseBean) msg.obj;
                    if(baseBean==null){
                        break;
                    }
                    if(baseBean.isSussess()){
                        //动态改变验证码秒数
                        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_SMS_CODE));
                    }else{
                        ToastUtil.showLong(baseBean.getDesc());
                    }
                    break;
                //验证码/密码登录
                case HandlerConstant.PWD_LOGIN_SUCCESS:
                case HandlerConstant.SMS_LOGIN_SUCCESS:
                     Login login= (Login) msg.obj;
                     if(login==null){
                         break;
                     }
                     if(login.isSussess() && login.getData()!=null){
                         //存储token
                         SPUtil.getInstance(activity).addString(SPUtil.TOKEN,login.getData().getToken());
                         activity.finish();
                     }else{
                         ToastUtil.showLong(login.getDesc());
                     }
                     break;
                case HandlerConstant.REQUST_ERROR:
                    ToastUtil.showLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 获取验证码
     */
    public void getCode(String mobile){
        DialogUtil.showProgress(activity,"验证码获取中");
        HttpMethod.sendCode(mobile,"1",handler);
    }


    /**
     * 验证码登录
     * @param code
     * @param mobile
     */
    public void smsLogin(String code,String mobile){
        DialogUtil.showProgress(activity,"登录中");
        HttpMethod.smsLogin(code,mobile,handler);
    }


    /**
     * 密码登录
     * @param pwd
     * @param mobile
     */
    public void pwdLogin(String pwd,String mobile){
        DialogUtil.showProgress(activity,"登录中");
        HttpMethod.pwdLogin("0",pwd,mobile,handler);
    }


    /**
     * 微信登录
     * @param headimgurl
     * @param nickname
     * @param unionid
     */
    public void wxLogin(String headimgurl,String nickname,String unionid){
        DialogUtil.showProgress(activity,"登录中");
        HttpMethod.threeLogin(unionid,"0","0",null,headimgurl,nickname,null,handler);
    }
}
