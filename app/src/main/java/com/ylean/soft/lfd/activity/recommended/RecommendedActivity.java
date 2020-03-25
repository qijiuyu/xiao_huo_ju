package com.ylean.soft.lfd.activity.recommended;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.UploadVideoActivity;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.adapter.recommended.FoundAdapter;
import com.ylean.soft.lfd.persenter.recommended.RecommendedPersenter;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
    public RecyclerView recyclerView;
    //当前滑动的页码,防止没滑动过去重新加载视频
    private int currentPosition = -1;
    //滑动停止后为true
    private boolean isStopScroll = true;
    //视频列表适配器
    private FoundAdapter foundAdapter;
    //视频对象
    private VideoInfo.VideoBean videoBean;
    private RecommendedPersenter recommendedPersenter;
    //视频集合
    private List<VideoInfo.VideoBean> videoList=new ArrayList<>();
    //查询次数
    private int loadNum;
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
        //获取视频数据
        recommendedPersenter.foundVideo(0);
    }


    /**
     * 初始化RecycleView配置
     */
    private void initRecycleView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //向下滑动,可监听到最后一个
        new GravityPagerSnapHelper(Gravity.BOTTOM, false, new GravitySnapHelper.SnapListener() {
            public void onSnap(final int positions) {
                //切换Item之后的操作
                if (isStopScroll == true) {
                    if (positions == (Integer.MAX_VALUE-1)) {
                        if (currentPosition != positions) {
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
                        currentPosition = positions;
                        //添加浏览记录
                       if(MyApplication.isLogin()){
                          foundAdapter.addBrowse();
                       }
                       //如果视频集合不够了，就继续加载
                       if(currentPosition==videoList.size()-1){
                           foundAdapter=null;
                           recommendedPersenter.foundVideo(0);
                           return;
                       }

                        //加载下个/上个视频
                        videoBean=videoList.get(currentPosition);
                        foundAdapter.selectPosition(videoBean,currentPosition);

                        if(currentPosition==videoList.size()-3){
                            recommendedPersenter.foundVideo(0);
                        }
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
        Intent intent=new Intent();
        switch (eventBusType.getStatus()) {
            //获取视频详情
            case EventStatus.FOUND_VIDEO_INFO:
                  videoBean= (VideoInfo.VideoBean) eventBusType.getObject();
                  videoList.add(videoBean);
                  if(foundAdapter==null){
                      foundAdapter=new FoundAdapter(this,videoBean);
                      recyclerView.setAdapter(foundAdapter);
                  }
                  break;
            //点赞、取消点赞
            case EventStatus.IS_THUMP:
                 if(videoBean.isThumbEpisode()){
                     videoBean.setThumbEpisode(false);
                 }else{
                     videoBean.setThumbEpisode(true);
                 }
                  foundAdapter.praiseEnd(videoBean);
                  break;
            //关注、取消关注剧集
            case EventStatus.FOCUS_SERIAL:
                  if(videoBean.isFollowSerial()){
                      videoBean.setFollowSerial(false);
                  }else{
                      videoBean.setFollowSerial(true);
                  }
                  foundAdapter.focusSerial(videoBean);
                  break;
            //发送弹屏成功
            case EventStatus.SEND_SCREEN:
                 foundAdapter.getScreen();
                 break;
            //获取弹屏成功
            case EventStatus.GET_SCREEEN:
                  List<Screen.ScreenBean> screenList= (List<Screen.ScreenBean>) eventBusType.getObject();
                  foundAdapter.showScreen(screenList);
                  break;
            //关闭侧边栏
            case EventStatus.CLOSE_VIDEO_RIGHT:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                break;
            //选择单集视频播放
            case EventStatus.FUND_SELECT_BLUES:
                  int singleId= (int) eventBusType.getObject();
                  //添加浏览记录
                  if(MyApplication.isLogin()){
                      foundAdapter.addBrowse();
                  }

                  //播放选中的单集视频
                  intent.setClass(this, VideoPlayActivity.class);
                  intent.putExtra("singleId",singleId);
                  startActivity(intent);
                  break;
            //关注、取消关注用户
            case EventStatus.IS_FOLLOW:
                 if(videoBean.isFollowUser()){
                     videoBean.setFollowUser(false);
                 }else{
                     videoBean.setFollowUser(true);
                 }
                foundAdapter.isFocusUser(videoBean);
                break;
            //取消用户关注
            case EventStatus.CANCLE_FOCUS_USER:
                videoBean.setFollowUser(false);
                foundAdapter.isFocusUser(videoBean);
                break;
            //关注用户
            case EventStatus.FOCUS_USER:
                videoBean.setFollowUser(true);
                foundAdapter.isFocusUser(videoBean);
                break;
            //分享
            case EventStatus.SHARE_APP:
                if(videoBean==null || TextUtils.isEmpty(videoBean.getVideourl())){
                    return;
                }
                int shareType= (int) eventBusType.getObject();
                intent.setClass(this, UploadVideoActivity.class);
                intent.putExtra("videoUrl",videoBean.getVideourl());
                intent.putExtra("shareType",shareType);
                startActivity(intent);
                break;
            case EventStatus.UPDATE_TAB_MENU:
                 if(foundAdapter!=null){
                     foundAdapter.setVideoStatus(false);
                 }
                  break;
            default:
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(foundAdapter!=null){
            foundAdapter.setVideoStatus(true);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(foundAdapter!=null){
            foundAdapter.setVideoStatus(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if(foundAdapter!=null){
            //添加浏览记录
            foundAdapter.addBrowse();
            //释放资源
            foundAdapter.removeVideo();
        }
    }
}
