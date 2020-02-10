package com.ylean.soft.lfd.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.HotterFragmentAdapter;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.view.MyRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HotterFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.re_list)
    MyRefreshLayout reList;
    Unbinder unbinder;
    private View headView;
    private HotterFragmentAdapter hotterFragmentAdapter;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more_hotter, container, false);
        unbinder = ButterKnife.bind(this, view);

        listView.setDivider(null);
        headView = LayoutInflater.from(mActivity).inflate(R.layout.hotter_head, null);
        listView.addHeaderView(headView);

        hotterFragmentAdapter=new HotterFragmentAdapter(mActivity);
        listView.setAdapter(hotterFragmentAdapter);
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
