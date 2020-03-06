package com.zxdc.utils.library.http;



import com.zxdc.utils.library.bean.Author;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.bean.Focus;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.bean.Project;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.bean.VideoInfo;

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
    Call<Tag> channel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_USER)
    Call<UserInfo> getUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.EDIT_USER)
    Call<BaseBean> editUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REFRESH)
    Call<BaseBean> refreshToken(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SET_CHANNEL)
    Call<BaseBean> setChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.HOT_TOP_LIST)
    Call<HotTop> getHot_Top(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GUESS_LIKE)
    Call<HotTop> guessLike(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_ONLINE)
    Call<HotTop> getOnline(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SORT_CHANNEL)
    Call<BaseBean> sortChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.HOT_AUTHOR)
    Call<Author> hotAuthor(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_VIDEO_INFO)
    Call<VideoInfo> videoInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOLLOW)
    Call<BaseBean> follow(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.THUMP)
    Call<BaseBean> thump(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SEND_SCREEN)
    Call<BaseBean> sendScreen(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_SCREEN)
    Call<Screen> getScreen(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SEND_COMMENT)
    Call<BaseBean> sendComment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.MAIN_BANNER)
    Call<HotTop> mainBanner(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_PROJECT)
    Call<Project> getProject(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.TOPIC_LIST)
    Call<HotTop> getProjectList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SERIAL_LIST)
    Call<HotTop> serialList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOUND_VIDEO)
    Call<VideoInfo> foundVideo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_COMMENT)
    Call<Comment> getComment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REPLY)
    Call<BaseBean> reply(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOCUS_USER)
    Call<Focus> focusUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOCUS_SERIAL)
    Call<HotTop> focusSerial(@FieldMap Map<String, String> map);
}
