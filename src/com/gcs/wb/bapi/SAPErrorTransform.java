/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi;

import org.hibersap.SapException;

/**
 *
 * @author vunguyent
 */
public class SAPErrorTransform {

    public static String getMessage(SapException.SapError error) {
        String result = null;
        if (error.getId().equalsIgnoreCase("M7") && error.getNumber().equalsIgnoreCase("006")) {
            result = "Vật tư không tồn tại trong Kho và Lô được chọn!!!";
        }
        if (error.getId().equalsIgnoreCase("M3") && error.getNumber().equalsIgnoreCase("897")) {
            String[] msg = error.getMessage().split(" ");
            result = "Thông tin vật tư bị khóa bởi người dùng " + msg[msg.length - 1];
        }
        return result;
    }
}
