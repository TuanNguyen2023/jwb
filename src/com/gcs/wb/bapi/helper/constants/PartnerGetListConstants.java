/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author THANGHL
 */
public interface PartnerGetListConstants extends BapiConstants {
    String BAPI_NAME = "ZMM_FM_WB_PARTNER_MASTER";
    //import
    String IV_START_DATE = "IV_START_DATE";
    String IV_START_TIME = "IV_START_TIME";
    String IV_END_DATE = "IV_END_DATE";
    String IV_END_TIME = "IV_END_TIME";
    String IV_INDICATOR = "IV_INDICATOR";
    
    // export
    String ET_PARTNER = "ET_PARTNER";
    
    String KUNNR = "KUNNR";
    String VKORG = "VKORG";
    String VTWEG = "VTWEG";
    String SPART = "SPART";
    String PARVW = "PARVW";
    String PARZA = "PARZA";
    String KUNN2 = "KUNN2";
}