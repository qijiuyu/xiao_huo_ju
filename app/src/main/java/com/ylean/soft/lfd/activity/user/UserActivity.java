package com.ylean.soft.lfd.activity.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.fragment.user.LookHistoryFragment;
import com.ylean.soft.lfd.fragment.user.MyFocusFragment;
import com.ylean.soft.lfd.fragment.user.MyLikeFragment;
import com.ylean.soft.lfd.view.ViewPagerForScrollView;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 * Created by Administrator on 2020/2/5.
 */
public class UserActivity extends BaseActivity{

    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_fans)
    TextView tvFans;
    @BindView(R.id.tv_focus)
    TextView tvFocus;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_xz)
    TextView tvXz;
    @BindView(R.id.tv_works)
    TextView tvWorks;
    @BindView(R.id.tv_my_focus)
    TextView tvMyFocus;
    @BindView(R.id.tv_my_look)
    TextView tvMyLook;
    @BindView(R.id.view_works)
    View viewWorks;
    @BindView(R.id.view_focus)
    View viewFocus;
    @BindView(R.id.view_look)
    View viewLook;
    @BindView(R.id.pager)
    public ViewPagerForScrollView pager;
    private List<TextView> tvList=new ArrayList<>();
    private List<View> viewList=new ArrayList<>();
    /**
     * 0：我的作品
     * 1：我的关注
     * 2：观看记录
     */
    private int pageIndex;
    //我的喜欢
    private MyLikeFragment myLikeFragment=new MyLikeFragment();
    //我的关注
    private MyFocusFragment myFocusFragment=new MyFocusFragment();
    //观看记录
    private LookHistoryFragment lookHistoryFragment=new LookHistoryFragment();
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        tvList.add(tvWorks);
        tvList.add(tvMyFocus);
        tvList.add(tvMyLook);
        viewList.add(viewWorks);
        viewList.add(viewFocus);
        viewList.add(viewLook);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(3);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            public void onPageSelected(int position) {
                pager.resetHeight(position);
                pageIndex=position;
                updateUI();
            }
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.img_setting, R.id.img_news, R.id.tv_edit, R.id.tv_works, R.id.tv_my_focus, R.id.tv_my_look,R.id.tv_cooperation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //设置
            case R.id.img_setting:
                setClass(SettingActivity.class);
                break;
            //消息
            case R.id.img_news:
                setClass(NewsActivity.class);
                break;
            //编辑个人资料
            case R.id.tv_edit:
                setClass(UserInfoActivity.class);
                break;
            //我的作品
            case R.id.tv_works:
                pageIndex=0;
                updateUI();
                break;
            //我的关注
            case R.id.tv_my_focus:
                pageIndex=1;
                updateUI();
                break;
            //观看记录
            case R.id.tv_my_look:
                pageIndex=2;
                updateUI();
                break;
            //内容合作
            case R.id.tv_cooperation:
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
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return myLikeFragment;
            }else if(position==1){
                return myFocusFragment;
            }else{
                return lookHistoryFragment;
            }
        }
    }


    /**
     * 更新ui
     */
    private void updateUI(){
        for (int i=0;i<3;i++){
              if(i==pageIndex){
                  tvList.get(i).setTextColor(getResources().getColor(R.color.color_FFBC32));
                  viewList.get(i).setVisibility(View.VISIBLE);
              }else{
                  tvList.get(i).setTextColor(getResources().getColor(android.R.color.black));
                  viewList.get(i).setVisibility(View.GONE);
              }
        }
        pager.setCurrentItem(pageIndex);
    }
}
