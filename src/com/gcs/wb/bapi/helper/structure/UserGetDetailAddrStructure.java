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
public class UserGetDetailAddrStructure implements Serializable {

    /**Person's Title*/
    @Parameter(UserGetDetailConstants.TITLE_P)
    private String _title;
    /**Full name*/
    @Parameter(UserGetDetailConstants.FULLNAME)
    private String _fullName;
    /**Language Key*/
    @Parameter(UserGetDetailConstants.LANGU_P)
    private String _lang;
    /**Language Key in ISO*/
    @Parameter(UserGetDetailConstants.LANGUP_ISO)
    private String _langISO;

    public UserGetDetailAddrStructure() {
    }

    /**
     * @return the _title
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @return the _fullName
     */
    public String getFullName() {
        return _fullName;
    }

    /**
     * @return the _lang
     */
    public String getLang() {
        return _lang;
    }

    /**
     * @return the _langISO
     */
    public String getLangISO() {
        return _langISO;
    }
}
