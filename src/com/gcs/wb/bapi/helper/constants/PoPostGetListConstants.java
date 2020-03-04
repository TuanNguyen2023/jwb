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
public interface PoPostGetListConstants extends BapiConstants {
    
    String BAPI_NAME = "ZMM_FM_WB_PO_STO_OUTBOUND";
    //import
    String IV_START_DATE = "IV_START_DATE";
    String IV_START_TIME = "IV_START_TIME";
    String IV_END_DATE = "IV_END_DATE";
    String IV_END_TIME = "IV_END_TIME";
    String IV_INDICATOR = "IV_INDICATOR";
    
    String ET_PO_STO = "ET_PO_STO";
    String EV_MESSAGE_ID = "EV_MESSAGE_ID";
    
    String CT_PO_STO_HEADER = "CT_PO_STO_HEADER";
    
    String PO_HEADER = "PO_HEADER";
    String PO_ITEM = "PO_ITEM";
    
    // ZSMMVN_WB_PO_HEADER
    String EBELN = "EBELN";
    String BSART = "BSART";
    String LOEKZ = "LOEKZ";
    String AEDAT = "AEDAT";
    String LIFNR = "LIFNR";
    String RESWK = "RESWK";
    String PROCSTAT = "PROCSTAT";
    String MEMORY = "MEMORY";
    
    //ZTTMMVN_WB_PO_ITEM
    String EBELP = "EBELP";
    String MATNR = "MATNR";
    String TXZ01 = "TXZ01";
    String WERKS = "WERKS";
    String LGORT = "LGORT";
    String MEINS = "MEINS";
    String MENGE = "MENGE";
    String NETWR = "NETWR";
    String EINDT = "EINDT";
    
}
