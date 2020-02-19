/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * GoodsIssueView.java
 *
 * Created on 26-11-2009, 14:22:57
 */
package com.gcs.wb.views;

import com.fazecast.jSerialComm.SerialPortInvalidPortException;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.SAPErrorTransform;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPOSTOCreatePGIBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.exceptions.IllegalPortException;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.base.util.RegexFormatter;
import com.gcs.wb.controller.WeightTicketController;
import com.gcs.wb.controller.WeightTicketRegistarationController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.JCoException;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.hibersap.SapException;
import org.hibersap.session.Session;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

import javax.persistence.EntityManager;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;

/*
 *
 * @author Tran-Vu
 */
public class WeightTicketView extends javax.swing.JInternalFrame {

    private AppConfig config = WeighBridgeApp.getApplication().getConfig();
    Configuration configuration = config.getConfiguration();
    private final SAPSetting sapSetting;
    private final User login;
    private static final Logger logger = Logger.getLogger(WeightTicketView.class);
    SimpleDateFormat formatter = new SimpleDateFormat();
    private BigDecimal total_qty_goods = BigDecimal.ZERO;
    private BigDecimal remain_qty_goods = BigDecimal.ZERO;
    private BigDecimal total_qty_free = BigDecimal.ZERO;
    private List<OutboundDelivery> outbDel_list = new ArrayList<>();
    private List<OutboundDeliveryDetail> outDetails_lits = new ArrayList<>();
    private String wt_ID = null;
    private boolean flag_fail = false;
    private int timeFrom = 0;
    private int timeTo = 0;
    private String ximang = null;
    public ResourceMap resourceMapMsg = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WeightTicketView.class);
    private com.gcs.wb.jpa.entity.Material material;
    private com.gcs.wb.jpa.entity.OutboundDelivery outbDel;
    private com.gcs.wb.jpa.entity.PurchaseOrder purOrder;
    private com.gcs.wb.jpa.entity.SAPSetting setting;
    private com.gcs.wb.jpa.entity.WeightTicket weightTicket;
    private MaterialConstraint materialConstraint;
    VendorRepository vendorRepository = new VendorRepository();
    EntityManager entityManager = JPAConnector.getInstance();

    WeightTicketController weightTicketController = new WeightTicketController();
    WeightTicketRegistarationController weightTicketRegistarationController = new WeightTicketRegistarationController();

    SAPService sapService = new SAPService();

    public WeightTicketView() {
        weightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        purOrder = new com.gcs.wb.jpa.entity.PurchaseOrder();
        outbDel = new com.gcs.wb.jpa.entity.OutboundDelivery();
        setting = java.beans.Beans.isDesignTime() ? null : WeighBridgeApp.getApplication().getSapSetting();
        material = new com.gcs.wb.jpa.entity.Material();
        materialConstraint = new MaterialConstraint();
        initComponents();

        txtMatnr.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                getSAPMatData(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                getSAPMatData(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                getSAPMatData(e);
            }
        });

        setBridge1(configuration.getWb1Port() != null);
        setBridge2(configuration.getWb2Port() != null);

        formatter = new SimpleDateFormat();
        cbxSLoc.setSelectedIndex(-1);
        txfCurScale.setEditable(true);
        txtInTime.setEditable(true);
        txtOutTime.setEditable(true);
        sapSetting = WeighBridgeApp.getApplication().getSapSetting();
        login = WeighBridgeApp.getApplication().getLogin();
        entityManager.clear();
        //String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        //List kunnr = this.customerRepository.getListCustomer(client);
        DefaultComboBoxModel result = weightTicketController.getCustomerByMaNdt();

        try {
            String pWbId = configuration.getWbId().trim();
            List sdev = null;// weightTicketRepository.getDev2(pWbId);
            if (sdev != null) {
                for (Object obj : sdev) {
                    Object[] wt = (Object[]) obj;
                    txfCurScale.setEditable(((Integer.parseInt(Base64_Utils.decodeNTimes(wt[0].toString())) == 1) ? true : false));
                    txtInTime.setEditable(((Integer.parseInt(Base64_Utils.decodeNTimes(wt[1].toString())) == 1) ? true : false));
                    txtOutTime.setEditable(((Integer.parseInt(Base64_Utils.decodeNTimes(wt[2].toString())) == 1) ? true : false));
                    break;
                }
            }
        } catch (NumberFormatException e) {
            logger.error(e.toString());
        }
        cbxKunnr.setModel(result);
        cbxKunnr.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer mat = (Customer) value;
                    setText(mat.getName1() + " " + mat.getName2());
                }
                return this;
            }
        });

        // tuanna 20120522_ setEnabled for "combo box Khach hang" depends on offline_mode
        cbxKunnr.setEnabled(WeighBridgeApp.getApplication().isOfflineMode());
//        TimeRange t = weightTicketController.getTime();
//        if (t != null) {
//            timeFrom = 0 + Integer.parseInt(t.getTimeFrom() != null ? (t.getTimeFrom().trim()) : "0");
//            timeTo = Integer.parseInt(t.getTimeTo() != null ? (t.getTimeTo().trim()) : "0");
//        }

        // cấu hình cho cầu cân hiển thị PO và vendor
        if ((sapSetting.getCheckPov()) != null && (sapSetting.getCheckPov()) == true) {
            txtPoPosto.setVisible(true);
            cbxVendorLoading.setVisible(true);
            cbxVendorTransport.setVisible(true);
            lblPoPosto.setVisible(true);
            lblVendorLoading.setVisible(true);
            lblVendorTransport.setVisible(true);
        } else {
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        grbType = new javax.swing.ButtonGroup();
        grbBridge = new javax.swing.ButtonGroup();
        grbCat = new javax.swing.ButtonGroup();
        pnWTFilter = new javax.swing.JPanel();
        lblWTNum = new javax.swing.JLabel();
        txtWTNum = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        pnCurScale = new javax.swing.JPanel();
        pnCurScaleData = new javax.swing.JPanel();
        rbtBridge1 = new javax.swing.JRadioButton();
        rbtBridge2 = new javax.swing.JRadioButton();
        txfCurScale = new javax.swing.JFormattedTextField();
        lblKG = new javax.swing.JLabel();
        btnAccept = new javax.swing.JButton();
        pnScaleData = new javax.swing.JPanel();
        lblIScale = new javax.swing.JLabel();
        lblOScale = new javax.swing.JLabel();
        lblGScale = new javax.swing.JLabel();
        txfInQty = new javax.swing.JFormattedTextField();
        txfOutQty = new javax.swing.JFormattedTextField();
        txfGoodsQty = new javax.swing.JFormattedTextField();
        lblIUnit = new javax.swing.JLabel();
        lblITime = new javax.swing.JLabel();
        lblOUnit = new javax.swing.JLabel();
        lblOTime = new javax.swing.JLabel();
        lblGUnit = new javax.swing.JLabel();
        txtInTime = new javax.swing.JTextField();
        txtOutTime = new javax.swing.JTextField();
        btnIScaleReset = new javax.swing.JButton();
        btnOScaleReset = new javax.swing.JButton();
        pnWTicket = new javax.swing.JPanel();
        btnPostAgain = new javax.swing.JButton();
        btnReprint = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        pnWTLeft = new javax.swing.JPanel();
        lblRegCat = new javax.swing.JLabel();
        rbtInward = new javax.swing.JRadioButton();
        rbtOutward = new javax.swing.JRadioButton();
        txtRegistrationNo = new javax.swing.JTextField();
        txtDName = new javax.swing.JTextField();
        lblRegistrationNo = new javax.swing.JLabel();
        lblDName = new javax.swing.JLabel();
        lblCMNDBL = new javax.swing.JLabel();
        txtCMNDBL = new javax.swing.JTextField();
        lblLicPlate = new javax.swing.JLabel();
        txtLicPlate = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblTrailerPlate = new javax.swing.JLabel();
        txtTrailerPlate = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        txtSling = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblSling = new javax.swing.JLabel();
        lblPallet = new javax.swing.JLabel();
        txtPallet = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblGRText = new javax.swing.JLabel();
        txtGRText = new javax.swing.JTextField();
        txtCementDesc = new javax.swing.JTextField();
        lblCementDesc = new javax.swing.JLabel();
        lblBatchProduce = new javax.swing.JLabel();
        txtBatchProduce = new javax.swing.JFormattedTextField();
        txtTicketId = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblTicketId = new javax.swing.JLabel();
        txtWeightTicketIdRef = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblWeightTicketIdRef = new javax.swing.JLabel();
        lblProcedure = new javax.swing.JLabel();
        txtProcedure = new javax.swing.JTextField();
        lblRemark = new javax.swing.JLabel();
        txtRemark = new javax.swing.JTextField();
        pnWTRight = new javax.swing.JPanel();
        txtDelNum = new javax.swing.JTextField();
        txtPONo = new javax.swing.JTextField();
        txtRegItem = new javax.swing.JTextField();
        txtMatnr = new javax.swing.JTextField();
        txtWeight = new javax.swing.JTextField();
        cbxKunnr = new javax.swing.JComboBox();
        cbxSLoc = new javax.swing.JComboBox();
        txtLgortIn = new javax.swing.JTextField();
        cbxCharg = new javax.swing.JComboBox();
        txtChargIn = new javax.swing.JTextField();
        txtPoPosto = new javax.swing.JTextField();
        cbxVendorLoading = new javax.swing.JComboBox();
        cbxVendorTransport = new javax.swing.JComboBox();
        lblVendorTransport = new javax.swing.JLabel();
        lblVendorLoading = new javax.swing.JLabel();
        lblPoPosto = new javax.swing.JLabel();
        lblChargIn = new javax.swing.JLabel();
        lblCharg = new javax.swing.JLabel();
        lblLgortIn = new javax.swing.JLabel();
        lblSLoc = new javax.swing.JLabel();
        lbKunnr = new javax.swing.JLabel();
        lblWeight = new javax.swing.JLabel();
        lblMatnr = new javax.swing.JLabel();
        lblRegItem = new javax.swing.JLabel();
        lblPONo = new javax.swing.JLabel();
        lblDelNum = new javax.swing.JLabel();
        lblSO = new javax.swing.JLabel();
        txtSO = new javax.swing.JTextField();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WeightTicketView.class);
        setBackground(resourceMap.getColor("Form.background")); // NOI18N
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setAutoscrolls(true);
        setName("Form"); // NOI18N

        pnWTFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnWTFilter.border.title"))); // NOI18N
        pnWTFilter.setMinimumSize(new java.awt.Dimension(306, 138));
        pnWTFilter.setName("pnWTFilter"); // NOI18N
        pnWTFilter.setPreferredSize(new java.awt.Dimension(306, 138));

        lblWTNum.setText(resourceMap.getString("lblWTNum.text")); // NOI18N
        lblWTNum.setName("lblWTNum"); // NOI18N

        txtWTNum.setFont(txtWTNum.getFont().deriveFont(txtWTNum.getFont().getStyle() | java.awt.Font.BOLD, txtWTNum.getFont().getSize()+2));
        txtWTNum.setForeground(resourceMap.getColor("txtWTNum.foreground")); // NOI18N
        txtWTNum.setText(resourceMap.getString("txtWTNum.text")); // NOI18N
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WeightTicketView.class, this);
        txtWTNum.setAction(actionMap.get("readWT")); // NOI18N
        txtWTNum.setName("txtWTNum"); // NOI18N
        txtWTNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtWTNumActionPerformed(evt);
            }
        });
        txtWTNum.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtWTNumFocusGained(evt);
            }
        });
        txtWTNum.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                checkWTNum(e);
            }

            public void removeUpdate(DocumentEvent e) {
                checkWTNum(e);
            }

            public void changedUpdate(DocumentEvent e) {
                checkWTNum(e);
            }
        });

        jButton1.setAction(actionMap.get("readWT")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setActionCommand(resourceMap.getString("jButton1.actionCommand")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnWTFilterLayout = new javax.swing.GroupLayout(pnWTFilter);
        pnWTFilter.setLayout(pnWTFilterLayout);
        pnWTFilterLayout.setHorizontalGroup(
            pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblWTNum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWTNum, javax.swing.GroupLayout.DEFAULT_SIZE, 324, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        pnWTFilterLayout.setVerticalGroup(
            pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTFilterLayout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTFilterLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblWTNum))
                    .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtWTNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        pnCurScale.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnCurScale.border.title"))); // NOI18N
        pnCurScale.setFocusable(false);
        pnCurScale.setMinimumSize(new java.awt.Dimension(292, 138));
        pnCurScale.setName("pnCurScale"); // NOI18N
        pnCurScale.setPreferredSize(new java.awt.Dimension(292, 138));
        pnCurScale.setLayout(new java.awt.BorderLayout());

        pnCurScaleData.setBackground(resourceMap.getColor("pnCurScaleData.background")); // NOI18N
        pnCurScaleData.setFocusable(false);
        pnCurScaleData.setMaximumSize(new java.awt.Dimension(281, 89));
        pnCurScaleData.setMinimumSize(new java.awt.Dimension(281, 89));
        pnCurScaleData.setName("pnCurScaleData"); // NOI18N
        pnCurScaleData.setPreferredSize(new java.awt.Dimension(280, 95));

        rbtBridge1.setBackground(resourceMap.getColor("rbtBridge1.background")); // NOI18N
        grbBridge.add(rbtBridge1);
        rbtBridge1.setFont(resourceMap.getFont("rbtBridge2.font")); // NOI18N
        rbtBridge1.setForeground(resourceMap.getColor("rbtBridge2.foreground")); // NOI18N
        rbtBridge1.setText(resourceMap.getString("rbtBridge1.text")); // NOI18N
        rbtBridge1.setToolTipText(resourceMap.getString("rbtBridge1.toolTipText")); // NOI18N
        rbtBridge1.setName("rbtBridge1"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${bridge1}"), rbtBridge1, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtBridge1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtBridge1ActionPerformed(evt);
            }
        });

        rbtBridge2.setAction(actionMap.get("checkedBridge2")); // NOI18N
        rbtBridge2.setBackground(resourceMap.getColor("rbtBridge2.background")); // NOI18N
        grbBridge.add(rbtBridge2);
        rbtBridge2.setFont(resourceMap.getFont("rbtBridge2.font")); // NOI18N
        rbtBridge2.setForeground(resourceMap.getColor("rbtBridge2.foreground")); // NOI18N
        rbtBridge2.setText(resourceMap.getString("rbtBridge2.text")); // NOI18N
        rbtBridge2.setName("rbtBridge2"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${bridge2}"), rbtBridge2, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtBridge2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtBridge2ActionPerformed(evt);
            }
        });

        txfCurScale.setBackground(resourceMap.getColor("txfCurScale.background")); // NOI18N
        txfCurScale.setBorder(null);
        txfCurScale.setForeground(resourceMap.getColor("txfCurScale.foreground")); // NOI18N
        txfCurScale.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfCurScale.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfCurScale.setFont(resourceMap.getFont("txfCurScale.font")); // NOI18N
        txfCurScale.setName("txfCurScale"); // NOI18N
        txfCurScale.setPreferredSize(new java.awt.Dimension(227, 38));
        txfCurScale.setValue((Number)0);

        lblKG.setBackground(resourceMap.getColor("lblKG.background")); // NOI18N
        lblKG.setFont(resourceMap.getFont("lblKG.font")); // NOI18N
        lblKG.setForeground(resourceMap.getColor("lblKG.foreground")); // NOI18N
        lblKG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblKG.setText(resourceMap.getString("lblKG.text")); // NOI18N
        lblKG.setName("lblKG"); // NOI18N

        btnAccept.setAction(actionMap.get("acceptScale")); // NOI18N
        btnAccept.setFont(resourceMap.getFont("btnAccept.font")); // NOI18N
        btnAccept.setText(resourceMap.getString("btnAccept.text")); // NOI18N
        btnAccept.setName("btnAccept"); // NOI18N
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnCurScaleDataLayout = new javax.swing.GroupLayout(pnCurScaleData);
        pnCurScaleData.setLayout(pnCurScaleDataLayout);
        pnCurScaleDataLayout.setHorizontalGroup(
            pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                        .addComponent(rbtBridge1)
                        .addGap(18, 18, 18)
                        .addComponent(rbtBridge2))
                    .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                                .addGap(167, 167, 167)
                                .addComponent(btnAccept, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE))
                            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                                .addComponent(txfCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lblKG)))))
                .addGap(53, 53, 53))
        );
        pnCurScaleDataLayout.setVerticalGroup(
            pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rbtBridge1)
                    .addComponent(rbtBridge2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKG))
                .addGap(8, 8, 8)
                .addComponent(btnAccept)
                .addContainerGap())
        );

        pnCurScale.add(pnCurScaleData, java.awt.BorderLayout.CENTER);

        pnScaleData.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnScaleData.border.title"))); // NOI18N
        pnScaleData.setToolTipText(resourceMap.getString("pnScaleData.toolTipText")); // NOI18N
        pnScaleData.setFocusable(false);
        pnScaleData.setName("pnScaleData"); // NOI18N

        lblIScale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIScale.setText(resourceMap.getString("lblIScale.text")); // NOI18N
        lblIScale.setName("lblIScale"); // NOI18N

        lblOScale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOScale.setText(resourceMap.getString("lblOScale.text")); // NOI18N
        lblOScale.setName("lblOScale"); // NOI18N

        lblGScale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblGScale.setText(resourceMap.getString("lblGScale.text")); // NOI18N
        lblGScale.setName("lblGScale"); // NOI18N

        txfInQty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txfInQty.setEditable(false);
        txfInQty.setForeground(resourceMap.getColor("txfInQty.foreground")); // NOI18N
        txfInQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfInQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfInQty.setText(resourceMap.getString("txfInQty.text")); // NOI18N
        txfInQty.setFont(resourceMap.getFont("txfGoodsQty.font")); // NOI18N
        txfInQty.setName("txfInQty"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${weightTicket.FScale}"), txfInQty, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txfOutQty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txfOutQty.setEditable(false);
        txfOutQty.setForeground(resourceMap.getColor("txfOutQty.foreground")); // NOI18N
        txfOutQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfOutQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfOutQty.setText(resourceMap.getString("txfOutQty.text")); // NOI18N
        txfOutQty.setFont(resourceMap.getFont("txfGoodsQty.font")); // NOI18N
        txfOutQty.setName("txfOutQty"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${weightTicket.SScale}"), txfOutQty, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txfGoodsQty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txfGoodsQty.setEditable(false);
        txfGoodsQty.setForeground(resourceMap.getColor("txfGoodsQty.foreground")); // NOI18N
        txfGoodsQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfGoodsQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfGoodsQty.setText(resourceMap.getString("txfGoodsQty.text")); // NOI18N
        txfGoodsQty.setFont(resourceMap.getFont("txfGoodsQty.font")); // NOI18N
        txfGoodsQty.setName("txfGoodsQty"); // NOI18N

        lblIUnit.setText(resourceMap.getString("lblIUnit.text")); // NOI18N
        lblIUnit.setName("lblIUnit"); // NOI18N

        lblITime.setText(resourceMap.getString("lblITime.text")); // NOI18N
        lblITime.setName("lblITime"); // NOI18N

        lblOUnit.setText(resourceMap.getString("lblOUnit.text")); // NOI18N
        lblOUnit.setName("lblOUnit"); // NOI18N

        lblOTime.setText(resourceMap.getString("lblOTime.text")); // NOI18N
        lblOTime.setName("lblOTime"); // NOI18N

        lblGUnit.setText(resourceMap.getString("lblGUnit.text")); // NOI18N
        lblGUnit.setName("lblGUnit"); // NOI18N

        txtInTime.setBackground(resourceMap.getColor("txtInTime.background")); // NOI18N
        txtInTime.setEditable(false);
        txtInTime.setFont(txtInTime.getFont().deriveFont(txtInTime.getFont().getStyle() | java.awt.Font.BOLD, txtInTime.getFont().getSize()+1));
        txtInTime.setForeground(resourceMap.getColor("txtInTime.foreground")); // NOI18N
        txtInTime.setText(resourceMap.getString("txtInTime.text")); // NOI18N
        txtInTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtInTime.setName("txtInTime"); // NOI18N
        txtInTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtInTimeKeyReleased(evt);
            }
        });

        txtOutTime.setBackground(resourceMap.getColor("txtOutTime.background")); // NOI18N
        txtOutTime.setEditable(false);
        txtOutTime.setFont(txtOutTime.getFont().deriveFont(txtOutTime.getFont().getStyle() | java.awt.Font.BOLD, txtOutTime.getFont().getSize()+1));
        txtOutTime.setForeground(resourceMap.getColor("txtInTime.foreground")); // NOI18N
        txtOutTime.setText(resourceMap.getString("txtOutTime.text")); // NOI18N
        txtOutTime.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtOutTime.setName("txtOutTime"); // NOI18N
        txtOutTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtOutTimeKeyReleased(evt);
            }
        });

        btnIScaleReset.setText(resourceMap.getString("btnIScaleReset.text")); // NOI18N
        btnIScaleReset.setFocusable(false);
        btnIScaleReset.setName("btnIScaleReset"); // NOI18N
        btnIScaleReset.setPreferredSize(new java.awt.Dimension(61, 21));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${stage1}"), btnIScaleReset, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        btnIScaleReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIScaleResetActionPerformed(evt);
            }
        });

        btnOScaleReset.setText(resourceMap.getString("btnOScaleReset.text")); // NOI18N
        btnOScaleReset.setFocusable(false);
        btnOScaleReset.setName("btnOScaleReset"); // NOI18N
        btnOScaleReset.setPreferredSize(new java.awt.Dimension(61, 21));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${stage2}"), btnOScaleReset, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        btnOScaleReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOScaleResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnScaleDataLayout = new javax.swing.GroupLayout(pnScaleData);
        pnScaleData.setLayout(pnScaleDataLayout);
        pnScaleDataLayout.setHorizontalGroup(
            pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnScaleDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblGScale)
                    .addComponent(lblOScale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblIScale, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfGoodsQty, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(txfOutQty, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addComponent(txfInQty, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnScaleDataLayout.createSequentialGroup()
                        .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblIUnit)
                            .addComponent(lblOUnit))
                        .addGap(18, 18, 18)
                        .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblITime)
                            .addComponent(lblOTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtOutTime, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                            .addComponent(txtInTime, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnIScaleReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnOScaleReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblGUnit))
                .addContainerGap())
        );
        pnScaleDataLayout.setVerticalGroup(
            pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnScaleDataLayout.createSequentialGroup()
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblIScale)
                    .addComponent(txfInQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtInTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblITime)
                    .addComponent(lblIUnit)
                    .addComponent(btnIScaleReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOScale, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txfOutQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOutTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOTime, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOScaleReset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGScale)
                    .addComponent(txfGoodsQty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblGUnit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnWTicket.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnWTicket.border.title"))); // NOI18N
        pnWTicket.setName("pnWTicket"); // NOI18N

        btnPostAgain.setText(resourceMap.getString("btnPostAgain.text")); // NOI18N
        btnPostAgain.setEnabled(false);
        btnPostAgain.setName("btnPostAgain"); // NOI18N
        btnPostAgain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostAgainActionPerformed(evt);
            }
        });

        btnReprint.setAction(actionMap.get("reprintWT")); // NOI18N
        btnReprint.setText(resourceMap.getString("btnReprint.text")); // NOI18N
        btnReprint.setName("btnReprint"); // NOI18N

        btnSave.setAction(actionMap.get("saveWT")); // NOI18N
        btnSave.setFont(resourceMap.getFont("btnSave.font")); // NOI18N
        btnSave.setText(resourceMap.getString("btnSave.text")); // NOI18N
        btnSave.setName("btnSave"); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        pnWTLeft.setName("pnWTLeft"); // NOI18N

        lblRegCat.setText(resourceMap.getString("lblRegCat.text")); // NOI18N
        lblRegCat.setName("lblRegCat"); // NOI18N

        grbCat.add(rbtInward);
        rbtInward.setText(resourceMap.getString("rbtInward.text")); // NOI18N
        rbtInward.setEnabled(false);
        rbtInward.setName("rbtInward"); // NOI18N

        grbCat.add(rbtOutward);
        rbtOutward.setText(resourceMap.getString("rbtOutward.text")); // NOI18N
        rbtOutward.setEnabled(false);
        rbtOutward.setName("rbtOutward"); // NOI18N

        txtRegistrationNo.setBackground(resourceMap.getColor("txtRegistrationNo.background")); // NOI18N
        txtRegistrationNo.setEditable(false);
        txtRegistrationNo.setText(resourceMap.getString("txtRegistrationNo.text")); // NOI18N
        txtRegistrationNo.setName("txtRegistrationNo"); // NOI18N

        txtDName.setEditable(false);
        txtDName.setName("txtDName"); // NOI18N

        lblRegistrationNo.setText(resourceMap.getString("lblRegistrationNo.text")); // NOI18N
        lblRegistrationNo.setName("lblRegistrationNo"); // NOI18N

        lblDName.setText(resourceMap.getString("lblDName.text")); // NOI18N
        lblDName.setName("lblDName"); // NOI18N

        lblCMNDBL.setText(resourceMap.getString("lblCMNDBL.text")); // NOI18N
        lblCMNDBL.setName("lblCMNDBL"); // NOI18N

        txtCMNDBL.setEditable(false);
        txtCMNDBL.setName("txtCMNDBL"); // NOI18N

        lblLicPlate.setText(resourceMap.getString("lblLicPlate.text")); // NOI18N
        lblLicPlate.setName("lblLicPlate"); // NOI18N

        txtLicPlate.setEditable(false);
        txtLicPlate.setName("txtLicPlate"); // NOI18N

        lblTrailerPlate.setText(resourceMap.getString("lblTrailerPlate.text")); // NOI18N
        lblTrailerPlate.setName("lblTrailerPlate"); // NOI18N

        txtTrailerPlate.setEditable(false);
        txtTrailerPlate.setName("txtTrailerPlate"); // NOI18N

        txtSling.setEditable(false);
        txtSling.setName("txtSling"); // NOI18N

        lblSling.setText(resourceMap.getString("lblSling.text")); // NOI18N
        lblSling.setName("lblSling"); // NOI18N

        lblPallet.setText(resourceMap.getString("lblPallet.text")); // NOI18N
        lblPallet.setName("lblPallet"); // NOI18N

        txtPallet.setEditable(false);
        txtPallet.setName("txtPallet"); // NOI18N

        lblGRText.setText(resourceMap.getString("lblGRText.text")); // NOI18N
        lblGRText.setName("lblGRText"); // NOI18N

        txtGRText.setEditable(false);
        txtGRText.setName("txtGRText"); // NOI18N
        txtGRText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGRTextKeyReleased(evt);
            }
        });

        txtCementDesc.setText(resourceMap.getString("txtCementDesc.text")); // NOI18N
        txtCementDesc.setName("txtCementDesc"); // NOI18N
        txtCementDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCementDescActionPerformed(evt);
            }
        });
        txtCementDesc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCementDescKeyReleased(evt);
            }
        });

        lblCementDesc.setText(resourceMap.getString("lblCementDesc.text")); // NOI18N
        lblCementDesc.setName("lblCementDesc"); // NOI18N

        lblBatchProduce.setText(resourceMap.getString("lblBatchProduce.text")); // NOI18N
        lblBatchProduce.setName("lblBatchProduce"); // NOI18N

        txtBatchProduce.setName("txtBatchProduce"); // NOI18N

        txtTicketId.setEditable(false);
        txtTicketId.setName("txtTicketId"); // NOI18N

        lblTicketId.setText(resourceMap.getString("lblTicketId.text")); // NOI18N
        lblTicketId.setName("lblTicketId"); // NOI18N

        txtWeightTicketIdRef.setEditable(false);
        txtWeightTicketIdRef.setName("txtWeightTicketIdRef"); // NOI18N

        lblWeightTicketIdRef.setText(resourceMap.getString("lblWeightTicketIdRef.text")); // NOI18N
        lblWeightTicketIdRef.setName("lblWeightTicketIdRef"); // NOI18N

        lblProcedure.setText(resourceMap.getString("lblProcedure.text")); // NOI18N
        lblProcedure.setToolTipText(resourceMap.getString("lblProcedure.toolTipText")); // NOI18N
        lblProcedure.setName("lblProcedure"); // NOI18N

        txtProcedure.setBackground(resourceMap.getColor("txtProcedure.background")); // NOI18N
        txtProcedure.setEditable(false);
        txtProcedure.setName("txtProcedure"); // NOI18N

        lblRemark.setText(resourceMap.getString("lblRemark.text")); // NOI18N
        lblRemark.setName("lblRemark"); // NOI18N

        txtRemark.setName("txtRemark"); // NOI18N
        txtRemark.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRemarkKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnWTLeftLayout = new javax.swing.GroupLayout(pnWTLeft);
        pnWTLeft.setLayout(pnWTLeftLayout);
        pnWTLeftLayout.setHorizontalGroup(
            pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTLeftLayout.createSequentialGroup()
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPallet)
                    .addComponent(lblGRText)
                    .addComponent(lblSling)
                    .addComponent(lblTrailerPlate)
                    .addComponent(lblRegCat)
                    .addComponent(lblRegistrationNo)
                    .addComponent(lblDName)
                    .addComponent(lblCMNDBL)
                    .addComponent(lblBatchProduce)
                    .addComponent(lblCementDesc)
                    .addComponent(lblWeightTicketIdRef)
                    .addComponent(lblTicketId)
                    .addComponent(lblLicPlate))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnWTLeftLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(rbtInward)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                        .addComponent(rbtOutward)
                        .addGap(10, 10, 10))
                    .addComponent(txtRegistrationNo, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtDName, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtGRText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtTrailerPlate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtSling, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtLicPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtCMNDBL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtPallet, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtTicketId, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtBatchProduce, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtCementDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                    .addComponent(txtWeightTicketIdRef, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTLeftLayout.createSequentialGroup()
                        .addComponent(lblProcedure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtProcedure, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
                    .addGroup(pnWTLeftLayout.createSequentialGroup()
                        .addComponent(lblRemark)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtRemark, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnWTLeftLayout.setVerticalGroup(
            pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtOutward)
                    .addComponent(rbtInward)
                    .addComponent(lblRegCat)
                    .addComponent(lblProcedure)
                    .addComponent(txtProcedure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegistrationNo)
                    .addComponent(txtRegistrationNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDName)
                    .addComponent(txtDName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCMNDBL)
                    .addComponent(txtCMNDBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLicPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLicPlate))
                .addGap(18, 18, 18)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrailerPlate)
                    .addComponent(txtTrailerPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblSling)
                    .addComponent(txtSling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPallet))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblGRText)
                    .addComponent(txtGRText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRemark))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCementDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCementDesc))
                .addGap(9, 9, 9)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBatchProduce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBatchProduce))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTicketId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTicketId))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWeightTicketIdRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWeightTicketIdRef))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pnWTRight.setName("pnWTRight"); // NOI18N

        txtDelNum.setEditable(false);
        txtDelNum.setName("txtDelNum"); // NOI18N

        txtPONo.setEditable(false);
        txtPONo.setAction(actionMap.get("readPO")); // NOI18N
        txtPONo.setName("txtPONo"); // NOI18N
        txtPONo.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                checkPONum(e);
            }

            public void removeUpdate(DocumentEvent e) {
                checkPONum(e);
            }

            public void changedUpdate(DocumentEvent e) {
                checkPONum(e);
            }
        });

        txtRegItem.setEditable(false);
        txtRegItem.setName("txtRegItem"); // NOI18N

        txtMatnr.setEditable(false);
        txtMatnr.setText(resourceMap.getString("txtMatnr.text")); // NOI18N
        txtMatnr.setName("txtMatnr"); // NOI18N

        txtWeight.setEditable(false);
        txtWeight.setName("txtWeight"); // NOI18N

        cbxKunnr.setName("cbxKunnr"); // NOI18N
        cbxKunnr.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxKunnrItemStateChanged(evt);
            }
        });

        cbxSLoc.setModel(sapService.getSlocModel());
        cbxSLoc.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SLoc) {
                    SLoc sloc = (SLoc)value;
                    setText(sloc.getLgort().concat(" - ").concat(sloc.getLgobe()));
                    //                    setText(sloc.getLgobe().concat(" - ").concat(sloc.getLgort()));
                }
                return this;
            }
        });
        cbxSLoc.setName("cbxSLoc"); // NOI18N
        cbxSLoc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxSLocItemStateChanged(evt);
            }
        });

        txtLgortIn.setEditable(false);
        txtLgortIn.setName("txtLgortIn"); // NOI18N
        txtLgortIn.setRequestFocusEnabled(false);

        cbxCharg.setAction(actionMap.get("acceptBatch")); // NOI18N
        cbxCharg.setName("cbxCharg"); // NOI18N
        cbxCharg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxChargKeyReleased(evt);
            }
        });

        txtChargIn.setEditable(false);
        txtChargIn.setName("txtChargIn"); // NOI18N

        txtPoPosto.setName("txtPoPosto"); // NOI18N
        txtPoPosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPoPostoActionPerformed(evt);
            }
        });
        txtPoPosto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPoPostoKeyReleased(evt);
            }
        });

        cbxVendorLoading.setModel(sapService.getVendorList());
        cbxVendorLoading.setAction(actionMap.get("acceptBatch")); // NOI18N
        cbxVendorLoading.setName("cbxVendorLoading"); // NOI18N
        cbxVendorLoading.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vendor) {
                    Vendor mat = (Vendor) value;
                    setText(mat.getName1() + " " + mat.getName2());
                }
                return this;
            }
        });
        cbxVendorLoading.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxVendorLoadingKeyReleased(evt);
            }
        });

        cbxVendorTransport.setModel(sapService.getVendorList());
        cbxVendorTransport.setAction(actionMap.get("acceptBatch")); // NOI18N
        cbxVendorTransport.setName("cbxVendorTransport"); // NOI18N
        cbxVendorTransport.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vendor) {
                    Vendor mat = (Vendor) value;
                    setText(mat.getName1() + " " + mat.getName2());
                }
                return this;
            }
        });
        cbxVendorTransport.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxVendorTransportKeyReleased(evt);
            }
        });

        lblVendorTransport.setText(resourceMap.getString("lblVendorTransport.text")); // NOI18N
        lblVendorTransport.setName("lblVendorTransport"); // NOI18N

        lblVendorLoading.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVendorLoading.setText(resourceMap.getString("lblVendorLoading.text")); // NOI18N
        lblVendorLoading.setName("lblVendorLoading"); // NOI18N

        lblPoPosto.setText(resourceMap.getString("lblPoPosto.text")); // NOI18N
        lblPoPosto.setName("lblPoPosto"); // NOI18N

        lblChargIn.setText(resourceMap.getString("lblChargIn.text")); // NOI18N
        lblChargIn.setName("lblChargIn"); // NOI18N

        lblCharg.setText(resourceMap.getString("lblCharg.text")); // NOI18N
        lblCharg.setName("lblCharg"); // NOI18N

        lblLgortIn.setText(resourceMap.getString("lblLgortIn.text")); // NOI18N
        lblLgortIn.setName("lblLgortIn"); // NOI18N

        lblSLoc.setText(resourceMap.getString("lblSLoc.text")); // NOI18N
        lblSLoc.setName("lblSLoc"); // NOI18N

        lbKunnr.setText(resourceMap.getString("lbKunnr.text")); // NOI18N
        lbKunnr.setName("lbKunnr"); // NOI18N

        lblWeight.setText(resourceMap.getString("lblWeight.text")); // NOI18N
        lblWeight.setName("lblWeight"); // NOI18N

        lblMatnr.setText(resourceMap.getString("lblMatnr.text")); // NOI18N
        lblMatnr.setName("lblMatnr"); // NOI18N

        lblRegItem.setText(resourceMap.getString("lblRegItem.text")); // NOI18N
        lblRegItem.setName("lblRegItem"); // NOI18N

        lblPONo.setText(resourceMap.getString("lblPONo.text")); // NOI18N
        lblPONo.setName("lblPONo"); // NOI18N

        lblDelNum.setText(resourceMap.getString("lblDelNum.text")); // NOI18N
        lblDelNum.setName("lblDelNum"); // NOI18N

        lblSO.setText(resourceMap.getString("lblSO.text")); // NOI18N
        lblSO.setName("lblSO"); // NOI18N

        txtSO.setEditable(false);
        txtSO.setText(resourceMap.getString("txtSO.text")); // NOI18N
        txtSO.setName("txtSO"); // NOI18N

        javax.swing.GroupLayout pnWTRightLayout = new javax.swing.GroupLayout(pnWTRight);
        pnWTRight.setLayout(pnWTRightLayout);
        pnWTRightLayout.setHorizontalGroup(
            pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblWeight)
                    .addComponent(lblRegItem)
                    .addComponent(lblPONo)
                    .addComponent(lblSO)
                    .addComponent(lblVendorTransport)
                    .addComponent(lblVendorLoading)
                    .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblSLoc)
                        .addComponent(lblMatnr)
                        .addComponent(lblPoPosto)
                        .addComponent(lblChargIn)
                        .addComponent(lblCharg)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnWTRightLayout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lbKunnr)
                                .addComponent(lblLgortIn, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblDelNum, javax.swing.GroupLayout.Alignment.TRAILING)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbxVendorTransport, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxCharg, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxVendorLoading, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxSLoc, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtDelNum)
                    .addComponent(txtSO, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(txtMatnr)
                    .addComponent(txtPONo, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(cbxKunnr, 0, 260, Short.MAX_VALUE)
                    .addComponent(txtChargIn, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(txtPoPosto, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(txtLgortIn, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(txtRegItem, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                    .addComponent(txtWeight))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnWTRightLayout.setVerticalGroup(
            pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblDelNum)
                    .addComponent(txtDelNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSO))
                .addGap(11, 11, 11)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPONo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPONo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtMatnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMatnr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lbKunnr)
                    .addComponent(cbxKunnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtLgortIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLgortIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCharg)
                    .addComponent(cbxCharg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblChargIn)
                    .addComponent(txtChargIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(lblPoPosto)
                    .addComponent(txtPoPosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorLoading))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorTransport))
                .addContainerGap())
        );

        txtMatnr.getAccessibleContext().setAccessibleName(resourceMap.getString("txtMatnr.AccessibleContext.accessibleName")); // NOI18N

        javax.swing.GroupLayout pnWTicketLayout = new javax.swing.GroupLayout(pnWTicket);
        pnWTicket.setLayout(pnWTicketLayout);
        pnWTicketLayout.setHorizontalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTicketLayout.createSequentialGroup()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addGap(561, 561, 561)
                        .addComponent(btnPostAgain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                        .addComponent(btnReprint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnWTicketLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnWTLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnWTRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnWTicketLayout.setVerticalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTicketLayout.createSequentialGroup()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnWTLeft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnWTRight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnReprint)
                    .addComponent(btnPostAgain))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnWTicket, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnScaleData, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnCurScale, javax.swing.GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnCurScale, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                    .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnScaleData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnWTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="expanded" desc="Event Handlers Area">
    private void rbtBridge1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBridge1ActionPerformed
        config = WeighBridgeApp.getApplication().getConfig();
        configuration = config.getConfiguration();
        try {

            btnAccept.setEnabled(WeighBridgeApp.getApplication().connectWB(
                    configuration.getWb1Port(), //string
                    configuration.getWb1BaudRate(), //int 
                    configuration.getWb1DataBit().shortValue(), //short 
                    configuration.getWb1StopBit().shortValue(), //short 
                    configuration.getWb1ParityControl().shortValue(), //short
                    configuration.getWb1Mettler(),
                    txfCurScale));

            setSaveNeeded(isValidated());

        } catch (SerialPortInvalidPortException | IllegalPortException | IOException | TooManyListenersException ex) {

            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_rbtBridge1ActionPerformed

    private void rbtBridge2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBridge2ActionPerformed
        config = WeighBridgeApp.getApplication().getConfig();
        configuration = config.getConfiguration();
        try {
            btnAccept.setEnabled(WeighBridgeApp.getApplication().connectWB(
                    configuration.getWb2Port(), //string
                    configuration.getWb2BaudRate(), //int 
                    configuration.getWb2DataBit().shortValue(), //short 
                    configuration.getWb2StopBit().shortValue(), //short 
                    configuration.getWb2ParityControl().shortValue(), //short
                    configuration.getWb2Mettler(),
                    txfCurScale));
            setSaveNeeded(isValidated());

        } catch (SerialPortInvalidPortException | IllegalPortException | IOException | TooManyListenersException ex) {
            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_rbtBridge2ActionPerformed

    private void btnIScaleResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIScaleResetActionPerformed
        txfInQty.setValue(null);
        txtInTime.setText(null);
        txfGoodsQty.setValue(null);
        //  Temporary enable Accept Button
//        btnAccept.setEnabled(true);
        grbBridge.clearSelection();
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_btnIScaleResetActionPerformed

    private void btnOScaleResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOScaleResetActionPerformed
        txfOutQty.setValue(null);
        txtOutTime.setText(null);
        txfGoodsQty.setValue(null);
        //  Temporary enable Accept Button
//        btnAccept.setEnabled(true);
        grbBridge.clearSelection();
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_btnOScaleResetActionPerformed

    private void cbxSLocItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxSLocItemStateChanged
        if ((cbxSLoc.getSelectedIndex() == -1) || (cbxSLoc.getSelectedItem() == null)) {
            return;
        }
        config = WeighBridgeApp.getApplication().getConfig();
        SLoc selSloc = (SLoc) cbxSLoc.getSelectedItem();
        weightTicket.setLgort(selSloc.getLgort());
        if (selSloc != null && (txtMatnr.getText() != null && !txtMatnr.getText().trim().isEmpty())) {
            lblSLoc.setForeground(Color.black);
            // sync data
            weightTicketController.getSyncBatchStocks(selSloc, weightTicket);
            // get data DB
            List<BatchStock> batchs = weightTicketController.getBatchStocks(selSloc, weightTicket);
            cbxCharg.setModel(weightTicketController.setCbxBatch(batchs));
            cbxCharg.setSelectedIndex(-1);
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxSLocItemStateChanged

    private void txtGRTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRTextKeyReleased
        if (weightTicket != null) {
            weightTicket.setText(txtGRText.getText().trim());
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtGRTextKeyReleased

    private void cbxChargKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxChargKeyReleased
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxChargKeyReleased

private void txtCementDescKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCementDescKeyReleased
// TODO add your handling code here:
    if (weightTicket != null) {
        weightTicket.setSoNiemXa(txtCementDesc.getText());
    }
    setSaveNeeded(isValidated());
}//GEN-LAST:event_txtCementDescKeyReleased

private void txtCementDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCementDescActionPerformed

    // TODO add your handling code here:
    setSaveNeeded(isValidated());
}//GEN-LAST:event_txtCementDescActionPerformed

private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
// TODO add your handling code here:
    setSaveNeeded(isValidated());
}//GEN-LAST:event_btnAcceptActionPerformed

private void btnPostAgainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostAgainActionPerformed
// TODO add your handling code here:
    int answer = -1;
    answer = JOptionPane.showConfirmDialog(
            this.getRootPane(),
            resourceMapMsg.getString("msg.questionPostTicket"),
            JOptionPane.OPTIONS_PROPERTY,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
    if (answer == JOptionPane.YES_OPTION) {
        setSaveNeeded(true);
        btnPostAgain.setEnabled(false);
        weightTicket.setPosted(true);
        weightTicketController.savePostAgainActionPerformed(weightTicket);
    } else {
        setSaveNeeded(false);
    }
}//GEN-LAST:event_btnPostAgainActionPerformed

private void cbxKunnrItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxKunnrItemStateChanged
    // 20120522_ setEnabled for "combo box Khach hang" depends on offline_mode
    // ++ check Ma phieu is empty or not to execute the code inside
    if (cbxKunnr.getSelectedItem() != null && !cbxKunnr.getSelectedItem().toString().equals("") && !txtWTNum.getText().trim().equals("")) {
        Object[] select = cbxKunnr.getSelectedObjects();
        Customer cust = (Customer) select[0];
        if (weightTicket != null) {
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            weightTicketDetail.setKunnr(cust.getKunnr());
            weightTicketController.saveKunnrItemStateChanged(weightTicket);
        }
    }
}//GEN-LAST:event_cbxKunnrItemStateChanged

private void txtOutTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOutTimeKeyReleased
// TODO add your handling code here:
    if (txtOutTime.getText().length() == 19) {
        String[] time = txtOutTime.getText().split(" ");
        weightTicket.setSTime(weightTicketController.setTimeWeightTicket(time));
    }
}//GEN-LAST:event_txtOutTimeKeyReleased

private void txtInTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInTimeKeyReleased
// TODO add your handling code here:
    if (txtInTime.getText().length() == 19) {
        String[] time = txtInTime.getText().split(" ");
        weightTicket.setFTime(weightTicketController.setTimeWeightTicket(time));
    }
}//GEN-LAST:event_txtInTimeKeyReleased

private void txtWTNumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWTNumFocusGained
// Tuanna add -- for paste ID from Clipboard if data is ticket  --28.11.2012

    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText
            = (contents != null)
            && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
        try {
            result = (String) contents.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            logger.error(ex.toString());
        }
    }
    if (result.length() == 10) {
        txtWTNum.setText(result);
    }

    //end Add.

}//GEN-LAST:event_txtWTNumFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //tuanna
        StringSelection stringSelection = new StringSelection("");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        txtWTNum.selectAll();
        txtWTNum.setText("");
        txtWTNum.requestFocusInWindow();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtWTNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWTNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWTNumActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

private void cbxVendorLoadingKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxVendorLoadingKeyReleased
// TODO add your handling code here:
}//GEN-LAST:event_cbxVendorLoadingKeyReleased

private void cbxVendorTransportKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxVendorTransportKeyReleased
// TODO add your handling code here:
}//GEN-LAST:event_cbxVendorTransportKeyReleased

private void txtPoPostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPoPostoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_txtPoPostoActionPerformed

private void txtPoPostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPoPostoKeyReleased
// TODO add your handling code here:
}//GEN-LAST:event_txtPoPostoKeyReleased

private void txtRemarkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarkKeyReleased
// TODO add your handling code here:
}//GEN-LAST:event_txtRemarkKeyReleased

    @Action
    public void showMB1BOption() {
//        if (weightTicket == null) {
//            return;
//        }
//        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
//
//        if (rbtMb1b.isSelected() && rbtInward.isSelected()) {
//            grbType.clearSelection();
//            return;
//        }
//        if (rbtMb1b.isSelected() && weightTicket.getRegType() == 'O') {
//            RecvSlocView rsview = new RecvSlocView(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), weightTicket.getRecvPo());
//            rsview.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
//            WeighBridgeApp.getApplication().show(rsview);
//            if (!rsview.isShowing()) {
//                if (rsview.getRecSloc() != null) {
//                    weightTicket.setRecvLgort(rsview.getRecSloc().getLgort());
//                } else {
//                    weightTicket.setRecvLgort(null);
//                }
//                if (rsview.getRefPO() != null) {
//                    weightTicket.setRecvPo(rsview.getRefPO());
//                } else {
//                    weightTicket.setRecvPo(null);
//                }
//                weightTicket.setRecvPlant(configuration.getWkPlant());
//                weightTicket.setRecvCharg(weightTicket.getCharg());
////                weightTicket.setRecvMatnr(setting.getMatnrClinker());
////                weightTicket.setMatnrRef(setting.getMatnrClinker());
//                weightTicketDetail.setRegItemDescription(Constants.WeightTicketView.ITEM_DESCRIPTION);
//                weightTicketDetail.setUnit("TO");
//                weightTicket.setMoveType("313");
//                weightTicket.setMoveReas("0003");
//                rsview.dispose();
//                rsview = null;
//                txtRegItem.setText(weightTicketDetail.getRegItemDescription());
//            }
//        }
//        txtMatnr.setText(weightTicketDetail.getRecvMatnr());
//        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
//                && grbType.getSelection() != null) {
//            rbtPO.setForeground(Color.black);
//            rbtMisc.setForeground(Color.black);
//            if (WeighBridgeApp.getApplication().isOfflineMode()) {
//                rbtMisc.setForeground(Color.RED);
//            }
//            rbtMb1b.setForeground(Color.black);
//            rbtMvt311.setForeground(Color.black);
//        }
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMvt311() {
//        if (weightTicket == null) {
//            return;
//        }
//
//        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
//        if (rbtMvt311.isSelected() && rbtInward.isSelected()) {
//            grbType.clearSelection();
//            return;
//        }
//        txtRegItem.setText(weightTicketDetail.getRegItemDescription());
//        if (rbtMvt311.isSelected() && weightTicket.getRegType() == 'O') {
//            Mvt311View mvt311View = new Mvt311View(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), null);
//            mvt311View.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
//            WeighBridgeApp.getApplication().show(mvt311View);
//            if (!mvt311View.isShowing()) {
//                if (mvt311View.getRecSloc() != null) {
//                    weightTicket.setRecvLgort(mvt311View.getRecSloc().getLgort());
//                } else {
//                    weightTicket.setRecvLgort(null);
//                }
//                if (mvt311View.getSelMaterial() != null) {
//                    material = mvt311View.getSelMaterial();
//                    weightTicketDetail.setRecvMatnr(material.getMatnr());
//                    weightTicketDetail.setMatnrRef(material.getMatnr());
//                    weightTicketDetail.setRegItemDescription(material.getMaktx());
//                } else {
//                    material = null;
//                    weightTicketDetail.setRecvMatnr(null);
//                    weightTicketDetail.setMatnrRef(null);
////                    weightTicket.setRegItemDescription(null);
//                }
//                weightTicket.setRecvPlant(configuration.getWkPlant());
//                weightTicket.setRecvCharg(weightTicket.getCharg());
//                weightTicketDetail.setUnit("TON");
//                weightTicket.setMoveType("311");
//                weightTicket.setMoveReas(null);
//                mvt311View.dispose();
//                mvt311View = null;
//                txtRegItem.setText(weightTicketDetail.getRegItemDescription());
//            }
//        }
//        txtMatnr.setText(weightTicketDetail.getRecvMatnr());
//        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
//                && grbType.getSelection() != null) {
//            rbtPO.setForeground(Color.black);
//            rbtMisc.setForeground(Color.black);
//            if (WeighBridgeApp.getApplication().isOfflineMode()) {
//                rbtMisc.setForeground(Color.red);
//            }
//            rbtMb1b.setForeground(Color.black);
//            rbtMvt311.setForeground(Color.black);
//        }
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtPO() {

            /* try{
            String sql = "call pvc_getTicketIndex ( ? ) " ;
            Query qx = (Query) entityManager.createNativeQuery(sql);
            qx.setParameter(1,txtWTNum.getText().toString());
            List wtx = qx.getResultList();
            
            try {
            //  SoNiemXa = wts.get(0).toString();
            //   txtCementDesc.setText(SoNiemXa);
            txtPONum.setText(wtx.get(2).toString());
            weightTicket.setAbbr(wtx.get(3).toString());
            
            }catch(Throwable cause ){
            //    klmax  = -1 ;
            }
            
            
            
            } catch(Throwable cause ){
            
            }
             * 
             */

        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMisc() {
//        if (rbtMisc.isSelected()) {
//            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
//            txtRegItem.setText(weightTicketDetail.getRegItemDescription());
//            weightTicketDetail.setEbeln(null);
//            weightTicketDetail.setItem(null);
//            weightTicket.setRecvLgort(null);
//            weightTicket.setRecvPlant(null);
//            weightTicket.setRecvCharg(null);
//            weightTicket.setRecvPo(null);
//            weightTicketDetail.setRecvMatnr(null);
//            weightTicketDetail.setMatnrRef(null);
//            weightTicketDetail.setUnit(null);
//        }
//        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
//                && grbType.getSelection() != null) {
//            rbtPO.setForeground(Color.black);
//            rbtMisc.setForeground(Color.black);
//            if (WeighBridgeApp.getApplication().isOfflineMode()) {
//                rbtMisc.setForeground(Color.red);
//            }
//            rbtMb1b.setForeground(Color.black);
//            rbtMvt311.setForeground(Color.black);
//        }
        setSaveNeeded(isValidated());
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task acceptScale() {
        String last = WeighBridgeApp.getApplication().getLast().toString();
        String now = WeighBridgeApp.getApplication().getNow().toString();
        if (!last.equals(now)) {
            JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.weighStability"));
            return null;
        }

        if (weightTicket == null || txfCurScale.getValue() == null || ((Number) txfCurScale.getValue()).intValue() == 0) {
            return null;
        }
        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
        if (!WeighBridgeApp.getApplication().isOfflineMode() && weightTicketDetail.getMatnrRef() == null) {
            return null;
        }
        if (!isStage1() && !isStage2()) {
            return null;
        }
        if ((txtPONo.getText() != null || !"".equals(txtPONo.getText())) && purOrder != null) {
            if (purOrder.getDocType().equals("UB")) {
                Material m = null;
                PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                try {
                    m = weightTicketController.checkPOSTO(purchaseOrderDetail.getMaterial());
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (m != null) {
                    if (m.getCheckPosto() == null) {
                        return new AcceptScaleTask(WeighBridgeApp.getApplication());
                    } else if (m.getCheckPosto() != null || !m.getCheckPosto().equals("")) {
                        if (!purchaseOrderDetail.getValType().equals("TRANSIT")) {
                            JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.Transit"));
                            return null;
                        }
                    }
                }
            }
        }
        return new AcceptScaleTask(WeighBridgeApp.getApplication());
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task readWT() {

        ximang = null;
        total_qty_goods = BigDecimal.ZERO;
        total_qty_free = BigDecimal.ZERO;
        WeighBridgeApp.getApplication().setNow(BigInteger.ZERO);
        WeighBridgeApp.getApplication().setLast(BigInteger.ZERO);
        WeighBridgeApp.getApplication().setMax(BigInteger.ZERO);
        outbDel_list.clear();
        outDetails_lits.clear();
        entityManager.clear();
        String txt = txtWTNum.getText().trim();
        if (isEnteredValidWTNum()) {
            return new ReadWTTask(WeighBridgeApp.getApplication(), txt);
        } else {
            clearForm();
            return null;
        }
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task readPO() {
        if (isEnteredValidPONum()) {
            return new ReadPOTask(WeighBridgeApp.getApplication(), txtPONo.getText().trim());
        } else {
            return null;
        }
    }

    @Action
    public Task acceptBatch() {
        if ((cbxCharg.getSelectedIndex() == -1 && !cbxCharg.isEditable()) || (cbxCharg.isEditable() && cbxCharg.getEditor().getItem().toString().trim().isEmpty())) {
            setSaveNeeded(isValidated());
            return null;
        }
        return new AcceptBatchTask(WeighBridgeApp.getApplication());
    }

    @Action(block = Task.BlockingScope.ACTION, enabledProperty = "reprintable")
    public Task reprintWT() {
        if (weightTicket == null) {
            return null;
        } else {
            if (!weightTicket.isDissolved()) { //+20100112#01 Phieu bi huy khong dc in lai
                return new ReprintWTTask(WeighBridgeApp.getApplication());
            } else { //+20100112#01 Phieu bi huy khong dc in lai
                JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.ticketDestroy"));
                return null;
            } //+20100112#01 Phieu bi huy khong dc in lai
        }
    }

    @Action(enabledProperty = "saveNeeded")
    public Task saveWT() throws Exception {
        String msg = "";

        boolean valid = isValidated();
        if (weightTicket != null) {
            if (!weightTicket.isPosted()) {
                valid = true;
            }
            //20121203 HOANGVV : check KL Dang Ky = KL Thuc te cho xi mang
            OutboundDelivery outdel_tmp = null;
            List<OutboundDeliveryDetail> details_list = new ArrayList<>();
            OutboundDeliveryDetail item = null;
            Material mat_tmp = null;
            Boolean ximang_tmp = false;
            Boolean flag_tmp = true;

            // Nhận dạng phiếu cân gỏ tay Hiệp Phước
            //   weightTicket.setText("ZBD"); 
            weightTicket.setText(txtGRText.getText().trim());

            for (int i = 0; i < outbDel_list.size(); i++) {
                outdel_tmp = outbDel_list.get(i);
                try {
                    mat_tmp = weightTicketController.checkPOSTO(outdel_tmp.getMatnr());

//                    ximang_tmp = mat_tmp.getBag();
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    details_list = weightTicketRegistarationController.findByMandtDelivNumb(outdel_tmp.getDeliveryOrderNo());
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int j = 0; j < details_list.size(); j++) {
                    item = details_list.get(j);
                    if (item.getLfimg().doubleValue() != item.getLfimg().doubleValue()) {
                        if (ximang_tmp) {
                            flag_tmp = false;
                        }
                    }
                }
            }
            if (!flag_tmp) {
                //    JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(),cmes.getMsg("3") );
                String Mes = resourceMapMsg.getString("msg.errorPointReceive");
                //    Mes = cmes.getMsg("3") ;
                JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), Mes.toString());
            }
            //end 20121203
        }
        if (valid) {
            //------------------------
            //Set require input ghi chu if process is GI Cement for POSTO.
            //Modified by Tuanna 08/04/2013
            //        boolean bGhichu = true  ; 
            String wplant = configuration.getWkPlant();
            try {
                //  msg = cmes.getMsg("2");
                msg = resourceMapMsg.getString("msg.errorNote");
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (outbDel != null) {

                try {

                    String sMvt = "";
                    sMvt = outbDel.getBwart();
                    if (sMvt.indexOf("641") >= 0
                            && wplant.indexOf("1311") >= 0
                            && outbDel.getMatnr() != null && outbDel.getMatnr().indexOf("1011304") >= 0) {
                        if (txtGRText.getText().length() <= 1 || txtGRText.getText() == null) {

                            //   String msgz = " Lỗi thiếu thông tin ghi chú !  ";
                            // setMessage(msg);
                            JOptionPane.showMessageDialog(rootPane, msg);
                            return null;
                        }
                    }
                } catch (NullPointerException e) {
                }

            }

            try {
                msg = resourceMapMsg.getString("msg.questionSaveTicket");
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer lv_return = JOptionPane.showConfirmDialog(WeighBridgeApp.getApplication().getMainFrame(), msg, resourceMapMsg.getString("msg.question"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (lv_return == JOptionPane.YES_OPTION) {
                //
                if (outbDel != null && outbDel.getMatnr() != null
                        //                        && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrPcb40())
                        && rbtOutward.isSelected() && isStage2()) {
                    //{+20101202#02 check material availability
                    //setMessage("Đang kiểm tra tồn kho trong SAP...");
                    //Double result = (Double) txfGoodsQty.getValue();
                    //Double remaining = CheckMatStock(weightTicket.getMatnrRef(), configuration.getWkPlant(), weightTicket.getLgort(), "");
                    Double result = (double) 1;
                    Double remaining = (double) 0;

                    if (result != null && remaining != null) {
                        if (result > remaining) {
                            //}+20101202#02 check material availability
                            //+{add logic check confirm
                            Variant vari = weightTicketController.findByParam(Constants.WeightTicketView.PROCESS_ORDER_CF);
                            String chkPROC1 = "";
                            if (vari != null) {
                                try {
                                    chkPROC1 = vari.getValue().toString();
                                } catch (Exception e) {
                                }
                            }
                            entityManager.clear();
                            //if (chkPROC1.equals("X")){ //20100214#01 huy phieu can
                            if (chkPROC1.equals("X")) { //20100214#01 huy phieu can
                                //+}add logic check confirm
                                //JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), "Không đủ tồn kho XM PCB40!!!");
                                WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                                ProcOrdView procoView = new ProcOrdView(WeighBridgeApp.getApplication().getMainFrame(), null);
                                procoView.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
                                WeighBridgeApp.getApplication().show(procoView);
//                                if (!procoView.isShowing()) {
//                                    if (procoView.getProcOrd() != null) {
//                                        weightTicketDetail.setPpProcord(procoView.getProcOrd());
//                                    } else {
//                                        weightTicketDetail.setPpProcord(null);
//                                    }
//                                    procoView.dispose();
//                                    procoView = null;
//                                    if (weightTicketDetail.getPpProcord() == null) {
//                                        JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.needProcessOrder"));
//                                        return null;
//                                    }
//                                }
                            }
                            //{+20101202#02 check material availability
                        }
                    }
                    //}+20101202#02 check material availability
                }
                return new SaveWTTask(WeighBridgeApp.getApplication());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void checkWTNum(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            boolean valid = text.matches("\\d{10}");  // 
            setEnteredValidWTNum(valid);
            if (valid) {
                lblWTNum.setForeground(Color.BLACK);
            } else {
                lblWTNum.setForeground(Color.RED);
            }
        } catch (BadLocationException ex) {
        } finally {
        }
    }

    private void checkPONum(DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            boolean valid = text.matches("\\d{10}");
            setEnteredValidPONum(valid);
            setValidPONum(false);
        } catch (BadLocationException ex) {
        } finally {
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Form's properties">
    private boolean stage1 = false;
    public static final String PROP_STAGE1 = Constants.WeightTicketView.PROP_STAGE1;
    private boolean stage2 = false;
    public static final String PROP_STAGE2 = Constants.WeightTicketView.PROP_STAGE2;
    private boolean saveNeeded = false;
    public static final String PROP_SAVENEEDED = Constants.WeightTicketView.PROP_SAVENEEDED;
    private boolean bridge1 = false;
    public static final String PROP_BRIDGE1 = Constants.WeightTicketView.PROP_BRIDGE1;
    private boolean bridge2 = false;
    public static final String PROP_BRIDGE2 = Constants.WeightTicketView.PROP_BRIDGE2;
    private boolean validPONum = false;
    public static final String PROP_VALIDPONUM = Constants.WeightTicketView.PROP_VALIDPONUM;
    private boolean dissolved = false;
    public static final String PROP_DISSOLVED = Constants.WeightTicketView.PROP_DISSOLVED;
    private boolean reprintable = false;
    public static final String PROP_REPRINTABLE = Constants.WeightTicketView.PROP_REPRINTABLE;
    private boolean enteredValidPONum = false;
    public static final String PROP_ENTEREDVALIDPONUM = Constants.WeightTicketView.PROP_ENTEREDVALIDPONUM;
    private boolean enteredValidWTNum = false;
    public static final String PROP_ENTEREDVALIDWTNUM = Constants.WeightTicketView.PROP_ENTEREDVALIDWTNUM;
    private boolean withoutDO = false;
    public static final String PROP_WITHOUTDO = Constants.WeightTicketView.PROP_WITHOUTDO;
    private boolean formEnable = false;
    public static final String PROP_FORMENABLE = Constants.WeightTicketView.PROP_FORMENABLE;
    private boolean subContract = false;
    public static final String PROP_SUBCONTRACT = Constants.WeightTicketView.PROP_SUBCONTRACT;
    private boolean materialAvailable = false;
    public static final String PROP_MATERIALAVAILABLE = Constants.WeightTicketView.PROP_MATERIALAVAILABLE;
    private Double matAvailStocks = null;
    public static final String PROP_MATAVAILSTOCKS = Constants.WeightTicketView.PROP_MATAVAILSTOCKS;
    private boolean mvt311 = false;
    public static final String PROP_MVT311 = Constants.WeightTicketView.PROP_MVT311;

    /**
     * Get the value of stage2
     *
     * @return the value of stage2
     */
    public boolean isStage2() {
        return stage2;
    }

    /**
     * Get the value of stage1
     *
     * @return the value of stage1
     */
    public boolean isStage1() {
        return stage1;
    }

    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    /**
     * Get the value of bridge1
     *
     * @return the value of bridge1
     */
    public boolean isBridge1() {
        return bridge1;
    }

    /**
     * Get the value of bridge2
     *
     * @return the value of bridge2
     */
    public boolean isBridge2() {
        return bridge2;
    }

    /**
     * Get the value of enteredValidWTNum
     *
     * @return the value of enteredValidWTNum
     */
    public boolean isEnteredValidWTNum() {
        return enteredValidWTNum;
    }

    /**
     * Get the value of validPONum
     *
     * @return the value of validPONum
     */
    public boolean isValidPONum() {
        return validPONum;
    }

    /**
     * Get the value of enteredValidPONum
     *
     * @return the value of enteredValidPONum
     */
    public boolean isEnteredValidPONum() {
        return enteredValidPONum;
    }

    /**
     * Get the value of withoutDO
     *
     * @return the value of withoutDO
     */
    public boolean isWithoutDO() {
        return withoutDO;
    }

    /**
     * Get the value of dissolved
     *
     * @return the value of dissolved
     */
    public boolean isDissolved() {
        return dissolved;
    }

    public boolean isReprintable() {
        return reprintable;
    }

    public boolean isFormEnable() {
        return formEnable;
    }

    public boolean isSubContract() {
        return subContract;
    }

    public boolean isMaterialAvailable() {
        return materialAvailable;
    }

    public Double getMatAvailStocks() {
        return matAvailStocks;
    }

    /**
     * Get the value of mvt311
     *
     * @return the value of mvt311
     */
    public boolean isMvt311() {
        return mvt311;
    }

    /**
     * Set the value of stage1
     *
     * @param stage1 new value of stage1
     */
    public void setStage1(boolean stage1) {
        boolean oldStage1 = this.stage1;
        this.stage1 = stage1;
        firePropertyChange(PROP_STAGE1, oldStage1, stage1);
    }

    /**
     * Set the value of stage2
     *
     * @param stage2 new value of stage2
     */
    public void setStage2(boolean stage2) {
        boolean oldStage2 = this.stage2;
        this.stage2 = stage2;
        firePropertyChange(PROP_STAGE2, oldStage2, stage2);
    }

    public void setSaveNeeded(boolean b) {
        boolean old = isSaveNeeded();
        this.saveNeeded = b;
        firePropertyChange(PROP_SAVENEEDED, old, isSaveNeeded());
    }

    /**
     * Set the value of bridge1
     *
     * @param bridge1 new value of bridge1
     */
    private void setBridge1(boolean bridge1) {
        boolean oldBridge1 = this.bridge1;
        this.bridge1 = bridge1;
        firePropertyChange(PROP_BRIDGE1, oldBridge1, bridge1);
    }

    /**
     * Set the value of bridge2
     *
     * @param bridge2 new value of bridge2
     */
    private void setBridge2(boolean bridge2) {
        boolean oldBridge2 = this.bridge2;
        this.bridge2 = bridge2;
        firePropertyChange(PROP_BRIDGE2, oldBridge2, bridge2);
    }

    /**
     * Set the value of enteredValidWTNum
     *
     * @param enteredValidWTNum new value of enteredValidWTNum
     */
    public void setEnteredValidWTNum(boolean enteredValidWTNum) {
        boolean oldEnteredValidWTNum = this.enteredValidWTNum;
        this.enteredValidWTNum = enteredValidWTNum;
        firePropertyChange(PROP_ENTEREDVALIDWTNUM, oldEnteredValidWTNum, enteredValidWTNum);
    }

    /**
     * Set the value of validPONum
     *
     * @param validPONum new value of validPONum
     */
    public void setValidPONum(boolean validPONum) {
        boolean oldValidPONum = this.validPONum;
        this.validPONum = validPONum;
        firePropertyChange(PROP_VALIDPONUM, oldValidPONum, validPONum);
    }

    /**
     * Set the value of enteredValidPONum
     *
     * @param enteredValidPONum new value of enteredValidPONum
     */
    public void setEnteredValidPONum(boolean enteredValidPONum) {
        boolean oldEnteredValidPONum = this.enteredValidPONum;
        this.enteredValidPONum = enteredValidPONum;
        firePropertyChange(PROP_ENTEREDVALIDPONUM, oldEnteredValidPONum, enteredValidPONum);
    }

    /**
     * Set the value of withoutDO
     *
     * @param withoutDO new value of withoutDO
     */
    public void setWithoutDO(boolean withoutDO) {
        boolean oldWithoutDO = this.withoutDO;
        this.withoutDO = withoutDO;
        firePropertyChange(PROP_WITHOUTDO, oldWithoutDO, withoutDO);
    }

    /**
     * Set the value of dissolved
     *
     * @param dissolved new value of dissolved
     */
    public void setDissolved(boolean dissolved) {
        boolean oldDissolved = this.dissolved;
        this.dissolved = dissolved;
        firePropertyChange(PROP_DISSOLVED, oldDissolved, dissolved);
    }

    public void setReprintable(boolean b) {
        boolean old = isReprintable();
        this.reprintable = b;
        firePropertyChange(PROP_REPRINTABLE, old, isReprintable());
    }

    public void setFormEnable(boolean formEnable) {
        boolean oldFormEnable = this.formEnable;
        this.formEnable = formEnable;
        firePropertyChange(PROP_FORMENABLE, oldFormEnable, formEnable);
    }

    public void setSubContract(boolean subContract) {
        boolean oldSubContract = this.subContract;
        this.subContract = subContract;
        firePropertyChange(PROP_SUBCONTRACT, oldSubContract, subContract);
    }

    public void setMaterialAvailable(boolean materialAvailable) {
        boolean oldMaterialAvailable = this.materialAvailable;
        this.materialAvailable = materialAvailable;
        firePropertyChange(PROP_MATERIALAVAILABLE, oldMaterialAvailable, materialAvailable);
    }

    public void setMatAvailStocks(Double matAvailStocks) {
        Double oldMatAvailStocks = this.matAvailStocks;
        this.matAvailStocks = matAvailStocks;
        firePropertyChange(PROP_MATAVAILSTOCKS, oldMatAvailStocks, matAvailStocks);
    }

    /**
     * Set the value of mvt311
     *
     * @param mvt311 new value of mvt311
     */
    public void setMvt311(boolean mvt311) {
        boolean oldMvt311 = this.mvt311;
        this.mvt311 = mvt311;
        firePropertyChange(PROP_MVT311, oldMvt311, mvt311);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Util. Classes/Methods">

    private DefaultComboBoxModel getMaterialList() {
        return weightTicketController.getMaterialList();
    }

    private class ReadWTTask extends Task<Object, Void> {

        int id;

        /*
        ReadWTTask(Application app, String id, String seq, String RegId ) {
        super(app);
        this.id = id;
        this.seq = Integer.valueOf(seq);
        setReprintable(false);
        grbType.clearSelection();
        grbCat.clearSelection();
        config = WeighBridgeApp.getApplication().getConfig();
        }
         */
        ReadWTTask(Application app, String id) {
            super(app);
            this.id = Integer.parseInt(id);
            setReprintable(false);
            grbType.clearSelection();
            grbCat.clearSelection();
            config = WeighBridgeApp.getApplication().getConfig();

        }

        @Override
        protected Object doInBackground() throws Exception {
            weightTicket = weightTicketController.findWeightTicket(weightTicket, id);
            if (weightTicket == null) {
                failed(new Exception(resourceMapMsg.getString("msg.notTicketNo", txtWTNum.getText())));
            } else {
                //show hide text and label
                switchShowHideMode(weightTicket.getMode());
                
                if (weightTicket.getRegType() == 'I') {
                    rbtInward.setSelected(true);
                } else {
                    rbtOutward.setSelected(true);
                }
                String Posto = "";
                txtCementDesc.setText(weightTicket.getSoNiemXa());
                
                if (txtCementDesc.getText() != null || !"".equals(txtCementDesc.getText())) {
                    txtCementDesc.setEditable(false);
                }
                if (weightTicket.getPosto() != null) {
                    txtPONo.setText(weightTicket.getPosto());
                }
                txtProcedure.setText(weightTicketController.getModeProcedure(weightTicket.getMode()));

                txtRegistrationNo.setText(weightTicket.getRegisteredNumber());
                //check mode quy trình show Sling and Pallet
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_ROAD.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_WATERWAY.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_OTHER.name().equals(weightTicket.getMode())) {
                    txtSling.setText(String.valueOf(weightTicket.getSling()));
                    txtPallet.setText(String.valueOf(weightTicket.getPallet()));
                }
                txtRemark.setText(weightTicket.getRemark());
                txtBatchProduce.setText(weightTicket.getBatch());
                if (txtBatchProduce.getText() != null || !"".equals(txtBatchProduce.getText())) {
                    txtBatchProduce.setEditable(false);
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_OTHER.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_PO_PURCHASE.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_WAREHOUSE_TRANSFER.name().equals(weightTicket.getMode())) {
                    txtTicketId.setText(weightTicket.getTicketId());
                }
                if (Constants.WeighingProcess.MODE_DETAIL.IN_WAREHOUSE_TRANSFER.name().equals(weightTicket.getMode())) {
                    txtWeightTicketIdRef.setText(String.valueOf(weightTicket.getWeightTicketIdRef()));
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_PO_PURCHASE.name().equals(weightTicket.getMode())) {
                    txtPONo.setText(weightTicket.getWeightTicketDetail().getEbeln());
                }
                txtWeight.setText(weightTicket.getWeightTicketDetail().getRegItemQuantity().toString());
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())) {
                    txtPoPosto.setText(weightTicket.getPosto());
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())) {
                    cbxVendorLoading.setSelectedItem(weightTicketRegistarationController.getVendor(weightTicket.getLoadVendor()));
                    cbxVendorTransport.setSelectedItem(weightTicketRegistarationController.getVendor(weightTicket.getTransVendor()));
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())) {
                    txtLgortIn.setText(weightTicket.getRecvLgort());
                    txtChargIn.setText(weightTicket.getRecvCharg());
                }
//                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_WATERWAY.name().equals(weightTicket.getMode())) {
//                    txtSO.setText(weightTicket.getSo());
//                }
                // <editor-fold defaultstate="collapsed" desc="Determine state of Weight Ticket">
                setStage1(false);
                setStage2(false);
                if (weightTicket.getFScale() == null && weightTicket.getSScale() == null) {
                    setStage1(true);
                } else if (weightTicket.getFScale() != null && weightTicket.getSScale() == null) {
                    setStage2(true);
                }
                OutboundDelivery od = null; //HLD18++
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Load D.O/P.O details">
                WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                if ((weightTicketDetail.getDeliveryOrderNo() == null || weightTicketDetail.getDeliveryOrderNo().trim().isEmpty())
                        || (!weightTicket.isPosted() && (weightTicketDetail.getEbeln() != null && !weightTicketDetail.getEbeln().trim().isEmpty()))
                        || (weightTicketDetail.getDeliveryOrderNo() == null && weightTicketDetail.getEbeln() == null)) {
                    setWithoutDO(true);
                } else {
                    // OutboundDelivery od = null; //HLD18--
                    List<OutboundDeliveryDetail> odt = null;
                    try {
                        od = weightTicketController.findByMandtOutDel(weightTicketDetail.getDeliveryOrderNo());
                    } catch (Exception ex) {
//                        java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (od == null && weightTicketDetail.getEbeln() != null) {
                        od = sapService.getOutboundDelivery(weightTicketDetail.getDeliveryOrderNo());
                        if (od != null) {
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            entityManager.persist(od);
                            entityManager.getTransaction().commit();
                        }
                        try {
                            odt = weightTicketRegistarationController.findByMandtDelivNumb(weightTicketDetail.getDeliveryOrderNo());
                        } catch (Exception ex) {
//                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if ((odt != null) && (odt.size() > 0)) {
                            OutboundDeliveryDetail item = odt.get(0);
//                          J-20062013 - add code to check double post GR
                            if (item.getMatDoc() != null
                                    && !item.getMatDoc().equals("")) {
                                setStage2(false);
                            }
                            item.setInScale(weightTicket.getFScale());
                            item.setOutScale(weightTicket.getSScale());
                            item.setGoodsQty(weightTicket.getGQty());
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            entityManager.merge(item);
                            entityManager.getTransaction().commit();
                        }
                    }
                    setWithoutDO(false);
                }
                if (WeighBridgeApp.getApplication().isOfflineMode()
                        && od == null //HLD18
                        ) {
                    setWithoutDO(true);
                }

                if (!isWithoutDO()) {
                    //xu ly nhieu DO trong WT
                    String[] do_list = weightTicketDetail.getDeliveryOrderNo().split("-");
                    List<OutboundDeliveryDetail> details_list = new ArrayList<>();
                    OutboundDeliveryDetail item = null;
                    outbDel_list.clear();
                    outDetails_lits.clear();
                    for (int i = 0; i < do_list.length; i++) {
                        try {
                            //outbDel = entityManager.find(OutboundDelivery.class, new OutbDelPK(config.getsClient(), do_list[i]));
                            outbDel = weightTicketController.findByMandtOutDel(do_list[i]);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!WeighBridgeApp.getApplication().isOfflineMode()) { //HLD18++offline
                            OutboundDelivery sapOutbDel = sapService.getOutboundDelivery(do_list[i]);
                            if (sapOutbDel != null && outbDel == null) {
                                if (!entityManager.getTransaction().isActive()) {
                                    entityManager.getTransaction().begin();
                                }
                                entityManager.persist(sapOutbDel);
                                entityManager.getTransaction().commit();
                                entityManager.clear();
                            } else if (sapOutbDel != null && outbDel != null) {
                                sapOutbDel.setId(outbDel.getId());
                                sapOutbDel.setPosted(outbDel.isPosted());
                                sapOutbDel.setMatDoc(outbDel.getMatDoc());
                                if (!entityManager.getTransaction().isActive()) {
                                    entityManager.getTransaction().begin();
                                }
                                entityManager.merge(sapOutbDel);
                                outbDel = sapOutbDel;
                                entityManager.getTransaction().commit();
                                entityManager.clear();
                            }
                        }//HLD18++
                        if (outbDel != null) {
                            outbDel_list.add(outbDel);
                            //lay total qty va qty_free

                            if (outbDel.getFreeQty() != null) {
                                if (isStage2()) {
                                    total_qty_free = total_qty_free.add(outbDel.getFreeQty());
                                }

                            }

                            if (outbDel.getLfimg() == null) {
                            } else {
                                if (isStage2()) {
                                    total_qty_goods = total_qty_goods.add(outbDel.getLfimg());
                                }

                            }
                            weightTicketDetail.setKunnr(null);
                            if ((weightTicketDetail.getKunnr() == null || weightTicketDetail.getKunnr().isEmpty())
                                    && outbDel.getKunnr() != null && !outbDel.getKunnr().isEmpty()) {
                                weightTicketDetail.setKunnr(outbDel.getKunnr());
                            }
                            weightTicketDetail.setRegItemDescription(outbDel.getArktx());
                            weightTicketDetail.setMatnrRef(outbDel.getMatnr());
                            weightTicketDetail.setItem(outbDel.getDeliveryItem());
                            weightTicketDetail.setUnit(outbDel.getVrkme());
                        }
                        //weightTicket.setUnit("TO");
                        try {
                            //get list outdetails
                            details_list = weightTicketRegistarationController.findByMandtDelivNumb(do_list[i]);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int j = 0; j < details_list.size(); j++) {
                            item = details_list.get(j);
                            outDetails_lits.add(item);
                        }
                    }
                    remain_qty_goods = total_qty_goods;
                }
                if (weightTicketDetail.getEbeln() != null && !weightTicketDetail.getEbeln().trim().isEmpty()) {

                    purOrder = weightTicketController.findByPoNumber(weightTicketDetail.getEbeln());
                    txtPONo.setText(weightTicketDetail.getEbeln());
                    setValidPONum(true);
                    setSubContract(false);
                    PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                    if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3' //                            && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())
                            ) {
                        setSubContract(true);
                    }
                } else {
                    if (Posto.equals("")) {
                        // Tuanna 2018 08015
                        txtPONo.setText(null);
                    }
                }
                if (weightTicket.getMoveType() != null && weightTicket.getMoveReas() != null
                        && weightTicket.getMoveType().equalsIgnoreCase("313")
                        && weightTicket.getMoveReas().equalsIgnoreCase("0003")) {
                    setSubContract(true);
                } else if (weightTicket.getMoveType() != null && weightTicket.getMoveType().equalsIgnoreCase("311")) {
                    setMvt311(true);
                }
//                if (weightTicket.getRegType() == 'I'
//                        && ((!isWithoutDO() && outbDel != null && !outbDel.getLfart().equalsIgnoreCase("LF")
//                        && !outbDel.getLfart().equalsIgnoreCase("LR")
//                        && !outbDel.getLfart().equalsIgnoreCase("ZTLF")
//                        && !outbDel.getLfart().equalsIgnoreCase("ZTLR")) || isWithoutDO())) {
//                    txtGRText.setEnabled(true);
//                    cbxCharg.setEditable(true);
//                } else {
////                    txtGRText.setEnabled(false);
//                    cbxCharg.setEditable(false);
//                    cbxSLoc.setEnabled(false);
//                    cbxCharg.setEnabled(false);
//                }
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Bind Weight Ticket">

                String smatref = "";
                try {
                    smatref = weightTicketDetail.getMatnrRef();
                } catch (ArithmeticException e) {
                }

                if (smatref != null) {
                    try {
                        //  txtMatnr.setText(weightTicket.getMatnrRef());
                        txtMatnr.setText("1234567890000");
                        txtMatnr.setText(smatref);
                        materialConstraint = weightTicketController.getMaterialConstraintByMatnr(smatref);
                        
                        if(isStage2() && materialConstraint != null && materialConstraint.getRequiredNiemXa()){
                            lblCementDesc.setBackground(Color.red);
                            txtCementDesc.setEditable(true);
                        }
                        if(isStage2() && materialConstraint != null && materialConstraint.getRequiredBatch()){
                            txtBatchProduce.setBackground(Color.red);
                            txtBatchProduce.setEditable(true);
                        }
                    } catch (ArithmeticException e) {
                    }
                }
                txtRegItem.setText(weightTicketDetail.getRegItemDescription());
                formatter.applyPattern(WeighBridgeApp.DATE_TIME_DISPLAY_FORMAT);
                if (weightTicket.getFScale() != null) {
                    txfInQty.setValue(weightTicket.getFScale());
                    txtInTime.setText(formatter.format(weightTicket.getFTime()));
                } else {
                    txfInQty.setValue(null);
                    txtInTime.setText(null);
                }
                if (weightTicket.getSScale() != null) {
                    txfOutQty.setValue(weightTicket.getSScale());
                    txtOutTime.setText(formatter.format(weightTicket.getSTime()));
//                    if (weightTicket.getGQty() != null) {
                    txfGoodsQty.setValue(weightTicket.getGQty());
//                    }
                } else {
                    txfOutQty.setValue(null);
                    txtOutTime.setText(null);
                    txfGoodsQty.setValue(null);
                }
                txtDName.setText(weightTicket.getDriverName());
                txtCMNDBL.setText(weightTicket.getDriverIdNo());
                txtLicPlate.setValue(weightTicket.getPlateNo());
                txtTrailerPlate.setValue(weightTicket.getTrailerId());

                //20120522_ setEnabled for "combo box Khach hang" depends on Offline(of Weight Ticket, radio Offline) and Posted status
                //Posted: -1-Post hong, 0-Chua Post, 1-Post vo SAP ok, 2-Post ok nhung Offline
                cbxKunnr.setSelectedItem(null);
                cbxKunnr.setSelectedIndex(-1);
                if (weightTicketDetail.getKunnr() != null && !weightTicketDetail.getKunnr().trim().isEmpty()) {
                    entityManager.clear();
                    Customer cust = weightTicketRegistarationController.findByKunnr(weightTicketDetail.getKunnr());
                    cbxKunnr.setSelectedItem(cust);
                }
                if ((WeighBridgeApp.getApplication().isOfflineMode()
                        && !weightTicket.isPosted())
                        || (!WeighBridgeApp.getApplication().isOfflineMode()
                        && !weightTicket.isPosted())) {
                    if (weightTicket.getRegType() == 'O' && weightTicketDetail.getEbeln() == null) {
                        cbxKunnr.setEnabled(true); // 2471
                    }
                } else {
                    // logic cho nay => no mac dinh offline khi usr can xuat clinker cho POSTO
                    //  cbxKunnr.setEnabled(false);
                }
                if (weightTicket.getLgort() != null && !weightTicket.getLgort().trim().isEmpty()) {
                    entityManager.clear();
                    SLoc sloc = weightTicketController.findByLgort(weightTicket.getLgort());
                    cbxSLoc.setSelectedItem(sloc);
                } else if (outbDel != null && outbDel.getLgort() != null && !outbDel.getLgort().trim().isEmpty()) {
                    SLoc sloc = weightTicketController.findByLgort(outbDel.getLgort());
                    cbxSLoc.setSelectedItem(sloc);
                } else {
                    cbxSLoc.setSelectedIndex(-1);
                }
                txtGRText.setText(weightTicket.getText());
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_ROAD.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_WAREHOUSE_TRANSFER.name().equals(weightTicket.getMode())) {
                    txtDelNum.setText(weightTicketDetail.getDeliveryOrderNo());
                }
//                setDissolved(chkDissolved.isSelected());
                if (isDissolved() || (!isStage1() && !isStage2() && weightTicket.isPosted())) {
                    setValidPONum(false);
                    setWithoutDO(false);
//                    txtGRText.setEnabled(false);
                    cbxCharg.setEditable(false);
                    setStage1(false);
                    setStage2(false);
                    setFormEnable(false);
                } else {

                    if (!isWithoutDO()
                            && (outbDel != null && outbDel.getLfart() != null
                            && (outbDel.getLfart().equalsIgnoreCase("LF")
                            || outbDel.getLfart().equalsIgnoreCase("LR")
                            || outbDel.getLfart().equalsIgnoreCase("ZTLF")
                            || outbDel.getLfart().equalsIgnoreCase("ZTLR")))) {
                        setFormEnable(false);
                    } else {
                        setFormEnable(true);
                    }

                }

                if (isDissolved() || (!isStage1() && isStage2()) || (!isStage1() && !isStage2() && weightTicket.isPosted())) {
                    setReprintable(true);
                }
                // <editor-fold defaultstate="collapsed" desc="bind batch">
                if (cbxSLoc.getSelectedIndex() > -1
                        && ((weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty())
                        || (!isWithoutDO() && outbDel != null && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()))
                        && weightTicketDetail.getMatnrRef() != null && !weightTicketDetail.getMatnrRef().trim().isEmpty()) {
                    String lgort = ((SLoc) cbxSLoc.getSelectedItem()).getLgort();
                    BatchStock batch = null;
                    if (weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty()) {
                        batch = weightTicketController.findByWerksLgortMatnrCharg(configuration.getWkPlant(), lgort, weightTicketDetail.getMatnrRef(), weightTicket.getCharg());
                    } else if (!isWithoutDO() && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()) {
                        batch = weightTicketController.findByWerksLgortMatnrCharg(configuration.getWkPlant(), lgort, weightTicketDetail.getMatnrRef(), outbDel.getCharg());
                    }
                    if (cbxCharg.getModel().getSize() == 0) {
                        // sync data
                        //sapService.syncBatchStocks(lgort, weightTicket.getMatnrRef(), weightTicket.getLgort());
                        weightTicketController.getSyncBatchStocks((SLoc) cbxSLoc.getSelectedItem(), weightTicket);
                        // get data DB
                        List<BatchStock> batchs = weightTicketController.getBatchStocks((SLoc) cbxSLoc.getSelectedItem(), weightTicket);
                        DefaultComboBoxModel result = new DefaultComboBoxModel();
                        for (BatchStock b : batchs) {
                            if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                                result.addElement(b.getCharg());
                            }
                        }
                        cbxCharg.setModel(result);
                    }
                    if (batch != null) {
                        cbxCharg.setSelectedItem(batch.getCharg());
                    } else if (weightTicket.getCharg() != null && cbxCharg.isEditable()) {
                        cbxCharg.setSelectedItem(weightTicket.getCharg());
                    } else {
                        cbxCharg.setSelectedIndex(-1);
                    }
                }
                // cấu hình cho cầu cân hiển thị PO và vendor
//                    if (sapSetting.getCheckPov() != null && sapSetting.getCheckPov() == true) {
//                        txtPoPosto.setVisible(true);
//                        cbxVendorLoading.setVisible(true);
//                        cbxVendorTransport.setVisible(true);
//                        lblPoPosto.setVisible(true);
//                        lblVendorLoading.setVisible(true);
//                        lblVendorTransport.setVisible(true);
//                    } else {
//                        txtPoPosto.setVisible(false);
//                        cbxVendorLoading.setVisible(false);
//                        cbxVendorTransport.setVisible(false);
//                        lblPoPosto.setVisible(false);
//                        lblVendorLoading.setVisible(false);
//                        lblVendorTransport.setVisible(false);
//                    }
                // </editor-fold>
                // </editor-fold>
            }
            return null;  // return your result
        }

        @Override
        protected void failed(Throwable cause) {
            if (cause instanceof SapException) {
                for (SapException.SapError error : ((SapException) cause).getErrors()) {
                    logger.error(null, new Exception(error.toString()));
                    String transformedMsg = SAPErrorTransform.getMessage(error);
                    if (transformedMsg == null) {
                        JOptionPane.showMessageDialog(rootPane, error.getMessage());
                    } else {
                        JOptionPane.showMessageDialog(rootPane, transformedMsg);
                    }
                }
            } else {
                if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                    cause = cause.getCause();
                }
                logger.error(null, cause);
                JOptionPane.showMessageDialog(rootPane,
                        (cause.getMessage() == null || cause.getMessage().isEmpty())
                        ? "Null Pointer Exception" : cause.getMessage());
            }
            clearForm();
        }

        @Override
        protected void finished() {
            setSaveNeeded(isValidated());
            if (weightTicket != null) {
                if (weightTicket.isDissolved()) {
                    setSaveNeeded(false);
                }
                if (!weightTicket.isPosted()) {
                    btnPostAgain.setEnabled(true);
                } else {
                    btnPostAgain.setEnabled(false);
                }
            }
        }
    }

    private void switchShowHideMode(String mode) {
        setAllChildPanelsVisible(pnWTLeft);
        setAllChildPanelsVisible(pnWTRight);
        btnPostAgain.setEnabled(false);
        btnReprint.setEnabled(false);
        btnSave.setEnabled(false);
        if (ModeEnum.IN_PO_PURCHASE.name().equals(mode)) {
            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblRegistrationNo.setVisible(false);
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblDelNum.setVisible(false);
            //lblWeight.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtRegistrationNo.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtDelNum.setVisible(false);
            //txtWeight.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.IN_WAREHOUSE_TRANSFER.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.IN_OTHER.name().equals(mode)) {
            lblDelNum.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtDelNum.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.OUT_SELL_ROAD.name().equals(mode)) {
            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.OUT_PLANT_PLANT.name().equals(mode)) {
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            //lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
//            lblVendorLoading.setVisible(false);
//            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            //cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
//            cbxVendorLoading.setVisible(false);
//            cbxVendorTransport.setVisible(false);

        }
        if (ModeEnum.OUT_SLOC_SLOC.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            //ma vat tu
        }
        if (ModeEnum.OUT_PULL_STATION.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.OUT_SELL_WATERWAY.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            //so SO
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);

            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            //so SO
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
        }
        if (ModeEnum.OUT_OTHER.name().equals(mode)) {
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lbKunnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            
            txtSO.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            cbxKunnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            cbxVendorLoading.setVisible(false);
            cbxVendorTransport.setVisible(false);
            
        }
    }

    private class ReadPOTask extends Task<Object, Void> {

        String poNum;
        PurchaseOrder sapPurOrder = null;
        Vendor vendor = null;
        Vendor supVendor = null;
        Customer customer = null;
        Vendor sapVendor = null;
        Vendor sapSupVendor = null;
        Customer sapCustomer = null;

        ReadPOTask(Application app, String poNum) {
            super(app);
            this.poNum = poNum;
            config = WeighBridgeApp.getApplication().getConfig();
        }

        @Override
        protected Object doInBackground() {
            setMessage(resourceMapMsg.getString("msg.getDataPO"));
            setProgress(0, 0, 3);
            // Tuanna >> 14.06.13 
            cbxKunnr.setSelectedIndex(-1);
            cbxKunnr.setEnabled(false);

            // << end comment.
            purOrder = weightTicketController.findPurOrder(poNum);
            setMessage(resourceMapMsg.getString("msg.searchDataPo"));
            setProgress(1, 0, 3);

            try {
                sapPurOrder = sapService.getPurchaseOrder(poNum);
            } catch (Exception ex) {
                failed(ex);
            }
            if (sapPurOrder != null) {
                if (sapPurOrder.getVendor() != null && !sapPurOrder.getVendor().trim().isEmpty()) {
                    vendor = weightTicketRegistarationController.findByLifnr(sapPurOrder.getVendor());
                    sapVendor = sapService.getVendor(sapPurOrder.getVendor());
                }
                if (sapPurOrder.getSupplVend() != null && !sapPurOrder.getSupplVend().trim().isEmpty()) {
                    supVendor = weightTicketRegistarationController.findByLifnr(sapPurOrder.getSupplVend());
                    sapSupVendor = sapService.getVendor(sapPurOrder.getSupplVend());
                }
                if (sapPurOrder.getCustomer() != null && !sapPurOrder.getCustomer().trim().isEmpty()) {
                    customer = weightTicketRegistarationController.findByKunnr(sapPurOrder.getCustomer());
                    sapCustomer = sapService.getCustomer(sapPurOrder.getCustomer());
                }
            }

            setMessage(resourceMapMsg.getString("msg.saveData"));
            setProgress(2, 0, 3);
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            //Store Ship to party Info
            if (sapVendor != null && vendor == null) {
                entityManager.persist(sapVendor);
            } else if (sapVendor != null && vendor != null) {
                sapVendor.setId(vendor.getId());
                entityManager.merge(sapVendor);
            } else if (sapVendor == null && vendor != null) {
                entityManager.remove(vendor);
            }
            //Store Sold to party Info
            if (sapSupVendor != null && supVendor == null && !sapPurOrder.getVendor().equalsIgnoreCase(sapPurOrder.getSupplVend())) {
                entityManager.persist(sapSupVendor);
            } else if (sapSupVendor != null && supVendor != null) {
                sapSupVendor.setId(supVendor.getId());
                entityManager.merge(sapSupVendor);
            } else if (sapSupVendor == null && supVendor != null && !sapPurOrder.getVendor().equalsIgnoreCase(sapPurOrder.getSupplVend())) {
                entityManager.remove(supVendor);
            }
            //Store Vendor Info
            if (sapCustomer != null && customer == null) {
                entityManager.persist(sapCustomer);
            } else if (sapCustomer != null && customer != null) {
                sapCustomer.setId(customer.getId());
                entityManager.merge(sapCustomer);
            } else if (sapCustomer == null && customer != null) {
                entityManager.remove(customer);
            }
            if (sapPurOrder != null && purOrder == null) {
                entityManager.persist(sapPurOrder);
            } else if (sapPurOrder != null && purOrder != null) {
                sapPurOrder.setId(purOrder.getId());
                entityManager.merge(sapPurOrder);
            } else if (sapPurOrder == null && purOrder != null) {
                entityManager.remove(purOrder);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
            if (sapPurOrder != null) {
                purOrder = weightTicketController.findByPoNumber(sapPurOrder.getPoNumber());
                entityManager.refresh(purOrder);
                entityManager.clear();
                setValidPONum(true);
            } else {
                purOrder = null;
                setValidPONum(false);
            }
            if (isValidPONum()) {
                setSubContract(false);
                PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                if ((rbtInward.isSelected() && purchaseOrderDetail.getPlant().equalsIgnoreCase(configuration.getWkPlant()))
                        || (rbtOutward.isSelected() && purOrder.getSupplPlnt().equalsIgnoreCase(configuration.getWkPlant()))) {
                    setValidPONum(true);
                } else if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3' //                        && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())
                        ) {
//                    purOrder.setMaterial(setting.getMatnrClinker());
                    purchaseOrderDetail.setShortText(Constants.WeightTicketView.ITEM_DESCRIPTION);
                    purchaseOrderDetail.setPoUnit("TO");
                    setValidPONum(true);
                    setSubContract(true);
                } else {
                    setValidPONum(true);
                }
            }
            return null;  // return your result
        }

        @Override
        protected void failed(Throwable cause) {
            setValidPONum(false);
            if (cause instanceof SapException) {
                for (SapException.SapError error : ((SapException) cause).getErrors()) {
                    logger.error(null, new Exception(error.toString()));
                    String transformedMsg = SAPErrorTransform.getMessage(error);
                    if (transformedMsg == null) {
                        JOptionPane.showMessageDialog(rootPane, error.getMessage());
                    } else {
                        JOptionPane.showMessageDialog(rootPane, transformedMsg);
                    }
                }
            } else {
                if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                    cause = cause.getCause();
                }
                logger.error(null, cause);
                JOptionPane.showMessageDialog(rootPane, cause.getMessage());
            }
        }

        @Override
        protected void finished() {
            setProgress(3, 0, 3);
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            if (isValidPONum()) {
                PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                setSaveNeeded(isValidated());
                txtRegItem.setText(purchaseOrderDetail.getShortText());
                txtMatnr.setText(purchaseOrderDetail.getMaterial());
//                weightTicketDetail.setEbeln(purOrder.getPoNumber());
//                weightTicketDetail.setItem(purchaseOrderDetail.getPoItem());
//                weightTicketDetail.setRegItemDescription(purchaseOrderDetail.getShortText());
//                weightTicketDetail.setMatnrRef(purchaseOrderDetail.getMaterial());
//                weightTicketDetail.setUnit("TON");
            } else {
                txtRegItem.setText(null);
                txtMatnr.setText(null);
                weightTicketDetail.setEbeln(null);
                weightTicketDetail.setItem(null);
                weightTicketDetail.setMatnrRef(null);
                weightTicketDetail.setUnit(null);
            }
        }
    }

    private class ReprintWTTask extends Task<Object, Void> {

        ReprintWTTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            printWT(weightTicket, true);
            return null;  // return your result
        }
    }

    private class SaveWTTask extends Task<Object, Void> {

        Session sapSession = null;
        Object objBapi = null;
        Object objBapi_Po = null;
        Object objBapi_Posto = null;
        boolean completed = true;
        String bapi_message = "";

        SaveWTTask(org.jdesktop.application.Application app) {
            super(app);
            btnSave.setEnabled(false);
            if (((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
                    || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
                    && (weightTicket != null && !weightTicket.isPosted()))) {
                sapSession = WeighBridgeApp.getApplication().getSAPSession();
            }
            setSaveNeeded(false);
        }

        @Override
        protected Object doInBackground() {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Date now = new Date();
            weightTicket.setUpdatedDate(now);
            weightTicket.getWeightTicketDetail().setUpdatedDate(new java.sql.Date(now.getTime()));
            entityManager.merge(weightTicket);
            OutboundDelivery outbDel = null;
            List<String> completedDO = new ArrayList<>();
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            if (((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
                        || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
                        && (weightTicket != null && !weightTicket.isPosted()))) {
                if(weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                    objBapi = getGiMB1BBapi(weightTicket);
                    objBapi_Po = getGrPoMigoBapi(weightTicket, weightTicket.getWeightTicketDetail().getEbeln());
                    objBapi_Posto = getGrPoMigoBapi(weightTicket, weightTicket.getPosto());
                }

                if(weightTicket.getMode().equals("OUT_PULL_STATION") && weightTicket.getPosto() != null) {          
                    objBapi = getMvtPOSTOCreatePGI(weightTicket, weightTicket.getPosto());
                }

                // post SAP
                if (WeighBridgeApp.getApplication().isOfflineMode() == false) {
                        if (objBapi != null) {
                            try {
                                sapSession.execute(objBapi);
                                if(objBapi_Po != null) {
                                    sapSession.execute(objBapi_Po);
                                }
                                if(objBapi_Posto != null) {
                                    sapSession.execute(objBapi_Posto);
                                }
                                if (objBapi instanceof DOCreate2PGIBapi) {
                                    weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
                                    weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage();
                                    } catch (Exception Ex) {
                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2880");
                                    }
                                }
                                if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage();
                                    } catch (Exception Ex) {
                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2889");
                                    }
                                }
                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));

                                    try {
                                        bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage();
                                    } catch (Exception Ex) {
                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2899");
                                    }
                                }
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));
//                                    if (weightTicketDetail.getPpProcord() != null && weightTicketDetail.getPpProcord().length() == 12) {
//                                        weightTicketDetail.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
//                                        weightTicketDetail.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
//                                    }

                                    try {
                                        bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2944");
                                    }

                                }

                                if (objBapi_Po instanceof GoodsMvtPoCreateBapi) {
                                    weightTicketDetail.setMatDocGi(((GoodsMvtPoCreateBapi) objBapi_Po).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi_Po).getMatYear()));
                                    try {
                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi_Po).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP ( GoodsMvtPoCreateBapi line >>2889 ) ";
                                    }
                                }

                                if (objBapi instanceof GoodsMvtPOSTOCreatePGIBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMatYear()));

                                    try {
                                        bapi_message = ((GoodsMvtPOSTOCreatePGIBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = " No message returned when call SAP ( GoodsMvtPOSTOCreatePGIBapi line 2899 )";
                                    }
                                }

                                if (objBapi_Posto instanceof GoodsMvtPoCreateBapi) {
                                    weightTicketDetail.setMatDocGi(((GoodsMvtPoCreateBapi) objBapi_Posto).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi_Posto).getMatYear()));
                                    try {
                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi_Posto).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP ( GoodsMvtPoCreateBapi line >>2889 ) ";
                                    }
                                }
                                
                                if (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDocGi()== null || weightTicketDetail.getMatDoc().equals("")) {
                                    revertCompletedDO(completedDO, null, null);
                                    weightTicket.setPosted(false);
                                    if (bapi_message == "") {
                                        bapi_message = resourceMapMsg.getString("msg.errorBAPI");
                                    }
                                    JOptionPane.showMessageDialog(rootPane, bapi_message);
                                    completed = false;
                                    entityManager.clear();
                                } else if ((weightTicketDetail.getMatDoc() != null) && (!weightTicketDetail.getMatDoc().equals(""))
                                        && (weightTicketDetail.getMatDocGi() != null) && (!weightTicketDetail.getMatDocGi().equals(""))) {
                                    weightTicket.setPosted(true);
                                    completedDO.add(weightTicketDetail.getDeliveryOrderNo());
                                }

                                // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
                                if (outbDel != null) {
                                    OutboundDelivery sapOutb = sapService.getOutboundDelivery(outbDel.getDeliveryOrderNo());
                                    Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                    Vendor lifnr = null, sapLifnr = null;
                                    if (sapOutb != null) {
                                        if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                            kunnr = weightTicketRegistarationController.findByKunnr(sapOutb.getKunnr());
                                            sapKunnr = sapService.getCustomer(sapOutb.getKunnr());
                                        }
                                        if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                            kunag = weightTicketRegistarationController.findByKunnr(sapOutb.getKunag());
                                            sapKunag = sapService.getCustomer(sapOutb.getKunag());
                                        }
                                        if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                            lifnr = weightTicketRegistarationController.findByLifnr(sapOutb.getLifnr());
                                            sapLifnr = sapService.getVendor(sapOutb.getLifnr());
                                        }
                                    }
                                    //Store Ship to party Info
                                    if (sapKunnr != null && kunnr == null) {
                                        entityManager.persist(sapKunnr);
                                    } else if (sapKunnr != null && kunnr != null) {
                                        sapKunnr.setId(kunnr.getId());
                                        entityManager.merge(sapKunnr);
                                    } else if (sapKunnr == null && kunnr != null) {
                                        entityManager.remove(kunnr);
                                    }
                                    //Store Sold to party Info
                                    if (sapKunag != null && kunag == null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                        entityManager.persist(sapKunag);
                                    } else if (sapKunag != null && kunag != null) {
                                        sapKunag.setId(kunag.getId());
                                        entityManager.merge(sapKunag);
                                    } else if (sapKunag == null && kunag != null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                        entityManager.remove(kunag);
                                    }
                                    //Store Vendor Info
                                    if (sapLifnr != null && lifnr == null) {
                                        entityManager.persist(sapLifnr);
                                    } else if (sapLifnr != null && lifnr != null) {
                                        sapLifnr.setId(lifnr.getId());
                                        entityManager.merge(sapLifnr);
                                    } else if (sapLifnr == null && lifnr != null) {
                                        entityManager.remove(lifnr);
                                    }
                                    sapOutb.setId(outbDel.getId());
                                    entityManager.merge(sapOutb);
                                    outbDel = sapOutb;
                                }
                                // </editor-fold>

                            } catch (Exception ex) {
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    if (((WsDeliveryUpdateBapi) objBapi).getReturn() != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)) != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId() != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId().equals("NOREVERT")) {
                                    } else {
                                        revertCompletedDO(completedDO, null, null);
                                    }
                                } else {
                                    revertCompletedDO(completedDO, null, null);
                                }

                                weightTicket.setPosted(false);
                                failed(ex);
                                completed = false;
                                entityManager.clear();
                            }
                        } else if (WeighBridgeApp.getApplication().isOfflineMode() || objBapi == null) {
                            weightTicket.setPosted(true);
                            weightTicketDetail.setUnit("TON");
                        }
                    } else {
                        bapi_message = resourceMapMsg.getString("msg.postOfflien");
                        weightTicket.setPosted(true);
                        weightTicketDetail.setUnit("TON");
                    }
            }
            
            ////////////////
            if ((txtPONo.getText() != null || !"".equals(txtPONo.getText()))) {
//                if (((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
//                        || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
//                        && (weightTicket != null && !weightTicket.isPosted()))) {
//                    if (rbtInward.isSelected()) {
//                        if (weightTicketDetail.getDeliveryOrderNo() == null) {
//                            //objBapi = getGrPoMigoBapi(weightTicket);
//                        } else if (outbDel != null && outbDel.getLfart().equalsIgnoreCase("LR") && outbDel.getLfart().equalsIgnoreCase("ZTLR")) {
//                            objBapi = getPgmVl02nBapi(weightTicket, outbDel);
//                        } else {
//                            objBapi = getGrDoMigoBapi(weightTicket, outbDel);
//                        }
//                    } else {
//                        // set 
//
//                        if (!rbtMisc.isSelected()) {
//                            if (isSubContract() || isMvt311()) {
//                                if (rbtPO.isSelected()) {
//                                    objBapi = getGi541MigoBapi(weightTicket);
//                                } else {
//                                    objBapi = getGiMB1BBapi(weightTicket);
//                                }
//                            } else if (weightTicketDetail.getDeliveryOrderNo() != null) {
//                                // tuanna 16.06.13
//                                //  cbxKunnr.setSelectedIndex(-1);
//                                objBapi = getPgmVl02nBapi(weightTicket, outbDel);
//
//                            } else {
//                                objBapi = getDoCreate2PGI(weightTicket, outbDel);
//                            }
//                        }
//                    }
//                    if (WeighBridgeApp.getApplication().isOfflineMode() == false) {
//                        if (objBapi != null) {
//                            try {
//                                sapSession.execute(objBapi);
//                                if (objBapi instanceof DOCreate2PGIBapi) {
//                                    weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
//                                    weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
//                                    weightTicketDetail.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
//                                    try {
//                                        bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage();
//                                    } catch (Exception Ex) {
//                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2880");
//                                    }
//                                }
//                                if (objBapi instanceof GoodsMvtPoCreateBapi) {
//                                    weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
//                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
//                                    try {
//                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage();
//                                    } catch (Exception Ex) {
//                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2889");
//                                    }
//                                }
//                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
//                                    weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
//                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));
//
//                                    try {
//                                        bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage();
//                                    } catch (Exception Ex) {
//                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2899");
//                                    }
//                                }
//                                if (objBapi instanceof WsDeliveryUpdateBapi) {
//                                    weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
//                                    weightTicketDetail.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));
////                                    if (weightTicketDetail.getPpProcord() != null && weightTicketDetail.getPpProcord().length() == 12) {
////                                        weightTicketDetail.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
////                                        weightTicketDetail.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
////                                    }
//
//                                    try {
//                                        bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
//                                    } catch (Exception Ex) {
//                                        bapi_message = resourceMapMsg.getString("msg.errorSAP2944");
//                                    }
//
//                                }
//                                if (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")) {
//                                    revertCompletedDO(completedDO, null, null);
//                                    weightTicket.setPosted(false);
//                                    if (bapi_message == "") {
//                                        bapi_message = resourceMapMsg.getString("msg.errorBAPI");
//                                    }
//                                    JOptionPane.showMessageDialog(rootPane, bapi_message);
//                                    completed = false;
//                                    entityManager.clear();
//                                } else if ((weightTicketDetail.getMatDoc() != null) && (!weightTicketDetail.getMatDoc().equals(""))) {
//                                    weightTicket.setPosted(true);
//                                    completedDO.add(weightTicketDetail.getDeliveryOrderNo());
//                                }
//
//                                // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
//                                if (outbDel != null) {
//                                    OutboundDelivery sapOutb = sapService.getOutboundDelivery(outbDel.getDeliveryOrderNo());
//                                    Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
//                                    Vendor lifnr = null, sapLifnr = null;
//                                    if (sapOutb != null) {
//                                        if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
//                                            kunnr = weightTicketRegistarationController.findByKunnr(sapOutb.getKunnr());
//                                            sapKunnr = sapService.getCustomer(sapOutb.getKunnr());
//                                        }
//                                        if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
//                                            kunag = weightTicketRegistarationController.findByKunnr(sapOutb.getKunag());
//                                            sapKunag = sapService.getCustomer(sapOutb.getKunag());
//                                        }
//                                        if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
//                                            lifnr = weightTicketRegistarationController.findByLifnr(sapOutb.getLifnr());
//                                            sapLifnr = sapService.getVendor(sapOutb.getLifnr());
//                                        }
//                                    }
//                                    //Store Ship to party Info
//                                    if (sapKunnr != null && kunnr == null) {
//                                        entityManager.persist(sapKunnr);
//                                    } else if (sapKunnr != null && kunnr != null) {
//                                        sapKunnr.setId(kunnr.getId());
//                                        entityManager.merge(sapKunnr);
//                                    } else if (sapKunnr == null && kunnr != null) {
//                                        entityManager.remove(kunnr);
//                                    }
//                                    //Store Sold to party Info
//                                    if (sapKunag != null && kunag == null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
//                                        entityManager.persist(sapKunag);
//                                    } else if (sapKunag != null && kunag != null) {
//                                        sapKunag.setId(kunag.getId());
//                                        entityManager.merge(sapKunag);
//                                    } else if (sapKunag == null && kunag != null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
//                                        entityManager.remove(kunag);
//                                    }
//                                    //Store Vendor Info
//                                    if (sapLifnr != null && lifnr == null) {
//                                        entityManager.persist(sapLifnr);
//                                    } else if (sapLifnr != null && lifnr != null) {
//                                        sapLifnr.setId(lifnr.getId());
//                                        entityManager.merge(sapLifnr);
//                                    } else if (sapLifnr == null && lifnr != null) {
//                                        entityManager.remove(lifnr);
//                                    }
//                                    sapOutb.setId(outbDel.getId());
//                                    entityManager.merge(sapOutb);
//                                    outbDel = sapOutb;
//                                }
//                                // </editor-fold>
//
//                            } catch (Exception ex) {
//                                if (objBapi instanceof WsDeliveryUpdateBapi) {
//                                    if (((WsDeliveryUpdateBapi) objBapi).getReturn() != null
//                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)) != null
//                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId() != null
//                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId().equals("NOREVERT")) {
//                                    } else {
//                                        revertCompletedDO(completedDO, null, null);
//                                    }
//                                } else {
//                                    revertCompletedDO(completedDO, null, null);
//                                }
//
//                                weightTicket.setPosted(false);
//                                failed(ex);
//                                completed = false;
//                                entityManager.clear();
//                            }
//                        } else if (rbtMisc.isSelected() || objBapi == null) {
//                            weightTicket.setPosted(true);
//                            weightTicketDetail.setUnit("TON");
//                        }
//                    } else {
//                        bapi_message = resourceMapMsg.getString("msg.postOfflien");
//                        weightTicket.setPosted(true);
//                        weightTicketDetail.setUnit("TON");
//                    }
//                }
                /*} else if (weightTicket.getDelivNumb() != null || !weightTicket.getDelivNumb().equals("")) {*/
                // 20120521: update check PO-STO.
            } else {
                for (int i = 0; i < outbDel_list.size(); i++) {
                    outbDel = outbDel_list.get(i);
                    if (((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
                            || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
                            && (weightTicket != null && !weightTicket.isPosted()))) {
                        if (rbtInward.isSelected()) {
                            if (weightTicketDetail.getDeliveryOrderNo() == null) {
                                //objBapi = getGrPoMigoBapi(weightTicket);
                            } else if (outbDel != null && outbDel.getLfart().equalsIgnoreCase("LR") && outbDel.getLfart().equalsIgnoreCase("ZTLR")) {
                                objBapi = getPgmVl02nBapi(weightTicket, outbDel);
                            } else {
                                objBapi = getGrDoMigoBapi(weightTicket, outbDel);
                            }
                        } else {
                            if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                                if (isSubContract() || isMvt311()) {
                                    if ((txtPONo.getText() != null || !"".equals(txtPONo.getText()))) {
                                        objBapi = getGi541MigoBapi(weightTicket);
                                    } else {
                                        objBapi = getGiMB1BBapi(weightTicket);
                                    }
                                } else if (weightTicketDetail.getDeliveryOrderNo() != null) {
                                    objBapi = getPgmVl02nBapi(weightTicket, outbDel);
                                } else {
                                    objBapi = getDoCreate2PGI(weightTicket, outbDel);
                                }
                            }
                        }
                        if (WeighBridgeApp.getApplication().isOfflineMode() == false) {
                            if (objBapi != null) {
                                try {
//                                  J-20062013 - move into "if statement"
//                                  to check post double GR
                                    sapSession.execute(objBapi);

                                    OutboundDeliveryDetail details_item = null;

                                    if (objBapi instanceof DOCreate2PGIBapi) {
                                        weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
                                        weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                        weightTicketDetail.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                        try {
                                            bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = resourceMapMsg.getString("msg.errorSAP3048");
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            //if (details_item.getDelivNumb().equals(outbDel.getDeliveryOrderNo())) {
                                            if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                                details_item.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((DOCreate2PGIBapi) objBapi).getDocYear());
                                                outbDel.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                            }
                                            if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted(false);
                                                outbDel.setPosted(false);
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted(true);
                                                outbDel.setPosted(true);
                                            }

                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted(false);
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted(true);
                                        }
                                    }
                                    if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                        weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                        weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                        try {
                                            bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = resourceMapMsg.getString("msg.errorSAP3086");
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                                details_item.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((GoodsMvtPoCreateBapi) objBapi).getMatYear());
                                                outbDel.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                            }
                                            if (((GoodsMvtPoCreateBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted(false);
                                                outbDel.setPosted(false);
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted(true);
                                                outbDel.setPosted(true);
                                            }
                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((GoodsMvtPoCreateBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted(false);

                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted(true);
                                        }
                                    }
                                    if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                        weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                        weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));
                                        try {
                                            bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = resourceMapMsg.getString("msg.errorSAP3123");
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                                details_item.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((GoodsMvtDoCreateBapi) objBapi).getMatYear());
                                                outbDel.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                            }
                                            if (((GoodsMvtDoCreateBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted(false);
                                                outbDel.setPosted(false);
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted(true);
                                                outbDel.setPosted(true);
                                            }
                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((GoodsMvtDoCreateBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted(false);
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted(true);
                                        }
                                    }
                                    if (objBapi instanceof WsDeliveryUpdateBapi) {
                                        weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                        weightTicketDetail.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));

                                        try {
                                            bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = resourceMapMsg.getString("msg.errorSAP3160");
                                        }
//                                        if (weightTicketDetail.getPpProcord() != null && weightTicketDetail.getPpProcord().length() == 12) {
//                                            weightTicketDetail.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
//                                            weightTicketDetail.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
//                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                                details_item.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                                details_item.setDocYear(((WsDeliveryUpdateBapi) objBapi).getDoc_year());
                                                outbDel.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                            }
                                            if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                                details_item.setPosted(false);
                                                outbDel.setPosted(false);
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted(true);
                                                outbDel.setPosted(true);
                                            }

                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(outbDel);
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                            outbDel.setPosted(false);
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted(true);
                                        }
                                    }

                                    if (flag_fail || weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")) {
                                        revertCompletedDO(completedDO, outDetails_lits, outbDel_list);
                                        weightTicket.setPosted(false);
                                        if (bapi_message == "") {
                                            bapi_message = resourceMapMsg.getString("msg.errorBAPI");
                                        }
                                        JOptionPane.showMessageDialog(rootPane, bapi_message);
                                        completed = false;
                                        entityManager.clear();
                                    } else if (!flag_fail) {
                                        weightTicket.setPosted(true);
                                        completedDO.add(weightTicketDetail.getDeliveryOrderNo());
                                    }

                                    // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
                                    if (outbDel != null) {
                                        OutboundDelivery sapOutb = sapService.getOutboundDelivery(outbDel.getDeliveryOrderNo());
                                        Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                        Vendor lifnr = null, sapLifnr = null;
                                        if (sapOutb != null) {
                                            if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                                kunnr = weightTicketRegistarationController.findByKunnr(sapOutb.getKunnr());
                                                sapKunnr = sapService.getCustomer(sapOutb.getKunnr());
                                            }
                                            if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                                kunag = weightTicketRegistarationController.findByKunnr(sapOutb.getKunag());
                                                sapKunag = sapService.getCustomer(sapOutb.getKunag());
                                            }
                                            if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                                lifnr = weightTicketRegistarationController.findByLifnr(sapOutb.getLifnr());
                                                sapLifnr = sapService.getVendor(sapOutb.getLifnr());
                                            }
                                        }
                                        //Store Ship to party Info
                                        if (sapKunnr != null && kunnr == null) {
                                            entityManager.persist(sapKunnr);
                                        } else if (sapKunnr != null && kunnr != null) {
                                            sapKunnr.setId(kunnr.getId());
                                            entityManager.merge(sapKunnr);
                                        } else if (sapKunnr == null && kunnr != null) {
                                            entityManager.remove(kunnr);
                                        }
                                        //Store Sold to party Info
                                        if (sapKunag != null && kunag == null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                            entityManager.persist(sapKunag);
                                        } else if (sapKunag != null && kunag != null) {
                                            sapKunag.setId(kunag.getId());
                                            entityManager.merge(sapKunag);
                                        } else if (sapKunag == null && kunag != null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                            entityManager.remove(kunag);
                                        }
                                        //Store Vendor Info
                                        if (sapLifnr != null && lifnr == null) {
                                            entityManager.persist(sapLifnr);
                                        } else if (sapLifnr != null && lifnr != null) {
                                            sapLifnr.setId(lifnr.getId());
                                            entityManager.merge(sapLifnr);
                                        } else if (sapLifnr == null && lifnr != null) {
                                            entityManager.remove(lifnr);
                                        }
                                        // 120518_17h keep values(posted, mat_doc) which are gotten from Save SAP at the first time
                                        sapOutb.setId(outbDel.getId());
                                        sapOutb.setPosted(outbDel.isPosted());
                                        sapOutb.setMatDoc(outbDel.getMatDoc());
                                        entityManager.merge(sapOutb);
                                        outbDel = sapOutb;
                                    }
                                    // </editor-fold>
//                    }
                                } catch (Exception e) {
                                    if (objBapi instanceof WsDeliveryUpdateBapi) {
                                        if (((WsDeliveryUpdateBapi) objBapi).getReturn() != null
                                                && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)) != null
                                                && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId() != null
                                                && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId().equals("NOREVERT")) {
                                        } else {

                                            revertCompletedDO(completedDO, outDetails_lits, outbDel_list);
                                            outbDel.setPosted(false);
                                        }
                                    }
                                    weightTicket.setPosted(false);
                                    failed(e);
                                    completed = false;
                                    entityManager.clear();
                                }

                            } else if (WeighBridgeApp.getApplication().isOfflineMode() || objBapi == null) {
                                weightTicket.setPosted(true);
                                weightTicketDetail.setUnit("TON");
                            }
                        } else {
                            weightTicket.setPosted(true);
                            weightTicketDetail.setUnit("TON");
                        }
                    }

                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    entityManager.merge(outbDel);
                    entityManager.getTransaction().commit();
                }
            }
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                weightTicket.setPosted(true);
                weightTicketDetail.setUnit("TON");
            }
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.merge(weightTicket);
            entityManager.getTransaction().commit();
            entityManager.clear();
            if (completed) {
                if (!weightTicket.isDissolved() || weightTicket.isPosted()) { //+20100111#01 khong in neu huy phieu
                    setMessage(resourceMapMsg.getString("msg.printing"));
                    printWT(weightTicket, false);
                }
            }
            btnSave.setEnabled(false);
            return null;
        }

        protected void revertCompletedDO(List<String> completedDOs, List<OutboundDeliveryDetail> OutbDetailsV2, List<OutboundDelivery> outbDels) {
            weightTicketController.revertCompletedDO(completedDOs, OutbDetailsV2, outbDels, weightTicket, outDetails_lits, sapSession);
        }

        @Override
        protected void failed(Throwable cause) {
            completed = false;
            if ((isStage2() && !weightTicket.isDissolved())
                    || (weightTicket != null && !weightTicket.isPosted())) {
//                weightTicket.setPosted(-1);
            }
//            entityManager.merge(weightTicket);
//            entityManager.getTransaction().commit();
//            entityManager.clear();
            if (cause instanceof SapException) {
                for (SapException.SapError error : ((SapException) cause).getErrors()) {
                    logger.error(null, new Exception(error.toString()));
                    String transformedMsg = SAPErrorTransform.getMessage(error);
                    if (transformedMsg == null) {
                        JOptionPane.showMessageDialog(rootPane, error.getMessage());
                    } else {
                        JOptionPane.showMessageDialog(rootPane, transformedMsg);
                    }
                    weightTicket.setPosted(false);
                }
            } else {
                if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                    cause = cause.getCause();
                }
                logger.error(null, cause);
                JOptionPane.showMessageDialog(rootPane, cause != null ? cause.getMessage() : "Null Pointer Exception");
                weightTicket.setPosted(false);
            }
            entityManager.merge(weightTicket);
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
            setSaveNeeded(true);
        }

        @Override
        protected void finished() {
            clearForm();
            setSaveNeeded(isValidated());
        }
    }

    private class AcceptBatchTask extends org.jdesktop.application.Task<Object, Void> {

        AcceptBatchTask(org.jdesktop.application.Application app) {
            super(app);
            config = WeighBridgeApp.getApplication().getConfig();
        }

        @Override
        protected Object doInBackground() {
            String charg = cbxCharg.getSelectedIndex() == -1 && cbxCharg.isEditable()
                    ? cbxCharg.getEditor().getItem().toString().trim().isEmpty() ? null : cbxCharg.getEditor().getItem().toString().trim()
                    : cbxCharg.getSelectedItem().toString();
            //+20100216#01
            if (charg != null) {
                charg.toUpperCase(Locale.ENGLISH);
            }
            //+20100216#01
            weightTicket.setCharg(charg);
//            if (rbtMvt311.isSelected()) {
//                weightTicket.setRecvCharg(charg);
//            }
            lblCharg.setForeground(Color.black);
            if (isSubContract() && txfGoodsQty.getValue() != null) {
                setMessage(resourceMapMsg.getString("msg.checkIssetWarehouse"));
                WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                Double remaining = CheckMatStock(weightTicketDetail.getMatnrRef(), configuration.getWkPlant(), weightTicket.getLgort(), weightTicket.getCharg());
                if (weightTicket.getGQty().doubleValue() > remaining) {
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.oBiggerWarehouse"));
                    txfOutQty.setValue(null);
                    txfGoodsQty.setValue(null);
                    weightTicket.setGQty(null);
                }
            }
            return null;  // return your result
        }

        @Override
        protected void finished() {
            setSaveNeeded(isValidated());
        }
    }

    private class AcceptScaleTask extends Task<Object, Void> {

        AcceptScaleTask(org.jdesktop.application.Application app) {
            super(app);
            config = WeighBridgeApp.getApplication().getConfig();
        }

        @Override
        protected Object doInBackground() throws Exception {
            WeighBridgeApp.getApplication().disconnectWB();
            formatter.applyPattern(WeighBridgeApp.DATE_TIME_DISPLAY_FORMAT);
            Date now = weightTicketController.getServerTime();
            grbBridge.clearSelection();
            btnAccept.setEnabled(false);
            if (isStage1()) {
                txfInQty.setValue(txfCurScale.getValue());
                txtInTime.setText(formatter.format(now));
            } else if (isStage2()) {
                txfOutQty.setValue(txfCurScale.getValue());
                txtOutTime.setText(formatter.format(now));
            }
            if (txfInQty.getValue() != null && txfOutQty.getValue() != null) {
                double dIn = ((Number) txfInQty.getValue()).doubleValue();
                double dOut = ((Number) txfOutQty.getValue()).doubleValue();
                double result = 0d;
                if (dIn > dOut) {
                    result = dIn - dOut;
                    if (weightTicket.getRegType() == 'O') {
                        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.iBiggerO"));
                        txfOutQty.setValue(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        return null;
                    }
                } else {
                    result = dOut - dIn;
                    if (weightTicket.getRegType() == 'I') {
                        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.oBiggerI"));
                        txfOutQty.setValue(null);
                        txtOutTime.setText(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        return null;
                    }
                }
                result = result / 1000d;
                if (result == 0) {
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.massOrderInvalid"));
                    txfOutQty.setValue(null);
                    txfGoodsQty.setValue(null);
                    weightTicket.setGQty(null);
                    return null;
                } else if (configuration.getWeightLimit() > 0
                        && result > configuration.getWeightLimit()) {
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.ordeRexceed", configuration.getWeightLimit()));
                    txfOutQty.setValue(null);
                    txfGoodsQty.setValue(null);
                    weightTicket.setGQty(null);
                    return null;
                }
                //  Conver result from KG unit to TON unit

                double main_qty = 0;
                double qty = 0;
                if (outbDel != null) {
                    try {
                        main_qty = outbDel.getLfimg() != null ? outbDel.getLfimg().doubleValue() : 0;
                    } catch (Exception e) {
                    }
                    double free_qty = 0;
                    try {
                        free_qty = outbDel.getFreeQty() != null ? outbDel.getFreeQty().doubleValue() : 0;
                    } catch (Exception e) {
                    }
                    qty = total_qty_goods.doubleValue() + total_qty_free.doubleValue();
                }

                //add by Tuanna 5/3/2013 for bug can not accept second fart in process GI clinker for DIC 
                String slfart = "";
                String sMatName = "";
                boolean bag = false;
                boolean btype = false;
                boolean bCompare = false;
                if (outbDel != null) {
                    slfart = outbDel.getLfart();
                    sMatName = outbDel.getArktx() != null ? outbDel.getArktx().toUpperCase() : "";
                    bag = (outbDel.getArktx() != null && outbDel.getArktx().toUpperCase().indexOf("BAO") >= 0) ? true : false;
                    btype = (outbDel.getLfart() != null
                            && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF"))) ? true : false;

                    bCompare = (bag || btype);
                }
                if (bCompare) // end add
                {
                    String param = (outbDel != null && outbDel.getMatnr() != null) ? outbDel.getMatnr().toString() : "";

                    if (param.equals("") && purOrder != null) {
                        PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                        if (purchaseOrderDetail.getMaterial() != null && !purchaseOrderDetail.getMaterial().isEmpty()) {
                            param = purchaseOrderDetail.getMaterial();
                        }
                    }
                    Variant vari = weightTicketController.findByParam(Constants.WeightTicketView.PROCESS_ORDER_CF);
                    double valu = 0;

                    if (vari != null) {

                        if (vari.getValue() != null && !vari.getValue().isEmpty()) {
                            valu = Double.parseDouble(vari.getValue());
                        }

                        double upper = qty + (qty * valu) / 100;
                        double lower = qty - (qty * valu) / 100;

                        if ((lower <= result && result <= upper)) {
                            txfGoodsQty.setValue(result);
                            weightTicket.setGQty(new BigDecimal(Double.toString(result)));
                        } else {
                            String msg = "";
                            // TODO review to remove this message
//                            try {
//                                msg = weightTicketController.getMsg();
//                            } catch (Exception ex) {
//                                msg = resourceMapMsg.getString("msg.massOrderOutLimit");
//                            }
                            JOptionPane.showMessageDialog(rootPane, msg); //variant mesage --> Tuanna
                            txfOutQty.setValue(null);
                            txtOutTime.setText(null);
                            txfGoodsQty.setValue(null);
                            weightTicket.setGQty(null);
                            btnAccept.setEnabled(false);
                            // Tuanna add 14.01.2013 - Check logic when input value not match require condition  
                            btnOScaleReset.setEnabled(true);
                            return null;
                        }
                    } else {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(Double.toString(result)));
                    }
                } else if (isSubContract() && weightTicket.getLgort() != null && weightTicket.getCharg() != null) {
                    setMessage(resourceMapMsg.getString("msg.checkIssetWarehouse")); // checking stock --tuanna
                    Double remaining = CheckMatStock(weightTicket.getWeightTicketDetail().getMatnrRef(), configuration.getWkPlant(), weightTicket.getLgort(), weightTicket.getCharg());
                    if (result <= remaining) {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(Double.toString(result)));
                    } else {
                        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.oBiggerWarehouse"));
                        txfOutQty.setValue(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        btnOScaleReset.setEnabled(true);
                    }
                } else {
                    txfGoodsQty.setValue(result);
                    weightTicket.setGQty(new BigDecimal(Double.toString(result)));
                }
            }
            if (isStage1()) {
                txfInQty.setValue(txfCurScale.getValue());
                txtInTime.setText(formatter.format(now));
                weightTicket.setFCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                weightTicket.setFScale(new BigDecimal(((Number) txfInQty.getValue()).doubleValue()));
                weightTicket.setFTime(now);
                lblIScale.setForeground(Color.black);
                OutboundDeliveryDetail item;
                if (outDetails_lits.size() > 0) {
                    for (int i = 0; i < outDetails_lits.size(); i++) {
                        item = outDetails_lits.get(i);
                        item.setInScale(new BigDecimal(((Number) txfInQty.getValue()).doubleValue() / 1000));
                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                        }
                        entityManager.merge(item);
                        entityManager.getTransaction().commit();
                    }
                }
            } else if (isStage2()) {
                txfOutQty.setValue(txfCurScale.getValue());
                txtOutTime.setText(formatter.format(now));
                weightTicket.setSCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                weightTicket.setSScale(new BigDecimal(((Number) txfOutQty.getValue()).doubleValue()));
                weightTicket.setSTime(now);
                lblOScale.setForeground(Color.black);
                OutboundDeliveryDetail item;
                double remain = ((Number) txfCurScale.getValue()).doubleValue() - ((Number) txfInQty.getValue()).doubleValue();
                if (remain < 0) {
                    remain = remain * 1;
                }
                remain = remain / 1000;
                // 20120522_ fix logic_code in for_loop
                if (outDetails_lits.size() > 1) {
                    for (int i = 0; i < outDetails_lits.size(); i++) {
                        item = outDetails_lits.get(i);
                        if (i < outDetails_lits.size() - 1) {
                            item.setGoodsQty(item.getLfimg());
                            item.setOutScale(BigDecimal.valueOf(item.getInScale().doubleValue() + item.getLfimg().doubleValue()));
                            remain = remain - item.getLfimg().doubleValue();
                        } else {
                            item.setGoodsQty(BigDecimal.valueOf(remain));
                            item.setOutScale(BigDecimal.valueOf(item.getInScale().doubleValue() + remain));
                        }
                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                        }
                        entityManager.merge(item);
                        entityManager.getTransaction().commit();
                    }
                } else if (outDetails_lits.size() == 1) {
                    BigDecimal div = BigDecimal.valueOf(1000);
                    item = outDetails_lits.get(0);
                    item.setOutScale(weightTicket.getSScale().divide(div));
                    item.setGoodsQty(weightTicket.getSScale().subtract(weightTicket.getFScale()).divide(div).abs());
                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    entityManager.merge(item);
                    entityManager.getTransaction().commit();
                }
            }
            return null;
        }

        @Override
        protected void finished() {
            if (txfCurScale.isEditable()) {
                weightTicket.setManual('X');
            } else {
                weightTicket.setManual(null);
            }
            setSaveNeeded(isValidated());
        }
    }

    private Double CheckMatStock(String matnr, String plant, String sloc, String batch) {
        Double remaining = 0d;
        try {
            weightTicketController.CheckMatStock(matnr, plant, sloc, batch);
        } catch (Throwable cause) {
            if (cause instanceof SapException) {
                for (SapException.SapError error : ((SapException) cause).getErrors()) {
                    logger.error(null, new Exception(error.toString()));
                    JOptionPane.showMessageDialog(rootPane, error.getMessage());
                }
            } else {
                if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                    cause = cause.getCause();
                }
                logger.error(null, cause);
                JOptionPane.showMessageDialog(rootPane, cause.getMessage());
            }
        }
        return remaining;
    }

    private void clearForm() {
        weightTicket = null;
        purOrder = null;
        outbDel = null;
        material = null;
        this.setDissolved(false);
        this.setEnteredValidPONum(false);
        this.setEnteredValidWTNum(false);
        this.setReprintable(false);
        this.setSaveNeeded(false);
        this.setStage1(false);
        this.setStage2(false);
        this.setValidPONum(false);
        this.setWithoutDO(false);
        cbxKunnr.setSelectedIndex(-1);
        cbxKunnr.setEnabled(false);
        txtGRText.setEnabled(false);
        cbxCharg.setEditable(false);
        this.setFormEnable(false);
        this.setSubContract(false);
        this.setMvt311(false);
        this.setMaterialAvailable(false);
        this.setMatAvailStocks(null);

        grbType.clearSelection();
        grbCat.clearSelection();

        txtPONo.setText(null);
        txfCurScale.setValue(null);
        txfInQty.setValue(null);
        txtInTime.setText(null);
        txfOutQty.setValue(null);
        txtOutTime.setText(null);
        txfGoodsQty.setValue(null);
        txtDName.setText(null);
        txtCMNDBL.setText(null);
        txtLicPlate.setValue(null);
        txtTrailerPlate.setValue(null);
        cbxSLoc.setSelectedIndex(-1);

        txtGRText.setText(null);
        txtRegItem.setText(null);
        txtMatnr.setText(null);
        txtDelNum.setText(null);
        cbxCharg.setModel(new DefaultComboBoxModel());
        txtCementDesc.setText(null);
        txtCementDesc.setEditable(true);

        lblIScale.setForeground(Color.black);
        lblOScale.setForeground(Color.black);
        lblGScale.setForeground(Color.black);
        lblSLoc.setForeground(Color.black);
        lblCharg.setForeground(Color.black);

        // Temporary enable btnAccept
//        btnAccept.setEnabled(true);
        txtRegistrationNo.setText(null);
        txtProcedure.setText(null);
        txtSling.setText(null);
        txtPallet.setText(null);
        txtBatchProduce.setText(null);
        txtBatchProduce.setEditable(true);
        txtTicketId.setText(null);
        txtWeightTicketIdRef.setText(null);
        txtRemark.setText(null);
        txtWeight.setText(null);
        txtPoPosto.setText(null);
        cbxVendorLoading.setModel(new DefaultComboBoxModel());
        cbxVendorTransport.setModel(new DefaultComboBoxModel());

        setAllChildPanelsVisible(pnWTLeft);
        setAllChildPanelsVisible(pnWTRight);
        btnPostAgain.setEnabled(false);
        btnReprint.setEnabled(false);
        btnSave.setEnabled(false);
    }

    private void setAllChildPanelsVisible(Container parent) {
        Component[] components = parent.getComponents();

        if (components.length > 0) {
            for (Component component : components) {
                component.setVisible(true);
            }
        }
    }

    private Object getGrDoMigoBapi(WeightTicket wt, OutboundDelivery outbDel) {
        return weightTicketController.getGrDoMigoBapi(wt, weightTicket, outbDel, outDetails_lits, timeFrom, timeTo);
    }

    private Object getGrPoMigoBapi(WeightTicket wt, String number) {
        return weightTicketController.getGrPoMigoBapi(wt, weightTicket, number, timeFrom, timeTo);
    }

    private Object getGi541MigoBapi(WeightTicket wt) {
        return weightTicketController.getGi541MigoBapi(wt, weightTicket, timeFrom, timeTo, purOrder, rbtOutward);
    }

    private Object getGiMB1BBapi(WeightTicket wt) {
        return weightTicketController.getGiMB1BBapi(wt, weightTicket, timeFrom, timeTo, rbtOutward);
    }

    private Object getDoCreate2PGI(WeightTicket wt, OutboundDelivery outbDel) {
        return weightTicketController.getDoCreate2PGI(wt, outbDel, weightTicket, timeFrom, timeTo, outDetails_lits);
    }

    private Object getPgmVl02nBapi(WeightTicket wt, OutboundDelivery outbDel) {
        return weightTicketController.getPgmVl02nBapi(wt, outbDel, weightTicket, timeFrom, timeTo, outDetails_lits);
    }

    private Object getMvtPOSTOCreatePGI(WeightTicket wt, String number) {
        return weightTicketController.getMvtPOSTOCreatePGI(wt, weightTicket, number, timeFrom, timeTo);
    }

    private void printWT(WeightTicket wt, boolean reprint) {
        weightTicketController.printWT(wt, reprint, ximang, outbDel_list, outDetails_lits, outbDel, txtPONo.getText(), stage1, rootPane);
    }

    private void getSAPMatData(DocumentEvent e) {
        if (weightTicket == null) {
            return;
        }
        try {
            String val = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            if (!val.isEmpty()) {
                material = sapService.getMaterialDetail(val);
            }
        } catch (Exception ex) {
            logger.error(null, ex);
        } finally {
            setSaveNeeded(isValidated());
        }
    }

    public boolean isValidated() {
        config = WeighBridgeApp.getApplication().getConfig();
        boolean result = false, bMisc = false, bPO = false, bMB1B = false, bMvt311 = false, bScale = false, bSLoc = false, bBatch = false;
        boolean bMaterial = false, bBatchMng = false;
        boolean bNiemXa = true;
        boolean bBatchProduce = true;
//        if (chkDissolved.isSelected()) {
//            bMisc = bPO = bMB1B = bMvt311 = bScale = bSLoc = bBatch = !isDissolved();
//            rbtMisc.setForeground(Color.black);
//            if (WeighBridgeApp.getApplication().isOfflineMode()) {
//                rbtMisc.setForeground(Color.red);
//            }
//            rbtPO.setForeground(Color.black);
//            rbtMb1b.setForeground(Color.black);
//            rbtMvt311.setForeground(Color.black);
//            lblIScale.setForeground(Color.black);
//            lblOScale.setForeground(Color.black);
//            lblGScale.setForeground(Color.black);
//            lblSLoc.setForeground(Color.black);
//            lblCharg.setForeground(Color.black);
//        } else {
        if (grbType.getSelection() == null && isWithoutDO()) {
        } else {
            bMisc = WeighBridgeApp.getApplication().isOfflineMode();
            bPO = true;
            bMB1B = true;
            bMvt311 = true;
        }
        if ((txtPONo.getText() != null || !"".equals(txtPONo.getText())) && isEnteredValidPONum() && isValidPONum()) {
            bPO = true;
        } else if ((txtPONo.getText() != null || !"".equals(txtPONo.getText())) && (!isEnteredValidPONum() || isValidPONum())) {
            bPO = false;
        }

        if (weightTicket.getRecvLgort() == null) {
            bMB1B = false;
        } else {
            bMB1B = true;
        }

        if ((weightTicket.getRecvLgort() == null || weightTicket.getWeightTicketDetail().getRecvMatnr() == null)) {
            bMvt311 = false;
        } else {
            bMvt311 = true;
        }

        if (!txtMatnr.getText().trim().isEmpty() && material != null) {
            bMaterial = true;
            if (material.getXchpf() != null && material.getXchpf().charValue() == 'X') {
                bBatchMng = true;
            } else {
                bBatchMng = false;
            }
        } else {
            bMaterial = bBatchMng = false;
        }

        if (!isStage1() && !isStage2()) {
            bScale = true;
        }
        if (isStage1()) {
            bScale = !(txfInQty.getValue() == null);
            if (bScale) {
                lblIScale.setForeground(Color.black);
            } else {
                lblIScale.setForeground(Color.red);
            }
        } else if (isStage2()) {
            bScale = !(txfOutQty.getValue() == null && txfGoodsQty.getValue() == null);
            if (bScale) {
                lblOScale.setForeground(Color.black);
                lblGScale.setForeground(Color.black);
            } else {
                lblOScale.setForeground(Color.red);
                lblGScale.setForeground(Color.red);
            }
        }
        bSLoc = !(cbxSLoc.getSelectedIndex() == -1);
        bBatch = !((cbxCharg.getSelectedIndex() == -1 && cbxCharg.isEditable()) || (cbxCharg.isEditable() && cbxCharg.getEditor().getItem().toString().trim().isEmpty()));
        if (bMisc) {
            bSLoc = true;
            bBatch = true;
        }
        if (!bBatch && bMaterial && !bBatchMng) {
            bBatch = true;
        }
        if (bSLoc) {
            lblSLoc.setForeground(Color.black);
        } else {
            lblSLoc.setForeground(Color.red);
        }
        if (bBatch) {
            lblCharg.setForeground(Color.black);
        } else {
            lblCharg.setForeground(Color.red);
        }
        //}
        bBatch = true;
        txtGRText.setEnabled(true);
        if (outbDel != null && outbDel.getMatnr() != null) {
            materialConstraint = weightTicketController.getMaterialConstraintByMatnr(outbDel.getMatnr());
            if (isStage2()
                    && materialConstraint != null && materialConstraint.getRequiredNiemXa() 
                    && (txtCementDesc.getText().trim() == null || txtCementDesc.getText().trim().equals(""))) {
                bNiemXa = false;
            }
            if (bNiemXa) {
                lblCementDesc.setForeground(Color.black);
            } else {
                lblCementDesc.setForeground(Color.red);
                txtCementDesc.setEditable(true);
            }
            if (isStage2() && outbDel.getMatnr() != null
                    && materialConstraint != null && materialConstraint.getRequiredBatch() 
                    && (txtBatchProduce.getText().trim() == null || txtBatchProduce.getText().trim().equals(""))) {
                bBatchProduce = false;
            }
            if (bBatchProduce) {
                lblBatchProduce.setForeground(Color.black);
            } else {
                lblBatchProduce.setForeground(Color.red);
                txtBatchProduce.setEditable(true);
            }
        }
        result = (bMisc || bPO || bMB1B || bMvt311) && bScale && bSLoc && bBatch && bBatchProduce && bNiemXa && (isStage1() || isStage2() || (!isStage1() && !isStage2() && weightTicket != null && weightTicket.isPosted()));
        return result;
    }

    public GoodsMvtWeightTicketStructure fillWTStructure(WeightTicket wt,
            OutboundDelivery od, List<OutboundDeliveryDetail> od_v2_list) {
        return weightTicketController.fillWTStructure(wt, od, od_v2_list, weightTicket);
    }
    // </editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Variables declaration Area">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnIScaleReset;
    private javax.swing.JButton btnOScaleReset;
    private javax.swing.JButton btnPostAgain;
    private javax.swing.JButton btnReprint;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox cbxCharg;
    private javax.swing.JComboBox cbxKunnr;
    private javax.swing.JComboBox cbxSLoc;
    private javax.swing.JComboBox cbxVendorLoading;
    private javax.swing.JComboBox cbxVendorTransport;
    private javax.swing.ButtonGroup grbBridge;
    private javax.swing.ButtonGroup grbCat;
    private javax.swing.ButtonGroup grbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel lbKunnr;
    private javax.swing.JLabel lblBatchProduce;
    private javax.swing.JLabel lblCMNDBL;
    private javax.swing.JLabel lblCementDesc;
    private javax.swing.JLabel lblCharg;
    private javax.swing.JLabel lblChargIn;
    private javax.swing.JLabel lblDName;
    private javax.swing.JLabel lblDelNum;
    private javax.swing.JLabel lblGRText;
    private javax.swing.JLabel lblGScale;
    private javax.swing.JLabel lblGUnit;
    private javax.swing.JLabel lblIScale;
    private javax.swing.JLabel lblITime;
    private javax.swing.JLabel lblIUnit;
    private javax.swing.JLabel lblKG;
    private javax.swing.JLabel lblLgortIn;
    private javax.swing.JLabel lblLicPlate;
    private javax.swing.JLabel lblMatnr;
    private javax.swing.JLabel lblOScale;
    private javax.swing.JLabel lblOTime;
    private javax.swing.JLabel lblOUnit;
    private javax.swing.JLabel lblPONo;
    private javax.swing.JLabel lblPallet;
    private javax.swing.JLabel lblPoPosto;
    private javax.swing.JLabel lblProcedure;
    private javax.swing.JLabel lblRegCat;
    private javax.swing.JLabel lblRegItem;
    private javax.swing.JLabel lblRegistrationNo;
    private javax.swing.JLabel lblRemark;
    private javax.swing.JLabel lblSLoc;
    private javax.swing.JLabel lblSO;
    private javax.swing.JLabel lblSling;
    private javax.swing.JLabel lblTicketId;
    private javax.swing.JLabel lblTrailerPlate;
    private javax.swing.JLabel lblVendorLoading;
    private javax.swing.JLabel lblVendorTransport;
    private javax.swing.JLabel lblWTNum;
    private javax.swing.JLabel lblWeight;
    private javax.swing.JLabel lblWeightTicketIdRef;
    private javax.swing.JPanel pnCurScale;
    private javax.swing.JPanel pnCurScaleData;
    private javax.swing.JPanel pnScaleData;
    private javax.swing.JPanel pnWTFilter;
    private javax.swing.JPanel pnWTLeft;
    private javax.swing.JPanel pnWTRight;
    private javax.swing.JPanel pnWTicket;
    private javax.swing.JRadioButton rbtBridge1;
    private javax.swing.JRadioButton rbtBridge2;
    private javax.swing.JRadioButton rbtInward;
    private javax.swing.JRadioButton rbtOutward;
    private javax.swing.JFormattedTextField txfCurScale;
    private javax.swing.JFormattedTextField txfGoodsQty;
    private javax.swing.JFormattedTextField txfInQty;
    private javax.swing.JFormattedTextField txfOutQty;
    private javax.swing.JFormattedTextField txtBatchProduce;
    private javax.swing.JTextField txtCMNDBL;
    private javax.swing.JTextField txtCementDesc;
    private javax.swing.JTextField txtChargIn;
    private javax.swing.JTextField txtDName;
    private javax.swing.JTextField txtDelNum;
    private javax.swing.JTextField txtGRText;
    private javax.swing.JTextField txtInTime;
    private javax.swing.JTextField txtLgortIn;
    private javax.swing.JFormattedTextField txtLicPlate;
    private javax.swing.JTextField txtMatnr;
    private javax.swing.JTextField txtOutTime;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JFormattedTextField txtPallet;
    private javax.swing.JTextField txtPoPosto;
    private javax.swing.JTextField txtProcedure;
    private javax.swing.JTextField txtRegItem;
    private javax.swing.JTextField txtRegistrationNo;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtSO;
    private javax.swing.JFormattedTextField txtSling;
    private javax.swing.JFormattedTextField txtTicketId;
    private javax.swing.JFormattedTextField txtTrailerPlate;
    private javax.swing.JTextField txtWTNum;
    private javax.swing.JTextField txtWeight;
    private javax.swing.JFormattedTextField txtWeightTicketIdRef;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
}
