package com.ylean.soft.lfd;

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
        UMConfigure.init(this,"5dfc65a30cafb2cfa300053e","Android",UMConfigure.DEVICE_TYPE_PHONE,"");
        //微信
        PlatformConfig.setWeixin("wx41f56978f20ce2fe", "696afe62e1a457cf0440cd30673b90d3");
        //QQ
        PlatformConfig.setQQZone("101838160", "3192d2ae301f12f59cd63a99283baa6f");

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
}
