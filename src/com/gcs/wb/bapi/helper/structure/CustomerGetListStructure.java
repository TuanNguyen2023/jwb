/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.CustomerGetListConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class CustomerGetListStructure implements Serializable{
    /**Client Number*/
    @Parameter(CustomerGetListConstants.MANDT)
    private String _mandt;
    /**Customer Number*/
    @Parameter(CustomerGetListConstants.KUNNR)
    private String _kunnr;
    /** Sales org*/
    @Parameter(CustomerGetListConstants.VKORG)
    private String _vkorg;
    /**Name 1*/
    @Parameter(CustomerGetListConstants.NAME1)
    private String _name1;
    /**Name 2*/
    @Parameter(CustomerGetListConstants.NAME2)
    private String _name2;
    /**Name 3*/
    @Parameter(CustomerGetListConstants.NAME3)
    private String _name3;
    /**Name 4*/
    @Parameter(CustomerGetListConstants.NAME4)
    private String _name4;

    public CustomerGetListStructure() {
    }

   /**
     * Client Number
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * Customer Number
     * @return the _kunnr
     */
    public String getKunnr() {
        return _kunnr;
    }

    /**
     * Sales org
     * @return the _vkorg
     */
    public String getVkorg() {
        return _vkorg;
    }

    /**
     * Name 1
     * @return the _name1
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
     * Name 3
     * @return the _name3
     */
    public String getName3() {
        return _name3;
    }
    
    /**
     * Name 4
     * @return the _name4
     */
    public String getName4() {
        return _name4;
    }
}
