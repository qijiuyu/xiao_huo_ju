package com.ylean.soft.lfd.adapter.recommended;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.ylean.soft.lfd.activity.main.CommentActivity;
import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.utils.MyOnTouchListener;
import com.ylean.soft.lfd.utils.ijkplayer.media.AndroidMediaController;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;

import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class FoundAdapter extends RecyclerView.Adapter<FoundAdapter.ViewHolder> implements View.OnClickListener{
    private RecommendedActivity activity;
    public ViewHolder holder;
    //当前的页码
    private int currentPosition = 0;
    //视频控制器
    private AndroidMediaController controller;
    //实例化MVP对象
    public VideoPlayPersenter videoPlayPersenter;
    private Handler handler=new Handler();
    //视频详情对象
    private VideoInfo.VideoBean videoBean;
    public FoundAdapter(RecommendedActivity activity,VideoInfo.VideoBean videoBean) {
        this.activity=activity;
        this.videoBean=videoBean;
        //实例化视频控制器
        controller=new AndroidMediaController(activity, false);
        //实例化MVP控制器
        videoPlayPersenter=new VideoPlayPersenter(activity);
    }

    /**
     * 滑动停止后，返回当前的页码
     */
    public void selectPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        //刷新
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private IjkVideoView videoView;
        private TableLayout tableLayout;
        private ProgressBar progressBar;
        private TextView tvTitle,tvBlues,tvPraise,tvComm,tvShare,tvTime;
        private CircleImageView imgHead;
        private ImageView imgFocus,imgPraise,imgColl,imgComm,imgShare,imgPlay,imgScreen,imgSelectBlues;
        private SeekBar seekbar;
        private AutoPollRecyclerView listComm;
        private Love love;
        private RelativeLayout relProgress,relScreen;
        private LinearLayout linScreen;
        private EditText etScreen;
        public ViewHolder(View view) {
            super(view);
            videoView=view.findViewById(R.id.videoView);
            tableLayout=view.findViewById(R.id.hud_view);
            progressBar=view.findViewById(R.id.pb_progressbar);
            tvTitle=view.findViewById(R.id.tv_title);
            tvBlues=view.findViewById(R.id.tv_blues);
            imgHead=view.findViewById(R.id.img_head);
            imgFocus=view.findViewById(R.id.img_focus);
            imgPraise=view.findViewById(R.id.img_praise);
            tvPraise=view.findViewById(R.id.tv_praise);
            imgComm=view.findViewById(R.id.img_comm);
            imgColl=view.findViewById(R.id.img_coll);
            tvComm=view.findViewById(R.id.tv_comm);
            imgShare=view.findViewById(R.id.img_share);
            tvShare=view.findViewById(R.id.tv_share);
            imgPlay=view.findViewById(R.id.img_play);
            imgScreen=view.findViewById(R.id.img_screen);
            tvTime=view.findViewById(R.id.tv_time);
            seekbar=view.findViewById(R.id.seekbar);
            listComm=view.findViewById(R.id.list_comm);
            imgSelectBlues=view.findViewById(R.id.img_select_blues);
            love=view.findViewById(R.id.love);
            relProgress=view.findViewById(R.id.rel_progress);
            linScreen=view.findViewById(R.id.lin_screen);
            relScreen=view.findViewById(R.id.rel_screen);
            etScreen=view.findViewById(R.id.et_screen);
        }
    }

    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_video, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(videoBean==null){
            return;
        }
        this.holder=holder;
        if(holder.getPosition()==currentPosition){
            //播放视频
            playVideo();
            holder.tvPraise.setText(String.valueOf(videoBean.getEpisodeCount()));
            if(videoBean.isFollowUser()){
                holder.imgFocus.setVisibility(View.GONE);
            }else{
                holder.imgFocus.setVisibility(View.VISIBLE);
            }
            holder.tvTitle.setText("剧情："+videoBean.getIntroduction());
            if(videoBean.isThumbEpisode()){
                holder.imgPraise.setImageResource(R.mipmap.yes_praise);
            }else{
                holder.imgPraise.setImageResource(R.mipmap.no_praise);
            }
            //获取弹屏列表
            holder.listComm.setVisibility(View.GONE);
            videoPlayPersenter.getScreen(videoBean.getId());

            //监听弹屏输入框
            holder.etScreen.setOnEditorActionListener(screenListener);
            holder.imgHead.setOnClickListener(this);
            holder.imgPraise.setOnClickListener(this);
            holder.imgComm.setOnClickListener(this);
            holder.imgShare.setOnClickListener(this);
            holder.imgSelectBlues.setOnClickListener(this);
            holder.imgColl.setOnClickListener(this);
            holder.relScreen.setOnClickListener(this);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            //屏幕点击
            holder.love.setOnTouchListener(new MyOnTouchListener(myClickCallBack));
        }else{
            holder.videoView.setVisibility(View.GONE);
        }
    }


    /**
     * item被回收时调用
     * @param holder
     */
    public void onViewRecycled(@NonNull ViewHolder holder) {
        this.holder=holder;
        removeVideo();
        super.onViewRecycled(holder);
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()){
            //进入作者首页
            case R.id.img_head:
                intent.setClass(activity,AuthorDetailsActivity.class);
                activity.startActivity(intent);
                break;
            //点赞
            case R.id.img_praise:
                 videoPlayPersenter.thump(videoBean.getSerialId());
                break;
            //收藏
            case R.id.img_coll:
                 holder.imgColl.setImageResource(R.mipmap.coll_icon_yes);
                 holder.imgColl.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                 break;
            //评论
            case R.id.img_comm:
                intent.setClass(activity,CommentActivity.class);
                intent.putExtra("videoBean",videoBean);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.activity_open,0);
                break;
            //转发
            case R.id.img_share:
                videoPlayPersenter.showShareDialog();
                break;
            //弹屏
            case R.id.rel_screen:
                if(holder.listComm.getVisibility()==View.VISIBLE){
                    holder.imgScreen.setImageResource(R.mipmap.img_screen);
                    holder.listComm.setVisibility(View.GONE);
                    holder.listComm.stop();
                    holder.etScreen.setHint("弹屏已关闭");
                    holder.etScreen.setFocusable(false);
                    holder. etScreen.setFocusableInTouchMode(false);
                }else{
                    holder.listComm.setVisibility(View.VISIBLE);
                    holder.listComm.start();
                    holder.imgScreen.setImageResource(R.mipmap.img_screen_yes);
                    holder.etScreen.setHint("发个弹幕冒个泡～");
                    holder.etScreen.setFocusableInTouchMode(true);
                    holder.etScreen.setFocusable(true);
                    holder.etScreen.requestFocus();
                }
                  break;
            //选集
            case R.id.img_select_blues:
                activity.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            default:
                break;
        }
    }


    /**
     * 播放视频
     */
    private void playVideo(){
        holder.videoView.setHudView(holder.tableLayout);
        holder.videoView.setMediaController(controller);
        holder.videoView.setVideoURI(Uri.parse("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4"));
        holder.videoView.start();
        //监听视频播放完毕
        holder.videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                holder.videoView.seekTo(0);
            }
        });

        //监听视频加载完成
        holder.videoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                //显示视频播放时长
                videoPlayPersenter.showVideoTime(holder.videoView, holder.tvTime);
                //设置播放进度条的最大值
                holder.seekbar.setMax(holder.videoView.getDuration());
                //动态更新播放进度
                handler.postDelayed(runnable, 0);
                //隐藏进度圈
                holder.progressBar.setVisibility(View.GONE);
            }
        });

        //进度条监听
        holder.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                if (holder.videoView.isPlaying()) {
                    // 设置当前播放的位置
                    holder.videoView.seekTo(progress);
                }
            }
        });
    }


    /**
     * 动态更新播放进度
     */
    private Runnable runnable = new Runnable() {
        public void run() {
            if (holder.videoView.isPlaying()) {
                holder.seekbar.setProgress(holder.videoView.getCurrentPosition());
            }
            handler.postDelayed(runnable, 1000);
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
            holder.love.addLoveView(event.getX(),event.getY());
            holder.love.addLoveView(event.getX(),event.getY());
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
            holder.imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
        }
    };


    /**
     * 视频点击
     */
    private void clickVideo(MotionEvent event){
        int deviceHeight= Util.getDeviceWH(activity,2);
        int point=deviceHeight/3;
        if(event.getY()>point && event.getY()<(point*2)){
            //播放/暂停
            if(holder.videoView.isPlaying()){
                holder.videoView.pause();
                holder.imgPlay.setVisibility(View.VISIBLE);
            }else{
                holder.videoView.start();
                holder.imgPlay.setVisibility(View.GONE);
            }
        }else{
            //底部布局切换
            if(holder.linScreen.getVisibility()==View.VISIBLE){
                holder.linScreen.setVisibility(View.GONE);
                holder.relProgress.setVisibility(View.VISIBLE);
            }else{
                holder.linScreen.setVisibility(View.VISIBLE);
                holder.relProgress.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 暂停及开始
     */
    public void setVideoStatus(){
        if(holder!=null && holder.videoView!=null){
            if(holder.videoView.isPlaying()){
                holder.videoView.pause();
            }else{
                holder.videoView.start();
                holder.imgPlay.setVisibility(View.GONE);
            }
        }
    }


    /**
     * item滑动销毁后
     */
    public void removeVideo(){
        //清除视频资源
        holder.videoView.stopPlayback();
        //关闭弹屏
        if(holder.listComm!=null){
            holder.listComm.stop();
        }
    }


    /**
     * true：点赞成功
     * false：取消点赞
     * @param status
     */
    public void praiseEnd(boolean status){
        if(status){
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
            holder.imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
        }else{
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }
    }

    /**
     * 展示弹屏列表
     * @param screenList
     */
    public void showScreen(List<Screen.ScreenBean> screenList){
        holder.listComm.setVisibility(View.VISIBLE);
        ScreenAdapter screenAdapter=new ScreenAdapter(activity,screenList);
        holder.listComm.setLayoutManager(new LinearLayoutManager(activity));
        holder.listComm.setAdapter(screenAdapter);
        holder.listComm.start();
    }


    /**
     * 监听弹屏输入框
     */
    private TextView.OnEditorActionListener screenListener=new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                String content=v.getText().toString().trim();
                if(TextUtils.isEmpty(content)){
                    ToastUtil.showLong("请输入弹屏内容");
                }else{
                    videoPlayPersenter.sendScreen(content,videoBean);
                    holder.etScreen.setText(null);
                }
            }
            return false;
        }
    };

    public void setVideoBean(VideoInfo.VideoBean videoBean){
        this.videoBean=videoBean;
    }
}
