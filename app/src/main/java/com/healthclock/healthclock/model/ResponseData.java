package com.healthclock.healthclock.model;

import java.io.Serializable;

public class ResponseData<T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    private int status;
    private String msg;
    private T data;


    public int getErrorCode() {
        return status;
    }

    public void setErrorCode(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return msg;
    }

    public void setErrorMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}