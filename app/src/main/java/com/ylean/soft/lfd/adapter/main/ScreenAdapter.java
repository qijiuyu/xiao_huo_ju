package com.ylean.soft.lfd.adapter.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.OvalImageViews;

public class ScreenAdapter extends RecyclerView.Adapter<ScreenAdapter.MyHolder> {

    private Activity activity;
    private String[] str=new String[]{"高健：视频很不错，我非常喜欢","刘成：我记得我昨天看过了","范文芳：武汉加油，全中国加油，大家一起加油","张三：好饿啊，好几天没有吃饭了","李龙：今年是老鼠年，大家都像过街老鼠一样","何进：我是何进，三国之乱由我开始的","曹操：我是孟德，我才高八斗","张飞：我很勇猛的","赵云：我七进七出，救了阿斗"};
    public ScreenAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_screen, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;

        String content=str[i%str.length];
        holder.tvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvContent=itemView.findViewById(R.id.tv_content);
        }
    }

}

