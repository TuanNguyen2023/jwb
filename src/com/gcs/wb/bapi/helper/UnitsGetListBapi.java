/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.UnitsGetListStructure;
import com.gcs.wb.bapi.helper.constants.UnitsGetListConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;

/**
 *
 * @author vunguyent
 */
@Bapi(value = UnitsGetListConstants.BAPI_NAME)
public class UnitsGetListBapi implements Serializable {
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">

    @Table
    @Parameter(UnitsGetListConstants.TD_UNITS)
    private List<UnitsGetListStructure> _tdUnits;
    // </editor-fold>

    public UnitsGetListBapi() {
        _tdUnits = new ArrayList<UnitsGetListStructure>();
    }

    /**
     * @return the _tdUnits
     */
    public List<UnitsGetListStructure> getTdUnits() {
        return _tdUnits;
    }
}
