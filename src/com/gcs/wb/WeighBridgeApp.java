/*
 * WeighBridgeApp.java
 */
package com.gcs.wb;

import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.gcs.wb.bapi.SAPSession;
import com.gcs.wb.base.exceptions.IllegalPortException;
import com.gcs.wb.base.serials.ScaleMettler;
import com.gcs.wb.base.serials.SerialComm;
import com.gcs.wb.batch.CronTriggerService;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.JReportConnector;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.ConfigurationRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.views.ConfigView;
import com.gcs.wb.views.LoginView;
import com.gcs.wb.views.SettingView;
import com.gcs.wb.views.WeighBridgeView;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.hibersap.session.Credentials;
import org.hibersap.session.Session;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import javax.swing.UIManager;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

/**
 * The main class of the application.
 */
public class WeighBridgeApp extends SingleFrameApplication {

    // <editor-fold defaultstate="collapsed" desc="Variables declaration area">
    private LoginView vLogin = null;
    private static WeighBridgeView vMain = null;
    private SettingView vSetting = null;
    private ConfigView configView = null;
    private AppConfig config = null;
    private SAPSetting sapSetting = null;
    private User login = null;
    /**
     * Working in Offline mode(SAP Server is not connectable)
     */
    private boolean offlineMode = false;
    private boolean authenticated = false;
    private SAPSession _SAPSession = null;
    /**
     * HiberSAP credential object
     */
    private Credentials credentials = null;
    private SerialComm normScale = null;
    private ScaleMettler mettlerScale = null;
    public static final String DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_DISPLAY_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String HOUR_DISPLAY_FORMAT = "HH:mm:ss";
    public static final String DATE_ID_FORMAT = "yyyyMMdd";
    public static final String DATE_HOUR_ID_FORMAT = "yyMMddHHmm";
    public static final String HOUR_ID_FORMAT = "HHmmss";
    private BigInteger last = BigInteger.ZERO;
    private BigInteger now = BigInteger.ZERO;
    private BigInteger max = BigInteger.ZERO;
    public static final int TIME_DELAY = 4;
    private String sloc = null;
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Methods override Area">
    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        config = new AppConfig();
        if (config.isHasDatabaseConfig()) {
            try {
                // check database connection
                JPAConnector.getInstance();

                // get config in database
                ConfigurationRepository configurationRepository = new ConfigurationRepository();
                config.setConfiguration(configurationRepository.getConfiguration(config.getWbId()));
            } catch (Exception ex) {
                ResourceMap resourceMap = Application.getInstance(this.getClass()).getContext().getResourceMap(ConfigView.class);
                JOptionPane.showMessageDialog(this.getMainFrame(), resourceMap.getString("msg.errorDbConnectionFail"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        if (!config.isFullyConfigured()) {
            configView = new ConfigView(this.getMainFrame());
            show(configView);
            if (!configView.isShowing()) {
                if (!config.isFullyConfigured()) {
                    configView.dispose();
                    configView = null;
                    exit();
                }
            }
        }

        vLogin = new LoginView(this.getMainFrame());
        show(vLogin);
        if (!vLogin.isShowing()) {
            authenticated = vLogin.isAuthenticated();
            if (authenticated) {
                vLogin.dispose();
                if (sapSetting.getNameRpt() == null || sapSetting.getAddress() == null
                        || sapSetting.getPhone() == null || sapSetting.getFax() == null) {
                    vSetting = new SettingView(this.getMainFrame());
                    show(vSetting);
                    if (!vSetting.isShowing()
                            && (sapSetting.getNameRpt() == null || sapSetting.getAddress() == null
                            || sapSetting.getPhone() == null || sapSetting.getFax() == null)) {
                        exit();
                    } else {
                        vSetting.dispose();
                        vSetting = null;
                    }
                }
                vMain = new WeighBridgeView(getApplication());
                vMain.getComponent().setSize(800, 600);
                vMain.getFrame().setSize(800, 600);
                vMain.getRootPane().setSize(800, 600);
                show(vMain);
                vLogin = null;
            } else {
                exit();
            }
        }
    }

    public void restartApplication() {
        getApplication().hide(vMain);
        vMain = null;
        JReportConnector.close();
        JPAConnector.close();

        try {
            CronTriggerService.close();
        } catch (SchedulerException ex) {
            java.util.logging.Logger.getLogger(WeighBridgeApp.class.getName()).log(Level.SEVERE, null, ex);
        }

        // restart
        launch(this.getClass(), null);
    }

    @Override
    protected void shutdown() {
        JPAConnector.close();

        if (_SAPSession != null && !_SAPSession.isClosed()) {
            _SAPSession.close();
        }
        super.shutdown();
    }

//
//    /**
//     * This method is to initialize the specified window by injecting resources.
//     * Windows shown in our application come fully initialized from the GUI
//     * builder, so this additional configuration is not needed.
//     */
//    @Override
//    protected void configureWindow(java.awt.Window root) {
//
//    }
    // </editor-fold>
    /**
     * A convenient static getter for the application instance.
     *
     * @return the instance of WeighBridgeApp
     */
    public static WeighBridgeApp getApplication() {
        return Application.getInstance(WeighBridgeApp.class);
    }

    public boolean connectWB(boolean autoSignal, String portName, Integer speed, Short dataBits, Short stopBits, Short parity, boolean isMettlerScale, JFormattedTextField control) throws SerialPortInvalidPortException, IllegalPortException, IOException, TooManyListenersException {

        boolean connected = false;
        Logger.getLogger(this.getClass()).info("@jSerialComm, connect to weight bridge, " + (isMettlerScale ? "@ScaleMettler" : "@SerialComm"));
        Logger.getLogger(this.getClass()).info("@jSerialComm, port: " + portName + " speed: " + speed + " databit: " + dataBits + " stopbit: " + stopBits);
        try {
            if (isMettlerScale) {
                if (mettlerScale != null) {
                    mettlerScale.disconnect();
                }
                if (autoSignal) {
                    mettlerScale = new ScaleMettler(portName, speed, dataBits, stopBits, parity, control);
                    mettlerScale.connect();
                }

            } else {
                if (normScale != null) {
                    normScale.disconnect();
                }
                if (autoSignal) {
                    normScale = new SerialComm(portName, speed, dataBits, stopBits, parity, control);
                    normScale.connect();
                }
            }
            connected = true;
        } catch (SerialPortInvalidPortException ex) {
            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 1", JOptionPane.WARNING_MESSAGE);
            throw ex;
        } catch (IllegalPortException ex) {
            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 2", JOptionPane.WARNING_MESSAGE);
            throw ex;
//        } catch (NoSuchPortException ex) {
//            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 3", JOptionPane.WARNING_MESSAGE);
//            throw ex;
//        } catch (UnsupportedCommOperationException ex) {
//            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 4", JOptionPane.WARNING_MESSAGE);
//            throw ex;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 5", JOptionPane.WARNING_MESSAGE);
            throw ex;
        } catch (TooManyListenersException ex) {
            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo 6", JOptionPane.WARNING_MESSAGE);
            throw ex;
        }
        return connected;
    }

    public void disconnectWB() {
        try {
            if (normScale != null) {
                normScale.disconnect();
            } else if (mettlerScale != null) {
                mettlerScale.disconnect();
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this.getMainFrame(), ex.getMessage(), "Thông báo thoát", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void bindJTableModel(JTable table, Object[][] data, Object[] columnNames,
            final Class[] types, final boolean[] editable) {//, Integer sortColumn, SortOrder order) {

        table.setModel(new DefaultTableModel(data, columnNames) {

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return editable[columnIndex];
            }
        });
    }

    /**
     * Main method launching the application.
     *
     * @param args
     */
    public static void main(String[] args) {
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
        launch(WeighBridgeApp.class, args);
    }

    // <editor-fold defaultstate="collapsed" desc="Variables Getter/Setter">
    /**
     * @return the config
     */
    public AppConfig getConfig() {
        return config;
    }

    /**
     * @param config the config to set
     */
    public void setConfig(AppConfig config) {
        this.config = config;
    }

    /**
     * @return the authenticated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @return the login
     */
    public User getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(User login) {
        this.login = login;
    }

    /**
     * Working in Offline mode(SAP Server is not connectable)
     *
     * @return the offlineMode
     */
    public boolean isOfflineMode() {
        return offlineMode;
    }

    /**
     * Working in Offline mode(SAP Server is not connectable)
     *
     * @param offlineMode the offlineMode to set
     */
    public void setOfflineMode(boolean offlineMode) {
        this.offlineMode = offlineMode;

        if (vMain != null && offlineMode) {
            vMain.switchToOfflineMode();
        }
    }

    /**
     * @return the sapSetting
     */
    public SAPSetting getSapSetting() {
        return sapSetting;
    }

    /**
     * @param sapSetting the sapSetting to set
     */
    public void setSapSetting(SAPSetting sapSetting) {
        this.sapSetting = sapSetting;
    }

    /**
     * HiberSAP credential object
     *
     * @param credentials the credentials to set
     */
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    /**
     * HiberSAP credential object
     *
     * @return the credentials
     */
    public Credentials getCredentials() {
        return credentials;
    }

    /**
     * @return the _SAPSession
     */
    public SAPSession getSAPSession() {
        return _SAPSession;
    }

    /**
     * @param SAPSession the _SAPSession to set
     */
    public void setSAPSession(SAPSession sapSession) {
        this._SAPSession = sapSession;
    }
    // </editor-fold>

    /**
     * @return the last
     */
    public BigInteger getLast() {
        return last;
    }

    /**
     * @param last the last to set
     */
    public void setLast(BigInteger last) {
        this.last = last;
    }

    /**
     * @return the now
     */
    public BigInteger getNow() {
        return now;
    }

    /**
     * @param now the now to set
     */
    public void setNow(BigInteger now) {
        this.now = now;
    }

    /**
     * @return the sloc
     */
    public String getSloc() {
        return sloc;
    }

    /**
     * @param sloc the sloc to set
     */
    public void setSloc(String sloc) {
        this.sloc = sloc;
    }

    /**
     * @return the max
     */
    public BigInteger getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(BigInteger max) {
        this.max = max;
    }
}
