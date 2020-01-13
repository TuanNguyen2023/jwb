/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.model;

import com.gcs.wb.base.util.Base64_Utils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 *
 * @author vunguyent
 */
public class AppConfig {

    private static final String FILE_NAME = "application.properties";
    private PropertiesConfiguration config = null;
    private boolean fullyConfigured = false;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public AppConfig() {
        config = new PropertiesConfiguration();
        config.setAutoSave(false);
        config.setFileName(FILE_NAME);
        try {
            config.load();

            getEndeTimes();
            getModeNormal();
            getWbId();
            getRptId();
            getDbHost();
            getDbName();
            getDbUsr();
            getDbPwd();

            getsHost();
            getGWHost();
            getsNumber();
            getsClient();
            getsRoute();
            getB1Port();
            getB1Speed();
            getB1DBits();
            getB1SBits();
            getB1PC();
            getB1Mettler();

            getB2Port();
            getB2Speed();
            getB2DBits();
            getB2SBits();
            getB2PC();
            getB2Mettler();

            getwPlant();
            getUsrName();
            getPsswrd();

        } catch (ConfigurationException ex) {
            try {
                config.save();



            } catch (ConfigurationException ex1) {
                Logger.getLogger(this.getClass()).error(null, ex1);
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Keys">
    /**
     * Configuration Key: ENDE_TIMES
     */
    public static final String ENDE_TIMES = "ENDE_TIMES";
    /**
     * Configuration Key: MODE_NORMAL
     */
    public static final String MODE_NORMAL = "MODE_NORMAL";
    /**
     * Configuration Key: DB_HOST
     */
    public static final String WB_ID = "WB_ID";
    public static final String SHIP_POINT = "SHIP_POINT";
    public static final String RPT_ID = "RPT_ID";
    /**
     * Configuration Key: DB_HOST
     */
    public static final String DB_HOST = "DB_HOST";
    /**
     * Configuration Key: DB_NAME
     */
    public static final String DB_NAME = "DB_NAME";
    /**
     * Configuration Key: DB_USR
     */
    public static final String DB_USR = "DB_USR";
    /**
     * Configuration Key: DB_PWD
     */
    public static final String DB_PWD = "DB_PWD";
    /**
     * Configuration Key: SAP_HOST
     */
    public static final String SAP_HOST = "SAP_HOST";
    /**
     * Configuration Key: SAP_GWHOST
     */
    public static final String SAP_GWHOST = "SAP_GWHOST";
    /**
     * Configuration Key: SAP_SN (System Number)
     */
    public static final String SAP_SN = "SAP_SN";
    /**
     * Configuration Key: SAP_RS (Route String)
     */
    public static final String SAP_RS = "SAP_RS";
    /**
     * Configuration Key: SAP_CLIENT
     */
    public static final String SAP_CLIENT = "SAP_CLIENT";
    /**
     * Configuration Key: WB1_PORT (Weigh Bridge 1: Port)
     */
    public static final String WB1_PORT = "WB1_PORT";
    /**
     * Configuration Key: WB1_BR (Weigh Bridge 1: Baud Rate | Speed)
     */
    public static final String WB1_BR = "WB1_BR";
    /**
     * Configuration Key: WB1_BR (Weigh Bridge 1: Data Bits)
     */
    public static final String WB1_DB = "WB1_DB";
    /**
     * Configuration Key: WB1_BR (Weigh Bridge 1: Stop Bits)
     */
    public static final String WB1_SB = "WB1_SB";
    /**
     * Configuration Key: WB1_BR (Weigh Bridge 1: Parity Control)
     */
    public static final String WB1_PC = "WB1_PC";
    /**
     * Configuration Key: WB1_METTLER (Weigh Bridge 1: Is Mettler scale)
     */
    public static final String WB1_METTLER = "WB1_METTLER";
    /**
     * Configuration Key: WB2_PORT (Weigh Bridge 2: Port)
     */
    public static final String WB2_PORT = "WB2_PORT";
    /**
     * Configuration Key: WB2_BR (Weigh Bridge 2: Baud Rate | Speed)
     */
    public static final String WB2_BR = "WB2_BR";
    /**
     * Configuration Key: WB2_BR (Weigh Bridge 2: Data Bits)
     */
    public static final String WB2_DB = "WB2_DB";
    /**
     * Configuration Key: WB2_BR (Weigh Bridge 2: Stop Bits)
     */
    public static final String WB2_SB = "WB2_SB";
    /**
     * Configuration Key: WB2_BR (Weigh Bridge 2: Parity Control)
     */
    public static final String WB2_PC = "WB2_PC";
    /**
     * Configuration Key: WB2_METTLER (Weigh Bridge 2: Is Mettler scale)
     */
    public static final String WB2_METTLER = "WB2_METTLER";
    /**
     * Configuration Key: WK_PLANT (Working plant)
     */
    public static final String WK_PLANT = "WK_PLANT";
    /**
     * Configuration Key: AD_USR (Administrator's User)
     */
    public static final String AD_USR = "AD_USR";
    /**
     * Configuration Key: AD_PWD (Administrator's Password)
     */
    public static final String AD_PWD = "AD_PWD";
    public static final String USER = "USER";
    public static final String PASS = "PASS";
    /**
     * Config param key for limit weight when output
     */
    public static final String W_LIMIT = "W_LIMIT";
    /**
     * Configuration Key: CHECK_VERSION_WB
     */
    public static final String CHECK_VERSION_WB = "CHECK_VERSION_WB";
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Configuration Attributes">
    /**
     * number of times (encode - decode)
     */
    private int endeTimes = 1;
    /**
     * normal or right
     */
    private boolean modeNormal = true;
    /**
     * DB Host String
     */
    private String wbId = null;
    private String shId = null;
    private String rptId = null;
    /**
     * DB Host String
     */
    private String dbHost = null;
    /**
     * DB Name
     */
    private String dbName = null;
    /**
     * DB login id
     */
    private String dbUsr = null;
    /**
     * DB login password
     */
    private String dbPwd = null;
    /**
     * SAP Host String
     */
    private String sHost = null;
    /**
     * SAP Gateway Host String
     */
    private String gwHost = null;
    /**
     * SAP Route string
     */
    private String sRoute = null;
    /**
     * SAP System Number
     */
    private String sNumber = null;
    /**
     * SAP default Client
     */
    private String sClient = null;
    /**
     * Weigh Bridge 1: Port
     */
    private String b1Port = null;
    /**
     * Weigh Bridge 1: Baud rate (Speed)
     */
    private Integer b1Speed = null;
    /**
     * Weigh Bridge 1: Data bits
     */
    private Short b1DBits = null;
    /**
     * Weigh Bridge 1: Stop bits
     */
    private Float b1SBits = null;
    /**
     * Weigh Bridge 1: Parity Control
     */
    private Short b1PC = null;
    /**
     * Weigh Bridge 1: Mettler scale?
     */
    private Boolean b1Mettler = null;
    /**
     * Weigh Bridge 2: Port
     */
    private String b2Port = null;
    /**
     * Weigh Bridge 2: Baud rate (Speed)
     */
    private Integer b2Speed = null;
    /**
     * Weigh Bridge 2: Data bits
     */
    private Short b2DBits = null;
    /**
     * Weigh Bridge 2: Stop bits
     */
    private Float b2SBits = null;
    /**
     * Weigh Bridge 2: Parity Control
     */
    private Short b2PC = null;
    /**
     * Weigh Bridge 2: Mettler scale?
     */
    private Boolean b2Mettler = null;
    /**
     * Current SAP Working plant
     */
    private String wPlant = null;
    /**
     * Administrator user
     */
    private String adminUsr = null;
    /**
     * Administrator password
     */
    private String adminPwd = null;
    private String usrName = null;
    private String psswrd = null;
    /**
     * Limited weight
     */
    private int limitWeight = 0;
    /**
     * Check Version WB
     */
    private boolean checkVersionWB = false;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Attribute's Getter Setter">

    /**
     * @return the fullyConfigured
     */
    public boolean isFullyConfigured() {
        boolean b1Configured = !(b1Port == null || b1Speed == null || b1DBits == null || b1SBits == null || b1PC == null || b1Mettler == null);
        boolean b2Configured = !(b2Port == null || b2Speed == null || b2DBits == null || b2SBits == null || b2PC == null || b1Mettler == null);
        fullyConfigured = !(dbHost == null || dbName == null || dbUsr == null || dbPwd == null
                || sHost == null || sNumber == null || sClient == null
                || wPlant == null) && (b1Configured || b2Configured);
        return fullyConfigured;
    }

    public int getEndeTimes() {
//        String str_endeTimes = Base64_U(config.getString(ENDE_TIMES, "1")));
        String str_endeTimes = Base64_Utils.decodeDefinedTimes(
                config.getString(ENDE_TIMES, "VmtaYVJrOVdRbEpRVkRBOQ=="), Base64_Utils.getENDETIMES_DEFAULT());
        try {
            endeTimes = Integer.parseInt(str_endeTimes);
        } catch (Exception e) {
            endeTimes = 1;
        }
        Base64_Utils.setNumberOfTimes(endeTimes);
        return endeTimes;
    }

    public void setEndeTimes(int endeTimes) {
        this.endeTimes = endeTimes;
    }

    /**
     * application mode (normal or have right)
     * @return the mode 
     */
    public boolean getModeNormal() {
        String str_modeNormal = Base64_Utils.decodeNTimes(config.getString(MODE_NORMAL, "true"));
        try {
            modeNormal = Boolean.parseBoolean(str_modeNormal);
        } catch (Exception e) {
            modeNormal = true;
        }
        return modeNormal;
    }

    /**
     * Mode normal or have right
     * @param mode to set
     */
    public void setModeNormal(boolean modeNormal) {
        this.modeNormal = modeNormal;
    }

    /**
     * DB Host String
     * @return the dbHost
     */
    public String getWbId() {
        wbId = Base64_Utils.decodeNTimes(config.getString(WB_ID, ""));
        return wbId;
    }

    /**
     * DB Host String
     * @param dbHost the dbHost to set
     */
    public void setWbId(String wbId) {
        config.setProperty(WB_ID, Base64_Utils.encodeNTimes(wbId));
        this.wbId = wbId;
    }

    /**
     * DB Host String
     * @return the dbHost
     */
    public String getShId() {
        shId = Base64_Utils.decodeNTimes(config.getString(SHIP_POINT, ""));
        return shId;
    }

    /**
     * DB Host String
     * @param dbHost the dbHost to set
     */
    public void setShId(String shId) {
        config.setProperty(SHIP_POINT, Base64_Utils.encodeNTimes(shId));
        this.shId = shId;
    }

    /**
     * DB Host String
     * @return the dbHost
     */
    public String getDbHost() {
        dbHost = Base64_Utils.decodeNTimes(config.getString(DB_HOST, ""));
//        dbHost = "172.16.5.101:3306";
        return dbHost;
    }

    /**
     * DB Host String
     * @param dbHost the dbHost to set
     */
    public void setDbHost(String dbHost) {
        config.setProperty(DB_HOST, Base64_Utils.encodeNTimes(dbHost));
        this.dbHost = dbHost;
    }

    /**
     * DB Name
     * @return the dbName
     */
    public String getDbName() {
        dbName = Base64_Utils.decodeNTimes(config.getString(DB_NAME, ""));
        return dbName;
    }

    /**
     * DB Name
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        config.setProperty(DB_NAME, Base64_Utils.encodeNTimes(dbName));
        this.dbName = dbName;
    }

    /**
     * DB login id
     * @return the dbUsr
     */
    public String getDbUsr() {
        dbUsr = Base64_Utils.decodeNTimes(config.getString(DB_USR, ""));
        return dbUsr;
    }

    /**
     * DB login id
     * @param dbUsr the dbUsr to set
     */
    public void setDbUsr(String dbUsr) {
        config.setProperty(DB_USR, Base64_Utils.encodeNTimes(dbUsr));
        this.dbUsr = dbUsr;
    }

    /**
     * DB login password
     * @return the dbPwd
     */
    public String getDbPwd() {
        dbPwd = Base64_Utils.decodeNTimes(config.getString(DB_PWD, ""));
        return dbPwd;
    }

    /**
     * DB login password
     * @param dbPwd the dbPwd to set
     */
    public void setDbPwd(String dbPwd) {
        config.setProperty(DB_PWD, Base64_Utils.encodeNTimes(dbPwd));
        this.dbPwd = dbPwd;
    }

    /**
     * SAP Host String
     * @return the sHost
     */
    public String getsHost() {
        sHost = Base64_Utils.decodeNTimes(config.getString(SAP_HOST, ""));
        return sHost;
    }

    /**
     * SAP Host String
     * @param sHost the sHost to set
     */
    public void setsHost(String sHost) {
        config.setProperty(SAP_HOST, Base64_Utils.encodeNTimes(sHost));
        this.sHost = sHost;
    }

    /**
     * SAP Gateway Host String
     * @return the gwHost
     */
    public String getGWHost() {
        gwHost = Base64_Utils.decodeNTimes(config.getString(SAP_GWHOST, ""));
//        gwHost = "172.16.5.21";
        return gwHost;
    }

    /**
     * SAP Gateway Host String
     * @param gwHost the sap_gwHost to set
     */
    public void setGWHost(String gwHost) {
        config.setProperty(SAP_GWHOST, gwHost);
        this.gwHost = gwHost;
    }

    /**
     * SAP System Number
     * @return the sNumber
     */
    public String getsNumber() {
        sNumber = Base64_Utils.decodeNTimes(config.getString(SAP_SN, ""));
        return sNumber;
    }

    /**
     * SAP System Number
     * @param sNumber the sNumber to set
     */
    public void setsNumber(String sNumber) {
        config.setProperty(SAP_SN, Base64_Utils.encodeNTimes(sNumber));
        this.sNumber = sNumber;
    }

    /**
     * SAP Route string
     * @return the sRoute
     */
    public String getsRoute() {
        sRoute = Base64_Utils.decodeNTimes(config.getString(SAP_RS, ""));
        return sRoute;
    }

    /**
     * SAP Route string
     * @param sRoute the sRoute to set
     */
    public void setsRoute(String sRoute) {
        config.setProperty(SAP_RS, Base64_Utils.encodeNTimes(sRoute));
        this.sRoute = sRoute;
    }

    /**
     * SAP default Client
     * @return the sClient
     */
    public String getsClient() {
        sClient = Base64_Utils.decodeNTimes(config.getString(SAP_CLIENT, ""));
        return sClient;
    }

    /**
     * SAP default Client
     * @param sClient the sClient to set
     */
    public void setsClient(String sClient) {
        config.setProperty(SAP_CLIENT, Base64_Utils.encodeNTimes(sClient));
        this.sClient = sClient;
    }

    /**
     * Weigh Bridge 1: Port
     * @return the b1Port
     */
    public String getB1Port() {
        b1Port = Base64_Utils.decodeNTimes(config.getString(WB1_PORT, ""));
        return b1Port;
    }

    /**
     * Weigh Bridge 1: Port
     * @param b1Port the b1Port to set
     */
    public void setB1Port(String b1Port) {
        config.setProperty(WB1_PORT, Base64_Utils.encodeNTimes(b1Port));
        this.b1Port = b1Port;
    }

    /**
     * Weigh Bridge 1: Baud rate (Speed)
     * @return the b1Speed
     */
    public Integer getB1Speed() {
        if (b1Port == null) {
            b1Speed = null;
        } else {
            String b1Speed_str = Base64_Utils.decodeNTimes(config.getString(WB1_BR, ""));
            try {
                b1Speed = Integer.parseInt(b1Speed_str);
            } catch (Exception e) {
                b1Speed = null;
            }
        }
        return b1Speed;
    }

    /**
     * Weigh Bridge 1: Baud rate (Speed)
     * @param b1Speed the b1Speed to set
     */
    public void setB1Speed(Integer b1Speed) {
        if (b1Port == null) {
            b1Speed = null;
        }
        config.setProperty(WB1_BR, Base64_Utils.encodeNTimes(b1Speed != null
                ? b1Speed.toString() : ""));
        this.b1Speed = b1Speed;
    }

    /**
     * Weigh Bridge 1: Data bits
     * @return the b1DBits
     */
    public Short getB1DBits() {
        if (b1Port == null) {
            b1DBits = (short) 5;
        } else {
            String b1DBits_str = Base64_Utils.decodeNTimes(config.getString(WB1_DB, ""));
            try {
                b1DBits = Short.parseShort(b1DBits_str);
            } catch (Exception e) {
                b1DBits = (short) 5;
            }
        }
        return b1DBits;
    }

    /**
     * Weigh Bridge 1: Data bits
     * @param b1DBits the b1DBits to set
     */
    public void setB1DBits(Short b1DBits) {
        if (b1Port == null) {
            b1DBits = (short) 5;
        }

        config.setProperty(WB1_DB, Base64_Utils.encodeNTimes(b1DBits.toString()));
        this.b1DBits = b1DBits;
    }

    /**
     * Weigh Bridge 1: Stop bits
     * @return the b1SBits
     */
    public Float getB1SBits() {
        if (b1Port == null) {
            b1SBits = 1f;
        } else {
            String b1SBits_str = Base64_Utils.decodeNTimes(config.getString(WB1_SB, ""));
            try {
                b1SBits = Float.parseFloat(b1SBits_str);
            } catch (Exception e) {
                b1SBits = 1f;
            }
        }
        return b1SBits;
    }

    /**
     * Weigh Bridge 1: Stop bits
     * @param b1SBits the b1SBits to set
     */
    public void setB1SBits(Float b1SBits) {
        if (b1Port == null) {
            b1SBits = 1f;
        }

        config.setProperty(WB1_SB, Base64_Utils.encodeNTimes(b1SBits.toString()));
        this.b1SBits = b1SBits;
    }

    /**
     * Weigh Bridge 1: Parity Control
     * @return the b1PC
     */
    public Short getB1PC() {
        if (b1Port == null) {
            b1PC = (short) 0;
        } else {
            String b1PC_str = Base64_Utils.decodeNTimes(config.getString(WB1_PC, ""));
            try {
                b1PC = Short.parseShort(b1PC_str);
            } catch (Exception e) {
                b1PC = (short) 0;
            }
        }
        return b1PC;
    }

    /**
     * Weigh Bridge 1: Parity Control
     * @param b1PC the b1PC to set
     */
    public void setB1PC(Short b1PC) {
        if (b1Port == null) {
            b1PC = (short) 0;
        }
        config.setProperty(WB1_PC, Base64_Utils.encodeNTimes(b1PC.toString()));
        this.b1PC = b1PC;
    }

    /**
     * @return the b1Mettler
     */
    public Boolean getB1Mettler() {
        if (b1Port == null) {
            b1Mettler = null;
        } else {
            String b1Mettler_str = Base64_Utils.decodeNTimes(config.getString(WB1_METTLER, ""));
            try {
                b1Mettler = Boolean.parseBoolean(b1Mettler_str);
            } catch (Exception e) {
                b1Mettler = null;
            }
        }
        return b1Mettler;
        //  return true; 
    }

    /**
     * @param b1Mettler the b1Mettler to set
     */
    public void setB1Mettler(Boolean b1Mettler) {
        if (b1Port == null) {
            b1Mettler = null;
        }
        String b1Mettler_str = Base64_Utils.decodeNTimes(config.getString(WB1_METTLER, ""));

        config.setProperty(WB1_METTLER, Base64_Utils.encodeNTimes(b1Mettler != null ? b1Mettler.toString() : "false"));
        // config.setProperty(WB1_METTLER,b1Mettler);
        this.b1Mettler = b1Mettler;
        //  this.b1Mettler = true ;
    }

    /**
     * Weigh Bridge 2: Port
     * @return the b2Port
     */
    public String getB2Port() {
        b2Port = Base64_Utils.decodeNTimes(config.getString(WB2_PORT, ""));
        return b2Port;
    }

    /**
     * Weigh Bridge 2: Port
     * @param b2Port the b2Port to set
     */
    public void setB2Port(String b2Port) {
        config.setProperty(WB2_PORT, Base64_Utils.encodeNTimes(b2Port));
        this.b2Port = b2Port;
    }

    /**
     * Weigh Bridge 2: Baud rate (Speed)
     * @return the b2Speed
     */
    public Integer getB2Speed() {
        if (b2Port == null) {
            b2Speed = null;
        } else {
            String b2Speed_str = Base64_Utils.decodeNTimes(config.getString(WB2_BR, ""));
            try {
                b2Speed = Integer.parseInt(b2Speed_str);
            } catch (Exception e) {
                b2Speed = null;
            }
        }
        return b2Speed;
    }

    /**
     * Weigh Bridge 2: Baud rate (Speed)
     * @param b2Speed the b2Speed to set
     */
    public void setB2Speed(Integer b2Speed) {
        if (b2Port == null) {
            b2Speed = null;
        }
        config.setProperty(WB2_BR, Base64_Utils.encodeNTimes(b2Speed != null
                ? b2Speed.toString() : ""));
        this.b2Speed = b2Speed;
    }

    /**
     * Weigh Bridge 2: Data bits
     * @return the b2DBits
     */
    public Short getB2DBits() {
        if (b2Port == null) {
            b2DBits = (short) 5;
        } else {
            String b2DBits_str = Base64_Utils.decodeNTimes(config.getString(WB2_DB, ""));
            try {
                b2DBits = Short.parseShort(b2DBits_str);
            } catch (Exception e) {
                b2DBits = (short) 5;
            }
        }
        return b2DBits;
    }

    /**
     * Weigh Bridge 2: Data bits
     * @param b2DBits the b2DBits to set
     */
    public void setB2DBits(Short b2DBits) {
        if (b2Port == null) {
            b2DBits = (short) 5;
        }
        config.setProperty(WB2_DB, Base64_Utils.encodeNTimes(b2DBits.toString()));
        this.b2DBits = b2DBits;
    }

    /**
     * Weigh Bridge 2: Stop bits
     * @return the b2SBits
     */
    public Float getB2SBits() {
        if (b2Port == null) {
            b2SBits = 1f;
        } else {
            String b2SBits_str = Base64_Utils.decodeNTimes(config.getString(WB2_SB, ""));
            try {
                b2SBits = Float.parseFloat(b2SBits_str);
            } catch (Exception e) {
                b2SBits = 1f;
            }
        }
        return b2SBits;
    }

    /**
     * Weigh Bridge 2: Stop bits
     * @param b2SBits the b2SBits to set
     */
    public void setB2SBits(Float b2SBits) {
        if (b2Port == null) {
            b2SBits = 1f;
        }
        config.setProperty(WB2_SB, Base64_Utils.encodeNTimes(b2SBits.toString()));
        this.b2SBits = b2SBits;
    }

    /**
     * Weigh Bridge 2: Parity Control
     * @return the b2PC
     */
    public Short getB2PC() {
        if (b2Port == null) {
            b2PC = (short) 0;
        } else {
            String b2PC_str = Base64_Utils.decodeNTimes(config.getString(WB2_PC, ""));
            try {
                b2PC = Short.parseShort(b2PC_str);
            } catch (Exception e) {
                b2PC = (short) 0;
            }
        }
        return b2PC;
    }

    /**
     * Weigh Bridge 2: Parity Control
     * @param b2PC the b2PC to set
     */
    public void setB2PC(Short b2PC) {
        if (b2Port == null) {
            b2PC = (short) 0;
        }
        config.setProperty(WB2_PC, Base64_Utils.encodeNTimes(b2PC.toString()));
        this.b2PC = b2PC;
    }

    /**
     * @return the b2Mettler
     */
    public Boolean getB2Mettler() {
        if (b1Port == null) {
            b2Mettler = null;
        } else {
            String b2Mettler_str = Base64_Utils.decodeNTimes(config.getString(WB2_METTLER, ""));
            try {
                b2Mettler = Boolean.parseBoolean(b2Mettler_str);
            } catch (Exception e) {
                b2Mettler = null;
            }
        }
        return b2Mettler;
    }

    /**
     * @param b2Mettler the b2Mettler to set
     */
    public void setB2Mettler(Boolean b2Mettler) {
        if (b1Port == null) {
            b2Mettler = null;
        }
        config.setProperty(WB2_METTLER, Base64_Utils.encodeNTimes(b2Mettler != null
                ? b2PC.toString() : ""));
        this.b2Mettler = b2Mettler;
    }

    /**
     * Current SAP Working plant
     * @return the wPlant
     */
    public String getwPlant() {
        wPlant = Base64_Utils.decodeNTimes(config.getString(WK_PLANT, ""));
        return wPlant;
    }

    /**
     * Current SAP Working plant
     * @param wPlant the wPlant to set
     */
    public void setwPlant(String wPlant) {
        config.setProperty(WK_PLANT, Base64_Utils.encodeNTimes(wPlant));
        this.wPlant = wPlant;
    }

    /**
     * Administrator user
     * @return the adminUsr
     */
    public String getAdminUsr() {
        adminUsr = Base64_Utils.decodeNTimes(config.getString(AD_USR, this.adminUsr));
        return adminUsr;
    }

    /**
     * Administrator user
     * @param adminUsr the adminUsr to set
     */
    public void setAdminUsr(String adminUsr) {
        if (adminUsr == null) {
            adminUsr = this.adminUsr;
        }
        config.setProperty(AD_USR, adminUsr);
        this.adminUsr = adminUsr;
    }

    /**
     * Administrator password
     * @return the adminPwd
     */
    public String getAdminPwd() {
        adminPwd = Base64_Utils.decodeNTimes(config.getString(AD_PWD, ""));
        return adminPwd;
    }

    /**
     * Administrator password
     * @param adminPwd the adminPwd to set
     */
    public void setAdminPwd(String adminPwd) {
        this.setAdminUsr(null);
        config.setProperty(AD_PWD, adminPwd);
        this.adminPwd = adminPwd;
    }

    /**
     * @return the usrName
     */
    public String getUsrName() {
        usrName = Base64_Utils.decodeNTimes(config.getString(USER, ""));
        return usrName;
    }

    /**
     * @param usrName the usrName to set
     */
    public void setUsrName(String usrName) {
        config.setProperty(USER, usrName);
        this.usrName = usrName;
    }

    /**
     * @return the psswrd
     */
    public String getPsswrd() {
        psswrd = Base64_Utils.decodeNTimes(config.getString(PASS, ""));
        return psswrd;
    }

    /**
     * @param psswrd the psswrd to set
     */
    public void setPsswrd(String psswrd) {
        config.setProperty(PASS, psswrd);
        this.psswrd = psswrd;
    }

    public int getLimitWeight() {
        String limitW_str = Base64_Utils.decodeNTimes(config.getString(W_LIMIT, ""));
        try {
            limitWeight = Integer.parseInt(limitW_str);
        } catch (Exception e) {
            limitWeight = 0;
        }
        return limitWeight;
    }

    public void setLimitWeight(int limitWeight) {
        this.limitWeight = limitWeight;
    }

    public boolean isCheckVersionWB() {
        String str_checkVersionWB = Base64_Utils.decodeNTimes(config.getString(CHECK_VERSION_WB, "false"));
        try {
            this.checkVersionWB = Boolean.parseBoolean(str_checkVersionWB);
        } catch (Exception e) {
            this.checkVersionWB = false;
        }
        return this.checkVersionWB;
    }

    public void setCheckVersionWB(boolean checkVersionWB) {
        config.setProperty(MODE_NORMAL, checkVersionWB);
        this.checkVersionWB = checkVersionWB;
    }

    // </editor-fold>
    public void save() throws ConfigurationException {
        try {
            if (b1Port == null) {
                config.setProperty(WB1_BR, null);
                config.setProperty(WB1_DB, null);
                config.setProperty(WB1_SB, null);
                config.setProperty(WB1_PC, null);
                config.setProperty(WB1_METTLER, null);
            }
            if (b2Port == null) {
                config.setProperty(WB2_BR, null);
                config.setProperty(WB2_DB, null);
                config.setProperty(WB2_SB, null);
                config.setProperty(WB2_PC, null);
                config.setProperty(WB2_METTLER, null);
            }
            config.save();
        } catch (ConfigurationException ex) {
            Logger.getLogger(this.getClass()).error(null, ex);
            throw ex;
        }
    }

    /**
     * @return the rptId
     */
    public String getRptId() {
        rptId = Base64_Utils.decodeNTimes(config.getString(RPT_ID, ""));
        return rptId;
    }

    /**
     * @param rptId the rptId to set
     */
    public void setRptId(String rptId) {
        config.setProperty(RPT_ID, Base64_Utils.encodeNTimes(rptId));
        this.rptId = rptId;
    }
}
