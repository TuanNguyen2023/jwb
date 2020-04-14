/*
 * WeighBridgeView.java
 */
package com.gcs.wb.views;

import com.gcs.wb.*;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.controller.WeighBridgeController;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.SchedulerSync;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.repositorys.SchedulerSyncRepository;
import com.gcs.wb.service.SyncMasterDataService;
import static com.gcs.wb.service.SyncMasterDataService.logger;
import java.awt.event.WindowEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.jdesktop.application.Application;

/**
 * The application's main frame.
 */
public class WeighBridgeView extends FrameView {

    private final User login;
    private final WeighBridgeController weighBridgeController = new WeighBridgeController();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(WeighBridgeView.class);
    public JFrame mainFrame = WeighBridgeApp.getApplication().getMainFrame();
    private Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    SchedulerSyncRepository schedulerSyncRepository;
    SchedulerSync schedulerSync;
    boolean allowToSync = true;
    private static final Object schedulerSyncLock = new Object();
    public WeighBridgeView(SingleFrameApplication app) {
        super(app);
        initComponents();
        login = WeighBridgeApp.getApplication().getLogin();
        weighBridgeController.setStatus(login, txt_status);
        lblOffline.setVisible(WeighBridgeApp.getApplication().isOfflineMode());

        app.getMainFrame().setSize(800, 600);
        this.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.getFrame().addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent evt) {
                weighBridgeViewWindowClosing();
            }
        });

        // <editor-fold defaultstate="collapsed" desc="status bar initialization - message timeout, idle icon and busy animation, etc">
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        // </editor-fold>

    }

    public void setStatus(String message) {
        txt_status.setText(message);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        tabPane = new javax.swing.JTabbedPane();
        dpVR = new javax.swing.JDesktopPane();
        jToolBar1 = new javax.swing.JToolBar();
        dpWT = new javax.swing.JDesktopPane();
        dpTA = new javax.swing.JDesktopPane();
        dpWTList = new javax.swing.JDesktopPane();
        dpWTDList = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu mFile = new javax.swing.JMenu();
        miConfig = new javax.swing.JMenuItem();
        miSetting = new javax.swing.JMenuItem();
        miSyncMasterData = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        javax.swing.JMenuItem miExit = new javax.swing.JMenuItem();
        javax.swing.JMenu mHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem miAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        txt_status = new javax.swing.JLabel();
        lblOffline = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N

        scrollPane.setName("scrollPane"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WeighBridgeView.class);
        tabPane.setBackground(resourceMap.getColor("tabPane.background")); // NOI18N
        tabPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        tabPane.setFont(resourceMap.getFont("tabPane.font")); // NOI18N
        tabPane.setMaximumSize(new java.awt.Dimension(1920, 2080));
        tabPane.setMinimumSize(new java.awt.Dimension(1920, 2080));
        tabPane.setName("tabPane"); // NOI18N
        tabPane.setPreferredSize(new java.awt.Dimension(1920, 1080));
        tabPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabPaneMouseClicked(evt);
            }
        });
        tabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPaneStateChanged(evt);
            }
        });

        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_BAOVE") >= 0 ){
            dpVR.setBackground(resourceMap.getColor("dpVR.background")); // NOI18N
            dpVR.setAutoscrolls(true);
            dpVR.setName("dpVR"); // NOI18N
            dpVR.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    dpVRMouseClicked(evt);
                }
            });
            dpVR.addContainerListener(new java.awt.event.ContainerAdapter() {
                public void componentRemoved(java.awt.event.ContainerEvent evt) {
                    dpVRComponentRemoved(evt);
                }
            });

            jToolBar1.setRollover(true);
            jToolBar1.setName("jToolBar1"); // NOI18N
            jToolBar1.setBounds(480, 90, 13, 2);
            dpVR.add(jToolBar1, javax.swing.JLayeredPane.DEFAULT_LAYER);

            tabPane.addTab(resourceMap.getString("dpVR.TabConstraints.tabTitle"), null, dpVR, resourceMap.getString("dpVR.TabConstraints.tabToolTip")); // NOI18N
        }

        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_CAUCAN") >= 0){
            dpWT.setBackground(resourceMap.getColor("dpWT.background")); // NOI18N
            dpWT.setAutoscrolls(true);
            dpWT.setName("dpWT"); // NOI18N
            dpWT.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    dpWTMouseClicked(evt);
                }
            });
            dpWT.addContainerListener(new java.awt.event.ContainerAdapter() {
                public void componentRemoved(java.awt.event.ContainerEvent evt) {
                    dpWTComponentRemoved(evt);
                }
            });
            tabPane.addTab(resourceMap.getString("dpWT.TabConstraints.tabTitle"), null, dpWT, resourceMap.getString("dpWT.TabConstraints.tabToolTip")); // NOI18N
        }

        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_ADMIN") >= 0){
            dpTA.setBackground(resourceMap.getColor("dpTA.background")); // NOI18N
            dpTA.setName("dpTA"); // NOI18N
            dpTA.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    dpTAMouseClicked(evt);
                }
            });
            dpTA.addContainerListener(new java.awt.event.ContainerAdapter() {
                public void componentRemoved(java.awt.event.ContainerEvent evt) {
                    dpTAComponentRemoved(evt);
                }
            });
            tabPane.addTab(resourceMap.getString("dpTA.TabConstraints.tabTitle"), dpTA); // NOI18N
        }

        dpWTList.setBackground(resourceMap.getColor("dpWTList.background")); // NOI18N
        dpWTList.setName("dpWTList"); // NOI18N
        dpWTList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dpWTListMouseClicked(evt);
            }
        });
        dpWTList.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                dpWTListComponentRemoved(evt);
            }
        });
        tabPane.addTab(resourceMap.getString("dpWTList.TabConstraints.tabTitle"), dpWTList); // NOI18N

        dpWTDList.setBackground(resourceMap.getColor("dpWTDList.background")); // NOI18N
        dpWTDList.setName("dpWTDList"); // NOI18N
        dpWTDList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dpWTDListMouseClicked(evt);
            }
        });
        dpWTDList.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                dpWTDListComponentRemoved(evt);
            }
        });
        tabPane.addTab(resourceMap.getString("dpWTDList.TabConstraints.tabTitle"), dpWTDList); // NOI18N

        scrollPane.setViewportView(tabPane);
        tabPane.getAccessibleContext().setAccessibleName("");
        tabPane.getAccessibleContext().setAccessibleParent(scrollPane);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );

        menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuBar.setName("menuBar"); // NOI18N

        mFile.setText(resourceMap.getString("mFile.text")); // NOI18N
        mFile.setName("mFile"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WeighBridgeView.class, this);
        miConfig.setAction(actionMap.get("showConfigView")); // NOI18N
        miConfig.setText(resourceMap.getString("miConfig.text")); // NOI18N
        miConfig.setName("miConfig"); // NOI18N
        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_ADMIN") >= 0 ){
            mFile.add(miConfig);
        }

        miSetting.setAction(actionMap.get("showSettingView")); // NOI18N
        miSetting.setText(resourceMap.getString("miSetting.text")); // NOI18N
        miSetting.setName("miSetting"); // NOI18N
        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_ADMIN") >= 0 ){
            mFile.add(miSetting);
        }

        miSyncMasterData.setAction(actionMap.get("syncMasterData")); // NOI18N
        miSyncMasterData.setText(resourceMap.getString("miSyncMasterData.text")); // NOI18N
        miSyncMasterData.setName("miSyncMasterData"); // NOI18N
        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_ADMIN") >= 0 && !WeighBridgeApp.getApplication().isOfflineMode()){
            mFile.add(miSyncMasterData);
        }

        jSeparator1.setName("jSeparator1"); // NOI18N
        if(WeighBridgeApp.getApplication().getLogin().getRoles().toUpperCase().indexOf("Z_JWB_ADMIN") >= 0 ){
            mFile.add(jSeparator1);
        }

        miExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.ALT_MASK));
        miExit.setText(resourceMap.getString("miExit.text")); // NOI18N
        miExit.setName("miExit"); // NOI18N
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        mFile.add(miExit);

        menuBar.add(mFile);

        mHelp.setText(resourceMap.getString("mHelp.text")); // NOI18N
        mHelp.setName("mHelp"); // NOI18N

        miAbout.setAction(actionMap.get("showAboutBox")); // NOI18N
        miAbout.setName("miAbout"); // NOI18N
        mHelp.add(miAbout);

        menuBar.add(mHelp);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        txt_status.setText(resourceMap.getString("txt_status.text")); // NOI18N
        txt_status.setName("txt_status"); // NOI18N

        lblOffline.setFont(resourceMap.getFont("lblOffline.font")); // NOI18N
        lblOffline.setForeground(resourceMap.getColor("lblOffline.foreground")); // NOI18N
        lblOffline.setText(resourceMap.getString("lblOffline.text")); // NOI18N
        lblOffline.setName("lblOffline"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(479, 479, 479)
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 151, Short.MAX_VALUE)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txt_status)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOffline)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 252, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)))
                    .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_status)
                        .addComponent(lblOffline)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // <editor-fold defaultstate="collapsed" desc="Event Handler Area">

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            mainFrame = WeighBridgeApp.getApplication().getMainFrame();
            aboutBox = new AboutView(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        WeighBridgeApp.getApplication().show(aboutBox);
    }

    @Action
    public Task showConfigView() {
        return new ShowConfigViewTask(getApplication());
    }

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneStateChanged
        reinitTabContain();
    }//GEN-LAST:event_tabPaneStateChanged

    private void dpVRComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_dpVRComponentRemoved
        if (vehicleRegistrationView.isClosed()) {
            vehicleRegistrationView.dispose();
            vehicleRegistrationView = null;
        }
    }//GEN-LAST:event_dpVRComponentRemoved

    private void dpWTComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_dpWTComponentRemoved
        if (ifWT.isClosed()) {
            ifWT.dispose();
            ifWT = null;
        }
    }//GEN-LAST:event_dpWTComponentRemoved

    private void tabPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabPaneMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
        weighBridgeController.setStatus(login, txt_status);
    }//GEN-LAST:event_tabPaneMouseClicked

    private void dpVRMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dpVRMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
    }//GEN-LAST:event_dpVRMouseClicked

    private void dpWTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dpWTMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
    }//GEN-LAST:event_dpWTMouseClicked

    private void miExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        weighBridgeViewWindowClosing();
    }//GEN-LAST:event_miExitActionPerformed

    private void dpTAComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_dpTAComponentRemoved
        if (ifTA.isClosed()) {
            ifTA.dispose();
            ifTA = null;
        }
    }//GEN-LAST:event_dpTAComponentRemoved

    private void dpTAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dpTAMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
    }//GEN-LAST:event_dpTAMouseClicked

    private void dpWTListComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_dpWTListComponentRemoved
        if (ifWTList.isClosed()) {
            ifWTList.dispose();
            ifWTList = null;
        }
    }//GEN-LAST:event_dpWTListComponentRemoved

    private void dpWTListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dpWTListMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
    }//GEN-LAST:event_dpWTListMouseClicked

    private void dpWTDListComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_dpWTDListComponentRemoved
        if (dailyReportView.isClosed()) {
            dailyReportView.dispose();
            dailyReportView = null;
        }
    }//GEN-LAST:event_dpWTDListComponentRemoved

    private void dpWTDListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dpWTDListMouseClicked
        if (evt.getClickCount() == 2) {
            reinitTabContain();
        }
    }//GEN-LAST:event_dpWTDListMouseClicked
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Util Classes/Methods">

    private class ShowConfigViewTask extends org.jdesktop.application.Task<Object, Void> {

        ShowConfigViewTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ShowConfigViewTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            mainFrame = WeighBridgeApp.getApplication().getMainFrame();
            dgConf = new ConfigView(mainFrame);
            dgConf.setLocationRelativeTo(mainFrame);
            return true;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            WeighBridgeApp.getApplication().show(dgConf);
            super.succeeded(result);
        }
    }

    private void weighBridgeViewWindowClosing() {
        ResourceMap resourceMap = getResourceMap();
        int answer = JOptionPane.showConfirmDialog(
                this.getRootPane(),
                resourceMap.getString("msg.outApplication"),
                JOptionPane.OPTIONS_PROPERTY,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) {
            WeighBridgeApp.getApplication().exit();
        }
    }

    public void switchToOfflineMode() {
        lblOffline.setVisible(true);

        vehicleRegistrationView.dispose();
        vehicleRegistrationView = new WTRegOfflineView();
        dpVR.removeAll();
        dpVR.add(vehicleRegistrationView);
        vehicleRegistrationView.show();

        JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.switchToOfflineModeSuccess"));
    }

    public void reinitTabContain() {
        if (tabPane.getSelectedComponent().getName().equalsIgnoreCase(dpVR.getName())) {
            if (vehicleRegistrationView == null) {
                if (WeighBridgeApp.getApplication().isOfflineMode()) {
                    vehicleRegistrationView = new WTRegOfflineView();
                } else {
                    vehicleRegistrationView = new WTRegView();
                }

                dpVR.add(vehicleRegistrationView);
                vehicleRegistrationView.show();
            }
        }
        if (tabPane.getSelectedComponent().getName().equalsIgnoreCase(dpWT.getName())) {
            if (ifWT == null) {
                ifWT = new WeightTicketView();
                dpWT.add(ifWT);
                ifWT.show();
            }
        }
        if (tabPane.getSelectedComponent().getName().equalsIgnoreCase(dpTA.getName())) {
            if (ifTA == null) {
                ifTA = new TransportAgentView();
                dpTA.add(ifTA);
                ifTA.show();
            }
        }
        if (tabPane.getSelectedComponent().getName().equalsIgnoreCase(dpWTList.getName())) {
            if (ifWTList == null) {
                ifWTList = new WeightTicketReportView();
                dpWTList.add(ifWTList);
                ifWTList.show();
            }
        }
        if (tabPane.getSelectedComponent().getName().equalsIgnoreCase(dpWTDList.getName())) {
            if (dailyReportView == null) {
                dailyReportView = new DailyReportView();
                dpWTDList.add(dailyReportView);
                dailyReportView.show();
            }
        }
    }

    private class PrintWNotesListTask extends Task<Object, Void> {

        JasperViewer jv = null;
        boolean failed = false;

        PrintWNotesListTask(Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to PrintWNotesListTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            try {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("MANDT", WeighBridgeApp.getApplication().getCredentials().getClient());
                String reportName = null;
                reportName = "./rpt/rptBT/WNotesList.jrxml";
//                if (WeighBridgeApp.getApplication().getConfig().getConfiguration().isModeNormal()) {
//                    reportName = "./rpt/rptBT/WNotesList.jrxml";
//                } else {
//                    reportName = "./rpt/rptPQ/WNotesList.jrxml";
//                }

//                JasperDesign jasperDesign = JRXmlLoader.load(reportName);
//                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
//                Connection connect = JReportConnector.getInstance();
//                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, connect);
//                jv = new JasperViewer(jasperPrint, false);
            } catch (Exception ex) {
                failed = true;
                failed(ex);
            } finally {
                succeeded(failed);
            }
            return null;  // return your result
        }

        @Override
        protected void failed(Throwable cause) {
            logger.error(null, cause);
            JOptionPane.showMessageDialog(getRootPane(), cause.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        @Override
        protected void succeeded(Object result) {
            jv.setVisible((Boolean) result);
        }
    }

    @Action
    public void showSettingView() {
        mainFrame = WeighBridgeApp.getApplication().getMainFrame();
        dpSetting = new SettingView(mainFrame);
        dpSetting.setLocationRelativeTo(mainFrame);
        WeighBridgeApp.getApplication().show(dpSetting);
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task syncMasterData() {
        return new SyncMasterDataTask(getApplication());
    }

    private class SyncMasterDataTask extends Task<Object, Void> {

        SyncMasterDataTask(Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() throws Exception {
            synchronized (schedulerSyncLock) {
                logger.info("Check sync scheduler...");
                String mandt = configuration.getSapClient();
                String wplant = configuration.getWkPlant();
                schedulerSyncRepository = new SchedulerSyncRepository();
                schedulerSync = schedulerSyncRepository.findByParamMandtWplant(mandt, wplant);

                if (schedulerSync != null) {
                    allowToSync = schedulerSync.isManualSyncAllowed();
                    if (!allowToSync) {
                        logger.info("The master data is syncing in another progress.");
                        JOptionPane.showMessageDialog(mainFrame, resourceMapMsg.getString("msg.manualSyncDenied"));
                        return null;
                    }
                } else {
                    schedulerSync = new SchedulerSync(mandt, wplant);
                }
                schedulerSync.setLastManualSync(new Date());
                schedulerSync.setManualSyncStatus(SchedulerSync.SYNC_IN_PROGRESS);
                schedulerSyncRepository.updateLastSync(schedulerSync);

                Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        schedulerSync.setManualSyncStatus(SchedulerSync.SYNC_ERROR);
                        schedulerSyncRepository.updateLastSync(schedulerSync);
                    }
                });
                
                setStep(1, resourceMapMsg.getString("msg.isSyncMasterData"));
                SyncMasterDataService syncMasterDataService = new SyncMasterDataService();
                syncMasterDataService.syncMasterData();
                return null;  // return your result
            }
        }

        @Override
        protected void succeeded(Object result) {
            setStep(2, resourceMapMsg.getString("msg.syncMasterDataSuccess"));
            if (allowToSync) {
                synchronized (schedulerSyncLock) {
                    schedulerSync.setManualSyncStatus(SchedulerSync.SYNC_COMPLETED);
                    schedulerSyncRepository.updateLastSync(schedulerSync);
                }
            }
        }

        @Override
        protected void failed(Throwable thrwbl) {
            logger.error(thrwbl);
            setStep(2, resourceMapMsg.getString("msg.syncMasterDataFailed"));
            if (allowToSync) {
                synchronized (schedulerSyncLock) {
                    schedulerSync.setManualSyncStatus(SchedulerSync.SYNC_ERROR);
                    schedulerSyncRepository.updateLastSync(schedulerSync);
                }
            }
        }

        private void setStep(int step, String msg) {
            if (StringUtil.isNotEmptyString(msg)) {
                setMessage(msg);
            }
            setProgress(step, 1, 2);
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Variables declaration Area">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JDesktopPane dpTA;
    javax.swing.JDesktopPane dpVR;
    javax.swing.JDesktopPane dpWT;
    javax.swing.JDesktopPane dpWTDList;
    javax.swing.JDesktopPane dpWTList;
    javax.swing.JSeparator jSeparator1;
    javax.swing.JToolBar jToolBar1;
    javax.swing.JLabel lblOffline;
    javax.swing.JPanel mainPanel;
    javax.swing.JMenuBar menuBar;
    javax.swing.JMenuItem miConfig;
    javax.swing.JMenuItem miSetting;
    javax.swing.JMenuItem miSyncMasterData;
    javax.swing.JProgressBar progressBar;
    javax.swing.JScrollPane scrollPane;
    javax.swing.JLabel statusAnimationLabel;
    static javax.swing.JLabel statusMessageLabel;
    javax.swing.JPanel statusPanel;
    javax.swing.JTabbedPane tabPane;
    static javax.swing.JLabel txt_status;
    // End of variables declaration//GEN-END:variables
    private static Logger logger = Logger.getLogger(WeighBridgeView.class);
    private static Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JInternalFrame vehicleRegistrationView;
    private JInternalFrame ifWT;
    private JInternalFrame ifTA;
    private JInternalFrame ifWTList;
    private JInternalFrame dailyReportView;
    private JDialog aboutBox;
    private JDialog dgConf;
    private JDialog dpSetting;
    //</editor-fold>
}
