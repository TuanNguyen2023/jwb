/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.text;

import com.gcs.wb.bapi.text.structure.TextLineStructure;
import com.gcs.wb.bapi.text.constants.TextCreateConstants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(TextCreateConstants.BAPI_NAME)
@ThrowExceptionOnError
public class TextCreateBapi implements Serializable {

    // <editor-fold defaultstate="expanded" desc="Import Parameters">
    @Import
    @Parameter(TextCreateConstants.FID)
    private String _id;
    @Import
    @Parameter(TextCreateConstants.FLANGUAGE)
    private String _language;
    @Import
    @Parameter(TextCreateConstants.FNAME)
    private String _name;
    @Import
    @Parameter(TextCreateConstants.FOBJECT)
    private String _object;
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Export Parameters">
    @Export
    @Parameter(TextCreateConstants.RETURN)
    private BapiRet2 _return;
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Table Parameters">
    @Table
    @Parameter(TextCreateConstants.FLINES)
    private List<TextLineStructure> _lines;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Class Constructors">

    public TextCreateBapi() {
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

    public void addLine(String format, String line) {
        _lines.add(new TextLineStructure(format, line));
    }
    // </editor-fold>
}
