package com.ylean.soft.lfd.activity.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.zxdc.utils.library.bean.Upload;
import com.zxdc.utils.library.bean.UserInfo;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.http.HandlerConstant;
import com.zxdc.utils.library.http.HttpMethod;
import com.zxdc.utils.library.util.BitMapUtil;
import com.zxdc.utils.library.util.DialogUtil;
import com.zxdc.utils.library.util.LogUtils;
import com.zxdc.utils.library.util.ToastUtil;
import com.zxdc.utils.library.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    //用户数据对象
    private UserInfo.UserBean userBean;
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
        userBean= (UserInfo.UserBean) getIntent().getSerializableExtra("userBean");
        //展示用户数据
        showUser(userBean);
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
                setSexBj(2);
                //修改个人信息
                editUser();
                break;
            //男
            case R.id.rel_boy:
               setSexBj(1);
                //修改个人信息
                editUser();
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


    private Handler handler=new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            DialogUtil.closeProgress();
            switch (msg.what){
                //图片上传
                case HandlerConstant.UPLOAD_SUCCESS:
                      Upload upload= (Upload) msg.obj;
                      if(upload==null){
                          break;
                      }
                      if(upload.isSussess() && upload.getData()!=null && upload.getData().size()>0){
                          userBean.setImgurl(upload.getData().get(0));
                          //修改个人信息
                          editUser();
                      }
                      ToastUtil.showLong(upload.getDesc());
                      break;
                case HandlerConstant.REQUST_ERROR:
                    ToastUtil.showLong(msg.obj.toString());
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    /**
     * 展示用户数据
     */
    private void showUser(UserInfo.UserBean userBean){
        if(userBean==null){
            return;
        }
        Glide.with(this).load(userBean.getImgurl()).error(R.mipmap.default_head).into(imgHead);
        tvName.setText(userBean.getNickname());
        //设置性别背景
        setSexBj(userBean.getSex());
        tvBirthday.setText(userBean.getBirthday());
        tvMobile.setText(userBean.getMobile());
        tvRemark.setText(userBean.getIntroduction());
    }


    /**
     * 设置性别背景
     * @param type
     */
    private void setSexBj(int type){
        userBean.setSex(type);
        if(type==1){
            relGirl.setBackground(getResources().getDrawable(R.drawable.bg_sex_box));
            relBoy.setBackground(getResources().getDrawable(R.drawable.btn_bg_sex));
            tvGirl.setTextColor(getResources().getColor(R.color.color_999999));
            tvBoy.setTextColor(getResources().getColor(android.R.color.white));
        }else{
            relGirl.setBackground(getResources().getDrawable(R.drawable.btn_bg_sex));
            relBoy.setBackground(getResources().getDrawable(R.drawable.bg_sex_box));
            tvGirl.setTextColor(getResources().getColor(android.R.color.white));
            tvBoy.setTextColor(getResources().getColor(R.color.color_999999));
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
                //上传图片
                upload(imgFile);
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
            //回执时间
            case EventStatus.SHOW_SELECT_TIME:
                  tvBirthday.setText(eventBusType.getObject().toString());
                  userBean.setBirthday(eventBusType.getObject().toString());
                 //修改个人信息
                 editUser();
                  break;
            //回执昵称
            case EventStatus.EDIT_NAME:
                  tvName.setText(eventBusType.getObject().toString());
                  userBean.setNickname(eventBusType.getObject().toString());
                 //修改个人信息
                 editUser();
                  break;
            //回执个人简介
            case EventStatus.EDIT_REMARK:
                  tvRemark.setText(eventBusType.getObject().toString());
                  userBean.setIntroduction(eventBusType.getObject().toString());
                 //修改个人信息
                 editUser();
                  break;
            default:
                break;
        }
    }


    /**
     * 修改个人信息
     */
    private void editUser(){
        if(userBean==null){
            userBean=new UserInfo.UserBean();
        }
        HttpMethod.editUser(userBean.getBirthday(),userBean.getImgurl(),userBean.getIntroduction(),userBean.getNickname(),String.valueOf(userBean.getSex()),handler);
    }


    /**
     * 上传图片
     */
    private void upload(File file){
        DialogUtil.showProgress(this,"图片上传中");
        HttpMethod.upload(file,handler);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
