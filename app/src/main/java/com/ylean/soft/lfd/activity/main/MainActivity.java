package com.ylean.soft.lfd.activity.main;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.fragment.main.OtherFragment;
import com.ylean.soft.lfd.fragment.main.SelectFragment;
import com.ylean.soft.lfd.view.PagerSlidingTabStrip;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private DisplayMetrics dm;
    private String[] title=new String[]{"精选","高甜","玄幻","修真","武侠","历史","穿越","都市","言情","傻逼"};
    private Map<String,BaseFragment> map=new HashMap<>();
    private List<Fragment> fragmentList=new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        for (int i=0;i<10;i++){
              if(i==0){
                  map.put(title[i],new SelectFragment());
              }else{
                  map.put(title[i],new OtherFragment());
              }
        }
        updateFragment();
    }

    /**
     * 初始化
     */
    private void initView() {
        dm = getResources().getDisplayMetrics();
        myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
        //设置预加载页面数量的方法
        pager.setOffscreenPageLimit(10);
        tabs.setViewPager(pager);
        setTabsValue();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        tabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        tabs.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 0, dm));
        // 设置Tab Indicator的高度
        tabs.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        tabs.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColorResource(android.R.color.white);
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColorResource(android.R.color.white);
        tabs.setSelectedTextColorResource(android.R.color.white);
        tabs.setSelectTextSize(16);
        tabs.setTypeface(null, Typeface.BOLD);
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

        public int getCount() {
            return title.length;
        }

        public Fragment getItem(int position) {
            return fragmentList.get(position);
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
//                title=new String[]{"精选","玄幻","高甜","修真","武侠","历史","穿越","都市","言情","傻逼"};
//                updateFragment();
//                myPagerAdapter.notifyDataSetChanged();
//                tabs.setViewPager(pager);
                break;
            default:
                break;
        }
    }


    private void updateFragment(){
        fragmentList.clear();
        for (int i=0;i<title.length;i++){
            fragmentList.add(map.get(title[i]));
        }
    }

}
