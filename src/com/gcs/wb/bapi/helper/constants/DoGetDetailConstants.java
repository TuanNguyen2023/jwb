/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author Tran-Vu
 */
public interface DoGetDetailConstants extends BapiConstants {

    String BAPI_NAME = "ZBAPI_WB2_DO_GET_DETAIL";
    // for import param    
    String ID_DO = "ID_DO";
    // for export params
    String ES_BZIRK = "ES_BZIRK";
    String ES_TEXT = "ES_TEXT";
    String ES_VSTEL = "ES_VSTEL";
    String ES_VSTEL_TXT = "ES_VSTEL_TXT";
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    String TD_DOS = "TD_DOS";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TD_DOS Structure">
    String VBELN = "VBELN";
    String POSNR = "POSNR";
    String ERDAT = "ERDAT";
    String LFART = "LFART";
    String AUTLF = "AUTLF";
    String WADAT = "WADAT";
    String LDDAT = "LDDAT";
    String KODAT = "KODAT";
    String VSTEL = "VSTEL";
    String LIFNR = "LIFNR";
    String KUNNR = "KUNNR";
    String KUNAG = "KUNAG";
    String TRATY = "TRATY";
    String TRAID = "TRAID";
    String BLDAT = "BLDAT";
    String MATNR = "MATNR";
    String WERKS = "WERKS";
    String LGORT = "LGORT";
    String CHARG = "CHARG";
    String LICHN = "LICHN";
    String LFIMG = "LFIMG";
    String MEINS = "MEINS";
    String VRKME = "VRKME";
    String UEBTK = "UEBTK";
    String UEBTO = "UEBTO";
    String UNTTO = "UNTTO";
    String ARKTX = "ARKTX";
    String VGBEL = "VGBEL";
    String VGPOS = "VGPOS";
    String BWTAR = "BWTAR";
    String BWART = "BWART";
    String RECV_PLANT = "RECV_PLANT";
    String KOSTK = "KOSTK";
    String KOQUK = "KOQUK";
    String WBSTK = "WBSTK";
    //{+20100212#01 Add new fields
    //Higher-level item in bill of material structures
    //Sales document item category
    String UEPOS = "UEPOS";
    String PSTYV = "PSTYV";
    //}+20100212#01 Add new fields

    // </editor-fold>
}
