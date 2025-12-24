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
public interface TransportagentGetListConstants  extends BapiConstants{
    
    String BAPI_NAME = "ZMM_FM_WB_VENDOR_MASTER";
    /**Import Parameter*/
    String IV_EKORG = "IV_EKORG";
    String IV_START_DATE = "IV_START_DATE";
    String IV_START_TIME = "IV_START_TIME";
    String IV_END_TIME = "IV_END_TIME";
    String IV_INDICATOR = "IV_INDICATOR";
    
    /**Export Parameter*/
    String ET_VENDOR = "ET_VENDOR";
    /**LIFNR*/
    String LIFNR = "LIFNR";
    /**Name 1*/
    String NAME1 = "NAME1";
    /**Name 2*/
    String NAME2 = "NAME2";
    /** Wplant */
    String EKORG = "EKORG";
    /**LOEVM*/
    String LOEVM = "LOEVM";
    /** Vendor account group */
    /**KTOKK*/
    String KTOKK = "KTOKK";
    // </editor-fold>
}
