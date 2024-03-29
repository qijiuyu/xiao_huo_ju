package com.ylean.soft.lfd.adapter.recommended;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.animation.Animation;
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
import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.activity.main.AuthorDetailsActivity;
import com.ylean.soft.lfd.activity.main.CommentActivity;
import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.persenter.main.VideoPlayPersenter;
import com.ylean.soft.lfd.utils.MyOnTouchListener;
import com.ylean.soft.lfd.utils.SoftKeyboardStateHelper;
import com.ylean.soft.lfd.utils.ijkplayer.media.AndroidMediaController;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.Love;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;

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
    private Animation animation;
    public FoundAdapter(RecommendedActivity activity,VideoInfo.VideoBean videoBean) {
        this.activity=activity;
        this.videoBean=videoBean;
        //实例化视频控制器
        controller=new AndroidMediaController(activity, false);
        //实例化MVP控制器
        videoPlayPersenter=new VideoPlayPersenter(activity);
        animation = AnimationUtils.loadAnimation(activity, R.anim.scale_focus);
    }

    /**
     * 滑动停止后，返回当前的页码
     */
    public void selectPosition(VideoInfo.VideoBean videoBean,int currentPosition) {
        this.videoBean=videoBean;
        this.currentPosition = currentPosition;
        //刷新
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private IjkVideoView videoView;
        private TableLayout tableLayout;
        private ProgressBar progressBar;
        private TextView tvTitle,tvBlues,tvPraise,tvFocusSerial,tvComm,tvTime;
        private CircleImageView imgHead;
        private ImageView imgFocus,imgPraise,imgColl,imgComm,imgShare,imgPlay,imgScreen,imgPlay2;
        private SeekBar seekbar;
        private AutoPollRecyclerView listComm;
        private Love love;
        private RelativeLayout relProgress,relScreen,rel;
        private LinearLayout linScreen,linSelectBlues,linPlay;
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
            tvFocusSerial=view.findViewById(R.id.tv_focusserial);
            tvComm=view.findViewById(R.id.tv_comm);
            imgShare=view.findViewById(R.id.img_share);
            imgPlay=view.findViewById(R.id.img_play);
            imgScreen=view.findViewById(R.id.img_screen);
            tvTime=view.findViewById(R.id.tv_time);
            seekbar=view.findViewById(R.id.seekbar);
            listComm=view.findViewById(R.id.list_comm);
            linSelectBlues=view.findViewById(R.id.lin_select_blues);
            love=view.findViewById(R.id.love);
            relProgress=view.findViewById(R.id.rel_progress);
            linScreen=view.findViewById(R.id.lin_screen);
            relScreen=view.findViewById(R.id.rel_screen);
            etScreen=view.findViewById(R.id.et_screen);
            imgPlay2=view.findViewById(R.id.img_play2);
            linPlay=view.findViewById(R.id.lin_play);
            rel=view.findViewById(R.id.rel);
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
        if(holder.getPosition()==currentPosition){
//            holder.setIsRecyclable(false);
            this.holder=holder;
            //播放视频
            playVideo();
            //展示视频界面数据
            showVideo();
        }else{
            holder.videoView.setVisibility(View.GONE);
            holder.imgPlay.setVisibility(View.GONE);
            holder.linScreen.setVisibility(View.VISIBLE);
            holder.relProgress.setVisibility(View.GONE);
        }
    }


    /**
     * item被回收时调用
     * @param holder
     */
    public void onViewRecycled(@NonNull ViewHolder holder) {
        this.holder=holder;
        //释放资源
        removeVideo();
        super.onViewRecycled(holder);
    }


    @Override
    public void onClick(View v) {
        if(videoBean==null){
            return;
        }
        Intent intent=new Intent();
        switch (v.getId()){
            //进入作者首页
            case R.id.img_head:
                intent.setClass(activity,AuthorDetailsActivity.class);
                intent.putExtra("id",videoBean.getUserId());
                activity.startActivity(intent);
                break;
            //关注/取消用户
            case R.id.img_focus:
                //先登录
                if(!MyApplication.isLogin()){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                videoPlayPersenter.followUser("0",videoBean.getUserId(), HandlerConstant.FOLLOW_SUCCESS);
                break;
            //点赞
            case R.id.img_praise:
                //先登录
                if(!MyApplication.isLogin()){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                videoPlayPersenter.thump(videoBean.getId());
                break;
            //关注剧情
            case R.id.img_coll:
                //先登录
                if(!MyApplication.isLogin()){
                    intent.setClass(activity,LoginActivity.class);
                    activity.startActivity(intent);
                    return;
                }
                videoPlayPersenter.followUser("1",videoBean.getSerialId(), HandlerConstant.FOLLOW_SERIAL_SUCCESS);
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
//                    holder.etScreen.requestFocus();
                }
                break;
            //选集
            case R.id.lin_select_blues:
                EventBus.getDefault().post(new EventBusType(EventStatus.SELECT_BLUES,videoBean.getSerialId()));
                activity.drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            //暂停/播放
            case R.id.img_play2:
                if(holder.videoView.isPlaying()){
                    holder.videoView.pause();
                    holder.imgPlay.setVisibility(View.VISIBLE);
                    holder.imgPlay2.setImageResource(R.mipmap.play_icon);
                }else{
                    holder.videoView.start();
                    holder.imgPlay.setVisibility(View.GONE);
                    holder.imgPlay2.setImageResource(R.mipmap.start_video);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 展示视频界面数据
     */
    private void showVideo(){
        if(videoBean==null){
            return;
        }
        holder.tvTitle.setText(videoBean.getIntroduction());
        /**
         * 是否关注用户
         */
        if(videoBean.isFollowUser()){
            holder.imgFocus.setVisibility(View.GONE);
        }else{
            holder.imgFocus.setImageResource(R.drawable.anim_focus);
            holder.imgFocus.setVisibility(View.VISIBLE);
        }
        /**
         * 是否点赞
         */
        if(videoBean.isThumbEpisode()){
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
        }else{
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
        }
        holder.tvPraise.setText(String.valueOf(videoBean.getThumbCountDesc()));
        //是否关注剧集
        if(videoBean.isFollowSerial()){
            holder.imgColl.setImageResource(R.mipmap.coll_icon_yes);
        }else{
            holder.imgColl.setImageResource(R.mipmap.coll_icon);
        }
        holder.tvFocusSerial.setText(String.valueOf(videoBean.getFollowCountDesc()));
        holder.tvComm.setText(String.valueOf(videoBean.getCommentCountDesc()));
        holder.tvBlues.setText("当前："+videoBean.getCurrentEpisode()+"集");
        holder.imgPlay.setVisibility(View.GONE);

        //用户头像
        String headUrl= HttpConstant.IP+videoBean.getUserImg();
        holder.imgHead.setTag(R.id.imageid,headUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && headUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(headUrl).into(holder.imgHead);
        }

        //获取弹屏列表
        getScreen();

        //监听弹屏输入框
        holder.etScreen.setOnEditorActionListener(screenListener);
        holder.imgHead.setOnClickListener(this);
        holder.imgPraise.setOnClickListener(this);
        holder.imgComm.setOnClickListener(this);
        holder.imgShare.setOnClickListener(this);
        holder.linSelectBlues.setOnClickListener(this);
        holder.imgColl.setOnClickListener(this);
        holder.relScreen.setOnClickListener(this);
        holder.imgFocus.setOnClickListener(this);
        holder.imgPlay2.setOnClickListener(this);

        holder.progressBar.setVisibility(View.VISIBLE);
        holder.videoView.setVisibility(View.VISIBLE);
        //屏幕点击
        holder.love.setOnTouchListener(new MyOnTouchListener(myClickCallBack));

        //监听软键盘打开还是关闭
        setListenerFotEditText(holder.rel);

        //解决SeekBar与DrawerLayout混合使用冲突问题
        holder.seekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                activity.drawerLayout.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    /**
     * 播放视频
     */
    private void playVideo(){
        if(videoBean==null){
            return;
        }
        holder.videoView.setHudView(holder.tableLayout);
        holder.videoView.setMediaController(controller);
        holder.videoView.setVideoURI(Uri.parse(videoBean.getVideourl()));
        holder.videoView.start();
        //监听视频播放完毕
        holder.videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                holder.videoView.seekTo(0);
                holder.seekbar.setProgress(0);
                holder.videoView.start();
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
                handler.removeCallbacks(runnable);
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 取得当前进度条的刻度
                int progress = seekBar.getProgress();
                if(holder.videoView.getDuration()<=0){
                    return;
                }
                // 设置当前播放的位置
                holder.videoView.seekTo(progress);
                seekBar.setProgress(holder.videoView.getCurrentPosition());
                if(!holder.videoView.isPlaying()){
                    holder.videoView.start();
                    holder.imgPlay.setVisibility(View.GONE);
                }
                handler.post(runnable);
            }
        });

        //监听错误
        holder.videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                if(i==-10000){
                    ToastUtil.showLong("视频资源出错");
                }
                return true;
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
        public void oneClick(MotionEvent event,float x) {
            clickVideo(event,x);
        }
        //双击
        public void doubleClick(MotionEvent event) {
            holder.love.addLoveView(event.getX(),event.getY());
            holder.love.addLoveView(event.getX(),event.getY());
            if(!MyApplication.isLogin()){
                return;
            }
            if(videoBean!=null && !videoBean.isThumbEpisode()){
                videoPlayPersenter.thump(videoBean.getId());
            }
        }
    };


    /**
     * 视频点击
     */
    private void clickVideo(MotionEvent event,float x){
        if(x-event.getX()>100){
            return;
        }
        int deviceHeight= Util.getDeviceWH(activity,2);
        int point=deviceHeight/3;
        if(event.getY()>point && event.getY()<(point*2)){
            //播放/暂停
            if(holder.videoView.isPlaying()){
                holder.videoView.pause();
                holder.imgPlay.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.play_anim));
                holder.imgPlay.setVisibility(View.VISIBLE);
                holder.imgPlay2.setImageResource(R.mipmap.play_icon);
            }else{
                holder.videoView.start();
                holder.imgPlay.setVisibility(View.GONE);
                holder.imgPlay2.setImageResource(R.mipmap.start_video);
            }
        }else{
            //底部布局切换
            if(holder.linScreen.getVisibility()==View.VISIBLE){
                holder.linScreen.setVisibility(View.GONE);
                holder.relProgress.setVisibility(View.VISIBLE);
                holder.linPlay.setVisibility(View.GONE);
            }else{
                holder.linScreen.setVisibility(View.VISIBLE);
                holder.relProgress.setVisibility(View.GONE);
                holder.linPlay.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 暂停及开始
     */
    public void setVideoStatus(boolean isPlay){
        if(holder!=null && holder.videoView!=null){
            if(isPlay){
                if(!holder.videoView.isPlaying()){
                    holder.videoView.start();
                    holder.imgPlay.setVisibility(View.GONE);
                    holder.imgPlay2.setImageResource(R.mipmap.start_video);
                }
            }else{
                if(holder.videoView.isPlaying()){
                    holder.videoView.pause();
                }
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
     */
    public void praiseEnd(VideoInfo.VideoBean videoBean){
        this.videoBean=videoBean;
        String praiseNum=holder.tvPraise.getText().toString().trim();
        if(videoBean.isThumbEpisode()){
            holder.imgPraise.setImageResource(R.mipmap.yes_praise);
            holder.imgPraise.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
            holder.tvPraise.setText(String.valueOf(Integer.parseInt(praiseNum)+1));
        }else{
            holder.imgPraise.setImageResource(R.mipmap.no_praise);
            holder.tvPraise.setText(String.valueOf(Integer.parseInt(praiseNum)-1));
        }
    }

    /**
     * 是否关注剧情
     */
    public void focusSerial(VideoInfo.VideoBean videoBean){
        this.videoBean=videoBean;
        String serialNum=holder.tvFocusSerial.getText().toString().trim();
        if(videoBean.isFollowSerial()){
            holder.imgColl.setImageResource(R.mipmap.coll_icon_yes);
            holder.imgColl.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.guide_scale));
            holder.tvFocusSerial.setText(String.valueOf(Integer.parseInt(serialNum)+1));
        }else{
            holder.imgColl.setImageResource(R.mipmap.coll_icon);
            holder.tvFocusSerial.setText(String.valueOf(Integer.parseInt(serialNum)-1));

            //发送取消关注剧情的eventBus
            EventBus.getDefault().post(new EventBusType(EventStatus.CANCLE_FOCUS_SERIAL,videoBean.getSerialId()));
        }
    }

    /**
     * 展示弹屏列表
     * @param screenList
     */
    public void showScreen(List<Screen.ScreenBean> screenList){
        holder.listComm.setVisibility(View.VISIBLE);
        holder.listComm.setLayoutManager(new LinearLayoutManager(activity));
        holder.listComm.setAdapter(new ScreenAdapter(activity,screenList));
        holder.listComm.start();
    }

    /**
     * 是否关注用户
     * @param videoBean
     */
    public void isFocusUser(VideoInfo.VideoBean videoBean){
        this.videoBean=videoBean;
        if(videoBean.isFollowUser()){
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.imgFocus.getDrawable();
            animationDrawable.start();
            handler.postDelayed(new Runnable() {
                public void run() {
                    holder.imgFocus.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }
                        public void onAnimationEnd(Animation animation) {
                            holder.imgFocus.setVisibility(View.GONE);
                        }
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                }
            },1500);
        }else{
            holder.imgFocus.setImageResource(R.drawable.anim_focus);
            holder.imgFocus.setVisibility(View.VISIBLE);
        }
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
                    //先登录
                    if(!MyApplication.isLogin()){
                        Intent intent=new Intent(activity,LoginActivity.class);
                        activity.startActivity(intent);
                        return false;
                    }
                    v.setText(null);
                    v.setCursorVisible(false);
                    videoPlayPersenter.sendScreen(content,videoBean);
                }
            }
            return true;
        }
    };


    /**
     * 添加浏览记录
     */
    public void addBrowse(){
        if(videoBean!=null){
            int seconds=holder.videoView.getCurrentPosition()/1000;
            if(seconds==0){
                return;
            }
            videoPlayPersenter.addBrowse(videoBean.getId(),seconds);
        }
    }


    /**
     * 获取弹屏列表
     */
    public void getScreen(){
        holder.listComm.setVisibility(View.GONE);
        videoPlayPersenter.getScreen(videoBean.getId());
    }

    /**
     * 监听软键盘打开还是关闭
     * @param view
     */
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
            }
            public void onSoftKeyboardClosed() {
                holder.etScreen.setText(null);
                holder.etScreen.setCursorVisible(false);
//                holder.etScreen.setFocusable(false);
//                holder. etScreen.setFocusableInTouchMode(false);
            }
        });
    }

}
