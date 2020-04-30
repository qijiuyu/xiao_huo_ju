package com.ylean.soft.lfd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.focus.FocusActivity;
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.activity.main.MainActivity;
import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.ylean.soft.lfd.activity.user.UserActivity;
import com.ylean.soft.lfd.utils.UpdateVersionUtils;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.ActivitysLifecycle;
import com.zxdc.utils.library.util.DataCleanManager;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.util.error.CockroachUtil;
import com.zxdc.utils.library.view.ClickLinearLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class TabActivity extends android.app.TabActivity {

    @BindView(R.id.img_main)
    ImageView imgMain;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.img_recommend)
    ImageView imgRecommend;
    @BindView(R.id.tv_recommend)
    TextView tvRecommend;
    @BindView(R.id.img_focus)
    ImageView imgFocus;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.img_user)
    ImageView imgUser;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(android.R.id.tabhost)
    TabHost tabhost;
    @BindView(R.id.lin_main)
    ClickLinearLayout linMain;
    @BindView(R.id.lin_recommend)
    ClickLinearLayout linRecommend;
    @BindView(R.id.lin_focus)
    ClickLinearLayout linFocus;
    @BindView(R.id.lin_user)
    ClickLinearLayout linUser;
    @BindView(R.id.rel)
    RelativeLayout rel;
    private int[] notClick = new int[]{R.mipmap.tab_1_false, R.mipmap.tab_2_false, R.mipmap.tab_3_false, R.mipmap.tab_4_false};
    private int[] yesClick = new int[]{R.mipmap.tab_1_true, R.mipmap.tab_2_true, R.mipmap.tab_3_true, R.mipmap.tab_4_true};
    // 按两次退出
    protected long exitTime = 0;
    private List<TextView> tvList = new ArrayList<>();
    private List<ImageView> imgList = new ArrayList<>();
    private View contentView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtils.transparencyBar(this);
//        StatusBarUtils.setStatusBarGradiant(this,R.drawable.textview_color);
        StatusBarUtils.setStatusBarMode(this,false,R.color.color_FFBC32);
        contentView = getLayoutInflater().inflate(R.layout.activity_tab_two, null);
        setContentView(contentView);
//        new KeyboardUtil(this, contentView);
        ButterKnife.bind(this);
        initView();

        //查询最新版本
        new UpdateVersionUtils().getVersion(this);
    }




    /**
     * 初始化
     */
    private void initView() {
        imgList.add(imgMain);
        imgList.add(imgRecommend);
        imgList.add(imgFocus);
        imgList.add(imgUser);
        tvList.add(tvMain);
        tvList.add(tvRecommend);
        tvList.add(tvFocus);
        tvList.add(tvUser);
        tabhost = this.getTabHost();
        TabHost.TabSpec spec;
        spec = tabhost.newTabSpec("推荐").setIndicator("推荐").setContent(new Intent(this, MainActivity.class));
        tabhost.addTab(spec);
        spec = tabhost.newTabSpec("发现").setIndicator("发现").setContent(new Intent(this, RecommendedActivity.class));
        tabhost.addTab(spec);
        spec = tabhost.newTabSpec("关注").setIndicator("关注").setContent(new Intent(this, FocusActivity.class));
        tabhost.addTab(spec);
        spec = tabhost.newTabSpec("我的").setIndicator("我的").setContent(new Intent(this, UserActivity.class));
        tabhost.addTab(spec);
        tabhost.setCurrentTab(0);

        //清理缓存
        new Handler().post(new Runnable() {
            public void run() {
                DataCleanManager.clearAllCache(TabActivity.this);
            }
        });
    }

    int type;
    @OnClick({R.id.lin_main, R.id.lin_recommend, R.id.lin_focus, R.id.lin_user})
    public void onViewClicked(View view) {
        Intent intent=new Intent(this, LoginActivity.class);
        switch (view.getId()) {
            //推荐
            case R.id.lin_main:
                StatusBarUtils.setStatusBarMode(this,false,R.color.color_FFBC32);
                updateTag(0);
                EventBus.getDefault().post(new EventBusType(EventStatus.UPDATE_TAB_MENU));
                break;
            //发现
            case R.id.lin_recommend:
                StatusBarUtils.setStatusBarMode(this,false,android.R.color.black);
                updateTag(1);
                break;
            //关注
            case R.id.lin_focus:
                if(MyApplication.isLogin()){
                    StatusBarUtils.setStatusBarMode(this,true,android.R.color.white);
                    updateTag(2);
                    EventBus.getDefault().post(new EventBusType(EventStatus.UPDATE_TAB_MENU));
                }else{
                    startActivity(intent);
                }
                break;
            //我的
            case R.id.lin_user:
                if(MyApplication.isLogin()){
                    StatusBarUtils.setStatusBarMode(this,true,R.color.color_f5f5f5);
                    updateTag(3);
                    EventBus.getDefault().post(new EventBusType(EventStatus.UPDATE_TAB_MENU));
                    EventBus.getDefault().post(new EventBusType(EventStatus.USER_CLEAR_DATA));
                }else{
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    /**
     * 切换tab时，更新图片和文字颜色
     */
    private Handler handler=new Handler();
    private void updateTag(int type) {
        for (int i = 0; i < 4; i++) {
            if (i == type) {
                imgList.get(i).setImageDrawable(getResources().getDrawable(yesClick[i]));
            } else {
                imgList.get(i).setImageDrawable(getResources().getDrawable(notClick[i]));
            }
        }
        tabhost.setCurrentTab(type);
    }


    /**
     * 连续点击两次返回退出
     *
     * @param event
     * @return
     */
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.showLong("再按一次退出程序!");
                exitTime = System.currentTimeMillis();
            } else {
                //关闭小强
                CockroachUtil.clear();
                ActivitysLifecycle.getInstance().exit();
            }
            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
