package com.ylean.soft.lfd.activity.init;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.qq.tencent.IUiListener;
import com.umeng.qq.tencent.Tencent;
import com.umeng.qq.tencent.UiError;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.Constant;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.util.ToastUtil;

import java.util.Map;
import java.util.Set;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化控件
     */
    private void initView(){
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
                if(TextUtils.isEmpty(mobile)){
                    ToastUtil.showLong("请输入您的手机号！");
                    return;
                }
                if(mobile.length()<11){
                    ToastUtil.showLong("请输入正确的手机号！");
                    return;
                }
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
     * 监听QQ登录
     */
    private UMAuthListener umAuthListener = new UMAuthListener() {
        public void onStart(SHARE_MEDIA share_media) {

        }
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            LogUtils.e("++++++++++++++++++++=111");
            Set<String> set = map.keySet();
            for (String string : set) {
                if (string.equals("screen_name")) {
                    LogUtils.e(map.get(string)+"+++++++++++++++++++++222");
                }
            }
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

}
