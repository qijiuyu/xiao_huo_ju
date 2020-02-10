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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/2/8.
 */

public class RemarkActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.et_name)
    EditText etName;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_remark);
        ButterKnife.bind(this);

        tvTitle.setText("个人介绍");
        tvRight.setText("确认");
    }

    @OnClick({R.id.img_bank, R.id.tv_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            case R.id.tv_right:
                String name=etName.getText().toString().trim();
                if(TextUtils.isEmpty(name)){
                    ToastUtil.showLong("请输入个人介绍");
                    return;
                }
                break;
            default:
                break;
        }
    }
}
