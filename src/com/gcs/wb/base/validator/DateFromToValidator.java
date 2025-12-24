/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.validator;

import com.gcs.wb.base.constant.Constants;
import java.util.Date;
import org.springframework.util.Assert;

/**
 * @author thanghl
 */
public class DateFromToValidator extends AbstractThrowableValidator<String, IllegalArgumentException> {

    public DateFromToValidator() {
    }

    public void validate(Date from, Date to) throws IllegalArgumentException {
        if (from != null && to != null) {
            boolean isValid = from.compareTo(to) <= 0 && to.compareTo(Constants.Date.MAX_DATE) <= 0;
            Assert.isTrue(isValid, "Date from must be <= Date to");
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void validate(String input) throws IllegalArgumentException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
