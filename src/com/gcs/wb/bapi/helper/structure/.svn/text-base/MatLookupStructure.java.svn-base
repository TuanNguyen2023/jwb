/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.MatLookupConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class MatLookupStructure implements Serializable {

    /**Material Number*/
    @Parameter(value = MatLookupConstants.MATERIAL, type = ParameterType.SIMPLE)
    private String _material = null;
    /**Material Description (Short Text)*/
    @Parameter(value = MatLookupConstants.MATL_DESC, type = ParameterType.SIMPLE)
    private String _desc = null;
    /**Batch management requirement indicator*/
    @Parameter(value = MatLookupConstants.XCHPF, type = ParameterType.SIMPLE)
    private String _xchpf = null;

    public MatLookupStructure() {
    }

    /**
     * Material Number
     * @return the _material
     */
    public String getMaterial() {
        return _material;
    }

    /**
     * Material Description (Short Text)
     * @return the _desc
     */
    public String getDesc() {
        return _desc;
    }

    /**
     * Batch management requirement indicator
     * @return the _xchpf
     */
    public String getXchpf() {
        return _xchpf;
    }
}
