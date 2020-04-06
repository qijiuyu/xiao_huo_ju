package com.ylean.soft.lfd.persenter.recommended;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
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

import java.lang.reflect.Field;

public class RecommendedPersenter {

    private RecommendedActivity activity;
    //加载次数
    private int loadNum;
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
                          if(videoInfo.getData()!=null){
                              EventBus.getDefault().post(new EventBusType(EventStatus.FOUND_VIDEO_INFO,videoInfo.getData()));
                              loadNum++;
                              if(loadNum<4){
                                  foundVideo(0);
                              }else{
                                  loadNum=0;
                              }
                          }
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
        String DEVICE_ID=null;
        if(!MyApplication.isLogin()){
            TelephonyManager tm = (TelephonyManager)activity.getSystemService(activity.TELEPHONY_SERVICE);
            DEVICE_ID = tm.getDeviceId();
        }
        HttpMethod.foundVideo(DEVICE_ID,episodeid,handler);
    }


    /**
     * 设置侧滑菜单可以全屏滑动
     * @param drawerLayout
     */
    public void setDrawerLeftEdgeSize (DrawerLayout drawerLayout) {
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mRightDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *1)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }

}
