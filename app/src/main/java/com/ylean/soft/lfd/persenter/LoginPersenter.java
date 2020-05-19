package com.ylean.soft.lfd.persenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ylean.soft.lfd.activity.TabActivity;
import com.ylean.soft.lfd.activity.init.BingMobileActivity;
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
    /**
     * openId：第三方openid
     * userImg：第三方用户头像
     * nickName：第三方昵称
     * type：0，微信，   1，QQ
     */
    private String openId,userImg,nickName,type;

    public LoginPersenter(LoginActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            Login login;
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
                     login= (Login) msg.obj;
                     if(login==null){
                         break;
                     }
                     if(login.isSussess() && login.getData()!=null){
                         //存储token
                         SPUtil.getInstance(activity).addString(SPUtil.TOKEN,login.getData().getToken());
                         //存储用户id
                         SPUtil.getInstance(activity).addString(SPUtil.USER_ID,login.getData().getId()+"");
                         //是否通过第三方登录
                         SPUtil.getInstance(activity).addBoolean(SPUtil.IS_THREE_LOGIN,false);

                         if(msg.what==HandlerConstant.SMS_LOGIN_SUCCESS){
                             //存储账号和密码
                             SPUtil.getInstance(activity).addString(SPUtil.ACCOUNT,login.getData().getMobile());
                             SPUtil.getInstance(activity).addString(SPUtil.PASSWORD,login.getData().getPassword());
                         }
                         Intent intent=new Intent(activity, TabActivity.class);
                         activity.startActivity(intent);
                         activity.finish();
                     }else{
                         ToastUtil.showLong(login.getDesc());
                     }
                     break;
                //第三方登录
                case HandlerConstant.THREE_LOGIN_SUCCESS:
                     login= (Login) msg.obj;
                     if(login==null){
                         break;
                     }
                     //去绑定手机号
                     if(login.getCode()==-200){
                         Intent intent=new Intent(activity, BingMobileActivity.class);
                         intent.putExtra("type",type);
                         intent.putExtra("openId",openId);
                         intent.putExtra("userImg",userImg);
                         intent.putExtra("nickName",nickName);
                         activity.startActivity(intent);
                         break;
                     }
                     //登录成功
                     if(login.isSussess() && login.getData()!=null){
                         //存储token
                         SPUtil.getInstance(activity).addString(SPUtil.TOKEN,login.getData().getToken());
                         //存储用户id
                         SPUtil.getInstance(activity).addString(SPUtil.USER_ID,login.getData().getId()+"");
                         //是否通过第三方登录
                         SPUtil.getInstance(activity).addBoolean(SPUtil.IS_THREE_LOGIN,true);
                         //存储账号和密码
                         SPUtil.getInstance(activity).addString(SPUtil.ACCOUNT,login.getData().getMobile());
                         SPUtil.getInstance(activity).addString(SPUtil.PASSWORD,login.getData().getPassword());

                         Intent intent=new Intent(activity, TabActivity.class);
                         activity.startActivity(intent);
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
        SPUtil.getInstance(activity).addBoolean(SPUtil.IS_SMSCODE_LOGIN,true);
        HttpMethod.smsLogin(code,mobile,handler);
    }


    /**
     * 密码登录
     * @param pwd
     * @param mobile
     */
    public void pwdLogin(String pwd,String mobile){
        DialogUtil.showProgress(activity,"登录中");
        SPUtil.getInstance(activity).addBoolean(SPUtil.IS_SMSCODE_LOGIN,false);
        //存储账号和密码
        SPUtil.getInstance(activity).addString(SPUtil.ACCOUNT,mobile);
        SPUtil.getInstance(activity).addString(SPUtil.PASSWORD,pwd);
        HttpMethod.pwdLogin("0",pwd,mobile,handler);
    }


    /**
     * 第三方登录
     */
    public void threeLogin(String openId,String type,String userImg,String nickName){
        this.openId=openId;
        this.type=type;
        this.userImg=userImg;
        this.nickName=nickName;
        //存储openId
        SPUtil.getInstance(activity).addString(SPUtil.OPEN_ID,openId);
        DialogUtil.showProgress(activity,"登录中");
        HttpMethod.threeLogin(openId,type,"0",null,userImg,nickName,null,handler);
    }
}
