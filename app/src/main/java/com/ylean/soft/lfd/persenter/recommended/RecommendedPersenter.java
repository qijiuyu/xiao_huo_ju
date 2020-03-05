package com.ylean.soft.lfd.persenter.recommended;

import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;

import com.ylean.soft.lfd.activity.recommended.RecommendedActivity;
import com.zxdc.utils.library.bean.VideoInfo;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;
public class RecommendedPersenter {

    private RecommendedActivity activity;
    private VideoInfo videoInfo;

    public RecommendedPersenter(RecommendedActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
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
    private void foundVideo(){
        if(videoInfo==null){
            TelephonyManager tm = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId();
            HttpMethod.foundVideo(DEVICE_ID,0,handler);
        }else{
//            HttpMethod.foundVideo();
        }
    }

}
