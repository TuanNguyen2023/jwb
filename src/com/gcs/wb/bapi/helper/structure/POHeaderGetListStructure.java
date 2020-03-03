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
public class POHeaderGetListStructure implements Serializable {
    @Parameter(PoPostGetListConstants.EBELN)
    private String _ebeln;
    @Parameter(PoPostGetListConstants.BSART)
    private String _bsart;
    @Parameter(PoPostGetListConstants.LOEKZ)
    private String _lorkz;
    @Parameter(PoPostGetListConstants.AEDAT)
    private Date _aedat;
    @Parameter(PoPostGetListConstants.LIFNR)
    private String _lifnr;
    @Parameter(PoPostGetListConstants.RESWK)
    private String _reswk;
    @Parameter(PoPostGetListConstants.PROCSTAT)
    private String _procstat;
    @Parameter(PoPostGetListConstants.MEMORY)
    private String _memory;
    
    public POHeaderGetListStructure() {
        
    }
    
    /**
     * @return the _ebeln
     */
    public String getEbeln() {
        return _ebeln;
    }

    /**
     * @param ebeln the _ebeln to set
     */
    public void setEbeln(String ebeln) {
        this._ebeln = ebeln;
    }
    
    /**
     * @return the _bsart
     */
    public String getBsart() {
        return _bsart;
    }

    /**
     * @param bsart the _bsart to set
     */
    public void setBsart(String bsart) {
        this._bsart = bsart;
    }
    
    /**
     * @return the _lorkz
     */
    public String getLorkz() {
        return _lorkz;
    }

    /**
     * @param lorkz the lorkz to set
     */
    public void setLorkz(String lorkz) {
        this._lorkz = lorkz;
    }

    /**
     * @return the _aedat
     */
    public Date getAedat() {
        return _aedat;
    }

    /**
     * @param aedat the _aedat to set
     */
    public void setAedat(Date aedat) {
        this._aedat = aedat;
    }
    
    /**
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
    }

    /**
     * @param lifnr the _lifnr to set
     */
    public void setLifnr(String lifnr) {
        this._lifnr = lifnr;
    }
    
    /**
     * @return the _reswk
     */
    public String getReswk() {
        return _reswk;
    }

    /**
     * @param reswk the _reswk to set
     */
    public void setReswk(String reswk) {
        this._reswk = reswk;
    }

    /**
     * @return the _procstat
     */
    public String getProcstat() {
        return _procstat;
    }

    /**
     * @param procstat the _procstat to set
     */
    public void setProcstat(String procstat) {
        this._procstat = procstat;
    }

    /**
     * @return the _memory
     */
    public String getMemory() {
        return _memory;
    }

    /**
     * @param memory the _memory to set
     */
    public void setMemory(String memory) {
        this._memory = memory;
    }

}
