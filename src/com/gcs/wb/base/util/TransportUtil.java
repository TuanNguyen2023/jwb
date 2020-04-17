/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import com.gcs.wb.base.constant.Constants;
import java.util.regex.Matcher;

/**
 *
 * @author HANGTT
 */
public class TransportUtil {
     public static String getTraty(String traid) {
        String traty = "";
        Matcher matcherXe = Constants.TransportAgent.LICENSE_PLATE_PATTERN.matcher(traid);
        Matcher matcherGhe = Constants.TransportAgent.LICENSE_PLATE_WATER_PATTERN.matcher(traid);
        if (matcherXe.matches()) {
            traty = Constants.PlateFormat.PLATE_XE;
        } else if (matcherGhe.matches()) {
            traty = Constants.PlateFormat.PLATE_GHE;
        }
        return traty;
    }
}
