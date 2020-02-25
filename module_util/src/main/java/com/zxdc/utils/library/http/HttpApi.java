package com.zxdc.utils.library.http;



import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.bean.UserInfo;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpApi {

//    @GET(HttpConstant.GET_SITE)
//    Call<Site> getSite(@Query("city") String city);
//
//
    @FormUrlEncoded
    @POST(HttpConstant.GET_CODE)
    Call<BaseBean> sendCode(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REGISTER)
    Call<BaseBean> register(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SMS_LOGIN)
    Call<Login> smsLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.PWD_LOGIN)
    Call<Login> pwdLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.EDIT_PWD)
    Call<BaseBean> editPwd(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.THREE_LOGIN)
    Call<Login> threeLogin(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.CHANNEL)
    Call<ResponseBody> channel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_USER)
    Call<UserInfo> getUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.EDIT_USER)
    Call<BaseBean> editUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REFRESH)
    Call<BaseBean> refreshToken(@FieldMap Map<String, String> map);
}
