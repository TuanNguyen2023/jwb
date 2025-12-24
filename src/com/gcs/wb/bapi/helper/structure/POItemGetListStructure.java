/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PoPostGetListConstants;
import java.io.Serializable;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class POItemGetListStructure implements Serializable {
    @Parameter(PoPostGetListConstants.EBELP)
    private String _ebelp;
    @Parameter(PoPostGetListConstants.MATNR)
    private String _matnr;
    @Parameter(PoPostGetListConstants.TXZ01)
    private String _txz01;
    @Parameter(PoPostGetListConstants.WERKS)
    private String _werks;
    @Parameter(PoPostGetListConstants.LGORT)
    private String _lgort;
    @Parameter(PoPostGetListConstants.MEINS)
    private String _meins;
    @Parameter(PoPostGetListConstants.MENGE)
    private String _menge;
    @Parameter(PoPostGetListConstants.NETWR)
    private String _netwr;
    @Parameter(PoPostGetListConstants.EINDT)
    private Date _eindt;
    
    public POItemGetListStructure() {
        
    }
    
    /**
     * @return the _ebelp
     */
    public String getEbelp() {
        return _ebelp;
    }

    /**
     * @param ebelp the _ebelp to set
     */
    public void setEbelp(String ebelp) {
        this._ebelp = ebelp;
    }

    /**
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }

    /**
     * @param matnr the _matnr to set
     */
    public void setMatnr(String matnr) {
        this._matnr = matnr;
    }
    
    /**
     * @return the _txz01
     */
    public String getTxz01() {
        return _txz01;
    }

    /**
     * @param txz01 the _txz01 to set
     */
    public void setTxz01(String txz01) {
        this._txz01 = txz01;
    }

    /**
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }

    /**
     * @param werks the _werks to set
     */
    public void setWerks(String werks) {
        this._werks = werks;
    }
    
    /**
     * @return the _lgort
     */
    public String getLgort() {
        return _lgort;
    }

    /**
     * @param lgort the _lgort to set
     */
    public void setLgort(String lgort) {
        this._lgort = lgort;
    }
    
    /**
     * @return the _meins
     */
    public String getMeins() {
        return _meins;
    }

    /**
     * @param meins the _meins to set
     */
    public void setMeins(String meins) {
        this._meins = meins;
    }
    
    /**
     * @return the _menge
     */
    public String getMenge() {
        return _menge;
    }

    /**
     * @param menge the _menge to set
     */
    public void setMenge(String menge) {
        this._menge = menge;
    }
    
    /**
     * @return the _netwr
     */
    public String getNetwr() {
        return _netwr;
    }

    /**
     * @param netwr the _netwr to set
     */
    public void setNetwr(String netwr) {
        this._netwr = netwr;
    }
    
    /**
     * @return the _eindt
     */
    public Date getEindt() {
        return _eindt;
    }

    /**
     * @param eindt the _eindt to set
     */
    public void setEindt(Date eindt) {
        this._eindt = eindt;
    }
    
}
