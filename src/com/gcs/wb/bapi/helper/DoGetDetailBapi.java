/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.Table;

import com.gcs.wb.bapi.helper.constants.DoGetDetailConstants;
import com.gcs.wb.bapi.helper.structure.DoGetDetailStructure;

/**
 *
 * @author Tran-Vu
 */
@Bapi(DoGetDetailConstants.BAPI_NAME)
public class DoGetDetailBapi implements Serializable {

    /**
	 * serial UID
	 */
	private static final long serialVersionUID = 7624350240374155505L;
	@Import
    @Parameter(DoGetDetailConstants.ID_DO)
    private String _id_do;
    @Export
    @Parameter(DoGetDetailConstants.ES_BZIRK)
    private String _es_bzirk;
    @Parameter(DoGetDetailConstants.ES_TEXT)
    private String _es_text;
    @Parameter(DoGetDetailConstants.ES_VSTEL)
    private String _es_vstel;
    @Parameter(DoGetDetailConstants.ES_VSTEL_TXT)
    private String _es_vstel_txt;
    
    @Table
    @Parameter(DoGetDetailConstants.TD_DOS)
    private List<DoGetDetailStructure> _td_dos;

    public DoGetDetailBapi() {
        _td_dos = new ArrayList<DoGetDetailStructure>();
    }

    /**
     * List DOs
     * @return the _td_dos
     */
    public List<DoGetDetailStructure> getTd_dos() {
        return _td_dos;
    }

    /**
     * @return the _id_do
     */
    public String getId_do() {
        return _id_do;
    }

    /**
     * @param id_do the _id_do to set
     */
    public void setId_do(String id_do) {
        this._id_do = id_do;
    }

	/**
	 * @return the _es_bzirk
	 */
	public String getEs_bzirk() {
		return _es_bzirk;
	}

	/**
	 * @param _es_bzirk the _es_bzirk to set
	 */
	public void setEs_bzirk(String _es_bzirk) {
		this._es_bzirk = _es_bzirk;
	}

	/**
	 * @return the _es_text
	 */
	public String getEs_text() {
		return _es_text;
	}

	/**
	 * @param _es_text the _es_text to set
	 */
	public void setEs_text(String _es_text) {
		this._es_text = _es_text;
	}
    

	/**
	 * @return the _es_text
	 */
	public String getEs_vstel() {
		return _es_vstel;
	}

	/**
	 * @param _es_text the _es_text to set
	 */
	public void setEs_vstel(String _es_vstel) {
		this._es_vstel = _es_vstel;
	}
    	/**
	 * @return the _es_text
	 */
	public String getEs_vstel_txt() {
		return _es_vstel_txt;
	}

	/**
	 * @param _es_text the _es_text to set
	 */
	public void setEs_vstel_txt(String _es_vstel_txt) {
		this._es_vstel_txt = _es_vstel_txt;
	}
    }