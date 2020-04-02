package com.ylean.soft.lfd.persenter.main;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.VideoPlayActivity;
import com.ylean.soft.lfd.utils.ijkplayer.media.IjkVideoView;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;
import org.greenrobot.eventbus.EventBus;
public class VideoPlayPersenter {

    private Activity activity;

    public VideoPlayPersenter(Activity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            BaseBean baseBean=null;
            switch (msg.what){
                //视频详情回执
                case HandlerConstant.GET_VIDEO_INFO_SUCCESS:
                      final VideoInfo videoInfo= (VideoInfo) msg.obj;
                      if(videoInfo==null){
                          break;
                      }
                      if(videoInfo.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_VIDEO_INFO,videoInfo.getData()));
                      }else{
                          ToastUtil.showLong(videoInfo.getDesc());
                      }
                      break;
                //关注、取消关注用户
                case HandlerConstant.FOLLOW_SUCCESS:
                      baseBean= (BaseBean) msg.obj;
                      if(baseBean==null){
                          break;
                      }
                      if(baseBean.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.IS_FOLLOW));
                      }
                      break;
                //关注、取消关注剧集
                case HandlerConstant.FOLLOW_SERIAL_SUCCESS:
                     baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.FOCUS_SERIAL));
                     }
                      break;
                //点赞、取消点赞
                case HandlerConstant.THUMP_SUCCESS:
                     baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.IS_THUMP));
                     }
                      break;
                //发送弹屏
                case HandlerConstant.SEND_SCREEN_SUCCESS:
                     baseBean= (BaseBean) msg.obj;
                     if(baseBean==null){
                         break;
                     }
                     if(baseBean.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.SEND_SCREEN));
                     }
                     ToastUtil.showLong(baseBean.getDesc());
                      break;
                //获取弹屏列表
                case HandlerConstant.GET_SCREEN_SUCCESS:
                      Screen screen= (Screen) msg.obj;
                      if(screen==null){
                          break;
                      }
                      if(screen.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.GET_SCREEEN,screen.getData()));
                      }
                      break;
                case HandlerConstant.REQUST_ERROR:
                    ToastUtil.showLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 展示分享弹框
     */
    Animation animation=null;
    public void showShareDialog(){
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_share, null);
        final PopupWindow popupWindow = DialogUtil.showPopWindow(view);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        animation=new TranslateAnimation(0,0,500,0);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);

        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeShare(view,popupWindow);
            }
        });
        view.findViewById(R.id.tv_wx).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeShare(view,popupWindow);
                if(activity instanceof VideoPlayActivity){
                    ((VideoPlayActivity)activity).startShare(SHARE_MEDIA.WEIXIN);
                    return;
                }
                EventBus.getDefault().post(new EventBusType(EventStatus.SHARE_APP,  SHARE_MEDIA.WEIXIN));
            }
        });
        view.findViewById(R.id.tv_pyq).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeShare(view,popupWindow);
                if(activity instanceof VideoPlayActivity){
                    ((VideoPlayActivity)activity).startShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                    return;
                }
                EventBus.getDefault().post(new EventBusType(EventStatus.SHARE_APP, SHARE_MEDIA.WEIXIN_CIRCLE));
            }
        });
        view.findViewById(R.id.tv_qq).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeShare(view,popupWindow);
                if(activity instanceof VideoPlayActivity){
                    ((VideoPlayActivity)activity).startShare(SHARE_MEDIA.QQ);
                    return;
                }
                EventBus.getDefault().post(new EventBusType(EventStatus.SHARE_APP, SHARE_MEDIA.QQ));
            }
        });
        view.findViewById(R.id.tv_kj).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                closeShare(view,popupWindow);
                if(activity instanceof VideoPlayActivity){
                    ((VideoPlayActivity)activity).startShare(SHARE_MEDIA.QZONE);
                    return;
                }
                EventBus.getDefault().post(new EventBusType(EventStatus.SHARE_APP, SHARE_MEDIA.QZONE));
            }
        });
    }


    private void closeShare(View view, final PopupWindow popupWindow){
        animation=new TranslateAnimation(0,0,0,500);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }
            public void onAnimationEnd(Animation animation) {
                popupWindow.dismiss();
            }
            public void onAnimationRepeat(Animation animation) {

            }
        });
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
     * 获取视频详情
     */
    public void videoInfo(int singleId,int serialId){
        DialogUtil.showProgress(activity,"视频加载中");
        HttpMethod.videoInfo(singleId,serialId,handler);
    }


    /**
     * 关注、取消关注用户
     */
    public void followUser(String type,int relateid,int index){
        HttpMethod.follow(relateid,type,index,handler);
    }


    /**
     * 点赞、取消点赞
     */
    public void thump(int serialId){
        HttpMethod.thump(serialId,handler);
    }


    /**
     * 发送弹屏
     */
    public void sendScreen(String content,VideoInfo.VideoBean videoBean){
        DialogUtil.showProgress(activity,"发送中");
        HttpMethod.sendScreen(content,videoBean.getId(),videoBean.getSeconds(),handler);
    }


    /**
     * 获取弹屏列表
     */
    public void getScreen(int id){
        HttpMethod.getScreen(id,handler);
    }


    /**
     * 添加浏览记录
     */
    public void addBrowse(int episodeid,int seconds){
        HttpMethod.addBrowse(episodeid,seconds,handler);
    }

}
