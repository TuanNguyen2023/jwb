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
public interface SyncContractSOGetListConstants extends BapiConstants {
    
    String BAPI_NAME = "ZWS2_SYNC_CONTRACT";
    
    String IV_KUNNR = "IV_KUNNR";
    String IV_DATE_F = "IV_DATE_F";
    String IV_DATE_T = "IV_DATE_T";
    String IV_VBELN = "IV_VBELN";
    String IV_OPTION = "IV_OPTION";
    
    String ET_SALESORDER = "ET_SALESORDER";
    String VBELN = "VBELN";
    String MATNR = "MATNR";
    String MAKTX = "MAKTX";
    String KUNNR = "KUNNR";
    String TRAID = "TRAID";
    String WERKS = "WERKS";
    // số lượng bán
    String KWMENG = "KWMENG";
    // số lượng KM
    String FREE_QUA = "FREE_QUA";
    // số lượng bồi hoàn
    String REC_QUA = "REC_QUA";
    // nơi nhận hàng
    String W_NAME = "W_NAME";
    // nơi giao hàng
    String SHIP_TO_NAME = "SHIP_TO_NAME";
    // ghi chú
    String NOTE = "NOTE";
    // status
    String CHANGED = "CHANGED";
    // infor customer
    String VSBED = "VSBED";
    String ZKVGR1 = "ZKVGR1";
    String ZKVGR1_TEXT = "ZKVGR1_TEXT";
    String ZKVGR2 = "ZKVGR2";
    String ZKVGR2_TEXT = "ZKVGR2_TEXT";
    String ZKVGR3 = "ZKVGR3";
    String ZKVGR3_TEXT = "ZKVGR3_TEXT";
    String SORT1 = "SORT1";
    
}
