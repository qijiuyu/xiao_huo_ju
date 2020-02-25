package com.ylean.soft.lfd.activity.init;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.persenter.LoginPersenter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.Constant;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**登录
 * Created by Administrator on 2020/2/5.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_code_login)
    TextView tvCodeLogin;
    @BindView(R.id.tv_pwd_login)
    TextView tvPwdLogin;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    /**
     * 1：验证码登录
     * 2：密码登录
     */
    private int loginType=1;
    private IWXAPI api;
    private UMShareAPI umShareAPI;
    //计数器
    private Timer mTimer;
    private int time = 0;
    private LoginPersenter loginPersenter;
    private Handler handler=new Handler();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        checkTime();
    }


    /**
     * 初始化控件
     */
    private void initView(){
        //实例化MVP类
        loginPersenter=new LoginPersenter(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        final String register_des="还没账号，去 <font color=\"#FF6D32\"><u>注册</u></font>";
        tvRegister.setText(Html.fromHtml(register_des));
    }


    @OnClick({R.id.img_close, R.id.tv_code_login, R.id.tv_pwd_login, R.id.tv_send_code, R.id.tv_forget_pwd, R.id.tv_login, R.id.tv_register, R.id.img_wx, R.id.img_qq})
    public void onViewClicked(View view) {
        String mobile=etMobile.getText().toString().trim();
        String code=etCode.getText().toString().trim();
        String pwd=etPwd.getText().toString().trim();
        switch (view.getId()) {
            case R.id.img_close:
                finish();
                break;
            //切换到验证码登录
            case R.id.tv_code_login:
                loginType=1;
                updateLoginType();
                break;
            //切换到密码登录
            case R.id.tv_pwd_login:
                loginType=2;
                updateLoginType();
                break;
            //发送验证码
            case R.id.tv_send_code:
                if(time>0){
                    return;
                }
                if(TextUtils.isEmpty(mobile)){
                    ToastUtil.showLong("请输入您的手机号！");
                    return;
                }
                if(mobile.length()<11){
                    ToastUtil.showLong("请输入正确的手机号！");
                    return;
                }
                loginPersenter.getCode(mobile);
                break;
            //忘记密码
            case R.id.tv_forget_pwd:
                setClass(VerifyMobileActvity.class);
                break;
            //登录
            case R.id.tv_login:
                if(TextUtils.isEmpty(mobile)){
                    ToastUtil.showLong("请输入您的手机号！");
                    return;
                }
                if(mobile.length()<11){
                    ToastUtil.showLong("请输入正确的手机号！");
                    return;
                }
                if(loginType==1 && TextUtils.isEmpty(code)){
                    ToastUtil.showLong("请输入验证码！");
                    return;
                }
                if(loginType==2 && TextUtils.isEmpty(pwd)){
                    ToastUtil.showLong("请输入登录密码！");
                    return;
                }
                if(loginType==1){
                    loginPersenter.smsLogin(code,mobile);
                }else {
                    loginPersenter.pwdLogin(pwd,mobile);
                }
                break;
            //去注册
            case R.id.tv_register:
                setClass(RegisterActivity.class);
                break;
            //微信登录
            case R.id.img_wx:
                //注册微信d
                api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID, true);
                api.registerApp(Constant.WX_APPID);
                if (!api.isWXAppInstalled()) {
                    ToastUtil.showLong("请先安装微信客户端!");
                } else {
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "wechat_sdk_demo_test";
                    api.sendReq(req);
                }
                break;
            //QQ登录
            case R.id.img_qq:
                umShareAPI = UMShareAPI.get(this);
                umShareAPI.doOauthVerify(this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            default:
                break;
        }
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //展示验证码
            case EventStatus.SHOW_SMS_CODE:
                 startTime();
                 break;
            //微信登录
            case EventStatus.WX_LOGIN:
                 String msg = (String) eventBusType.getObject();
                 if (TextUtils.isEmpty(msg)) {
                     return;
                 }
                 String[] str = msg.split(",");
                 loginPersenter.wxLogin(str[0], str[1], str[2]);
                 break;
            default:
                break;
        }
    }


    /**
     * 切换登录方式
     */
    private void updateLoginType(){
        if(loginType==1){
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            etCode.setVisibility(View.VISIBLE);
            tvSendCode.setVisibility(View.VISIBLE);
            etPwd.setVisibility(View.GONE);
            tvForgetPwd.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);
        }else{
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            etCode.setVisibility(View.GONE);
            tvSendCode.setVisibility(View.GONE);
            etPwd.setVisibility(View.VISIBLE);
            tvForgetPwd.setVisibility(View.VISIBLE);
            tvRegister.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 动态改变验证码秒数
     */
    private void startTime() {
        time=60;
        //保存计时时间
        SPUtil.getInstance(activity).addString(SPUtil.SEND_CODE, String.valueOf((System.currentTimeMillis() + 60000)));
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (time <= 0) {
                    handler.post(new Runnable() {
                        public void run() {
                            mTimer.cancel();
                            tvSendCode.setText("获取验证码");
                            SPUtil.getInstance(activity).removeMessage(SPUtil.SEND_CODE);
                        }
                    });
                } else {
                    --time;
                    handler.post(new Runnable() {
                        public void run() {
                            tvSendCode.setText(time + "秒");
                        }
                    });
                }
            }
        }, 0, 1000);
    }


    /**
     * 判断验证码秒数是否超过一分钟
     */
    private void checkTime() {
        String stoptime = SPUtil.getInstance(activity).getString(SPUtil.SEND_CODE);
        if (!TextUtils.isEmpty(stoptime)) {
            int t = (int) ((Double.parseDouble(stoptime) - System.currentTimeMillis()) / 1000);
            if (t > 0) {
                time = t;
                startTime();
            }
        }
    }


    /**
     * 监听QQ登录
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        public void onStart(SHARE_MEDIA share_media) {

        }
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            LogUtils.e("++++++++++++++++++++=111");
            String unionid = map.get("unionid");
            String name = map.get("name");
            String iconurl = map.get("iconurl");
            String openid = map.get("openid");
            umShareAPI.getPlatformInfo(LoginActivity.this, share_media,
                    new UMAuthListener() {
                        public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
                        }
                        public void onStart(SHARE_MEDIA share_media) {
                        }
                        public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, String> data) {
                            Set<String> set = data.keySet();
                            for (String string : set) {
                                // 获得头像图片的网络地址
//                                if (string.equals("profile_image_url")) {
//                                    image_url = data.get(string);
//                                }
//                                // 设置昵称
//                                if (string.equals("screen_name")) {
//                                    name = data.get(string);
//                                }
                            }
                        }
                        @Override
                        public void onCancel(SHARE_MEDIA arg0, int arg1) {

                        }
                    });
        }
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        umShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
            mTimer.purge();
            mTimer=null;
        }
        EventBus.getDefault().unregister(this);
    }
}
