package com.ylean.soft.lfd.adapter.init;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.view.OvalImageViews;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectTagAdapter extends RecyclerView.Adapter<SelectTagAdapter.MyHolder> {

    private Activity activity;
    private List<Tag.TagBean> list;
    public Map<Integer,Integer> map=new HashMap<>();
    public SelectTagAdapter(Activity activity,List<Tag.TagBean> list) {
        this.activity = activity;
        this.list=list;
    }

    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(activity).inflate(R.layout.item_select_tag, viewGroup,false);
        MyHolder holder = new MyHolder(inflate);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        MyHolder holder =myHolder;
        final Tag.TagBean tagBean=list.get(i);
        if(tagBean==null){
            return;
        }
        holder.tvName.setHint(tagBean.getName());
        //背景图片
        String imgUrl=tagBean.getImgurl();
        holder.imgHead.setTag(R.id.imageid,imgUrl);
        if(holder.imgHead.getTag(R.id.imageid)!=null && imgUrl==holder.imgHead.getTag(R.id.imageid)){
            Glide.with(activity).load(imgUrl).into(holder.imgHead);
        }

        if(map.get(tagBean.getId())!=null){
            holder.imgCheck.setImageResource(R.mipmap.yes_check);
        }else{
            holder.imgCheck.setImageResource(R.mipmap.no_check);
        }

        holder.imgCheck.setTag(tagBean.getId());
        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(v.getTag()==null){
                    return;
                }
                int id= (int) v.getTag();
                if(map.get(id)!=null){
                    map.remove(id);
                }else{
                    map.put(id,id);
                }
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list==null ? 0 : list.size();
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

