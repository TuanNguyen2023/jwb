/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

/**
 *
 * @author THANGPT
 */
public abstract class AbstractThrowableParamConverter<FROM, TO, EXCEPTION extends Exception>
        implements IConverterParam<FROM, TO, EXCEPTION> {

    @Override
    public abstract TO convertHasParameter(FROM from, String val) throws EXCEPTION;

    @Override
    public abstract TO convertsHasParameter(FROM from, String val, boolean refresh) throws EXCEPTION;
}
