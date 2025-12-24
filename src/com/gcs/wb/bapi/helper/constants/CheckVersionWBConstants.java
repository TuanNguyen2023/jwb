/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.helper.constants;

import org.hibersap.bapi.BapiConstants;

/**
 *
 * @author vunguyent
 */
public interface CheckVersionWBConstants extends BapiConstants {

    String BAPI_NAME = "ZBAPI_CHECK_WB_VERSIONID";
    /*Import Parameter*/
    String versionid = "IV_VERSIONID";
    /*Export Parameter*/
    String result = "ES_RESULT";

}
