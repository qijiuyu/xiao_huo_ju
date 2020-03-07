package com.ylean.soft.lfd.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.AgreementActivity;
import com.ylean.soft.lfd.activity.init.BingMobileActivity;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.DataCleanManager;
import com.zxdc.utils.library.util.DialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置
 * Created by Administrator on 2020/2/8.
 */

public class SettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    private Handler handler=new Handler();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     */
    private void initView(){
        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.img_bank, R.id.rel_mobile, R.id.rel_wx, R.id.rel_qq, R.id.rel_pwd, R.id.rel_agreement, R.id.rel_privacy, R.id.rel_hezuo,R.id.rel_help, R.id.rel_feedback, R.id.rel_cache, R.id.rel_about, R.id.tv_out})
    public void onViewClicked(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            case R.id.rel_mobile:
                setClass(BingMobileActivity.class);
                break;
            case R.id.rel_wx:
                break;
            case R.id.rel_qq:
                break;
            case R.id.rel_pwd:
                break;
            //用户协议
            case R.id.rel_agreement:
                intent.setClass(this,AgreementActivity.class);
                intent.putExtra("type",2);
                startActivity(intent);
                break;
            //隐私协议
            case R.id.rel_privacy:
                intent.setClass(this,AgreementActivity.class);
                intent.putExtra("type",3);
                startActivity(intent);
                break;
            //合作内容
            case R.id.rel_hezuo:
                 intent.setClass(this,AgreementActivity.class);
                 intent.putExtra("type",4);
                 startActivity(intent);
                  break;
            case R.id.rel_help:
                 setClass(HelpListActivity.class);
                break;
            case R.id.rel_feedback:
                setClass(FeedBackActivity.class);
                break;
            case R.id.rel_cache:
                clearCache();
                break;
            case R.id.rel_about:
                setClass(AbountActivity.class);
                break;
            case R.id.tv_out:
                break;
            default:
                break;
        }
    }


    /**
     * 清理缓存
     */
    private void clearCache(){
        DialogUtil.showProgress(this,"缓存清理中...");
        try {
            DataCleanManager.clearAllCache(this);
            handler.postDelayed(new Runnable() {
                public void run() {
                    DialogUtil.closeProgress();
                    tvCache.setText("0.00K");
                }
            },2000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
