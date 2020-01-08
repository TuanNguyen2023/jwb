/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pho.vo
 */
public class PhoneValidator extends AbstractThrowableValidator<String, IllegalArgumentException> {

	public static final String PHONE_REGEX_DASH = "^[0-9]{3}-[0-9]{4}-[0-9]{4}$";
	public static final String PHONE_REGEX = "^\\d{11}$";

	@Override
	public void validate(String input) throws IllegalArgumentException {
		if (StringUtils.hasText(input)) {
			String phoneRegex = "";
			if(input.contains("-")){
				phoneRegex = PHONE_REGEX_DASH;
			} else {
				phoneRegex = PHONE_REGEX;
			}
			
			Matcher matcher = Pattern.compile(phoneRegex).matcher(input);
			Assert.isTrue(matcher.find(), "invalid phone number@" + input);
		}
	}
}
