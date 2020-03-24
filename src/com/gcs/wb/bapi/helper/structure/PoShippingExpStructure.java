/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.PoGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author thanghl
 */
@BapiStructure
public class PoShippingExpStructure implements Serializable {

    /**
     * Purchasing Document Number
     */
    @Parameter(PoGetDetailConstants.PO_ITEM)
    private String _PO_ITEM;
    /**
     * Customer
     */
    @Parameter(PoGetDetailConstants.CUSTOMER)
    private String _CUSTOMER;

    public PoShippingExpStructure() {
    }

    /**
     * Item Number of Purchasing Document
     *
     * @return the _PO_ITEM
     */
    public String getPO_ITEM() {
        return _PO_ITEM;
    }

    /**
     * Customer
     *
     * @return the _CUSTOMER
     */
    public String getCUSTOMER() {
        return _CUSTOMER;
    }
}
