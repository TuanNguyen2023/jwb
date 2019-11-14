/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.text;

import com.gcs.wb.bapi.text.structure.TextLineStructure;
import com.gcs.wb.bapi.text.structure.TextHeaderStructure;
import com.gcs.wb.bapi.text.constants.TextSaveConstants;
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
@Bapi(TextSaveConstants.BAPI_NAME)
@ThrowExceptionOnError
public class TextSaveBapi implements Serializable {

    // <editor-fold defaultstate="expanded" desc="Import Parameters">
    @Import
    @Parameter(value = TextSaveConstants.HEADER, type = ParameterType.STRUCTURE)
    private TextHeaderStructure _header;
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Export Parameters">
    @Export
    @Parameter(value = TextSaveConstants.HEADER, type = ParameterType.STRUCTURE)
    private TextHeaderStructure _newHeader;
    @Export
    @Parameter(value = TextSaveConstants.RETURN, type = ParameterType.STRUCTURE)
    private BapiRet2 _return;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    @Table
    @Parameter(TextSaveConstants.LINES)
    private List<TextLineStructure> _lines;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Class Constructors">

    public TextSaveBapi() {
        _lines = new ArrayList<TextLineStructure>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Field Getter/Setter">

    /**
     * @return the _header
     */
    public TextHeaderStructure getHeader() {
        return _header;
    }

    /**
     * @param header the _header to set
     */
    public void setHeader(TextHeaderStructure header) {
        this._header = header;
    }

    /**
     * @return the _newHeader
     */
    public TextHeaderStructure getNewHeader() {
        return _newHeader;
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

    /**
     * @param format
     * @param line 
     */
    public void addLine(String format, String line) {
        _lines.add(new TextLineStructure(format, line));
    }
    // </editor-fold>
}
