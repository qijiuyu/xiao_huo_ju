package com.ylean.soft.lfd.activity.init;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册
 * Created by Administrator on 2020/2/5.
 */

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.img_check)
    ImageView imgCheck;
    @BindView(R.id.tv_des)
    TextView tvDes;
    /**
     * 是否选择协议
     */
    private boolean isSelect=false;
    //计数器
    private Timer mTimer;
    private int time = 0;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        checkTime();
    }


    /**
     * 初始化
     */
    private void initView(){
        //注册eventBus
        EventBus.getDefault().register(this);
        tvTitle.setText("注册");
        final String register_des="我已阅读并同意<font color=\"#FF6D32\">《小火剧注册协议》</font>";
        tvDes.setText(Html.fromHtml(register_des));
    }

    @OnClick({R.id.img_bank, R.id.tv_send_code, R.id.img_check, R.id.tv_des, R.id.tv_next})
    public void onViewClicked(View view) {
        Intent intent=new Intent();
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
                HttpMethod.sendCode(mobile,"0",handler);
                break;
            //协议
            case R.id.img_check:
                if(isSelect){
                    isSelect=false;
                    imgCheck.setImageResource(R.mipmap.no_check);
                }else{
                    isSelect=true;
                    imgCheck.setImageResource(R.mipmap.yes_check);
                }
                break;
            case R.id.tv_des:
                 intent.setClass(this,AgreementActivity.class);
                 intent.putExtra("type",1);
                 startActivity(intent);
                break;
            //下一步
            case R.id.tv_next:
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
                if(!isSelect){
                    ToastUtil.showLong("请同意注册协议！");
                    return;
                }
                intent.setClass(this,ResetPassWordActivity.class);
                intent.putExtra("type",1);
                intent.putExtra("mobile",mobile);
                intent.putExtra("code",code);
                startActivity(intent);
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
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            case EventStatus.CLOSE_PAGE:
                 finish();
                 break;
            default:
                break;
        }
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
        removeHandler(handler);
    }

}
