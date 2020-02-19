/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;
import com.gcs.wb.bapi.helper.constants.VendorValiationCheckContants;
import java.io.Serializable;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HangTT
 */
@Bapi(VendorValiationCheckContants.BAPI_NAME)
public class VendorValiationCheckBapi implements Serializable {
    /**Import Parameter: IV_VENDOR(Account Number of Vendor or Creditor)*/
    @Import
    @Parameter(VendorValiationCheckContants.IV_VENDOR)
    private String _ivVendor;
    
    /**Import Parameter: IV_WERKS(Plant)*/
    @Import
    @Parameter(VendorValiationCheckContants.IV_WERKS)
    private String _ivWerks;
    
    /**Import Parameter: IV_MATNR(Material Number)*/
    @Import
    @Parameter(VendorValiationCheckContants.IV_MATNR)
    private String _ivMatnr;
    
    /**Import Parameter: IV_KSCHL(Condition type (ZIFQ or ZLCQ))*/
    @Import
    @Parameter(VendorValiationCheckContants.IV_KSCHL)
    private String _ivKschl;
    
    /**Export Parameter: EV_RETURN (Message Text)*/
    @Export
    @Parameter(value = VendorValiationCheckContants.EV_RETURN)
    private String _evReturn;

    public VendorValiationCheckBapi() {
    }

    /**
     * Import Parameter: IV_VENDOR (Vendor Number)
     * @param ivVendor the _ivVendor to set
     */
    public void setIvVendor(String ivVendor) {
        this._ivVendor = ivVendor;
    }
    
     /**
     * Import Parameter: IV_WERKS (Plant)
     * @param ivWerks the _ivWerks to set
     */
    public void setIvWerks(String ivWerks) {
        this._ivWerks = ivWerks;
    }
    
     /**
     * Import Parameter: IV_MATNR (Material)
     * @param ivMatnr the _ivMatnr to set
     */
    public void setIvMatnr(String ivMatnr) {
        this._ivMatnr = ivMatnr;
    }
    
     /**
     * Import Parameter: IV_KSCHL (Condition Type)
     * @param ivKschl the _ivKschl to set
     */
    public void setIvKschl(String ivKschl) {
        this._ivKschl = ivKschl;
    }

    /**
     * Export Parameter: EV_RETURN (Message text)
     * @return the _evReturn
     */
    public String getEvReturn() {
        return _evReturn;
    }
}
