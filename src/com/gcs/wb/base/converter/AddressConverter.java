/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import java.util.List;

public class AddressConverter extends AbstractThrowableConverter<Object, Object, Exception>{

    @Override
    public Object convert(Object from){
        Object to = new Object();

        return to;
    }

    @Override
    public Object convertHasParameter(Object from, String val) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object convertsHasParameter(Object from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
