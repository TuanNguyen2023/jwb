/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PoGetDetailConstants;
import java.io.Serializable;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class PoGetDetailHeaderStructure implements Serializable {

    /**Purchasing Document Number*/
    @Parameter(PoGetDetailConstants.PO_NUMBER)
    private String _PO_NUMBER;
    /**Purchasing Document Type*/
    @Parameter(PoGetDetailConstants.DOC_TYPE)
    private String _DOC_TYPE;
    /**Deletion Indicator in Purchasing Document*/
    @Parameter(PoGetDetailConstants.DELETE_IND)
    private String _DELETE_IND;
    /**Status of Purchasing Document*/
    @Parameter(PoGetDetailConstants.STATUS)
    private String _STATUS;
    /**Date on Which Record Was Created*/
    @Parameter(PoGetDetailConstants.CREAT_DATE)
    private Date _CREAT_DATE;
    /**Vendor Account Number*/
    @Parameter(PoGetDetailConstants.VENDOR)
    private String _VENDOR;
    /**Supplying Vendor*/
    @Parameter(PoGetDetailConstants.SUPPL_VEND)
    private String _SUPPL_VEND;
    /**Customer Number 1*/
    @Parameter(PoGetDetailConstants.CUSTOMER)
    private String _CUSTOMER;
    /**Supplying (Issuing) Plant in Stock Transport Order*/
    @Parameter(PoGetDetailConstants.SUPPL_PLNT)
    private String _SUPPL_PLNT;
    /**Release Indicator: Purchasing Document*/
    @Parameter(PoGetDetailConstants.PO_REL_IND)
    private String _PO_REL_IND;
    /**Release status*/
    @Parameter(PoGetDetailConstants.REL_STATUS)
    private String _REL_STATUS;

    public PoGetDetailHeaderStructure() {
    }

    /**
     * Purchasing Document Number
     * @return the _PO_NUMBER
     */
    public String getPO_NUMBER() {
        return _PO_NUMBER;
    }

    /**
     * Purchasing Document Type
     * @return the _DOC_TYPE
     */
    public String getDOC_TYPE() {
        return _DOC_TYPE;
    }

    /**
     * Deletion Indicator in Purchasing Document
     * @return the _DELETE_IND
     */
    public String getDELETE_IND() {
        return _DELETE_IND;
    }

    /**
     * Status of Purchasing Document
     * @return the _STATUS
     */
    public String getSTATUS() {
        return _STATUS;
    }

    /**
     * Date on Which Record Was Created
     * @return the _CREAT_DATE
     */
    public Date getCREAT_DATE() {
        return _CREAT_DATE;
    }

    /**
     * Supplying Vendor
     * @return the _SUPPL_VEND
     */
    public String getSUPPL_VEND() {
        return _SUPPL_VEND;
    }

    /**
     * Customer Number 1
     * @return the _CUSTOMER
     */
    public String getCUSTOMER() {
        return _CUSTOMER;
    }

    /**
     * Supplying (Issuing) Plant in Stock Transport Order
     * @return the _SUPPL_PLNT
     */
    public String getSUPPL_PLNT() {
        return _SUPPL_PLNT;
    }

    /**
     * Release Indicator: Purchasing Document
     * @return the _PO_REL_IND
     */
    public String getPO_REL_IND() {
        return _PO_REL_IND;
    }

    /**
     * Release status
     * @return the _REL_STATUS
     */
    public String getREL_STATUS() {
        return _REL_STATUS;
    }

    /**
     * Vendor Account Number
     * @return the _VENDOR
     */
    public String getVENDOR() {
        return _VENDOR;
    }
}
