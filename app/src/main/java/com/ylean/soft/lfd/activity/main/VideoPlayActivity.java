package com.ylean.soft.lfd.activity.main;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.utils.MyOnTouchListener;
import com.ylean.soft.lfd.utils.ijkplayer.media.AndroidMediaController;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * 播放视频
 * Created by Administrator on 2020/2/6.
 */

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.hud_view)
    TableLayout hubView;
    @BindView(R.id.videoView)
    IjkVideoView videoView;
    @BindView(R.id.pb_progressbar)
    ProgressBar pbProgressbar;
    @BindView(R.id.tv_blues)
    TextView tvBlues;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.img_focus)
    ImageView imgFocus;
    @BindView(R.id.img_praise)
    ImageView imgPraise;
    @BindView(R.id.tv_praise)
    TextView tvPraise;
    @BindView(R.id.tv_comm)
    TextView tvComm;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.list_comm)
    AutoPollRecyclerView listComm;
    @BindView(R.id.love)
    Love love;
    @BindView(R.id.rel_progress)
    RelativeLayout relProgress;
    @BindView(R.id.lin_screen)
    LinearLayout linScreen;
    @BindView(R.id.et_screen)
    EditText etScreen;
    @BindView(R.id.img_play)
    ImageView imgPlay;
    @BindView(R.id.img_screen)
    ImageView imgScreen;
    @BindView(R.id.img_coll)
    ImageView imgColl;
    @BindView(R.id.tv_focus_serial)
    TextView tvFocusSerial;
    //视频控制器
    private AndroidMediaController controller;
    private Handler handler=new Handler();
    /**
     * true:已点赞
     * false：未点赞
     */
    private boolean isPraise=false;
    /**
     * 1：显示弹屏布局
     * 2：显示进度条布局
     */
    private int showBottom=1;
    private VideoPlayPersenter videoPlayPersenter;
    //视频详情对象
    private VideoInfo.VideoBean videoBean;
    //弹屏adapter
    private ScreenAdapter screenAdapter;
    //弹屏列表数据
    private List<Screen.ScreenBean> screenList;
    //视频id
    private int videoId;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        initView();
        rightMenu();
        //获取视频详情
        videoPlayPersenter.videoInfo(videoId);
    }


    /**
     * 初始化
     */
    private void initView(){
        videoId=getIntent().getIntExtra("videoId",0);
        //实例化MVP对象
        videoPlayPersenter=new VideoPlayPersenter(this);
        //实例化视频控制器
        controller=new AndroidMediaController(this, false);
        videoView.setHudView(hubView);
        //进度条监听
        seekbar.setOnSeekBarChangeListener(mSeekBarListener);
        //屏幕点击
        love.setOnTouchListener(new MyOnTouchListener(myClickCallBack));
        //监听弹屏输入框
        etScreen.setOnEditorActionListener(screenListener);
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
     * 播放视频
     */
    private void startPlay(){
        videoView.setMediaController(controller);
        videoView.setVideoURI(Uri.parse(videoBean.getVideourl()));
        videoView.start();
        //监听视频播放完毕
        videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                videoView.seekTo(0);
                seekbar.setProgress(0);
                videoView.start();
            }
        });
        //监听视频加载完成
        videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                //显示视频播放时长
                videoPlayPersenter.showVideoTime(videoView, tvTime);
                //设置播放进度条的最大值
                seekbar.setMax(videoView.getDuration());
                //动态更新播放进度
                handler.postDelayed(runnable, 0);
                //隐藏进度圈
                pbProgressbar.setVisibility(View.GONE);
            }
        });
    }


    @OnClick({R.id.img_bank,R.id.img_head, R.id.tv_blues, R.id.img_focus, R.id.img_praise, R.id.img_comm, R.id.img_share, R.id.rel_screen, R.id.img_select_blues,R.id.img_coll})
    public void onViewClicked(View view) {
        Intent intent=new Intent();
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            //进入作者首页
            case R.id.img_head:
                 intent.setClass(this,AuthorDetailsActivity.class);
                 intent.putExtra("id",videoBean.getUserId());
                 startActivity(intent);
                 break;
            //选集
            case R.id.tv_blues:
                break;
            //关注用户
            case R.id.img_focus:
                videoPlayPersenter.followUser("0",videoBean.getUserId(), HandlerConstant.FOLLOW_SUCCESS);
                break;
            //点赞
            case R.id.img_praise:
                videoPlayPersenter.thump(videoBean.getSerialId());
                break;
            //关注剧集
            case R.id.img_coll:
                videoPlayPersenter.followUser("1",videoBean.getSerialId(), HandlerConstant.FOLLOW_SERIAL_SUCCESS);
                 break;
            //评论
            case R.id.img_comm:
                intent.setClass(this,CommentActivity.class);
                intent.putExtra("videoBean",videoBean);
                startActivity(intent);
                overridePendingTransition(R.anim.activity_open,0);
                break;
            //转发
            case R.id.img_share:
                videoPlayPersenter.showShareDialog();
                break;
            //弹屏
            case R.id.rel_screen:
                 if(listComm.getVisibility()==View.VISIBLE){
                     imgScreen.setImageResource(R.mipmap.img_screen);
                     listComm.setVisibility(View.GONE);
                     listComm.stop();
                     etScreen.setHint("弹屏已关闭");
                     etScreen.setFocusable(false);
                     etScreen.setFocusableInTouchMode(false);
                 }else{
                     listComm.setVisibility(View.VISIBLE);
                     listComm.start();
                     imgScreen.setImageResource(R.mipmap.img_screen_yes);
                     etScreen.setHint("发个弹幕冒个泡～");
                     etScreen.setFocusableInTouchMode(true);
                     etScreen.setFocusable(true);
                     etScreen.requestFocus();
                 }
                break;
            //选集
            case R.id.img_select_blues:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            default:
                break;
        }
    }


    /**
     * 动态更新播放进度
     */
    private Runnable runnable = new Runnable() {
        public void run() {
            if (videoView.isPlaying()) {
                seekbar.setProgress(videoView.getCurrentPosition());
            }
            handler.postDelayed(runnable, 1000);
        }
    };


    private SeekBar.OnSeekBarChangeListener mSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
        // 通知进度已经被修改
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }
        // 通知用户已经开始一个触摸拖动手势
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        // 通知用户触摸手势已经结束
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 取得当前进度条的刻度
            int progress = seekBar.getProgress();
            if (videoView.isPlaying()) {
                // 设置当前播放的位置
                videoView.seekTo(progress);
            }
        }
    };


    /**
     * 监听屏幕点击事件
     */
    private MyOnTouchListener.MyClickCallBack myClickCallBack=new MyOnTouchListener.MyClickCallBack() {
        //单击
        public void oneClick(MotionEvent event) {
            clickVideo(event);
        }
        //双击
        public void doubleClick(MotionEvent event) {
            love.addLoveView(event.getX(),event.getY());
            love.addLoveView(event.getX(),event.getY());
            if(!isPraise){
                isPraise=true;
                imgPraise.setImageResource(R.mipmap.yes_praise);
                imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
            }
        }
    };


    /**
     * 视频点击
     */
    private void clickVideo(MotionEvent event){
        int deviceHeight=Util.getDeviceWH(this,2);
        int point=deviceHeight/3;
        if(event.getY()>point && event.getY()<(point*2)){
            //播放/暂停
            if(videoView.isPlaying()){
                videoView.pause();
                imgPlay.setVisibility(View.VISIBLE);
            }else{
                videoView.start();
                imgPlay.setVisibility(View.GONE);
            }
        }else{
            //底部布局切换
            if(showBottom==1){
                showBottom=2;
                relProgress.setVisibility(View.VISIBLE);
                linScreen.setVisibility(View.GONE);
            }else{
                showBottom=1;
                relProgress.setVisibility(View.GONE);
                linScreen.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            //显示视频详情
            case EventStatus.SHOW_VIDEO_INFO:
                  videoBean= (VideoInfo.VideoBean) eventBusType.getObject();
                  //显示视频详情数据
                  showVideoInfo();
                  //播放视频
                  startPlay();
                  //获取弹屏列表
                  videoPlayPersenter.getScreen(videoBean.getId());
                  break;
            //关注、取消关注用户
            case EventStatus.IS_FOLLOW:
                  if(imgFocus.getVisibility()==View.VISIBLE){
                      imgFocus.setVisibility(View.GONE);
                  }else{
                      imgFocus.setVisibility(View.VISIBLE);
                  }
                  break;
            //关注、取消关注剧集
            case EventStatus.FOCUS_SERIAL:
                  String serialNum=tvFocusSerial.getText().toString().trim();
                  if(!videoBean.isFollowSerial()){
                      videoBean.setFollowSerial(true);
                      imgColl.setImageResource(R.mipmap.coll_icon_yes);
                      imgColl.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                      tvFocusSerial.setText(String.valueOf(Integer.parseInt(serialNum)+1));
                  }else{
                      videoBean.setFollowSerial(false);
                      imgColl.setImageResource(R.mipmap.coll_icon);
                      tvFocusSerial.setText(String.valueOf(Integer.parseInt(serialNum)-1));
                  }
                  break;
            //点赞、取消点赞
            case EventStatus.IS_THUMP:
                  String praiseNum=tvPraise.getText().toString().trim();
                  if(videoBean.isThumbEpisode()){
                      videoBean.setThumbEpisode(false);
                      imgPraise.setImageResource(R.mipmap.no_praise);
                      tvPraise.setText(String.valueOf(Integer.parseInt(praiseNum)-1));
                  }else{
                      videoBean.setThumbEpisode(true);
                      imgPraise.setImageResource(R.mipmap.yes_praise);
                      imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                      tvPraise.setText(String.valueOf(Integer.parseInt(praiseNum)+1));
                  }
                  break;
            //发送弹屏成功
            case EventStatus.SEND_SCREEN:
                  videoPlayPersenter.getScreen(videoBean.getId());
                  break;
            //获取弹屏成功
            case EventStatus.GET_SCREEEN:
                 screenList= (List<Screen.ScreenBean>) eventBusType.getObject();
                  if(screenAdapter==null){
                      screenAdapter=new ScreenAdapter(this,screenList);
                      listComm.setLayoutManager(new LinearLayoutManager(this));
                      listComm.setAdapter(screenAdapter);
                      listComm.start();
                  }else{
                      screenAdapter.notifyDataSetChanged();
                      listComm.start();
                  }
                  break;
            //关闭侧边栏
            case EventStatus.CLOSE_VIDEO_RIGHT:
                  drawerLayout.closeDrawer(Gravity.RIGHT);
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
     * 显示视频详情数据
     */
    private void showVideoInfo(){
        if(videoBean==null){
            return;
        }
        tvTitle.setText(videoBean.getIntroduction());
        Glide.with(activity).load(videoBean.getUserImg()).into(imgHead);
        //是否关注用户
        if(videoBean.isFollowUser()){
            imgFocus.setVisibility(View.GONE);
        }else{
            imgFocus.setVisibility(View.VISIBLE);
        }
        //是否点赞
        if(videoBean.isThumbEpisode()){
            imgPraise.setImageResource(R.mipmap.yes_praise);
        }else{
            imgPraise.setImageResource(R.mipmap.no_praise);
        }
        tvPraise.setText(String.valueOf(videoBean.getEpisodeCount()));
        //是否关注剧集
        if(videoBean.isFollowSerial()){
            imgColl.setImageResource(R.mipmap.coll_icon_yes);
        }else{
            imgColl.setImageResource(R.mipmap.coll_icon);
        }
        tvFocusSerial.setText(String.valueOf(videoBean.getFollowCount()));
    }


    /**
     * 监听弹屏输入框
     */
    private TextView.OnEditorActionListener screenListener=new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String content=etScreen.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.showLong("请输入弹屏内容");
                }else{
                    videoPlayPersenter.sendScreen(content,videoBean);
                    etScreen.setText(null);
                }
            }
            return false;
        }
    };


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
        //继续视频播放
        if(!videoView.isPlaying()){
            videoView.start();
            imgPlay.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //暂停视频播放
        if(videoView.isPlaying()){
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //释放视频资源
        videoView.stopPlayback();
    }
}
