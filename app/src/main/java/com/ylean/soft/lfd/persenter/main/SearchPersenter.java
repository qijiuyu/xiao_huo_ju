package com.ylean.soft.lfd.persenter.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.SearchActivity;
import com.ylean.soft.lfd.activity.main.SearchResultActivity;
import com.ylean.soft.lfd.view.TagsLayout;
import com.zxdc.utils.library.bean.HotSearch;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.SPUtil;
import com.zxdc.utils.library.util.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2020/2/7.
 */

public class SearchPersenter {

    private Activity activity;

    private TagsLayout hotTags;

    public SearchPersenter(Activity activity){
        this.activity=activity;
    }


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //获取热门搜索
                case HandlerConstant.HOT_SEARCH_SUCCESS:
                      HotSearch hotSearch= (HotSearch) msg.obj;
                      if(hotSearch==null){
                          break;
                      }
                      if(hotSearch.isSussess()){
                          //展示热门搜索
                          showHot(hotSearch.getData());
                      }else{
                          ToastUtil.showLong(hotSearch.getDesc());
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
     * 保存搜索过的关键字
     */
    public void addTabKey(String strKey) {
        String keys = SPUtil.getInstance(activity).getString(SPUtil.SEARCH_KEY);
        Map<String, String> keyMap;
        if (!TextUtils.isEmpty(keys)) {
            keyMap = SPUtil.gson.fromJson(keys, Map.class);
        } else {
            keyMap = new HashMap<>();
        }
        keyMap.put(strKey, strKey);
        SPUtil.getInstance(activity).addString(SPUtil.SEARCH_KEY, SPUtil.gson.toJson(keyMap));
    }


    /**
     * 显示搜索过的历史
     * @param tags
     */
    public void showHistory(TagsLayout tags){
        tags.removeAllViews();
        final String keyStr= SPUtil.getInstance(activity).getString(SPUtil.SEARCH_KEY);
        if(TextUtils.isEmpty(keyStr)) {
            return;
        }
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Map<String, String> keyMap = SPUtil.gson.fromJson(keyStr, Map.class);
        Set<String> keys = keyMap.keySet();
        for (String key : keys) {
            TextView textView = new TextView(activity);
            textView.setText(keyMap.get(key));
            textView.setTextColor(activity.getResources().getColor(R.color.color_666666));
            textView.setTextSize(12);
            textView.setBackgroundResource(R.drawable.bg_item_des);
            textView.setPadding(20, 10, 20, 10);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    gotoSearchList(((TextView) v).getText().toString());
                }
            });
            tags.addView(textView, lp);
        }
    }


    /**
     * 展示热门搜索
     */
    public void showHot(List<HotSearch.HotSearchBean> list){
        hotTags.removeAllViews();
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<list.size();i++){
            TextView textView = new TextView(activity);
            textView.setText(list.get(i).getName());
            textView.setTextColor(activity.getResources().getColor(R.color.color_666666));
            textView.setTextSize(12);
            textView.setBackgroundResource(R.drawable.bg_item_des);
            textView.setPadding(20, 10, 20, 10);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    gotoSearchList(((TextView) v).getText().toString());
                }
            });
            hotTags.addView(textView, lp);
        }
    }

    /**
     * 跳转搜索结果页
     * @param keys
     */
    public void gotoSearchList(String keys){
        Intent intent=new Intent(activity,SearchResultActivity.class);
        intent.putExtra("keys",keys);
        activity.startActivity(intent);
    }


    /**
     * 热门搜索
     */
    public void hotSearch(TagsLayout hotTags){
        this.hotTags=hotTags;
        HttpMethod.hotSearch(handler);
    }
}
