/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

/**
 *
 * @author THANGLH
 */
public class IntegerUtil {

    public static Integer valueOf(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
