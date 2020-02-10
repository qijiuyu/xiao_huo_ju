package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.DragAdapter;
import com.ylean.soft.lfd.view.DragGrid;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.ChannelItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 频道管理
 * Created by Administrator on 2020/2/5.
 */

public class TagManagerActivity extends BaseActivity {

    @BindView(R.id.grid_tag)
    DragGrid gridTag;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_manager);
        ButterKnife.bind(this);

        List<ChannelItem> defaultUserChannels=new ArrayList<>();
        defaultUserChannels.add(new ChannelItem(1, "推荐", 1, 1));
        defaultUserChannels.add(new ChannelItem(2, "热点", 2, 1));
        defaultUserChannels.add(new ChannelItem(3, "娱乐", 3, 1));
        defaultUserChannels.add(new ChannelItem(4, "时尚", 4, 1));
        defaultUserChannels.add(new ChannelItem(5, "科技", 5, 1));
        defaultUserChannels.add(new ChannelItem(6, "体育", 6, 1));
        defaultUserChannels.add(new ChannelItem(7, "军事", 7, 1));
        gridTag.setAdapter(new DragAdapter(this,defaultUserChannels));


    }
}
