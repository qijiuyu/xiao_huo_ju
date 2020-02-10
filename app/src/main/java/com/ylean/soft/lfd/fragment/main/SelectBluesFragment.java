package com.ylean.soft.lfd.fragment.main;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.OtherListAdapter;
import com.ylean.soft.lfd.adapter.main.SelectBluesAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.view.MyRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SelectBluesFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_blues, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.findViewById(R.id.rel).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusType(EventStatus.CLOSE_VIDEO_RIGHT));
            }
        });

        reList.setPullDownRefreshEnable(false);
        listView.setDivider(null);
        listView.setAdapter(new SelectBluesAdapter(mActivity));
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
