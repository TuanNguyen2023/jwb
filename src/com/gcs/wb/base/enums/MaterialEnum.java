package com.gcs.wb.base.enums;

/**
 * Created by phov on 1/31/2020.
 */
public enum MaterialEnum {
    ALL("-2"),

    OTHER("-1");

    public final String VALUE;

    MaterialEnum(String VALUE) {
        this.VALUE = VALUE;
    }
}
