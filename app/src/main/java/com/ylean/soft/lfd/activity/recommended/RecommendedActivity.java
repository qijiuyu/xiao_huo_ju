package com.ylean.soft.lfd.activity.recommended;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ylean.soft.lfd.R;
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
                            if(videoBean!=null){
                                //获取视频数据
                                recommendedPersenter.foundVideo(videoBean.getNextEpisodeId());
                            }
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
                        if(videoBean!=null){
                            //获取视频数据
                            recommendedPersenter.foundVideo(videoBean.getPrevEpisodeId());
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
        switch (eventBusType.getStatus()) {
            //获取视频详情
            case EventStatus.FOUND_VIDEO_INFO:
                  videoBean= (VideoInfo.VideoBean) eventBusType.getObject();
                  if(videoBean==null){
                      ToastUtil.showLong("无法播放该视频");
                      return;
                  }
                  if(foundAdapter==null){
                      foundAdapter=new FoundAdapter(this,videoBean,recommendedPersenter);
                      recyclerView.setAdapter(foundAdapter);
                  }else{
                      foundAdapter.setVideoBean(videoBean);
                      foundAdapter.selectPosition(currentPosition);
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
            case EventStatus.SELECT_SINGLE_PLAY:
                  drawerLayout.closeDrawer(Gravity.RIGHT);
                  int singleId= (int) eventBusType.getObject();
                  recommendedPersenter.videoInfo(singleId);
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
                SHARE_MEDIA share_media= (SHARE_MEDIA) eventBusType.getObject();
                startShare(share_media);
                break;
            default:
                break;

        }
    }


    /**
     * 分享
     */
    private void startShare(SHARE_MEDIA share_media) {
        UMWeb web = new UMWeb("www.baidu.com");
        web.setTitle("上市品质 邀您共鉴！");
        web.setDescription("东易日盛20余年的努力与坚持，都只是为了做好家装这一件事！");
        new ShareAction(activity).setPlatform(share_media)
                .setCallback(umShareListener)
                .withMedia(web)
                .share();
    }


    private UMShareListener umShareListener = new UMShareListener() {
        public void onStart(SHARE_MEDIA share_media) {
        }

        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                ToastUtil.showLong(activity.getString(R.string.share_success));
            } else {
                ToastUtil.showLong(activity.getString(R.string.share_success));
            }
        }

        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t.getMessage().indexOf("2008") != -1) {
                if (platform.name().equals("WEIXIN") || platform.name().equals("WEIXIN_CIRCLE")) {
                    ToastUtil.showLong(activity.getString(R.string.share_failed_install_wechat));
                } else if (platform.name().equals("QQ") || platform.name().equals("QZONE")) {
                    ToastUtil.showLong(activity.getString(R.string.share_failed_install_qq));
                }
            }
            ToastUtil.showLong(activity.getString(R.string.share_failed));
        }

        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showLong(activity.getString(R.string.share_canceled));
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(foundAdapter!=null){
            foundAdapter.setVideoStatus();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(foundAdapter!=null){
            foundAdapter.setVideoStatus();
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
