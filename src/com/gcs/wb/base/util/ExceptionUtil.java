/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import com.sap.conn.jco.JCoException;

/**
 *
 * @author thanghl
 */
public class ExceptionUtil {

    public static boolean isSapDisConnectedException(Throwable ex) {
        if (ex.getCause() instanceof JCoException) {
            JCoException jcoException = (JCoException) ex.getCause();
            if (jcoException.getGroup() == JCoException.JCO_ERROR_COMMUNICATION) {
                return true;
            }
        }

        return false;
    }
}
