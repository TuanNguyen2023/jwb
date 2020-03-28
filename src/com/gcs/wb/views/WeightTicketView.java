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
import com.gcs.wb.bapi.SAPSession;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPOSTOCreatePGIBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.DOPostingPGIBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.exceptions.IllegalPortException;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.base.util.IntegerUtil;
import com.gcs.wb.base.util.RegexFormatter;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.base.util.ToleranceUtil;
import com.gcs.wb.base.validator.LengthValidator;
import com.gcs.wb.controller.WeightTicketController;
import com.gcs.wb.controller.WeightTicketRegistarationController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.repositorys.MaterialInterPlantRepository;
import com.gcs.wb.jpa.repositorys.MaterialInternalRepository;
import com.gcs.wb.jpa.repositorys.MaterialRepository;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketDetailRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketRepository;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.JCoException;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.hibersap.SapException;
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
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.border.Border;

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
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(WeightTicketView.class);
    private com.gcs.wb.jpa.entity.Material material;
    private com.gcs.wb.jpa.entity.OutboundDelivery outbDel;
    private com.gcs.wb.jpa.entity.PurchaseOrder purOrder;
    private com.gcs.wb.jpa.entity.SAPSetting setting;
    private com.gcs.wb.jpa.entity.WeightTicket weightTicket;
    private MaterialConstraint materialConstraint;
    VendorRepository vendorRepository = new VendorRepository();
    WeightTicketDetailRepository weightTicketDetailRepository = new WeightTicketDetailRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    ToleranceUtil toleranceUtil = new ToleranceUtil();
    MaterialRepository materialRepository = new MaterialRepository();
    MaterialInternalRepository materialInternalRepository = new MaterialInternalRepository();

    WeightTicketController weightTicketController = new WeightTicketController();
    WeightTicketRegistarationController weightTicketRegistarationController = new WeightTicketRegistarationController();

    SAPService sapService = new SAPService();
    PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
    MaterialInterPlantRepository materialInterPlantRepository = new MaterialInterPlantRepository();

    PurchaseOrder purchaseOrder = new PurchaseOrder();
    private boolean flgPost = false;
    private DecimalFormat df = new DecimalFormat("#,##0.000");

    public WeightTicketView() {
        weightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        purOrder = new com.gcs.wb.jpa.entity.PurchaseOrder();
        outbDel = new com.gcs.wb.jpa.entity.OutboundDelivery();
        setting = java.beans.Beans.isDesignTime() ? null : WeighBridgeApp.getApplication().getSapSetting();
        material = new com.gcs.wb.jpa.entity.Material();
        materialConstraint = new MaterialConstraint();
        initComponents();

        setBridge1(configuration.getWb1Port() != null);
        setBridge2(configuration.getWb2Port() != null);

        formatter = new SimpleDateFormat();
        txfCurScale.setEditable(true);
        txtInTime.setEditable(true);
        txtOutTime.setEditable(true);
        sapSetting = WeighBridgeApp.getApplication().getSapSetting();
        login = WeighBridgeApp.getApplication().getLogin();
        entityManager.clear();

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

        // cấu hình cho cầu cân hiển thị PO và vendor
        if ((sapSetting.getCheckPov()) != null && (sapSetting.getCheckPov()) == true) {
            txtPoPosto.setVisible(true);
            txtVendorLoading.setVisible(true);
            txtVendorTransport.setVisible(true);
            lblPoPosto.setVisible(true);
            lblVendorLoading.setVisible(true);
            lblVendorTransport.setVisible(true);
        } else {
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
        }

        txtSLoc.setText(null);
        setAllChildPanelsVisible(pnWTLeft);
        setAllChildPanelsVisible(pnWTRight);
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
        lblSalan = new javax.swing.JLabel();
        txtSalan = new javax.swing.JTextField();
        lblLoadSource = new javax.swing.JLabel();
        txtLoadSource = new javax.swing.JTextField();
        pnWTRight = new javax.swing.JPanel();
        txtDelNum = new javax.swing.JTextField();
        txtPONo = new javax.swing.JTextField();
        txtRegItem = new javax.swing.JTextField();
        txtMatnr = new javax.swing.JTextField();
        txtWeight = new javax.swing.JTextField();
        txtLgortIn = new javax.swing.JTextField();
        txtChargIn = new javax.swing.JTextField();
        lblVendorTransport = new javax.swing.JLabel();
        lblVendorLoading = new javax.swing.JLabel();
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
        jLabel1 = new javax.swing.JLabel();
        txtSO = new javax.swing.JTextField();
        lblSO = new javax.swing.JLabel();
        txtVendorLoading = new javax.swing.JTextField();
        txtVendorTransport = new javax.swing.JTextField();
        txtCharg = new javax.swing.JTextField();
        txtCustomer = new javax.swing.JTextField();
        txtSLoc = new javax.swing.JTextField();
        lblPoPosto = new javax.swing.JLabel();
        txtPoPosto = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnPostAgain = new javax.swing.JButton();
        btnReprint = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

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
                .addComponent(txtWTNum, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(129, 129, 129))
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
                .addContainerGap()
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                        .addComponent(rbtBridge1)
                        .addGap(18, 18, 18)
                        .addComponent(rbtBridge2))
                    .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                        .addComponent(txfCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblKG))
                    .addComponent(btnAccept, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
        pnCurScaleDataLayout.setVerticalGroup(
            pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtBridge1)
                    .addComponent(rbtBridge2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnCurScaleDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txfCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblKG))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAccept)
                .addGap(24, 24, 24))
        );

        pnCurScale.add(pnCurScaleData, java.awt.BorderLayout.CENTER);

        pnScaleData.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnScaleData.border.title"))); // NOI18N
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
        txfInQty.setFont(resourceMap.getFont("txfGoodsQty.font")); // NOI18N
        txfInQty.setName("txfInQty"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${weightTicket.FScale}"), txfInQty, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txfOutQty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txfOutQty.setEditable(false);
        txfOutQty.setForeground(resourceMap.getColor("txfOutQty.foreground")); // NOI18N
        txfOutQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfOutQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txfOutQty.setFont(resourceMap.getFont("txfGoodsQty.font")); // NOI18N
        txfOutQty.setName("txfOutQty"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${weightTicket.SScale}"), txfOutQty, org.jdesktop.beansbinding.BeanProperty.create("value"));
        bindingGroup.addBinding(binding);

        txfGoodsQty.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txfGoodsQty.setEditable(false);
        txfGoodsQty.setForeground(resourceMap.getColor("txfGoodsQty.foreground")); // NOI18N
        txfGoodsQty.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txfGoodsQty.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
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
                    .addComponent(txfGoodsQty, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .addComponent(txfOutQty, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                    .addComponent(txfInQty, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE))
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
                            .addComponent(txtOutTime, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                            .addComponent(txtInTime, javax.swing.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE))
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

        txtRegistrationNo.setEditable(false);
        txtRegistrationNo.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtRegistrationNo.setName("txtRegistrationNo"); // NOI18N

        txtDName.setEditable(false);
        txtDName.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtDName.setName("txtDName"); // NOI18N

        lblRegistrationNo.setText(resourceMap.getString("lblRegistrationNo.text")); // NOI18N
        lblRegistrationNo.setName("lblRegistrationNo"); // NOI18N

        lblDName.setText(resourceMap.getString("lblDName.text")); // NOI18N
        lblDName.setName("lblDName"); // NOI18N

        lblCMNDBL.setText(resourceMap.getString("lblCMNDBL.text")); // NOI18N
        lblCMNDBL.setName("lblCMNDBL"); // NOI18N

        txtCMNDBL.setEditable(false);
        txtCMNDBL.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtCMNDBL.setName("txtCMNDBL"); // NOI18N

        lblLicPlate.setText(resourceMap.getString("lblLicPlate.text")); // NOI18N
        lblLicPlate.setName("lblLicPlate"); // NOI18N

        txtLicPlate.setEditable(false);
        txtLicPlate.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtLicPlate.setName("txtLicPlate"); // NOI18N

        lblTrailerPlate.setText(resourceMap.getString("lblTrailerPlate.text")); // NOI18N
        lblTrailerPlate.setName("lblTrailerPlate"); // NOI18N

        txtTrailerPlate.setEditable(false);
        txtTrailerPlate.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtTrailerPlate.setName("txtTrailerPlate"); // NOI18N

        txtSling.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtSling.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtSling.setName("txtSling"); // NOI18N

        lblSling.setText(resourceMap.getString("lblSling.text")); // NOI18N
        lblSling.setName("lblSling"); // NOI18N

        lblPallet.setText(resourceMap.getString("lblPallet.text")); // NOI18N
        lblPallet.setName("lblPallet"); // NOI18N

        txtPallet.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtPallet.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtPallet.setName("txtPallet"); // NOI18N

        lblGRText.setText(resourceMap.getString("lblGRText.text")); // NOI18N
        lblGRText.setName("lblGRText"); // NOI18N

        txtGRText.setEditable(false);
        txtGRText.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtGRText.setName("txtGRText"); // NOI18N
        txtGRText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGRTextKeyReleased(evt);
            }
        });

        txtCementDesc.setText(resourceMap.getString("txtCementDesc.text")); // NOI18N
        txtCementDesc.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
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

        txtBatchProduce.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtBatchProduce.setName("txtBatchProduce"); // NOI18N
        txtBatchProduce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBatchProduceActionPerformed(evt);
            }
        });
        txtBatchProduce.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBatchProduceKeyReleased(evt);
            }
        });

        txtTicketId.setEditable(false);
        txtTicketId.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtTicketId.setName("txtTicketId"); // NOI18N

        lblTicketId.setText(resourceMap.getString("lblTicketId.text")); // NOI18N
        lblTicketId.setName("lblTicketId"); // NOI18N

        txtWeightTicketIdRef.setEditable(false);
        txtWeightTicketIdRef.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtWeightTicketIdRef.setName("txtWeightTicketIdRef"); // NOI18N

        lblWeightTicketIdRef.setText(resourceMap.getString("lblWeightTicketIdRef.text")); // NOI18N
        lblWeightTicketIdRef.setName("lblWeightTicketIdRef"); // NOI18N

        lblProcedure.setText(resourceMap.getString("lblProcedure.text")); // NOI18N
        lblProcedure.setName("lblProcedure"); // NOI18N

        txtProcedure.setEditable(false);
        txtProcedure.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtProcedure.setName("txtProcedure"); // NOI18N

        lblRemark.setText(resourceMap.getString("lblRemark.text")); // NOI18N
        lblRemark.setName("lblRemark"); // NOI18N

        txtRemark.setName("txtRemark"); // NOI18N
        txtRemark.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRemarkKeyReleased(evt);
            }
        });

        lblSalan.setText(resourceMap.getString("lblSalan.text")); // NOI18N
        lblSalan.setName("lblSalan"); // NOI18N

        txtSalan.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtSalan.setEnabled(false);
        txtSalan.setName("txtSalan"); // NOI18N

        lblLoadSource.setText(resourceMap.getString("lblLoadSource.text")); // NOI18N
        lblLoadSource.setName("lblLoadSource"); // NOI18N

        txtLoadSource.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtLoadSource.setEnabled(false);
        txtLoadSource.setName("txtLoadSource"); // NOI18N

        javax.swing.GroupLayout pnWTLeftLayout = new javax.swing.GroupLayout(pnWTLeft);
        pnWTLeft.setLayout(pnWTLeftLayout);
        pnWTLeftLayout.setHorizontalGroup(
            pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblRemark)
                    .addComponent(lblBatchProduce)
                    .addComponent(lblCementDesc)
                    .addComponent(lblRegCat)
                    .addComponent(lblRegistrationNo)
                    .addComponent(lblDName)
                    .addComponent(lblCMNDBL)
                    .addComponent(lblLicPlate)
                    .addComponent(lblSalan)
                    .addComponent(lblSling)
                    .addComponent(lblGRText)
                    .addComponent(lblLoadSource)
                    .addComponent(lblTicketId)
                    .addComponent(lblWeightTicketIdRef))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtRemark, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtWeightTicketIdRef, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtTicketId, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addGroup(pnWTLeftLayout.createSequentialGroup()
                        .addComponent(rbtInward)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtOutward)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblProcedure)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtProcedure, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE))
                    .addComponent(txtRegistrationNo, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtDName, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtCMNDBL, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addGroup(pnWTLeftLayout.createSequentialGroup()
                        .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtLicPlate, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSling, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTrailerPlate)
                            .addComponent(lblPallet))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTrailerPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                            .addComponent(txtPallet, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)))
                    .addComponent(txtSalan, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtGRText, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtCementDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtBatchProduce, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                    .addComponent(txtLoadSource, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE))
                .addGap(26, 26, 26))
        );
        pnWTLeftLayout.setVerticalGroup(
            pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRegCat)
                    .addComponent(rbtInward)
                    .addComponent(rbtOutward)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCMNDBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCMNDBL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTrailerPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblTrailerPlate))
                    .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtLicPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblLicPlate)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalan)
                    .addComponent(txtSalan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSling)
                    .addComponent(txtSling, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPallet)
                    .addComponent(txtPallet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGRText)
                    .addComponent(txtGRText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCementDesc)
                    .addComponent(txtCementDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatchProduce)
                    .addComponent(txtBatchProduce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoadSource)
                    .addComponent(txtLoadSource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTicketId)
                    .addComponent(txtTicketId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWeightTicketIdRef)
                    .addComponent(txtWeightTicketIdRef, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRemark)
                    .addComponent(txtRemark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnWTRight.setName("pnWTRight"); // NOI18N

        txtDelNum.setEditable(false);
        txtDelNum.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtDelNum.setName("txtDelNum"); // NOI18N

        txtPONo.setEditable(false);
        txtPONo.setAction(actionMap.get("readPO")); // NOI18N
        txtPONo.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
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
        txtRegItem.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtRegItem.setName("txtRegItem"); // NOI18N

        txtMatnr.setEditable(false);
        txtMatnr.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtMatnr.setName("txtMatnr"); // NOI18N

        txtWeight.setEditable(false);
        txtWeight.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtWeight.setName("txtWeight"); // NOI18N

        txtLgortIn.setEditable(false);
        txtLgortIn.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtLgortIn.setName("txtLgortIn"); // NOI18N
        txtLgortIn.setRequestFocusEnabled(false);

        txtChargIn.setEditable(false);
        txtChargIn.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtChargIn.setName("txtChargIn"); // NOI18N

        lblVendorTransport.setText(resourceMap.getString("lblVendorTransport.text")); // NOI18N
        lblVendorTransport.setName("lblVendorTransport"); // NOI18N

        lblVendorLoading.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblVendorLoading.setText(resourceMap.getString("lblVendorLoading.text")); // NOI18N
        lblVendorLoading.setName("lblVendorLoading"); // NOI18N

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

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        txtSO.setEditable(false);
        txtSO.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtSO.setName("txtSO"); // NOI18N

        lblSO.setText(resourceMap.getString("lblSO.text")); // NOI18N
        lblSO.setName("lblSO"); // NOI18N

        txtVendorLoading.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtVendorLoading.setEnabled(false);
        txtVendorLoading.setName("txtVendorLoading"); // NOI18N

        txtVendorTransport.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtVendorTransport.setEnabled(false);
        txtVendorTransport.setName("txtVendorTransport"); // NOI18N

        txtCharg.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtCharg.setEnabled(false);
        txtCharg.setName("txtCharg"); // NOI18N

        txtCustomer.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtCustomer.setEnabled(false);
        txtCustomer.setName("txtCustomer"); // NOI18N

        txtSLoc.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
        txtSLoc.setEnabled(false);
        txtSLoc.setName("txtSLoc"); // NOI18N

        lblPoPosto.setText(resourceMap.getString("lblPoPosto.text")); // NOI18N
        lblPoPosto.setName("lblPoPosto"); // NOI18N

        txtPoPosto.setEditable(false);
        txtPoPosto.setDisabledTextColor(resourceMap.getColor("txtGRText.disabledTextColor")); // NOI18N
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

        javax.swing.GroupLayout pnWTRightLayout = new javax.swing.GroupLayout(pnWTRight);
        pnWTRight.setLayout(pnWTRightLayout);
        pnWTRightLayout.setHorizontalGroup(
            pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTRightLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblPoPosto)
                    .addComponent(lblRegItem)
                    .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(lblMatnr)
                        .addComponent(lbKunnr, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(lblWeight)
                    .addComponent(lblLgortIn)
                    .addComponent(lblCharg)
                    .addComponent(lblChargIn)
                    .addComponent(lblSLoc)
                    .addComponent(lblVendorLoading)
                    .addComponent(lblVendorTransport)
                    .addComponent(lblSO)
                    .addComponent(lblPONo)
                    .addComponent(lblDelNum))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSO, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtDelNum, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtPONo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtPoPosto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtMatnr, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnWTRightLayout.createSequentialGroup()
                        .addComponent(txtWeight, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtSLoc, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtLgortIn, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtCharg, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtChargIn, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtRegItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtVendorTransport, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE)
                    .addComponent(txtVendorLoading, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnWTRightLayout.setVerticalGroup(
            pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSO))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDelNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDelNum))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPONo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPONo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPoPosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPoPosto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                    .addComponent(txtMatnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMatnr))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(lblWeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbKunnr)
                    .addComponent(txtCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtLgortIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLgortIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCharg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCharg))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtChargIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblChargIn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVendorLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorLoading))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtVendorTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorTransport))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setName("jPanel1"); // NOI18N

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(708, Short.MAX_VALUE)
                .addComponent(btnPostAgain)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReprint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnSave)
                .addComponent(btnReprint)
                .addComponent(btnPostAgain))
        );

        javax.swing.GroupLayout pnWTicketLayout = new javax.swing.GroupLayout(pnWTicket);
        pnWTicket.setLayout(pnWTicketLayout);
        pnWTicketLayout.setHorizontalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTicketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(pnWTLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnWTRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnWTicketLayout.setVerticalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTicketLayout.createSequentialGroup()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnWTLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnWTRight, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 675, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnWTFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnScaleData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnWTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                    configuration.getWb1AutoSignal(),
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
                    configuration.getWb2AutoSignal(),
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
        // enable button print
        btnReprint.setEnabled(true);
        grbBridge.clearSelection();
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_btnOScaleResetActionPerformed

    private void txtGRTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRTextKeyReleased
        if (weightTicket != null) {
            weightTicket.setNote(txtGRText.getText().trim());
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtGRTextKeyReleased

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
            weightTicket.setPosted(false);
            weightTicketController.savePostAgainActionPerformed(weightTicket);
            flgPost = true;
        } else {
            setSaveNeeded(false);
        }
    }//GEN-LAST:event_btnPostAgainActionPerformed

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


    }//GEN-LAST:event_txtWTNumFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
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

    private void txtPoPostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPoPostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPoPostoActionPerformed

    private void txtPoPostoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPoPostoKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPoPostoKeyReleased

    private void txtRemarkKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRemarkKeyReleased
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtRemarkKeyReleased

    private void txtBatchProduceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBatchProduceActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtBatchProduceActionPerformed

    private void txtBatchProduceKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBatchProduceKeyReleased
        if (weightTicket != null) {
            weightTicket.setBatch(txtBatchProduce.getText());
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtBatchProduceKeyReleased

    @Action
    public void showMB1BOption() {
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMvt311() {
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtPO() {

        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMisc() {
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
        if ((txtPONo.getText() != null && !"".equals(txtPONo.getText())) && purOrder != null) {
            if (purOrder.getDocType() != null && purOrder.getDocType().equals("UB")) {
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
        checkWTNum(txt);
        if (isEnteredValidWTNum()) {
            return new ReadWTTask(WeighBridgeApp.getApplication(), txt);
        } else {
            clearForm();
            return null;
        }
    }

    private void checkWTNum(String wtNum) {
        boolean valid = wtNum.matches("^[a-zA-Z0-9]{10}$");  //
        setEnteredValidWTNum(valid);
        if (valid) {
            lblWTNum.setForeground(Color.BLACK);
        } else {
            lblWTNum.setForeground(Color.RED);
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
        if (txtCharg.getText().trim().isEmpty()) {
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
            if (!weightTicket.isDissolved()) {
                return new ReprintWTTask(WeighBridgeApp.getApplication());
            } else {
                JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.ticketDestroy"));
                return null;
            }
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
            OutboundDelivery outdel_tmp = null;
            List<OutboundDeliveryDetail> details_list = new ArrayList<>();
            OutboundDeliveryDetail item = null;
            Material mat_tmp = null;
            Boolean ximang_tmp = false;
            Boolean flag_tmp = true;

            weightTicket.setNote(txtGRText.getText().trim());
            weightTicket.setRemark(txtRemark.getText().trim());
            for (int i = 0; i < outbDel_list.size(); i++) {
                outdel_tmp = outbDel_list.get(i);
                try {
                    mat_tmp = weightTicketController.checkPOSTO(outdel_tmp.getMatnr());
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
                String Mes = resourceMapMsg.getString("msg.errorPointReceive");
                JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), Mes.toString());
            }
        }
        if (valid) {
            //------------------------
            String wplant = configuration.getWkPlant();
            try {
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
            checkWTNum(text);
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
    private boolean checkPlant = false;

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

    private void setCanCopyField() {
        txtLicPlate.setEnabled(true);
        txtLicPlate.setEditable(false);
        txtLicPlate.setBackground(new Color(222, 225, 229));

        txtSO.setEnabled(true);
        txtSO.setEditable(false);
        txtSO.setBackground(new Color(222, 225, 229));

        txtDelNum.setEnabled(true);
        txtDelNum.setEditable(false);
        txtDelNum.setBackground(new Color(222, 225, 229));

        txtPONo.setEnabled(true);
        txtPONo.setEditable(false);
        txtPONo.setBackground(new Color(222, 225, 229));

        txtPoPosto.setEnabled(true);
        txtPoPosto.setEditable(false);
        txtPoPosto.setBackground(new Color(222, 225, 229));
    }

    private class ReadWTTask extends Task<Object, Void> {

        String id;

        ReadWTTask(Application app, String id) {
            super(app);
            this.id = id;
            setReprintable(false);
            grbType.clearSelection();
            grbCat.clearSelection();
            config = WeighBridgeApp.getApplication().getConfig();
            btnSave.setEnabled(false);

        }

        @Override
        protected Object doInBackground() throws Exception {
            weightTicket = weightTicketController.findWeightTicket(weightTicket, id);
            if (weightTicket == null) {
                failed(new Exception(resourceMapMsg.getString("msg.notTicketNo", txtWTNum.getText())));
            } else {
                //show hide text and label
                switchShowHideMode(weightTicket.getMode());

                setCanCopyField();

                lblPONo.setText(resourceMapMsg.getString("lblPONo.text"));
                lblPoPosto.setText(resourceMapMsg.getString("lblPoPosto.text"));
                lblSLoc.setText(resourceMapMsg.getString("lblSLoc.text"));
                lblCharg.setText(resourceMapMsg.getString("lblCharg.text"));
                lblLicPlate.setText(resourceMapMsg.getString("lblLicPlate.text"));

                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())) {
                    lblPONo.setText(resourceMapMsg.getString("lblPoTrans"));
                    lblPoPosto.setText(resourceMapMsg.getString("lblPostoLoad"));
                    lblSLoc.setText(resourceMapMsg.getString("lblSlocOut"));
                    lblCharg.setText(resourceMapMsg.getString("lblBatchStockOut"));
                }

                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())) {
                    lblPONo.setText(resourceMapMsg.getString("lblPoOut"));
                    lblPoPosto.setText(resourceMapMsg.getString("lblPostoIn"));
                }

                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_WATERWAY.name().equals(weightTicket.getMode())) {
                    lblLicPlate.setText(resourceMapMsg.getString("lblPlateNoWater"));
                }

                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())) {
                    lblLicPlate.setText(resourceMapMsg.getString("lblVehicleNo"));
                }

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
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_OTHER.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_OTHER.name().equals(weightTicket.getMode())) {
                    txtSling.setText(NumberFormat.getInstance().format(weightTicket.getSling()));
                    txtPallet.setText(NumberFormat.getInstance().format(weightTicket.getPallet()));
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
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.IN_WAREHOUSE_TRANSFER.name().equals(weightTicket.getMode())) {
                    txtLoadSource.setText(weightTicket.getLoadSource());
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

                if (WeighBridgeApp.getApplication().isOfflineMode()) {
                    txtRegItem.setText(weightTicket.getWeightTicketDetail().getRegItemDescription());
                }
                txtWeight.setText(df.format(weightTicket.getWeightTicketDetail().getRegItemQuantity()).toString());
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())) {
                    txtPoPosto.setText(weightTicket.getPosto());
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_PLANT_PLANT.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_PULL_STATION.name().equals(weightTicket.getMode())
                        || Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())) {
                    Vendor vendorLoading = weightTicketRegistarationController.getVendor(weightTicket.getWeightTicketDetail().getLoadVendor());
                    txtVendorLoading.setText(vendorLoading != null ? vendorLoading.getName1() + " " + vendorLoading.getName2() : "");
                    Vendor vendorTransport = weightTicketRegistarationController.getVendor(weightTicket.getWeightTicketDetail().getTransVendor());
                    txtVendorTransport.setText(vendorTransport != null ? vendorTransport.getName1() + " " + vendorTransport.getName2() : "");
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SLOC_SLOC.name().equals(weightTicket.getMode())) {
                    if (weightTicket.getRecvLgort() != null && !weightTicket.getRecvLgort().trim().isEmpty()) {
                        entityManager.clear();
                        SLoc sloc = weightTicketController.findByLgort(weightTicket.getRecvLgort());
                        if (sloc != null) {
                            txtLgortIn.setText(sloc.getLgort() + " - " + sloc.getLgobe());
                        }
                    } else {
                        txtSLoc.setText(null);
                    }

                    txtChargIn.setText(weightTicket.getRecvCharg());
                }
                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_WATERWAY.name().equals(weightTicket.getMode())) {
                    String[] soNums = weightTicket.getWeightTicketDetails().stream().map(t -> t.getSoNumber()).toArray(String[]::new);
                    txtSO.setText(String.join(" - ", soNums));
                }
                // <editor-fold defaultstate="collapsed" desc="Determine state of Weight Ticket">
                setStage1(false);
                setStage2(false);
                if (weightTicket.getFScale() == null && weightTicket.getSScale() == null) {
                    setStage1(true);
                } else if (weightTicket.getFScale() != null && weightTicket.getSScale() == null) {
                    setStage2(true);
                }
                OutboundDelivery od = null;
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Load D.O/P.O details">
                List<WeightTicketDetail> weightTicketDetails = weightTicket.getWeightTicketDetails();
                List<String> do_list = new ArrayList<>();
                BigDecimal totalRegItemQuantity = BigDecimal.ZERO;
                for (WeightTicketDetail weightTicketDetail : weightTicketDetails) {
                    if ((weightTicketDetail.getDeliveryOrderNo() == null || weightTicketDetail.getDeliveryOrderNo().trim().isEmpty())
                            || (!weightTicket.isPosted() && (!weightTicket.getMode().equals("IN_WAREHOUSE_TRANSFER") && weightTicketDetail.getEbeln() != null && !weightTicketDetail.getEbeln().trim().isEmpty()))
                            || (weightTicketDetail.getDeliveryOrderNo() == null && weightTicketDetail.getEbeln() == null)) {
                        setWithoutDO(true);
                    } else {
                        List<OutboundDeliveryDetail> odt = null;
                        do_list.add(weightTicketDetail.getDeliveryOrderNo());
                        totalRegItemQuantity = totalRegItemQuantity.add(weightTicketDetail.getRegItemQuantity());
                        try {
                            od = weightTicketController.findByMandtOutDel(weightTicketDetail.getDeliveryOrderNo());
                        } catch (Exception ex) {
//                        java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (od == null && weightTicketDetail.getEbeln() != null) {
                            try {
                                od = sapService.getOutboundDelivery(weightTicketDetail.getDeliveryOrderNo());
                                if (od != null) {
                                    if (!entityManager.getTransaction().isActive()) {
                                        entityManager.getTransaction().begin();
                                    }
                                    entityManager.persist(od);
                                    entityManager.getTransaction().commit();
                                }
                                odt = weightTicketRegistarationController.findByMandtDelivNumb(weightTicketDetail.getDeliveryOrderNo());
                            } catch (Exception ex) {
//                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if ((odt != null) && (odt.size() > 0)) {
                                OutboundDeliveryDetail item = odt.get(0);
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
                        txtMatnr.setText(weightTicketDetail.getMatnrRef());
                        setWithoutDO(false);
                    }
                }

                if (WeighBridgeApp.getApplication().isOfflineMode()
                        && od == null) {
                    setWithoutDO(true);
                }

                if (!isWithoutDO()) {
                    //xu ly nhieu DO trong WT
                    List<OutboundDeliveryDetail> details_list = new ArrayList<>();
                    OutboundDeliveryDetail item = null;
                    String doNums = "";
                    String regItemDescription = "";
                    outbDel_list.clear();
                    outDetails_lits.clear();
                    for (int index = 0; index < do_list.size(); index++) {
                        WeightTicketDetail weightTicketDetail = weightTicketDetailRepository.findBydeliveryOrderNo(do_list.get(index));
                        try {
                            outbDel = weightTicketController.findByMandtOutDel(do_list.get(index));
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                            try {
                                OutboundDelivery sapOutbDel = sapService.getOutboundDelivery(do_list.get(index));
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
                                    sapOutbDel.setWeightTicketId(outbDel.getWeightTicketId());
                                    if (!entityManager.getTransaction().isActive()) {
                                        entityManager.getTransaction().begin();
                                    }
                                    entityManager.merge(sapOutbDel);
                                    outbDel = sapOutbDel;
                                    entityManager.getTransaction().commit();
                                    entityManager.clear();
                                }
                            } catch (Exception ex) {
                            }
                        }
                        if (outbDel != null) {
                            outbDel_list.add(outbDel);
                            total_qty_goods = total_qty_goods.add(outbDel.getLfimg());
                            if (doNums.equals("")) {
                                doNums = weightTicketDetail.getDeliveryOrderNo();
                            } else {
                                doNums += " - " + weightTicketDetail.getDeliveryOrderNo();
                            }
                            for (OutboundDeliveryDetail outboundDeliveryDetail : outbDel.getOutboundDeliveryDetails()) {
                                if (regItemDescription.isEmpty()) {
                                    regItemDescription = outboundDeliveryDetail.getArktx();
                                } else if (!regItemDescription.contains(outboundDeliveryDetail.getArktx())) {
                                    regItemDescription += " - " + outboundDeliveryDetail.getArktx();
                                }
                            }
                        }

                        try {
                            //get list outdetails
                            details_list = weightTicketRegistarationController.findByMandtDelivNumb(do_list.get(index));
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        for (int j = 0; j < details_list.size(); j++) {
                            item = details_list.get(j);
                            outDetails_lits.add(item);
                        }
                    }

                    if (outbDel_list.size() > 1) {
                        txtWeight.setText(df.format(totalRegItemQuantity).toString());
                    }

                    txtDelNum.setText(doNums);
                    txtRegItem.setText(regItemDescription);

                } else if (weightTicket.getWeightTicketDetail().getEbeln() != null && !weightTicket.getWeightTicketDetail().getEbeln().trim().isEmpty()) {
                    purOrder = weightTicketController.findByPoNumber(weightTicket.getWeightTicketDetail().getEbeln());
                    txtPONo.setText(weightTicket.getWeightTicketDetail().getEbeln());
                    setValidPONum(true);
                    setSubContract(false);
                    PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                    txtRegItem.setText(weightTicket.getWeightTicketDetail().getRegItemDescription());
                    if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3') {
                        setSubContract(true);
                    }
                }
                if ((Constants.WeighingProcess.MODE_DETAIL.IN_OTHER.name().equals(weightTicket.getMode()))
                        || (Constants.WeighingProcess.MODE_DETAIL.OUT_OTHER.name().equals(weightTicket.getMode()))) {
                    MaterialInternal mat = materialInternalRepository.findByMatnr(weightTicket.getRecvMatnr());
                    if (mat != null) {
                        txtRegItem.setText(mat.getMaktx());
                    }
                }

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
                    txfGoodsQty.setValue(weightTicket.getGQty());
                } else {
                    txfOutQty.setValue(null);
                    txtOutTime.setText(null);
                    txfGoodsQty.setValue(null);
                }
                txtDName.setText(weightTicket.getDriverName());
                txtCMNDBL.setText(weightTicket.getDriverIdNo());
                txtLicPlate.setValue(weightTicket.getPlateNo());
                txtTrailerPlate.setValue(weightTicket.getTrailerId());
                txtSalan.setText(weightTicket.getChargEnh());
                txtCustomer.setText(null);
                String kunnr = weightTicket.getWeightTicketDetail().getKunnr();
                    if (kunnr != null && !kunnr.trim().isEmpty()) {
                         entityManager.clear();
                        if(Constants.WeighingProcess.MODE_DETAIL.IN_PO_PURCHASE.name().equals(weightTicket.getMode())) {
                            Vendor custVendor = weightTicketRegistarationController.findByLifnrIsCustomer(weightTicket.getWeightTicketDetail().getKunnr());
                            if(custVendor != null) {
                                txtCustomer.setText(custVendor.getName1() + " " + custVendor.getName2());
                            }
                        } else {
                            Customer cust = weightTicketRegistarationController.findByKunnr(weightTicket.getWeightTicketDetail().getKunnr());
                            if(cust != null) {
                                String name = cust.getName2();
                                if (!StringUtil.isEmptyString(cust.getName3())) {
                                    name += " " + cust.getName3();
                                }
                                if (!StringUtil.isEmptyString(cust.getName4())) {
                                    name += " " + cust.getName4();
                                }
                                txtCustomer.setText(name);
                            }
                        }
                    
                }
                if ((WeighBridgeApp.getApplication().isOfflineMode()
                        && !weightTicket.isPosted())
                        || (!WeighBridgeApp.getApplication().isOfflineMode()
                        && !weightTicket.isPosted())) {
                    if (weightTicket.getRegType() == 'O' && weightTicket.getWeightTicketDetail().getEbeln() == null) {
                        txtCustomer.setEnabled(true);
                    }
                }
                

                String lgort = null;
                if (weightTicket.getLgort() != null && !weightTicket.getLgort().trim().isEmpty()) {
                    entityManager.clear();
                    SLoc sloc = weightTicketController.findByLgort(weightTicket.getLgort());
                    if (sloc != null) {
                        lgort = sloc.getLgort();
                        txtSLoc.setText(sloc.getLgort() + " - " + sloc.getLgobe());
                    }
                } else if (outbDel != null && outbDel.getLgort() != null && !outbDel.getLgort().trim().isEmpty()) {
                    SLoc sloc = weightTicketController.findByLgort(outbDel.getLgort());
                    if (sloc != null) {
                        txtSLoc.setText(sloc.getLgort() + " - " + sloc.getLgobe());
                        lgort = sloc.getLgort();
                    }
                } else {
                    txtSLoc.setText(null);
                }
                txtGRText.setText(weightTicket.getNote());
//                if (Constants.WeighingProcess.MODE_DETAIL.OUT_SELL_ROAD.name().equals(weightTicket.getMode())
//                        || Constants.WeighingProcess.MODE_DETAIL.IN_WAREHOUSE_TRANSFER.name().equals(weightTicket.getMode())) {
//                    txtDelNum.setText(weightTicket.getWeightTicketDetail().getDeliveryOrderNo());
//                }
//                setDissolved(chkDissolved.isSelected());
                if (isDissolved() || (!isStage1() && !isStage2() && weightTicket.isPosted())) {
                    setValidPONum(false);
                    setWithoutDO(false);
//                    txtGRText.setEnabled(false);
                    txtCharg.setEditable(false);
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
                txtCharg.setText(null);
                if (lgort != null && !lgort.isEmpty()
                        && ((weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty())
                        || (!isWithoutDO() && outbDel != null && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()))
                        && weightTicket.getWeightTicketDetail().getMatnrRef() != null && !weightTicket.getWeightTicketDetail().getMatnrRef().trim().isEmpty()) {
                    BatchStock batch = null;
                    if (weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty()) {
                        batch = weightTicketController.findByWerksLgortMatnrCharg(configuration.getWkPlant(), lgort, weightTicket.getWeightTicketDetail().getMatnrRef(), weightTicket.getCharg());
                    } else if (!isWithoutDO() && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()) {
                        batch = weightTicketController.findByWerksLgortMatnrCharg(configuration.getWkPlant(), lgort, weightTicket.getWeightTicketDetail().getMatnrRef(), outbDel.getCharg());
                    }
                    if (batch != null) {
                        txtCharg.setText(batch.getCharg());
                    } else if (weightTicket.getCharg() != null && txtCharg.isEditable()) {
                        txtCharg.setText(weightTicket.getCharg());
                    } else {
                        txtCharg.setText(null);
                    }
                }
                //disnable drowdownlist
                txtCustomer.setEnabled(false);
                txtSLoc.setEnabled(false);
                txtCharg.setEnabled(false);
                txtVendorLoading.setEnabled(false);
                txtVendorTransport.setEnabled(false);
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
                if (!ExceptionUtil.isDatabaseDisconnectedException(cause)) {
                    if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                        cause = cause.getCause();
                    }
                    logger.error(null, cause);
                    JOptionPane.showMessageDialog(rootPane,
                            (cause.getMessage() == null || cause.getMessage().isEmpty())
                            ? "Null Pointer Exception" : cause.getMessage());
                }
            }

            clearForm();
        }

        @Override
        protected void finished() {
            if (weightTicket != null) {
                if (!weightTicket.isPosted() && !isStage1() && !isStage2()) {
                    btnPostAgain.setEnabled(true);
                } else {
                    btnPostAgain.setEnabled(false);
                }

                if (weightTicket.isPosted()) {
                    btnSave.setEnabled(false);
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
        txtRemark.setEnabled(true);
        if (ModeEnum.IN_PO_PURCHASE.name().equals(mode)) {
            lblWeightTicketIdRef.setVisible(false);
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtSO.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
        }
        if (ModeEnum.IN_WAREHOUSE_TRANSFER.name().equals(mode)) {

            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);

            txtSO.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
        }
        if (ModeEnum.IN_OTHER.name().equals(mode) || ModeEnum.OUT_OTHER.name().equals(mode)) {
            lblDelNum.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtSO.setVisible(false);
            txtDelNum.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
        }
        if (ModeEnum.OUT_SELL_ROAD.name().equals(mode)) {
            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblSO.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtSO.setVisible(false);
            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
        }
        if (ModeEnum.OUT_PLANT_PLANT.name().equals(mode)) {
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblSO.setVisible(false);

            txtSO.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);

        }
        if (ModeEnum.OUT_SLOC_SLOC.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lblSO.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtSO.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
        }
        if (ModeEnum.OUT_PULL_STATION.name().equals(mode)) {
            lblSling.setVisible(false);
            lblPallet.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            lblDelNum.setVisible(false);
            lblMatnr.setVisible(false);
            lblSO.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtSO.setVisible(false);
            txtSling.setVisible(false);
            txtPallet.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            txtDelNum.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
        }
        if (ModeEnum.OUT_SELL_WATERWAY.name().equals(mode)) {

            lblTicketId.setVisible(false);
            lblWeightTicketIdRef.setVisible(false);
            //so SO
            lblPONo.setVisible(false);
            lblMatnr.setVisible(false);
            lblLgortIn.setVisible(false);
            lblChargIn.setVisible(false);
            lblPoPosto.setVisible(false);
            lblVendorLoading.setVisible(false);
            lblVendorTransport.setVisible(false);
            lblLoadSource.setVisible(false);
            txtLoadSource.setVisible(false);

            txtTicketId.setVisible(false);
            txtWeightTicketIdRef.setVisible(false);
            //so SO
            txtPONo.setVisible(false);
            txtMatnr.setVisible(false);
            txtLgortIn.setVisible(false);
            txtChargIn.setVisible(false);
            txtPoPosto.setVisible(false);
            txtVendorLoading.setVisible(false);
            txtVendorTransport.setVisible(false);
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
            txtCustomer.setText(null);
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
                } else if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3') {
                    purchaseOrderDetail.setShortText(Constants.WeightTicketView.ITEM_DESCRIPTION);
                    purchaseOrderDetail.setPoUnit(weightTicketRegistarationController.getUnit().getPurchaseUnit());
                    setValidPONum(true);
                    setSubContract(true);
                } else {
                    setValidPONum(true);
                }
            }
            return null;
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
            return null;
        }
    }

    private class SaveWTTask extends Task<Object, Void> {

        SAPSession sapSession = null;
        Object objBapi = null;
        Object objBapi_Po = null;
        Object objBapi_Posto = null;
        boolean completed = true;
        String bapi_message = "";
        List<String> bapi_messages = new ArrayList<>();
        List<String> bapi_messagesSloc = new ArrayList<>();
        List<String> bapi_messagesPo = new ArrayList<>();
        List<String> bapi_messagesPosto = new ArrayList<>();

        SaveWTTask(org.jdesktop.application.Application app) {
            super(app);
            btnSave.setEnabled(false);
            sapSession = WeighBridgeApp.getApplication().getSAPSession();
            setSaveNeeded(false);
        }

        @Override
        protected Object doInBackground() {
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            Date now = new Date();
            weightTicket.setUpdatedDate(now);
            List<WeightTicketDetail> weightTicketDetails = weightTicket.getWeightTicketDetails();
            for (WeightTicketDetail wtDetail : weightTicketDetails) {
                wtDetail.setUpdatedDate(now);
            }
            entityManager.merge(weightTicket);
            OutboundDelivery outbDel = null;
            List<String> completedDO = new ArrayList<>();
            String modeFlg = null;
            boolean flgGqty = false;

            if (((isStage1()) && checkPlant) || ((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
                    || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
                    && (weightTicket != null && !weightTicket.isPosted()))) {

                // <editor-fold defaultstate="collapsed" desc="Input PO">
                if (weightTicket.getWeightTicketDetail().getEbeln() != null) {
                    WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                    purchaseOrder = purchaseOrderRepository.findByPoNumber(weightTicket.getWeightTicketDetail().getEbeln());
                    // nhap mua hang
                    if (weightTicket.getMode().equals("IN_PO_PURCHASE")) {
                        objBapi = getGrPoMigoBapi(weightTicket, purchaseOrder);
                    }
                    // mode xuat plant
                    if (weightTicket.getMode().equals("OUT_PLANT_PLANT")) {
                        // check post PGI for post lai
                        if (StringUtil.isEmptyString(weightTicketDetail.getDeliveryOrderNo())
                                && StringUtil.isEmptyString(weightTicketDetail.getMatDoc())) {
                            objBapi = getDoCreate2PGI(weightTicket, outbDel);
                        } else {
                            objBapi = getDOPostingPGI(weightTicket, outbDel, weightTicketDetail.getDeliveryOrderNo());
                        }
                    }
                    // chuyen kho noi bo
                    if (weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                        objBapi = getGiMB1BBapi(weightTicket);
                        objBapi_Po = getGrPoMigoBapi(weightTicket, purchaseOrder);
                        if (weightTicket.getPosto() != null) {
                            purchaseOrder = purchaseOrderRepository.findByPoNumber(weightTicket.getPosto());
                            objBapi_Posto = getGrPoMigoBapi(weightTicket, purchaseOrder);
                        }
                    }
                    // xuat ben keo
                    if (weightTicket.getMode().equals("OUT_PULL_STATION") && weightTicket.getPosto() != null) {
                        // check post PGI for post lai
                        if (StringUtil.isEmptyString(weightTicketDetail.getDeliveryOrderNo())
                                && StringUtil.isEmptyString(weightTicketDetail.getMatDoc())) {
                            objBapi = getMvtPOSTOCreatePGI(weightTicket, weightTicket.getPosto(), flgPost);
                        } else {
                            objBapi = getDOPostingPGI(weightTicket, outbDel, weightTicketDetail.getDeliveryOrderNo());
                        }
                    }

                    if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                        if (objBapi != null) {
                            try {
                                if (!weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                                    logger.info("[SAP] Get infor before post SAP: " + objBapi.toString());
                                    sapSession.execute(objBapi);
                                }
                                // check chuyen kho noi bo for post lai
                                if (weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                                    if (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")) {
                                        logger.info("[SAP] Get infor before post SAP: " + objBapi.toString());
                                        sapSession.execute(objBapi);
                                    }
                                    if ((objBapi_Po != null)
                                            && (weightTicketDetail.getMatDocGr() == null || weightTicketDetail.getMatDocGr().equals(""))) {
                                        logger.info("[SAP] Get infor before post SAP: " + objBapi_Po.toString());
                                        sapSession.execute(objBapi_Po);
                                    }
                                    if ((objBapi_Posto != null)
                                            && (weightTicketDetail.getMatDocGi() == null || weightTicketDetail.getMatDocGi().equals(""))) {
                                        logger.info("[SAP] Get infor before post SAP: " + objBapi_Posto.toString());
                                        sapSession.execute(objBapi_Posto);
                                    }
                                }
                                if (objBapi instanceof DOCreate2PGIBapi) {
                                    weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
                                    weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_messages = ((DOCreate2PGIBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }
                                if ((objBapi instanceof GoodsMvtPoCreateBapi)
                                        && (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals(""))) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        if (weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                                            bapi_messagesSloc = ((GoodsMvtPoCreateBapi) objBapi).getReturnMessage();
                                        } else {
                                            bapi_messages = ((GoodsMvtPoCreateBapi) objBapi).getReturnMessage();
                                        }
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }
                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));

                                    try {
                                        bapi_messages = ((GoodsMvtDoCreateBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));

                                    try {
                                        bapi_messages = ((WsDeliveryUpdateBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }

                                }

                                if ((objBapi_Po instanceof GoodsMvtPoCreateBapi)
                                        && (weightTicketDetail.getMatDocGr() == null || weightTicketDetail.getMatDocGr().equals(""))) {
                                    weightTicketDetail.setMatDocGr(((GoodsMvtPoCreateBapi) objBapi_Po).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtPoCreateBapi) objBapi_Po).getMatYear()));
                                    try {
                                        bapi_messagesPo = ((GoodsMvtPoCreateBapi) objBapi_Po).getReturnMessage();
                                    } catch (Exception Ex) {
                                        //bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }

                                if (objBapi instanceof GoodsMvtPOSTOCreatePGIBapi) {
                                    weightTicketDetail.setDeliveryOrderNo(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getDelivery());
                                    weightTicketDetail.setMatDoc(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMatYear()));
                                    weightTicketDetail.setIvMaterialDocument((((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMaterialDocumentOut()));
                                    weightTicketDetail.setIvMatDocumentYear(((GoodsMvtPOSTOCreatePGIBapi) objBapi).getMatDocumentYearOut());

                                    try {
                                        bapi_messages = ((GoodsMvtPOSTOCreatePGIBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }

                                if (objBapi instanceof DOPostingPGIBapi) {
                                    weightTicketDetail.setMatDoc(((DOPostingPGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((DOPostingPGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_messages = ((DOPostingPGIBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }

                                if ((objBapi_Posto instanceof GoodsMvtPoCreateBapi)
                                        && (weightTicketDetail.getMatDocGi() == null || weightTicketDetail.getMatDocGi().equals(""))) {
                                    weightTicketDetail.setMatDocGi(((GoodsMvtPoCreateBapi) objBapi_Posto).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtPoCreateBapi) objBapi_Posto).getMatYear()));
                                    try {
                                        bapi_messagesPosto = ((GoodsMvtPoCreateBapi) objBapi_Posto).getReturnMessage();
                                    } catch (Exception Ex) {
                                        //bapi_messagesPosto.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                }

                                if (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")
                                        || ((objBapi_Po != null) && (weightTicketDetail.getMatDocGr() == null || weightTicketDetail.getMatDocGr().equals("")))
                                        || ((objBapi_Posto != null) && (weightTicketDetail.getMatDocGi() == null || weightTicketDetail.getMatDocGi().equals("")))) {
                                    revertCompletedDO(completedDO, null, null);
                                    weightTicket.setPosted(false);

                                    if (weightTicket.getMode().equals("OUT_SLOC_SLOC")) {
                                        if (bapi_messagesSloc.isEmpty() && bapi_messagesPo.isEmpty() && bapi_messagesPosto.isEmpty()) {
                                            bapi_messagesSloc.add(resourceMapMsg.getString("msg.errorSAP"));
                                        }
                                        bapi_messagesSloc.forEach(msg -> JOptionPane.showMessageDialog(rootPane, msg));
                                        if (!bapi_messagesPo.isEmpty()) {
                                            bapi_messagesPo.forEach(msg -> JOptionPane.showMessageDialog(rootPane, msg));
                                        }
                                        if (!bapi_messagesPosto.isEmpty()) {
                                            bapi_messagesPosto.forEach(msg -> JOptionPane.showMessageDialog(rootPane, msg));
                                        }
                                    } else {
                                        if (bapi_messages.isEmpty()) {
                                            bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                        }
                                        bapi_messages.forEach(msg -> JOptionPane.showMessageDialog(rootPane, msg));
                                    }
                                    completed = false;
                                    entityManager.clear();
                                } else if ((weightTicketDetail.getMatDoc() != null) && (!weightTicketDetail.getMatDoc().equals(""))
                                        && ((objBapi_Po == null) || (objBapi_Po != null && weightTicketDetail.getMatDocGr() != null && !weightTicketDetail.getMatDocGr().equals("")))
                                        || ((objBapi_Posto == null) || (objBapi_Posto != null && weightTicketDetail.getMatDocGi() != null && !weightTicketDetail.getMatDocGi().equals("")))) {
                                    weightTicketDetail.setPosted(true);
                                    weightTicket.setPosted(true);
                                    completedDO.add(weightTicketDetail.getDeliveryOrderNo());
                                }

                            } catch (Exception ex) {
                                logger.error(ex);
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

                                weightTicketDetail.setPosted(false);
                                weightTicket.setPosted(false);
                                failed(ex);
                                completed = false;
                                entityManager.clear();
                            }
                        } else if (WeighBridgeApp.getApplication().isOfflineMode()) {
                            weightTicketDetail.setPosted(false);
                            weightTicket.setPosted(false);
                            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
                        } else {
                            weightTicket.setPosted(false);
                        }
                    } else {
                        bapi_messages.add(resourceMapMsg.getString("msg.postOfflien"));
                        weightTicketDetail.setPosted(false);
                        weightTicket.setPosted(false);
                        weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
                    }
                }
                // </editor-fold>

                // <editor-fold defaultstate="collapsed" desc="Input DO">
                BigDecimal sumQtyReg = BigDecimal.ZERO;
                // sum trọng lượng đăng ký
                for (int i = 0; i < outbDel_list.size(); i++) {
                    outbDel = outbDel_list.get(i);
                    sumQtyReg = sumQtyReg.add(outbDel.getLfimg());
                }
                
                // update DO list
                for (int i = 0; i < outbDel_list.size(); i++) {
                    try {
                        outbDel = weightTicketController.findByMandtOutDel(outbDel_list.get(i).getDeliveryOrderNo());
                        outbDel_list.set(i, outbDel);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }

                // post SAP
                String ivWbidNosave = "";
                for (int i = 0; i < outbDel_list.size(); i++) {
                    try {
                        outbDel = outbDel_list.get(i);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                    if (i != 0) {
                        ivWbidNosave = "X";
                    }

                    WeightTicketDetail weightTicketDetail = new WeightTicketDetail();
                    for (WeightTicketDetail wtDetail : weightTicketDetails) {
                        if (wtDetail.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                            weightTicketDetail = wtDetail;
                        }
                    }
                    // validate trọng lượng DO
                    flgGqty = validateTolerance(null, outbDel);

                    // check dung sai -> set Qty
//                    String material = (outbDel != null && outbDel.getMatnr() != null) ? outbDel.getMatnr().toString() : "";
//                    if(checkVariantByMaterial(weightTicket, material, weightTicket.getGQty())) {
//                        weightTicket.setGQty(sumQtyReg);
//                    }
                    // mode nhap DO
                    if (weightTicket.getMode().equals("IN_WAREHOUSE_TRANSFER")) {
                        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LR")
                                || outbDel.getLfart().equalsIgnoreCase("ZRET")
                                || outbDel.getLfart().equalsIgnoreCase("ZVND"))) {
                            modeFlg = "Z001";
                            objBapi = getPgmVl02nBapi(weightTicket, outbDel, modeFlg, ivWbidNosave, sumQtyReg);
                        } else {
                            objBapi = getGrDoMigoBapi(weightTicket, outbDel);
                        }
                    } else if ((!weightTicket.getMode().equals("IN_WAREHOUSE_TRANSFER")) && (flgGqty)) {
                        // xuat DO
                        if (weightTicket.getMode().equals("OUT_SELL_ROAD")) {
                            modeFlg = "Z001";
                            objBapi = getPgmVl02nBapi(weightTicket, outbDel, modeFlg, ivWbidNosave, sumQtyReg);
                        }

                        // ban hang thuy
                        if (weightTicket.getMode().equals("OUT_SELL_WATERWAY")) {
                            modeFlg = "Z002";
                            objBapi = getPgmVl02nBapi(weightTicket, outbDel, modeFlg, ivWbidNosave, sumQtyReg);
                        }
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Chênh lệnh vượt dung sai cho phép!");
                        weightTicket.setPosted(false);
                        return null;
//                        completed = false;
//                        entityManager.clear();
                    }

                    if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                        if (objBapi != null) {
                            try {
                                logger.info("[SAP] Get infor before post SAP: " + objBapi.toString());
                                sapSession.execute(objBapi);
                                OutboundDeliveryDetail details_item = null;

                                if (objBapi instanceof DOCreate2PGIBapi) {
                                    weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
                                    weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_messages = ((DOCreate2PGIBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                    for (int k = 0; k < outDetails_lits.size(); k++) {
                                        details_item = outDetails_lits.get(k);
                                        if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                            details_item.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                            details_item.setDocYear(((DOCreate2PGIBapi) objBapi).getDocYear());
                                            outbDel.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                        }
                                        if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                            details_item.setPosted(false);
                                            details_item.setUpdatedDate(now);
                                            outbDel.setPosted(false);
                                            outbDel.setUpdatedDate(now);
                                            flag_fail = true;
                                        } else {
                                            details_item.setPosted(true);
                                            outbDel.setPosted(true);
                                            details_item.setUpdatedDate(now);
                                            outbDel.setUpdatedDate(now);
                                        }

                                        if (!entityManager.getTransaction().isActive()) {
                                            entityManager.getTransaction().begin();
                                        }
                                        entityManager.merge(details_item);
                                        entityManager.getTransaction().commit();
                                    }
                                    if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                        outbDel.setPosted(false);
                                        outbDel.setUpdatedDate(now);
                                        flag_fail = true;
                                    } else {
                                        outbDel.setPosted(true);
                                        outbDel.setUpdatedDate(now);
                                    }
                                }
                                if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        bapi_messages = ((GoodsMvtPoCreateBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
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
                                            details_item.setUpdatedDate(now);
                                            outbDel.setUpdatedDate(now);
                                            flag_fail = true;
                                        } else {
                                            details_item.setPosted(true);
                                            outbDel.setPosted(true);
                                            details_item.setUpdatedDate(now);
                                            outbDel.setUpdatedDate(now);
                                        }
                                        if (!entityManager.getTransaction().isActive()) {
                                            entityManager.getTransaction().begin();
                                        }
                                        entityManager.merge(details_item);
                                        entityManager.getTransaction().commit();
                                    }
                                    if (((GoodsMvtPoCreateBapi) objBapi).getMatDoc() == null) {
                                        outbDel.setPosted(false);
                                        outbDel.setUpdatedDate(now);

                                        flag_fail = true;
                                    } else {
                                        outbDel.setPosted(true);
                                        outbDel.setUpdatedDate(now);
                                    }
                                }
                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        bapi_messages = ((GoodsMvtDoCreateBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
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
                                            details_item.setUpdatedDate(now);
                                            outbDel.setUpdatedDate(now);
                                            flag_fail = true;
                                        } else {
                                            details_item.setPosted(true);
                                            outbDel.setPosted(true);
                                            details_item.setUpdatedDate(now);
                                            outbDel.setUpdatedDate(now);
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
                                        outbDel.setUpdatedDate(now);
                                    } else {
                                        outbDel.setPosted(true);
                                        outbDel.setUpdatedDate(now);
                                    }
                                }
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                    weightTicketDetail.setDocYear(IntegerUtil.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));

                                    try {
                                        bapi_messages = ((WsDeliveryUpdateBapi) objBapi).getReturnMessage();
                                    } catch (Exception Ex) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }

                                    for (int k = 0; k < outDetails_lits.size(); k++) {
                                        details_item = outDetails_lits.get(k);
                                        if (details_item.getDeliveryOrderNo().equals(outbDel.getDeliveryOrderNo())) {
                                            details_item.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                            details_item.setDocYear(((WsDeliveryUpdateBapi) objBapi).getDoc_year());
                                            outbDel.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                        }
                                        if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                            details_item.setPosted(false);
                                            details_item.setUpdatedDate(now);
                                            flag_fail = true;
                                        } else {
                                            details_item.setPosted(true);
                                            details_item.setUpdatedDate(now);
                                        }

                                        if (!entityManager.getTransaction().isActive()) {
                                            entityManager.getTransaction().begin();
                                        }
                                        entityManager.merge(details_item);
                                        entityManager.getTransaction().commit();
                                    }
                                    if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                        outbDel.setPosted(false);
                                        flag_fail = true;
                                        outbDel.setUpdatedDate(now);
                                    } else {
                                        outbDel.setPosted(true);
                                        outbDel.setUpdatedDate(now);
                                    }
                                }

                                if (flag_fail || weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")) {
                                    revertCompletedDO(completedDO, outDetails_lits, outbDel_list);
                                    weightTicket.setPosted(false);
                                    weightTicketDetail.setPosted(false);
                                    if (bapi_messages.isEmpty()) {
                                        bapi_messages.add(resourceMapMsg.getString("msg.errorSAP"));
                                    }
                                    bapi_messages.forEach(msg -> JOptionPane.showMessageDialog(rootPane, msg));
                                    completed = false;
                                    entityManager.clear();
                                } else if (!flag_fail) {
                                    weightTicket.setPosted(true);
                                    weightTicketDetail.setPosted(true);
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
                                    sapOutb.setWeightTicketId(outbDel.getWeightTicketId());
                                    sapOutb.setPosted(outbDel.isPosted());
                                    sapOutb.setMatDoc(outbDel.getMatDoc());
                                    sapOutb.setUpdatedDate(now);
                                    entityManager.merge(sapOutb);
                                    outbDel = sapOutb;
                                    outbDel_list.set(i, sapOutb);
                                }
                                // </editor-fold>
                            } catch (Exception e) {
                                logger.error(e);
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    if (((WsDeliveryUpdateBapi) objBapi).getReturn() != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)) != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId() != null
                                            && (((WsDeliveryUpdateBapi) objBapi).getReturn().get(0)).getId().equals("NOREVERT")) {
                                    } else {

                                        revertCompletedDO(completedDO, outDetails_lits, outbDel_list);
                                        outbDel.setPosted(false);
                                        outbDel.setUpdatedDate(now);
                                    }
                                }
                                weightTicket.setPosted(false);
                                failed(e);
                                completed = false;
                                entityManager.clear();
                            }
                        }
                    } else if (WeighBridgeApp.getApplication().isOfflineMode()) {
                        weightTicket.setPosted(false);
                        weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
                    } else {
                        weightTicket.setPosted(false);
                    }

                    if (!entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().begin();
                    }
                    entityManager.merge(outbDel);
                    entityManager.getTransaction().commit();

                    if (!completed) {
                        failed(new Exception("Post SAP không thành công. Vui lòng thử lại."));
                        break;
                    }
                }
                // check status posted for Ghep ma
                boolean flgNotPosted = false;
                for (WeightTicketDetail wtDetail : weightTicketDetails) {
                    if (StringUtil.isEmptyString(wtDetail.getMatDoc())) {
                        flgNotPosted = true;
                    }
                }
                if (flgNotPosted) {
                    weightTicket.setPosted(false);
                } else {
                    weightTicket.setPosted(true);
                }
                // </editor-fold>

            }

            if (WeighBridgeApp.getApplication().isOfflineMode()
                    || weightTicket.getMode().equals("IN_OTHER")
                    || weightTicket.getMode().equals("OUT_OTHER")) {
                if (isStage2()) {
                    weightTicket.setPosted(false);
                }
                for (WeightTicketDetail wtDetail : weightTicketDetails) {
                    wtDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
                }
            }
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.merge(weightTicket);
            entityManager.getTransaction().commit();
            entityManager.clear();
            if (completed) {
                if (!weightTicket.isDissolved() || weightTicket.isPosted()) {
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
                if (!ExceptionUtil.isSapDisConnectedException(cause)) {
                    if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                        cause = cause.getCause();
                    }
                    logger.error(null, cause);
                    JOptionPane.showMessageDialog(rootPane, cause != null ? cause.getMessage() : "Null Pointer Exception");
                }
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
            setSaveNeeded(false);
        }
    }

    private class AcceptBatchTask extends org.jdesktop.application.Task<Object, Void> {

        AcceptBatchTask(org.jdesktop.application.Application app) {
            super(app);
            config = WeighBridgeApp.getApplication().getConfig();
        }

        @Override
        protected Object doInBackground() {
            String charg = txtCharg.getText().trim().isEmpty() ? null : txtCharg.getText().trim();
            if (charg != null) {
                charg = charg.toUpperCase(Locale.ENGLISH);
            }
            weightTicket.setCharg(charg);

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
            boolean checkVariant = false;
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

                // check tolorance for PO mua hang
                if (weightTicket.getMode().equals("IN_PO_PURCHASE")) {
                    purchaseOrder = purchaseOrderRepository.findByPoNumber(weightTicket.getWeightTicketDetail().getEbeln());
                    if (validateQuantityPO(purchaseOrder, new BigDecimal(Double.toString(result)))) {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                    } else {
                        String msg = "Không thể nhập hàng vì trọng lượng vượt quá đăng ký!";
                        JOptionPane.showMessageDialog(rootPane, msg);
                        txfOutQty.setValue(null);
                        txtOutTime.setText(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        btnAccept.setEnabled(false);
                        btnOScaleReset.setEnabled(true);
                        return null;
                    }
                }
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

                if (outbDel != null) {
                    String param = (outbDel != null && outbDel.getMatnr() != null) ? outbDel.getMatnr().toString() : "";

                    if (param.equals("") && purOrder != null) {
                        PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                        if (purchaseOrderDetail.getMaterial() != null && !purchaseOrderDetail.getMaterial().isEmpty()) {
                            param = purchaseOrderDetail.getMaterial();
                        }
                    }

                    Variant vari = weightTicketController.findByParamMandtWplant(param, configuration.getSapClient(), configuration.getWkPlant());
                    double valueUp = 0;
                    double valueDown = 0;

                    if (vari != null) {
                        if (vari.getValueUp() != null && !vari.getValueUp().isEmpty()) {
                            valueUp = Double.parseDouble(vari.getValueUp());
                        }

                        if (vari.getValueDown() != null && !vari.getValueDown().isEmpty()) {
                            valueDown = Double.parseDouble(vari.getValueDown());
                        }

                        double upper = qty + (qty * valueUp) / 100;
                        double lower = qty - (qty * valueDown) / 100;

                        if ((lower <= result && result <= upper)) {
                            txfGoodsQty.setValue(result);
                            weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                            checkVariant = true;
                        } else {
                            String msg = "Chênh lệch vượt dung sai cho phép!";
                            JOptionPane.showMessageDialog(rootPane, msg);
                            txfOutQty.setValue(null);
                            txtOutTime.setText(null);
                            txfGoodsQty.setValue(null);
                            weightTicket.setGQty(null);
                            btnAccept.setEnabled(false);
                            btnOScaleReset.setEnabled(true);
                            return null;
                        }
                    } else if (weightTicket.getMode().equals("OUT_SELL_ROAD")
                            || weightTicket.getMode().equals("OUT_SELL_WATERWAY")) {
                        double upper = qty + (qty * valueUp) / 100;
                        if ((result <= upper)) {
                            txfGoodsQty.setValue(result);
                            weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                        } else {
                            String msg = "Chênh lệch vượt dung sai cho phép!";
                            JOptionPane.showMessageDialog(rootPane, msg);
                            txfOutQty.setValue(null);
                            txtOutTime.setText(null);
                            txfGoodsQty.setValue(null);
                            weightTicket.setGQty(null);
                            btnAccept.setEnabled(false);
                            btnOScaleReset.setEnabled(true);
                            return null;
                        }
                    } else {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                    }
                } else if (isSubContract() && weightTicket.getLgort() != null && weightTicket.getCharg() != null) {
                    setMessage(resourceMapMsg.getString("msg.checkIssetWarehouse"));
                    Double remaining = CheckMatStock(weightTicket.getWeightTicketDetail().getMatnrRef(), configuration.getWkPlant(), weightTicket.getLgort(), weightTicket.getCharg());
                    if (result <= remaining) {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                    } else {
                        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.oBiggerWarehouse"));
                        txfOutQty.setValue(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        btnOScaleReset.setEnabled(true);
                    }
                } else {
                    txfGoodsQty.setValue(result);
                    weightTicket.setGQty(new BigDecimal(Double.toString(result)).setScale(3, RoundingMode.HALF_UP));
                }
            }
            if (isStage1()) {
                txfInQty.setValue(txfCurScale.getValue());
                txtInTime.setText(formatter.format(now));
                weightTicket.setFCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                weightTicket.setFScale(new BigDecimal(((Number) txfInQty.getValue()).doubleValue()).setScale(3, RoundingMode.HALF_UP));
                weightTicket.setFTime(now);
                lblIScale.setForeground(Color.black);
                OutboundDeliveryDetail item;
                checkPlant = false;
                if (outDetails_lits.size() > 0) {
                    for (int i = 0; i < outDetails_lits.size(); i++) {
                        item = outDetails_lits.get(i);
                        BigDecimal inScale = new BigDecimal(((Number) txfInQty.getValue()).doubleValue() / 1000);
                        item.setInScale(inScale.setScale(3, RoundingMode.HALF_UP));
                        item.setfTime(now);
                        item.setLfimg_ori(item.getLfimg());
                        item.setUpdatedDate(now);
                        // tinh toan cho Nhap kho tu plant xuat > plant nhap
                        if(weightTicket.getMode().equals("IN_WAREHOUSE_TRANSFER")) {
                            if (checkPlantOutToIn(item, outbDel.getWerks())) {
                                double outSScalePlant = 0;
                                double result = ((Number) txfInQty.getValue()).doubleValue();
                                outSScalePlant = item.getSscale().doubleValue();
                                // check can 1 cua nhap voi can 2 xuat chenh lech 1%
                                double upper = outSScalePlant + (outSScalePlant * 1) / 100;
                                double lower = outSScalePlant - (outSScalePlant * 1) / 100;
                                if ((lower <= result && result <= upper)) {
                                    item.setGoodsQty(item.getLfimg());
                                    item.setOutScale((BigDecimal.valueOf(item.getInScale().doubleValue() - item.getGoodsQty().doubleValue())).setScale(3, RoundingMode.HALF_UP));
                                    item.setsTime(now);
                                    weightTicket.setSCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                                    weightTicket.setSScale((BigDecimal.valueOf((item.getInScale().doubleValue() - item.getGoodsQty().doubleValue()) * 1000)).setScale(3, RoundingMode.HALF_UP));
                                    weightTicket.setSTime(now);
                                    weightTicket.setGQty((BigDecimal.valueOf(item.getInScale().doubleValue() - item.getOutScale().doubleValue())).setScale(3, RoundingMode.HALF_UP));
                                    checkPlant = true;
                                }
                            }
                        }
                        
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
                weightTicket.setSScale(new BigDecimal(((Number) txfOutQty.getValue()).doubleValue()).setScale(3, RoundingMode.HALF_UP));
                weightTicket.setSTime(now);
                lblOScale.setForeground(Color.black);
                OutboundDeliveryDetail item;
                double remain = (((Number) txfCurScale.getValue()).doubleValue() - ((Number) txfInQty.getValue()).doubleValue()) / 1000;

                // chia cân
                if (outDetails_lits.size() > 1) {
                    List<OutboundDeliveryDetail> outDetailFrees = outDetails_lits.stream()
                            .filter(t -> t.getFreeItem() != null && t.getFreeItem() == 'X')
                            .collect(Collectors.toList());

                    for (OutboundDeliveryDetail obj : outDetailFrees) {
                        obj.setGoodsQty(obj.getLfimg());
                        obj.setOutScale(obj.getInScale().add(obj.getLfimg()).setScale(3, RoundingMode.HALF_UP));
                        obj.setsTime(now);
                        obj.setUpdatedDate(now);
                        remain = remain - obj.getLfimg().doubleValue();

                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                        }
                        entityManager.merge(obj);
                        entityManager.getTransaction().commit();
                    }

                    List<OutboundDeliveryDetail> outDetails = outDetails_lits.stream()
                            .filter(t -> t.getFreeItem() == null || t.getFreeItem() != 'X')
                            .collect(Collectors.toList());

                    for (int i = 0; i < outDetails.size(); i++) {
                        item = outDetails.get(i);
                        if (i < outDetails.size() - 1) {
                            item.setGoodsQty(item.getLfimg());
                            item.setOutScale(item.getInScale().add(item.getLfimg()).setScale(3, RoundingMode.HALF_UP));
                            item.setsTime(now);
                            item.setUpdatedDate(now);
                            remain = remain - item.getLfimg().doubleValue();
                        } else {
                            if (checkVariant) {
                                item.setGoodsQty(item.getLfimg());
                            } else {
                                item.setGoodsQty(BigDecimal.valueOf(remain).setScale(3, RoundingMode.HALF_UP));
                            }
                            item.setOutScale((BigDecimal.valueOf(item.getInScale().doubleValue() + remain)).setScale(3, RoundingMode.HALF_UP));
                            item.setsTime(now);
                            item.setUpdatedDate(now);
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
                    item.setOutScale(weightTicket.getSScale().divide(div).setScale(3, RoundingMode.HALF_UP));
                    if (checkVariant) {
                        item.setGoodsQty(item.getLfimg());
                    } else {
                        item.setGoodsQty((weightTicket.getSScale().subtract(weightTicket.getFScale()).divide(div).abs()).setScale(3, RoundingMode.HALF_UP));
                    }
                    item.setsTime(now);
                    item.setUpdatedDate(now);
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
        txtCustomer.setText(null);
        txtCustomer.setEnabled(false);
        txtGRText.setEnabled(false);
        txtCharg.setEditable(false);
        this.setFormEnable(false);
        this.setSubContract(false);
        this.setMvt311(false);
        this.setMaterialAvailable(false);
        this.setMatAvailStocks(null);

        grbType.clearSelection();
        grbCat.clearSelection();

        txtSO.setText(null);
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
        txtSalan.setText(null);
        txtSLoc.setText(null);

        txtGRText.setText(null);
        txtRegItem.setText(null);
        txtMatnr.setText(null);
        txtDelNum.setText(null);
        txtCharg.setText(null);
        txtCementDesc.setText(null);
        txtCementDesc.setEditable(true);
        txtLgortIn.setText(null);
        txtChargIn.setText(null);

        lblIScale.setForeground(Color.black);
        lblOScale.setForeground(Color.black);
        lblGScale.setForeground(Color.black);
        lblSLoc.setForeground(Color.black);
        lblCharg.setForeground(Color.black);
        lblCementDesc.setForeground(Color.black);
        lblBatchProduce.setForeground(Color.black);

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
        txtVendorLoading.setText(null);
        txtVendorTransport.setText(null);

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
                if (!(component instanceof JLabel)) {
                    component.setEnabled(false);
                    component.setForeground(Color.black);
                }
            }
        }
    }

    private Object getGrDoMigoBapi(WeightTicket wt, OutboundDelivery outbDel) {
        return weightTicketController.getGrDoMigoBapi(wt, weightTicket, outbDel, outDetails_lits, timeFrom, timeTo);
    }

    private Object getGrPoMigoBapi(WeightTicket wt, PurchaseOrder purchaseOrder) {
        return weightTicketController.getGrPoMigoBapi(wt, weightTicket, purchaseOrder, timeFrom, timeTo);
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

    private Object getDOPostingPGI(WeightTicket wt, OutboundDelivery outbDel, String deliveryNum) {
        return weightTicketController.getDOPostingPGI(wt, outbDel, weightTicket, timeFrom, timeTo, outDetails_lits, deliveryNum);
    }

    private Object getPgmVl02nBapi(WeightTicket wt, OutboundDelivery outbDel, String modeFlg, String ivWbidNosave, BigDecimal sumQtyReg) {
        return weightTicketController.getPgmVl02nBapi(wt, outbDel, weightTicket, modeFlg, timeFrom, timeTo, outDetails_lits, ivWbidNosave, sumQtyReg);
    }

    private Object getMvtPOSTOCreatePGI(WeightTicket wt, String number, boolean flgPost) {
        return weightTicketController.getMvtPOSTOCreatePGI(wt, weightTicket, number, timeFrom, timeTo, flgPost);
    }

    private void printWT(WeightTicket wt, boolean reprint) {
        weightTicketController.printWT(wt, reprint, ximang, outbDel_list, outDetails_lits, outbDel, txtPONo.getText(), stage1, rootPane);
    }

    public boolean isValidated() {
        config = WeighBridgeApp.getApplication().getConfig();
        boolean result = false, bMisc = false, bPO = false, bMB1B = false, bMvt311 = false, bScale = false, bSLoc = false, bBatch = false;
        boolean bMaterial = false, bBatchMng = false;
        boolean bNiemXa = true;
        boolean bBatchProduce = true;
        boolean isRemark = true;
        if (grbType.getSelection() == null && isWithoutDO()) {
        } else {
            bMisc = WeighBridgeApp.getApplication().isOfflineMode();
            bPO = true;
            bMB1B = true;
            bMvt311 = true;
        }
        if ((txtPONo.getText() != null || !"".equals(txtPONo.getText())) && isEnteredValidPONum() && isValidPONum()) {
            bPO = true;
        } else if ((txtPONo.getText() != null && !"".equals(txtPONo.getText())) && (!isEnteredValidPONum() || isValidPONum())) {
            bPO = false;
        }

        if (weightTicket.getRecvLgort() == null) {
            bMB1B = false;
        } else {
            bMB1B = true;
        }

        if (weightTicket.getRecvMatnr() == null) {
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
        bSLoc = !(txtSLoc.getText().trim().isEmpty());
        bBatch = !(txtCharg.getText().trim().isEmpty());
        if (bMisc) {
            bSLoc = true;
            bBatch = true;
        }
        if (!bBatch && bMaterial && !bBatchMng) {
            bBatch = true;
        }

        bBatch = true;
        if (weightTicket.getWeightTicketDetail() != null && weightTicket.getWeightTicketDetail().getMatnrRef() != null) {
            materialConstraint = weightTicketController.getMaterialConstraintByMatnr(weightTicket.getWeightTicketDetail().getMatnrRef());
            if (isStage2()
                    && materialConstraint != null && materialConstraint.getRequiredNiemXa()
                    && (txtCementDesc.getText().trim() == null || txtCementDesc.getText().trim().equals(""))
                    && validateLength(txtCementDesc.getText().trim(), lblCementDesc, 0, 60)) {
                bNiemXa = false;
            }
            if (bNiemXa) {
                lblCementDesc.setForeground(Color.black);
            } else {
                lblCementDesc.setForeground(Color.red);
                txtCementDesc.setEditable(true);
                txtCementDesc.setEnabled(true);
            }
            if (isStage2()
                    && materialConstraint != null && materialConstraint.getRequiredBatch()
                    && (txtBatchProduce.getText().trim() == null || txtBatchProduce.getText().trim().equals(""))
                    && validateLength(txtBatchProduce.getText().trim(), lblBatchProduce, 0, 128)) {
                bBatchProduce = false;
            }
            if (bBatchProduce) {
                lblBatchProduce.setForeground(Color.black);
            } else {
                lblBatchProduce.setForeground(Color.red);
                txtBatchProduce.setEditable(true);
                txtBatchProduce.setEnabled(true);
            }
        }
        isRemark = validateLength(txtRemark.getText().trim(), lblRemark, 0, 128);
        result = (bMisc || bPO || bMB1B || bMvt311)
                && bScale && bSLoc && bBatch && bBatchProduce && bNiemXa
                && (isStage1() || isStage2() || (!isStage1() && !isStage2()
                && weightTicket != null && weightTicket.isPosted())) && isRemark;
        return result;
    }

    public boolean validateLength(String value, JComponent label, int min, int max) {
        value = value.trim();
        LengthValidator lengthValidator;
        try {
            lengthValidator = new LengthValidator(min, max);
            lengthValidator.validate(value);
            label.setForeground(Color.black);
            return true;
        } catch (IllegalArgumentException ex) {
            label.setForeground(Color.red);
            return false;
        }
    }

    public GoodsMvtWeightTicketStructure fillWTStructure(WeightTicket wt,
            OutboundDelivery od, List<OutboundDeliveryDetail> od_v2_list) {
        return weightTicketController.fillWTStructure(wt, od, od_v2_list, weightTicket);
    }

    public boolean checkVariantByMaterial(WeightTicket wt, String material, BigDecimal gQty) {
        Variant vari = weightTicketController.findByParamMandtWplant(material, configuration.getSapClient(), configuration.getWkPlant());
        double valueUp = 0;
        double valueDown = 0;
        double result = gQty.doubleValue();

        if (vari != null) {
            if (vari.getValueUp() != null && !vari.getValueUp().isEmpty()) {
                valueUp = Double.parseDouble(vari.getValueUp());
            }

            if (vari.getValueDown() != null && !vari.getValueDown().isEmpty()) {
                valueDown = Double.parseDouble(vari.getValueDown());
            }

            double upper = result + (result * valueUp) / 100;
            double lower = result - (result * valueDown) / 100;

            if ((lower <= result && result <= upper)) {
                return true;
            }
        }
        return false;
    }

    private boolean validateTolerance(PurchaseOrder purchaseOrder, OutboundDelivery outboundDelivery) {
        BigDecimal numCheckWeight = BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal tolerance = BigDecimal.ZERO;
        BigDecimal freeQty = BigDecimal.ZERO;
        // check for PO
        if (purchaseOrder != null) {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();

            quantity = purchaseOrderDetail.getQuantity() != null ? purchaseOrderDetail.getQuantity() : BigDecimal.ZERO;
            tolerance = purchaseOrderDetail.getOverDlvTol() != null ? purchaseOrderDetail.getOverDlvTol() : BigDecimal.ZERO;

            numCheckWeight = quantity.add(
                    quantity.multiply(tolerance).divide(new BigDecimal(100))
            ).subtract(weightTicketRegistarationController.getSumGqtyWithPoNo(purchaseOrder.getPoNumber()));

            BigDecimal result = numCheckWeight.subtract(weightTicket.getGQty());
            if (result.compareTo(BigDecimal.ZERO) <= 0) {
                return false;
            }
        }

        // check for DO hàng tặng
        if (outboundDelivery != null) {
            numCheckWeight = outboundDelivery.getLfimg() != null ? outboundDelivery.getLfimg() : BigDecimal.ZERO;
            freeQty = outboundDelivery.getFreeQty();

            if (freeQty != null) {
                BigDecimal resultFree = freeQty.subtract(weightTicket.getGQty());
                if (resultFree.compareTo(BigDecimal.ZERO) > 0) {
                    return false;
                }
            }

        }
        return true;
    }

    private boolean validateQuantityPO(PurchaseOrder purchaseOrder, BigDecimal qty) {
        BigDecimal numCheckWeight = BigDecimal.ZERO;
        BigDecimal quantity = BigDecimal.ZERO;
        BigDecimal tolerance = BigDecimal.ZERO;
        BigDecimal freeQty = BigDecimal.ZERO;
        // check for PO
        if (purchaseOrder != null) {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();

            quantity = purchaseOrderDetail.getQuantity() != null ? purchaseOrderDetail.getQuantity() : BigDecimal.ZERO;
            tolerance = purchaseOrderDetail.getOverDlvTol() != null ? purchaseOrderDetail.getOverDlvTol() : BigDecimal.ZERO;

            numCheckWeight = quantity.add(
                    quantity.multiply(tolerance).divide(new BigDecimal(100))
            ).subtract(weightTicketRegistarationController.getSumGqtyWithPoNo(purchaseOrder.getPoNumber()));

            BigDecimal result = numCheckWeight.subtract(qty);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPlantOutToIn(OutboundDeliveryDetail item, String wplantOut) {
        boolean checkBag = false;
        Material mat = materialRepository.findByMatnr(item.getMatnr());
        // check dong bao
        if (mat == null || StringUtil.isEmptyString(mat.getGroes())) {
            return false;
        }
        String groes = mat.getGroes().replaceAll("\\s+", "");
        String[] output = groes.split("\\|");
        if (output.length != 2) {
            return false;
        }
        String b = output[0];
        if (b.equals(Constants.Groes.B)) {
            checkBag = true;
        }
        MaterialInterPlant materialInterPlant = materialInterPlantRepository.findByMatnrAndPlantInOut(item.getMatnr(), configuration.getWkPlant(), wplantOut);
        if ((materialInterPlant != null)
                && (checkBag)) {
            return true;
        }
        return false;
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
    private javax.swing.ButtonGroup grbBridge;
    private javax.swing.ButtonGroup grbCat;
    private javax.swing.ButtonGroup grbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JLabel lblLoadSource;
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
    private javax.swing.JLabel lblSalan;
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
    private javax.swing.JTextField txtCharg;
    private javax.swing.JTextField txtChargIn;
    private javax.swing.JTextField txtCustomer;
    private javax.swing.JTextField txtDName;
    private javax.swing.JTextField txtDelNum;
    private javax.swing.JTextField txtGRText;
    private javax.swing.JTextField txtInTime;
    private javax.swing.JTextField txtLgortIn;
    private javax.swing.JFormattedTextField txtLicPlate;
    private javax.swing.JTextField txtLoadSource;
    private javax.swing.JTextField txtMatnr;
    private javax.swing.JTextField txtOutTime;
    private javax.swing.JTextField txtPONo;
    private javax.swing.JFormattedTextField txtPallet;
    private javax.swing.JTextField txtPoPosto;
    private javax.swing.JTextField txtProcedure;
    private javax.swing.JTextField txtRegItem;
    private javax.swing.JTextField txtRegistrationNo;
    private javax.swing.JTextField txtRemark;
    private javax.swing.JTextField txtSLoc;
    private javax.swing.JTextField txtSO;
    private javax.swing.JTextField txtSalan;
    private javax.swing.JFormattedTextField txtSling;
    private javax.swing.JFormattedTextField txtTicketId;
    private javax.swing.JFormattedTextField txtTrailerPlate;
    private javax.swing.JTextField txtVendorLoading;
    private javax.swing.JTextField txtVendorTransport;
    private javax.swing.JTextField txtWTNum;
    private javax.swing.JTextField txtWeight;
    private javax.swing.JFormattedTextField txtWeightTicketIdRef;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    // </editor-fold>
}
