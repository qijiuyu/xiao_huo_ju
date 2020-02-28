package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.HotTop;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.MeasureListView;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.List;

public class MainHottestAdapter extends RecyclerView.Adapter<MainHottestAdapter.MyHolder> {

    private Activity activity;
    private List<HotTop.DataBean> list;
    public MainHottestAdapter(Activity activity,List<HotTop.DataBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_hottest_list, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MainHottestDataAdapter mainHottestDataAdapter=new MainHottestDataAdapter(activity,list,i);
        myHolder.listView.setAdapter(mainHottestDataAdapter);
    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : getNum();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        MeasureListView listView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            listView=itemView.findViewById(R.id.listView);
        }
    }


    private int getNum(){
        int num=list.size()/3;
        if(list.size()%3!=0){
            num++;
        }
        return num;
    }

}

