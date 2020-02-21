/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author HANGTT
 */
public interface SOGetDetailConstants extends BapiConstants {
    String BAPI_NAME = "ZSD_FM_WB_SO_CHECK_MUTIPLE";
    // for import param    
    String IT_SO_CHECK = "IT_SO_CHECK";
    // for export params
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    String TD_DO_CHECK = "TD_DO_CHECK";
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="IT_SO_CHECK Structure">
    String VBELN = "VBELN";
    String TRAID = "TRAID";
    String TRAILER = "TRAILER";
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="TD_DO_CHECK Structure">
    String VBELN_SO = "VBELN_SO";
    String VBELN_DO = "VBELN_DO";
    String MESSAGE = "MESSAGE";
    // </editor-fold>
}
