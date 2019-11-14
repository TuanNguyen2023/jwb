/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.utils;

import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author admin
 */
public class Base64_Utils {
    private static int ENDETIMES_DEFAULT = 5;

    
    // NUMBEROFTIMES is the times encode or decode get from application.properties
    private static int NUMBEROFTIMES = 1;
    
    // this method is used to decode a byte[] array with recursion
    // Input: byte[], and the times
    // Output: byte[]
    public static byte[] decode(byte[] textInByte, int n) {
        if (n <= 0) {            
            return textInByte;
        } else {
            return decode(Base64.decodeBase64(textInByte),n-1);
        }
    }
    
    // this method is used to decode a string using NUMBEROFTIMES - static property of class
    // Input: string

    public static String decodeNTimes(String encodedText) {
        
     //    if ( CheckZZ(str) ) return "null" ; 
         
        return new String(decode(encodedText.getBytes(), NUMBEROFTIMES));        
    }
    // this method is used to decode a string using times - param in method
    // Input: string
    // Output: string
    public static String decodeDefinedTimes(String encodedText, int times) {
      //  if ( Thoa dieu kien ABC ) return "null" ; 
        
        return new String(decode(encodedText.getBytes(), times));        
    }
    
     public static String decodeDefinedTimes(String encodedText, int times, String wbid ) {
      //  if ( Thoa dieu kien ABC ) return "null" ; 

                   
        return new String(decode(encodedText.getBytes(), times));        
    }
     
    
    // this method is used to encode a byte[] array with recursion
    // Input: byte[], and the times
    // Output: byte[]
    public static byte[] encode(byte[] textInByte, int n) {
        if (n <= 0) {            
            return textInByte;
        } else {
            return encode(Base64.encodeBase64(textInByte),n-1);
        }
    }
    
    // this method is used to encode a string using NUMBEROFTIMES - static property of class
    // Input: string
    // Output: string    
    public static String encodeNTimes(String encodedText) {
        return new String(encode(encodedText.getBytes(), NUMBEROFTIMES));        
    }   
    
    // this method is used to encode a string using times - param in method
    // Input: string
    // Output: string
    public static String encodeDefinedTimes(String encodedText, int times) {
        return new String(encode(encodedText.getBytes(), times));        
    }

    public static int getNumberOfTimes() {
        return NUMBEROFTIMES;
    }

    public static void setNumberOfTimes(int numberOfTimes) {
        Base64_Utils.NUMBEROFTIMES = numberOfTimes;
    }
    
    public static int getENDETIMES_DEFAULT() {
        return ENDETIMES_DEFAULT;
    }
}
