/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author Tran-Vu
 */
public interface GoodsMvtCreateConstants extends BapiConstants {

 //  String BAPI_NAME = "ZBAPI_GOODSMVT_CREATE_V2";
  String BAPI_NAME = "ZJBAPI_GOODSMVT_CREATE_V2_2606";
    // <editor-fold defaultstate="collapsed" desc="Import Parameteres">
    String GOODSMVT_HEADER = "GOODSMVT_HEADER";
    String GOODSMVT_CODE = "GOODSMVT_CODE";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Export Parameteres">
    String MATERIALDOCUMENT = "MATERIALDOCUMENT";
    String MATDOCUMENTYEAR = "MATDOCUMENTYEAR";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameteres">
    String GOODSMVT_ITEM = "GOODSMVT_ITEM";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GOODSMVT_HEADER Structure">
    String PSTNG_DATE = "PSTNG_DATE";
    String DOC_DATE = "DOC_DATE";
    String REF_DOC_NO = "REF_DOC_NO";
    String REF_DOC_NO_LONG = "REF_DOC_NO_LONG";
    /**Truck ID*/
    String BILL_OF_LADING = "BILL_OF_LADING";
    /**Identification Card*/
    String GR_GI_SLIP_NO = "GR_GI_SLIP_NO";
    /**Document Header Text*/
    String HEADER_TXT = "HEADER_TXT";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GOODSMVT_CODE Structure">
    String GM_CODE = "GM_CODE";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="GOODSMVT_ITEM Structure">
    String PLANT = "PLANT";
    String STGE_LOC = "STGE_LOC";
    String BATCH = "BATCH";
    String MOVE_TYPE = "MOVE_TYPE";
    String ENTRY_QNT = "ENTRY_QNT";
    String ENTRY_UOM = "ENTRY_UOM";
    String ENTRY_UOM_ISO = "ENTRY_UOM_ISO";
    String GR_RCPT = "GR_RCPT";
    String MVT_IND = "MVT_IND";
    String NO_MORE_GR = "NO_MORE_GR";
    String MOVE_REAS = "MOVE_REAS";
    String ITEM_TEXT = "ITEM_TEXT";
    // </editor-fold>
//    {[M001-DungDang-27062013] - fixing bug double post GoodsMovement
    String I_WEIGHTTICKET = "I_WEIGHTTICKET";
    String CLIENT = "CLIENT";
    String WPLANT = "WPLANT";
    String WB_ID = "WB_ID";
    String WT_ID = "WT_ID";
    String PO_NUMBER = "PO_NUMBER";
    String DO_NUMBER = "DO_NUMBER";    
    String DO_WT = "DO_WT"; 
    String MATDOC = "MATDOC";
    String DOC_YEAR = "DOC_YEAR";
    String USERNAME = "USERNAME";
    String CREATE_DATE = "CREATE_DATE";
    String CREATE_TIME = "CREATE_TIME";
    String FDATE = "FDATE";
    String FTIME = "FTIME";
    String SDATE = "SDATE";
    String STIME = "STIME";
    String MVT_TYPE = "MVT_TYPE";
    String SLOC = "SLOC";
//    String BATCH = "BATCH";
    String VTYPE = "VTYPE";
    String FSCALE = "FSCALE";
    String SSCALE = "SSCALE";
    String REGQTY = "REGQTY";
    String GQTY = "GQTY";
    String REGQTY_WT = "REGQTY_WT";
    String GQTY_WT = "GQTY_WT";    
    String UNIT = "UNIT";
    String TRANSID = "TRANSID";
    String SHIPTYPE = "SHIPTYPE";
    String DRIVERN = "DRIVERN";
    String DRIVERID = "DRIVERID";
    String SALEDT = "SALEDT";
    String MATID = "MATID";
    String MATNAME = "MATNAME";
    String KUNNR = "KUNNR";
    String LIFNR = "LIFNR";
    String CAT_TYPE = "CAT_TYPE";
    String NTEXT = "NTEXT";
    String LFART = "LFART";
    String STATUS = "STATUS";
}
