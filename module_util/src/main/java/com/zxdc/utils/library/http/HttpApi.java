package com.zxdc.utils.library.http;



import com.zxdc.utils.library.bean.AbvertList;
import com.zxdc.utils.library.bean.AddComment;
import com.zxdc.utils.library.bean.AddReply;
import com.zxdc.utils.library.bean.Agreement;
import com.zxdc.utils.library.bean.Author;
import com.zxdc.utils.library.bean.AuthorDetails;
import com.zxdc.utils.library.bean.BaseBean;
import com.zxdc.utils.library.bean.Browse;
import com.zxdc.utils.library.bean.Comment;
import com.zxdc.utils.library.bean.CommentList;
import com.zxdc.utils.library.bean.Focus;
import com.zxdc.utils.library.bean.Help;
import com.zxdc.utils.library.bean.HotSearch;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Login;
import com.zxdc.utils.library.bean.News;
import com.zxdc.utils.library.bean.Project;
import com.zxdc.utils.library.bean.ReplyList;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.bean.SerialVideo;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.bean.Version;
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
    Call<Login> register(@FieldMap Map<String, String> map);

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
    @POST(HttpConstant.GET_USER_INFO)
    Call<UserInfo> getUserInfo(@FieldMap Map<String, String> map);

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
    Call<AddComment> sendComment(@FieldMap Map<String, String> map);

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
    Call<CommentList> getComment(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REPLY)
    Call<AddReply> reply(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOCUS_USER)
    Call<Focus> focusUser(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FOCUS_SERIAL)
    Call<HotTop> focusSerial(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.MY_LIKE)
    Call<HotTop> mylike(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_NEWS)
    Call<News> getNews(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.AGREEMENT)
    Call<Agreement> getAgreement(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.HELP)
    Call<Help> getHelp(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.FEEDBACK)
    Call<BaseBean> feedBack(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.VERSION)
    Call<Version> version(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.AUTHOR_DETAILS)
    Call<AuthorDetails> getAuthorDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.SERIAL_VIDEO)
    Call<SerialVideo> getSerialVideo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.ADD_BROWSE)
    Call<BaseBean> addBrowse(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_BROWSE)
    Call<Browse> getBrowse(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.REPLY_LIST)
    Call<ReplyList> getReply(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.HOT_SEARCH)
    Call<HotSearch> hotSearch(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.CLEAR_LOOK)
    Call<BaseBean> clearLook(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.COMM_PRISE)
    Call<BaseBean> commPrise(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.GET_ABVERT)
    Call<AbvertList> getAbvert(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST(HttpConstant.BESPOKE)
    Call<BaseBean> bespoke(@FieldMap Map<String, String> map);
}
