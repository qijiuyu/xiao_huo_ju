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
import android.widget.RelativeLayout;

import com.github.rubensousa.gravitysnaphelper.GravityPagerSnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.adapter.recommended.FoundAdapter;
import com.ylean.soft.lfd.persenter.recommended.RecommendedPersenter;
import com.ylean.soft.lfd.view.NewDrawerLayout;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
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
    public NewDrawerLayout drawerLayout;
    @BindView(R.id.listView)
    public RecyclerView recyclerView;
    @BindView(R.id.rel)
    RelativeLayout rel;
    //当前滑动的页码,防止没滑动过去重新加载视频
    public int currentPosition = -1;
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
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        //设置侧滑菜单可以全屏滑动
        recommendedPersenter. setDrawerLeftEdgeSize(drawerLayout);
        drawerLayout.setDrawerListener(new NewDrawerLayout.DrawerListener() {
            public void onDrawerStateChanged(int arg0) {
            }
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View content = drawerLayout.getChildAt(0);
                View menu = drawerView;
                float scale = 1-slideOffset;//1~0
                float leftScale = (float) (1-0.01*scale);
                float rightScale = (float) (0.7f+0.3*scale);//0.7~1
                menu.setScaleX(leftScale);//1~0.7
                menu.setScaleY(leftScale);//1~0.7


                content.setScaleX(rightScale);
                content.setScaleY(rightScale);
                content.setTranslationX(-(menu.getMeasuredWidth()*slideOffset)/2);//0~width
            }
            public void onDrawerOpened(View arg0) {
                if(videoBean!=null){
                    EventBus.getDefault().post(new EventBusType(EventStatus.SELECT_BLUES,videoBean.getSerialId()));
                }
            }
            public void onDrawerClosed(View arg0) {
            }
        });
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
                startShare((SHARE_MEDIA) eventBusType.getObject());
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


    /**
     * 通过点击选择跳转视频播放页面
     * @param id
     */
    public void editVideo(int id){
        //添加浏览记录
        if(MyApplication.isLogin()){
            foundAdapter.addBrowse();
        }

        //播放选中的单集视频
        Intent intent=new Intent();
        intent.setClass(this, VideoPlayActivity.class);
        intent.putExtra("singleId",id);
        startActivity(intent);
    }


    /**
     * 分享
     */
    public void startShare(SHARE_MEDIA share_media) {
        String url = null;
        try {
            url= com.ylean.soft.lfd.utils.URLEncoder.encode(videoBean.getVideourl(), "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        UMWeb web = new UMWeb("http://xhj.yl-mall.cn/api/app/share");
        web.setTitle("小火剧");
        web.setDescription("提供小视频的娱乐平台");
        new ShareAction(this).setPlatform(share_media)
                .setCallback(umShareListener)
                .withMedia(web)
                .share();
    }


    private UMShareListener umShareListener = new UMShareListener() {
        public void onStart(SHARE_MEDIA share_media) {
        }

        public void onResult(SHARE_MEDIA platform) {
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                ToastUtil.showLong(getString(R.string.share_success));
            } else {
                ToastUtil.showLong(getString(R.string.share_success));
            }
        }

        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t.getMessage().indexOf("2008") != -1) {
                if (platform.name().equals("WEIXIN") || platform.name().equals("WEIXIN_CIRCLE")) {
                    ToastUtil.showLong(getString(R.string.share_failed_install_wechat));
                } else if (platform.name().equals("QQ") || platform.name().equals("QZONE")) {
                    ToastUtil.showLong(getString(R.string.share_failed_install_qq));
                }
            }
            ToastUtil.showLong(getString(R.string.share_failed));
        }

        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.showLong(getString(R.string.share_canceled));
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
