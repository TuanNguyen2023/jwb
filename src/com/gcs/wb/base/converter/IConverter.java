/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import java.util.List;

public interface IConverter<FROM, TO, EXCEPTION extends Exception> {
    TO convert(FROM from) throws EXCEPTION;

    //List<TO> convert(List<FROM> from) throws EXCEPTION;
}
