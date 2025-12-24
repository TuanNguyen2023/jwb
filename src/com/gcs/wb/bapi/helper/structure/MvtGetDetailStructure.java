/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MvtGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class MvtGetDetailStructure implements Serializable {

    @Parameter(MvtGetDetailConstants.MANDT)
    private String _mandt;
    @Parameter(MvtGetDetailConstants.BWART)
    private String _bwart;
    @Parameter(MvtGetDetailConstants.SPRAS)
    private String _spras;
    @Parameter(MvtGetDetailConstants.BTEXT)
    private String _btext;

    public MvtGetDetailStructure() {
    }

    /**
     * @return the _mandt
     */
    public String getMandt() {
        return _mandt;
    }

    /**
     * @return the _spras
     */
    public String getSpras() {
        return _spras;
    }

    /**
     * @return the _bwart
     */
    public String getBwart() {
        return _bwart;
    }

    /**
     * @return the _btext
     */
    public String getBtext() {
        return _btext;
    }
}
