package com.zxdc.utils.library.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.zxdc.utils.library.base.BaseApplication;

/**
 * Toast统一管理类
 */

public class ToastUtil {
    private static boolean isShow = true;//默认显示
    private static  Toast mToast = null;//全局唯一的Toast

    /*private控制不应该被实例化*/
    private ToastUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * 全局控制是否显示Toast
     * @param isShowToast
     */
    public static void controlShow(boolean isShowToast){
        isShow = isShowToast;
    }

    /**
     * 取消Toast显示
     */
    public void cancelToast() {
        if(isShow && mToast != null){
            mToast.cancel();
        }
    }


    /**
     * 长时间显示Toast
     */
    public static void showLong(CharSequence message) {
        if(TextUtils.isEmpty(message)){
            return;
        }
        if(message.toString().startsWith("系统错误")){
            return;
        }
        if (isShow){
            Toast.makeText(BaseApplication.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 自定义显示Toast时间
     * @param duration 单位:毫秒
     */
    public static void show(CharSequence message, int duration) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(BaseApplication.getContext(), message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.show();
        }
    }



    /**
     * 自定义Toast的View
     * @param duration 单位:毫秒
     * @param view 显示自己的View
     */
    public static void customToastView( CharSequence message, int duration,View view) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(BaseApplication.getContext(), message, duration);
            } else {
                mToast.setText(message);
            }
            if(view != null){
                mToast.setView(view);
            }
            mToast.show();
        }
    }



    /**
     * 自定义带图片和文字的Toast，最终的效果就是上面是图片，下面是文字
     * @param iconResId 图片的资源id,如:R.drawable.icon
     */
    public static void showToastWithImageAndText( CharSequence message, int iconResId,int duration,int gravity, int xOffset, int yOffset) {
        if (isShow){
            if (mToast == null) {
                mToast = Toast.makeText(BaseApplication.getContext(), message, duration);
            } else {
                mToast.setText(message);
            }
            mToast.setGravity(gravity, xOffset, yOffset);
            LinearLayout toastView = (LinearLayout) mToast.getView();
            ImageView imageView = new ImageView(BaseApplication.getContext());
            imageView.setImageResource(iconResId);
            toastView.addView(imageView, 0);
            mToast.show();
        }
    }

}
