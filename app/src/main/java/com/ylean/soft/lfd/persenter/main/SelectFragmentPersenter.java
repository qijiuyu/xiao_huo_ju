package com.ylean.soft.lfd.persenter.main;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.zxdc.utils.library.bean.Author;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.bean.Project;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
public class SelectFragmentPersenter {

    private Activity activity;

    public SelectFragmentPersenter(Activity activity){
        this.activity=activity;
    }

    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            HotTop hotTop;
            switch (msg.what){
                //首页banner
                case HandlerConstant.GET_MAIN_BANNER:
                    hotTop= (HotTop) msg.obj;
                    if(hotTop==null){
                        break;
                    }
                    if(hotTop.isSussess()){
                        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_BANNER,hotTop.getData()));
                    }else{
                        ToastUtil.showLong(hotTop.getDesc());
                    }
                      break;
                //获取热播和精选TOP剧集列表
                case HandlerConstant.GET_HOT_TOP_SUCCESS1:
                      hotTop= (HotTop) msg.obj;
                      if(hotTop==null){
                          break;
                      }
                      if(hotTop.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_HOTTER,hotTop.getData()));
                      }else{
                          ToastUtil.showLong(hotTop.getDesc());
                      }
                      break;
                //获取猜你喜欢的数据
                case HandlerConstant.GET_GUESS_LIKE_SUCCESS:
                     hotTop= (HotTop) msg.obj;
                     if(hotTop==null){
                         break;
                     }
                     if(hotTop.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_LOOK,hotTop.getData()));
                     }else{
                         ToastUtil.showLong(hotTop.getDesc());
                     }
                      break;
                //获取专题列表
                case HandlerConstant.GET_PROJECT_SUCCESS1:
                     Project project= (Project) msg.obj;
                     if(project==null){
                         break;
                     }
                     if(project.isSussess()){
                         EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_PROJECT,project.getData()));
                     }else{
                         ToastUtil.showLong(project.getDesc());
                     }
                      break;
                //获取即将上线的数据
                case HandlerConstant.GET_ONLINE_SUCCESS1:
                    hotTop= (HotTop) msg.obj;
                    if(hotTop==null){
                        break;
                    }
                    if(hotTop.isSussess()){
                        EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_ONLINE,hotTop.getData()));
                    }else{
                        ToastUtil.showLong(hotTop.getDesc());
                    }
                    break;
                //获取热门作者
                case HandlerConstant.HOT_AUTHOR_SUCCESS1:
                      Author author= (Author) msg.obj;
                      if(author==null){
                          break;
                      }
                      if(author.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_AUTHOR,author.getData()));
                      }else{
                          ToastUtil.showLong(author.getDesc());
                      }
                      break;
                //获取各个频道的剧集
                case HandlerConstant.GET_CHANNEL_SUCCESS:
                      Tag tag= (Tag) msg.obj;
                      if(tag==null){
                          break;
                      }
                      if(tag.isSussess()){
                          EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_MAIN_BLUES,tag.getData()));
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
     * 获取热播和精选TOP剧集列表
     * @param hot_top
     */
    public void getHot_Top(int hot_top){
        HttpMethod.getHot_Top(String.valueOf(hot_top),1, HandlerConstant.GET_HOT_TOP_SUCCESS1,handler);
    }

    /**
     * 猜你喜欢
     */
    public void guessLike(){
        HttpMethod.guessLike(handler);
    }


    /**
     * 即将上线
     */
    public void getOnline(){
        HttpMethod.getOnline(1,HandlerConstant.GET_ONLINE_SUCCESS1,handler);
    }


    /**
     * 热门作者
     */
    public void hotAuthor(){
        HttpMethod.hotAuthor(1,HandlerConstant.HOT_AUTHOR_SUCCESS1,handler);
    }

    /**
     * 获取频道剧集列表
     */
    public void channel(){
        HttpMethod.channel("1",handler);
    }

    /**
     * 获取首页banner
     */
    public void mainBanner(){
        HttpMethod.mainBanner(handler);
    }

    /**
     * 获取专题列表
     */
    public void getProject(){
        HttpMethod.getProject(1,1,HandlerConstant.GET_PROJECT_SUCCESS1,handler);
    }
}
