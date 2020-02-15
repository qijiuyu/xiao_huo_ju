package com.ylean.soft.lfd.persenter.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.CommentAdapter;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.utils.ThreadPoolUtil;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.Util;
import com.zxdc.utils.library.view.MyRefreshLayout;
import java.util.HashMap;

/**
 * Created by Administrator on 2020/2/8.
 */

public class VideoPlayPersenter {

    private Activity activity;
    //视频缩略图
    private Bitmap bitmap = null;

    public VideoPlayPersenter(Activity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            return false;
        }
    });

    /**
     * 展示分享弹框
     */
    public void showShareDialog(){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        final PopupWindow popupWindow = DialogUtil.showPopWindow(view);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.tv_wx).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.tv_pyq).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.tv_kj).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }


    /**
     * 展示评论弹框
     */
    public void showComment(){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_comment, null);
        final PopupWindow popupWindow = DialogUtil.showPopWindow(view);
        popupWindow.setAnimationStyle(R.style.take_photo_anim);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        //点击空白处关闭
        view.findViewById(R.id.rel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.img_close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView tvTotal=view.findViewById(R.id.tv_total);
        MyRefreshLayout myRefreshLayout=view.findViewById(R.id.re_list);
        myRefreshLayout.setPullDownRefreshEnable(false);
        //显示评论列表
        ListView listView=view.findViewById(R.id.listView);
        CommentAdapter commentAdapter=new CommentAdapter(activity);
        listView.setAdapter(commentAdapter);
        listView.setDivider(null);
    }

    /**
     * 显示视频播放时长
     */
    public void showVideoTime(IjkVideoView videoView, TextView tvTime){
        int time=videoView.getDuration()/1000;
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
     * 显示弹屏
     */
    public void showScreen(AutoPollRecyclerView listComm){
        listComm.setLayoutManager(new LinearLayoutManager(activity));
        listComm.setAdapter(new ScreenAdapter(activity));
        listComm.start();
    }


    /**
     * 获取视频文件截图
     * @return Bitmap 返回获取的Bitmap
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void createVideoThumbnail(final String url, final ImageView imageView) {
        ThreadPoolUtil.execute(new Runnable() {
            public void run() {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                int kind = MediaStore.Video.Thumbnails.MINI_KIND;
                try {
                    if (Build.VERSION.SDK_INT >= 14) {
                        retriever.setDataSource(url, new HashMap<String, String>());
                    } else {
                        retriever.setDataSource(url);
                    }
                    bitmap = retriever.getFrameAtTime();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        retriever.release();
                    } catch (RuntimeException ex) {
                    }
                }
                if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, Util.getDeviceWH(activity,1), Util.getDeviceWH(activity,2),ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                }
               handler.post(new Runnable() {
                   public void run() {
                       imageView.setImageBitmap(bitmap);
                   }
               });
            }
        });
    }



    /**
     * item滑动销毁后
     */
    public void removeVideo(IjkVideoView videoView, AutoPollRecyclerView listComm){
        //清除视频资源
        videoView.stopPlayback();
        //关闭弹屏
        listComm.stop();
        //释放缩略图内存
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }
    }
}
