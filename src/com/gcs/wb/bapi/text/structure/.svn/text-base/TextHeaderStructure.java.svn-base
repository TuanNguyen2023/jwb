/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.text.structure;

import com.gcs.wb.bapi.text.constants.TextSaveConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class TextHeaderStructure implements Serializable {

    @Parameter(TextSaveConstants.TDOBJECT)
    private String _tdObject;
    @Parameter(TextSaveConstants.TDNAME)
    private String _tdName;
    @Parameter(TextSaveConstants.TDID)
    private String _tdId;
    @Parameter(TextSaveConstants.TDSPRAS)
    private String _tdSpras;

    public TextHeaderStructure(String obj, String name, String id, String spras) {
        this._tdObject = obj;
        this._tdName = name;
        this._tdId = id;
        this._tdSpras = spras;
    }

    /**
     * @return the _tdObject
     */
    public String getTdObject() {
        return _tdObject;
    }

    /**
     * @return the _tdName
     */
    public String getTdName() {
        return _tdName;
    }

    /**
     * @return the _tdId
     */
    public String getTdId() {
        return _tdId;
    }

    /**
     * @return the _tdSpras
     */
    public String getTdSpras() {
        return _tdSpras;
    }
}
