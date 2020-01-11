/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.validator;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * @author thanghl
 */
public class LengthValidator extends AbstractThrowableValidator<String, IllegalArgumentException> {

    private int min;
    private int max;

    public LengthValidator() {
    }

    public LengthValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void validate(String input) throws IllegalArgumentException {
        if (StringUtils.hasText(input)) {
            boolean isValid = this.min <= input.length() && input.length() <= this.max;
            Assert.isTrue(isValid, "invalid text length [min=" + this.min + ", max=" + this.max + "]@" + input);
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
