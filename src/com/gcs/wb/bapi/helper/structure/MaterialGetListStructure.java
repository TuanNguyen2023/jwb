/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MaterialGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class MaterialGetListStructure implements Serializable {
    /**Material Number*/
    @Parameter(value = MaterialGetListConstants.MATNR)
    private String _matnr;
    
    /**Material Description (Short Text)*/
    @Parameter(value = MaterialGetListConstants.MAKTX)
    private String _maktx;
    
    /**Material Description (Long Text)*/
    @Parameter(value = MaterialGetListConstants.MAKTX_LONG)
    private String _maktxLong;
    
    /**Base Unit of Measure*/
    @Parameter(value = MaterialGetListConstants.MEINS)
    private String _meins;
    
    /**Plant*/
    @Parameter(value = MaterialGetListConstants.WERKS)
    private String _werks;
    
    /**Storage Location*/
    @Parameter(value = MaterialGetListConstants.LGORT)
    private String _lgort;
    
    /**Flag Material for Deletion at Plant Level*/
    @Parameter(value = MaterialGetListConstants.LVORM)
    private String _lvorm;

    /**Flag Material for Xi mang bao*/
    @Parameter(value = MaterialGetListConstants.GROES)
    private String _groes;

    public MaterialGetListStructure() {
        
    }
    
    /**
     * Material Number
     * @return the _matnr
     */
    public String getMatnr() {
        return _matnr;
    }
    
    /**
     * Material Description (Short Text)
     * @return the _maktx
     */
    public String getMaktx() {
        return _maktx;
    }
    
    /**
     * Material Description (Long Text)
     * @return the _maktxLong
     */
    public String getMaktxLong() {
        return _maktxLong;
    }
    
    /**
     * Base Unit of Measure
     * @return the _meins
     */
    public String getMeins() {
        return _meins;
    }
    
    /**
     * Plant
     * @return the _werks
     */
    public String getWerks() {
        return _werks;
    }
    
    /**
     * Storage Location
     * @return the _lgort
     */
    public String getLgort() {
        return _lgort;
    }
    
    /**
     * Flag Material
     * @return the _lvorm
     */
    public String getLvorm() {
        return _lvorm;
    }

    /**
     * Flag Material xi mang bao
     * @return the _groes
     */
    public String getGroes() {
        return _groes;
    }
}
