package com.ylean.soft.lfd.persenter.main;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.SearchResultActivity;
import com.ylean.soft.lfd.view.TagsLayout;
import com.zxdc.utils.library.util.SPUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2020/2/7.
 */

public class SearchPersenter {

    private Activity activity;

    public SearchPersenter(Activity activity){
        this.activity=activity;
    }


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
     * 跳转搜索结果页
     * @param keys
     */
    public void gotoSearchList(String keys){
        Intent intent=new Intent(activity,SearchResultActivity.class);
        intent.putExtra("keys",keys);
        activity.startActivity(intent);
    }
}
