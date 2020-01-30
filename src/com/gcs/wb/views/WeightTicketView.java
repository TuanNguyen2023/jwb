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

import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.SAPErrorTransform;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.OutboundDeliveryDetail;
import com.gcs.wb.jpa.entity.BatchStock;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MovementPK;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import com.gcs.wb.jpa.entity.Reason;
import com.gcs.wb.jpa.entity.ReasonPK;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.TimeRange;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.base.util.RegexFormatter;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.JCoException;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.hibersap.SapException;
import org.hibersap.session.Session;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import javax.persistence.EntityManager;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.procedures.WeightTicketJpaRepository;
import com.gcs.wb.jpa.procedures.WeightTicketRepository;
import com.gcs.wb.jpa.repositorys.BatchStockRepository;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.SignalsRepository;
import com.gcs.wb.jpa.repositorys.TimeRangeRepository;
import com.gcs.wb.controller.WeightTicketController;
import com.gcs.wb.jpa.entity.PurchaseOrderDetail;
import com.gcs.wb.jpa.entity.WeightTicketDetail;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.repositorys.SLocRepository;
import com.gcs.wb.jpa.repositorys.VariantRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.jpa.service.JPAService;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.math.BigInteger;
// import java.util.Locale;
// import java.util.Set;
import java.awt.datatransfer.StringSelection;
import java.util.*;

/*
 *
 * @author Tran-Vu
 */
public class WeightTicketView extends javax.swing.JInternalFrame {

    private AppConfig config = WeighBridgeApp.getApplication().getConfig();
    private final SAPSetting sapSetting;
    private final User login;
    SignalsRepository noneRepository = new SignalsRepository();
    VendorRepository vendorRepository = new VendorRepository();
    EntityManager entityManager = JPAConnector.getInstance();
    CustomerRepository customerRepository = new CustomerRepository();
    SLocRepository sLocRepository = new SLocRepository();
    VariantRepository variantRepository = new VariantRepository();
    BatchStockRepository batchStockRepository = new BatchStockRepository();
    PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
    WeightTicketController weightTicketController = new WeightTicketController();
    SAPService sapService = new SAPService();
    //JPAService jpaService = new JPAService();

    public WeightTicketView() {
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

        setBridge1(config.getB1Port() != null);
        setBridge2(config.getB2Port() != null);

        formatter = new SimpleDateFormat();
        cbxSLoc.setSelectedIndex(-1);
        cbxReason.setSelectedIndex(-1);
        rbtMvt311.setVisible(true);
        rbtMb1b.setVisible(false);
        boolean flag = true; // tram can , true -- giao nhan  file   
        boolean flagAdmin = true;  // false normal , true -> SCM admin 
        txfCurScale.setEditable(flag);
        txtInTime.setEditable(flagAdmin);
        txtOutTime.setEditable(flagAdmin);
        sapSetting = WeighBridgeApp.getApplication().getSapSetting();
        login = WeighBridgeApp.getApplication().getLogin();
        entityManager.clear();
        chkDissolved.setEnabled(false);
        chkDissolved.setVisible(false);
        lblReason.setVisible(false);
        cbxReason.setVisible(false);
        lblComplete.setVisible(false);
        cbxCompleted.setVisible(false);
        rbtMisc.setForeground(Color.red);
        //String client = WeighBridgeApp.getApplication().getConfig().getsClient();
        //List kunnr = this.customerRepository.getListCustomer(client);
        DefaultComboBoxModel result = weightTicketController.getCustomerByMaNdt();

        try {
            String pWbId = WeighBridgeApp.getApplication().getConfig().getWbId().trim();
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
        } catch (Exception e) {
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
        weightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        purOrder = new com.gcs.wb.jpa.entity.PurchaseOrder();
        outbDel = new com.gcs.wb.jpa.entity.OutboundDelivery();
        setting = java.beans.Beans.isDesignTime() ? null : WeighBridgeApp.getApplication().getSapSetting();
        material = new com.gcs.wb.jpa.entity.Material();
        pnWTFilter = new javax.swing.JPanel();
        lblWTNum = new javax.swing.JLabel();
        rbtPO = new javax.swing.JRadioButton();
        txtPONum = new javax.swing.JTextField();
        rbtMisc = new javax.swing.JRadioButton();
        txtWTNum = new javax.swing.JTextField();
        rbtMb1b = new javax.swing.JRadioButton();
        rbtMvt311 = new javax.swing.JRadioButton();
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
        lblDName = new javax.swing.JLabel();
        txtDName = new javax.swing.JTextField();
        lblCMNDBL = new javax.swing.JLabel();
        txtCMNDBL = new javax.swing.JTextField();
        lblLicPlate = new javax.swing.JLabel();
        txtLicPlate = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblTrailerPlate = new javax.swing.JLabel();
        txtTrailerPlate = new javax.swing.JFormattedTextField(new RegexFormatter("\\d{2}\\w-\\d{4}"));
        lblRegCat = new javax.swing.JLabel();
        rbtInward = new javax.swing.JRadioButton();
        rbtOutward = new javax.swing.JRadioButton();
        lblRegItem = new javax.swing.JLabel();
        txtRegItem = new javax.swing.JTextField();
        lblMatnr = new javax.swing.JLabel();
        lblDelNum = new javax.swing.JLabel();
        txtDelNum = new javax.swing.JTextField();
        lblSLoc = new javax.swing.JLabel();
        cbxSLoc = new javax.swing.JComboBox();
        lblBatch = new javax.swing.JLabel();
        cbxBatch = new javax.swing.JComboBox();
        lblReason = new javax.swing.JLabel();
        cbxReason = new javax.swing.JComboBox();
        lblCementDesc = new javax.swing.JLabel();
        txtCementDesc = new javax.swing.JTextField();
        lblGRText = new javax.swing.JLabel();
        txtGRText = new javax.swing.JTextField();
        lblComplete = new javax.swing.JLabel();
        cbxCompleted = new javax.swing.JComboBox();
        chkDissolved = new javax.swing.JCheckBox();
        txtMatnr = new javax.swing.JTextField();
        lbKunnr = new javax.swing.JLabel();
        cbxKunnr = new javax.swing.JComboBox();
        btnPostAgain = new javax.swing.JButton();
        btnReprint = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        cbxVendorLoading = new javax.swing.JComboBox();
        cbxVendorTransport = new javax.swing.JComboBox();
        txtPoPosto = new javax.swing.JTextField();
        lblPoPosto = new javax.swing.JLabel();
        lblVendorLoading = new javax.swing.JLabel();
        lblVendorTransport = new javax.swing.JLabel();

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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WeightTicketView.class, this);
        rbtPO.setAction(actionMap.get("selectRbtPO")); // NOI18N
        grbType.add(rbtPO);
        rbtPO.setText(resourceMap.getString("rbtPO.text")); // NOI18N
        rbtPO.setName("rbtPO"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${withoutDO}"), rbtPO, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtPO.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtPOItemStateChanged(evt);
            }
        });

        txtPONum.setFont(txtPONum.getFont().deriveFont(txtPONum.getFont().getStyle() | java.awt.Font.BOLD, txtPONum.getFont().getSize()+2));
        txtPONum.setForeground(resourceMap.getColor("txtPONum.foreground")); // NOI18N
        txtPONum.setText(resourceMap.getString("txtPONum.text")); // NOI18N
        txtPONum.setAction(actionMap.get("readPO")); // NOI18N
        txtPONum.setName("txtPONum"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rbtPO, org.jdesktop.beansbinding.ELProperty.create("${selected}"), txtPONum, org.jdesktop.beansbinding.BeanProperty.create("editable"));
        bindingGroup.addBinding(binding);
        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, rbtPO, org.jdesktop.beansbinding.ELProperty.create("${enabled}"), txtPONum, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtPONum.getDocument().addDocumentListener(new DocumentListener() {

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

        rbtMisc.setAction(actionMap.get("selectRbtMisc")); // NOI18N
        grbType.add(rbtMisc);
        rbtMisc.setText(resourceMap.getString("rbtMisc.text")); // NOI18N
        rbtMisc.setMaximumSize(new java.awt.Dimension(67, 20));
        rbtMisc.setMinimumSize(new java.awt.Dimension(67, 20));
        rbtMisc.setName("rbtMisc"); // NOI18N
        rbtMisc.setPreferredSize(new java.awt.Dimension(67, 20));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${withoutDO}"), rbtMisc, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtMisc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtMiscItemStateChanged(evt);
            }
        });

        txtWTNum.setFont(txtWTNum.getFont().deriveFont(txtWTNum.getFont().getStyle() | java.awt.Font.BOLD, txtWTNum.getFont().getSize()+2));
        txtWTNum.setForeground(resourceMap.getColor("txtWTNum.foreground")); // NOI18N
        txtWTNum.setText(resourceMap.getString("txtWTNum.text")); // NOI18N
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

        rbtMb1b.setAction(actionMap.get("showMB1BOption")); // NOI18N
        grbType.add(rbtMb1b);
        rbtMb1b.setText(resourceMap.getString("rbtMb1b.text")); // NOI18N
        rbtMb1b.setName("rbtMb1b"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${withoutDO}"), rbtMb1b, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtMb1b.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtMb1bItemStateChanged(evt);
            }
        });

        rbtMvt311.setAction(actionMap.get("selectRbtMvt311")); // NOI18N
        grbType.add(rbtMvt311);
        rbtMvt311.setText(resourceMap.getString("rbtMvt311.text")); // NOI18N
        rbtMvt311.setName("rbtMvt311"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${withoutDO}"), rbtMvt311, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtMvt311.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtMvt311ItemStateChanged(evt);
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnWTFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnWTFilterLayout.createSequentialGroup()
                        .addComponent(lblWTNum)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtWTNum, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6))
                    .addGroup(pnWTFilterLayout.createSequentialGroup()
                        .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnWTFilterLayout.createSequentialGroup()
                                .addComponent(rbtPO)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtPONum, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(rbtMvt311, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtMb1b, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(rbtMisc, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        pnWTFilterLayout.setVerticalGroup(
            pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTFilterLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblWTNum))
                    .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtWTNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton1)
                        .addComponent(jButton2)))
                .addGap(18, 18, 18)
                .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtPO)
                    .addComponent(txtPONum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtMisc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rbtMvt311)
                    .addComponent(rbtMb1b))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${bridge1}"), rbtBridge1, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
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
                            .addComponent(btnAccept)
                            .addGroup(pnCurScaleDataLayout.createSequentialGroup()
                                .addComponent(txfCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(4, 4, 4)
                                .addComponent(lblKG)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(txfGoodsQty, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(txfOutQty, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
                    .addComponent(txfInQty, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))
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
                            .addComponent(txtOutTime, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                            .addComponent(txtInTime, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
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

        lblDName.setText(resourceMap.getString("lblDName.text")); // NOI18N
        lblDName.setName("lblDName"); // NOI18N

        txtDName.setEditable(false);
        txtDName.setName("txtDName"); // NOI18N

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

        lblRegItem.setText(resourceMap.getString("lblRegItem.text")); // NOI18N
        lblRegItem.setName("lblRegItem"); // NOI18N

        txtRegItem.setEditable(false);
        txtRegItem.setName("txtRegItem"); // NOI18N

        lblMatnr.setText(resourceMap.getString("lblMatnr.text")); // NOI18N
        lblMatnr.setName("lblMatnr"); // NOI18N

        lblDelNum.setText(resourceMap.getString("lblDelNum.text")); // NOI18N
        lblDelNum.setName("lblDelNum"); // NOI18N

        txtDelNum.setEditable(false);
        txtDelNum.setName("txtDelNum"); // NOI18N

        lblSLoc.setText(resourceMap.getString("lblSLoc.text")); // NOI18N
        lblSLoc.setName("lblSLoc"); // NOI18N

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

        lblBatch.setText(resourceMap.getString("lblBatch.text")); // NOI18N
        lblBatch.setName("lblBatch"); // NOI18N

        cbxBatch.setAction(actionMap.get("acceptBatch")); // NOI18N
        cbxBatch.setName("cbxBatch"); // NOI18N
        cbxBatch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxBatchKeyReleased(evt);
            }
        });

        lblReason.setText(resourceMap.getString("lblReason.text")); // NOI18N
        lblReason.setName("lblReason"); // NOI18N

        cbxReason.setModel(getReasonModel());
        cbxReason.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Reason) {
                    Reason r = (Reason)value;
                    setText(r.getGrtxt());
                }
                return this;
            }
        });
        cbxReason.setEnabled(false);
        cbxReason.setName("cbxReason"); // NOI18N
        cbxReason.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxReasonItemStateChanged(evt);
            }
        });

        lblCementDesc.setText(resourceMap.getString("lblCementDesc.text")); // NOI18N
        lblCementDesc.setName("lblCementDesc"); // NOI18N

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

        lblGRText.setText(resourceMap.getString("lblGRText.text")); // NOI18N
        lblGRText.setName("lblGRText"); // NOI18N

        txtGRText.setName("txtGRText"); // NOI18N
        txtGRText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtGRTextKeyReleased(evt);
            }
        });

        lblComplete.setText(resourceMap.getString("lblComplete.text")); // NOI18N
        lblComplete.setName("lblComplete"); // NOI18N

        cbxCompleted.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto", "Set", "NotSet" }));
        cbxCompleted.setEnabled(false);
        cbxCompleted.setName("cbxCompleted"); // NOI18N
        cbxCompleted.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCompletedItemStateChanged(evt);
            }
        });

        chkDissolved.setText(resourceMap.getString("chkDissolved.text")); // NOI18N
        chkDissolved.setEnabled(false);
        chkDissolved.setFocusable(false);
        chkDissolved.setHideActionText(true);
        chkDissolved.setName("chkDissolved"); // NOI18N
        chkDissolved.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDissolvedItemStateChanged(evt);
            }
        });

        txtMatnr.setEditable(false);
        txtMatnr.setText(resourceMap.getString("txtMatnr.text")); // NOI18N
        txtMatnr.setName("txtMatnr"); // NOI18N

        lbKunnr.setText(resourceMap.getString("lbKunnr.text")); // NOI18N
        lbKunnr.setName("lbKunnr"); // NOI18N

        cbxKunnr.setName("cbxKunnr"); // NOI18N
        cbxKunnr.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxKunnrItemStateChanged(evt);
            }
        });

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

        jButton3.setText(resourceMap.getString("cmdNiemXa.text")); // NOI18N
        jButton3.setEnabled(false);
        jButton3.setName("cmdNiemXa"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
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

        lblPoPosto.setText(resourceMap.getString("lblPoPosto.text")); // NOI18N
        lblPoPosto.setName("lblPoPosto"); // NOI18N

        lblVendorLoading.setText(resourceMap.getString("lblVendorLoading.text")); // NOI18N
        lblVendorLoading.setName("lblVendorLoading"); // NOI18N

        lblVendorTransport.setText(resourceMap.getString("lblVendorTransport.text")); // NOI18N
        lblVendorTransport.setName("lblVendorTransport"); // NOI18N

        javax.swing.GroupLayout pnWTicketLayout = new javax.swing.GroupLayout(pnWTicket);
        pnWTicket.setLayout(pnWTicketLayout);
        pnWTicketLayout.setHorizontalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnWTicketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSLoc)
                    .addComponent(lblTrailerPlate)
                    .addComponent(lblLicPlate)
                    .addComponent(lblCMNDBL)
                    .addComponent(lblDName)
                    .addComponent(lblGRText)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(lblReason)
                        .addGap(4, 4, 4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(chkDissolved)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 347, Short.MAX_VALUE)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPostAgain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReprint)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblVendorTransport)
                            .addComponent(lblVendorLoading)
                            .addComponent(lblPoPosto)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnWTicketLayout.createSequentialGroup()
                                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbxReason, 0, 240, Short.MAX_VALUE)
                                    .addComponent(txtGRText, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(cbxSLoc, javax.swing.GroupLayout.Alignment.TRAILING, 0, 240, Short.MAX_VALUE)
                                    .addComponent(txtLicPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(txtDName, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(txtTrailerPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                                    .addComponent(txtCMNDBL, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblRegItem)
                                    .addComponent(lblRegCat)
                                    .addComponent(lblMatnr)
                                    .addComponent(lblDelNum)
                                    .addComponent(lblBatch)
                                    .addComponent(lblCementDesc)
                                    .addComponent(lblComplete)))
                            .addComponent(lbKunnr))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPoPosto, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addGroup(pnWTicketLayout.createSequentialGroup()
                                .addComponent(rbtInward)
                                .addGap(18, 18, 18)
                                .addComponent(rbtOutward))
                            .addComponent(txtRegItem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDelNum, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(cbxBatch, 0, 429, Short.MAX_VALUE)
                            .addComponent(txtCementDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(cbxCompleted, 0, 429, Short.MAX_VALUE)
                            .addComponent(txtMatnr, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(cbxKunnr, 0, 429, Short.MAX_VALUE)
                            .addComponent(cbxVendorLoading, javax.swing.GroupLayout.Alignment.TRAILING, 0, 429, Short.MAX_VALUE)
                            .addComponent(cbxVendorTransport, javax.swing.GroupLayout.Alignment.TRAILING, 0, 429, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnWTicketLayout.setVerticalGroup(
            pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWTicketLayout.createSequentialGroup()
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDName)
                    .addComponent(txtDName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegCat)
                    .addComponent(rbtInward)
                    .addComponent(rbtOutward))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCMNDBL)
                            .addComponent(txtCMNDBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLicPlate)
                            .addComponent(txtLicPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTrailerPlate)
                            .addComponent(txtTrailerPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblRegItem)
                            .addComponent(txtRegItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMatnr)
                            .addComponent(txtMatnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblDelNum)
                            .addComponent(txtDelNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblSLoc)
                        .addComponent(cbxSLoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblBatch)
                        .addComponent(cbxBatch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblCementDesc)
                        .addComponent(txtCementDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblGRText)
                        .addComponent(txtGRText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblReason)
                        .addComponent(cbxReason, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblComplete)
                        .addComponent(cbxCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbKunnr)
                    .addComponent(cbxKunnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPoPosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPoPosto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorLoading, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorLoading))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorTransport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorTransport))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(chkDissolved)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSave)
                            .addComponent(btnReprint)
                            .addComponent(btnPostAgain)
                            .addComponent(jButton3))
                        .addContainerGap())))
        );

        txtMatnr.getAccessibleContext().setAccessibleName(resourceMap.getString("txtMatnr.AccessibleContext.accessibleName")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnScaleData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 563, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnCurScale, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnWTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnCurScale, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnScaleData, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnWTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="expanded" desc="Event Handlers Area">
    private void rbtBridge1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBridge1ActionPerformed
        /*
        String sport = ""; 
        //    Integer speed = 0 ;
        Short  databit = 0 ; 
        Integer  brate = 0 ;           
        Short stopbit = 0 ; 
        Short Parity = 0 ;
        Boolean  Mettler = false;     
         * 
         */
        // config = WeighBridgeApp.getApplication().getConfig();
        WeightTicketJpaController con = new WeightTicketJpaController();
        try {
            config = con.getDev(WeighBridgeApp.getApplication().getConfig().getWbId());

            btnAccept.setEnabled(WeighBridgeApp.getApplication().connectWB(
                    config.getB1Port(), //string
                    config.getB1Speed(), //int 
                    config.getB1DBits(), //short 
                    config.getB1SBits().shortValue(), //short 
                    config.getB1PC(), //short
                    config.getB1Mettler(),
                    txfCurScale));

            setSaveNeeded(isValidated());

        } catch (Exception ex) {

            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_rbtBridge1ActionPerformed

    private void rbtBridge2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBridge2ActionPerformed
        config = WeighBridgeApp.getApplication().getConfig();
        try {
            btnAccept.setEnabled(WeighBridgeApp.getApplication().connectWB(
                    config.getB2Port(),
                    config.getB2Speed(),
                    config.getB2DBits(),
                    config.getB2SBits().shortValue(),
                    config.getB2PC(),
                    config.getB2Mettler(),
                    txfCurScale));
            setSaveNeeded(isValidated());

        } catch (Exception ex) {
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
            cbxBatch.setModel(weightTicketController.setCbxBatch(batchs));
            cbxBatch.setSelectedIndex(-1);
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxSLocItemStateChanged

    private void cbxReasonItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxReasonItemStateChanged
        if (cbxReason.getSelectedIndex() == -1) {
            return;
        }
        Reason r = (Reason) cbxReason.getSelectedItem();
        weightTicket.setMoveReas(r.getReasonPK().getGrund());
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxReasonItemStateChanged

    private void txtGRTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGRTextKeyReleased
        if (weightTicket != null) {
            weightTicket.setText(txtGRText.getText().trim());
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtGRTextKeyReleased

    private void cbxCompletedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCompletedItemStateChanged
        if (weightTicket != null) {
            if (cbxCompleted.getSelectedIndex() == 1) {
                weightTicket.setNoMoreGr('2');
            } else {
                weightTicket.setNoMoreGr(' ');
            }
        }
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxCompletedItemStateChanged

    private void rbtMiscItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtMiscItemStateChanged
        if (!rbtMisc.isSelected()) {
            String regItemDescription = weightTicket.getWeightTicketDetail().getRegItemDescription();
            if (weightTicket != null && (regItemDescription != null && !regItemDescription.trim().isEmpty())) {
                txtRegItem.setText(regItemDescription);
            } else if (purOrder != null) {
                PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                txtRegItem.setText(purchaseOrderDetail.getShortText());
            }
        }
    }//GEN-LAST:event_rbtMiscItemStateChanged

    private void chkDissolvedItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDissolvedItemStateChanged
        if (weightTicket != null) {
            //     chkDissolved
            weightTicket.setDissolved(chkDissolved.isSelected());
            if (chkDissolved.isSelected() == true) {
                //  setSaveNeeded(true);
                // Tuanna 0909 2015 
                setSaveNeeded(false);
            } else {
                setSaveNeeded(false);
            }
        }
        //   chkDissolved.
//        setSaveNeeded(isValidated());
    }//GEN-LAST:event_chkDissolvedItemStateChanged

    private void rbtPOItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtPOItemStateChanged
    }//GEN-LAST:event_rbtPOItemStateChanged

    private void cbxBatchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxBatchKeyReleased
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxBatchKeyReleased

    private void rbtMb1bItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtMb1bItemStateChanged
        if (!rbtMb1b.isSelected()) {
            grbType.clearSelection();
            if (weightTicket == null) {
                return;
            }
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicket.setRecvPo(null);
            weightTicketDetail.setRecvMatnr(null);
            weightTicketDetail.setMatnrRef(null);
//            weightTicket.setRegItemDescription(null);
            weightTicketDetail.setUnit(null);
            weightTicket.setMoveType(null);
            weightTicket.setMoveReas(null);
        }
    }//GEN-LAST:event_rbtMb1bItemStateChanged

    private void rbtMvt311ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtMvt311ItemStateChanged
        if (!rbtMvt311.isSelected()) {
            grbType.clearSelection();
            if (weightTicket == null) {
                return;
            }
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicketDetail.setRecvMatnr(null);
            weightTicketDetail.setMatnrRef(null);
//            weightTicket.setRegItemDescription(null);
            weightTicketDetail.setUnit(null);
            weightTicket.setMoveType(null);
            weightTicket.setMoveReas(null);
        }
    }//GEN-LAST:event_rbtMvt311ItemStateChanged

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
            "Bạn có chắc chắn muốn post lại phiếu này?",
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
        weightTicket.setSTime((java.sql.Date) weightTicketController.setTimeWeightTicket(time));
    }
}//GEN-LAST:event_txtOutTimeKeyReleased

private void txtInTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInTimeKeyReleased
// TODO add your handling code here:
    if (txtInTime.getText().length() == 19) {
        String[] time = txtInTime.getText().split(" ");
        weightTicket.setFTime((java.sql.Date) weightTicketController.setTimeWeightTicket(time));
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
        } catch (UnsupportedFlavorException ex) {
            logger.error(ex.toString());
        } catch (IOException ex) {
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    }//GEN-LAST:event_jButton3ActionPerformed

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

    @Action
    public void showMB1BOption() {
        if (weightTicket == null) {
            return;
        }
        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();

        if (rbtMb1b.isSelected() && rbtInward.isSelected()) {
            grbType.clearSelection();
            return;
        }
        if (rbtMb1b.isSelected() && weightTicket.getRegType() == 'O') {
            RecvSlocView rsview = new RecvSlocView(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), weightTicket.getRecvPo());
            rsview.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
            WeighBridgeApp.getApplication().show(rsview);
            if (!rsview.isShowing()) {
                if (rsview.getRecSloc() != null) {
                    weightTicket.setRecvLgort(rsview.getRecSloc().getLgort());
                } else {
                    weightTicket.setRecvLgort(null);
                }
                if (rsview.getRefPO() != null) {
                    weightTicket.setRecvPo(rsview.getRefPO());
                } else {
                    weightTicket.setRecvPo(null);
                }
                weightTicket.setRecvPlant(config.getwPlant());
                weightTicket.setRecvCharg(weightTicket.getCharg());
//                weightTicket.setRecvMatnr(setting.getMatnrClinker());
//                weightTicket.setMatnrRef(setting.getMatnrClinker());
                weightTicketDetail.setRegItemDescription("Clinker gia công");
                weightTicketDetail.setUnit("TO");
                weightTicket.setMoveType("313");
                weightTicket.setMoveReas("0003");
                rsview.dispose();
                rsview = null;
                txtRegItem.setText(weightTicketDetail.getRegItemDescription());
            }
        }
        txtMatnr.setText(weightTicketDetail.getRecvMatnr());
        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
                && grbType.getSelection() != null) {
            rbtPO.setForeground(Color.black);
            rbtMisc.setForeground(Color.black);
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                rbtMisc.setForeground(Color.RED);
            }
            rbtMb1b.setForeground(Color.black);
            rbtMvt311.setForeground(Color.black);
        }
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMvt311() {
        if (weightTicket == null) {
            return;
        }

        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
        if (rbtMvt311.isSelected() && rbtInward.isSelected()) {
            grbType.clearSelection();
            return;
        }
        txtRegItem.setText(weightTicketDetail.getRegItemDescription());
        if (rbtMvt311.isSelected() && weightTicket.getRegType() == 'O') {
            Mvt311View mvt311View = new Mvt311View(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), null);
            mvt311View.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
            WeighBridgeApp.getApplication().show(mvt311View);
            if (!mvt311View.isShowing()) {
                if (mvt311View.getRecSloc() != null) {
                    weightTicket.setRecvLgort(mvt311View.getRecSloc().getLgort());
                } else {
                    weightTicket.setRecvLgort(null);
                }
                if (mvt311View.getSelMaterial() != null) {
                    material = mvt311View.getSelMaterial();
                    weightTicketDetail.setRecvMatnr(material.getMatnr());
                    weightTicketDetail.setMatnrRef(material.getMatnr());
                    weightTicketDetail.setRegItemDescription(material.getMaktx());
                } else {
                    material = null;
                    weightTicketDetail.setRecvMatnr(null);
                    weightTicketDetail.setMatnrRef(null);
//                    weightTicket.setRegItemDescription(null);
                }
                weightTicket.setRecvPlant(config.getwPlant());
                weightTicket.setRecvCharg(weightTicket.getCharg());
                weightTicketDetail.setUnit("TON");
                weightTicket.setMoveType("311");
                weightTicket.setMoveReas(null);
                mvt311View.dispose();
                mvt311View = null;
                txtRegItem.setText(weightTicketDetail.getRegItemDescription());
            }
        }
        txtMatnr.setText(weightTicketDetail.getRecvMatnr());
        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
                && grbType.getSelection() != null) {
            rbtPO.setForeground(Color.black);
            rbtMisc.setForeground(Color.black);
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                rbtMisc.setForeground(Color.red);
            }
            rbtMb1b.setForeground(Color.black);
            rbtMvt311.setForeground(Color.black);
        }
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtPO() {
        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
                && grbType.getSelection() != null) {
            rbtPO.setForeground(Color.black);
            rbtMisc.setForeground(Color.black);
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                rbtMisc.setForeground(Color.red);
            }
            rbtMb1b.setForeground(Color.black);
            rbtMvt311.setForeground(Color.black);

        }

        if (rbtPO.isEnabled()) {
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
        }
        setSaveNeeded(isValidated());
    }

    @Action
    public void selectRbtMisc() {
        if (rbtMisc.isSelected()) {
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            txtRegItem.setText(weightTicketDetail.getRegItemDescription());
            weightTicketDetail.setEbeln(null);
            weightTicketDetail.setItem(null);
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicket.setRecvPo(null);
            weightTicketDetail.setRecvMatnr(null);
            weightTicketDetail.setMatnrRef(null);
            weightTicketDetail.setUnit(null);
        }
        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && rbtMvt311.isEnabled()
                && grbType.getSelection() != null) {
            rbtPO.setForeground(Color.black);
            rbtMisc.setForeground(Color.black);
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                rbtMisc.setForeground(Color.red);
            }
            rbtMb1b.setForeground(Color.black);
            rbtMvt311.setForeground(Color.black);
        }
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
        boolean fCheckSignal = false;
        boolean fOk = false;
        if (fCheckSignal == true) {
            int count = noneRepository.getCountSingal();
            JOptionPane.showMessageDialog(rootPane, count);
        }

        if (weightTicket == null || txfCurScale.getValue() == null || ((Number) txfCurScale.getValue()).intValue() == 0) {
            return null;
        }
        WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
        if (!rbtMisc.isSelected() && weightTicketDetail.getMatnrRef() == null) {
            return null;
        }
        if (!isStage1() && !isStage2()) {
            return null;
        }
        if (rbtPO.isSelected()) {
            if (purOrder.getDocType().equals("UB")) {
                WeightTicketJpaController con = new WeightTicketJpaController();
                Material m = null;
                PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                try {
                    m = con.CheckPOSTO(purchaseOrderDetail.getMaterial());
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
        chkDissolved.setEnabled(false);
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
            return new ReadPOTask(WeighBridgeApp.getApplication(), txtPONum.getText().trim());
        } else {
            return null;
        }
    }

    @Action
    public Task acceptBatch() {
        if ((cbxBatch.getSelectedIndex() == -1 && !cbxBatch.isEditable()) || (cbxBatch.isEditable() && cbxBatch.getEditor().getItem().toString().trim().isEmpty())) {
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
        WeightTicketJpaController cmes = new WeightTicketJpaController();
        String msg = "";

        boolean valid = isValidated();
        if (weightTicket != null) {
            if (!weightTicket.isPosted() && chkDissolved.isSelected()) {
                valid = true;
            }
            //20121203 HOANGVV : check KL Dang Ky = KL Thuc te cho xi mang
            OutboundDelivery outdel_tmp = null;
            WeightTicketJpaController con = new WeightTicketJpaController();
            List<OutboundDeliveryDetail> details_list = new ArrayList<OutboundDeliveryDetail>();
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
                    mat_tmp = con.CheckPOSTO(outdel_tmp.getMatnr());

//                    ximang_tmp = mat_tmp.getBag();
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    details_list = con.findByMandtDelivNumb(outdel_tmp.getDeliveryOrderNo());
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
            String wplant = WeighBridgeApp.getApplication().getConfig().getwPlant().toString();
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
                // msg = cmes.getMsg("1");
                msg = " Có muốn lưu phiếu cân không ? ";
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer lv_return = JOptionPane.showConfirmDialog(WeighBridgeApp.getApplication().getMainFrame(), msg, "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (lv_return == JOptionPane.YES_OPTION) {
                //
                if (outbDel != null && outbDel.getMatnr() != null
                        //                        && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrPcb40())
                        && rbtOutward.isSelected() && isStage2()) {
                    //{+20101202#02 check material availability
                    //setMessage("Đang kiểm tra tồn kho trong SAP...");
                    //Double result = (Double) txfGoodsQty.getValue();
                    //Double remaining = CheckMatStock(weightTicket.getMatnrRef(), config.getwPlant(), weightTicket.getLgort(), "");
                    Double result = (double) 1;
                    Double remaining = (double) 0;

                    if (result != null && remaining != null) {
                        if (result > remaining) {
                            //}+20101202#02 check material availability
                            //+{add logic check confirm
                            Variant vari = variantRepository.findByParam("PROCESS_ORDER_CF");
                            String chkPROC1 = "";
                            if (vari != null) {
                                try {
                                    chkPROC1 = vari.getValue().toString();
                                } catch (Exception e) {
                                }
                            }
                            entityManager.clear();
                            //if (chkPROC1.equals("X")){ //20100214#01 huy phieu can
                            if (chkPROC1.equals("X") && !chkDissolved.isSelected()) { //20100214#01 huy phieu can
                                //+}add logic check confirm
                                //JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), "Không đủ tồn kho XM PCB40!!!");
                                WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                                ProcOrdView procoView = new ProcOrdView(WeighBridgeApp.getApplication().getMainFrame(), weightTicketDetail.getPpProcord());
                                procoView.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
                                WeighBridgeApp.getApplication().show(procoView);
                                if (!procoView.isShowing()) {
                                    if (procoView.getProcOrd() != null) {
                                        weightTicketDetail.setPpProcord(procoView.getProcOrd());
                                    } else {
                                        weightTicketDetail.setPpProcord(null);
                                    }
                                    procoView.dispose();
                                    procoView = null;
                                    if (weightTicketDetail.getPpProcord() == null) {
                                        JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), resourceMapMsg.getString("msg.needProcessOrder"));
                                        return null;
                                    }
                                }
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
            if (valid || text.length() == 0) {
                rbtPO.setForeground(Color.BLACK);
            } else {
                rbtPO.setForeground(Color.RED);
            }
        } catch (BadLocationException ex) {
        } finally {
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Form's properties">
    private boolean stage1 = false;
    public static final String PROP_STAGE1 = "stage1";
    private boolean stage2 = false;
    public static final String PROP_STAGE2 = "stage2";
    private boolean saveNeeded = false;
    public static final String PROP_SAVENEEDED = "saveNeeded";
    private boolean bridge1 = false;
    public static final String PROP_BRIDGE1 = "bridge1";
    private boolean bridge2 = false;
    public static final String PROP_BRIDGE2 = "bridge2";
    private boolean validPONum = false;
    public static final String PROP_VALIDPONUM = "validPONum";
    private boolean dissolved = false;
    public static final String PROP_DISSOLVED = "dissolved";
    private boolean reprintable = false;
    public static final String PROP_REPRINTABLE = "reprintable";
    private boolean enteredValidPONum = false;
    public static final String PROP_ENTEREDVALIDPONUM = "enteredValidPONum";
    private boolean enteredValidWTNum = false;
    public static final String PROP_ENTEREDVALIDWTNUM = "enteredValidWTNum";
    private boolean withoutDO = false;
    public static final String PROP_WITHOUTDO = "withoutDO";
    private boolean formEnable = false;
    public static final String PROP_FORMENABLE = "formEnable";
    private boolean subContract = false;
    public static final String PROP_SUBCONTRACT = "subContract";
    private boolean materialAvailable = false;
    public static final String PROP_MATERIALAVAILABLE = "materialAvailable";
    private Double matAvailStocks = null;
    public static final String PROP_MATAVAILSTOCKS = "matAvailStocks";
    private boolean mvt311 = false;
    public static final String PROP_MVT311 = "mvt311";

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

    private void setMessage(String msg) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

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
        protected Object doInBackground() {
            weightTicket = weightTicketController.findWeightTicket(weightTicket, id);
            if (weightTicket == null) {
                failed(new Exception("Không có phiếu cân số: " + txtWTNum.getText()));
            } else {
                if (weightTicket.getRegType() == 'I') {
                    rbtInward.setSelected(true);
                } else {

                    rbtOutward.setSelected(true);
                }
                String pWtId = txtWTNum.getText().trim();
                String SoNiemXa = weightTicketController.getSoNiemXa(pWtId);
                String Posto = "";
                txtCementDesc.setText(SoNiemXa);
                if (weightTicket.getPosto() != null) {
                    txtPONum.setText(weightTicket.getPosto());
                    rbtPO.setSelected(true);
                }

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
                    WeightTicketJpaController con = new WeightTicketJpaController();
                    try {
                        od = con.findByMandtOutDel(weightTicketDetail.getDeliveryOrderNo());
                    } catch (Exception ex) {
//                        java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (od == null && weightTicketDetail.getEbeln() != null) {
                        od = sapService.getOutboundDelivery(weightTicketDetail.getDeliveryOrderNo(), false);
                        if (od != null) {
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            entityManager.persist(od);
                            entityManager.getTransaction().commit();
                        }
                        try {
                            odt = con.findByMandtDelivNumb(weightTicketDetail.getDeliveryOrderNo());
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
                if (isStage1() && isWithoutDO()) {
                    rbtPO.setForeground(Color.red);
                    rbtMisc.setForeground(Color.red);
                    rbtMb1b.setForeground(Color.red);
                } else {
                    rbtPO.setForeground(Color.black);
                    rbtMisc.setForeground(Color.black);
                    if (WeighBridgeApp.getApplication().isOfflineMode()) {
                        rbtMisc.setForeground(Color.red);
                    }
                    rbtMb1b.setForeground(Color.black);
                }

                if (!isWithoutDO()) {
                    //xu ly nhieu DO trong WT
                    String[] do_list = weightTicketDetail.getDeliveryOrderNo().split("-");
                    WeightTicketJpaController con = new WeightTicketJpaController();
                    List<OutboundDeliveryDetail> details_list = new ArrayList<OutboundDeliveryDetail>();
                    OutboundDeliveryDetail item = null;
                    outbDel_list.clear();
                    outDetails_lits.clear();
                    for (int i = 0; i < do_list.length; i++) {
                        try {
                            //outbDel = entityManager.find(OutboundDelivery.class, new OutbDelPK(config.getsClient(), do_list[i]));
                            outbDel = con.findByMandtOutDel(do_list[i]);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!WeighBridgeApp.getApplication().isOfflineMode()) { //HLD18++offline
                            OutboundDelivery sapOutbDel = sapService.getOutboundDelivery(do_list[i], false);
                            if (sapOutbDel != null && outbDel == null) {
                                if (!entityManager.getTransaction().isActive()) {
                                    entityManager.getTransaction().begin();
                                }
                                entityManager.persist(sapOutbDel);
                                entityManager.getTransaction().commit();
                                entityManager.clear();
                            } else if (sapOutbDel != null && outbDel != null) {
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
                            details_list = con.findByMandtDelivNumb(do_list[i]);
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

                    purOrder = purchaseOrderRepository.findByPoNumber(weightTicketDetail.getEbeln());
                    txtPONum.setText(weightTicketDetail.getEbeln());
                    setValidPONum(true);
                    rbtPO.setSelected(true);
                    setSubContract(false);
                    PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                    if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3' //                            && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())
                            ) {
                        setSubContract(true);
                    }
                } else {
                    if (Posto.equals("")) {
                        // Tuanna 2018 08015
                        txtPONum.setText(null);
                        rbtPO.setSelected(false);
                    } else {
                        rbtPO.setSelected(true);
                        rbtMisc.setSelected(false);
                    }
                }
                if (weightTicket.getMoveType() != null && weightTicket.getMoveReas() != null
                        && weightTicket.getMoveType().equalsIgnoreCase("313")
                        && weightTicket.getMoveReas().equalsIgnoreCase("0003")) {
                    rbtMb1b.setSelected(true);
                    setSubContract(true);
                } else if (weightTicket.getMoveType() != null && weightTicket.getMoveType().equalsIgnoreCase("311")) {
                    rbtMvt311.setSelected(true);
                    setMvt311(true);
                } else if (isWithoutDO() && (weightTicketDetail.getEbeln() == null || weightTicketDetail.getEbeln().trim().isEmpty())) {
                    rbtMisc.setSelected(true);
                } else if (!isWithoutDO() && WeighBridgeApp.getApplication().isOfflineMode()) //HLD18++
                {
                    rbtMisc.setSelected(true);
                }
                if (weightTicket.getRegType() == 'I'
                        && ((!isWithoutDO() && outbDel != null && !outbDel.getLfart().equalsIgnoreCase("LF")
                        && !outbDel.getLfart().equalsIgnoreCase("LR")
                        && !outbDel.getLfart().equalsIgnoreCase("ZTLF")
                        && !outbDel.getLfart().equalsIgnoreCase("ZTLR")) || isWithoutDO())) {
                    cbxReason.setEnabled(true);
                    cbxCompleted.setEnabled(true);
                    txtGRText.setEnabled(true);
                    cbxBatch.setEditable(true);
                } else {
                    cbxReason.setEnabled(false);
                    cbxCompleted.setEnabled(false);
//                    txtGRText.setEnabled(false);
                    cbxBatch.setEditable(false);
//                    cbxSLoc.setEnabled(false);
                }
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
                    Customer cust = customerRepository.findByKunnr(weightTicketDetail.getKunnr());
                    cbxKunnr.setSelectedItem(cust);
                }
                if ((WeighBridgeApp.getApplication().isOfflineMode()
                        && !weightTicket.isPosted())
                        || (!WeighBridgeApp.getApplication().isOfflineMode()
                        && (rbtMisc.isSelected())
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
                    SLoc sloc = sLocRepository.findByLgort(weightTicket.getLgort());
                    cbxSLoc.setSelectedItem(sloc);
                } else if (outbDel != null && outbDel.getLgort() != null && !outbDel.getLgort().trim().isEmpty()) {
                    SLoc sloc = sLocRepository.findByLgort(outbDel.getLgort());
                    cbxSLoc.setSelectedItem(sloc);
                } else {
                    cbxSLoc.setSelectedIndex(-1);
                }
                if (weightTicket.getMoveType() != null && weightTicket.getMoveType().equalsIgnoreCase("101") && weightTicket.getMoveReas() != null && !weightTicket.getMoveReas().trim().isEmpty()) {
                    Reason reason = entityManager.find(Reason.class, new ReasonPK(config.getsClient(), "101", weightTicket.getMoveReas()));
                    if (reason != null) {
                        cbxReason.setSelectedItem(reason);
                    }
                }
                txtGRText.setText(weightTicket.getText());

                txtDelNum.setText(weightTicketDetail.getDeliveryOrderNo());
                txtCementDesc.setText(SoNiemXa);
                if (weightTicket.getNoMoreGr() != null && weightTicket.getNoMoreGr() == '2') {
                    cbxCompleted.setSelectedIndex(1);
                } else {
                    cbxCompleted.setSelectedIndex(0);
                }
                chkDissolved.setSelected(weightTicket.isDissolved());
                if (chkDissolved.isSelected() || (!isStage1() && !isStage2() && weightTicket.isPosted())) {
                    chkDissolved.setEnabled(false);
                } else {
                    chkDissolved.setEnabled(true);
                }
//                setDissolved(chkDissolved.isSelected());
                if (isDissolved() || (!isStage1() && !isStage2() && weightTicket.isPosted())) {
                    setValidPONum(false);
                    setWithoutDO(false);
                    cbxReason.setEnabled(false);
                    cbxCompleted.setEnabled(false);
//                    txtGRText.setEnabled(false);
                    cbxBatch.setEditable(false);
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
                        batch = batchStockRepository.findByWerksLgortMatnrCharg(config.getwPlant(), lgort, weightTicketDetail.getMatnrRef(), weightTicket.getCharg());
                    } else if (!isWithoutDO() && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()) {
                        batch = batchStockRepository.findByWerksLgortMatnrCharg(config.getwPlant(), lgort, weightTicketDetail.getMatnrRef(), outbDel.getCharg());
                    }
                    if (cbxBatch.getModel().getSize() == 0) {
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
                        cbxBatch.setModel(result);
                    }
                    if (batch != null) {
                        cbxBatch.setSelectedItem(batch.getCharg());
                    } else if (weightTicket.getCharg() != null && cbxBatch.isEditable()) {
                        cbxBatch.setSelectedItem(weightTicket.getCharg());
                    } else {
                        cbxBatch.setSelectedIndex(-1);
                    }
                }

                // cấu hình cho cầu cân hiển thị PO và vendor
                if (sapSetting.getCheckPov() != null && sapSetting.getCheckPov() == true) {
                    txtPoPosto.setVisible(true);
                    cbxVendorLoading.setVisible(true);
                    cbxVendorTransport.setVisible(true);
//                    lblPoPosto.setVisible(true);
//                    lblVendorLoading.setVisible(true);
//                    lblVendorTransport.setVisible(true);
                } else {
                    txtPoPosto.setVisible(false);
                    cbxVendorLoading.setVisible(false);
                    cbxVendorTransport.setVisible(false);
//                    lblPoPosto.setVisible(false);
//                    lblVendorLoading.setVisible(false);
//                    lblVendorTransport.setVisible(false);
                }

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
                if (!weightTicket.isPosted()) {
                    chkDissolved.setEnabled(true);
                } else {
                    chkDissolved.setEnabled(false);
                }
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
                sapPurOrder = weightTicketController.getSapPurOrder(poNum);
            } catch (Exception ex) {
                failed(ex);
            }
            if (sapPurOrder != null) {
                if (sapPurOrder.getVendor() != null && !sapPurOrder.getVendor().trim().isEmpty()) {
                    vendor = vendorRepository.findByLifnr(sapPurOrder.getVendor());
                    sapVendor = sapService.getVendor(sapPurOrder.getVendor());
                }
                if (sapPurOrder.getSupplVend() != null && !sapPurOrder.getSupplVend().trim().isEmpty()) {
                    supVendor = vendorRepository.findByLifnr(sapPurOrder.getSupplVend());
                    sapSupVendor = sapService.getVendor(sapPurOrder.getSupplVend());
                }
                if (sapPurOrder.getCustomer() != null && !sapPurOrder.getCustomer().trim().isEmpty()) {
                    customer = customerRepository.findByKunnr(sapPurOrder.getCustomer());
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
                entityManager.merge(sapVendor);
            } else if (sapVendor == null && vendor != null) {
                entityManager.remove(vendor);
            }
            //Store Sold to party Info
            if (sapSupVendor != null && supVendor == null && !sapPurOrder.getVendor().equalsIgnoreCase(sapPurOrder.getSupplVend())) {
                entityManager.persist(sapSupVendor);
            } else if (sapSupVendor != null && supVendor != null) {
                entityManager.merge(sapSupVendor);
            } else if (sapSupVendor == null && supVendor != null && !sapPurOrder.getVendor().equalsIgnoreCase(sapPurOrder.getSupplVend())) {
                entityManager.remove(supVendor);
            }
            //Store Vendor Info
            if (sapCustomer != null && customer == null) {
                entityManager.persist(sapCustomer);
            } else if (sapCustomer != null && customer != null) {
                entityManager.merge(sapCustomer);
            } else if (sapCustomer == null && customer != null) {
                entityManager.remove(customer);
            }
            if (sapPurOrder != null && purOrder == null) {
                entityManager.persist(sapPurOrder);
            } else if (sapPurOrder != null && purOrder != null) {
                entityManager.merge(sapPurOrder);
            } else if (sapPurOrder == null && purOrder != null) {
                entityManager.remove(purOrder);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
            if (sapPurOrder != null) {
                purOrder = purchaseOrderRepository.findByPoNumber(sapPurOrder.getPoNumber());
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
                if ((rbtInward.isSelected() && purchaseOrderDetail.getPlant().equalsIgnoreCase(config.getwPlant()))
                        || (rbtOutward.isSelected() && purOrder.getSupplPlnt().equalsIgnoreCase(config.getwPlant()))) {
                    setValidPONum(true);
                } else if (rbtOutward.isSelected() && purchaseOrderDetail.getItemCat() == '3' //                        && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())
                        ) {
//                    purOrder.setMaterial(setting.getMatnrClinker());
                    purchaseOrderDetail.setShortText("Clinker gia công");
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
                weightTicketDetail.setEbeln(purOrder.getPoNumber());
                weightTicketDetail.setItem(purchaseOrderDetail.getPoItem());
                weightTicketDetail.setRegItemDescription(purchaseOrderDetail.getShortText());
                weightTicketDetail.setMatnrRef(purchaseOrderDetail.getMaterial());
                weightTicketDetail.setUnit("TON");
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
            entityManager.merge(weightTicket);
            OutboundDelivery outbDel = null;
            List<String> completedDO = new ArrayList<String>();
            WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
            if (rbtPO.isSelected()) {
                if (((isStage2() || (!isStage1() && !isStage2())) && !weightTicket.isDissolved())
                        || (!isStage1() && !isStage2() && !weightTicket.isDissolved()
                        && (weightTicket != null && !weightTicket.isPosted()))) {
                    if (rbtInward.isSelected()) {
                        if (weightTicketDetail.getDeliveryOrderNo() == null) {
                            objBapi = getGrPoMigoBapi(weightTicket);
                        } else if (outbDel != null && outbDel.getLfart().equalsIgnoreCase("LR") && outbDel.getLfart().equalsIgnoreCase("ZTLR")) {
                            objBapi = getPgmVl02nBapi(weightTicket, outbDel);
                        } else {
                            objBapi = getGrDoMigoBapi(weightTicket, outbDel);
                        }
                    } else {
                        // set 

                        if (!rbtMisc.isSelected()) {
                            if (isSubContract() || isMvt311()) {
                                if (rbtPO.isSelected()) {
                                    objBapi = getGi541MigoBapi(weightTicket);
                                } else {
                                    objBapi = getGiMB1BBapi(weightTicket);
                                }
                            } else if (weightTicketDetail.getDeliveryOrderNo() != null) {
                                // tuanna 16.06.13
                                //  cbxKunnr.setSelectedIndex(-1);
                                objBapi = getPgmVl02nBapi(weightTicket, outbDel);

                            } else {
                                objBapi = getDoCreate2PGI(weightTicket, outbDel);
                            }
                        }
                    }
                    if (WeighBridgeApp.getApplication().isOfflineMode() == false) {
                        if (objBapi != null) {
                            try {
                                sapSession.execute(objBapi);
                                if (objBapi instanceof DOCreate2PGIBapi) {
                                    weightTicketDetail.setDeliveryOrderNo(((DOCreate2PGIBapi) objBapi).getDelivery());
                                    weightTicketDetail.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP (DOCreate2PGIBapi line >>> 2880) ";
                                    }
                                }
                                if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP ( GoodsMvtPoCreateBapi line >>2889 ) ";
                                    }
                                }
                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                    weightTicketDetail.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));

                                    try {
                                        bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = " No message returned when call SAP ( GoodsMvtDoCreateBapi line 2899 )";
                                    }
                                }
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    weightTicketDetail.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                    weightTicketDetail.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));
                                    if (weightTicketDetail.getPpProcord() != null && weightTicketDetail.getPpProcord().length() == 12) {
                                        weightTicketDetail.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
                                        weightTicketDetail.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
                                    }

                                    try {
                                        bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "ERROR:2944- No message returned error ";
                                    }

                                }
                                if (weightTicketDetail.getMatDoc() == null || weightTicketDetail.getMatDoc().equals("")) {
                                    revertCompletedDO(completedDO, null, null);
                                    weightTicket.setPosted(false);
                                    if (bapi_message == "") {
                                        bapi_message = "Error in BAPI function";
                                    }
                                    JOptionPane.showMessageDialog(rootPane, bapi_message);
                                    completed = false;
                                    entityManager.clear();
                                } else if ((weightTicketDetail.getMatDoc() != null) && (!weightTicketDetail.getMatDoc().equals(""))) {
                                    weightTicket.setPosted(true);
                                    completedDO.add(weightTicketDetail.getDeliveryOrderNo());
                                }

                                // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
                                if (outbDel != null) {
                                    OutboundDelivery sapOutb = sapService.getOutboundDelivery(outbDel.getDeliveryOrderNo(), false);
                                    Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                    Vendor lifnr = null, sapLifnr = null;
                                    if (sapOutb != null) {
                                        if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                            kunnr = customerRepository.findByKunnr(sapOutb.getKunnr());
                                            sapKunnr = sapService.getCustomer(sapOutb.getKunnr());
                                        }
                                        if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                            kunag = customerRepository.findByKunnr(sapOutb.getKunag());
                                            sapKunag = sapService.getCustomer(sapOutb.getKunag());
                                        }
                                        if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                            lifnr = vendorRepository.findByLifnr(sapOutb.getLifnr());
                                            sapLifnr = sapService.getVendor(sapOutb.getLifnr());
                                        }
                                    }
                                    //Store Ship to party Info
                                    if (sapKunnr != null && kunnr == null) {
                                        entityManager.persist(sapKunnr);
                                    } else if (sapKunnr != null && kunnr != null) {
                                        entityManager.merge(sapKunnr);
                                    } else if (sapKunnr == null && kunnr != null) {
                                        entityManager.remove(kunnr);
                                    }
                                    //Store Sold to party Info
                                    if (sapKunag != null && kunag == null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                        entityManager.persist(sapKunag);
                                    } else if (sapKunag != null && kunag != null) {
                                        entityManager.merge(sapKunag);
                                    } else if (sapKunag == null && kunag != null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                        entityManager.remove(kunag);
                                    }
                                    //Store Vendor Info
                                    if (sapLifnr != null && lifnr == null) {
                                        entityManager.persist(sapLifnr);
                                    } else if (sapLifnr != null && lifnr != null) {
                                        entityManager.merge(sapLifnr);
                                    } else if (sapLifnr == null && lifnr != null) {
                                        entityManager.remove(lifnr);
                                    }
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
                        } else if (rbtMisc.isSelected() || objBapi == null) {
                            weightTicket.setPosted(true);
                            weightTicketDetail.setUnit("TON");
                        }
                    } else {
                        bapi_message = "Đang post phiếu ở chế độ OFFLINE 3024";
                        weightTicket.setPosted(true);
                        weightTicketDetail.setUnit("TON");
                    }
                }
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
                                objBapi = getGrPoMigoBapi(weightTicket);
                            } else if (outbDel != null && outbDel.getLfart().equalsIgnoreCase("LR") && outbDel.getLfart().equalsIgnoreCase("ZTLR")) {
                                objBapi = getPgmVl02nBapi(weightTicket, outbDel);
                            } else {
                                objBapi = getGrDoMigoBapi(weightTicket, outbDel);
                            }
                        } else {
                            if (!rbtMisc.isSelected()) {
                                if (isSubContract() || isMvt311()) {
                                    if (rbtPO.isSelected()) {
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
                                            bapi_message = "No message returned  >> ERR 3048 ";
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
                                            bapi_message = "No message returned  >> ERROR 3086 ";
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
                                            bapi_message = "No message returned ERROR 3123 ";
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
                                            bapi_message = "No message returned ERROR 3160";
                                        }
                                        if (weightTicketDetail.getPpProcord() != null && weightTicketDetail.getPpProcord().length() == 12) {
                                            weightTicketDetail.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
                                            weightTicketDetail.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
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
                                            bapi_message = "Error in BAPI function";
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
                                        OutboundDelivery sapOutb = sapService.getOutboundDelivery(outbDel.getDeliveryOrderNo(), false);
                                        Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                        Vendor lifnr = null, sapLifnr = null;
                                        if (sapOutb != null) {
                                            if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                                kunnr = customerRepository.findByKunnr(sapOutb.getKunnr());
                                                sapKunnr = sapService.getCustomer(sapOutb.getKunnr());
                                            }
                                            if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                                kunag = customerRepository.findByKunnr(sapOutb.getKunag());
                                                sapKunag = sapService.getCustomer(sapOutb.getKunag());
                                            }
                                            if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                                lifnr = vendorRepository.findByLifnr(sapOutb.getLifnr());
                                                sapLifnr = sapService.getVendor(sapOutb.getLifnr());
                                            }
                                        }
                                        //Store Ship to party Info
                                        if (sapKunnr != null && kunnr == null) {
                                            entityManager.persist(sapKunnr);
                                        } else if (sapKunnr != null && kunnr != null) {
                                            entityManager.merge(sapKunnr);
                                        } else if (sapKunnr == null && kunnr != null) {
                                            entityManager.remove(kunnr);
                                        }
                                        //Store Sold to party Info
                                        if (sapKunag != null && kunag == null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                            entityManager.persist(sapKunag);
                                        } else if (sapKunag != null && kunag != null) {
                                            entityManager.merge(sapKunag);
                                        } else if (sapKunag == null && kunag != null && !sapKunnr.getKunnr().equalsIgnoreCase(sapKunag.getKunnr())) {
                                            entityManager.remove(kunag);
                                        }
                                        //Store Vendor Info
                                        if (sapLifnr != null && lifnr == null) {
                                            entityManager.persist(sapLifnr);
                                        } else if (sapLifnr != null && lifnr != null) {
                                            entityManager.merge(sapLifnr);
                                        } else if (sapLifnr == null && lifnr != null) {
                                            entityManager.remove(lifnr);
                                        }
                                        // 120518_17h keep values(posted, mat_doc) which are gotten from Save SAP at the first time
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

                            } else if (rbtMisc.isSelected() || objBapi == null) {
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
            if (rbtMisc.isSelected()) {
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
            String charg = cbxBatch.getSelectedIndex() == -1 && cbxBatch.isEditable()
                    ? cbxBatch.getEditor().getItem().toString().trim().isEmpty() ? null : cbxBatch.getEditor().getItem().toString().trim()
                    : cbxBatch.getSelectedItem().toString();
            //+20100216#01
            if (charg != null) {
                charg.toUpperCase(Locale.ENGLISH);
            }
            //+20100216#01
            weightTicket.setCharg(charg);
            if (rbtMb1b.isSelected() || rbtMvt311.isSelected()) {
                weightTicket.setRecvCharg(charg);
            }
            lblBatch.setForeground(Color.black);
            if (isSubContract() && txfGoodsQty.getValue() != null) {
                setMessage(resourceMapMsg.getString("msg.checkIssetWarehouse"));
                WeightTicketDetail weightTicketDetail = weightTicket.getWeightTicketDetail();
                Double remaining = CheckMatStock(weightTicketDetail.getMatnrRef(), config.getwPlant(), weightTicket.getLgort(), weightTicket.getCharg());
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
            Date now = null;

            formatter.applyPattern(WeighBridgeApp.DATE_TIME_DISPLAY_FORMAT);//      
            now = weightTicketController.getServerTime();
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
                } else if (config.getLimitWeight() > 0
                        && result > config.getLimitWeight()) {
                    JOptionPane.showMessageDialog(rootPane, "K.L hàng không được vượt quá "
                            + config.getLimitWeight() + " tấn!");
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

                    PurchaseOrderDetail purchaseOrderDetail = purOrder.getPurchaseOrderDetail();
                    if (param.equals("") && purOrder != null
                            && purchaseOrderDetail.getMaterial() != null && !purchaseOrderDetail.getMaterial().isEmpty()) {
                        param = purchaseOrderDetail.getMaterial();
                    }
                    Variant vari = variantRepository.findByParam("PROCESS_ORDER_CF");
                    double valu = 0;

                    if (vari != null) {

                        if (!vari.getValue().equals("")) {
                            valu = Double.parseDouble(vari.getValue());
                        }

                        double upper = qty + (qty * valu) / 100;
                        double lower = qty - (qty * valu) / 100;

                        if ((lower <= result && result <= upper)) {
                            txfGoodsQty.setValue(result);
                            weightTicket.setGQty(new BigDecimal(Double.toString(result)));
                        } else {
                            String msg = null;
                            try {
                                msg = weightTicketController.getMsg();
                            } catch (Exception ex) {
                                msg = resourceMapMsg.getString("msg.massOrderOutLimit");
                            }
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
                    Double remaining = CheckMatStock(weightTicket.getWeightTicketDetail().getMatnrRef(), config.getwPlant(), weightTicket.getLgort(), weightTicket.getCharg());
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
                if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                    weightTicket.setFCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                } else {
                    weightTicket.setFCreator(WeighBridgeApp.getApplication().getCurrent_user());
                }
                weightTicket.setFScale(new BigDecimal(((Number) txfInQty.getValue()).doubleValue()));
                weightTicket.setFTime(new java.sql.Date(now.getTime()));
                lblIScale.setForeground(Color.black);
                OutboundDeliveryDetail item = null;
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
                if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                    weightTicket.setSCreator(WeighBridgeApp.getApplication().getLogin().getUid());
                } else {
                    weightTicket.setSCreator(WeighBridgeApp.getApplication().getCurrent_user());
                }
                weightTicket.setSScale(new BigDecimal(((Number) txfOutQty.getValue()).doubleValue()));
                weightTicket.setSTime(new java.sql.Date(now.getTime()));
                lblOScale.setForeground(Color.black);
                OutboundDeliveryDetail item = null;
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
        cbxReason.setEnabled(false);
        cbxCompleted.setEnabled(false);
        cbxKunnr.setSelectedIndex(-1);
        cbxKunnr.setEnabled(false);
        txtGRText.setEnabled(false);
        cbxBatch.setEditable(false);
        chkDissolved.setEnabled(false);
        chkDissolved.setSelected(false);
        this.setFormEnable(false);
        this.setSubContract(false);
        this.setMvt311(false);
        this.setMaterialAvailable(false);
        this.setMatAvailStocks(null);

        grbType.clearSelection();
        grbCat.clearSelection();

        txtPONum.setText(null);
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
        cbxReason.setSelectedIndex(-1);

        txtGRText.setText(null);
        txtRegItem.setText(null);
        txtMatnr.setText(null);
        txtDelNum.setText(null);
        cbxBatch.setModel(new DefaultComboBoxModel());
        txtCementDesc.setText(null);
        cbxCompleted.setSelectedIndex(-1);

        rbtPO.setForeground(Color.black);
        rbtMisc.setForeground(Color.black);
        if (WeighBridgeApp.getApplication().isOfflineMode()) {
            rbtMisc.setForeground(Color.red);
        }
        rbtMb1b.setForeground(Color.black);
        lblIScale.setForeground(Color.black);
        lblOScale.setForeground(Color.black);
        lblGScale.setForeground(Color.black);
        lblSLoc.setForeground(Color.black);
        lblBatch.setForeground(Color.black);

        // Temporary enable btnAccept
//        btnAccept.setEnabled(true);
    }

    private Object getGrDoMigoBapi(WeightTicket wt, OutboundDelivery outbDel) {
        return weightTicketController.getGrDoMigoBapi(wt, weightTicket, outbDel, outDetails_lits, timeFrom, timeTo);
    }

    private Object getGrPoMigoBapi(WeightTicket wt) {
        return weightTicketController.getGrPoMigoBapi(wt, weightTicket, timeFrom, timeTo);
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

    private DefaultComboBoxModel getReasonModel() {
//        return weightTicketController.getReasonModel();
        return new DefaultComboBoxModel();
    }

    private void printWT(WeightTicket wt, boolean reprint) {
        weightTicketController.printWT(wt, reprint, ximang, outbDel_list, outDetails_lits, outbDel, rbtMisc, rbtPO, stage1, rootPane);
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
        if (chkDissolved.isSelected()) {
            bMisc = bPO = bMB1B = bMvt311 = bScale = bSLoc = bBatch = !isDissolved();
            rbtMisc.setForeground(Color.black);
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                rbtMisc.setForeground(Color.red);
            }
            rbtPO.setForeground(Color.black);
            rbtMb1b.setForeground(Color.black);
            rbtMvt311.setForeground(Color.black);
            lblIScale.setForeground(Color.black);
            lblOScale.setForeground(Color.black);
            lblGScale.setForeground(Color.black);
            lblSLoc.setForeground(Color.black);
            lblBatch.setForeground(Color.black);
        } else {
            if (grbType.getSelection() == null && isWithoutDO()) {
                rbtMisc.setForeground(Color.red);
                rbtPO.setForeground(Color.red);
                rbtMb1b.setForeground(Color.red);
                rbtMvt311.setForeground(Color.red);
            } else {
                rbtMisc.setForeground(Color.black);
                if (WeighBridgeApp.getApplication().isOfflineMode()) {
                    rbtMisc.setForeground(Color.red);
                }
                rbtPO.setForeground(Color.black);
                rbtMb1b.setForeground(Color.black);
                rbtMvt311.setForeground(Color.black);
                bMisc = rbtMisc.isSelected();
                bPO = true;
                bMB1B = true;
                bMvt311 = true;
            }
            if (rbtPO.isSelected() && isEnteredValidPONum() && isValidPONum()) {
                rbtPO.setForeground(Color.black);
                bPO = true;
            } else if (rbtPO.isSelected() && (!isEnteredValidPONum() || isValidPONum())) {
                rbtPO.setForeground(Color.red);
                bPO = false;
            }

            if (rbtMb1b.isSelected() && weightTicket.getRecvLgort() == null) {
                rbtMb1b.setForeground(Color.red);
                bMB1B = false;
            } else {
                rbtMb1b.setForeground(Color.black);
                bMB1B = true;
            }

            if (rbtMvt311.isSelected() && (weightTicket.getRecvLgort() == null || weightTicket.getWeightTicketDetail().getRecvMatnr() == null)) {
                rbtMvt311.setForeground(Color.red);
                bMvt311 = false;
            } else {
                rbtMvt311.setForeground(Color.black);
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
            bBatch = !((cbxBatch.getSelectedIndex() == -1 && !cbxBatch.isEditable()) || (cbxBatch.isEditable() && cbxBatch.getEditor().getItem().toString().trim().isEmpty()));
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
                lblBatch.setForeground(Color.black);
            } else {
                lblBatch.setForeground(Color.red);
            }
        }
        bBatch = true;
        txtGRText.setEnabled(true);
        if (outbDel != null) {
//            if (isStage2() && outbDel.getMatnr() != null
//                    && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrXmxa()) && (txtCementDesc.getText().trim() == null || txtCementDesc.getText().trim().equals(""))) {
//                bNiemXa = false;
//            }
            if (bNiemXa) {
                lblCementDesc.setForeground(Color.black);
            } else {
                lblCementDesc.setForeground(Color.red);
            }
        }
        result = (bMisc || bPO || bMB1B || bMvt311) && bScale && bSLoc && bBatch && bNiemXa && (isStage1() || isStage2() || (!isStage1() && !isStage2() && weightTicket != null && weightTicket.isPosted()));
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
    private javax.swing.JComboBox cbxBatch;
    private javax.swing.JComboBox cbxCompleted;
    private javax.swing.JComboBox cbxKunnr;
    private javax.swing.JComboBox cbxReason;
    private javax.swing.JComboBox cbxSLoc;
    private javax.swing.JComboBox cbxVendorLoading;
    private javax.swing.JComboBox cbxVendorTransport;
    private javax.swing.JCheckBox chkDissolved;
    private javax.swing.ButtonGroup grbBridge;
    private javax.swing.ButtonGroup grbCat;
    private javax.swing.ButtonGroup grbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel lbKunnr;
    private javax.swing.JLabel lblBatch;
    private javax.swing.JLabel lblCMNDBL;
    private javax.swing.JLabel lblCementDesc;
    private javax.swing.JLabel lblComplete;
    private javax.swing.JLabel lblDName;
    private javax.swing.JLabel lblDelNum;
    private javax.swing.JLabel lblGRText;
    private javax.swing.JLabel lblGScale;
    private javax.swing.JLabel lblGUnit;
    private javax.swing.JLabel lblIScale;
    private javax.swing.JLabel lblITime;
    private javax.swing.JLabel lblIUnit;
    private javax.swing.JLabel lblKG;
    private javax.swing.JLabel lblLicPlate;
    private javax.swing.JLabel lblMatnr;
    private javax.swing.JLabel lblOScale;
    private javax.swing.JLabel lblOTime;
    private javax.swing.JLabel lblOUnit;
    private javax.swing.JLabel lblPoPosto;
    private javax.swing.JLabel lblReason;
    private javax.swing.JLabel lblRegCat;
    private javax.swing.JLabel lblRegItem;
    private javax.swing.JLabel lblSLoc;
    private javax.swing.JLabel lblTrailerPlate;
    private javax.swing.JLabel lblVendorLoading;
    private javax.swing.JLabel lblVendorTransport;
    private javax.swing.JLabel lblWTNum;
    private com.gcs.wb.jpa.entity.Material material;
    private com.gcs.wb.jpa.entity.OutboundDelivery outbDel;
    private javax.swing.JPanel pnCurScale;
    private javax.swing.JPanel pnCurScaleData;
    private javax.swing.JPanel pnScaleData;
    private javax.swing.JPanel pnWTFilter;
    private javax.swing.JPanel pnWTicket;
    private com.gcs.wb.jpa.entity.PurchaseOrder purOrder;
    private javax.swing.JRadioButton rbtBridge1;
    private javax.swing.JRadioButton rbtBridge2;
    private javax.swing.JRadioButton rbtInward;
    private javax.swing.JRadioButton rbtMb1b;
    private javax.swing.JRadioButton rbtMisc;
    private javax.swing.JRadioButton rbtMvt311;
    private javax.swing.JRadioButton rbtOutward;
    private javax.swing.JRadioButton rbtPO;
    private com.gcs.wb.jpa.entity.SAPSetting setting;
    private javax.swing.JFormattedTextField txfCurScale;
    private javax.swing.JFormattedTextField txfGoodsQty;
    private javax.swing.JFormattedTextField txfInQty;
    private javax.swing.JFormattedTextField txfOutQty;
    private javax.swing.JTextField txtCMNDBL;
    private javax.swing.JTextField txtCementDesc;
    private javax.swing.JTextField txtDName;
    private javax.swing.JTextField txtDelNum;
    private javax.swing.JTextField txtGRText;
    private javax.swing.JTextField txtInTime;
    private javax.swing.JFormattedTextField txtLicPlate;
    private javax.swing.JTextField txtMatnr;
    private javax.swing.JTextField txtOutTime;
    private javax.swing.JTextField txtPONum;
    private javax.swing.JTextField txtPoPosto;
    private javax.swing.JTextField txtRegItem;
    private javax.swing.JFormattedTextField txtTrailerPlate;
    private javax.swing.JTextField txtWTNum;
    private com.gcs.wb.jpa.entity.WeightTicket weightTicket;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
    private static Logger logger = Logger.getLogger(WeightTicketView.class);
    SimpleDateFormat formatter = new SimpleDateFormat();
    private BigDecimal total_qty_goods = BigDecimal.ZERO;
    private BigDecimal remain_qty_goods = BigDecimal.ZERO;
    private BigDecimal total_qty_free = BigDecimal.ZERO;
    private List<OutboundDelivery> outbDel_list = new ArrayList<OutboundDelivery>();
    private List<OutboundDeliveryDetail> outDetails_lits = new ArrayList<OutboundDeliveryDetail>();
    private String wt_ID = null;
    private boolean flag_fail = false;
    private int timeFrom = 0;
    private int timeTo = 0;
    private String ximang = null;
    public org.jdesktop.application.ResourceMap resourceMapMsg = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WeightTicketView.class);
    // </editor-fold>
}
