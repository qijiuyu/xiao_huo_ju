package com.ylean.soft.lfd.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.utils.SelectPhoto;
import com.ylean.soft.lfd.view.SelectTimeDialog;
import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2020/2/8.
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_head)
    CircleImageView imgHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_girl)
    TextView tvGirl;
    @BindView(R.id.rel_girl)
    RelativeLayout relGirl;
    @BindView(R.id.tv_boy)
    TextView tvBoy;
    @BindView(R.id.rel_boy)
    RelativeLayout relBoy;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        //注册eventBus
        EventBus.getDefault().register(this);
        initView();
    }

    /**
     * 初始化
     */
    private void initView(){
        tvTitle.setText("个人信息");
    }

    @OnClick({R.id.img_bank, R.id.img_head, R.id.tv_name, R.id.rel_girl, R.id.rel_boy, R.id.tv_birthday,R.id.rel_remark})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bank:
                finish();
                break;
            //选择头像
            case R.id.img_head:
                showPhotoDialog();
                break;
            //昵称
            case R.id.tv_name:
                setClass(EditNameActivity.class);
                break;
            //女孩
            case R.id.rel_girl:
                relGirl.setBackground(getResources().getDrawable(R.drawable.btn_bg_sex));
                relBoy.setBackground(getResources().getDrawable(R.drawable.bg_sex_box));
                tvGirl.setTextColor(getResources().getColor(android.R.color.white));
                tvBoy.setTextColor(getResources().getColor(R.color.color_999999));
                break;
            //男
            case R.id.rel_boy:
                relGirl.setBackground(getResources().getDrawable(R.drawable.bg_sex_box));
                relBoy.setBackground(getResources().getDrawable(R.drawable.btn_bg_sex));
                tvGirl.setTextColor(getResources().getColor(R.color.color_999999));
                tvBoy.setTextColor(getResources().getColor(android.R.color.white));
                break;
            //出生年月
            case R.id.tv_birthday:
                SelectTimeDialog selectTimeDialog=new SelectTimeDialog(this);
                selectTimeDialog.show();
                break;
            //个人介绍
            case R.id.rel_remark:
                 setClass(RemarkActivity.class);
                 break;
            default:
                break;
        }
    }


    /**
     * 展示选择图片的弹框
     */
    private void showPhotoDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_photo, null);
        final PopupWindow popupWindow = DialogUtil.showPopWindow(view);
        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        //拍照
        view.findViewById(R.id.tv_picture).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
                SelectPhoto.selectPhoto(UserInfoActivity.this,1);
            }
        });
        //从相册选择
        view.findViewById(R.id.tv_photo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
                SelectPhoto.selectPhoto(UserInfoActivity.this,2);
            }
        });
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //返回拍照图片
            case SelectPhoto.CODE_CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    File tempFile = new File(SelectPhoto.pai);
                    if (tempFile.isFile()) {
                        SelectPhoto.cropRawPhoto(Uri.fromFile(tempFile),this);
                    }
                }
                break;
            //返回相册选择图片
            case SelectPhoto.CODE_GALLERY_REQUEST:
                if (data != null) {
                    SelectPhoto.cropRawPhoto(data.getData(),this);
                }
                break;
            //返回裁剪的图片
            case SelectPhoto.CODE_RESULT_REQUEST:
                File imgFile= new File(SelectPhoto.crop);
                Glide.with(UserInfoActivity.this).load(Uri.fromFile(imgFile)).into(imgHead);
//                uploadFile(imgFile);
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
        switch (eventBusType.getStatus()) {
            case EventStatus.SHOW_SELECT_TIME:
                  tvBirthday.setText(eventBusType.getObject().toString());
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
