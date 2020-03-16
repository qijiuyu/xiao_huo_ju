package com.ylean.soft.lfd.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.init.SelectTagActivity;
import com.ylean.soft.lfd.fragment.user.LookHistoryFragment;
import com.ylean.soft.lfd.fragment.user.MyFocusFragment;
import com.ylean.soft.lfd.fragment.user.MyLikeFragment;
import com.ylean.soft.lfd.persenter.user.UserPersenter;
import com.ylean.soft.lfd.view.ViewPagerForScrollView;
import com.ylean.soft.lfd.view.scrollview.XScrollView;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HttpConstant;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.view.CircleImageView;
import com.zxdc.utils.library.view.MyRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的
 * Created by Administrator on 2020/2/5.
 */
public class UserActivity extends BaseActivity implements XScrollView.IXScrollViewListener{

    @BindView(R.id.scrollView)
    XScrollView scrollView;
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
    private UserPersenter userPersenter;
    //用户数据对象
    private UserInfo.UserBean userBean;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        initView();
        //注册eventBus
        EventBus.getDefault().register(this);
        userPersenter=new UserPersenter(this);
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

        //scrollView上拉下刷事件
        scrollView.setXScrollViewListener(this);
        //隐藏下拉刷新
        scrollView.setPullRefreshEnable(false);
    }

    @OnClick({R.id.img_setting, R.id.img_news, R.id.tv_edit, R.id.tv_works, R.id.tv_my_focus, R.id.tv_my_look})
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
                Intent intent=new Intent(this,UserInfoActivity.class);
                intent.putExtra("userBean",userBean);
                startActivity(intent);
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
                  tvList.get(i).setTextColor(getResources().getColor(android.R.color.black));
                  viewList.get(i).setVisibility(View.VISIBLE);
              }else{
                  tvList.get(i).setTextColor(getResources().getColor(R.color.color_666666));
                  viewList.get(i).setVisibility(View.GONE);
              }
        }
        pager.setCurrentItem(pageIndex);
    }


    /**
     * 显示用户数据
     * @param userBean
     */
    public void showUserInfo(UserInfo.UserBean userBean){
        this.userBean=userBean;
        if(userBean==null){
            return;
        }
        if(!TextUtils.isEmpty(userBean.getImgurl())){
            Glide.with(this).load(HttpConstant.IP+userBean.getImgurl()).into(imgHead);
        }
        tvFans.setText(String.valueOf(userBean.getFansCount()));
        tvFocus.setText(String.valueOf(userBean.getFollowCount()));
        tvId.setText("ID:"+userBean.getCode());
        if(!TextUtils.isEmpty(userBean.getNickname())){
            tvName.setText(userBean.getNickname());
        }else{
            tvName.setText("无");
        }
        if(!TextUtils.isEmpty(userBean.getIntroduction())){
            tvRemark.setText(userBean.getIntroduction());
        }else{
            tvRemark.setText("无");
        }
        switch (userBean.getSex()){
            case 0:
                 tvSex.setText("无");
                 break;
            case 1:
                tvSex.setText("男");
                break;
            case 2:
                tvSex.setText("女");
                break;
            default:
                break;
        }
        if(!TextUtils.isEmpty(userBean.getBirthday()) && userBean.getBirthday().length()>=10){
            Calendar calendar = Calendar.getInstance();
            //年
            int newYear = calendar.get(Calendar.YEAR);
            int oldYear=Integer.parseInt(userBean.getBirthday().substring(0,4));
            tvAge.setText(String.valueOf(newYear-oldYear));
        }else{
            tvAge.setText("无");
        }

        switch (userBean.getConstellation()){
            case 1:
                tvXz.setText("白羊座");
                 break;
            case 2:
                tvXz.setText("金牛座");
                break;
            case 3:
                tvXz.setText("双子座");
                break;
            case 4:
                tvXz.setText("巨蟹座");
                break;
            case 5:
                tvXz.setText("狮子座");
                break;
            case 6:
                tvXz.setText("处女座");
                break;
            case 7:
                tvXz.setText("天秤座");
                break;
            case 8:
                tvXz.setText("天蝎座");
                break;
            case 9:
                tvXz.setText("射手座");
                break;
            case 10:
                tvXz.setText("摩羯座");
                break;
            case 11:
                tvXz.setText("水瓶座");
                break;
            case 12:
                tvXz.setText("双鱼座");
                break;
            default:
                tvXz.setText("无");
                break;
        }
    }


    /**
     * 刷新
     */
    public void onRefresh() {

    }

    /**
     * 加载
     */
    public void onLoadMore() {
        EventBus.getDefault().post(new EventBusType(EventStatus.USER_LOAD_MORE));
    }

    /**
     * EventBus注解
     */
    @Subscribe
    public void onEvent(EventBusType eventBusType) {
        switch (eventBusType.getStatus()) {
            case EventStatus.USER_LOAD_MORE_SUCCESS:
                 scrollView.stopLoadMore();  // 停止加载
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //获取用户信息
        userPersenter.getUser();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
