package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.ToastUtil;

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        tvTitle.setText("注册");
        final String register_des="我已阅读并同意<font color=\"#FF6D32\">《小火剧注册协议》</font>";
        tvDes.setText(Html.fromHtml(register_des));
    }

    @OnClick({R.id.img_bank, R.id.tv_send_code, R.id.img_check, R.id.tv_des, R.id.tv_next})
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
