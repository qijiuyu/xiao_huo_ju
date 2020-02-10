package com.ylean.soft.lfd.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.zxdc.utils.library.util.FileUtils;

import java.io.File;

/**
 * 本地相册单选图片
 */
public class SelectPhoto {

    //相册
    public static final int CODE_GALLERY_REQUEST = 0xa3;
    //拍照
    public static final int CODE_CAMERA_REQUEST = 0xa4;
    //裁剪
    public static final int CODE_RESULT_REQUEST = 0xa5;
    public static final String pai = FileUtils.getSdcardPath() + "pictures.jpg";
    //裁剪后的图片路径
    public static String crop;

    /**
     * 选择照片
     */
    public static  void selectPhoto(Activity activity,int type){
        if(type==1){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(pai)));
            activity.startActivityForResult(intent, CODE_CAMERA_REQUEST);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK);
            // 设置文件类型
            intent.setType("image/*");
            activity.startActivityForResult(intent, CODE_GALLERY_REQUEST);
        }

    }


    /**
     * 裁剪原始的图片
     */
    public static void cropRawPhoto(Uri uri,Activity activity) {
        crop = FileUtils.getSdcardPath()+System.currentTimeMillis()+".jpg";
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(new File(crop)));
        // 设置裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", false);
        activity.startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
}
