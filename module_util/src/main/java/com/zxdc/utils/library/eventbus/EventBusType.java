package com.zxdc.utils.library.eventbus;

public class EventBusType {

    private int status;

    private Object object;

    private Object object2;

    public EventBusType(int status){
        this.status=status;
    }

    public EventBusType(int status,Object object){
        this.status=status;
        this.object=object;
    }

    public EventBusType(int status,Object object,Object object2){
        this.status=status;
        this.object=object;
        this.object2=object2;
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

    public Object getObject2() {
        return object2;
    }

    public void setObject2(Object object2) {
        this.object2 = object2;
    }
}
