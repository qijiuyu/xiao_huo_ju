package com.ylean.soft.lfd.persenter.main;

import android.os.Handler;
import android.os.Message;

import com.ylean.soft.lfd.activity.main.MainActivity;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2020/3/3.
 */

public class MainPersenter {

    private MainActivity activity;

    public MainPersenter(MainActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取频道列表
                case HandlerConstant.GET_CHANNEL_SUCCESS:
                      final Tag tag= (Tag) msg.obj;
                      if(tag==null){
                          break;
                      }
                      if(tag.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_CHANNEL,tag.getData()));
                      }else{
                          ToastUtil.showLong(tag.getDesc());
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
     * 获取频道列表
     */
    public void channel(){
        DialogUtil.showProgress(activity,"数据加载中");
        HttpMethod.channel("0",handler);
    }

}
