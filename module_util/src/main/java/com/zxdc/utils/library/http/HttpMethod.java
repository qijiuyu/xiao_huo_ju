package com.zxdc.utils.library.http;

import android.os.Handler;
import android.text.TextUtils;

import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.http.base.BaseRequst;
import com.zxdc.utils.library.http.base.Http;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.SPUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HttpMethod extends BaseRequst {

    private static String size="10";
    public static int pageSize=10;

    /**
     * 获取站点
     */
//    public static void getSite(String city,final Handler handler) {
//        Http.getRetrofit().create(HttpApi.class).getSite(city).enqueue(new Callback<Site>() {
//            public void onResponse(Call<Site> call, Response<Site> response) {
//                BaseRequst.sendMessage(handler, HandlerConstant.GET_SITE_SUCCESS, response.body());
//            }
//            public void onFailure(Call<Site> call, Throwable t) {
//                BaseRequst.sendMessage(handler, HandlerConstant.REQUST_ERROR, t.getMessage());
//            }
//        });
//    }
//
//
//
//
//
//    /**
//     * 绑定手机号
//     */
//    public static void bingMobile(String mobile,String password,String smscode,final Handler handler) {
//        Map<String,String> map=new HashMap<>();
//        map.put("mobile",mobile);
//        map.put("password",password);
//        map.put("smscode",smscode);
//        Http.getRetrofit().create(HttpApi.class).bingMobile(map).enqueue(new Callback<BaseBean>() {
//            public void onResponse(Call<BaseBean> call, Response<BaseBean> response) {
//                BaseRequst.sendMessage(handler, HandlerConstant.BING_MOBILE_SUCCESS, response.body());
//            }
//            public void onFailure(Call<BaseBean> call, Throwable t) {
//                BaseRequst.sendMessage(handler, HandlerConstant.REQUST_ERROR, t.getMessage());
//            }
//        });
//    }
}
