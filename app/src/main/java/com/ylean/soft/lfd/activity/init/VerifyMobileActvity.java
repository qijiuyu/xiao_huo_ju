package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 验证手机号
 * Created by Administrator on 2020/2/5.
 */

public class VerifyMobileActvity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_mobile);
        ButterKnife.bind(this);
        tvTitle.setText("验证手机号");
    }

    @OnClick({R.id.img_bank, R.id.tv_send_code, R.id.tv_next})
    public void onViewClicked(View view) {
        String mobile=etMobile.getText().toString().trim();
        String code=etCode.getText().toString().trim();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
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
                setClass(ResetPassWordActivity.class);
                break;
            default:
                break;
        }
    }
}
