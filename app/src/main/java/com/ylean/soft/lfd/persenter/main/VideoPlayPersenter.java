package com.ylean.soft.lfd.persenter.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
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
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDanmakus;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.ui.widget.DanmakuView;

/**
 * Created by Administrator on 2020/2/8.
 */

public class VideoPlayPersenter {

    private Activity activity;
    //视频缩略图
    private Bitmap bitmap = null;

    private DanmakuContext danmakuContext;

    public VideoPlayPersenter(Activity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            return false;
        }
    });


    /**
     * 开始弹屏
     */
    public void startDanmaku(final DanmakuView danmakuView){
        danmakuView.enableDanmakuDrawingCache(true);
        danmakuView.setCallback(new DrawHandler.Callback() {
            public void prepared() {
                danmakuView.start();
                String[] content=new String[]{"高健：视频很不错，我非常喜欢","刘成：我记得我昨天看过了","范文芳：武汉加油，全中国加油，大家一起加油","张三：好饿啊，好几天没有吃饭了","李龙：今年是老鼠年，大家都像过街老鼠一样","何进：我是何进，三国之乱由我开始的","曹操：我是孟德，我才高八斗","张飞：我很勇猛的","赵云：我七进七出，救了阿斗"};
                addDanmaku(danmakuView,content,false);
            }
            public void updateTimer(DanmakuTimer timer) {

            }
            public void danmakuShown(BaseDanmaku danmaku) {

            }
            public void drawingFinished() {

            }
        });
        danmakuContext = DanmakuContext.create();
        // 设置弹幕的最大显示行数
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 2);
        // 设置是否禁止重叠
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<Integer, Boolean>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_LR, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_BOTTOM, true);

        danmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3) //设置描边样式
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f) //是否启用合并重复弹幕
                .setScaleTextSize(1.2f) //设置弹幕滚动速度系数,只对滚动弹幕有效
                .setMaximumLines(maxLinesPair) //设置最大显示行数
                .preventOverlapping(overlappingEnablePair); //设置防弹幕重叠，null为允许重叠
        danmakuView.prepare(parser, danmakuContext);
    }

    private BaseDanmakuParser parser = new BaseDanmakuParser() {
        protected IDanmakus parse() {
            return new Danmakus();
        }
    };


    /**
     * 向弹幕View中添加一条弹幕
     * @param content
     *          弹幕的具体内容
     * @param  withBorder
     *          弹幕是否有边框
     */
    public void addDanmaku(DanmakuView danmakuView, String[] content, boolean withBorder) {
        for (int i=0,len=content.length;i<len;i++){
            BaseDanmaku danmaku = danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
            danmaku.text = content[i];
            danmaku.padding = 5;
            danmaku.textSize = Util.sp2px(activity,17);
            danmaku.textColor = Color.WHITE;
            danmaku.setTime(danmakuView.getCurrentTime() + 1200);
            if (withBorder) {
                danmaku.borderColor = Color.GREEN;
            }
            danmakuView.addDanmaku(danmaku);
        }
        startDanmaku(danmakuView);
    }


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

}
