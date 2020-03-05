package com.ylean.soft.lfd.persenter.recommended;

import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.ylean.soft.lfd.MyApplication;
import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

public class RecommendedPersenter {

    private RecommendedActivity activity;
    public RecommendedPersenter(RecommendedActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取视频对象
                case HandlerConstant.FOUND_VIDEO_SUCCESS:
                      final VideoInfo videoInfo= (VideoInfo) msg.obj;
                      if(videoInfo==null){
                          break;
                      }
                      if(videoInfo.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.FOUND_VIDEO_INFO,videoInfo.getData()));
                      }else{
                          ToastUtil.showLong(videoInfo.getDesc());
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
     * 获取视频数据
     */
    public void foundVideo(int episodeid){
        if(MyApplication.isLogin()){
            HttpMethod.foundVideo(null,episodeid,handler);
        }else{
            TelephonyManager tm = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId();
            HttpMethod.foundVideo(DEVICE_ID,episodeid,handler);
        }
    }

}
