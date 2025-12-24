/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.TransportagentGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class TransportagentGetListStructure implements Serializable{
    
    /**Ma DVVC*/
    @Parameter(TransportagentGetListConstants.LIFNR)
    private String _lifnr = null;
    
    /**Name 1*/
    @Parameter(TransportagentGetListConstants.NAME1)
    private String _name1 = null;
    
    /**Name 2*/
    @Parameter(TransportagentGetListConstants.NAME2)
    private String _name2 = null;
    
    /** Ma Plant nha may */
    @Parameter(TransportagentGetListConstants.EKORG)
    private String _ekorg = null;
    
    /**LOEVM*/
    @Parameter(TransportagentGetListConstants.LOEVM)
    private String _loevm = null;
    
    /**KTOKK*/
    @Parameter(TransportagentGetListConstants.KTOKK)
    private String _ktokk;
    
    public TransportagentGetListStructure() {
    }
    
    public TransportagentGetListStructure(String lifnr, String name1, String name2, String ekorg, String loevm, String ktokk) {
        this._lifnr = lifnr;
        this._name1 = name1;
        this._name2 = name2;
        this._ekorg = ekorg;
        this._loevm = loevm;
        this._ktokk = ktokk;
    }
    
    /**
     * Ma DVVC
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
    }
    
     /**
     * Name 1
     * @return the _Name1
     */
    public String getName1() {
        return _name1;
    }
    
     /**
     * Name 2
     * @return the _name2
     */
    public String getName2() {
        return _name2;
    }
    
     /**
     * Plant nha may
     * @return the _ekorg
     */
    public String getEkorg() {
        return _ekorg;
    }
    
     /**
     * LOEVM
     * @return the _loevm
     */
    public String getLoevm() {
        return _loevm;
    }

    /**
     * KTOKK
     * @return the _ktokk
     */
    public String getKtokk() {
        return _ktokk;
    }
}
