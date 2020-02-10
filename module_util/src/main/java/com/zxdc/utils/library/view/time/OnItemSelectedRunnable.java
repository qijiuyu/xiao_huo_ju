package com.zxdc.utils.library.view.time;

public class OnItemSelectedRunnable implements Runnable {
    final WheelView loopView;

    public OnItemSelectedRunnable(WheelView loopview) {
        loopView = loopview;
    }

    @Override
    public void run() {
        loopView.onItemSelectedListener.onItemSelected(loopView.getCurrentItem());
    }
}

