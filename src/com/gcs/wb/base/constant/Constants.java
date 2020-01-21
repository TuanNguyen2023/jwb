/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.constant;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Pattern;

/**
 *
 * @author THANGPT
 */
public class Constants {

    public static class Label {

        public static final String LABEL_ALL = "Tất cả";
        public static final String LABEL_OTHER = "Khác";
    }

    public static class Vehicle {

        public static final String STATUS_ACTIVED = "ACTIVED";
        public static final String STATUS_INACTIVED = "INACTIVED";
    }

    public static class WeightTicket {

        public static final String STATUS_POSTED = "POSTED";
        public static final String STATUS_DISSOLVED = "DISSOLVED";
    }

    public static class LengthValidator {

        public static final int MAX_LENGTH_NAMEPRT = 255;
        public static final int MAX_LENGTH_ADDRESS = 255;
    }

    public static class DailyReport {

        public static final Object[] wtColNames = new String[]{
            "STT",
            "Số đăng tài",
            "Tên tài xế",
            "CMND/Bằng lái",
            "Số Xe",
            "Số Rơmoóc",
            "Người tạo",
            "Ngày giờ tạo",
            "Nhập/Xuất(I/O)",
            "Loại hàng",
            "Ngày giờ vào",
            "T.L vào",
            "Ngày giờ ra",
            "T.L ra",
            "T.L Hàng",
            "Số DO",
            "Số chứng từ SAP",
            "Hủy",
            "SAP Posted",
            "DVVC",
            "Số P.O"};
        public static final Class[] wtColTypes = new Class[]{
            Integer.class,
            Long.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Date.class,
            Character.class,
            String.class,
            Date.class,
            BigDecimal.class,
            Date.class,
            BigDecimal.class,
            BigDecimal.class,
            String.class,
            String.class,
            Boolean.class,
            Boolean.class,
            String.class,
            String.class};
    }

    public static class WTList {

        public static final Object[] wtCols = new String[]{
            "STT",
            "S.Đ.Tài",
            "Tên tài xế",
            "CMND/BL",
            "Số Xe",
            "Số Rơmoóc",
            "Người tạo",
            "Ngày giờ tạo",
            "Nhập/Xuất(I/O)",
            "Loại hàng",
            "Ngày giờ vào",
            "T.L vào",
            "Ngày giờ ra",
            "T.L ra",
            "T.L Hàng",
            "Số D.O",
            "Số chứng từ SAP",
            "Hủy",
            "SAP Posted",
            "Đơn vị vân chuyển",
            "Số P.O"};
        public static final Class[] wtTypes = new Class[]{
            Integer.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Date.class,
            Character.class,
            String.class,
            Date.class,
            BigDecimal.class,
            Date.class,
            BigDecimal.class,
            BigDecimal.class,
            String.class,
            String.class,
            Boolean.class,
            Boolean.class,
            String.class,
            String.class};
    }

    public static final class ProcOrdView {

        private ProcOrdView() {
        }
        public static final String PROP_PROCORD = "procOrd";
    }

    public static final class TransportAgent {

        public static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^(\\d{2}[A-Z]-\\d{4})|(\\d{2}[A-Z]-\\d{3}.\\d{2})$");
    }

    public static final class WTRegView {

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

    public static final class WeightTicketView {

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

    public static final class WeightTicketReport {

        public static final Object[] wtColNames = new String[]{
            "STT",
            "S.Đ.Tài",
            "Tên tài xế",
            "CMND/BL",
            "Số Xe",
            "Số Rơmoóc",
            "Người tạo",
            "Ngày giờ tạo",
            "Nhập/Xuất(I/O)",
            "Loại hàng",
            "Ngày giờ vào",
            "T.L vào",
            "Ngày giờ ra",
            "T.L ra",
            "T.L Hàng",
            "Số D.O",
            "Số chứng từ SAP",
            "SAP Posted",
            "DVVC",
            "Số P.O"};
        public static final Class[] wtColTypes = new Class[]{
            Integer.class,
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Date.class,
            Character.class,
            String.class,
            Date.class,
            BigDecimal.class,
            Date.class,
            BigDecimal.class,
            BigDecimal.class,
            String.class,
            String.class,
            Boolean.class,
            String.class,
            String.class};
        public static final String[] modesModel = {"Tất cả", "Nhập", "Xuất"};
        public static final String[] statusModel = {"Tất cả", "Hoàn tất"};
    }
}
