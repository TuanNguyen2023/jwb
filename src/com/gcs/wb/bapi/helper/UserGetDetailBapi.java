/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper;

import com.gcs.wb.bapi.helper.structure.UserGetDetailAddrStructure;
import com.gcs.wb.bapi.helper.structure.UserGetDetailAGRStructure;
import com.gcs.wb.bapi.helper.constants.UserGetDetailConstants;
import java.io.Serializable;
import java.util.List;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author vunguyent
 */
@Bapi(UserGetDetailConstants.BAPI_NAME)
public class UserGetDetailBapi implements Serializable {

    /**User Name (Import Parameter)*/
    @Import
    @Parameter(UserGetDetailConstants.USERNAME)
    private String _userName = null;
    /**Address (Export Parameter)*/
    @Export
    @Parameter(value = UserGetDetailConstants.ADDRESS, type = ParameterType.STRUCTURE)
    private UserGetDetailAddrStructure _address;
    /**Activity Group (Table Parameter)*/
    @Table
    @Parameter(UserGetDetailConstants.ACTIVITYGROUPS)
    private List<UserGetDetailAGRStructure> _actgrps;
    /**Return (Table Parameter)*/
    @Table
    @Parameter(UserGetDetailConstants.RETURN)
    private List<BapiRet2> _return;

    public UserGetDetailBapi() {
    }

    /**
     * User Name (Import Parameter)
     * @param userName the _userName to set
     */
    public void setUserName(String userName) {
        this._userName = userName;
    }

    /**
     * Address (Export Parameter)
     * @return the _address
     */
    public UserGetDetailAddrStructure getAddress() {
        return _address;
    }

    /**
     * Activity Group (Table Parameter)
     * @return the _actgrps
     */
    public List<UserGetDetailAGRStructure> getActgrps() {
        return _actgrps;
    }

    /**
     * Return (Table Parameter)
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }
}
