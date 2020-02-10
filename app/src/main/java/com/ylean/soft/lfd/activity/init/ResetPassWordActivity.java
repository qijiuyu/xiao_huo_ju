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
 * 设置登录密码
 * Created by Administrator on 2020/2/5.
 */

public class ResetPassWordActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd2)
    EditText etPwd2;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pwd);
        ButterKnife.bind(this);
        tvTitle.setText("重置登录密码");
    }

    @OnClick({R.id.img_bank, R.id.tv_confirm})
    public void onViewClicked(View view) {
        String pwd=etPwd.getText().toString().trim();
        String pwd2=etPwd2.getText().toString().trim();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            case R.id.tv_confirm:
                if(TextUtils.isEmpty(pwd)){
                    ToastUtil.showLong("请输入登录密码");
                    return;
                }
                if(TextUtils.isEmpty(pwd2)){
                    ToastUtil.showLong("请再次输入登录密码");
                    return;
                }
                break;
            default:
                break;
        }
    }
}
