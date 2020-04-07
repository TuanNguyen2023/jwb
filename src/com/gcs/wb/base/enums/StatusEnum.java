package com.gcs.wb.base.enums;

/**
 * Created by PHUCPH on 1/31/2020.
 */
public enum StatusEnum {
    ALL("Tất cả"),
    POSTED("Hoàn tất"),
    UNFINISH("Chưa hoàn tất"),
    OFFLINE("Mất kết nối SAP");

    public final String VALUE;

    StatusEnum(String VALUE) {
        this.VALUE = VALUE;
    }

    public String getValue() {
        return VALUE;
    }
}
