package com.ylean.soft.lfd.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.ylean.soft.lfd.R;
import com.ylean.soft.lfd.utils.SelectTimeUtils;
import com.zxdc.utils.library.eventbus.EventBusType;
import com.zxdc.utils.library.eventbus.EventStatus;
import com.zxdc.utils.library.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
public class SelectTimeDialog extends Dialog implements View.OnClickListener {

    private View mContentView;
    private Activity context;
    private String strYear="",strMonth="",strDay="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(context);
        mContentView = inflater.inflate(R.layout.dialog_select_time, null);
        setContentView(mContentView);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = context.getResources().getDisplayMetrics().widthPixels; // 宽度
        initView();
        initListener();
    }

    public SelectTimeDialog(Activity context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    private void initView() {
        CycleWheelView year=findViewById(R.id.wv_year);
        year.setLabels(SelectTimeUtils.getYear());
        CycleWheelView month=findViewById(R.id.wv_month);
        month.setLabels(SelectTimeUtils.getMonth());
        CycleWheelView day=findViewById(R.id.wv_day);
        day.setLabels(SelectTimeUtils.getDay());

        //获取当前年月日
        Calendar calendar = Calendar.getInstance();
        //年
        int intYear = calendar.get(Calendar.YEAR);
        //月
        int intMonth = (calendar.get(Calendar.MONTH)+1);
        //日
        int intDay = calendar.get(Calendar.DAY_OF_MONTH);

        for(int i=0;i<SelectTimeUtils.getYear().size();i++){
            if(SelectTimeUtils.getYear().get(i).replace("年","").equals(String.valueOf(intYear))){
                year.setSelection(i);
                break;
            }
        }

        for(int i=0;i<SelectTimeUtils.getMonth().size();i++){
            if(Integer.parseInt(SelectTimeUtils.getMonth().get(i).replace("月",""))==intMonth){
                month.setSelection(i);
                break;
            }
        }

        for(int i=0;i<SelectTimeUtils.getDay().size();i++){
            if(Integer.parseInt(SelectTimeUtils.getDay().get(i).replace("日",""))==intDay){
                day.setSelection(i);
                break;
            }
        }


        try {
            year.setWheelSize(5);
            month.setWheelSize(5);
            day.setWheelSize(5);
        } catch (CycleWheelView.CycleWheelViewException e) {
            e.printStackTrace();
        }
        year.setCycleEnable(false);
        year.setAlphaGradual(0.5f);
        year.setDivider(Color.parseColor("#FFBC32"),1);
        year.setSolid(Color.WHITE,Color.WHITE);
        year.setLabelColor(Color.GRAY);
        year.setLabelSelectColor(Color.BLACK);
        year.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            public void onItemSelected(int position, String label) {
                strYear=label;
            }
        });

        month.setCycleEnable(false);
        month.setAlphaGradual(0.5f);
        month.setDivider(Color.parseColor("#FFBC32"),1);
        month.setSolid(Color.WHITE,Color.WHITE);
        month.setLabelColor(Color.GRAY);
        month.setLabelSelectColor(Color.BLACK);
        month.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            public void onItemSelected(int position, String label) {
                strMonth=label;
            }
        });

        day.setCycleEnable(false);
        day.setAlphaGradual(0.5f);
        day.setDivider(Color.parseColor("#FFBC32"),1);
        day.setSolid(Color.WHITE,Color.WHITE);
        day.setLabelColor(Color.GRAY);
        day.setLabelSelectColor(Color.BLACK);
        day.setOnWheelItemSelectedListener(new CycleWheelView.WheelItemSelectedListener() {
            public void onItemSelected(int position, String label) {
                strDay=label;
            }
        });

    }

    private void initListener() {
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                LogUtils.e("++++++++++++++++++"+strYear+strMonth+strDay);
                 EventBus.getDefault().post(new EventBusType(EventStatus.SHOW_SELECT_TIME,strYear+strMonth+strDay));
                 break;
           default:
               break;
        }
        dismiss();
    }
}
