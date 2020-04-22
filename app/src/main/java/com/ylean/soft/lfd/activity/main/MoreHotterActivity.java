package com.ylean.soft.lfd.activity.main;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.AgreementActivity;
import com.ylean.soft.lfd.fragment.main.HotterFragment;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 今日最热和top50查看更多
 * Created by Administrator on 2020/2/6.
 */

public class MoreHotterActivity extends BaseActivity {

    @BindView(R.id.tv_hotter)
    TextView tvHotter;
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.view_hot)
    View viewHot;
    @BindView(R.id.view_top)
    View viewTop;
    @BindView(R.id.pager)
    ViewPager pager;
    //fragment页码
    public static int pageIndex;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.transparencyBar(this);
        setContentView(R.layout.activity_more_hotter);
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 初始化
     *
     * @return
     */
    private void initView() {
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(2);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            public void onPageSelected(int position) {
                pageIndex=position;
                updateViewPager();
            }
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.img_bank, R.id.tv_hotter, R.id.tv_top,R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            //今日最热
            case R.id.tv_hotter:
                pageIndex=0;
                updateViewPager();
                pager.setCurrentItem(0);
                break;
            //top
            case R.id.tv_top:
                pageIndex=1;
                updateViewPager();
                pager.setCurrentItem(1);
                break;
            case R.id.tv_agreement:
                 Intent intent=new Intent(this,AgreementActivity.class);
                 intent.putExtra("type",6);
                 startActivity(intent);
                  break;
            default:
                break;
        }
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            return new HotterFragment();
        }
    }


    private void updateViewPager(){
        if(pageIndex==0){
            viewHot.setVisibility(View.VISIBLE);
            viewTop.setVisibility(View.GONE);
            tvHotter.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvTop.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvHotter.setTextSize(18);
            tvTop.setTextSize(15);
        }else{
            viewHot.setVisibility(View.GONE);
            viewTop.setVisibility(View.VISIBLE);
            tvTop.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvHotter.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvTop.setTextSize(18);
            tvHotter.setTextSize(15);
        }
    }
}
