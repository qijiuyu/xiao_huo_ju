package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.MeasureListView;
import com.zxdc.utils.library.view.OvalImageViews;

public class MainHottestAdapter extends RecyclerView.Adapter<MainHottestAdapter.MyHolder> {

    private Activity activity;
    public MainHottestAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_main_hottest_list, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;
        MainHottestDataAdapter mainHottestDataAdapter=new MainHottestDataAdapter(activity);
        holder.listView.setAdapter(mainHottestDataAdapter);

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        MeasureListView listView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            listView=itemView.findViewById(R.id.listView);
        }
    }

}

