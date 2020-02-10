package com.ylean.soft.lfd.activity.main;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.MySurfaceView;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.StatusBarUtils;
import com.zxdc.utils.library.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 播放视频
 * Created by Administrator on 2020/2/6.
 */

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.surfaceView)
    public MySurfaceView surfaceView;
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
    //播放器
    public MediaPlayer mMediaPlayer;
    /**
     * true：在播放
     * false：暂停
     */
    private boolean isPlay;
    private String videoUrl="http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4";
    private Handler handler=new Handler();
    private VideoPlayPersenter videoPlayPersenter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_videoview);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        initView();
        rightMenu();
    }

    /**
     * 初始化
     */
    private void initView(){
        //实例化MVP对象
        videoPlayPersenter=new VideoPlayPersenter(this);
        //创建MediaPlayer对象
        mMediaPlayer = new MediaPlayer();
        surfaceView.setKeepScreenOn(true);
        surfaceView.getHolder().addCallback(mCallback);
        seekbar.setOnSeekBarChangeListener(mSeekBarListener);


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
                if(mMediaPlayer.isPlaying()){
                    mMediaPlayer.pause();
                    imgPlay.setImageResource(R.mipmap.play_icon);
                }else{
                    mMediaPlayer.start();
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


    private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback(){
        public void surfaceCreated(SurfaceHolder holder) {
            new Thread(new Runnable() {
                public void run() {
                    //播放视频
                    playVideo();
                }
            }).start();
        }
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaPlayer.setDisplay(holder);
        }
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                pbProgressbar.setVisibility(View.VISIBLE);
            }
        }
    };


    /**
     * 播放视频
     */
    private void playVideo() {
        try {
            mMediaPlayer.reset(); // 重置
            mMediaPlayer.setDataSource(videoUrl);
            //把视频画面输出到SurfaceView中
            mMediaPlayer.setDisplay(surfaceView.getHolder());
            //当装载流媒体完毕的时候回调
            mMediaPlayer.setOnPreparedListener(new PreparedListener());
            //监听视频播放完成
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.seekTo(0);
                    seekbar.setProgress(0);
                    imgPlay.setImageResource(R.mipmap.play_icon);
                }
            });
            //设置全屏播放视频不会变形
            mMediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            //同步的方式装载流媒体文件
            mMediaPlayer.prepare();
            //设置播放进度条的最大值
            seekbar.setMax(mMediaPlayer.getDuration());
            //动态更新播放进度
            handler.postDelayed(runnable, 0);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 动态更新播放进度
     */
    private Runnable runnable = new Runnable() {
        public void run() {
            if (mMediaPlayer.isPlaying()) {
                seekbar.setProgress(mMediaPlayer.getCurrentPosition());
            }
            handler.postDelayed(runnable, 1000);
        }
    };


    /**
     * 当装载流媒体完毕的时候回调
     */
    class PreparedListener implements MediaPlayer.OnPreparedListener {
        public void onPrepared(MediaPlayer mp) {
            //根据屏幕宽高改变视频分辨率
            videoPlayPersenter.updateSurfaceSize();
            //显示视频播放时长
            showVideoTime();
            pbProgressbar.setVisibility(View.GONE); // 取消dialog
            //开始播放
            mMediaPlayer.start();
        }
    }


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
            if (mMediaPlayer.isPlaying()) {
                // 设置当前播放的位置
                mMediaPlayer.seekTo(progress);
            }
        }
    };

    /**
     * 显示视频播放时长
     */
    private void showVideoTime(){
        int time=mMediaPlayer.getDuration()/1000;
        final int hoursInt = time / 3600;
        final int minutesInt = (time - hoursInt * 3600) / 60;
        final int secondsInt = time - hoursInt * 3600 - minutesInt * 60;
        if(hoursInt==0){
            tvTime.setText(String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt));
        } else{
            tvTime.setText(String.format("%02d", hoursInt) + ":" + String.format("%02d", minutesInt) + ":" + String.format("%02d", secondsInt));
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
