package com.zxdc.utils.library.eventbus;

public class EventBusType {

    private int status;

    private Object object;

    public EventBusType(int status){
        this.status=status;
    }

    public EventBusType(int status,Object object){
        this.status=status;
        this.object=object;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
