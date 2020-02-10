package com.ylean.soft.lfd.adapter.init;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.HashMap;
import java.util.Map;

public class SelectTagAdapter extends RecyclerView.Adapter<SelectTagAdapter.MyHolder> {

    private Activity activity;
    public Map<Integer,Integer> map=new HashMap<>();
    public SelectTagAdapter(Activity activity) {
        this.activity = activity;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_select_tag, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;

        if(map.get(i)!=null){
            holder.imgCheck.setImageResource(R.mipmap.yes_check);
        }else{
            holder.imgCheck.setImageResource(R.mipmap.no_check);
        }

        holder.imgCheck.setTag(i);
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag()==null){
                    return;
                }
                int position= (int) v.getTag();
                if(map.get(position)!=null){
                    map.remove(position);
                }else{
                    map.put(position,position);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return 35;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
       OvalImageViews imgHead;
        ImageView imgCheck;
        TextView tvName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imgHead=itemView.findViewById(R.id.img_head);
            imgCheck=itemView.findViewById(R.id.img_check);
            tvName=itemView.findViewById(R.id.tv_name);
        }
    }

}

