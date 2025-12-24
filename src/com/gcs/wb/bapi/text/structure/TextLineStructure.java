/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.text.structure;

import com.gcs.wb.bapi.text.constants.TextLineConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Convert;
import org.hibersap.annotations.Parameter;
import org.hibersap.conversion.CharConverter;

/**
 *
 * @author Tran-Vu
 */
@BapiStructure
public class TextLineStructure implements Serializable {

    @Parameter(TextLineConstants.TDFORMAT)
    @Convert(converter = CharConverter.class)
    private String _tdFormat;
    @Parameter(TextLineConstants.TDLINE)
    private String _tdLine;

    public TextLineStructure() {
    }

    public TextLineStructure(String tdFormat, String tdLine) {
        this._tdFormat = tdFormat;
        this._tdLine = tdLine;
    }

    /**
     * @return the _tdFormat
     */
    public String getTdFormat() {
        return _tdFormat;
    }

    /**
     * @return the _tdLine
     */
    public String getTdLine() {
        return _tdLine;
    }
}
