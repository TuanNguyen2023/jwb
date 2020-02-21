/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.enums;

/**
 *
 * @author thangtp.nr
 */
public enum ModeEnum {

    IN_PO_PURCHASE("PO Mua Hàng"),
    IN_WAREHOUSE_TRANSFER("Chuyển Kho"),
    IN_OTHER("Cân khác"),
    OUT_SELL_ROAD("Bán Hàng (Bộ)"),
    OUT_PLANT_PLANT("Plant - Plant"),
    OUT_SLOC_SLOC("Sloc - Sloc"),
    OUT_PULL_STATION("Nhập Bến Kéo"),
    OUT_SELL_WATERWAY("Bán Hàng (Thủy)"),
    OUT_OTHER("Cân khác");
    private String name;

    private ModeEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
