package com.ylean.soft.lfd.adapter.recommended;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.utils.ijkplayer.media.AndroidMediaController;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class FoundAdapter extends RecyclerView.Adapter<FoundAdapter.ViewHolder> implements View.OnClickListener{
    private RecommendedActivity activity;
    private ViewHolder holder;
    //当前的页码
    private int currentPosition = 0;
    private List<String> list=new ArrayList<>();
    //视频控制器
    private AndroidMediaController controller;
    //实例化MVP对象
    public VideoPlayPersenter videoPlayPersenter;
    private Handler handler=new Handler();
    public FoundAdapter(RecommendedActivity activity) {
        this.activity=activity;
        //实例化视频控制器
        controller=new AndroidMediaController(activity, false);
        //实例化MVP控制器
        videoPlayPersenter=new VideoPlayPersenter(activity);

        list.add("http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4");
        list.add("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
        list.add("http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4");
        list.add("http://flashmedia.eastday.com/newdate/news/2016-11/shznews1125-19.mp4");
        list.add("http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4");
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
        private TextView tvBlues,tvPraise,tvComm,tvShare,tvTime;
        private CircleImageView imgHead;
        private ImageView imgFocus,imgPraise,imgComm,imgShare,imgPlay,imgScreen,imgSelectBlues;
        private SeekBar seekbar;
        private AutoPollRecyclerView listComm;
        private Love love;
        public ViewHolder(View view) {
            super(view);
            videoView=view.findViewById(R.id.videoView);
            tableLayout=view.findViewById(R.id.hud_view);
            progressBar=view.findViewById(R.id.pb_progressbar);
            tvBlues=view.findViewById(R.id.tv_blues);
            imgHead=view.findViewById(R.id.img_head);
            imgFocus=view.findViewById(R.id.img_focus);
            imgPraise=view.findViewById(R.id.img_praise);
            tvPraise=view.findViewById(R.id.tv_praise);
            imgComm=view.findViewById(R.id.img_comm);
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
        }
    }

    public int getItemCount() {
        return list.size();
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_video, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        this.holder=holder;
        if(holder.getPosition()==currentPosition){
            //播放视频
            playVideo();
            //显示弹屏
            videoPlayPersenter.showScreen(holder.listComm);
            //双击点赞功能
            holder.love.setOnTouchListener(ClickPraise);
            holder.imgHead.setOnClickListener(this);
            holder.imgPraise.setOnClickListener(this);
            holder.imgComm.setOnClickListener(this);
            holder.imgShare.setOnClickListener(this);
            holder.imgSelectBlues.setOnClickListener(this);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
        }else{
            holder.videoView.setVisibility(View.GONE);
        }
        String tag=holder.imgPraise.getTag().toString();
        if(tag.equals("1")){
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }else{
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
        }
    }


    /**
     * item被回收时调用
     * @param holder
     */
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        removeVideo();
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
                String tag=v.getTag().toString();
                if(tag.equals("1")){
                    v.setTag("0");
                    holder.imgPraise.setImageResource(R.mipmap.no_praise);
                }else{
                    v.setTag("1");
                    holder.imgPraise.setImageResource(R.mipmap.yes_praise);
                }
                holder.imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                break;
            //评论
            case R.id.img_comm:
                videoPlayPersenter.showComment();
                break;
            //转发
            case R.id.img_share:
                videoPlayPersenter.showShareDialog();
                break;
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
        holder.videoView.setVideoURI(Uri.parse(list.get(currentPosition)));
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
     * 双击点赞功能
     */
    protected long exitTime = 0;
    private View.OnTouchListener ClickPraise=new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction()==MotionEvent.ACTION_UP){
                if ((System.currentTimeMillis() - exitTime) > 1000) {
                    exitTime = System.currentTimeMillis();
                }else{
                    holder.love.addLoveView(event.getX(),event.getY());
                    holder.love.addLoveView(event.getX(),event.getY());
                    String tag=holder.imgPraise.getTag().toString();
                    if(tag.equals("0")){
                        v.setTag("1");
                        holder.imgPraise.setImageResource(R.mipmap.yes_praise);
                        holder.imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
                    }
                }
            }
            return true;
        }
    };


    /**
     * 暂停及开始
     */
    public void setVideoStatus(){
        if(holder!=null && holder.videoView!=null){
            if(holder.videoView.isPlaying()){
                holder.videoView.pause();
            }else{
                holder.videoView.start();
            }
        }
    }


    /**
     * item滑动销毁后
     */
    public void removeVideo(){
        //清除视频资源
        if(holder.videoView!=null){
            holder.videoView.stopPlayback();
        }
        //关闭弹屏
        if(holder.listComm!=null){
            holder.listComm.stop();
        }
    }

}
