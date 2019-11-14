/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.CustomerGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class CustomerGetDetailStructure implements Serializable {

    /**Client Number*/
    @Parameter(CustomerGetDetailConstants.MANDT)
    private String _mandt;
    /**Customer Number*/
    @Parameter(CustomerGetDetailConstants.KUNNR)
    private String _kunnr;
    /**Name 1*/
    @Parameter(CustomerGetDetailConstants.NAME1)
    private String _name1;
    /**Name 2*/
    @Parameter(CustomerGetDetailConstants.NAME2)
    private String _name2;

    public CustomerGetDetailStructure() {
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
}
