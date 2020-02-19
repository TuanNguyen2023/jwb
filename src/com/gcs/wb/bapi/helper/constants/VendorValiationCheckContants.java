/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author HANGTT
 */
public interface VendorValiationCheckContants extends BapiConstants{
    String BAPI_NAME = "ZMM_FM_WB_VENDOR_CHECK_COND";
    /**Import Parameter*/
    String IV_VENDOR = "IV_VENDOR";
    String IV_WERKS = "IV_WERKS";
    String IV_MATNR = "IV_MATNR";
    String IV_KSCHL = "IV_KSCHL";
    
    /**Export Parameter*/
    /**Message Text*/
    String EV_RETURN = "EV_RETURN";
}
