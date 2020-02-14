/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.views;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;
import com.gcs.wb.base.enums.MaterialEnum;
import com.gcs.wb.base.enums.StatusEnum;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.controller.WeightTicketRegistarationController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.repositorys.TransportAgentVehicleRepository;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.model.WeighingMode;
import com.gcs.wb.views.validations.WeightTicketRegistrationValidation;
import com.sap.conn.jco.JCoException;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class WTRegView extends javax.swing.JInternalFrame {

    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    SAPService sapService = new SAPService();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    WeightTicketRegistarationController weightTicketRegistarationController = new WeightTicketRegistarationController();
    public ResourceMap resourceMapMsg = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WTRegView.class);
    private static final Logger logger = Logger.getLogger(WTRegView.class);
    private final List<WeightTicket> weightTicketList;
    private boolean isValidOutboundDelivery = true;
    private boolean formValid;
    private String abbr;
    private boolean isNeedRevertWeightTicket = false;
    private String outboundDeliveryNo = null;
    private boolean isHasCheck = false;
    private com.gcs.wb.jpa.entity.WeightTicket newWeightTicket;
    private OutboundDelivery outboundDelivery;
    private com.gcs.wb.jpa.entity.WeightTicket selectedWeightTicket;
    private boolean[] editable = null;
    Object[][] wtData = null;
    Object[] wtCols = Constants.WTRegView.WEIGHTTICKET_COLUMS;
    Class[] wtTypes = Constants.WTRegView.WEIGHTTICKET_TYPES;
    // TODO update ui
    private MODE mode;
    private MODE_DETAIL modeDetail;
    private WeightTicketRegistrationValidation wtRegisValidation;
    private List<OutboundDelivery> outboundDeliverys;

    public WTRegView() {
        newWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        selectedWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        outboundDelivery = new com.gcs.wb.jpa.entity.OutboundDelivery();
        weightTicketList = new ArrayList<>();
        outboundDeliverys = new ArrayList<>();
        wtRegisValidation = new WeightTicketRegistrationValidation(rootPane, resourceMapMsg);
        initComponents();
        initComboboxRenderer();

        SearchWeightTicketTask t = new SearchWeightTicketTask(WeighBridgeApp.getApplication());
        t.execute();

        cbxHourTo.setSelectedIndex(23);
        txtNMaterial.setVisible(false);
        cbxNMaterial.setModel(weightTicketRegistarationController.getListMaterial());
    }

    private void initComboboxRenderer() {
        cbxModeType.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof WeighingMode) {
                    WeighingMode mod = (WeighingMode) value;
                    setText(mod.getTitle());
                    setToolTipText(mod.getTitle());
                }
                return this;
            }
        });

        cbxSlocN.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SLoc) {
                    SLoc sloc = (SLoc) value;
                    setText(sloc.getLgort().concat(" - ").concat(sloc.getLgobe()));
                }
                return this;
            }
        });

        cbxBatchStockN.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof BatchStock) {
                    BatchStock batchStock = (BatchStock) value;
                    setText(batchStock.getCharg());
                }
                return this;
            }
        });
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

        rbtRegCatGroup = new javax.swing.ButtonGroup();
        jTextField1 = new javax.swing.JTextField();
        pnFilter = new javax.swing.JPanel();
        lblDateFrom = new javax.swing.JLabel();
        dpDateFrom = new org.jdesktop.swingx.JXDatePicker();
        lblDateTo = new javax.swing.JLabel();
        dpDateTo = new org.jdesktop.swingx.JXDatePicker();
        lblMaterialType = new javax.swing.JLabel();
        cbxMaterialType = new javax.swing.JComboBox();
        lblHourFrom = new javax.swing.JLabel();
        cbxHourFrom = new javax.swing.JComboBox();
        lblHourTo = new javax.swing.JLabel();
        cbxHourTo = new javax.swing.JComboBox();
        lblCreator = new javax.swing.JLabel();
        txtCreator = new javax.swing.JTextField();
        lblDriverName = new javax.swing.JLabel();
        txtDriverName = new javax.swing.JTextField();
        lblPlateNo = new javax.swing.JLabel();
        txtPlateNo = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        cbxStatus = new javax.swing.JComboBox();
        spnResult = new javax.swing.JScrollPane();
        tabResults = new org.jdesktop.swingx.JXTable();
        pnPrintControl = new javax.swing.JPanel();
        btnReprint = new javax.swing.JButton();
        pnRegistrationOfVehicle = new javax.swing.JPanel();
        pnROVTop = new javax.swing.JPanel();
        rbtInput = new javax.swing.JRadioButton();
        rbtOutput = new javax.swing.JRadioButton();
        cbxModeType = new javax.swing.JComboBox();
        pnWTicket = new javax.swing.JPanel();
        pnNLeft = new javax.swing.JPanel();
        lblNDriverName = new javax.swing.JLabel();
        txtNDriverName = new javax.swing.JTextField();
        lblNCMNDBL = new javax.swing.JLabel();
        txtNCMNDBL = new javax.swing.JTextField();
        lblNPlateNo = new javax.swing.JLabel();
        txtNPlateNo = new javax.swing.JTextField();
        lblNTrailerPlate = new javax.swing.JLabel();
        txtNTrailerPlate = new javax.swing.JTextField();
        pnNRight = new javax.swing.JPanel();
        lblNRegCat = new javax.swing.JLabel();
        rbtNInward = new javax.swing.JRadioButton();
        rbtNOutward = new javax.swing.JRadioButton();
        txtNMaterial = new javax.swing.JTextField();
        lblNMaterial = new javax.swing.JLabel();
        cbxNMaterial = new javax.swing.JComboBox();
        lblNWeight = new javax.swing.JLabel();
        txtNWeight = new javax.swing.JFormattedTextField();
        lblNWeightUnit = new javax.swing.JLabel();
        lblNDONum = new javax.swing.JLabel();
        txtNDONum = new javax.swing.JTextField();
        btnCheckDO = new javax.swing.JButton();
        pnROVContent = new javax.swing.JPanel();
        pnROVLeft = new javax.swing.JPanel();
        lblWeightTickerRefN = new javax.swing.JLabel();
        lblRegisterIdN = new javax.swing.JLabel();
        lblDriverNameN = new javax.swing.JLabel();
        lblCMNDN = new javax.swing.JLabel();
        lblPlateNoN = new javax.swing.JLabel();
        lblTonnageN = new javax.swing.JLabel();
        lblTonngageUnitN = new javax.swing.JLabel();
        lblTrailerNoN = new javax.swing.JLabel();
        lblSlingN = new javax.swing.JLabel();
        lblPalletN = new javax.swing.JLabel();
        lblSoNiemXaN = new javax.swing.JLabel();
        lblProductionBatchN = new javax.swing.JLabel();
        lblNoteN = new javax.swing.JLabel();
        txtWeightTickerRefN = new javax.swing.JTextField();
        txtRegisterIdN = new javax.swing.JTextField();
        txtDriverNameN = new javax.swing.JTextField();
        txtCMNDN = new javax.swing.JTextField();
        txtPlateNoN = new javax.swing.JTextField();
        txtTonnageN = new javax.swing.JFormattedTextField();
        txtTrailerNoN = new javax.swing.JTextField();
        txtSlingN = new javax.swing.JFormattedTextField();
        txtPalletN = new javax.swing.JFormattedTextField();
        txtSoNiemXaN = new javax.swing.JTextField();
        txtProductionBatchN = new javax.swing.JTextField();
        txtNoteN = new javax.swing.JTextField();
        lblTicketIdN = new javax.swing.JLabel();
        txtTicketIdN = new javax.swing.JTextField();
        pnROVRight = new javax.swing.JPanel();
        lblMaterialTypeN = new javax.swing.JLabel();
        lblWeightN = new javax.swing.JLabel();
        lblWeightUnitN = new javax.swing.JLabel();
        lblSlocN = new javax.swing.JLabel();
        lblSloc2N = new javax.swing.JLabel();
        lblBatchStockN = new javax.swing.JLabel();
        lblBatchStock2N = new javax.swing.JLabel();
        lblDONumN = new javax.swing.JLabel();
        lblSONumN = new javax.swing.JLabel();
        lblPONumN = new javax.swing.JLabel();
        lblPOSTONumN = new javax.swing.JLabel();
        lblVendorLoadingN = new javax.swing.JLabel();
        lblVendorTransportN = new javax.swing.JLabel();
        lblSuppliesIdN = new javax.swing.JLabel();
        cbxMaterialTypeN = new javax.swing.JComboBox();
        txtWeightN = new javax.swing.JFormattedTextField();
        cbxSlocN = new javax.swing.JComboBox();
        cbxSloc2N = new javax.swing.JComboBox();
        cbxBatchStockN = new javax.swing.JComboBox();
        cbxBatchStock2N = new javax.swing.JComboBox();
        txtDONumN = new javax.swing.JTextField();
        btnDOCheckN = new javax.swing.JButton();
        txtSONumN = new javax.swing.JTextField();
        btnSOCheckN = new javax.swing.JButton();
        txtPONumN = new javax.swing.JTextField();
        btnPOCheckN = new javax.swing.JButton();
        txtPOSTONumN = new javax.swing.JTextField();
        btnPOSTOCheckN = new javax.swing.JButton();
        cbxVendorLoadingN = new javax.swing.JComboBox();
        cbxVendorTransportN = new javax.swing.JComboBox();
        txtSuppliesIdN = new javax.swing.JTextField();
        pnControl = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnMany = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        lblWeightTicketNo = new javax.swing.JLabel();
        txtWeightTicketNo = new javax.swing.JTextField();
        btnSo = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WTRegView.class);
        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnFilter.border.title"))); // NOI18N
        pnFilter.setMinimumSize(new java.awt.Dimension(12, 137));
        pnFilter.setName("pnFilter"); // NOI18N

        lblDateFrom.setText(resourceMap.getString("lblDateFrom.text")); // NOI18N
        lblDateFrom.setName("lblDateFrom"); // NOI18N

        dpDateFrom.setDate(Calendar.getInstance().getTime());
        dpDateFrom.setName("dpDateFrom"); // NOI18N
        dpDateFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dpDateFromActionPerformed(evt);
            }
        });

        lblDateTo.setText(resourceMap.getString("lblDateTo.text")); // NOI18N
        lblDateTo.setName("lblDateTo"); // NOI18N

        dpDateTo.setDate(Calendar.getInstance().getTime());
        dpDateTo.setName("dpDateTo"); // NOI18N

        lblMaterialType.setLabelFor(cbxMaterialType);
        lblMaterialType.setText(resourceMap.getString("lblMaterialType.text")); // NOI18N
        lblMaterialType.setName("lblMaterialType"); // NOI18N

        cbxMaterialType.setModel(getMatsModel());
        cbxMaterialType.setName("cbxMaterialType"); // NOI18N
        cbxMaterialType.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Material) {
                    Material mat = (Material)value;
                    setText(mat.getMaktx());
                    setToolTipText(mat.getMatnr());
                }
                return this;
            }
        });

        lblHourFrom.setText(resourceMap.getString("lblHourFrom.text")); // NOI18N
        lblHourFrom.setName("lblHourFrom"); // NOI18N

        cbxHourFrom.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cbxHourFrom.setName("cbxHourFrom"); // NOI18N

        lblHourTo.setText(resourceMap.getString("lblHourTo.text")); // NOI18N
        lblHourTo.setName("lblHourTo"); // NOI18N

        cbxHourTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cbxHourTo.setName("cbxHourTo"); // NOI18N
        cbxHourTo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxHourToItemStateChanged(evt);
            }
        });

        lblCreator.setLabelFor(txtCreator);
        lblCreator.setText(resourceMap.getString("lblCreator.text")); // NOI18N
        lblCreator.setName("lblCreator"); // NOI18N

        txtCreator.setName("txtCreator"); // NOI18N

        lblDriverName.setLabelFor(txtCreator);
        lblDriverName.setText(resourceMap.getString("lblDriverName.text")); // NOI18N
        lblDriverName.setName("lblDriverName"); // NOI18N

        txtDriverName.setName("txtDriverName"); // NOI18N

        lblPlateNo.setText(resourceMap.getString("lblPlateNo.text")); // NOI18N
        lblPlateNo.setName("lblPlateNo"); // NOI18N

        txtPlateNo.setName("txtPlateNo"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WTRegView.class, this);
        btnFind.setAction(actionMap.get("searchWeightTickets")); // NOI18N
        btnFind.setText(resourceMap.getString("btnFind.text")); // NOI18N
        btnFind.setName("btnFind"); // NOI18N

        lblStatus.setText(resourceMap.getString("lblStatus.text")); // NOI18N
        lblStatus.setName("lblStatus"); // NOI18N

        cbxStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tất cả", "Hoàn tất" }));
        cbxStatus.setName("cbxStatus"); // NOI18N

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDriverName)
                    .addComponent(lblDateFrom)
                    .addComponent(lblMaterialType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addComponent(btnFind)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 633, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxMaterialType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDriverName)
                            .addComponent(dpDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPlateNo)
                            .addComponent(lblDateTo)
                            .addComponent(lblStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxStatus, 0, 166, Short.MAX_VALUE)
                            .addComponent(txtPlateNo, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                            .addComponent(dpDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblHourFrom)
                            .addComponent(lblCreator))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnFilterLayout.createSequentialGroup()
                                .addComponent(cbxHourFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(lblHourTo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbxHourTo, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtCreator, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))))
                .addGap(86, 86, 86))
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxHourTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbxHourFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHourFrom)
                        .addComponent(lblHourTo))
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(dpDateFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDateFrom))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPlateNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPlateNo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCreator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCreator))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStatus)
                            .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxMaterialType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaterialType)))
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dpDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDateTo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFind)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        spnResult.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("spnResult.border.title"))); // NOI18N
        spnResult.setName("spnResult"); // NOI18N

        tabResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabResults.setEditable(false);
        tabResults.setName("tabResults"); // NOI18N
        tabResults.setShowGrid(true);
        spnResult.setViewportView(tabResults);

        pnPrintControl.setName("pnPrintControl"); // NOI18N
        pnPrintControl.setLayout(new javax.swing.BoxLayout(pnPrintControl, javax.swing.BoxLayout.LINE_AXIS));

        btnReprint.setAction(actionMap.get("reprintRecord")); // NOI18N
        btnReprint.setText(resourceMap.getString("btnReprint.text")); // NOI18N
        btnReprint.setName("btnReprint"); // NOI18N
        pnPrintControl.add(btnReprint);

        pnRegistrationOfVehicle.setName("pnRegistrationOfVehicle"); // NOI18N

        pnROVTop.setName("pnROVTop"); // NOI18N

        rbtRegCatGroup.add(rbtInput);
        rbtInput.setSelected(true);
        rbtInput.setText(resourceMap.getString("rbtInput.text")); // NOI18N
        rbtInput.setName("rbtInput"); // NOI18N
        rbtInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtInputActionPerformed(evt);
            }
        });

        rbtRegCatGroup.add(rbtOutput);
        rbtOutput.setText(resourceMap.getString("rbtOutput.text")); // NOI18N
        rbtOutput.setName("rbtOutput"); // NOI18N
        rbtOutput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtOutputActionPerformed(evt);
            }
        });

        cbxModeType.setName("cbxModeType"); // NOI18N
        cbxModeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxModeTypeActionPerformed(evt);
            }
        });

        pnWTicket.setName("pnWTicket"); // NOI18N
        pnWTicket.setLayout(new javax.swing.BoxLayout(pnWTicket, javax.swing.BoxLayout.LINE_AXIS));

        pnNLeft.setName("pnNLeft"); // NOI18N

        lblNDriverName.setText(resourceMap.getString("lblNDriverName.text")); // NOI18N
        lblNDriverName.setName("lblNDriverName"); // NOI18N

        txtNDriverName.setName("txtNDriverName"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNDriverName, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNDriverName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNDriverNameActionPerformed(evt);
            }
        });
        txtNDriverName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNDriverNameFocusLost(evt);
            }
        });

        lblNCMNDBL.setText(resourceMap.getString("lblNCMNDBL.text")); // NOI18N
        lblNCMNDBL.setName("lblNCMNDBL"); // NOI18N

        txtNCMNDBL.setName("txtNCMNDBL"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNCMNDBL, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNCMNDBL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNCMNDBLActionPerformed(evt);
            }
        });
        txtNCMNDBL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNCMNDBLFocusLost(evt);
            }
        });

        lblNPlateNo.setText(resourceMap.getString("lblNPlateNo.text")); // NOI18N
        lblNPlateNo.setName("lblNPlateNo"); // NOI18N

        txtNPlateNo.setName("txtNPlateNo"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNPlateNo, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNPlateNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNPlateNoActionPerformed(evt);
            }
        });
        txtNPlateNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNPlateNoFocusLost(evt);
            }
        });

        lblNTrailerPlate.setText(resourceMap.getString("lblNTrailerPlate.text")); // NOI18N
        lblNTrailerPlate.setName("lblNTrailerPlate"); // NOI18N

        txtNTrailerPlate.setName("txtNTrailerPlate"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNTrailerPlate, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNTrailerPlate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNTrailerPlateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnNLeftLayout = new javax.swing.GroupLayout(pnNLeft);
        pnNLeft.setLayout(pnNLeftLayout);
        pnNLeftLayout.setHorizontalGroup(
            pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNTrailerPlate)
                    .addComponent(lblNDriverName)
                    .addComponent(lblNCMNDBL)
                    .addComponent(lblNPlateNo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNCMNDBL)
                    .addComponent(txtNPlateNo)
                    .addComponent(txtNTrailerPlate)
                    .addComponent(txtNDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnNLeftLayout.setVerticalGroup(
            pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNDriverName)
                    .addComponent(txtNDriverName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNCMNDBL)
                    .addComponent(txtNCMNDBL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNPlateNo)
                    .addComponent(txtNPlateNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNTrailerPlate)
                    .addComponent(txtNTrailerPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnWTicket.add(pnNLeft);

        pnNRight.setName("pnNRight"); // NOI18N

        lblNRegCat.setText(resourceMap.getString("lblNRegCat.text")); // NOI18N
        lblNRegCat.setName("lblNRegCat"); // NOI18N

        rbtNInward.setText(resourceMap.getString("rbtNInward.text")); // NOI18N
        rbtNInward.setName("rbtNInward"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${rbtEnabled}"), rbtNInward, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtNInward.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtNInwardItemStateChanged(evt);
            }
        });

        rbtNOutward.setText(resourceMap.getString("rbtNOutward.text")); // NOI18N
        rbtNOutward.setName("rbtNOutward"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${rbtEnabled}"), rbtNOutward, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        rbtNOutward.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rbtNOutwardItemStateChanged(evt);
            }
        });

        txtNMaterial.setName("txtNMaterial"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNMaterial, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNMaterialActionPerformed(evt);
            }
        });
        txtNMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNMaterialKeyReleased(evt);
            }
        });

        lblNMaterial.setText(resourceMap.getString("lblNMaterial.text")); // NOI18N
        lblNMaterial.setName("lblNMaterial"); // NOI18N

        cbxNMaterial.setEditable(true);
        cbxNMaterial.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " " }));
        cbxNMaterial.setName("cbxNMaterial"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), cbxNMaterial, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        cbxNMaterial.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxNMaterialItemStateChanged(evt);
            }
        });
        cbxNMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNMaterialActionPerformed(evt);
            }
        });
        cbxNMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbxNMaterialKeyReleased(evt);
            }
        });

        lblNWeight.setText(resourceMap.getString("lblNWeight.text")); // NOI18N
        lblNWeight.setName("lblNWeight"); // NOI18N

        txtNWeight.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtNWeight.setName("txtNWeight"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNWeight, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNWeight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNWeightActionPerformed(evt);
            }
        });
        txtNWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNWeightKeyReleased(evt);
            }
        });

        lblNWeightUnit.setText(resourceMap.getString("lblNWeightUnit.text")); // NOI18N
        lblNWeightUnit.setName("lblNWeightUnit"); // NOI18N

        lblNDONum.setText(resourceMap.getString("lblNDONum.text")); // NOI18N
        lblNDONum.setName("lblNDONum"); // NOI18N

        txtNDONum.setEditable(false);
        txtNDONum.setAction(actionMap.get("checkDO")); // NOI18N
        txtNDONum.setName("txtNDONum"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${formEditable}"), txtNDONum, org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        txtNDONum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNDONumActionPerformed(evt);
            }
        });
        txtNDONum.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNDONumKeyReleased(evt);
            }
        });

        btnCheckDO.setAction(actionMap.get("checkDO")); // NOI18N
        btnCheckDO.setText(resourceMap.getString("btnCheckDO.text")); // NOI18N
        btnCheckDO.setName("btnCheckDO"); // NOI18N
        btnCheckDO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheckDOActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnNRightLayout = new javax.swing.GroupLayout(pnNRight);
        pnNRight.setLayout(pnNRightLayout);
        pnNRightLayout.setHorizontalGroup(
            pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNRightLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnNRightLayout.createSequentialGroup()
                        .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblNDONum)
                            .addComponent(lblNWeight))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnNRightLayout.createSequentialGroup()
                            .addGap(11, 11, 11)
                            .addComponent(lblNMaterial))
                        .addGroup(pnNRightLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(lblNRegCat, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE))))
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnNRightLayout.createSequentialGroup()
                        .addComponent(rbtNInward)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtNOutward)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtNMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cbxNMaterial, 0, 198, Short.MAX_VALUE)
                    .addComponent(txtNDONum, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                    .addComponent(txtNWeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNWeightUnit)
                    .addComponent(btnCheckDO, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(110, 110, 110))
        );
        pnNRightLayout.setVerticalGroup(
            pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNRegCat)
                    .addComponent(rbtNInward)
                    .addComponent(rbtNOutward)
                    .addComponent(txtNMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxNMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNMaterial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNWeightUnit)
                    .addComponent(lblNWeight))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnNRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNDONum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNDONum)
                    .addComponent(btnCheckDO))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnWTicket.add(pnNRight);

        javax.swing.GroupLayout pnROVTopLayout = new javax.swing.GroupLayout(pnROVTop);
        pnROVTop.setLayout(pnROVTopLayout);
        pnROVTopLayout.setHorizontalGroup(
            pnROVTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVTopLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(rbtInput)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbtOutput)
                .addGroup(pnROVTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnROVTopLayout.createSequentialGroup()
                        .addGap(371, 371, 371)
                        .addComponent(pnWTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnROVTopLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxModeType, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(317, Short.MAX_VALUE))
        );
        pnROVTopLayout.setVerticalGroup(
            pnROVTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVTopLayout.createSequentialGroup()
                .addContainerGap(22, Short.MAX_VALUE)
                .addComponent(pnWTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnROVTopLayout.createSequentialGroup()
                .addGroup(pnROVTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxModeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtOutput)
                    .addComponent(rbtInput))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnROVContent.setName("pnROVContent"); // NOI18N

        pnROVLeft.setName("pnROVLeft"); // NOI18N

        lblWeightTickerRefN.setText(resourceMap.getString("lblWeightTickerRefN.text")); // NOI18N
        lblWeightTickerRefN.setName("lblWeightTickerRefN"); // NOI18N

        lblRegisterIdN.setText(resourceMap.getString("lblRegisterIdN.text")); // NOI18N
        lblRegisterIdN.setName("lblRegisterIdN"); // NOI18N

        lblDriverNameN.setText(resourceMap.getString("lblDriverNameN.text")); // NOI18N
        lblDriverNameN.setName("lblDriverNameN"); // NOI18N

        lblCMNDN.setText(resourceMap.getString("lblCMNDN.text")); // NOI18N
        lblCMNDN.setName("lblCMNDN"); // NOI18N

        lblPlateNoN.setText(resourceMap.getString("lblPlateNoN.text")); // NOI18N
        lblPlateNoN.setName("lblPlateNoN"); // NOI18N

        lblTonnageN.setText(resourceMap.getString("lblTonnageN.text")); // NOI18N
        lblTonnageN.setName("lblTonnageN"); // NOI18N

        lblTonngageUnitN.setText(resourceMap.getString("lblTonngageUnitN.text")); // NOI18N
        lblTonngageUnitN.setName("lblTonngageUnitN"); // NOI18N

        lblTrailerNoN.setText(resourceMap.getString("lblTrailerNoN.text")); // NOI18N
        lblTrailerNoN.setName("lblTrailerNoN"); // NOI18N

        lblSlingN.setText(resourceMap.getString("lblSlingN.text")); // NOI18N
        lblSlingN.setName("lblSlingN"); // NOI18N

        lblPalletN.setText(resourceMap.getString("lblPalletN.text")); // NOI18N
        lblPalletN.setName("lblPalletN"); // NOI18N

        lblSoNiemXaN.setText(resourceMap.getString("lblSoNiemXaN.text")); // NOI18N
        lblSoNiemXaN.setName("lblSoNiemXaN"); // NOI18N

        lblProductionBatchN.setText(resourceMap.getString("lblProductionBatchN.text")); // NOI18N
        lblProductionBatchN.setName("lblProductionBatchN"); // NOI18N

        lblNoteN.setText(resourceMap.getString("lblNoteN.text")); // NOI18N
        lblNoteN.setName("lblNoteN"); // NOI18N

        txtWeightTickerRefN.setName("txtWeightTickerRefN"); // NOI18N
        txtWeightTickerRefN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWeightTickerRefNKeyReleased(evt);
            }
        });

        txtRegisterIdN.setName("txtRegisterIdN"); // NOI18N
        txtRegisterIdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtRegisterIdNKeyReleased(evt);
            }
        });

        txtDriverNameN.setName("txtDriverNameN"); // NOI18N
        txtDriverNameN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDriverNameNKeyReleased(evt);
            }
        });

        txtCMNDN.setName("txtCMNDN"); // NOI18N
        txtCMNDN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCMNDNKeyReleased(evt);
            }
        });

        txtPlateNoN.setName("txtPlateNoN"); // NOI18N
        txtPlateNoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlateNoNKeyReleased(evt);
            }
        });

        txtTonnageN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtTonnageN.setText(resourceMap.getString("txtTonnageN.text")); // NOI18N
        txtTonnageN.setName("txtTonnageN"); // NOI18N

        txtTrailerNoN.setName("txtTrailerNoN"); // NOI18N

        txtSlingN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtSlingN.setText(resourceMap.getString("txtSlingN.text")); // NOI18N
        txtSlingN.setName("txtSlingN"); // NOI18N
        txtSlingN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSlingNKeyReleased(evt);
            }
        });

        txtPalletN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        txtPalletN.setText(resourceMap.getString("txtPalletN.text")); // NOI18N
        txtPalletN.setName("txtPalletN"); // NOI18N
        txtPalletN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPalletNKeyReleased(evt);
            }
        });

        txtSoNiemXaN.setName("txtSoNiemXaN"); // NOI18N

        txtProductionBatchN.setName("txtProductionBatchN"); // NOI18N

        txtNoteN.setName("txtNoteN"); // NOI18N

        lblTicketIdN.setText(resourceMap.getString("lblTicketIdN.text")); // NOI18N
        lblTicketIdN.setName("lblTicketIdN"); // NOI18N

        txtTicketIdN.setName("txtTicketIdN"); // NOI18N

        javax.swing.GroupLayout pnROVLeftLayout = new javax.swing.GroupLayout(pnROVLeft);
        pnROVLeft.setLayout(pnROVLeftLayout);
        pnROVLeftLayout.setHorizontalGroup(
            pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVLeftLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblTicketIdN)
                    .addComponent(lblWeightTickerRefN)
                    .addComponent(lblRegisterIdN)
                    .addComponent(lblDriverNameN)
                    .addComponent(lblCMNDN)
                    .addComponent(lblPlateNoN)
                    .addComponent(lblTrailerNoN)
                    .addComponent(lblSlingN)
                    .addComponent(lblPalletN)
                    .addComponent(lblSoNiemXaN)
                    .addComponent(lblProductionBatchN)
                    .addComponent(lblNoteN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTicketIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtNoteN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtProductionBatchN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtCMNDN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtTrailerNoN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addGroup(pnROVLeftLayout.createSequentialGroup()
                        .addComponent(txtPlateNoN, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTonnageN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTonnageN, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTonngageUnitN))
                    .addComponent(txtWeightTickerRefN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtDriverNameN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtRegisterIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtSlingN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtPalletN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );
        pnROVLeftLayout.setVerticalGroup(
            pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTicketIdN)
                    .addComponent(txtTicketIdN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWeightTickerRefN)
                    .addComponent(txtWeightTickerRefN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRegisterIdN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblRegisterIdN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDriverNameN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDriverNameN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCMNDN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCMNDN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlateNoN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTonnageN)
                    .addComponent(txtTonnageN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTonngageUnitN)
                    .addComponent(lblPlateNoN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTrailerNoN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTrailerNoN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSlingN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSlingN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPalletN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPalletN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSoNiemXaN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductionBatchN)
                    .addComponent(lblProductionBatchN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoteN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNoteN))
                .addGap(67, 67, 67))
        );

        pnROVRight.setName("pnROVRight"); // NOI18N

        lblMaterialTypeN.setText(resourceMap.getString("lblMaterialTypeN.text")); // NOI18N
        lblMaterialTypeN.setName("lblMaterialTypeN"); // NOI18N

        lblWeightN.setText(resourceMap.getString("lblWeightN.text")); // NOI18N
        lblWeightN.setName("lblWeightN"); // NOI18N

        lblWeightUnitN.setText(resourceMap.getString("lblWeightUnitN.text")); // NOI18N
        lblWeightUnitN.setName("lblWeightUnitN"); // NOI18N

        lblSlocN.setText(resourceMap.getString("lblSlocN.text")); // NOI18N
        lblSlocN.setName("lblSlocN"); // NOI18N

        lblSloc2N.setText(resourceMap.getString("lblSloc2N.text")); // NOI18N
        lblSloc2N.setName("lblSloc2N"); // NOI18N

        lblBatchStockN.setText(resourceMap.getString("lblBatchStockN.text")); // NOI18N
        lblBatchStockN.setName("lblBatchStockN"); // NOI18N

        lblBatchStock2N.setText(resourceMap.getString("lblBatchStock2N.text")); // NOI18N
        lblBatchStock2N.setName("lblBatchStock2N"); // NOI18N

        lblDONumN.setText(resourceMap.getString("lblDONumN.text")); // NOI18N
        lblDONumN.setName("lblDONumN"); // NOI18N

        lblSONumN.setText(resourceMap.getString("lblSONumN.text")); // NOI18N
        lblSONumN.setName("lblSONumN"); // NOI18N

        lblPONumN.setText(resourceMap.getString("lblPONumN.text")); // NOI18N
        lblPONumN.setName("lblPONumN"); // NOI18N

        lblPOSTONumN.setText(resourceMap.getString("lblPOSTONumN.text")); // NOI18N
        lblPOSTONumN.setName("lblPOSTONumN"); // NOI18N

        lblVendorLoadingN.setText(resourceMap.getString("lblVendorLoadingN.text")); // NOI18N
        lblVendorLoadingN.setName("lblVendorLoadingN"); // NOI18N

        lblVendorTransportN.setText(resourceMap.getString("lblVendorTransportN.text")); // NOI18N
        lblVendorTransportN.setName("lblVendorTransportN"); // NOI18N

        lblSuppliesIdN.setText(resourceMap.getString("lblSuppliesIdN.text")); // NOI18N
        lblSuppliesIdN.setName("lblSuppliesIdN"); // NOI18N

        cbxMaterialTypeN.setEditable(true);
        cbxMaterialTypeN.setName("cbxMaterialTypeN"); // NOI18N

        txtWeightN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtWeightN.setText(resourceMap.getString("txtWeightN.text")); // NOI18N
        txtWeightN.setName("txtWeightN"); // NOI18N

        cbxSlocN.setName("cbxSlocN"); // NOI18N
        cbxSlocN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSlocNActionPerformed(evt);
            }
        });

        cbxSloc2N.setName("cbxSloc2N"); // NOI18N

        cbxBatchStockN.setName("cbxBatchStockN"); // NOI18N
        cbxBatchStockN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBatchStockNActionPerformed(evt);
            }
        });

        cbxBatchStock2N.setName("cbxBatchStock2N"); // NOI18N

        txtDONumN.setName("txtDONumN"); // NOI18N
        txtDONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDONumNKeyReleased(evt);
            }
        });

        btnDOCheckN.setAction(actionMap.get("checkDO")); // NOI18N
        btnDOCheckN.setText(resourceMap.getString("btnDOCheckN.text")); // NOI18N
        btnDOCheckN.setName("btnDOCheckN"); // NOI18N

        txtSONumN.setName("txtSONumN"); // NOI18N

        btnSOCheckN.setText(resourceMap.getString("btnSOCheckN.text")); // NOI18N
        btnSOCheckN.setName("btnSOCheckN"); // NOI18N

        txtPONumN.setName("txtPONumN"); // NOI18N

        btnPOCheckN.setText(resourceMap.getString("btnPOCheckN.text")); // NOI18N
        btnPOCheckN.setName("btnPOCheckN"); // NOI18N

        txtPOSTONumN.setName("txtPOSTONumN"); // NOI18N

        btnPOSTOCheckN.setText(resourceMap.getString("btnPOSTOCheckN.text")); // NOI18N
        btnPOSTOCheckN.setName("btnPOSTOCheckN"); // NOI18N

        cbxVendorLoadingN.setName("cbxVendorLoadingN"); // NOI18N

        cbxVendorTransportN.setName("cbxVendorTransportN"); // NOI18N

        txtSuppliesIdN.setName("txtSuppliesIdN"); // NOI18N

        javax.swing.GroupLayout pnROVRightLayout = new javax.swing.GroupLayout(pnROVRight);
        pnROVRight.setLayout(pnROVRightLayout);
        pnROVRightLayout.setHorizontalGroup(
            pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSloc2N)
                    .addComponent(lblBatchStockN)
                    .addComponent(lblBatchStock2N)
                    .addComponent(lblSlocN)
                    .addComponent(lblMaterialTypeN)
                    .addComponent(lblPOSTONumN)
                    .addComponent(lblPONumN)
                    .addGroup(pnROVRightLayout.createSequentialGroup()
                        .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblDONumN)
                            .addComponent(lblSONumN))
                        .addGap(1, 1, 1))
                    .addComponent(lblVendorLoadingN)
                    .addComponent(lblVendorTransportN)
                    .addComponent(lblSuppliesIdN)
                    .addComponent(lblWeightN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSuppliesIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(cbxVendorTransportN, 0, 271, Short.MAX_VALUE)
                    .addComponent(cbxVendorLoadingN, 0, 271, Short.MAX_VALUE)
                    .addComponent(txtSONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtPONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtPOSTONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(cbxMaterialTypeN, 0, 271, Short.MAX_VALUE)
                    .addComponent(cbxBatchStock2N, 0, 271, Short.MAX_VALUE)
                    .addComponent(cbxBatchStockN, 0, 271, Short.MAX_VALUE)
                    .addComponent(cbxSloc2N, 0, 271, Short.MAX_VALUE)
                    .addComponent(cbxSlocN, 0, 271, Short.MAX_VALUE)
                    .addComponent(txtWeightN, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addComponent(txtDONumN, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSOCheckN)
                    .addComponent(btnDOCheckN)
                    .addComponent(btnPOCheckN)
                    .addComponent(btnPOSTOCheckN)
                    .addComponent(lblWeightUnitN))
                .addGap(29, 29, 29))
        );
        pnROVRightLayout.setVerticalGroup(
            pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDONumN)
                    .addComponent(txtDONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDOCheckN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSONumN)
                    .addComponent(txtSONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSOCheckN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPONumN)
                    .addComponent(txtPONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPOCheckN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPOSTONumN)
                    .addComponent(txtPOSTONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPOSTOCheckN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxMaterialTypeN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaterialTypeN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtWeightN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblWeightN))
                    .addGroup(pnROVRightLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(lblWeightUnitN)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSlocN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSlocN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxSloc2N, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSloc2N))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxBatchStockN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBatchStockN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxBatchStock2N, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblBatchStock2N))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVendorLoadingN)
                    .addComponent(cbxVendorLoadingN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorTransportN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorTransportN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSuppliesIdN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSuppliesIdN))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnROVContentLayout = new javax.swing.GroupLayout(pnROVContent);
        pnROVContent.setLayout(pnROVContentLayout);
        pnROVContentLayout.setHorizontalGroup(
            pnROVContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVContentLayout.createSequentialGroup()
                .addComponent(pnROVLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnROVRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnROVContentLayout.setVerticalGroup(
            pnROVContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnROVRight, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
            .addComponent(pnROVLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnRegistrationOfVehicleLayout = new javax.swing.GroupLayout(pnRegistrationOfVehicle);
        pnRegistrationOfVehicle.setLayout(pnRegistrationOfVehicleLayout);
        pnRegistrationOfVehicleLayout.setHorizontalGroup(
            pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                .addGroup(pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnROVContent, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnROVTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnRegistrationOfVehicleLayout.setVerticalGroup(
            pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                .addComponent(pnROVTop, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnROVContent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        pnControl.setName("pnControl"); // NOI18N

        btnNew.setAction(actionMap.get("newRecord")); // NOI18N
        btnNew.setText(resourceMap.getString("btnNew.text")); // NOI18N
        btnNew.setName("btnNew"); // NOI18N

        btnClear.setAction(actionMap.get("clearForm")); // NOI18N
        btnClear.setText(resourceMap.getString("btnClear.text")); // NOI18N
        btnClear.setName("btnClear"); // NOI18N

        btnMany.setText(resourceMap.getString("btnMany.text")); // NOI18N
        btnMany.setEnabled(false);
        btnMany.setName("btnMany"); // NOI18N
        btnMany.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnManyActionPerformed(evt);
            }
        });

        btnSave.setAction(actionMap.get("saveRecord")); // NOI18N
        btnSave.setText(resourceMap.getString("btnSave.text")); // NOI18N
        btnSave.setName("btnSave"); // NOI18N

        lblWeightTicketNo.setText(resourceMap.getString("lblWeightTicketNo.text")); // NOI18N
        lblWeightTicketNo.setName("lblWeightTicketNo"); // NOI18N

        txtWeightTicketNo.setEditable(false);
        txtWeightTicketNo.setAutoscrolls(false);
        txtWeightTicketNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtWeightTicketNo.setName("txtWeightTicketNo"); // NOI18N

        btnSo.setText(resourceMap.getString("btnSo.text")); // NOI18N
        btnSo.setEnabled(false);
        btnSo.setName("btnSo"); // NOI18N
        btnSo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnControlLayout = new javax.swing.GroupLayout(pnControl);
        pnControl.setLayout(pnControlLayout);
        pnControlLayout.setHorizontalGroup(
            pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnControlLayout.createSequentialGroup()
                .addComponent(btnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnMany)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSave)
                .addGap(29, 29, 29)
                .addComponent(lblWeightTicketNo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtWeightTicketNo, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135))
        );
        pnControlLayout.setVerticalGroup(
            pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnControlLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew)
                    .addComponent(btnClear)
                    .addComponent(btnMany)
                    .addComponent(btnSo)
                    .addComponent(btnSave)
                    .addComponent(txtWeightTicketNo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWeightTicketNo)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnRegistrationOfVehicle, 0, 921, Short.MAX_VALUE)
                    .addComponent(pnPrintControl, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                    .addComponent(spnResult, javax.swing.GroupLayout.DEFAULT_SIZE, 921, Short.MAX_VALUE)
                    .addComponent(pnFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnControl, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spnResult, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnPrintControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnRegistrationOfVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 415, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Old Code">
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Event methods">
    private void rbtNInwardItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtNInwardItemStateChanged
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_rbtNInwardItemStateChanged

    private void rbtNOutwardItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rbtNOutwardItemStateChanged
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_rbtNOutwardItemStateChanged

    private void txtNMaterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNMaterialKeyReleased
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNMaterialKeyReleased

    private void txtNWeightKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNWeightKeyReleased
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNWeightKeyReleased

    private void txtNDONumKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNDONumKeyReleased
        if (WeighBridgeApp.getApplication().isOfflineMode()) {
            return;
        }

        if (txtNDONum.getText().trim() == null) {
            return;
        }
        int DOLength = txtNDONum.getText().trim().length();
        if (DOLength == 0 || (isValidOutboundDelivery && DOLength >= 7 && isValidOutboundDelivery && DOLength <= 10)) {
            if ((evt.isControlDown() && evt.getKeyCode() == KeyEvent.VK_V) || (evt.isShiftDown() && evt.getKeyCode() == KeyEvent.VK_INSERT)) {
                isValidOutboundDelivery = false;
                txtNDONum.setForeground(Color.red);
            } else {
                txtNDONum.setForeground(Color.black);
            }
        } else {
            isValidOutboundDelivery = false;
            txtNDONum.setForeground(Color.red);
        }

        if (DOLength == 0) {
            isValidOutboundDelivery = true;
            setRbtEnabled(true);
            txtNMaterial.setEditable(true);
            txtNWeight.setEditable(true);
        } else {
            txtNMaterial.setEditable(false);
            txtNWeight.setEditable(false);
        }

        if (isValidOutboundDelivery) {
            setSaveNeeded(isValidated());
        }
    }//GEN-LAST:event_txtNDONumKeyReleased

    private void txtNPlateNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNPlateNoActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNPlateNoActionPerformed

    private void txtNDriverNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNDriverNameActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNDriverNameActionPerformed

    private void txtNCMNDBLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNCMNDBLActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNCMNDBLActionPerformed

    private void txtNMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNMaterialActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNMaterialActionPerformed

    private void txtNWeightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNWeightActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNWeightActionPerformed

    private void txtNDONumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNDONumActionPerformed
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNDONumActionPerformed

    private void txtNTrailerPlateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNTrailerPlateActionPerformed
    }//GEN-LAST:event_txtNTrailerPlateActionPerformed

    private void cbxHourToItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxHourToItemStateChanged
        if (cbxHourTo.getSelectedIndex() < cbxHourFrom.getSelectedIndex()) {
            cbxHourTo.setSelectedIndex(cbxHourFrom.getSelectedIndex());
        }
    }//GEN-LAST:event_cbxHourToItemStateChanged

    private void cbxNMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNMaterialActionPerformed
        txtNMaterial.setText(cbxNMaterial.getSelectedItem().toString());
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxNMaterialActionPerformed

    private void cbxNMaterialItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxNMaterialItemStateChanged
        txtNMaterial.setText(cbxNMaterial.getSelectedItem().toString());
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxNMaterialItemStateChanged

    private void cbxNMaterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbxNMaterialKeyReleased
        txtNMaterial.setText(cbxNMaterial.getSelectedItem().toString());
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_cbxNMaterialKeyReleased

    private void btnManyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnManyActionPerformed
        String soDO = JOptionPane.showInputDialog(this.getRootPane(), resourceMapMsg.getString("msg.inputDO"));
        boolean isNumber = true;
        if (StringUtil.isNotEmptyString(soDO)) {
            soDO = soDO.trim();
            for (int i = 0; i < soDO.length(); i++) {
                if (!Character.isDigit(soDO.charAt(i))) {
                    isNumber = false;
                    break;
                }
            }

            if (isNumber && StringUtil.hasLengthInRange(soDO, 8, 10)) {
                if (txtNDONum.getText().equals("")) {
                    txtNDONum.setText(StringUtil.paddingZero(soDO, 10));
                } else {
                    String inputDO = (soDO.length() == 8) ? StringUtil.paddingZero(soDO, 8) : StringUtil.paddingZero(soDO, 10);
                    if (!StringUtil.contains(txtNDONum.getText(), inputDO)) {
                        txtNDONum.setText(txtNDONum.getText() + "-" + inputDO);
                    } else {
                        JOptionPane.showMessageDialog(this.getRootPane(), resourceMapMsg.getString("msg.duplicateDo"));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this.getRootPane(), resourceMapMsg.getString("msg.falseDo"));
                txtNDONum.setText("");
            }

        }
    }//GEN-LAST:event_btnManyActionPerformed

    private void btnCheckDOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheckDOActionPerformed
        setSaveNeeded(isValidated());
        isHasCheck = true;
    }//GEN-LAST:event_btnCheckDOActionPerformed

    private void dpDateFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dpDateFromActionPerformed
    }//GEN-LAST:event_dpDateFromActionPerformed

    private void txtNPlateNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNPlateNoFocusLost
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNPlateNoFocusLost

    private void txtNDriverNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNDriverNameFocusLost
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNDriverNameFocusLost

    private void txtNCMNDBLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNCMNDBLFocusLost
        setSaveNeeded(isValidated());
    }//GEN-LAST:event_txtNCMNDBLFocusLost

    private void btnSoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSoActionPerformed
        String soSO = JOptionPane.showInputDialog(this.getRootPane(), resourceMapMsg.getString("msg.inputSO"));
        boolean isNumber = false;
        if (soSO == null) {
            isNumber = false;
        } else if (soSO != null || !soSO.equals("")) {
            soSO = soSO.trim();
            for (int i = 0; i < soSO.length(); i++) {
                if (!Character.isDigit(soSO.charAt(i))) {
                    isNumber = false;
                } else {
                    isNumber = true;
                }
            }
            if (isNumber && soSO.length() >= 8 && soSO.length() <= 10) {
                if (txtNDONum.getText().equals("")) {
                    txtNDONum.setText(StringUtil.paddingZero(soSO, 10));

                } else {
                    String inputDO = (soSO.length() == 8) ? StringUtil.paddingZero(soSO, 8) : StringUtil.paddingZero(soSO, 10);
                    // String inputDO2 = StringUtil.paddingZero(soDO, 8);
                    if (txtNDONum.getText().indexOf(inputDO) == -1) {
                        txtNDONum.setText(txtNDONum.getText() + "-" + inputDO);
                    } else {
                        JOptionPane.showMessageDialog(this.getRootPane(), resourceMapMsg.getString("msg.duplicateDo"));
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this.getRootPane(), resourceMapMsg.getString("msg.falseDo"));
                txtNDONum.setText("");
            }

        }
    }//GEN-LAST:event_btnSoActionPerformed

private void rbtInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtInputActionPerformed
    loadModeTypeModel(MODE.INPUT);
}//GEN-LAST:event_rbtInputActionPerformed

private void rbtOutputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtOutputActionPerformed
    loadModeTypeModel(MODE.OUTPUT);
}//GEN-LAST:event_rbtOutputActionPerformed

private void cbxModeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxModeTypeActionPerformed
    this.modeDetail = ((WeighingMode) cbxModeType.getSelectedItem()).getModeDetail();

    // TODO: new ui set enable input
    prepareEditableForm(modeDetail);
}//GEN-LAST:event_cbxModeTypeActionPerformed

private void txtWeightTickerRefNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWeightTickerRefNKeyReleased
    validateForm();
}//GEN-LAST:event_txtWeightTickerRefNKeyReleased

private void txtRegisterIdNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtRegisterIdNKeyReleased
    validateForm();
}//GEN-LAST:event_txtRegisterIdNKeyReleased

private void txtDriverNameNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDriverNameNKeyReleased
    validateForm();
}//GEN-LAST:event_txtDriverNameNKeyReleased

private void txtCMNDNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCMNDNKeyReleased
    validateForm();
}//GEN-LAST:event_txtCMNDNKeyReleased

private void txtPlateNoNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlateNoNKeyReleased
    validateForm();
}//GEN-LAST:event_txtPlateNoNKeyReleased

private void txtSlingNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSlingNKeyReleased
    validateForm();
}//GEN-LAST:event_txtSlingNKeyReleased

private void txtPalletNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPalletNKeyReleased
    validateForm();
}//GEN-LAST:event_txtPalletNKeyReleased

private void txtDONumNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDONumNKeyReleased
    validateForm();
}//GEN-LAST:event_txtDONumNKeyReleased

private void cbxSlocNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSlocNActionPerformed
    loadBatchStockModel();
}//GEN-LAST:event_cbxSlocNActionPerformed

private void cbxBatchStockNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxBatchStockNActionPerformed
    if (cbxBatchStockN.getSelectedIndex() == -1) {
        return;
    }
    
    validateForm();

    BatchStock batchStock = (BatchStock) cbxBatchStockN.getSelectedItem();
    if (newWeightTicket != null) {
        newWeightTicket.setCharg(batchStock.getCharg());
    }
}//GEN-LAST:event_cbxBatchStockNActionPerformed

    private DefaultComboBoxModel getMatsModel() {
        return weightTicketRegistarationController.getMatsModel();
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task searchWeightTickets() {

        return new SearchWeightTicketTask(WeighBridgeApp.getApplication());
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task reprintRecord() {
        if (tabResults.getSelectedRow() == -1) {
            return null;
        } else {
            return new ReprintTask(WeighBridgeApp.getApplication());
        }
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task printReport() {
        if (tabResults.getModel().getRowCount() == 0) {
            return null;

        } else {
            return new PrintReportTask(org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class));
        }
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task checkDO() {
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(txtPlateNoN.getText(), lblPlateNoN);
        if (!isPlateNoValid) {
            JOptionPane.showMessageDialog(rootPane,
                    resourceMapMsg.getString("msg.plzInputPlateNo"));
            return null;
        }

        return new CheckDOTask(WeighBridgeApp.getApplication());
    }

    @Action(enabledProperty = "creatable")
    public void newRecord() {
        setFormEditable(true);
        setRbtEnabled(true);
        setCreatable(false);
        setClearable(true);
        setSaveNeeded(isValidated());
        txtWeightTicketNo.setText("");
        txtNMaterial.setEditable(true);
        cbxNMaterial.setEditable(true);
        txtNWeight.setEditable(true);
        btnMany.setEnabled(true);
        btnSo.setEnabled(true);
        btnCheckDO.setEnabled(true);
        btnSave.setEnabled(false);

        // TODO new ui
        rbtInput.setEnabled(true);
        rbtOutput.setEnabled(true);
        cbxModeType.setEnabled(true);
        loadModeTypeModel(MODE.OUTPUT);
    }

    @Action(enabledProperty = "clearable")
    public void clearForm() {
        cleanData();
        setCreatable(true);
        setFormEditable(false);
        setRbtEnabled(false);
        setSaveNeeded(false);
        setClearable(false);
        btnMany.setEnabled(false);
        btnSo.setEnabled(false);
        btnCheckDO.setEnabled(false);

        // TODO new ui
        rbtInput.setEnabled(false);
        rbtOutput.setEnabled(false);
        cbxModeType.setEnabled(false);
        loadModeTypeModel(MODE.OUTPUT);
        disableAllInForm();
    }

    public void loadModeTypeModel(MODE mode) {
        if (mode == MODE.INPUT) {
            rbtInput.setSelected(true);
        } else {
            rbtOutput.setSelected(true);
        }

        cbxModeType.setModel(weightTicketRegistarationController.getModeTypeModel(mode));
        this.mode = mode;
        this.modeDetail = ((WeighingMode) cbxModeType.getSelectedItem()).getModeDetail();

        // TODO: new ui set enable input
        prepareEditableForm(modeDetail);
    }

    private void disableAllInForm() {
        txtTicketIdN.setEnabled(false);
        txtWeightTickerRefN.setEnabled(false);
        txtRegisterIdN.setEnabled(false);
        txtDriverNameN.setEnabled(false);
        txtCMNDN.setEnabled(false);
        txtPlateNoN.setEnabled(false);
        txtTonnageN.setEnabled(false);
        txtTrailerNoN.setEnabled(false);
        txtSlingN.setEnabled(false);
        txtPalletN.setEnabled(false);
        txtSoNiemXaN.setEnabled(false);
        txtProductionBatchN.setEnabled(false);
        txtNoteN.setEnabled(false);
        txtDONumN.setEnabled(false);
        btnDOCheckN.setEnabled(false);
        txtSONumN.setEnabled(false);
        btnSOCheckN.setEnabled(false);
        txtPONumN.setEnabled(false);
        btnPOCheckN.setEnabled(false);
        txtPOSTONumN.setEnabled(false);
        btnPOSTOCheckN.setEnabled(false);
        cbxMaterialTypeN.setEnabled(false);
        txtWeightN.setEnabled(false);
        cbxSlocN.setEnabled(false);
        cbxSloc2N.setEnabled(false);
        cbxBatchStockN.setEnabled(false);
        cbxBatchStock2N.setEnabled(false);
        cbxVendorLoadingN.setEnabled(false);
        cbxVendorTransportN.setEnabled(false);
        txtSuppliesIdN.setEnabled(false);
    }

    private void prepareEditableForm(MODE_DETAIL modeDetail) {
        switch (modeDetail) {
            case IN_PO_PURCHASE:
                prepareInPOPurchaseMode();
                break;
            case IN_WAREHOUSE_TRANSFER:
                prepareInWarehouseTransfer();
                break;
            case IN_OTHER:
                prepareInOutOther();
                break;
            case OUT_SELL_ROAD:
                prepareOutSellRoad();
                break;
            case OUT_PLANT_PLANT:
                prepareOutPlantPlant();
                break;
            case OUT_SLOC_SLOC:
                prepareOutSlocSloc();
                break;
            case OUT_PULL_STATION:
                prepareOutPullStation();
                break;
            case OUT_SELL_WATERWAY:
                prepareOutSellWateway();
                break;
            case OUT_OTHER:
                prepareInOutOther();
                break;
        }

        validateForm();
    }

    private void showComponent(JComponent component, JComponent label, boolean isVisible, boolean isEnabled) {
        label.setVisible(isVisible);
        component.setVisible(isVisible);
        component.setVisible(isVisible);
        component.setEnabled(isEnabled);
    }

    private void showComponent(JComponent component, JComponent label, JComponent unit, boolean isVisible, boolean isEnabled) {
        label.setVisible(isVisible);
        component.setVisible(isVisible);
        component.setVisible(isVisible);
        component.setEnabled(isEnabled);
        unit.setVisible(isVisible);
    }

    private void prepareInPOPurchaseMode() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void prepareInWarehouseTransfer() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, true, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, true);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, false, false);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void prepareInOutOther() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, false, false);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void prepareOutSellRoad() {
        showComponent(txtTicketIdN, lblTicketIdN, false, false);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, true);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, false, false);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);

        cbxSlocN.setModel(sapService.getSlocModel());
    }

    private void prepareOutPlantPlant() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void prepareOutSlocSloc() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, true, true);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, true, true);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, true, true);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, true, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, true, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, true, true);
    }

    private void prepareOutPullStation() {
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, true, true);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, true, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, true, true);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void prepareOutSellWateway() {
        showComponent(txtTicketIdN, lblTicketIdN, false, false);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, true, true);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, false, false);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, false);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, false);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(txtSuppliesIdN, lblSuppliesIdN, false, false);
    }

    private void validateForm() {
        boolean isValid = false;
        switch (modeDetail) {
            case IN_PO_PURCHASE:

                break;
            case IN_WAREHOUSE_TRANSFER:
                break;
            case IN_OTHER:
                break;
            case OUT_SELL_ROAD:
                isValid = isValidOutboundDelivery && validateOutSellRoad();
                break;
            case OUT_PLANT_PLANT:
                break;
            case OUT_SLOC_SLOC:
                break;
            case OUT_PULL_STATION:
                break;
            case OUT_SELL_WATERWAY:
                break;
            case OUT_OTHER:
                break;
        }

        btnSave.setEnabled(isValid);
    }

    private boolean validateOutSellRoad() {
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 1, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            newWeightTicket.setAbbr(weightTicketRegistarationController.loadTransportAgentAbbr(plateNo));
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 12);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isDOValid = wtRegisValidation.validateDO(txtDONumN.getText(), lblDONumN);
        btnDOCheckN.setEnabled(isDOValid);

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isBatchStockValid = wtRegisValidation.validateCbxSelected(cbxBatchStockN.getSelectedIndex(), lblBatchStockN);

        return isRegisterIdValid && isDriverNameValid && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid && isBatchStockValid;
    }

    private void loadBatchStockModel() {
        if (cbxSlocN.getSelectedIndex() == -1) {
            return;
        }

        SLoc sloc = (SLoc) cbxSlocN.getSelectedItem();
        if (newWeightTicket != null) {
            newWeightTicket.setLgort(sloc.getLgort());

            List<WeightTicketDetail> weightTicketDetails = newWeightTicket.getWeightTicketDetails();
            if (weightTicketDetails.size() > 0) {
                String[] arr_matnr = weightTicketDetails.stream().map(item -> item.getMatnrRef()).toArray(String[]::new);

                if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                    weightTicketRegistarationController.getSyncBatchStocks(sloc, arr_matnr);
                }
                List<BatchStock> batchStocks = weightTicketRegistarationController.getBatchStocks(sloc, arr_matnr);
                cbxBatchStockN.setModel(weightTicketRegistarationController.getBatchStockModel(batchStocks));
                cbxBatchStockN.setSelectedIndex(-1);
            }
        }

        validateForm();
    }

    @Action(enabledProperty = "saveNeeded")
    public Task saveRecord() {
        int answer = JOptionPane.YES_OPTION;
        if (StringUtil.isEmptyString(txtNDONum.getText())) {
            answer = JOptionPane.showConfirmDialog(
                    this.getRootPane(),
                    resourceMapMsg.getString("msg.questtionSaveDO"),
                    JOptionPane.OPTIONS_PROPERTY,
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
        }

        if (answer == JOptionPane.YES_OPTION) {
            btnSave.setEnabled(false);
            return new SaveWeightTicketTask(WeighBridgeApp.getApplication());
        } else {
            btnSave.setEnabled(true);
            return null;
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Task Classes">

    private class SearchWeightTicketTask extends org.jdesktop.application.Task<Object, Void> {

        SearchWeightTicketTask(org.jdesktop.application.Application app) {
            super(app);
        }

        private List<WeightTicket> getWeightTicketWithOtherType(String from, String to) throws Exception {
            List<WeightTicket> data = null;
            if (cbxStatus.getSelectedIndex() == StatusEnum.POSTED.VALUE) {
                data = weightTicketRegistarationController.findByDatePostedNull(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), txtPlateNo.getText().trim());
            } else if (cbxStatus.getSelectedIndex() == StatusEnum.ALL.VALUE) {
                data = weightTicketRegistarationController.findByDateAllNull(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), txtPlateNo.getText().trim());
            }

            return filterHours(data, cbxHourFrom.getSelectedItem().toString(), cbxHourTo.getSelectedItem().toString());
        }

        private List<WeightTicket> getWeightTicketWithAllType(String from, String to) throws Exception {
            List<WeightTicket> data = null;
            if (cbxStatus.getSelectedIndex() == StatusEnum.POSTED.VALUE) {
                data = weightTicketRegistarationController.findByDatePostedNullAll(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), txtPlateNo.getText().trim());
            } else if (cbxStatus.getSelectedIndex() == StatusEnum.ALL.VALUE) {
                data = weightTicketRegistarationController.findByDateAllNullAll(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), txtPlateNo.getText().trim());
            }

            return filterHours(data, cbxHourFrom.getSelectedItem().toString(), cbxHourTo.getSelectedItem().toString());
        }

        private List<WeightTicket> getWeightTicketWithSelectedType(String from, String to, String matnr) throws Exception {
            List<WeightTicket> data = null;
            if (cbxStatus.getSelectedIndex() == StatusEnum.POSTED.VALUE) {
                data = weightTicketRegistarationController.findByDatePosted(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), matnr, txtPlateNo.getText().trim());
            } else if (cbxStatus.getSelectedIndex() == StatusEnum.ALL.VALUE) {
                data = weightTicketRegistarationController.findByDateAll(from, to, txtCreator.getText().trim(), txtDriverName.getText().trim(), matnr, txtPlateNo.getText().trim());
            }

            return filterHours(data, cbxHourFrom.getSelectedItem().toString(), cbxHourTo.getSelectedItem().toString());
        }

        private void setWeightTicketData() {
            for (int i = 0; i < weightTicketList.size(); i++) {
                WeightTicket item = weightTicketList.get(i);
                WeightTicketDetail weightTicketDetail = item.getWeightTicketDetail();
                wtData[i][0] = item.getSeqDay();
                wtData[i][1] = item.getDriverName();
                wtData[i][2] = item.getDriverIdNo();
                wtData[i][3] = item.getPlateNo();
                wtData[i][4] = item.getTrailerId();
                wtData[i][5] = item.getRegType();
                wtData[i][6] = weightTicketDetail.getRegItemDescription();
                wtData[i][7] = weightTicketDetail.getRegItemQuantity();
                wtData[i][8] = weightTicketDetail.getDeliveryOrderNo();
                wtData[i][9] = item.getCreator();
                wtData[i][10] = item.getSeqMonth();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                wtData[i][11] = dateFormat.format(item.getCreatedDate());
                String time = item.getCreatedTime().replaceAll(":", "");
                String hh = time.substring(0, 2);
                String mm = time.substring(2, 4);
                String ss = time.substring(4, 6);
                wtData[i][12] = hh + ":" + mm + ":" + ss;
                if (item.isPosted()) {
                    wtData[i][13] = true;
                } else {
                    wtData[i][13] = false;
                }
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Object doInBackground() {
            try {
                setProgress(0, 0, 4);
                weightTicketList.clear();

                setProgress(1, 0, 4);
                setMessage(resourceMapMsg.getString("msg.getData"));

                Object[] select = cbxMaterialType.getSelectedObjects();
                com.gcs.wb.jpa.entity.Material material = (com.gcs.wb.jpa.entity.Material) select[0];
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String from = format.format(dpDateFrom.getDate());
                String to = format.format(dpDateTo.getDate());
                List<WeightTicket> result;

                if (material.getMatnr().equals(MaterialEnum.OTHER.VALUE)) {
                    result = getWeightTicketWithOtherType(from, to);
                } else if (material.getMatnr().equals(MaterialEnum.ALL.VALUE)) {
                    result = getWeightTicketWithAllType(from, to);
                } else {
                    result = getWeightTicketWithSelectedType(from, to, material.getMatnr());
                }

                setProgress(2, 0, 4);
                setMessage(resourceMapMsg.getString("msg.handleDate"));
                weightTicketList.addAll(result);
                wtData = new Object[weightTicketList.size()][wtCols.length];

                setWeightTicketData();

                setProgress(3, 0, 4);
                editable = new boolean[wtCols.length];
                for (int i = 0; i < editable.length; i++) {
                    editable[i] = false;
                }
                setProgress(4, 0, 4);
            } catch (Exception ex) {
                failed(ex);
            }
            return null;
        }

        @Override
        protected void succeeded(Object result) {
        }

        @Override
        protected void finished() {
            setMessage(resourceMapMsg.getString("msg.finished"));
            WeighBridgeApp.getApplication().bindJTableModel(tabResults, wtData, wtCols, wtTypes, editable);

            setCreatable(true);
            setFormEditable(false);
            setClearable(false);
            setSaveNeeded(false);
            clearForm();
        }

        @Override
        protected void failed(Throwable cause) {
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());
        }
    }

    private class ReprintTask extends org.jdesktop.application.Task<Object, Void> {

        ReprintTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            selectedWeightTicket = weightTicketList.get(tabResults.convertRowIndexToModel(tabResults.getSelectedRow()));
            if (selectedWeightTicket != null) {
                try {
                    if (!selectedWeightTicket.isDissolved()) {
                        setMessage(resourceMapMsg.getString("msg.rePrinting"));
                        txtWeightTicketNo.setText(selectedWeightTicket.getId() + "");
                        StringSelection stringSelection = new StringSelection(txtWeightTicketNo.getText());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        printWT(selectedWeightTicket, true);
                    } else {
                        setMessage(resourceMapMsg.getString("msg.ticketDestroy"));
                    }
                } catch (Exception ex) {
                    failed(ex);
                }
            }
            return null;
        }

        @Override
        protected void failed(Throwable cause) {
            JOptionPane.showMessageDialog(rootPane, cause);
        }

        @Override
        protected void succeeded(Object result) {
        }

        @Override
        protected void finished() {
            setMessage(resourceMapMsg.getString("msg.finished"));
        }
    }

    private class PrintReportTask extends org.jdesktop.application.Task<Object, Void> {

        PrintReportTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String from = format.format(dpDateFrom.getDate());
                String to = format.format(dpDateTo.getDate());
                Map<String, Object> params = weightTicketRegistarationController.getPrintReport(from, to);
                String reportName = weightTicketRegistarationController.getReportName();
                weightTicketRegistarationController.printReport(params, reportName);
            } catch (Exception ex) {
                failed(ex);
            }
            return null;
        }

        @Override
        protected void failed(Throwable cause) {
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());
        }
    }

    private class CheckDOTask extends org.jdesktop.application.Task<Object, Void> {

        private List<String> strMaterial = new ArrayList<>();
        private BigDecimal totalWeight = BigDecimal.ZERO;

        CheckDOTask(org.jdesktop.application.Application app) {
            super(app);
            newWeightTicket.setWeightTicketDetails(new ArrayList<>());
        }

        @Override
        protected Object doInBackground() throws Exception {
            String[] deliveryOrderNos = txtDONumN.getText().split("-");
            for (String deliveryOrderNo : deliveryOrderNos) {
                setStep(1, resourceMapMsg.getString("msg.checkDOInDB"));
                deliveryOrderNo = StringUtil.paddingZero(deliveryOrderNo.trim(), 10);
                OutboundDelivery outboundDelivery = weightTicketRegistarationController.findByDeliveryOrderNumber(deliveryOrderNo);

                // sync from SAP
                if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                    outboundDelivery = syncOutboundDelivery(deliveryOrderNo, outboundDelivery);
                }

                // check exist DO
                if (outboundDelivery == null) {
                    throw new Exception(resourceMapMsg.getString("msg.dONotExitst", deliveryOrderNo));
                }

                // check mapping Plate No
                String plateNo = txtPlateNoN.getText().trim();
                if (!outboundDelivery.getTraid().trim().startsWith(plateNo)) {
                    throw new Exception(resourceMapMsg.getString("msg.notDuplicateLicensePlate"));
                }

                // check DO in used
                if (isDOInUsed(deliveryOrderNo, outboundDelivery)) {
                    throw new Exception(resourceMapMsg.getString("msg.typeDO", deliveryOrderNo, getMode(outboundDelivery)));
                }

                // set DO data to Weight ticket
                updateWeightTicket(outboundDelivery);
                setStep(4, null);
            }

            return null;
        }

        private OutboundDelivery syncOutboundDelivery(String deliveryOrderNo, OutboundDelivery outboundDelivery) {
            try {
                setStep(2, resourceMapMsg.getString("checkDOInSap"));
                OutboundDelivery sapOutboundDelivery = sapService.getOutboundDelivery(deliveryOrderNo);

                setStep(3, resourceMapMsg.getString("msg.saveDataToDb"));
                return sapService.syncOutboundDelivery(sapOutboundDelivery, outboundDelivery, deliveryOrderNo);
            } catch (Exception ex) {
                return null;
            }
        }

        private void setStep(int step, String msg) {
            if (StringUtil.isNotEmptyString(msg)) {
                setMessage(msg);
            }
            setProgress(step, 1, 4);
        }

        private void updateWeightTicket(OutboundDelivery outboundDelivery) {
            WeightTicketDetail weightTicketDetail = new WeightTicketDetail();
            weightTicketDetail.setItem(outboundDelivery.getDeliveryItem());
            weightTicketDetail.setMatnrRef(outboundDelivery.getMatnr());
            weightTicketDetail.setRegItemDescription(outboundDelivery.getArktx());
            weightTicketDetail.setUnit(outboundDelivery.getVrkme());
            weightTicketDetail.setKunnr(outboundDelivery.getKunnr());
            weightTicketDetail.setDeliveryOrderNo(outboundDelivery.getDeliveryOrderNo());

            BigDecimal weight = BigDecimal.ZERO;
            List<OutboundDeliveryDetail> outboundDeliveryDetails = outboundDelivery.getOutboundDeliveryDetails();
            for (OutboundDeliveryDetail outboundDeliveryDetail : outboundDeliveryDetails) {
                strMaterial.add(outboundDeliveryDetail.getArktx());
                weight = weight.add(outboundDeliveryDetail.getLfimg());
            }

            totalWeight = totalWeight.add(weight);
            weightTicketDetail.setRegItemQuantity(weight);
            newWeightTicket.addWeightTicketDetail(weightTicketDetail);
        }

        private boolean isDOInUsed(String deliveryOrderNo, OutboundDelivery outboundDelivery) {
            String wplant = configuration.getWkPlant();
            String sDoType = Constants.WTRegView.DO_TYPES;

            WeightTicket weightTicket = weightTicketRegistarationController.findByDeliveryOrderNo(deliveryOrderNo);
            String Lfart = outboundDelivery.getLfart();

            if ((sDoType.contains(Lfart) && outboundDelivery.getWbstk() == 'X'
                    && outboundDelivery.getWerks().equalsIgnoreCase(wplant))
                    || (weightTicket != null && !weightTicket.isPosted())) {
                return true;
            } else {
                return false;
            }
        }

        private String getMode(OutboundDelivery outboundDelivery) {
            if (outboundDelivery.getLfart().equalsIgnoreCase("LF") || outboundDelivery.getLfart().equalsIgnoreCase("ZTLF")) {
                return Constants.WTRegView.OUTPUT_LOWCASE;
            } else {
                return Constants.WTRegView.INPUT_LOWCASE;
            }
        }

        @Override
        protected void succeeded(Object t) {
            isValidOutboundDelivery = true;
            cbxMaterialTypeN.setSelectedItem(String.join(" - ", strMaterial));
            txtWeightN.setText(totalWeight.toString());

            loadBatchStockModel();
        }

        @Override
        protected void failed(Throwable cause) {
            newWeightTicket.setWeightTicketDetails(new ArrayList<>());
            cbxMaterialTypeN.setSelectedItem("");
            txtWeightN.setText("0");

            isValidOutboundDelivery = false;
            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
            }
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());
        }
    }

    private class SaveWeightTicketTask extends org.jdesktop.application.Task<Object, Void> {

        SaveWeightTicketTask(org.jdesktop.application.Application app) {
            super(app);
            setSaveNeeded(false);
            setClearable(false);
        }

        @Override
        protected Object doInBackground() {
            final Date now = weightTicketRegistarationController.getServerDate();

            SimpleDateFormat formatter = new SimpleDateFormat();
            int seqBDay = weightTicketRegistarationController.getNewSeqBDay() + 1;
            int seqBMonth = weightTicketRegistarationController.getNewSeqBMonth() + 1;

            formatter.applyPattern("yyyy");
            int year = Integer.parseInt(formatter.format(now));
            formatter.applyPattern("HH:mm:ss");
            String createdTime = formatter.format(now);

            newWeightTicket.setMandt(configuration.getSapClient());
            newWeightTicket.setWplant(configuration.getWkPlant());
            newWeightTicket.setSeqDay(seqBDay);
            newWeightTicket.setSeqMonth(seqBMonth);
            newWeightTicket.setCreatedTime(createdTime);
            newWeightTicket.setCreatedDate(now);
            newWeightTicket.setCreator(WeighBridgeApp.getApplication().getLogin().getUid());
            newWeightTicket.setOfflineMode(WeighBridgeApp.getApplication().isOfflineMode());
            newWeightTicket.setWbId(configuration.getWbId());
            newWeightTicket.setPosted(false);

            newWeightTicket.setRegType(mode == MODE.INPUT ? 'I' : 'O');
            newWeightTicket.setMode(modeDetail.name());
            newWeightTicket.setRegisteredNumber(txtRegisterIdN.getText().trim());
            newWeightTicket.setDriverName(txtDriverNameN.getText().trim());
            newWeightTicket.setDriverIdNo(txtCMNDN.getText().trim());
            newWeightTicket.setPlateNo(txtPlateNoN.getText().trim());
            newWeightTicket.setTrailerId(txtTrailerNoN.getText().trim());
            newWeightTicket.setSling(Integer.parseInt(txtSlingN.getText()));
            newWeightTicket.setPallet(Integer.parseInt(txtPalletN.getText()));
            newWeightTicket.setSoNiemXa(txtSoNiemXaN.getText().trim());
            newWeightTicket.setBatch(txtProductionBatchN.getText().trim());
            newWeightTicket.setText(txtNoteN.getText().trim());

            List<WeightTicketDetail> weightTicketDetails = newWeightTicket.getWeightTicketDetails();
            if (weightTicketDetails.size() > 0) {
                weightTicketDetails.forEach(weightTicketDetail -> {
                    weightTicketDetail.setCreatedTime(createdTime);
                    weightTicketDetail.setDocYear(year);
                    weightTicketDetail.setCreatedDate(now);
                });
            }

            setMessage(resourceMapMsg.getString("msg.saveData"));
            try {
                if (!entityTransaction.isActive()) {
                    entityTransaction.begin();
                }
                entityManager.persist(newWeightTicket);
                entityTransaction.commit();
                entityManager.clear();
            } catch (Exception ex) {
                throw ex;
            }

            try {
                setMessage(resourceMapMsg.getString("msg.printing"));
                txtWeightTicketNo.setText(newWeightTicket.getId() + "");
                printWT(newWeightTicket, false);
                StringSelection stringSelection = new StringSelection(txtWeightTicketNo.getText());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            } catch (Exception ex) {
                btnSave.setEnabled(true);
            }
            SearchWeightTicketTask t = new SearchWeightTicketTask(this.getApplication());
            t.execute();

            return null;
        }

        @Override
        protected void failed(Throwable cause) {
            if (entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            setSaveNeeded(true);
        }

        @Override
        protected void finished() {
            setMessage(resourceMapMsg.getString("msg.finished"));
            cleanData();
            setCreatable(true);
            setFormEditable(false);
            setRbtEnabled(false);
            setClearable(false);
            setSaveNeeded(false);
        }
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Form's properties">
    private boolean creatable = false;

    public boolean isCreatable() {
        return creatable;
    }

    public void setCreatable(boolean b) {
        boolean old = isCreatable();
        this.creatable = b;
        firePropertyChange("creatable", old, isCreatable());
    }
    private boolean clearable = false;

    public boolean isClearable() {
        return clearable;
    }

    public void setClearable(boolean b) {
        boolean old = isClearable();
        this.clearable = b;
        firePropertyChange("clearable", old, isClearable());
    }
    private boolean saveNeeded = false;

    public boolean isSaveNeeded() {
        return saveNeeded;
    }

    public void setSaveNeeded(boolean b) {
        boolean old = isSaveNeeded();
        this.saveNeeded = b;
        firePropertyChange("saveNeeded", old, isSaveNeeded());
    }

    private boolean validateDriver() {
        boolean isValid = !(txtNDriverName.getText() == null || txtNDriverName.getText().trim().isEmpty());
        if (isValid) {
            lblNDriverName.setForeground(Color.black);
        } else {
            lblNDriverName.setForeground(Color.red);
        }

        return isValid;
    }

    private boolean validateDriverNoId() {
        boolean isValid = !(txtNCMNDBL.getText() == null || txtNCMNDBL.getText().trim().isEmpty() || txtNCMNDBL.getText().trim().length() > 9);
        if (isValid) {
            lblNCMNDBL.setForeground(Color.black);
        } else {
            lblNCMNDBL.setForeground(Color.red);
        }

        return isValid;
    }

    private boolean validatePlate() {
        boolean isValid = !(txtNPlateNo.getText().trim().isEmpty());
        if (isValid) {
            if (txtNDONum.getText().trim().isEmpty() || outboundDelivery == null) {
                isValid = validateLicensePlate(null, null);
            } else {
                isValid = validateLicensePlate(outboundDelivery.getLfart(), outboundDelivery.getTraid());
            }
        }

        if (isValid && rbtNInward.isSelected()) {
            if (outboundDelivery != null) {
                String soXe_tmp = txtNPlateNo.getText().trim();
                int indexRomooc = -1;
                if (outboundDelivery.getTraid() != null && !outboundDelivery.getTraid().equals("")) {
                    indexRomooc = outboundDelivery.getTraid().indexOf("|");
                    if (indexRomooc >= 0) {
                        soXe_tmp = soXe_tmp.concat("|").concat(txtNTrailerPlate.getText().trim());
                    }
                }
                if (!soXe_tmp.equals(outboundDelivery.getTraid().trim())) {
                    if (outboundDelivery.getTraid() != null
                            && indexRomooc >= 0
                            && indexRomooc != (outboundDelivery.getTraid().trim().length() - 1)
                            && txtNTrailerPlate.getText().trim().equals("")) {
                        JOptionPane.showMessageDialog(rootPane,
                                resourceMapMsg.getString("msg.plzInputRomoc"));
                    } else {
                        JOptionPane.showMessageDialog(rootPane,
                                resourceMapMsg.getString("msg.errorLicensePlate"));
                    }
                }

            }
        }

        if (isValid) {
            lblNPlateNo.setForeground(Color.black);
        } else {
            lblNPlateNo.setForeground(Color.red);
        }

        return isValid;
    }

    private boolean validateRegisteredCategory() {
        boolean isValid = rbtNInward.isSelected() || rbtNOutward.isSelected();
        if (isValid) {
            lblNRegCat.setForeground(Color.black);
        } else {
            lblNRegCat.setForeground(Color.red);
        }
        return isValid;
    }

    private boolean validateMaterial() {
        boolean isValid = !(txtNMaterial.getText() == null || txtNMaterial.getText().trim().isEmpty());
        if (isValid) {
            lblNMaterial.setForeground(Color.black);
        } else {
            lblNMaterial.setForeground(Color.red);
        }
        return isValid;
    }

    private boolean validateQuantity() {
        boolean isValid = !(txtNWeight.getText().isEmpty() || Double.parseDouble(txtNWeight.getText()) <= 0d);
        if (isValid) {
            lblNWeight.setForeground(Color.black);
        } else {
            lblNWeight.setForeground(Color.red);
        }

        return isValid;
    }

    public boolean isValidated() {
        boolean isValidDriverName = validateDriver();
        boolean isValidDriverNoId = validateDriverNoId();
        boolean isValidPlateNo = validatePlate();
        boolean isValidRegisteredCategory = validateRegisteredCategory();
        boolean isValidRegisteredMaterial = validateMaterial();
        boolean isValidRegisteredQuantity = validateQuantity();
        boolean result = isValidDriverName && isValidDriverNoId && isValidPlateNo && isValidRegisteredCategory && isValidRegisteredMaterial && isValidRegisteredQuantity;
        setFormValid(result);

        boolean isValid;
        if (StringUtil.isNotEmptyString(txtNDONum.getText())) {
            isValid = result && isValidOutboundDelivery && isHasCheck;
        } else {
            isValid = result && isValidOutboundDelivery;
        }
        return isValid;
    }

    private void printWT(WeightTicket wt, boolean reprint) throws Exception {
        try {
            weightTicketRegistarationController.printRegWT(wt, reprint);
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    private boolean validateLicensePlate(String lfart, String traid) {
        boolean result = false;
        if (lfart != null && traid != null && (lfart.equalsIgnoreCase("ZTLF") || lfart.equalsIgnoreCase("NL"))) {
            String txt = traid;
            String doLicPlate;
            int idx1 = txt.indexOf("|");
            int idx2 = txt.indexOf("\\");
            if (idx1 >= 0 || idx2 >= 0) {
                int splitPos = idx1 < 0 ? idx2 : idx1;
                int bIdx = splitPos - 8;
                if (bIdx < 0) {
                    bIdx = 0;
                }
                doLicPlate = txt.substring(bIdx, splitPos);
                splitPos++;
            } else {
                int countSpace = 0;
                int[] pos = new int[3];
                char[] chars = txt.toCharArray();
                for (int x = 0; x < chars.length; x++) {
                    char c = chars[x];
                    if (c == ' ') {
                        pos[countSpace] = x;
                        countSpace++;
                    }
                }
                if (countSpace == 3) {
                    int splitPos = pos[1];
                    int bIdx = splitPos - 8;
                    if (bIdx < 0) {
                        bIdx = 0;
                    }
                    doLicPlate = txt.substring(bIdx, splitPos);
                    splitPos++;
                } else {
                    doLicPlate = traid;
                }
            }
            if (doLicPlate.equalsIgnoreCase(newWeightTicket.getPlateNo())) {
                result = true;
            } else {
                result = false;
                JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.errorLicensePlate", newWeightTicket.getPlateNo(), doLicPlate));
            }
        } else if (newWeightTicket != null) {
            Vehicle vehicle = weightTicketRegistarationController.findByPlateNo(txtNPlateNo.getText().trim());
            if (vehicle == null) {
                result = false;
                JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.errorLicensePlate", txtNPlateNo.getText().trim()));
            } else {
                result = true;
                if (vehicle.isProhibit()) {
                    result = false;
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.invalidLicensePlate", txtNPlateNo.getText().trim()));
                }

                List<TransportAgentVehicle> transportAgentVehicles = weightTicketRegistarationController.findByVehicleId(vehicle.getId());
                if (transportAgentVehicles != null && transportAgentVehicles.size() == 1) {
                    abbr = transportAgentVehicles.get(0).getTransportAgent().getAbbr();
                }
            }
        }
        return result;
    }

    private void cleanData() {
        newWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        outboundDelivery = null;
        txtNDriverName.setText("");
        txtNCMNDBL.setText("");
        txtNPlateNo.setText("");
        txtNTrailerPlate.setText("");
        rbtRegCatGroup.clearSelection();
        rbtNInward.setEnabled(true);
        rbtNOutward.setEnabled(true);
        txtNMaterial.setText("");
        cbxNMaterial.setSelectedItem("");
        txtNWeight.setValue(null);
        txtNDONum.setText("");

        lblNDriverName.setForeground(Color.black);
        lblNCMNDBL.setForeground(Color.black);
        lblNPlateNo.setForeground(Color.black);
        lblNTrailerPlate.setForeground(Color.black);
        lblNRegCat.setForeground(Color.black);
        lblNMaterial.setForeground(Color.black);
        lblNWeight.setForeground(Color.black);
        lblNDONum.setForeground(Color.black);
        isValidOutboundDelivery = true;
    }

    private List<WeightTicket> filterHours(List<WeightTicket> data, String timeFrom, String timeTo) {
        List<WeightTicket> result = new ArrayList<>();
        int startTime = Integer.parseInt(timeFrom);
        double endTime = Integer.parseInt(timeTo) + 0.99;
        if (!data.isEmpty()) {
            for (int index = 0; index < data.size(); index++) {
                WeightTicket item = data.get(index);
                int createTime = Integer.parseInt(item.getCreatedTime().substring(0, 2));
                if (startTime <= createTime && createTime <= endTime) {
                    result.add(item);
                }
            }
        }

        return result;
    }
    // </editor-fold>
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCheckDO;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDOCheckN;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnMany;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPOCheckN;
    private javax.swing.JButton btnPOSTOCheckN;
    private javax.swing.JButton btnReprint;
    private javax.swing.JButton btnSOCheckN;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSo;
    private javax.swing.JComboBox cbxBatchStock2N;
    private javax.swing.JComboBox cbxBatchStockN;
    private javax.swing.JComboBox cbxHourFrom;
    private javax.swing.JComboBox cbxHourTo;
    private javax.swing.JComboBox cbxMaterialType;
    private javax.swing.JComboBox cbxMaterialTypeN;
    private javax.swing.JComboBox cbxModeType;
    private javax.swing.JComboBox cbxNMaterial;
    private javax.swing.JComboBox cbxSloc2N;
    private javax.swing.JComboBox cbxSlocN;
    private javax.swing.JComboBox cbxStatus;
    private javax.swing.JComboBox cbxVendorLoadingN;
    private javax.swing.JComboBox cbxVendorTransportN;
    private org.jdesktop.swingx.JXDatePicker dpDateFrom;
    private org.jdesktop.swingx.JXDatePicker dpDateTo;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblBatchStock2N;
    private javax.swing.JLabel lblBatchStockN;
    private javax.swing.JLabel lblCMNDN;
    private javax.swing.JLabel lblCreator;
    private javax.swing.JLabel lblDONumN;
    private javax.swing.JLabel lblDateFrom;
    private javax.swing.JLabel lblDateTo;
    private javax.swing.JLabel lblDriverName;
    private javax.swing.JLabel lblDriverNameN;
    private javax.swing.JLabel lblHourFrom;
    private javax.swing.JLabel lblHourTo;
    private javax.swing.JLabel lblMaterialType;
    private javax.swing.JLabel lblMaterialTypeN;
    private javax.swing.JLabel lblNCMNDBL;
    private javax.swing.JLabel lblNDONum;
    private javax.swing.JLabel lblNDriverName;
    private javax.swing.JLabel lblNMaterial;
    private javax.swing.JLabel lblNPlateNo;
    private javax.swing.JLabel lblNRegCat;
    private javax.swing.JLabel lblNTrailerPlate;
    private javax.swing.JLabel lblNWeight;
    private javax.swing.JLabel lblNWeightUnit;
    private javax.swing.JLabel lblNoteN;
    private javax.swing.JLabel lblPONumN;
    private javax.swing.JLabel lblPOSTONumN;
    private javax.swing.JLabel lblPalletN;
    private javax.swing.JLabel lblPlateNo;
    private javax.swing.JLabel lblPlateNoN;
    private javax.swing.JLabel lblProductionBatchN;
    private javax.swing.JLabel lblRegisterIdN;
    private javax.swing.JLabel lblSONumN;
    private javax.swing.JLabel lblSlingN;
    private javax.swing.JLabel lblSloc2N;
    private javax.swing.JLabel lblSlocN;
    private javax.swing.JLabel lblSoNiemXaN;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblSuppliesIdN;
    private javax.swing.JLabel lblTicketIdN;
    private javax.swing.JLabel lblTonnageN;
    private javax.swing.JLabel lblTonngageUnitN;
    private javax.swing.JLabel lblTrailerNoN;
    private javax.swing.JLabel lblVendorLoadingN;
    private javax.swing.JLabel lblVendorTransportN;
    private javax.swing.JLabel lblWeightN;
    private javax.swing.JLabel lblWeightTickerRefN;
    private javax.swing.JLabel lblWeightTicketNo;
    private javax.swing.JLabel lblWeightUnitN;
    private javax.swing.JPanel pnControl;
    private javax.swing.JPanel pnFilter;
    private javax.swing.JPanel pnNLeft;
    private javax.swing.JPanel pnNRight;
    private javax.swing.JPanel pnPrintControl;
    private javax.swing.JPanel pnROVContent;
    private javax.swing.JPanel pnROVLeft;
    private javax.swing.JPanel pnROVRight;
    private javax.swing.JPanel pnROVTop;
    private javax.swing.JPanel pnRegistrationOfVehicle;
    private javax.swing.JPanel pnWTicket;
    private javax.swing.JRadioButton rbtInput;
    private javax.swing.JRadioButton rbtNInward;
    private javax.swing.JRadioButton rbtNOutward;
    private javax.swing.JRadioButton rbtOutput;
    private javax.swing.ButtonGroup rbtRegCatGroup;
    private javax.swing.JScrollPane spnResult;
    private org.jdesktop.swingx.JXTable tabResults;
    private javax.swing.JTextField txtCMNDN;
    private javax.swing.JTextField txtCreator;
    private javax.swing.JTextField txtDONumN;
    private javax.swing.JTextField txtDriverName;
    private javax.swing.JTextField txtDriverNameN;
    private javax.swing.JTextField txtNCMNDBL;
    private javax.swing.JTextField txtNDONum;
    private javax.swing.JTextField txtNDriverName;
    private javax.swing.JTextField txtNMaterial;
    private javax.swing.JTextField txtNPlateNo;
    private javax.swing.JTextField txtNTrailerPlate;
    private javax.swing.JFormattedTextField txtNWeight;
    private javax.swing.JTextField txtNoteN;
    private javax.swing.JTextField txtPONumN;
    private javax.swing.JTextField txtPOSTONumN;
    private javax.swing.JFormattedTextField txtPalletN;
    private javax.swing.JTextField txtPlateNo;
    private javax.swing.JTextField txtPlateNoN;
    private javax.swing.JTextField txtProductionBatchN;
    private javax.swing.JTextField txtRegisterIdN;
    private javax.swing.JTextField txtSONumN;
    private javax.swing.JFormattedTextField txtSlingN;
    private javax.swing.JTextField txtSoNiemXaN;
    private javax.swing.JTextField txtSuppliesIdN;
    private javax.swing.JTextField txtTicketIdN;
    private javax.swing.JFormattedTextField txtTonnageN;
    private javax.swing.JTextField txtTrailerNoN;
    private javax.swing.JFormattedTextField txtWeightN;
    private javax.swing.JTextField txtWeightTickerRefN;
    private javax.swing.JTextField txtWeightTicketNo;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    /**
     * Get the value of formValid
     *
     * @return the value of formValid
     */
    public boolean isFormValid() {
        return formValid;
    }

    /**
     * Set the value of formValid
     *
     * @param formValid new value of formValid
     */
    public void setFormValid(boolean formValid) {
        boolean oldFormValid = this.formValid;
        this.formValid = formValid;
        firePropertyChange(Constants.WTRegView.PROP_FORMVALID, oldFormValid, formValid);
    }
    private boolean formEditable;

    /**
     * Get the value of formEditable
     *
     * @return the value of formEditable
     */
    public boolean isFormEditable() {
        return formEditable;
    }

    /**
     * Set the value of formEditable
     *
     * @param formEditable new value of formEditable
     */
    public void setFormEditable(boolean formEditable) {
        boolean oldFormEditable = this.formEditable;
        this.formEditable = formEditable;
        firePropertyChange(Constants.WTRegView.PROP_FORMEDITABLE, oldFormEditable, formEditable);
    }
    private boolean rbtEnabled;

    /**
     * Get the value of rbtEnabled
     *
     * @return the value of rbtEnabled
     */
    public boolean isRbtEnabled() {
        return rbtEnabled;
    }

    /**
     * Set the value of rbtEnabled
     *
     * @param rbtEnabled new value of rbtEnabled
     */
    public void setRbtEnabled(boolean rbtEnabled) {
        boolean oldRbtEnabled = this.rbtEnabled;
        this.rbtEnabled = rbtEnabled;
        firePropertyChange(Constants.WTRegView.PROP_RBTENABLED, oldRbtEnabled, rbtEnabled);
    }
}
