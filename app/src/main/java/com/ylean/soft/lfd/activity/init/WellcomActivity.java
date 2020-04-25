package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.TabActivity;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.StatusBarUtils;

/**
 * Created by Administrator on 2020/2/8.
 */

public class WellcomActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        StatusBarUtils.setTranslucentStatus(this);
        setContentView(R.layout.activity_wellcome);
        initView();
    }


    /**
     * 初始化
     */
    private void initView() {
        LinearLayout linearLayout=findViewById(R.id.lin_wellcome);
        Animation myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
        myAnimation_Alpha.setDuration(2500);
        myAnimation_Alpha.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }
            public void onAnimationRepeat(Animation animation) {
            }
            public void onAnimationEnd(Animation animation) {
                if(SPUtil.getInstance(WellcomActivity.this).getBoolean(SPUtil.IS_FIRST_OPEN)){
                    setClass(TabActivity.class);
                }else{
                    setClass(GuideActivity.class);
                }
                finish();
            }
        });
        linearLayout.setAnimation(myAnimation_Alpha);
        myAnimation_Alpha.start();
    }
}
