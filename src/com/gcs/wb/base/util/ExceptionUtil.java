/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import com.sap.conn.jco.JCoException;
import com.gcs.wb.base.constant.Constants;
import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author thanghl
 */
public class ExceptionUtil {

    public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
        throw (E) e;
    }

    public static boolean isSapDisConnectedException(Throwable ex) {
        if (ex.getCause() instanceof JCoException) {
            JCoException jcoException = (JCoException) ex.getCause();
            if (jcoException.getGroup() == JCoException.JCO_ERROR_COMMUNICATION) {
                return true;
            }
        }

        return false;
    }

    public static void checkDatabaseDisconnectedException(Exception ex) {
        if (ex.getCause() instanceof DatabaseException) {
            DatabaseException dbex = (DatabaseException) ex.getCause();
            if (dbex.isCommunicationFailure()) {
                sneakyThrow(new Exception(Constants.Message.DB_DISCONNECTED));
            }
        }

        sneakyThrow(ex);
    }
}
