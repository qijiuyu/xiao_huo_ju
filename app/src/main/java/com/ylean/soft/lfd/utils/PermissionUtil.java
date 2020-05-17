package com.ylean.soft.lfd.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

public class PermissionUtil {

private static AlertDialog.Builder normalDialog;
    /**
     * getPermission 动态获取权限方法
     *
     * @param context 上下文
     */
    public static void getPermission(final Context context,final PermissionCallBack permissionCallBack){
        if (XXPermissions.isHasPermission(context,
                //所需危险权限可以在此处添加：
                Permission.READ_PHONE_STATE,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.READ_EXTERNAL_STORAGE,
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.CAMERA)
                ) {
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    permissionCallBack.onclick();
                }
            },2000);
        }else {
            XXPermissions.with((Activity)context).permission(
                    //同时在此处添加：
                    Permission.READ_PHONE_STATE,
                    Permission.WRITE_EXTERNAL_STORAGE,
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.ACCESS_FINE_LOCATION,
                    Permission.ACCESS_COARSE_LOCATION,
                    Permission.CAMERA
            ).request(new OnPermission() {
                @Override
                public void noPermission(List<String> denied, boolean quick) {
                    if (quick) {
                        //如果是被永久拒绝就跳转到应用权限系统设置页面
                        if(normalDialog!=null){
                            return;
                        }
                        normalDialog = new AlertDialog.Builder(context);
                        normalDialog.setTitle("开启权限引导");
                        normalDialog.setMessage("被您永久禁用的权限为应用必要权限，是否需要引导您去手动开启权限呢？");
                        normalDialog.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                normalDialog=null;
                                XXPermissions.gotoPermissionSettings(context);
                            }
                        });
                        normalDialog.show();
                    }else {
                        getPermission(context,permissionCallBack);
                    }
                }

                @Override
                public void hasPermission(List<String> granted, boolean isAll) {
                    if (isAll) {
                        Log.e("tag","获取权限成功");
                        permissionCallBack.onclick();
                    }else {
                        getPermission(context,permissionCallBack);
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
