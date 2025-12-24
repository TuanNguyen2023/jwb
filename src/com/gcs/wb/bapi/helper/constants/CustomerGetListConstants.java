/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author HANGTT
 */
public interface CustomerGetListConstants extends BapiConstants {
    String BAPI_NAME = "ZMM_FM_WB_CUSTOMER_MASTER";
    //import
    String IV_START_DATE = "IV_START_DATE";
    String IV_START_TIME = "IV_START_TIME";
    String IV_END_DATE = "IV_END_DATE";
    String IV_END_TIME = "IV_END_TIME";
    String IV_INDICATOR = "IV_INDICATOR";
    
    // export
    String ET_CUSTOMER = "ET_CUSTOMER";
    
    String MANDT = "MANDT";
    String KUNNR = "KUNNR";
    String VKORG = "VKORG";
    String NAME1 = "NAME1";
    String NAME2 = "NAME2";
    String NAME3 = "NAME3";
    String NAME4 = "NAME4";
}
