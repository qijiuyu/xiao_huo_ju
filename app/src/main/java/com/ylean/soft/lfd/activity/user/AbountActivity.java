package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Agreement;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/2/8.
 */

public class AbountActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.tv_content)
    TextView tvContent;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abount);
        ButterKnife.bind(this);

        tvTitle.setText("关于我们");
        tvVersion.setText(Util.getVersionName(this));

        getAgreement();
    }

    @OnClick(R.id.img_bank)
    public void onViewClicked() {
        finish();
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                case HandlerConstant.GET_AGREEMENT_SUCCESS:
                    Agreement agreement= (Agreement) msg.obj;
                    if(agreement==null){
                        break;
                    }
                    if(agreement.isSussess() && agreement.getData()!=null){
                        tvContent.setText(Html.fromHtml(agreement.getData().getContent()));
                        scrollView.scrollTo(0,0);
                    }else{
                        ToastUtil.showLong(agreement.getDesc());
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
     * 查询协议
     */
    private void getAgreement(){
        DialogUtil.showProgress(this,"加载中");
        HttpMethod.getAgreement(5,handler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeHandler(handler);
    }
}
