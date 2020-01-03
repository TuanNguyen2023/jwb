SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

DROP SCHEMA IF EXISTS `jWeighBridge` ;
CREATE SCHEMA IF NOT EXISTS `jWeighBridge` DEFAULT CHARACTER SET utf8 ;
USE `jWeighBridge` ;

-- -----------------------------------------------------
-- Table `jWeighBridge`.`SAPSetting`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`SAPSetting` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`SAPSetting` (
  `MANDT` CHAR(3) NOT NULL ,
  `WPlant` CHAR(4) NOT NULL ,
  `Name1` VARCHAR(30) NULL DEFAULT NULL ,
  `Name2` VARCHAR(30) NULL DEFAULT NULL ,
  `NAME_RPT` VARCHAR(255) NULL DEFAULT NULL ,
  `ADDRESS` VARCHAR(255) NULL DEFAULT NULL ,
  `PHONE` VARCHAR(20) NULL DEFAULT NULL ,
  `FAX` VARCHAR(20) NULL DEFAULT NULL ,
  `MATNR_PCB40` CHAR(18) NULL DEFAULT NULL ,
  `MATNR_XMXA` CHAR(18) NULL DEFAULT NULL ,
  `MATNR_CLINKER` CHAR(18) NULL DEFAULT NULL ,
  `CHECK_TALP` TINYINT(1) UNSIGNED NULL DEFAULT NULL COMMENT 'Flag of checking Transport Agent License Plate' ,
  `BACT1_VAL` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'Base activity 1 value' ,
  `BACT1_UNIT` VARCHAR(3) NULL DEFAULT 'STD' COMMENT 'Base activity 1 unit' ,
  `BACT2_VAL` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'Base activity 2 value' ,
  `BACT2_UNIT` VARCHAR(3) NULL DEFAULT 'STD' COMMENT 'Base activity 2 unit' ,
  `BACT3_VAL` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'Base activity 3 value' ,
  `BACT3_UNIT` VARCHAR(3) NULL DEFAULT 'KWH' COMMENT 'Base activity 3 unit' ,
  `BACT4_VAL` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'Base activity 4 value' ,
  `BACT4_UNIT` VARCHAR(3) NULL DEFAULT 'M3' COMMENT 'Base activity 4 unit' ,
  `ROLE_WM` VARCHAR(30) NULL DEFAULT NULL COMMENT 'SAP Role name for App. role Watchman' ,
  `ROLE_SS` VARCHAR(30) NULL DEFAULT NULL COMMENT 'SAP Role name for App. role Station Staff' ,
  `ROLE_AD` VARCHAR(30) NULL DEFAULT NULL COMMENT 'SAP Role name for App. role Administration' ,
  `ROLE_SP` VARCHAR(30) NULL DEFAULT NULL COMMENT 'SAP Role name for App. role Supervisor' ,
  `WB1_TOL` DECIMAL(3,3) NULL DEFAULT NULL COMMENT 'WB1 Tolerance' ,
  `WB2_TOL` DECIMAL(3,3) NULL DEFAULT NULL COMMENT 'WB2 Tolerance' ,
  PRIMARY KEY (`MANDT`, `WPlant`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Setting table';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`BatchStocks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`BatchStocks` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`BatchStocks` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `WERKS` CHAR(4) NOT NULL COMMENT 'Plant' ,
  `LGORT` CHAR(4) NOT NULL COMMENT 'Sloc Number' ,
  `MATNR` VARCHAR(18) NOT NULL COMMENT 'Material Number' ,
  `CHARG` VARCHAR(10) NOT NULL COMMENT 'Batch' ,
  `LVORM` CHAR(1) NULL DEFAULT NULL COMMENT 'Deletion flag' ,
  PRIMARY KEY (`MANDT`, `WERKS`, `LGORT`, `MATNR`, `CHARG`) ,
  INDEX `fk_Batch_setting` (`MANDT` ASC, `WERKS` ASC) ,
  CONSTRAINT `fk_Batch_setting`
    FOREIGN KEY (`MANDT` , `WERKS` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` , `WPlant` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Customer` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Customer` (
  `MANDT` CHAR(3) NOT NULL ,
  `KUNNR` VARCHAR(10) NOT NULL ,
  `NAME1` VARCHAR(35) NULL DEFAULT NULL ,
  `NAME2` VARCHAR(35) NULL DEFAULT NULL ,
  PRIMARY KEY (`MANDT`, `KUNNR`) ,
  INDEX `fk_customer_sap` (`MANDT` ASC) ,
  CONSTRAINT `fk_customer_sap`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: KNA1';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`OutbDel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`OutbDel` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`OutbDel` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `DELIV_NUMB` VARCHAR(10) NOT NULL COMMENT 'DELIVERY' ,
  `DELIV_ITEM` VARCHAR(6) NOT NULL COMMENT 'DELIVERY ITEM' ,
  `DELIV_ITEM_FREE` VARCHAR(6) NULL DEFAULT NULL ,
  `MATNR` VARCHAR(18) NULL DEFAULT NULL COMMENT 'MATERIAL NUMBER' ,
  `ARKTX` VARCHAR(40) NULL DEFAULT NULL COMMENT 'SHORT TEXT FOR SALES ORDER ITEM' ,
  `ERDAT` DATE NULL DEFAULT NULL COMMENT 'DATE ON WHICH RECORD WAS CREATED' ,
  `LFART` VARCHAR(4) NULL DEFAULT NULL COMMENT 'DELIVERY TYPE' ,
  `LDDAT` DATE NULL DEFAULT NULL COMMENT 'LOADING DATE' ,
  `WADAT` DATE NULL DEFAULT NULL COMMENT 'GOODS ISSUE DATE' ,
  `KODAT` DATE NULL DEFAULT NULL COMMENT 'PICKING DATE' ,
  `SHIP_POINT` CHAR(4) NULL DEFAULT NULL COMMENT 'SHIPPING POINT/RECEIVING POINT' ,
  `LIFNR` VARCHAR(10) NULL DEFAULT NULL COMMENT 'ACCOUNT NUMBER OF VENDOR OR CREDITOR' ,
  `KUNNR` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Customer Number 1(Ship-to Party)' ,
  `KUNAG` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Sold-to party' ,
  `TRATY` CHAR(4) NULL DEFAULT NULL COMMENT 'MEANS-OF-TRANSPORT TYPE' ,
  `TRAID` VARCHAR(20) NULL DEFAULT NULL COMMENT 'MEANS OF TRANSPORT ID' ,
  `BLDAT` DATE NULL DEFAULT NULL COMMENT 'DOCUMENT DATE IN DOCUMENT' ,
  `WERKS` CHAR(4) NULL DEFAULT NULL COMMENT 'SUPPLYING (ISSUING) PLANT IN STOCK TRANSPORT ORDER' ,
  `RECV_PLANT` CHAR(4) NULL DEFAULT NULL COMMENT 'RECEIVING PLANT' ,
  `LGORT` CHAR(4) NULL DEFAULT NULL COMMENT 'STORAGE LOCATION' ,
  `CHARG` VARCHAR(10) NULL DEFAULT NULL COMMENT 'BATCH NUMBER' ,
  `LICHN` VARCHAR(15) NULL DEFAULT NULL COMMENT 'VENDOR BATCH NUMBER' ,
  `LFIMG` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'ACTUAL QUANTITY DELIVERED (IN SALES UNITS)' ,
  `FREE_QTY` DECIMAL(13,3) NULL DEFAULT NULL COMMENT 'Free Item Qty.' ,
  `MEINS` VARCHAR(3) NULL DEFAULT NULL COMMENT 'BASE UNIT OF MEASURE' ,
  `VRKME` VARCHAR(3) NULL DEFAULT NULL COMMENT 'SALES UNIT' ,
  `UNTTO` DECIMAL(3,1) NULL DEFAULT NULL COMMENT 'UNDERDELIVERY TOLERANCE LIMIT' ,
  `UEBTO` DECIMAL(3,1) NULL DEFAULT NULL COMMENT 'OVERDELIVERY TOLERANCE LIMIT' ,
  `UEBTK` CHAR(1) NULL DEFAULT NULL COMMENT 'INDICATOR: UNLIMITED OVERDELIVERY ALLOWED' ,
  `VGBEL` VARCHAR(10) NULL DEFAULT NULL COMMENT 'DOCUMENT NUMBER OF THE REFERENCE DOCUMENT' ,
  `VGPOS` VARCHAR(6) NULL DEFAULT NULL COMMENT 'ITEM NUMBER OF THE REFERENCE ITEM' ,
  `BWTAR` VARCHAR(10) NULL DEFAULT NULL COMMENT 'VALUATION TYPE' ,
  `BWART` CHAR(3) NULL DEFAULT NULL COMMENT 'MOVEMENT TYPE (INVENTORY MANAGEMENT)' ,
  `KOSTK` CHAR(1) NULL DEFAULT NULL COMMENT 'SAP Table: VBUK.\nPicking state with values could be:\n+ A : Not yet processed\n+ B : Partially processed\n+ C : Completely processed' ,
  `KOQUK` CHAR(1) NULL DEFAULT NULL COMMENT 'SAP Table: VBUK.\nConfirmation state with values could be:\n+ A : Not yet processed\n+ B : Partially processed\n+ C : Completely processed' ,
  `WBSTK` CHAR(1) NULL DEFAULT NULL COMMENT 'SAP Table: VBUK.\nGood Issue state with values could be:\n+ A : Not yet processed\n+ B : Partially processed\n+ C : Completely processed' ,
  PRIMARY KEY (`MANDT`, `DELIV_NUMB`) ,
  INDEX `fk_outbdel_setting` (`MANDT` ASC) ,
  CONSTRAINT `fk_outbdel_setting`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Outbound Delivery';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`DocFlow`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`DocFlow` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`DocFlow` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `VBELV` VARCHAR(10) NOT NULL COMMENT 'Preceding Doc.' ,
  `VBELN` VARCHAR(10) NOT NULL COMMENT 'Follow-on doc.' ,
  `VBTYP_V` CHAR(1) NULL DEFAULT NULL COMMENT 'Prec.doc.categ.' ,
  `VBTYP_N` CHAR(1) NULL DEFAULT NULL COMMENT 'Subs.doc.categ.' ,
  `MJAHR` YEAR NULL DEFAULT NULL COMMENT 'Mat. Doc. Year' ,
  `RFMNG` DECIMAL(15,3) NULL DEFAULT NULL COMMENT 'Quantity' ,
  `MEINS` VARCHAR(3) NULL DEFAULT NULL COMMENT 'Unit' ,
  `PLMIN` CHAR(1) NULL DEFAULT NULL COMMENT 'Pos./ negative for calculation or not at all' ,
  `ERDAT` DATE NULL DEFAULT NULL COMMENT 'Created on' ,
  `ERZET` TIME NULL DEFAULT NULL COMMENT 'Time' ,
  `MATNR` VARCHAR(18) NULL DEFAULT NULL COMMENT 'Material Number' ,
  `BWART` CHAR(3) NULL DEFAULT NULL COMMENT 'Movement type' ,
  PRIMARY KEY (`MANDT`, `VBELV`, `VBELN`) ,
  INDEX `fk_docflow_outbdel` (`MANDT` ASC, `VBELV` ASC) ,
  CONSTRAINT `fk_docflow_outbdel`
    FOREIGN KEY (`MANDT` , `VBELV` )
    REFERENCES `jWeighBridge`.`OutbDel` (`MANDT` , `DELIV_NUMB` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: VBFA';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Material`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Material` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Material` (
  `MANDT` CHAR(3) NOT NULL ,
  `MATNR` VARCHAR(18) NOT NULL COMMENT 'Material Number' ,
  `MAKTX` VARCHAR(40) NULL DEFAULT NULL COMMENT 'Material Description (Short Text)' ,
  `MAKTG` VARCHAR(40) NULL DEFAULT NULL COMMENT 'Material description in upper case for matchcodes' ,
  `XCHPF` CHAR(1) NULL COMMENT 'Batch management requirement indicator' ,
  PRIMARY KEY (`MATNR`, `MANDT`) ,
  INDEX `fk_mat_setting` (`MANDT` ASC) ,
  CONSTRAINT `fk_mat_setting`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: MAKT';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Movement`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Movement` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Movement` (
  `MANDT` CHAR(3) NOT NULL ,
  `BWART` CHAR(3) NOT NULL ,
  `SPRAS` CHAR(2) NOT NULL ,
  `BTEXT` VARCHAR(20) NULL DEFAULT NULL ,
  PRIMARY KEY (`MANDT`, `BWART`) ,
  UNIQUE INDEX `Unique` (`MANDT` ASC, `BWART` ASC, `SPRAS` ASC) ,
  INDEX `fk_mvt_setting` (`MANDT` ASC) ,
  CONSTRAINT `fk_mvt_setting`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Movement Type';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`PurOrder`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`PurOrder` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`PurOrder` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `PO_NUMBER` CHAR(10) NOT NULL COMMENT 'Purchasing Document Number' ,
  `DOC_TYPE` VARCHAR(4) NULL DEFAULT NULL COMMENT 'Purchasing Document Type' ,
  `DELETE_IND` CHAR(1) NULL DEFAULT NULL COMMENT 'Deletion Indicator in Purchasing Document' ,
  `STATUS` CHAR(1) NULL DEFAULT NULL COMMENT 'Status of Purchasing Document' ,
  `CREAT_DATE` DATE NULL DEFAULT NULL COMMENT 'Date on Which Record Was Created' ,
  `VENDOR` VARCHAR(10) NULL DEFAULT NULL COMMENT 'ELIFN : Vendor Account Number' ,
  `SUPPL_VEND` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Supplying Vendor' ,
  `CUSTOMER` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Customer Number 1' ,
  `SUPPL_PLNT` VARCHAR(4) NULL DEFAULT NULL COMMENT 'Supplying (Issuing) Plant in Stock Transport Order' ,
  `PO_REL_IND` CHAR(1) NULL DEFAULT NULL COMMENT 'Release Indicator: Purchasing Document' ,
  `PO_ITEM` VARCHAR(5) NOT NULL COMMENT 'Item Number of Purchasing Document' ,
  `PO_ITEM_FREE` VARCHAR(5) NULL DEFAULT NULL COMMENT 'Item Number of Purchasing Document (Free Item)' ,
  `I_DELETE_IND` CHAR(1) NULL DEFAULT NULL COMMENT 'Deletion Indicator in Purchasing Document(Item Level)' ,
  `IF_DELETE_IND` CHAR(1) NULL DEFAULT NULL COMMENT 'Deletion Indicator in Purchasing Document(Item Free Level)' ,
  `SHORT_TEXT` VARCHAR(40) NULL DEFAULT NULL ,
  `MATERIAL` VARCHAR(18) NULL DEFAULT NULL ,
  `PLANT` CHAR(4) NULL DEFAULT NULL ,
  `STGE_LOC` CHAR(4) NULL DEFAULT NULL COMMENT 'Storage Location' ,
  `VEND_MAT` VARCHAR(35) NULL DEFAULT NULL COMMENT 'Material Number Used by Vendor' ,
  `QUANTITY` DECIMAL(13,3) NULL DEFAULT NULL ,
  `QUANTITY_FREE` DECIMAL(13,3) NULL DEFAULT NULL ,
  `PO_UNIT` VARCHAR(3) NULL DEFAULT NULL ,
  `PO_UNIT_ISO` VARCHAR(3) NULL DEFAULT NULL ,
  `QUAL_INSP` CHAR(1) NULL DEFAULT NULL COMMENT 'Stock Type' ,
  `OVER_DLV_TOL` DECIMAL(3,1) NULL DEFAULT NULL COMMENT 'Overdelivery Tolerance Limit' ,
  `UNLIMITED_DLV` CHAR(1) NULL DEFAULT NULL COMMENT 'Indicator: Unlimited Overdelivery Allowed' ,
  `UNDER_DLV_TOL` DECIMAL(3,1) NULL DEFAULT NULL COMMENT 'Underdelivery Tolerance Limit' ,
  `VAL_TYPE` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Valuation Type' ,
  `NO_MORE_GR` CHAR(1) NULL DEFAULT NULL COMMENT '\"Delivery Completed\" Indicator' ,
  `FINAL_INV` CHAR(1) NULL DEFAULT NULL COMMENT 'Final Invoice Indicator' ,
  `ITEM_CAT` CHAR(1) NULL DEFAULT NULL COMMENT 'Item Category in Purchasing Document' ,
  `ITEM_FREE_CAT` CHAR(1) NULL DEFAULT NULL COMMENT 'Item Free Category in Purchasing Document' ,
  `GR_IND` CHAR(1) NULL DEFAULT NULL COMMENT 'Goods Receipt Indicator' ,
  `GR_NON_VAL` CHAR(1) NULL DEFAULT NULL COMMENT 'Goods Receipt, Non-Valuated' ,
  `DELIV_COMPL` CHAR(1) NULL DEFAULT NULL COMMENT '\"Outward Delivery Completed\" Indicator' ,
  `PART_DELIV` CHAR(1) NULL DEFAULT NULL COMMENT 'Partial Delivery at Item Level (Stock Transfer)' ,
  `REL_STATUS` VARCHAR(8) NULL DEFAULT NULL COMMENT 'Release status' ,
  PRIMARY KEY (`PO_NUMBER`, `MANDT`) ,
  INDEX `fk_purc_setting` (`MANDT` ASC) ,
  CONSTRAINT `fk_purc_setting`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Purchase Order';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Reason`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Reason` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Reason` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `BWART` CHAR(3) NOT NULL COMMENT 'Movement Type (Code)' ,
  `GRUND` CHAR(4) NOT NULL COMMENT 'Reason for Movement (Code)' ,
  `GRTXT` VARCHAR(20) NULL DEFAULT NULL COMMENT 'Text: Reason for Goods Movement' ,
  PRIMARY KEY (`BWART`, `GRUND`, `MANDT`) ,
  INDEX `fk_reason_mvt` (`MANDT` ASC, `BWART` ASC) ,
  CONSTRAINT `fk_reason_mvt`
    FOREIGN KEY (`MANDT` , `BWART` )
    REFERENCES `jWeighBridge`.`Movement` (`MANDT` , `BWART` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: T157E - Reason for movement type';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`SLoc`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`SLoc` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`SLoc` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `WPlant` CHAR(4) NOT NULL COMMENT 'Working Plant' ,
  `LGORT` CHAR(4) NOT NULL COMMENT 'ID: Storage Location' ,
  `LGOBE` VARCHAR(16) NULL DEFAULT NULL COMMENT 'LGOBE: Description of Storage Location' ,
  PRIMARY KEY (`LGORT`, `MANDT`, `WPlant`) ,
  INDEX `fk_sloc_setting` (`MANDT` ASC, `WPlant` ASC) ,
  CONSTRAINT `fk_sloc_setting`
    FOREIGN KEY (`MANDT` , `WPlant` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` , `WPlant` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: T001L (Storage Locations)';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`TransportAgent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`TransportAgent` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`TransportAgent` (
  `ABBR` VARCHAR(10) NOT NULL ,
  `Name` VARCHAR(255) NULL DEFAULT NULL ,
  PRIMARY KEY (`ABBR`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Unit`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Unit` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Unit` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `MSEHI` VARCHAR(3) NOT NULL COMMENT 'Unit of Measurement' ,
  `SPRAS` CHAR(2) NOT NULL COMMENT 'Language key' ,
  `ANDEC` DECIMAL(5,0) NULL DEFAULT NULL COMMENT 'No. of decimal places to which rounding should be performed' ,
  `DIMID` VARCHAR(6) NULL DEFAULT NULL COMMENT 'Dimension key' ,
  `ZAEHL` DECIMAL(10,0) NULL DEFAULT NULL COMMENT 'Numerator for conversion to SI unit' ,
  `NENNR` DECIMAL(10,0) NULL DEFAULT NULL COMMENT 'Denominator for conversion into SI unit' ,
  `EXP10` DECIMAL(5,0) NULL DEFAULT NULL COMMENT 'Base ten exponent for conversion to SI unit' ,
  `ADDKO` DECIMAL(9,6) NULL DEFAULT NULL COMMENT 'Additive constant for conversion to SI unit' ,
  `DECAN` DECIMAL(5,0) NULL DEFAULT NULL COMMENT 'Number of decimal places for number display' ,
  `ISOCODE` VARCHAR(3) NULL DEFAULT NULL COMMENT 'ISO code for unit of measurement' ,
  `PRIMARY_UNIT` VARCHAR(1) NULL DEFAULT NULL COMMENT 'Objects to be processed' ,
  `MSEH3` VARCHAR(3) NULL DEFAULT NULL COMMENT 'External Unit of Measurement in Commercial Format (3-Char.)' ,
  `MSEHT` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Unit of Measurement Text (Maximum 10 Characters)' ,
  PRIMARY KEY (`MSEHI`, `MANDT`) ,
  UNIQUE INDEX `unique` (`MANDT` ASC, `MSEHI` ASC, `SPRAS` ASC) ,
  INDEX `fk_unit_setting` (`MANDT` ASC) ,
  CONSTRAINT `fk_unit_setting`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'SAP Table: T006 - Unit';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`User`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`User` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`User` (
  `MANDT` CHAR(3) NOT NULL ,
  `WPlant` CHAR(4) NOT NULL ,
  `ID` VARCHAR(12) NOT NULL COMMENT 'User\'s Login ID' ,
  `PWD` VARCHAR(45) NOT NULL COMMENT 'User\'s password' ,
  `TITLE` VARCHAR(30) NULL DEFAULT NULL ,
  `FULL_NAME` VARCHAR(80) NULL DEFAULT NULL ,
  `ROLES` MEDIUMTEXT NULL DEFAULT NULL ,
  `LANGU_P` CHAR(1) NULL DEFAULT NULL COMMENT 'Language Key' ,
  `LANGUP_ISO` CHAR(2) NULL DEFAULT NULL COMMENT 'Language key according to ISO 639' ,
  PRIMARY KEY (`ID`, `MANDT`, `WPlant`) ,
  INDEX `fk_user_setting` (`MANDT` ASC, `WPlant` ASC) ,
  CONSTRAINT `fk_user_setting`
    FOREIGN KEY (`MANDT` , `WPlant` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` , `WPlant` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Login use';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Vehicle`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Vehicle` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Vehicle` (
  `SO_XE` VARCHAR(10) NOT NULL ,
  `TA_ABBR` VARCHAR(10) NULL DEFAULT NULL ,
  PRIMARY KEY (`SO_XE`) ,
  INDEX `fk_v_ta` (`TA_ABBR` ASC) ,
  CONSTRAINT `fk_v_ta`
    FOREIGN KEY (`TA_ABBR` )
    REFERENCES `jWeighBridge`.`TransportAgent` (`ABBR` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jWeighBridge`.`Vendor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`Vendor` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`Vendor` (
  `MANDT` CHAR(3) NOT NULL ,
  `LIFNR` VARCHAR(10) NOT NULL ,
  `NAME1` VARCHAR(35) NULL DEFAULT NULL ,
  `NAME2` VARCHAR(35) NULL DEFAULT NULL ,
  PRIMARY KEY (`MANDT`, `LIFNR`) ,
  INDEX `fk_vendor_sap` (`MANDT` ASC) ,
  CONSTRAINT `fk_vendor_sap`
    FOREIGN KEY (`MANDT` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `jWeighBridge`.`WeightTicket`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`WeightTicket` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`WeightTicket` (
  `MANDT` CHAR(3) NOT NULL COMMENT 'Client' ,
  `WPlant` CHAR(4) NOT NULL COMMENT 'Working Plant' ,
  `ID` CHAR(10) NOT NULL COMMENT 'ID with:\n- Format: yyMMddHHmm' ,
  `SEQ_BY_DAY` INT(3) UNSIGNED ZEROFILL NOT NULL COMMENT 'Sequence Number with:\n-Format: ddd (fill zero)' ,
  `SEQ_BY_MONTH` INT(4) UNSIGNED ZEROFILL NOT NULL ,
  `TEN_TAI_XE` VARCHAR(70) NOT NULL COMMENT 'Driver\'s Name' ,
  `CMND_BL` VARCHAR(10) NOT NULL COMMENT 'Personal ID or Driver License number' ,
  `SO_XE` VARCHAR(10) NOT NULL COMMENT 'License Plate' ,
  `SO_ROMOOC` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Trailer ID' ,
  `REG_CATEGORY` CHAR(1) NOT NULL COMMENT 'I for GR\nO for GI' ,
  `REG_ITEM_TEXT` VARCHAR(70) NOT NULL COMMENT 'Registered Item' ,
  `REG_ITEM_QTY` DECIMAL(13,3) NOT NULL COMMENT 'Registered Qty.' ,
  `CREATE_DATE` DATE NOT NULL COMMENT 'Create date' ,
  `CREATE_TIME` VARCHAR(45) NOT NULL COMMENT 'Create time' ,
  `CREATOR` VARCHAR(12) NULL DEFAULT NULL ,
  `LGORT` CHAR(4) NULL DEFAULT NULL ,
  `CHARG` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Batch' ,
  `DELIV_NUMB` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Outb.Del. Number' ,
  `EBELN` CHAR(10) NULL DEFAULT NULL COMMENT 'PO Number' ,
  `ITEM` VARCHAR(6) NULL DEFAULT NULL ,
  `MATNR_REF` VARCHAR(18) NULL DEFAULT NULL ,
  `NO_MORE_GR` CHAR(1) NULL DEFAULT NULL COMMENT 'Flag: Complete GR' ,
  `MOVE_TYPE` CHAR(3) NULL DEFAULT '101' COMMENT 'Movement Type' ,
  `MOVE_REAS` CHAR(4) NULL DEFAULT NULL COMMENT 'Move Reason' ,
  `TEXT` VARCHAR(50) NULL DEFAULT NULL COMMENT 'Item text' ,
  `MVT_IND` CHAR(1) NULL DEFAULT 'B' ,
  `LICHN` VARCHAR(15) NULL DEFAULT NULL COMMENT 'Vendor Batch' ,
  `SO_NIEM_XA` VARCHAR(255) NULL DEFAULT NULL ,
  `F_SCALE` DECIMAL(13,3) NULL DEFAULT NULL ,
  `F_TIME` TIMESTAMP NULL DEFAULT NULL ,
  `F_CREATOR` VARCHAR(12) NULL DEFAULT NULL ,
  `S_SCALE` DECIMAL(13,3) NULL DEFAULT NULL ,
  `S_TIME` TIMESTAMP NULL DEFAULT NULL ,
  `S_CREATOR` VARCHAR(12) NULL DEFAULT NULL ,
  `G_QTY` DECIMAL(13,3) NULL DEFAULT NULL ,
  `UNIT` VARCHAR(3) NULL DEFAULT NULL ,
  `OFFLINE_MODE` TINYINT(1) UNSIGNED NULL DEFAULT NULL COMMENT '0 = false\n1 = true' ,
  `TRANSFERED_POSTING` TINYINT(1) UNSIGNED NULL DEFAULT NULL ,
  `DISSOLVED` TINYINT(1) UNSIGNED NULL DEFAULT NULL COMMENT '0 = false\n1 = true' ,
  `POSTED` TINYINT(1) UNSIGNED NULL DEFAULT NULL ,
  `MAT_DOC` VARCHAR(21) NULL DEFAULT NULL COMMENT 'MBLNR : Number of Material Document' ,
  `DOC_YEAR` INT(4) NULL DEFAULT NULL COMMENT 'MJAHR : Material Document Year' ,
  `RECV_MATNR` VARCHAR(18) NULL DEFAULT NULL ,
  `RECV_PLANT` CHAR(4) NULL DEFAULT NULL ,
  `RECV_LGORT` CHAR(4) NULL DEFAULT NULL ,
  `RECV_CHARG` VARCHAR(10) NULL DEFAULT NULL ,
  `RECV_PO` CHAR(10) NULL DEFAULT NULL ,
  `PP_PROCORD` VARCHAR(12) NULL DEFAULT NULL COMMENT 'Process Order Number for PP module' ,
  `PP_PROCORDCNF` VARCHAR(10) NULL DEFAULT NULL COMMENT 'Process Order Confirmation Number for PP module' ,
  `PP_PROCORDCNFCNT` VARCHAR(8) NULL DEFAULT NULL COMMENT 'Process Order Confirmation Number Counter for PP module' ,
  PRIMARY KEY (`MANDT`, `WPlant`, `ID`, `SEQ_BY_DAY`) ,
  INDEX `fk_wt_setting` (`MANDT` ASC, `WPlant` ASC) ,
  CONSTRAINT `fk_wt_setting`
    FOREIGN KEY (`MANDT` , `WPlant` )
    REFERENCES `jWeighBridge`.`SAPSetting` (`MANDT` , `WPlant` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = 'Weight Ticket';


-- -----------------------------------------------------
-- Table `jWeighBridge`.`variant`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `jWeighBridge`.`variant` ;

CREATE  TABLE IF NOT EXISTS `jWeighBridge`.`variant` (
  `MANDT` CHAR(3) NOT NULL ,
  `WPlant` CHAR(4) NOT NULL ,
  `PARAM` CHAR(255) NOT NULL COMMENT 'Parameters' ,
  `VALUE` TEXT NULL COMMENT 'Value' ,
  PRIMARY KEY (`MANDT`, `WPlant`, `PARAM`) )
ENGINE = InnoDB
COMMENT = 'Application configuration';



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
