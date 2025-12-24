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

    public boolean isInvalidTolerance(BigDecimal registeredQty, BigDecimal usedQty, BigDecimal tolerance) {
        if (registeredQty.doubleValue() == usedQty.doubleValue()) {
            return false;
        }

        double value = 100 - (usedQty.doubleValue() / registeredQty.doubleValue()) * 100;

        return value > tolerance.doubleValue() || value < tolerance.doubleValue() * (-1);
    }
}
