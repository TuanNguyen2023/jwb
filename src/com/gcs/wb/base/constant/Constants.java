/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.constant;

import java.util.regex.Pattern;

/**
 *
 * @author THANGPT
 */
public class Constants {

    public static final class ProcOrdView {
        private ProcOrdView() {
        }
        public static final String PROP_PROCORD = "procOrd";
    }
    public static final class TransportAgent {

        private TransportAgent() {
        }
        public static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^(\\d{2}[A-Z]-\\d{4})|(\\d{2}[A-Z]-\\d{3}.\\d{2})$");
    }

    public static final class VRView {

        private VRView() {
        }
        public static final Pattern patLicPlate = Pattern.compile("\\d{2}[A-Z]-\\d{4}");
        public static final Pattern patLicPlatenew = Pattern.compile("\\d{2}[A-Z]-\\S+"); //+20110309#01
        public static final Pattern patAAbbr = Pattern.compile("[0-9A-Z]{1,10}");
    }

    public static final class VRView1 {

        private VRView1() {
        }
        public static final Pattern patLicPlate = Pattern.compile("\\d{2}[A-Z]-\\d{4}");
        public static final Pattern patLicPlatenew = Pattern.compile("\\d{2}[A-Z]-\\S+"); //+20110309#01
        public static final Pattern patAAbbr = Pattern.compile("[0-9A-Z]{1,10}");
    }
    
    public static final class WTRegView{
        private WTRegView() {
        }
        public static final Pattern patLicPlate = Pattern.compile("\\d{2}[A-Z]-\\d{4}");
        public static final Pattern patLicPlatenew = Pattern.compile("\\d{2}[A-Z]-\\S+");
        public static final String PROP_FORMVALID = "formValid";
        public static final String MODE_REG = "MODE_REG";
        public static final String MODE_RPT = "MODE_RPT";
        public static final String PROP_FORMEDITABLE = "formEditable";
        public static final String PROP_RBTENABLED = "rbtEnabled";
    }
    
    public static final class WeightTicketView{
        public static final String PROP_STAGE1 = "stage1";
        public static final String PROP_STAGE2 = "stage2";
        public static final String PROP_SAVENEEDED = "saveNeeded";
        public static final String PROP_BRIDGE1 = "bridge1";
        public static final String PROP_BRIDGE2 = "bridge2";
        public static final String PROP_VALIDPONUM = "validPONum";
        public static final String PROP_DISSOLVED = "dissolved";
        public static final String PROP_REPRINTABLE = "reprintable";
        public static final String PROP_ENTEREDVALIDPONUM = "enteredValidPONum";
        public static final String PROP_ENTEREDVALIDWTNUM = "enteredValidWTNum";
        public static final String PROP_WITHOUTDO = "withoutDO";
        public static final String PROP_FORMENABLE = "formEnable";
        public static final String PROP_SUBCONTRACT = "subContract";
        public static final String PROP_MATERIALAVAILABLE = "materialAvailable";
        public static final String PROP_MATAVAILSTOCKS = "matAvailStocks";
        public static final String PROP_MVT311 = "mvt311";

    }
}
