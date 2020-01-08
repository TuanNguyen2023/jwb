/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractThrowableConverter<FROM, TO, EXCEPTION extends Exception>
        implements IConverter<FROM, TO, EXCEPTION> {
    @Override
    public abstract TO convert(FROM from) throws EXCEPTION;

    @Override
    public List<TO> convert(List<FROM> fromList) throws EXCEPTION {
        List<TO> toList = null;
        if (fromList != null) {
            toList = new ArrayList<TO>();
            for (FROM from : fromList) {
                toList.add(convert(from));
            }
        }

        return toList;
    }
}
