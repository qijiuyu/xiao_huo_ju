package com.ylean.soft.lfd.persenter.recommended;

import android.app.Activity;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.ylean.soft.lfd.adapter.main.RecommendedAdapter;
import com.ylean.soft.lfd.adapter.main.ScreenAdapter;
import com.ylean.soft.lfd.view.AutoPollRecyclerView;
import com.ylean.soft.lfd.view.MySurfaceView;
import com.zxdc.utils.library.util.Util;

/**
 * Created by Administrator on 2020/2/13.
 */

public class VideoAdapterPersenter {

    private Activity activity;

    public VideoAdapterPersenter(Activity activity){
        this.activity=activity;
    }

    /**
     * 设置surfaceView的宽高比列
     * @param mySurfaceView
     * @param mediaPlayer
     */
    public void setSurFaceView(MySurfaceView mySurfaceView, MediaPlayer mediaPlayer){
        //获取屏幕的宽高
        int screenWidth = Util.getDeviceWH(activity,1);
        int screenHeight = Util.getDeviceWH(activity,2);
        int myVideoWidth = mediaPlayer.getVideoWidth();
        //视频高度
        int myVideoHight =mediaPlayer.getVideoHeight();
        //获取拉伸的宽度比例
        float scale_width = screenWidth/myVideoWidth;
        //获取拉伸高度的比例
        float scale_hight = screenHeight/myVideoHight;
        float mVideoWidth = myVideoWidth*scale_hight;
        mySurfaceView.getHolder().setFixedSize((int)mVideoWidth,screenHeight);
        mySurfaceView.setMeasure(mVideoWidth,screenHeight);
        mySurfaceView.requestLayout();
    }


    /**
     * 显示视频播放时长
     */
    public void showVideoTime(MediaPlayer mediaPlayer, TextView tvTime){
        int time=mediaPlayer.getDuration()/1000;
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
     * item滑动销毁后
     */
    public void removeVideo(MediaPlayer mediaPlayer, AutoPollRecyclerView listComm){
        //清除视频
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        //关闭弹屏
        listComm.stop();
    }
}
