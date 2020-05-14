package com.ylean.soft.lfd;

import android.text.TextUtils;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.zxdc.utils.library.base.BaseApplication;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.error.CockroachUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2020/2/5.
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);

        //开启小强
        CockroachUtil.install();

        //初始化友盟分享
        initShare();

        //初始化消息推送
        initPush();
    }


    /**
     * 初始化友盟分享
     */
    private void initShare(){
        UMConfigure.setLogEnabled(true);
        //初始化
        UMConfigure.init(this,"5ebbf8f3dbc2ec07f77a0455","Android",UMConfigure.DEVICE_TYPE_PHONE,"");
        //微信
        PlatformConfig.setWeixin("wx43eb89624326c53e", "22476afa67667baf5d2e9a99e55a0106");
        //QQ
        PlatformConfig.setQQZone("101862530", "a3ced0c83f1f2f2e796c6a08efcffbbc");

        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
    }


    /**
     * 初始化消息推送
     */
    private void initPush(){
        //设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(true);
        //初始化 JPush
        JPushInterface.init(this);
    }


    /**
     * 判断是否登录过
     * @return
     */
    public static boolean isLogin(){
        final String token= SPUtil.getInstance(getContext()).getString(SPUtil.TOKEN);
        if(!TextUtils.isEmpty(token)){
            return true;
        }
        return false;
    }
}
