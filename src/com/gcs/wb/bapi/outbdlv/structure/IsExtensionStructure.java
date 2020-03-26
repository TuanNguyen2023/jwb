/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv.structure;

import com.gcs.wb.bapi.outbdlv.constants.IsExtensionConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author thanghl
 */
@BapiStructure
public class IsExtensionStructure implements Serializable {

    @Parameter(IsExtensionConstants.ZSLING)
    private String _z_sling;
    @Parameter(IsExtensionConstants.ZPALLET)
    private String _z_pallet;

    public IsExtensionStructure() {
    }

    public String getZ_sling() {
        return _z_sling;
    }

    public void setZ_sling(String _z_sling) {
        this._z_sling = _z_sling;
    }

    public String getZ_pallet() {
        return _z_pallet;
    }

    public void setZ_pallet(String _z_pallet) {
        this._z_pallet = _z_pallet;
    }
}
