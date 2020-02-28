package com.gcs.wb.base.enums;

/**
 * Created by PHUCPH on 1/31/2020.
 */
public enum StatusEnum {
    ALL(0),

    POSTED(1),
    
    UNFINISH(2);
    
    public final int VALUE;

    StatusEnum(int VALUE) {
        this.VALUE = VALUE;
    }
}
