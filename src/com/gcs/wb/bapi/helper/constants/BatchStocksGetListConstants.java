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
public interface BatchStocksGetListConstants extends BapiConstants {

    String BAPI_NAME = "ZBAPI_WB2_BATCH_STOCKS_LIST";
    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    String ID_MANDT = "ID_MANDT";
    String ID_WERKS = "ID_WERKS";
    String ID_LGORT = "ID_LGORT";
    String ID_MATNR = "ID_MATNR";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    String TD_BATCH_STOCKS = "TD_BATCH_STOCKS";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TD_BATCH_STOCKS Structure">
    String MANDT = "MANDT";
    String MATNR = "MATNR";
    String WERKS = "WERKS";
    String LGORT = "LGORT";
    String CHARG = "CHARG";
    String LVORM = "LVORM";
    // </editor-fold>
}
