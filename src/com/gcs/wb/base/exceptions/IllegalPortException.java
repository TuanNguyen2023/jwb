/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.exceptions;

/**
 *
 * @author Tran-Vu
 */
public class IllegalPortException extends Exception {

    public IllegalPortException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalPortException(String message) {
        super(message);
    }
}
