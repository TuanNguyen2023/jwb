/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.CustomerGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author HANGTT
 */
@BapiStructure
public class CustomerAdrcGetDetailStructure implements Serializable {
    /**Client Number*/
    @Parameter(CustomerGetDetailConstants.ADDRNUMBER)
    private String _addrnumber;
    /**Customer Number*/
    @Parameter(CustomerGetDetailConstants.NAME1)
    private String _name1;
    /**Name 1*/
    @Parameter(CustomerGetDetailConstants.NAME2)
    private String _name2;
    /**Name 2*/
    @Parameter(CustomerGetDetailConstants.NAME3)
    private String _name3;
    /**Name 2*/
    @Parameter(CustomerGetDetailConstants.NAME4)
    private String _name4;

    public CustomerAdrcGetDetailStructure() {
    }

    /**
     * Addr Number
     * @return the _addrnumber
     */
    public String getAddrnumber() {
        return _addrnumber;
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
     * Name 4
     * @return the _name4
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
