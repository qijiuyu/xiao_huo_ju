package com.ylean.soft.lfd.activity.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.activity.TabActivity;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.util.SPUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2020/2/8.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    //用来存放导航图片实例
    private ArrayList<ImageView> imageViews;
    //导航页资源
    private int[] images = new int[]{
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3,
            R.mipmap.guide_4
    };
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        SPUtil.getInstance(activity).addBoolean(SPUtil.IS_FIRST_OPEN, true);

        imageViews = new ArrayList<>();
        //初始化导航页面
        for (int i = 0; i < images.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setImageResource(images[i]);
            imageViews.add(iv);
            iv.setTag(i);
            iv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int index= (int) v.getTag();
                    if(index==3){
                        setClass(TabActivity.class);
                        finish();
                    }
                }
            });
        }
        //为ViewPager添加适配器
        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    //PagerAdapter有四个方法
    class MyAdapter extends PagerAdapter {
        //返回导航页的个数
        @Override
        public int getCount() {
            return images.length;
        }

        //判断是否由对象生成
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //加载页面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = imageViews.get(position);
            container.addView(iv);
            return iv;
        }

        //移除页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
