/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PartnerGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author THANGHL
 */
@BapiStructure
public class PartnerGetListStructure implements Serializable{
    /**Customer Number*/
    @Parameter(PartnerGetListConstants.KUNNR)
    private String _kunnr;
    /**Sales Organization*/
    @Parameter(PartnerGetListConstants.VKORG)
    private String _vkorg;
    /**Distribution Channel*/
    @Parameter(PartnerGetListConstants.VTWEG)
    private String _vtweg;
    /**Division*/
    @Parameter(PartnerGetListConstants.SPART)
    private String _spart;
    /**Partner Function*/
    @Parameter(PartnerGetListConstants.PARVW)
    private String _parvw;
    /**Partner counter*/
    @Parameter(PartnerGetListConstants.PARZA)
    private String _parza;
    /**Customer number of business partner*/
    @Parameter(PartnerGetListConstants.KUNN2)
    private String _kunn2;
    
    public PartnerGetListStructure() {
    }

    /**
     * Customer Number
     * @return _kunnr
     */
    public String getKunnr() {
        return _kunnr;
    }

    /**
     * Sales Organization
     * @return _vkorg
     */
    public String getVkorg() {
        return _vkorg;
    }

    /**
     * Distribution Channel
     * @return _vtweg
     */
    public String getVtweg() {
        return _vtweg;
    }

    /**
     * Division
     * @return _spart
     */
    public String getSpart() {
        return _spart;
    }

    /**
     * Partner Function
     * @return _parvw
     */
    public String getParvw() {
        return _parvw;
    }

    /**
     * Partner counter
     * @return _parza
     */
    public String getParza() {
        return _parza;
    }

    /**
     * Customer number of business partner
     * @return _kunn2
     */
    public String getKunn2() {
        return _kunn2;
    }
}
