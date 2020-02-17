package com.ylean.soft.lfd.activity.recommended;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.recommended.FoundAdapter;
import com.ylean.soft.lfd.persenter.recommended.RecommendedPersenter;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 推荐
 * Created by Administrator on 2020/2/5.
 */

public class RecommendedActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawerLayout;
    @BindView(R.id.listView)
    RecyclerView recyclerView;
    //当前滑动的页码,防止没滑动过去重新加载视频
    private int currentPosition = -1;
    //滑动停止后为true
    private boolean isStopScroll = true;
    private FoundAdapter foundAdapter;
    private RecommendedPersenter recommendedPersenter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        //实例化MVP对象
        recommendedPersenter=new RecommendedPersenter(this);
        //初始化RecycleView配置
        initRecycleView();
        //设置侧边栏
        rightMenu();
    }


    /**
     * 初始化RecycleView配置
     */
    private void initRecycleView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        foundAdapter=new FoundAdapter(this);
        recyclerView.setAdapter(foundAdapter);
        //向下滑动,可监听到最后一个
        new GravityPagerSnapHelper(Gravity.BOTTOM, false, new GravitySnapHelper.SnapListener() {
            public void onSnap(final int positions) {
                //切换Item之后的操作
                if (isStopScroll == true) {
                    if (positions == 4) {
                        if (currentPosition != positions) {
                            foundAdapter.selectPosition(positions);
                            currentPosition = positions;
                        }
                    }
                }

            }
        }).attachToRecyclerView(recyclerView);

        //向上滑动,可监听到0
        new GravityPagerSnapHelper(Gravity.TOP, false, new GravitySnapHelper.SnapListener() {
            public void onSnap(final int positions) {
                if (isStopScroll == true) {
                    if (currentPosition != positions) {
                        foundAdapter.selectPosition(positions);
                        currentPosition = positions;
                    }
                }
            }
        }).attachToRecyclerView(recyclerView);

        //item切换监听
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    //已经停止滚动
                    case 0:
                        isStopScroll = true;
                        break;
                    //正在被拖拽
                    case 1:
                        isStopScroll = false;
                        break;
                    //正在依靠惯性滚动
                    case 2:
                        isStopScroll = false;
                        break;
                    default:
                        break;
                }
            }
        });
    }


    /**
     * 设置侧边栏
     */
    private void rightMenu() {
        // 设置遮盖主要内容的布颜色
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }



    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //关闭侧边栏
            case EventStatus.CLOSE_VIDEO_RIGHT:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            default:
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        foundAdapter.setVideoStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foundAdapter.setVideoStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        foundAdapter.removeVideo();
    }
}
