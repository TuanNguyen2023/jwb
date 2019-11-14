/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.structure;

import com.gcs.wb.bapi.helper.constants.UserGetDetailConstants;
import java.io.Serializable;
import org.hibersap.annotations.BapiStructure;
import org.hibersap.annotations.Parameter;

/**
 *
 * @author vunguyent
 */
@BapiStructure
public class UserGetDetailAGRStructure implements Serializable {

    /**Role Name*/
    @Parameter(UserGetDetailConstants.AGR_NAME)
    private String _agr_name = null;

    public UserGetDetailAGRStructure() {
    }

    /**
     * Role Name
     * @return the _agr_name
     */
    public String getAgr_name() {
        return _agr_name;
    }
}
