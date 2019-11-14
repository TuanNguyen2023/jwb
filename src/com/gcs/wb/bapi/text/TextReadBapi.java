/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.text;

import com.gcs.wb.bapi.text.structure.TextLineStructure;
import com.gcs.wb.bapi.text.constants.TextReadConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(TextReadConstants.BAPI_NAME)
@ThrowExceptionOnError
public class TextReadBapi implements Serializable {

    // <editor-fold defaultstate="expanded" desc="Import Parameters">
    @Import
    @Parameter(TextReadConstants.ID)
    private String _id;
    @Import
    @Parameter(TextReadConstants.LANGUAGE)
    private String _language;
    @Import
    @Parameter(TextReadConstants.NAME)
    private String _name;
    @Import
    @Parameter(TextReadConstants.OBJECT)
    private String _object;
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Export Parameters">
    @Export
    @Parameter(value = TextReadConstants.RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 _return;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    @Table
    @Parameter(TextReadConstants.LINES)
    private List<TextLineStructure> _lines;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Class Constructors">

    public TextReadBapi() {
        _lines = new ArrayList<TextLineStructure>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">

    /**
     * @return the _id
     */
    public String getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(String id) {
        this._id = id;
    }

    /**
     * @return the _language
     */
    public String getLanguage() {
        return _language;
    }

    /**
     * @param language the _language to set
     */
    public void setLanguage(String language) {
        this._language = language;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the _object
     */
    public String getObject() {
        return _object;
    }

    /**
     * @param object the _object to set
     */
    public void setObject(String object) {
        this._object = object;
    }

    /**
     * @return the _return
     */
    public BapiRet2 getReturn() {
        return _return;
    }

    /**
     * @return the _lines
     */
    public List<TextLineStructure> getLines() {
        return _lines;
    }
    // </editor-fold>
}
