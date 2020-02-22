/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.jpa.entity.Configuration;

import java.math.BigDecimal;

/**
 *
 * @author PHUCPH
 */
public class ToleranceUtil {

    public static boolean isInvalidTolerance(BigDecimal registeredQty, BigDecimal usedQty, BigDecimal tolerance) {
        if (registeredQty.doubleValue() == usedQty.doubleValue()) {
            return false;
        }

        double value = 100 - (usedQty.doubleValue() / registeredQty.doubleValue()) * 100;

        return value > tolerance.doubleValue() || value < tolerance.doubleValue() * (-1);
    }

    public static void main(String[] args) {
        System.out.println("registeredQty 11, usedQty 9.3, tolerance 9, @" + ToleranceUtil.isInvalidTolerance(BigDecimal.valueOf(11d), BigDecimal.valueOf(9.3d), BigDecimal.valueOf(9d)));
        System.out.println("registeredQty 11, usedQty 12.2, tolerance 9, @" + ToleranceUtil.isInvalidTolerance(BigDecimal.valueOf(11d), BigDecimal.valueOf(12.2d), BigDecimal.valueOf(9d)));
        System.out.println("registeredQty 11, usedQty 11.2, tolerance 9, @" + ToleranceUtil.isInvalidTolerance(BigDecimal.valueOf(11d), BigDecimal.valueOf(11.2d), BigDecimal.valueOf(9d)));
        System.out.println("registeredQty 11, usedQty 10.9, tolerance 9, @" + ToleranceUtil.isInvalidTolerance(BigDecimal.valueOf(11d), BigDecimal.valueOf(10.9d), BigDecimal.valueOf(9d)));
    }
    
}
