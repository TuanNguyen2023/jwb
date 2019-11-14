/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.goodsmvt;

import com.gcs.wb.bapi.goodsmvt.constants.GoodsMvtCancelConstants;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(GoodsMvtCancelConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class GoodsMvtCancelBapi implements Serializable {

    @Import()
    @Parameter(value = GoodsMvtCancelConstants.MATERIALDOCUMENT)
    private String _materialDocument;
    @Import
    @Parameter(value = GoodsMvtCancelConstants.MATDOCUMENTYEAR)
    private Integer _matdocumentYear;
    // </editor-fold>
    @Table
    @Parameter(GoodsMvtCancelConstants.RETURN)
    private List<BapiRet2> _return;

    public GoodsMvtCancelBapi() {
    }

    public GoodsMvtCancelBapi(String materialDocument, int year) {
        this._materialDocument = materialDocument;
        this._matdocumentYear = year;
    }

    /**
     * @return the _materialDocument
     */
    public String getMaterialDocument() {
        return _materialDocument;
    }

    /**
     * @param materialDocument the _materialDocument to set
     */
    public void setMaterialDocument(String materialDocument) {
        this._materialDocument = materialDocument;
    }

    /**
     * @return the _matdocumentYear
     */
    public Integer getMatdocumentYear() {
        return _matdocumentYear;
    }

    /**
     * @param matdocumentYear the _matdocumentYear to set
     */
    public void setMatdocumentYear(Integer matdocumentYear) {
        this._matdocumentYear = matdocumentYear;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }

}
