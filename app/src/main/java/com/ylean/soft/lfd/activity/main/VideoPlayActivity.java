package com.ylean.soft.lfd.activity.main;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.utils.ijkplayer.media.AndroidMediaController;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import master.flame.danmaku.ui.widget.DanmakuView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 播放视频
 * Created by Administrator on 2020/2/6.
 */

public class VideoPlayActivity extends BaseActivity {

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
    @BindView(R.id.tv_share)
    TextView tvShare;
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
    //视频控制器
    private AndroidMediaController controller;
    private String videoUrl="http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        initView();
        rightMenu();
        //播放视频
        startPlay();
    }

    /**
     * 初始化
     */
    private void initView(){
        //实例化MVP对象
        videoPlayPersenter=new VideoPlayPersenter(this);
        //实例化视频控制器
        controller=new AndroidMediaController(this, false);
        videoView.setHudView(hubView);
        //进度条监听
        seekbar.setOnSeekBarChangeListener(mSeekBarListener);
        //双击点赞功能
        love.setOnTouchListener(ClickPraise);


        listComm.setLayoutManager(new LinearLayoutManager(this));
        listComm.setAdapter(new ScreenAdapter(this));
        listComm.start();
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
        videoView.setVideoURI(Uri.parse(videoUrl));
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


    @OnClick({R.id.img_bank,R.id.img_head, R.id.tv_blues, R.id.img_focus, R.id.img_praise, R.id.img_comm, R.id.img_share, R.id.img_screen, R.id.img_select_blues,R.id.img_coll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            //进入作者首页
            case R.id.img_head:
                 setClass(AuthorDetailsActivity.class);
                 break;
            //选集
            case R.id.tv_blues:
                break;
            //关注
            case R.id.img_focus:
                break;
            //点赞
            case R.id.img_praise:
                if(isPraise){
                    isPraise=false;
                    imgPraise.setImageResource(R.mipmap.no_praise);
                }else{
                    isPraise=true;
                    imgPraise.setImageResource(R.mipmap.yes_praise);
                }
                imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                break;
            //收藏
            case R.id.img_coll:
                 imgColl.setImageResource(R.mipmap.coll_icon_yes);
                 imgColl.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                 break;
            //评论
            case R.id.img_comm:
                videoPlayPersenter.showComment();
                break;
            //转发
            case R.id.img_share:
                videoPlayPersenter.showShareDialog();
                break;
            //弹屏
            case R.id.img_screen:
                 if(listComm.getVisibility()==View.VISIBLE){
                     imgScreen.setImageResource(R.mipmap.img_screen_yes);
                     listComm.setVisibility(View.GONE);
                     listComm.stop();
                 }else{
                     listComm.setVisibility(View.VISIBLE);
                     listComm.start();
                     imgScreen.setImageResource(R.mipmap.img_screen);
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
     * 双击点赞功能
     */
    protected long exitTime = 0;
    private View.OnTouchListener ClickPraise=new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_UP){
                if ((System.currentTimeMillis() - exitTime) > 500) {
                    exitTime = System.currentTimeMillis();
                    //视频单击后
                    clickVideo(event);
                }else{
                    love.addLoveView(event.getX(),event.getY());
                    love.addLoveView(event.getX(),event.getY());
                    if(!isPraise){
                        isPraise=true;
                        imgPraise.setImageResource(R.mipmap.yes_praise);
                        imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                    }
                }
                handler.postDelayed(new Runnable() {
                    public void run() {
                        exitTime=0;
                    }
                },500);
            }
            return true;
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
            //关闭侧边栏
            case EventStatus.CLOSE_VIDEO_RIGHT:
                  drawerLayout.closeDrawer(Gravity.RIGHT);
                 break;
            default:
                break;

        }
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
