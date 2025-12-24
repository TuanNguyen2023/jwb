/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.VendorGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class VendorGetDetailStructure implements Serializable {

    /**Client Number*/
    @Parameter(VendorGetDetailConstants.MANDT)
    private String _mandt;
    /**Vendor Number*/
    @Parameter(VendorGetDetailConstants.LIFNR)
    private String _lifnr;
    /**Name 1*/
    @Parameter(VendorGetDetailConstants.NAME1)
    private String _name1;
    /**Name 2*/
    @Parameter(VendorGetDetailConstants.NAME2)
    private String _name2;

    public VendorGetDetailStructure() {
    }

    /**
     * Client Number
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * Vendor Number
     * @return the _lifnr
     */
    public String getLifnr() {
        return _lifnr;
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
