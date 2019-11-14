/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MatAvailableConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class MatAvailableStructure implements Serializable {

    /**Committed Date*/
    @Parameter(MatAvailableConstants.COM_DATE)
    private Date _com_date;
    /**Comitted Quantity*/
    @Parameter(MatAvailableConstants.COM_QTY)
    private BigDecimal _com_qty;

    public MatAvailableStructure() {
    }

    /**
     * Committed Date
     * @return the _com_date
     */
    public Date getCom_date() {
        return _com_date;
    }

    /**
     * Comitted Quantity
     * @return the _com_qty
     */
    public BigDecimal getCom_qty() {
        return _com_qty;
    }
}
