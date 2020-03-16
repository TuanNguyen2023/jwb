/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.constant;

import com.gcs.wb.model.WeighingMode;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author THANGPT
 */
public class Constants {

    public static class WeighingProcess {

        public static enum MODE {
            INPUT,
            OUTPUT
        }

        public static enum MODE_DETAIL {
            IN_PO_PURCHASE,
            IN_WAREHOUSE_TRANSFER,
            IN_OTHER,
            OUT_SELL_ROAD,
            OUT_PLANT_PLANT,
            OUT_SLOC_SLOC,
            OUT_PULL_STATION,
            OUT_SELL_WATERWAY,
            OUT_OTHER
        };

        public static List<WeighingMode> getOutputModeList() {
            List<WeighingMode> mode = new ArrayList<>();
            mode.add(new WeighingMode(MODE_DETAIL.OUT_SELL_ROAD, "Bán Hàng (Bộ)"));
            mode.add(new WeighingMode(MODE_DETAIL.OUT_PLANT_PLANT, "Plant - Plant"));
            mode.add(new WeighingMode(MODE_DETAIL.OUT_SLOC_SLOC, "Sloc - Sloc"));
            mode.add(new WeighingMode(MODE_DETAIL.OUT_PULL_STATION, "Nhập Bến Kéo"));
            mode.add(new WeighingMode(MODE_DETAIL.OUT_SELL_WATERWAY, "Bán Hàng (Thủy)"));
            mode.add(new WeighingMode(MODE_DETAIL.OUT_OTHER, "Cân khác"));

            return mode;
        }

        public static List<WeighingMode> getInputModeList() {
            List<WeighingMode> mode = new ArrayList<>();
            mode.add(new WeighingMode(MODE_DETAIL.IN_PO_PURCHASE, "PO Mua Hàng"));
            mode.add(new WeighingMode(MODE_DETAIL.IN_WAREHOUSE_TRANSFER, "Chuyển Kho"));
            mode.add(new WeighingMode(MODE_DETAIL.IN_OTHER, "Cân khác"));

            return mode;
        }
    }

    public static class Configuration {

        public static final String FILE_NAME = "application.properties";
        public static final int WEIGHT_LIMIT = 60;
        public static final boolean MODE_NORMAL = true;
        public static final String RPT_ID = "";
        public static final String WPLANT_MAP = "0";
        public static final BigDecimal TOLERANCE = new BigDecimal(9);
    }

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
        public static final String STATUS_INCOMPLETED = "INCOMPLETED ";
        public static final String STATUS_OFFLINE = "OFFLINE";
    }

    public static class LengthValidator {

        public static final int MAX_LENGTH_NAMEPRT = 255;
        public static final int MAX_LENGTH_ADDRESS = 255;
    }

    public static class DailyReport {

        public static final Object[] WT_COL_NAMES = new String[]{
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
            "SAP Posted",
            "DVVC",
            "Số P.O"};
        public static final Class[] WT_COL_TYPES = new Class[]{
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
            String.class,
            String.class};
    }

    public static final class ProcOrdView {

        private ProcOrdView() {
        }
        public static final String PROP_PROCORD = "procOrd";
    }

    public static final class TransportAgent {

        public static final Pattern LICENSE_PLATE_PATTERN = Pattern.compile("^(\\d{2}[A-Za-z]-\\d{4})|(\\d{2}[A-Za-z]-\\d{3}\\.\\d{2})|(\\d{2}[A-Za-z]-\\d{4,6})|(\\d{2}[A-Za-z]\\d{4,7})$");
        public static final Pattern LICENSE_PLATE_WATER_PATTERN = Pattern.compile("^([A-Za-z]{2}\\d{4,8})$");
    }

    public static final class ComboBox {

        public static final int UNSELECTED = -1;
        public static final int FIRST_INDEX = 0;
        public static final int SECOND_INDEX = 1;
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
        public static final Object[] WEIGHTTICKET_COLUMS = new String[]{
            "Số tài",
            "Tên tài xế",
            "CMND/BL",
            "B.S Xe",
            "B.S Rơmoóc",
            "Nhập/Xuất (I/O)",
            "Loại hàng đăng ký",
            "Trọng lượng đăng ký",
            "Số D.O",
            "Người tạo",
            "Số Phiếu trong tháng",
            "Ngày",
            "Giờ",
            "SAP Posted"};
        public static final Class[] WEIGHTTICKET_TYPES = new Class[]{
            Integer.class,
            String.class,
            String.class,
            String.class,
            String.class,
            Character.class,
            String.class,
            BigDecimal.class,
            String.class,
            String.class,
            Integer.class,
            Date.class,
            String.class,
            Boolean.class};

        public static final String INPUT_LOWCASE = "nhập";
        public static final String OUTPUT_LOWCASE = "xuất";

        public static final String INPUT = "Nhập";
        public static final String OUTPUT = "Xuất";

        public static final String DO_TYPES = "LF,LR,NL,ZTLF,ZTLR";
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

        public static final String PROCESS_ORDER_CF = "PROCESS_ORDER_CF";

        public static final String ITEM_DESCRIPTION = "Clinker gia công";
    }

    public static final class WeightTicketReport {

        public static final Object[] WT_COL_NAMES = new String[]{
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
        public static final Class[] WT_COL_TYPES = new Class[]{
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
        public static final String[] MODES_MODEL = {"Tất cả", "Nhập", "Xuất"};
        public static final String[] STATUS_MODEL = {"Tất cả", "Hoàn tất"};
    }

    public static final class LookupMaterial {

        public static final Object[] WEIGHTTICKET_COLUMS = new String[]{
            "Mã vật tư",
            "Loại vật tư"};
        public static final Class[] WEIGHTTICKET_TYPES = new Class[]{
            String.class,
            String.class};
    }

    public static final class Date {

        public static final String FORMAT = "dd/MM/yyyy";
    }
    
    public static final class SyncMasterData {

        public static final String CRON_EXPRESSION = "0 0 0 ? * * *"; // 00:00:00 every day
        public static final String TIME_SYNC = "000000"; // 00:00:00
    }
}
