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

    ALL("Tất cả"),
    IN_PO_PURCHASE("Nhập - PO Mua Hàng"),
    IN_WAREHOUSE_TRANSFER("Nhập - Chuyển Kho"),
    IN_OTHER("Nhập - Cân khác"),
    OUT_SELL_ROAD("Xuất - Bán Hàng (Bộ)"),
    OUT_PLANT_PLANT("Xuất - VCNB - Ngoài NM"),
    OUT_SLOC_SLOC("Xuất - VCNB - Trong NM"),
    OUT_PULL_STATION("Xuất - Nhập Bến Kéo"),
    OUT_SELL_WATERWAY("Xuất - Bán Hàng (Thủy)"),
    OUT_OTHER("Xuất - Cân khác");
    private String name;

    private ModeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
