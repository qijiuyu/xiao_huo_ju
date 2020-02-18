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
import android.widget.ImageView;
import android.widget.ProgressBar;
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
    @BindView(R.id.img_play)
    ImageView imgPlay;
    @BindView(R.id.img_screen)
    ImageView imgScreen;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.list_comm)
    AutoPollRecyclerView listComm;
    @BindView(R.id.love)
    Love love;
    //视频控制器
    private AndroidMediaController controller;
    private String videoUrl="http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private Handler handler=new Handler();
    /**
     * true:已点赞
     * false：未点赞
     */
    private boolean isPraise=false;
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


    @OnClick({R.id.img_bank,R.id.img_head, R.id.tv_blues, R.id.img_focus, R.id.img_praise, R.id.img_comm, R.id.img_share, R.id.img_play, R.id.img_screen, R.id.img_select_blues})
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
            //评论
            case R.id.img_comm:
                videoPlayPersenter.showComment();
                break;
            //转发
            case R.id.img_share:
                videoPlayPersenter.showShareDialog();
                break;
            //播放/暂停
            case R.id.img_play:
                if(videoView.isPlaying()){
                    videoView.pause();
                    imgPlay.setImageResource(R.mipmap.play_icon);
                }else{
                    videoView.start();
                    imgPlay.setImageResource(R.mipmap.start_video);
                }
                break;
            //弹屏
            case R.id.img_screen:
                 if(listComm.getVisibility()==View.VISIBLE){
                     listComm.setVisibility(View.GONE);
                     listComm.stop();
                 }else{
                     listComm.setVisibility(View.VISIBLE);
                     listComm.start();
                 }
                break;
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
                if ((System.currentTimeMillis() - exitTime) > 1000) {
                    exitTime = System.currentTimeMillis();
                }else{
                    love.addLoveView(event.getX(),event.getY());
                    love.addLoveView(event.getX(),event.getY());
                    if(!isPraise){
                        isPraise=true;
                        imgPraise.setImageResource(R.mipmap.yes_praise);
                        imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                    }
                }
            }
            return true;
        }
    };

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
