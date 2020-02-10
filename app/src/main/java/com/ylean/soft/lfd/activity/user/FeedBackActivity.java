package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.ClickTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/2/8.
 */

public class FeedBackActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);

        tvTitle.setText("意见反馈");
    }

    @OnClick({R.id.img_bank, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            case R.id.tv_confirm:
                String name=etName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showLong("请输入您想反馈的意见或建议");
                    return;
                }
                break;
            default:
                break;
        }
    }
}
