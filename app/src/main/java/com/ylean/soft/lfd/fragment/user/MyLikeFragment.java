package com.ylean.soft.lfd.fragment.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.user.UserActivity;
import com.ylean.soft.lfd.adapter.user.MyLikeAdapter;
import com.zxdc.utils.library.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyLikeFragment extends BaseFragment {

    @BindView(R.id.listView)
    RecyclerView listView;
    Unbinder unbinder;
    private MyLikeAdapter myLikeAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    View view;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mylike, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((UserActivity)mActivity).pager.setObjectForPosition(view,0);

        myLikeAdapter=new MyLikeAdapter(mActivity);
        listView.setHasFixedSize(true);
        listView.setNestedScrollingEnabled(false);
        listView.setLayoutManager(new GridLayoutManager(mActivity, 3));
        listView.setAdapter(myLikeAdapter);

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
