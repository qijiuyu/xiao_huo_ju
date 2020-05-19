package com.ylean.soft.lfd.activity.main;

import android.content.Intent;
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
import com.ylean.soft.lfd.activity.init.LoginActivity;
import com.ylean.soft.lfd.fragment.main.OtherFragment;
import com.ylean.soft.lfd.fragment.main.SelectFragment;
import com.ylean.soft.lfd.persenter.main.MainPersenter;
import com.ylean.soft.lfd.view.PagerSlidingTabStrip;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.base.BaseFragment;
import com.zxdc.utils.library.bean.Tag;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
 * 76c14f983e646792e2fee85d90e8c4e9
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    private DisplayMetrics dm;
    //导航频道列表
    public List<Tag.TagBean> channelList=new ArrayList<>();
    //存储导航名称，以及对应的fragment
    private Map<String,BaseFragment> map=new HashMap<>();
    //生成几个fragment
    private List<Fragment> fragmentList=new ArrayList<>();
    private MyPagerAdapter myPagerAdapter;
    private MainPersenter mainPersenter;
    //fragment页码
    public int pageIndex;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //初始化
        initView();
        //查询导航频道列表
        mainPersenter.channel();
    }

    /**
     * 初始化
     */
    private void initView() {
        //注册eventBus
        EventBus.getDefault().register(this);
        //实例化MVP
        mainPersenter=new MainPersenter(this);
        dm = getResources().getDisplayMetrics();
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        myPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(myPagerAdapter);
        //设置预加载页面数量的方法
        pager.setOffscreenPageLimit(channelList.size());
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
          public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          }
          public void onPageSelected(int position) {
              pageIndex=position;
          }
          public void onPageScrollStateChanged(int state) {
          }
        });
        tabs.setViewPager(pager);
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
                TypedValue.COMPLEX_UNIT_SP, 15, dm));
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColorResource(android.R.color.white);
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        tabs.setTextColorResource(android.R.color.white);
        tabs.setSelectedTextColorResource(android.R.color.white);
        tabs.setSelectTextSize(18);
        tabs.setTypeface(null, Typeface.BOLD);
        // 取消点击Tab时的背景色
        tabs.setTabBackground(0);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public CharSequence getPageTitle(int position) {
            return channelList.get(position).getName();
        }

        public int getCount() {
            return channelList.size();
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
            //频道管理
            case R.id.img_menu:
                setClass(TagManagerActivity.class);
                break;
            default:
                break;
        }
    }


    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        Tag.TagBean tagBean=null;
        switch (eventBusType.getStatus()) {
            //显示频道列表
            case EventStatus.SHOW_MAIN_CHANNEL:
                 channelList= (List<Tag.TagBean>) eventBusType.getObject();
                 tagBean=new Tag.TagBean("精选");
                 channelList.add(0,tagBean);

                 for (int i=0;i<channelList.size();i++){
                      if(i==0){
                          map.put(channelList.get(i).getName(),new SelectFragment());
                      }else{
                          map.put(channelList.get(i).getName(),new OtherFragment());
                      }
                 }
                 //添加对应的fragment到集合中
                 for (int i=0;i<channelList.size();i++){
                      fragmentList.add(map.get(channelList.get(i).getName()));
                 }
                 //对PagerSlidingTabStrip的各项属性进行赋值
                 setTabsValue();
                  break;
            //频道排序完成
            case EventStatus.CHANNEL_SORT_SUCCESS:
                  channelList= (List<Tag.TagBean>) eventBusType.getObject();
                  tagBean=new Tag.TagBean("精选");
                  channelList.add(0,tagBean);
                  //清空fragment集合
                  fragmentList.clear();
                  //添加对应的fragment到集合中
                  for (int i=0;i<channelList.size();i++){
                      fragmentList.add(map.get(channelList.get(i).getName()));
                  }
                  myPagerAdapter.notifyDataSetChanged();
                  tabs.setViewPager(pager);
                  break;
            //查看更多---切换各个频道
            case EventStatus.LOOK_MORE_MAIN_BLUES:
                  final int channelId= (int) eventBusType.getObject();
                  for (int i=0;i<channelList.size();i++){
                       if(channelId==channelList.get(i).getId()){
                           pager.setCurrentItem(i);
                           break;
                       }
                  }
                  break;
                  //重新登录
            case EventStatus.GO_TO_LOGIN:
                Intent intent=new Intent(this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                 break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
