/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt.structure;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCreateConstants;
import java.io.Serializable;
import java.util.Date;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class GoodsMvtHeaderStructure implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Structure Field">
    @Parameter(GoodsMvtCreateConstants.PSTNG_DATE)
    private Date _pstngDate;
    @Parameter(GoodsMvtCreateConstants.DOC_DATE)
    private Date _docDate;
    @Parameter(GoodsMvtCreateConstants.REF_DOC_NO)
    private String _refDocNo;
    /**Truck ID*/
    @Parameter(GoodsMvtCreateConstants.BILL_OF_LADING)
    private String _billOfLading;
    /**Identification Card*/
    @Parameter(GoodsMvtCreateConstants.GR_GI_SLIP_NO)
    private String _grGiSlipNo;
    @Parameter(GoodsMvtCreateConstants.HEADER_TXT)
    private String _headerText;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Class Constructor">

    public GoodsMvtHeaderStructure() {
    }

    public GoodsMvtHeaderStructure(Date pstngDate, Date docDate, String refDocNo) {
        this._pstngDate = pstngDate;
        this._docDate = docDate;
        this._refDocNo = refDocNo;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">

    /**
     * @return the _pstngDate
     */
    public Date getPstngDate() {
        return _pstngDate;
    }

    /**
     * @param pstngDate the _pstngDate to set
     */
    public void setPstngDate(Date pstngDate) {
        this._pstngDate = pstngDate;
    }

    /**
     * @return the _docDate
     */
    public Date getDocDate() {
        return _docDate;
    }

    /**
     * @param docDate the _docDate to set
     */
    public void setDocDate(Date docDate) {
        this._docDate = docDate;
    }

    /**
     * @return the _refDocNo
     */
    public String getRefDocNo() {
        return _refDocNo;
    }

    /**
     * @param refDocNo the _refDocNo to set
     */
    public void setRefDocNo(String refDocNo) {
        this._refDocNo = refDocNo;
    }

    /**
     * Truck ID
     * @return the _billOfLading
     */
    public String getBillOfLading() {
        return _billOfLading;
    }

    /**
     * Truck ID
     * @param billOfLading the _billOfLading to set
     */
    public void setBillOfLading(String billOfLading) {
        this._billOfLading = billOfLading;
    }

    /**
     * Identification Card
     * @return the _grGiSlipNo
     */
    public String getGrGiSlipNo() {
        return _grGiSlipNo;
    }

    /**
     * Identification Card
     * @param grGiSlipNo the _grGiSlipNo to set
     */
    public void setGrGiSlipNo(String grGiSlipNo) {
        this._grGiSlipNo = grGiSlipNo;
    }

    /**
     * @return the _headerText
     */
    public String getHeaderText() {
        return _headerText;
    }

    /**
     * @param headerText the _headerText to set
     */
    public void setHeaderText(String headerText) {
        this._headerText = headerText;
    }
    // </editor-fold>
}
