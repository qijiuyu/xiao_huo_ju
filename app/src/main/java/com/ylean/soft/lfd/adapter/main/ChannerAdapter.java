package com.ylean.soft.lfd.adapter.main;

import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.main.TagManagerActivity;
import com.ylean.soft.lfd.utils.AnimUtil;
import com.ylean.soft.lfd.utils.channel.DataUtils;
import com.ylean.soft.lfd.utils.channel.TouchInterface;
import com.zxdc.utils.library.bean.Tag;
import java.util.Collections;
import java.util.List;

public class ChannerAdapter extends RecyclerView.Adapter<MyViewHolder> implements TouchInterface {

    private TagManagerActivity activity;
    private ItemTouchHelper touchHelper;
    public List<Tag.TagBean> list;
    /**
     * true：点击直接拖拽
     * false：只能长按拖拽
     */
    private boolean isClick=false;
    public ChannerAdapter(TagManagerActivity activity, List<Tag.TagBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_channel, parent, false));
        return viewHolder;
    }

    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Tag.TagBean tagBean=list.get(position);
        //名称
        holder.tvName.setText(tagBean.getName());
        //图片
//        Glide.with(activity).load(tagBean.getImgurl()).into(holder.imgTag);


        /**
         * 标签长按震动
         */
        holder.tvName.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Vibrator vibrator = (Vibrator)activity.getSystemService(activity.VIBRATOR_SERVICE);
                vibrator.vibrate(70);
                //设置按钮状态
                activity.editBtn();
                return true;
            }
        });

        /**
         * 标签点击拖动
         */
        holder.tvName.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(!isClick){
                        return false;
                    }
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onMove(int currentPosition, int targetPosition) {
        Collections.swap(list, currentPosition, targetPosition);
        if (targetPosition < currentPosition) {
            List<Tag.TagBean> subList = list.subList(targetPosition + 1, currentPosition + 1);
            //向右移一位
            DataUtils.rightStepList(0, subList);
        } else {
            List<Tag.TagBean> subList = list.subList(currentPosition, targetPosition);
            //向左移一位
            DataUtils.leftStepList(0, subList);
        }
        notifyItemMoved(currentPosition, targetPosition);
    }


    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper=touchHelper;
    }

    public void setOnClick(boolean isClick){
        this.isClick=isClick;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public ImageView imgTag;
    public MyViewHolder(View itemView) {
        super(itemView);
        tvName =itemView.findViewById(R.id.text_item);
        imgTag=itemView.findViewWithTag(R.id.img_tag);
    }
}

