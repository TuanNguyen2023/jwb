/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.bapi.helper.constants.BatchStocksGetListConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;

/**
 *
 * @author Tran-Vu
 */
@Bapi(BatchStocksGetListConstants.BAPI_NAME)
public class BatchStocksGetListBapi implements Serializable {

    @Import
    @Parameter(BatchStocksGetListConstants.ID_MANDT)
    private String _idMandt;
    @Import
    @Parameter(BatchStocksGetListConstants.ID_WERKS)
    private String _idWerks;
    @Import
    @Parameter(BatchStocksGetListConstants.ID_LGORT)
    private String _idLgort;
    @Import
    @Parameter(BatchStocksGetListConstants.ID_MATNR)
    private String _idMatnr;
    @Table
    @Parameter(value = BatchStocksGetListConstants.TD_BATCH_STOCKS, type = ParameterType.STRUCTURE)
    private List<BatchStocksStructure> _batchStocks;

    public BatchStocksGetListBapi() {
        _batchStocks = new ArrayList<BatchStocksStructure>();
    }

    /**
     * @param idMandt the _idMandt to set
     */
    public void setIdMandt(String idMandt) {
        this._idMandt = idMandt;
    }

    /**
     * @param idWerks the _idWerks to set
     */
    public void setIdWerks(String idWerks) {
        this._idWerks = idWerks;
    }

    /**
     * @param idLgort the _idLgort to set
     */
    public void setIdLgort(String idLgort) {
        this._idLgort = idLgort;
    }

    /**
     * @param idMatnr the _idMatnr to set
     */
    public void setIdMatnr(String idMatnr) {
        this._idMatnr = idMatnr;
    }

    /**
     * @return the _batchStocks
     */
    public List<BatchStocksStructure> getBatchStocks() {
        return _batchStocks;
    }
}
