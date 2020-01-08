/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.validator;

import java.util.List;

/**
 * @author pho.vo
 */
public interface IValidator<INPUT, EXCEPTION extends Exception>
{
    void validate(INPUT input) throws EXCEPTION;

    void validate(List<INPUT> inputList) throws EXCEPTION;
}
