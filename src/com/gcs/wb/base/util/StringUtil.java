/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import org.apache.commons.lang.StringUtils;

/**
 *
 * @author gcsadmin
 */
public class StringUtil {

    public static String paddingZero(String input_string, int length) {
        String val = input_string;
        String zero = "0";
        while (val.length() < length) {
            val = zero.concat(val);
        }

        return val;
    }

    public static boolean isEmptyString(String input) {
        return StringUtils.isEmpty(input) || StringUtils.isEmpty(input.trim());
    }

    public static boolean isNotEmptyString(String input) {
        return !isEmptyString(input);
    }

    public static boolean hasLengthInRange(String str, int start, int end) {
        return isNotEmptyString(str) && str.length() >= start && str.length() <= end;
    }

    public static boolean contains(String source, String dest) {
        return isNotEmptyString(source) && isNotEmptyString(dest) && source.indexOf(dest) != -1;
    }

    public static String correctPlateNo(String input_string) {
        if (isNotEmptyString(input_string)) {
            return input_string.replace("-", "").replace(".", "");
        }

        return "";
    }
}
