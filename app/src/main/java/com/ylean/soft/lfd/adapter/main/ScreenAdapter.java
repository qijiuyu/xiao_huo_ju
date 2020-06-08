package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Screen;
import com.zxdc.utils.library.util.Util;

import java.util.List;

public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.MyHolder> {

    private Activity activity;
    private List<Screen.ScreenBean> list;
    public ScreenAdapter(Activity activity,List<Screen.ScreenBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_screen, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int i) {
        Screen.ScreenBean screenBean=list.get(i%list.size());
        final String nickName=screenBean.getNickname();
        if(Util.isPhoneNumber(nickName)){
            holder.tvContent.setText(nickName.substring(0, 3) + "****" + nickName.substring(nickName.length() - 4, nickName.length())+"："+screenBean.getContent());
        }else{
            holder.tvContent.setText(screenBean.getNickname()+"："+screenBean.getContent());
        }

    }

    @Override
    public int getItemCount() {
        if(list==null || list.size()==0){
            return 0;
        }
        return Integer.MAX_VALUE;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvContent=itemView.findViewById(R.id.tv_content);
        }
    }

//    /**
//     * 添加新的弹屏内容
//     * @param screenBean
//     */
//    public void addScreen(Screen.ScreenBean screenBean){
//        this.list.add(screenBean);
//        notifyDataSetChanged();
//    }

}

