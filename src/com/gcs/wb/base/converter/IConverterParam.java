/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

/**
 *
 * @author THANGPT
 */
public interface IConverterParam <FROM, TO, EXCEPTION extends Exception> {

    TO convertHasParameter(FROM from, String val) throws EXCEPTION;
    
    TO convertsHasParameter(FROM from, String val, boolean refresh) throws EXCEPTION;
}
