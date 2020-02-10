package com.ylean.soft.lfd.persenter.main;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.adapter.main.CommentAdapter;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.view.MyRefreshLayout;

/**
 * Created by Administrator on 2020/2/8.
 */

public class VideoPlayPersenter {

    private VideoPlayActivity activity;

    public VideoPlayPersenter(VideoPlayActivity activity){
        this.activity=activity;
    }


    /**
     * 根据屏幕宽高改变视频分辨率
     */
    public void updateSurfaceSize(){
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        int myVideoWidth = activity.mMediaPlayer.getVideoWidth();
        //视频高度
        int myVideoHight = activity.mMediaPlayer.getVideoHeight();
        //获取拉伸的宽度比例
        float scale_width = screenWidth/myVideoWidth;
        //获取拉伸高度的比例
        float scale_hight = screenHeight/myVideoHight;
        float mVideoWidth = myVideoWidth*scale_hight;
        activity.surfaceView.getHolder().setFixedSize((int)mVideoWidth,screenHeight);
        activity.surfaceView.setMeasure(mVideoWidth,screenHeight);
        activity.surfaceView.requestLayout();
    }

    /**
     * 展示分享弹框
     */
    public void showShareDialog(){
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        final PopupWindow popupWindow = DialogUtil.showPopWindow(view);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
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
}
