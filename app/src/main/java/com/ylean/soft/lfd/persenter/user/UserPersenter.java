package com.ylean.soft.lfd.persenter.user;

import android.os.Handler;
import android.os.Message;

import com.ylean.soft.lfd.activity.user.UserActivity;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.ToastUtil;

/**
 * Created by Administrator on 2020/2/22.
 */

public class UserPersenter {

    private UserActivity activity;

    public UserPersenter(UserActivity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取用户信息
                case HandlerConstant.GET_USER_SUCCESS:
                     final UserInfo userInfo= (UserInfo) msg.obj;
                     if(userInfo==null){
                         break;
                     }
                     if(userInfo.isSussess()){
                         //展示用户数据
                         activity.showUserInfo(userInfo.getData());
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
     * 获取用户信息
     */
    public void getUser(){
        HttpMethod.getUser(handler);
    }
}
