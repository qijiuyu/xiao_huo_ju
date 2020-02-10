package com.zxdc.utils.library.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.zxdc.utils.library.base.BaseActivity;
import com.zxdc.utils.library.base.BaseApplication;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/**
 * @author Administrator
 */
public class Util extends ClassLoader {

    /**
     * 判断是否输入表情符号
     * @param string
     * @return
     */
    public static boolean isEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }


    /**
     * 保留小数的double数据
     * @param d
     * @return
     */
    public static String setDouble(double d,int type){
        DecimalFormat df=null;
        switch (type){
            case 0:
                df = new DecimalFormat("0");
                break;
            case 1:
                df = new DecimalFormat("0.0");
                break;
            case 2:
                df = new DecimalFormat("0.00");
                break;
            case 3:
                df = new DecimalFormat("0.000");
                break;
            case 4:
                df = new DecimalFormat("0.0000");
                break;
        }
        return df.format(d);
    }


    /**
     * double 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double sum(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.add(bd2).doubleValue();
    }

    /**
     * double 相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1,double d2){
        BigDecimal bd1 = new BigDecimal(Double.toString(d1));
        BigDecimal bd2 = new BigDecimal(Double.toString(d2));
        return bd1.subtract(bd2).doubleValue();
    }


    /**
     * 只允许字母、数字和汉字
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static boolean stringFilter(String str)throws PatternSyntaxException {
        String   regEx  =  "^[\\u4e00-\\u9fa5*a-zA-Z0-9]+$";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher   m   =   p.matcher(str);
        return   m.matches();
    }


    /**
     * 获取渠道名称
     * @return
     */
    public static String getChannel(Context mContext){
        final ApplicationInfo appInfo;
        try {
            appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            final String utmSource = appInfo.metaData.get("SENSORS_ANALYTICS_UTM_SOURCE")+"";
            return utmSource;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取屏幕宽高
     * @param context
     * @param type
     * @return
     */
    public static int getDeviceWH(Context context,int type){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 获取显示度量，该显示度量描述了显示的大小和密度
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        int height=metrics.heightPixels;

        if(type==1){
            return width;
        }else{
            return height;
        }
    }


    /**
     *获取状态栏高度
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        //状态栏不存在
        if((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN){
            LogUtils.e("++++++++++++++++++状态栏不存在");
            return 0;
        }

        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    /*
     * 设置控件所在的位置YY，并且不改变宽高，
     * XY为绝对位置
     */
    public static void setLayout(View view, int x, int y) {
        ViewGroup.MarginLayoutParams margin=new ViewGroup.MarginLayoutParams(view.getLayoutParams());
        margin.setMargins(x,y, x+margin.width, y+margin.height);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        view.setLayoutParams(layoutParams);
    }


    /**
     * 去掉标点符号
     * @param s
     * @return
     */
    public static String format(String s){
        String str=s.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？|-]", "");
        return str;
    }

    /**
     * 获取当前系统的版本名称
     *
     * @return
     */
    public static String getVersionName(Context mContext) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断两个数组中是否有相同的元素
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isRepeat(String str1,String str2){
        String[] strOne=str1.split(",");
        String[] strTwo=str2.split(",");
        boolean has = false;
        Set<String> set = new HashSet<>(Arrays.asList(strOne));
        set.retainAll(Arrays.asList(strTwo));
        if(set.size() > 0){
            has =  true;
        }
        return has;
    }


    /**
     * 复制文字
     * @param message
     */
    public static void copyTxt(String message){
        ClipboardManager cm = (ClipboardManager) BaseApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("text", message));
        if (cm.hasPrimaryClip()) {
            cm.getPrimaryClip().getItemAt(0).getText();
        }
        ToastUtil.showLong("复制成功");
    }

    public static int dip2px(Context context,float dpValue){
        final float scale = context.getResources ().getDisplayMetrics ().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 获取当前系统的版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            int version = packInfo.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
