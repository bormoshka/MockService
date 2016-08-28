package ru.bormoshka.dao.model;

public enum SmsStatus {
    NEW("new"),
    NULL("null"),
    DELIVERED("delivered"),
    PENDING("pending"),
    SENDING("sending"),
    SENT("sent"),
    NOTSENT("notsent"),
    NOTDELIVERED("notdelivered"),
    UNKNOWN("unknown");

    private String code;

    SmsStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
