package com.zxdc.utils.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
/**
 * Created by Administrator on 2017/3/23 0023.
 */
public class BaseFragment extends Fragment {

    protected Activity mActivity;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    protected void setClass(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), cls);
        startActivity(intent);
    }


    /**
     * 删除handler中的消息
     * @param mHandler
     */
    public void removeHandler(Handler mHandler){
        if(null!=mHandler){
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void onDetach(){
        super.onDetach();
        mActivity = null;
    }
}
