package ru.bormoshka.mock.plugins.dao.model;

import java.io.Serializable;

/**
 * смс сообщение
 */
public class SmsModel implements Serializable {
    private String msid;
    private String phone;
    private String text;
    private String status;
    private String date;
    private String info;
    private Integer touchCount = 0;

    public SmsModel(String msid, String phone, String text, String status, String date, String info, int touchCount) {
        this.msid = msid;
        this.phone = phone;
        this.text = text;
        this.status = status;
        this.date = date;
        this.info = info;
        this.touchCount = touchCount;
    }

    public SmsModel() {
    }


    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMsid() {
        return msid;
    }

    public void setMsid(String msid) {
        this.msid = msid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTouchCount() {
        return touchCount;
    }

    public void setTouchCount(Integer touchCount) {
        this.touchCount = touchCount;
    }
}
