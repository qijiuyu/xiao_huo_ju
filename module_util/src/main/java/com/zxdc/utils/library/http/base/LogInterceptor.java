package com.zxdc.utils.library.http.base;

import android.text.TextUtils;

import com.zxdc.utils.library.base.BaseApplication;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpApi;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
/**
 * HTTP拦截器
 * Created by lyn on 2017/4/13.
 */
public class LogInterceptor implements Interceptor {

    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        if (request.method().equals("POST")) {
            request = addPostParameter(request);
        }else{
            request = addGetParameter(request);
        }
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        String body = response.body().string();
        //如果ACCESS_TOKEN失效，自动重新获取一次
        final int code = getCode(body);
        if(code==-401){
            Login login=refreshToken();
            if(login!=null && login.isSussess()){
                SPUtil.getInstance(BaseApplication.getContext()).addString(SPUtil.TOKEN,login.getData().getToken());
                request = addPostParameter(request);
                response = chain.proceed(request);
                body = response.body().string();
            }
        }
        try {
            if(!TextUtils.isEmpty(body)){
                JSONObject jsonObject=new JSONObject(body);
                if(!jsonObject.isNull("data") && jsonObject.getString("data").equals("")){
                    jsonObject.put("data",null);
                    body=jsonObject.toString();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        LogUtils.e(String.format("response %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, body));
        return response.newBuilder().body(ResponseBody.create(response.body().contentType(), body)).build();
    }


    /**
     * 传递GET请求的全局参数
     * @param request
     * @return
     */
    public Request addGetParameter(Request request){
        HttpUrl.Builder builder = request.url().newBuilder();
        builder.setEncodedQueryParameter("token", SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.TOKEN));
        Request newRequest = request.newBuilder()
                .method(request.method(), request.body())
                .url(builder.build())
                .build();
        return newRequest;
    }


    /***
     * 添加POST的公共参数
     */
    public Request addPostParameter(Request request) throws IOException {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        FormBody formBody;
        Map<String, String> requstMap = new HashMap<>();
        requstMap.put("token", SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.TOKEN));
        LogUtils.e("参数：token="+SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.TOKEN));
        if (request.body().contentLength() > 0 && request.body() instanceof FormBody) {
            formBody = (FormBody) request.body();
            //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
            for (int i = 0; i < formBody.size(); i++) {
                  requstMap.put(formBody.name(i), formBody.value(i));
                  LogUtils.e(request.url() + "参数:" + formBody.name(i) + "=" + formBody.value(i));
            }
        }
        //添加公共参数
        for (String key : requstMap.keySet()) {
            bodyBuilder.add(key, requstMap.get(key));
        }
        formBody = bodyBuilder.build();
        request = request.newBuilder().post(formBody).build();
        return request;
    }



    /**
     * 刷新token
     */
    private Login refreshToken(){
        final String WX_OPEN_ID=SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.WX_OPEN_ID);
        final String QQ_OPEN_ID=SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.QQ_OPEN_ID);
        if(!TextUtils.isEmpty(WX_OPEN_ID) || !TextUtils.isEmpty(QQ_OPEN_ID)){
            Map<String,String> map=new HashMap<>();

            if(!TextUtils.isEmpty(WX_OPEN_ID)){
                map.put("openid", WX_OPEN_ID);
                map.put("thirdlogintype","0");
            }
            if(!TextUtils.isEmpty(QQ_OPEN_ID)){
                map.put("openid", QQ_OPEN_ID);
                map.put("thirdlogintype","1");
            }

            map.put("type","0");
            Login  login= null;
            try {
                login = Http.getRetrofitNoInterceptor().create(HttpApi.class).threeLogin(map).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return login;
        }

        final boolean isCode=SPUtil.getInstance(BaseApplication.getContext()).getBoolean(SPUtil.IS_SMSCODE_LOGIN);
        final String mobile=SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.ACCOUNT);
        final String password=SPUtil.getInstance(BaseApplication.getContext()).getString(SPUtil.PASSWORD);
        if(!TextUtils.isEmpty(mobile) && !TextUtils.isEmpty(password)){
            Map<String,String> map=new HashMap<>();
            if(isCode){
                map.put("logintype", "1");
            }else{
                map.put("logintype", "0");
            }
            map.put("password",password);
            map.put("phone",mobile);
            try {
                Login  login= Http.getRetrofitNoInterceptor().create(HttpApi.class).pwdLogin(map).execute().body();
                return login;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            EventBus.getDefault().post(new EventBusType(EventStatus.GO_TO_LOGIN));
        }

        return null;
    }


    public int getCode(String json) {
        int code = 0;
        try {
            JSONObject jsonObject = new JSONObject(json);
            code = jsonObject.getInt("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }


}
