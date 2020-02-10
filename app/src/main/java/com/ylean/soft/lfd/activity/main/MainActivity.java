package com.ylean.soft.lfd.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.adapter.main.MainTypeAdapter;
import com.ylean.soft.lfd.fragment.main.OtherFragment;
import com.ylean.soft.lfd.fragment.main.SelectFragment;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 * Created by Administrator on 2020/2/5.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.list_type)
    RecyclerView listType;
    private MainTypeAdapter mainTypeAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        listType.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mainTypeAdapter = new MainTypeAdapter(this, new MainTypeAdapter.OnItemClickListener() {
            public void onItemClick(int index) {
                pager.setCurrentItem(index);
            }
        });
        listType.setAdapter(mainTypeAdapter);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(10);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            public void onPageSelected(int position) {
                mainTypeAdapter.index=position;
                mainTypeAdapter.notifyDataSetChanged();

            }
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new SelectFragment();
            }
            return new OtherFragment();
        }
    }


    @OnClick({R.id.rel_search, R.id.img_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //搜索
            case R.id.rel_search:
                setClass(SearchActivity.class);
                break;
            case R.id.img_menu:
                setClass(TagManagerActivity.class);
                break;
            default:
                break;
        }
    }

}
