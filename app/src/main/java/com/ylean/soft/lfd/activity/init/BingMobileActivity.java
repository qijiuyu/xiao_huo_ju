package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
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

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 绑定手机号
 * Created by Administrator on 2020/2/8.
 */
public class BingMobileActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    /**
     * openId：第三方openid
     * userImg：第三方用户头像
     * nickName：第三方昵称
     * type：0，微信，   1，QQ
     */
    private String openId,userImg,nickName,type;
    //计数器
    private Timer mTimer;
    private int time = 0;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        ButterKnife.bind(this);
        initView();
        checkTime();
    }

    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("绑定手机号");
        openId=getIntent().getStringExtra("openId");
        userImg=getIntent().getStringExtra("userImg");
        nickName=getIntent().getStringExtra("nickName");
        type=getIntent().getStringExtra("type");
    }

    @OnClick({R.id.img_bank, R.id.tv_send_code, R.id.tv_confirm})
    public void onViewClicked(View view) {
        String mobile=etMobile.getText().toString().trim();
        String code=etCode.getText().toString().trim();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
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
                DialogUtil.showProgress(this,"获取验证码中");
                HttpMethod.sendCode(mobile,"2",handler);
                break;
            case R.id.tv_confirm:
                if(TextUtils.isEmpty(mobile)){
                    ToastUtil.showLong("请输入您的手机号！");
                    return;
                }
                if(mobile.length()<11){
                    ToastUtil.showLong("请输入正确的手机号！");
                    return;
                }
                if(TextUtils.isEmpty(code)){
                    ToastUtil.showLong("请输入验证码！");
                    return;
                }
                threeLogin(code,mobile);
                break;
            default:
                break;
        }
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
                        startTime();
                    }else{
                        ToastUtil.showLong(baseBean.getDesc());
                    }
                    break;
                //第三方登录
                case HandlerConstant.THREE_LOGIN_SUCCESS:
                      Login login= (Login) msg.obj;
                      if(login==null){
                          break;
                      }
                      if(login.isSussess()){
                          //存储token
                          SPUtil.getInstance(activity).addString(SPUtil.TOKEN,login.getData().getToken());
                          //存储用户id
                          SPUtil.getInstance(activity).addString(SPUtil.USER_ID,login.getData().getId()+"");
                          //是否通过第三方登录
                          SPUtil.getInstance(activity).addBoolean(SPUtil.IS_THREE_LOGIN,true);
                          //通知关闭上个页面
                          EventBus.getDefault().post(new EventBusType(EventStatus.CLOSE_PAGE));
                          //进入选择喜好的页面
                          setClass(SelectTagActivity.class);
                          finish();

                          //埋点注册
                          MobclickAgent.onEvent(BingMobileActivity.this, "register");
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
     * 第三方登录
     */
    private void threeLogin(String code,String mobile){
        DialogUtil.showProgress(activity,"绑定中");
        HttpMethod.threeLogin(openId,type,"1",code,userImg,nickName,mobile,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
