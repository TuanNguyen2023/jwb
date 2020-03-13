/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.views;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.goodsmvt.structure.DOCheckStructure;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.enums.StatusEnum;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.base.validator.DateFromToValidator;
import com.gcs.wb.controller.WeightTicketController;
import com.gcs.wb.controller.WeightTicketRegistarationController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.jpa.repositorys.MaterialGroupRepository;
import com.gcs.wb.jpa.repositorys.MaterialInternalRepository;
import com.gcs.wb.jpa.repositorys.OutboundDeliveryRepository;
import com.gcs.wb.jpa.repositorys.OutboundDetailRepository;
import com.gcs.wb.jpa.repositorys.PurchaseOrderRepository;
import com.gcs.wb.jpa.repositorys.WeightTicketDetailRepository;
import com.gcs.wb.model.WeighingMode;
import com.gcs.wb.views.validations.WeightTicketRegistrationValidation;
import com.sap.conn.jco.JCoException;
import org.apache.log4j.Logger;
import org.hibersap.HibersapException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import javax.swing.event.ListSelectionEvent;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Stream;
import org.apache.commons.lang.SerializationUtils;
import org.jdesktop.application.Application;

public class WTRegView extends javax.swing.JInternalFrame {

    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    SAPService sapService = new SAPService();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    WeightTicketRegistarationController weightTicketRegistarationController = new WeightTicketRegistarationController();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(WTRegView.class);
    private static final Logger logger = Logger.getLogger(WTRegView.class);
    private final List<WeightTicket> weightTicketList;
    private boolean isEditMode = false;
    private boolean isValidDO = false;
    private boolean isValidPO = false;
    private boolean isValidPOSTO = false;
    private boolean isValidSO = false;
    private boolean isValidWeight = false;
    private boolean isValidVendorLoad = false;
    private boolean isValidVendorTransport = false;
    private BigDecimal numCheckWeight = BigDecimal.ZERO;
    private String plateNoValidDO = "";

    private boolean formValid;
    private com.gcs.wb.jpa.entity.WeightTicket newWeightTicket;
    private com.gcs.wb.jpa.entity.WeightTicket selectedWeightTicket;
    private boolean[] editable = null;
    Object[][] wtData = null;
    Object[] wtCols = Constants.WTRegView.WEIGHTTICKET_COLUMS;
    Class[] wtTypes = Constants.WTRegView.WEIGHTTICKET_TYPES;
    // TODO update ui
    private MODE mode = MODE.OUTPUT;
    private MODE_DETAIL modeDetail;
    private final DateFromToValidator dateFromToValidator = new DateFromToValidator();
    public static final String SDATE = "date";
    private WeightTicketRegistrationValidation wtRegisValidation;
    private PurchaseOrderRepository purchaseOrderRepository = new PurchaseOrderRepository();
    MaterialInternalRepository materialInternalRepository = new MaterialInternalRepository();
    WeightTicketController weightTicketController = new WeightTicketController();
    MaterialGroupRepository materialGroupRepository = new MaterialGroupRepository();
    List<String> cbxSlocs = new ArrayList<>();
    private PurchaseOrder purchaseOrderPO = new PurchaseOrder();
    private PurchaseOrder purchaseOrderPOSTO = new PurchaseOrder();
    OutboundDetailRepository detailRepository = new OutboundDetailRepository();
    WeightTicketDetailRepository weightTicketDetailRepository = new WeightTicketDetailRepository();

    public WTRegView() {
        newWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        selectedWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        weightTicketList = new ArrayList<>();
        wtRegisValidation = new WeightTicketRegistrationValidation(rootPane, resourceMapMsg);
        initComponents();
        dpDateFrom.setFormats(Constants.Date.FORMAT);
        dpDateTo.setFormats(Constants.Date.FORMAT);
        pnShowFilter.setVisible(false);
        btnEdit.setEnabled(false);
        initComboboxModel();
        initComboboxRenderer();
        initTableEvent();

        SearchWeightTicketTask t = new SearchWeightTicketTask(WeighBridgeApp.getApplication());
        t.execute();

        cbxHourTo.setSelectedIndex(23);
        cbxModeSearch.setModel(new DefaultComboBoxModel<>(ModeEnum.values()));
        cbxStatus.setModel(new DefaultComboBoxModel<>(StatusEnum.values()));
    }

    private void initTableEvent() {
        tabResults.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            try {
                selectedWeightTicket = weightTicketList.get(tabResults.convertRowIndexToModel(tabResults.getSelectedRow()));
                if (selectedWeightTicket != null && selectedWeightTicket.getOfflineMode()) {
                    btnEdit.setEnabled(true);
                } else {
                    btnEdit.setEnabled(false);
                }
            } catch (Exception ex) {
                btnEdit.setEnabled(false);
            }
        });
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

        cbxStatus.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof StatusEnum) {
                    StatusEnum status = (StatusEnum) value;
                    setText(status.getValue());
                }
                return this;
            }
        });

        cbxModeSearch.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ModeEnum) {
                    ModeEnum mod = (ModeEnum) value;
                    setText(mod.getName());
                }
                return this;
            }
        });

        cbxMaterialTypeN.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof MaterialInternal) {
                    MaterialInternal materialInternal = (MaterialInternal) value;
                    String maktx = materialInternal.getMaktx();
                    if (maktx != null && !maktx.isEmpty()) {
                        setText(materialInternal.getMatnr() + " - " + materialInternal.getMaktx());
                    } else {
                        setText(materialInternal.getMatnr() + " - " + materialInternal.getMaktg());
                    }
                }

                if (value instanceof Material) {
                    Material material = (Material) value;
                    String maktx = material.getMaktx();
                    if (maktx != null && !maktx.isEmpty()) {
                        setText(material.getMatnr() + " - " + material.getMaktx());
                    } else {
                        setText(material.getMatnr() + " - " + material.getMaktg());
                    }
                }
                return this;
            }
        });

        DefaultListCellRenderer cellRendererForSloc = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof SLoc) {
                    SLoc sloc = (SLoc) value;
                    setText(sloc.getLgort().concat(" - ").concat(sloc.getLgobe()));
                    //setText(sloc.getLgobe());
                    //setToolTipText(sloc.getLgort());
                    cbxSlocs.add(sloc.getLgort().concat(" - ").concat(sloc.getLgobe()));
                }

                return this;
            }
        };
        cbxSlocN.setRenderer(cellRendererForSloc);
        cbxSloc2N.setRenderer(cellRendererForSloc);

        DefaultListCellRenderer cellRendererForBatchStock = new DefaultListCellRenderer() {

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
        };
        cbxBatchStockN.setRenderer(cellRendererForBatchStock);
        cbxBatchStock2N.setRenderer(cellRendererForBatchStock);

        DefaultListCellRenderer cellRendererVendor = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Vendor) {
                    Vendor vendor = (Vendor) value;
                    setText(vendor.getName1());
                }
                return this;
            }
        };
        cbxVendorLoadingN.setRenderer(cellRendererVendor);
        cbxVendorTransportN.setRenderer(cellRendererVendor);

        cbxCustomerN.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer customer = (Customer) value;
                    String name = customer.getName1();
                    if (customer.getName2() != null && !customer.getName2().isEmpty()) {
                        name += " - " + customer.getName2();
                    }
                    setText(name);
                    setToolTipText(customer.getKunnr());
                }
                return this;
            }
        });
    }

    private void initComboboxModel() {
        DefaultComboBoxModel vendorModel = weightTicketRegistarationController.getVendorModel();
        DefaultComboBoxModel vendor2Model = (DefaultComboBoxModel) SerializationUtils.clone(vendorModel);
        cbxVendorLoadingN.setModel(vendorModel);
        cbxVendorLoadingN.setSelectedIndex(-1);
        cbxVendorTransportN.setModel(vendor2Model);
        cbxVendorTransportN.setSelectedIndex(-1);
    }

    private void loadSLoc(List<String> lgorts, String lgortSelected) {
        List<SLoc> sLocs = weightTicketRegistarationController.getListSLoc(lgorts);
        DefaultComboBoxModel sLocModel = new DefaultComboBoxModel(sLocs.toArray());
        DefaultComboBoxModel sLoc2Model = (DefaultComboBoxModel) SerializationUtils.clone(sLocModel);
        cbxSlocN.setModel(sLocModel);
        if (lgortSelected != null && !lgortSelected.isEmpty() && !sLocs.isEmpty()) {
            SLoc sLoc = sLocs.stream()
                    .filter(t -> lgortSelected.equals(t.getLgort()))
                    .findFirst()
                    .orElse(null);

            cbxSlocN.setSelectedItem(sLoc);
        } else {
            cbxSlocN.setSelectedIndex(-1);
        }

        cbxSloc2N.setModel(sLoc2Model);
        cbxSloc2N.setSelectedIndex(-1);

        cbxBatchStockN.setModel(new DefaultComboBoxModel());
        cbxBatchStock2N.setModel(new DefaultComboBoxModel());

        validateForm();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        lblMode = new javax.swing.JLabel();
        cbxModeSearch = new javax.swing.JComboBox();
        btnHideFilter = new javax.swing.JButton();
        spnResult = new javax.swing.JScrollPane();
        tabResults = new org.jdesktop.swingx.JXTable();
        pnPrintControl = new javax.swing.JPanel();
        btnReprint = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        pnRegistrationOfVehicle = new javax.swing.JPanel();
        pnROVLeft = new javax.swing.JPanel();
        lblWeightTickerRefN = new javax.swing.JLabel();
        lblRegisterIdN = new javax.swing.JLabel();
        lblDriverNameN = new javax.swing.JLabel();
        lblCMNDN = new javax.swing.JLabel();
        lblPlateNoN = new javax.swing.JLabel();
        lblTonnageN = new javax.swing.JLabel();
        lblTonngageUnitN = new javax.swing.JLabel();
        lblTrailerNoN = new javax.swing.JLabel();
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
        rbtInput = new javax.swing.JRadioButton();
        rbtOutput = new javax.swing.JRadioButton();
        cbxModeType = new javax.swing.JComboBox();
        lblSlingN = new javax.swing.JLabel();
        pnControl = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
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
        lblWeightTicketNo = new javax.swing.JLabel();
        txtWeightTicketNo = new javax.swing.JTextField();
        lblCustomerN = new javax.swing.JLabel();
        cbxCustomerN = new javax.swing.JComboBox();
        pnShowFilter = new javax.swing.JPanel();
        btnShowFilter = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WTRegView.class);
        jTextField1.setText(resourceMap.getString("jTextField1.text")); // NOI18N
        jTextField1.setName("jTextField1"); // NOI18N

        setClosable(true);
        setResizable(true);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        pnFilter.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("pnFilter.border.title"))); // NOI18N
        pnFilter.setMinimumSize(new java.awt.Dimension(1034, 137));
        pnFilter.setName("pnFilter"); // NOI18N

        lblDateFrom.setText(resourceMap.getString("lblDateFrom.text")); // NOI18N
        lblDateFrom.setName("lblDateFrom"); // NOI18N

        dpDateFrom.setDate(Calendar.getInstance().getTime());
        dpDateFrom.setName("dpDateFrom"); // NOI18N
        dpDateFrom.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dpDateFromPropertyChange(evt);
            }
        });

        lblDateTo.setText(resourceMap.getString("lblDateTo.text")); // NOI18N
        lblDateTo.setName("lblDateTo"); // NOI18N

        dpDateTo.setDate(Calendar.getInstance().getTime());
        dpDateTo.setName("dpDateTo"); // NOI18N
        dpDateTo.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dpDateToPropertyChange(evt);
            }
        });

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
        txtCreator.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCreatorKeyReleased(evt);
            }
        });

        lblDriverName.setLabelFor(txtCreator);
        lblDriverName.setText(resourceMap.getString("lblDriverName.text")); // NOI18N
        lblDriverName.setName("lblDriverName"); // NOI18N

        txtDriverName.setName("txtDriverName"); // NOI18N
        txtDriverName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDriverNameKeyReleased(evt);
            }
        });

        lblPlateNo.setText(resourceMap.getString("lblPlateNo.text")); // NOI18N
        lblPlateNo.setName("lblPlateNo"); // NOI18N

        txtPlateNo.setName("txtPlateNo"); // NOI18N
        txtPlateNo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlateNoKeyReleased(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WTRegView.class, this);
        btnFind.setAction(actionMap.get("searchWeightTickets")); // NOI18N
        btnFind.setText(resourceMap.getString("btnFind.text")); // NOI18N
        btnFind.setName("btnFind"); // NOI18N

        lblStatus.setText(resourceMap.getString("lblStatus.text")); // NOI18N
        lblStatus.setName("lblStatus"); // NOI18N

        cbxStatus.setName("cbxStatus"); // NOI18N

        lblMode.setText(resourceMap.getString("lblMode.text")); // NOI18N
        lblMode.setName("lblMode"); // NOI18N

        cbxModeSearch.setName("cbxModeSearch"); // NOI18N
        cbxModeSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxModeSearchActionPerformed(evt);
            }
        });

        btnHideFilter.setText(resourceMap.getString("btnHideFilter.text")); // NOI18N
        btnHideFilter.setName("btnHideFilter"); // NOI18N
        btnHideFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHideFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnFilterLayout = new javax.swing.GroupLayout(pnFilter);
        pnFilter.setLayout(pnFilterLayout);
        pnFilterLayout.setHorizontalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnFilterLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDriverName)
                    .addComponent(lblDateFrom)
                    .addComponent(lblMaterialType))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFind)
                    .addGroup(pnFilterLayout.createSequentialGroup()
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxMaterialType, 0, 181, Short.MAX_VALUE)
                            .addComponent(txtDriverName, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addComponent(dpDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                        .addGap(31, 31, 31)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPlateNo)
                            .addComponent(lblDateTo)
                            .addComponent(lblStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbxStatus, 0, 181, Short.MAX_VALUE)
                            .addComponent(txtPlateNo, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addComponent(dpDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
                        .addGap(42, 42, 42)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblCreator)
                            .addComponent(lblMode)
                            .addComponent(lblHourFrom))
                        .addGap(10, 10, 10)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxModeSearch, 0, 181, Short.MAX_VALUE)
                            .addComponent(txtCreator, javax.swing.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                            .addGroup(pnFilterLayout.createSequentialGroup()
                                .addComponent(cbxHourFrom, 0, 70, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(lblHourTo)
                                .addGap(18, 18, 18)
                                .addComponent(cbxHourTo, 0, 70, Short.MAX_VALUE)))))
                .addGap(54, 54, 54)
                .addComponent(btnHideFilter)
                .addContainerGap())
        );
        pnFilterLayout.setVerticalGroup(
            pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblHourFrom)
                        .addComponent(cbxHourFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblHourTo)
                        .addComponent(cbxHourTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(lblCreator)
                            .addComponent(txtCreator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStatus)
                            .addComponent(cbxStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxMaterialType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaterialType)
                            .addComponent(lblMode)
                            .addComponent(cbxModeSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(dpDateTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblDateTo)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFind)
                    .addComponent(btnHideFilter))
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

        btnReprint.setAction(actionMap.get("reprintRecord")); // NOI18N
        btnReprint.setText(resourceMap.getString("btnReprint.text")); // NOI18N
        btnReprint.setName("btnReprint"); // NOI18N

        btnEdit.setAction(actionMap.get("editOfflineRecord")); // NOI18N
        btnEdit.setText(resourceMap.getString("btnEdit.text")); // NOI18N
        btnEdit.setName("btnEdit"); // NOI18N

        javax.swing.GroupLayout pnPrintControlLayout = new javax.swing.GroupLayout(pnPrintControl);
        pnPrintControl.setLayout(pnPrintControlLayout);
        pnPrintControlLayout.setHorizontalGroup(
            pnPrintControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPrintControlLayout.createSequentialGroup()
                .addComponent(btnReprint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEdit)
                .addGap(832, 832, 832))
        );
        pnPrintControlLayout.setVerticalGroup(
            pnPrintControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPrintControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(btnReprint)
                .addComponent(btnEdit))
        );

        pnRegistrationOfVehicle.setName("pnRegistrationOfVehicle"); // NOI18N

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
        txtPlateNoN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPlateNoNFocusLost(evt);
            }
        });
        txtPlateNoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPlateNoNKeyReleased(evt);
            }
        });

        txtTonnageN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtTonnageN.setText(resourceMap.getString("txtTonnageN.text")); // NOI18N
        txtTonnageN.setName("txtTonnageN"); // NOI18N

        txtTrailerNoN.setName("txtTrailerNoN"); // NOI18N
        txtTrailerNoN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTrailerNoNKeyReleased(evt);
            }
        });

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
        txtSoNiemXaN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSoNiemXaNKeyReleased(evt);
            }
        });

        txtProductionBatchN.setName("txtProductionBatchN"); // NOI18N
        txtProductionBatchN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtProductionBatchNKeyReleased(evt);
            }
        });

        txtNoteN.setName("txtNoteN"); // NOI18N
        txtNoteN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoteNKeyReleased(evt);
            }
        });

        lblTicketIdN.setText(resourceMap.getString("lblTicketIdN.text")); // NOI18N
        lblTicketIdN.setName("lblTicketIdN"); // NOI18N

        txtTicketIdN.setName("txtTicketIdN"); // NOI18N
        txtTicketIdN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtTicketIdNKeyReleased(evt);
            }
        });

        rbtRegCatGroup.add(rbtInput);
        rbtInput.setText(resourceMap.getString("rbtInput.text")); // NOI18N
        rbtInput.setName("rbtInput"); // NOI18N
        rbtInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtInputActionPerformed(evt);
            }
        });

        rbtRegCatGroup.add(rbtOutput);
        rbtOutput.setSelected(true);
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

        lblSlingN.setText(resourceMap.getString("lblSlingN.text")); // NOI18N
        lblSlingN.setName("lblSlingN"); // NOI18N

        javax.swing.GroupLayout pnROVLeftLayout = new javax.swing.GroupLayout(pnROVLeft);
        pnROVLeft.setLayout(pnROVLeftLayout);
        pnROVLeftLayout.setHorizontalGroup(
            pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnROVLeftLayout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addComponent(lblPlateNoN))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnROVLeftLayout.createSequentialGroup()
                            .addComponent(rbtInput)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(rbtOutput)))
                    .addComponent(lblSoNiemXaN)
                    .addComponent(lblSlingN)
                    .addComponent(lblProductionBatchN)
                    .addComponent(lblNoteN)
                    .addComponent(lblDriverNameN)
                    .addComponent(lblRegisterIdN)
                    .addComponent(lblWeightTickerRefN)
                    .addComponent(lblTicketIdN)
                    .addComponent(lblCMNDN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProductionBatchN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(txtNoteN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(pnROVLeftLayout.createSequentialGroup()
                        .addComponent(txtSlingN, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                        .addGap(32, 32, 32)
                        .addComponent(lblPalletN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtPalletN, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE))
                    .addComponent(cbxModeType, 0, 380, Short.MAX_VALUE)
                    .addComponent(txtTicketIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(txtCMNDN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(pnROVLeftLayout.createSequentialGroup()
                        .addComponent(txtPlateNoN, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTrailerNoN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTrailerNoN, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTonnageN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTonnageN, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE))
                    .addComponent(txtWeightTickerRefN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(txtDriverNameN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(txtRegisterIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTonngageUnitN)
                .addContainerGap())
        );
        pnROVLeftLayout.setVerticalGroup(
            pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxModeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rbtOutput)
                    .addComponent(rbtInput))
                .addGap(15, 15, 15)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTicketIdN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTicketIdN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWeightTickerRefN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWeightTickerRefN))
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
                    .addComponent(lblPlateNoN)
                    .addComponent(lblTrailerNoN)
                    .addComponent(txtTrailerNoN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTonnageN)
                    .addComponent(txtTonnageN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTonngageUnitN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblPalletN)
                        .addComponent(txtPalletN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtSlingN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSlingN)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSoNiemXaN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoteN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblProductionBatchN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoteN)
                    .addComponent(txtProductionBatchN, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pnControl.setName("pnControl"); // NOI18N

        btnNew.setAction(actionMap.get("newRecord")); // NOI18N
        btnNew.setText(resourceMap.getString("btnNew.text")); // NOI18N
        btnNew.setName("btnNew"); // NOI18N

        btnClear.setAction(actionMap.get("clearForm")); // NOI18N
        btnClear.setText(resourceMap.getString("btnClear.text")); // NOI18N
        btnClear.setName("btnClear"); // NOI18N

        btnSave.setAction(actionMap.get("saveRecord")); // NOI18N
        btnSave.setText(resourceMap.getString("btnSave.text")); // NOI18N
        btnSave.setName("btnSave"); // NOI18N

        javax.swing.GroupLayout pnControlLayout = new javax.swing.GroupLayout(pnControl);
        pnControl.setLayout(pnControlLayout);
        pnControlLayout.setHorizontalGroup(
            pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnControlLayout.createSequentialGroup()
                .addComponent(btnNew)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnClear)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSave)
                .addContainerGap(319, Short.MAX_VALUE))
        );
        pnControlLayout.setVerticalGroup(
            pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnControlLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(pnControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNew)
                    .addComponent(btnClear)
                    .addComponent(btnSave)))
        );

        javax.swing.GroupLayout pnRegistrationOfVehicleLayout = new javax.swing.GroupLayout(pnRegistrationOfVehicle);
        pnRegistrationOfVehicle.setLayout(pnRegistrationOfVehicleLayout);
        pnRegistrationOfVehicleLayout.setHorizontalGroup(
            pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                        .addComponent(pnROVLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                        .addComponent(pnControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(11, 11, 11))))
        );
        pnRegistrationOfVehicleLayout.setVerticalGroup(
            pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                .addComponent(pnROVLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
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

        cbxMaterialTypeN.setEditable(true);
        cbxMaterialTypeN.setName("cbxMaterialTypeN"); // NOI18N
        cbxMaterialTypeN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMaterialTypeNActionPerformed(evt);
            }
        });

        txtWeightN.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.000"))));
        txtWeightN.setText(resourceMap.getString("txtWeightN.text")); // NOI18N
        txtWeightN.setName("txtWeightN"); // NOI18N
        txtWeightN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWeightNKeyReleased(evt);
            }
        });

        cbxSlocN.setName("cbxSlocN"); // NOI18N
        cbxSlocN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSlocNActionPerformed(evt);
            }
        });

        cbxSloc2N.setName("cbxSloc2N"); // NOI18N
        cbxSloc2N.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSloc2NActionPerformed(evt);
            }
        });

        cbxBatchStockN.setName("cbxBatchStockN"); // NOI18N
        cbxBatchStockN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBatchStockNActionPerformed(evt);
            }
        });

        cbxBatchStock2N.setName("cbxBatchStock2N"); // NOI18N
        cbxBatchStock2N.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBatchStock2NActionPerformed(evt);
            }
        });

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
        txtSONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSONumNKeyReleased(evt);
            }
        });

        btnSOCheckN.setAction(actionMap.get("checkSO")); // NOI18N
        btnSOCheckN.setText(resourceMap.getString("btnSOCheckN.text")); // NOI18N
        btnSOCheckN.setName("btnSOCheckN"); // NOI18N

        txtPONumN.setName("txtPONumN"); // NOI18N
        txtPONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPONumNKeyReleased(evt);
            }
        });

        btnPOCheckN.setAction(actionMap.get("checkPO")); // NOI18N
        btnPOCheckN.setText(resourceMap.getString("btnPOCheckN.text")); // NOI18N
        btnPOCheckN.setName("btnPOCheckN"); // NOI18N

        txtPOSTONumN.setName("txtPOSTONumN"); // NOI18N
        txtPOSTONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPOSTONumNKeyReleased(evt);
            }
        });

        btnPOSTOCheckN.setAction(actionMap.get("checkPOSTO")); // NOI18N
        btnPOSTOCheckN.setText(resourceMap.getString("btnPOSTOCheckN.text")); // NOI18N
        btnPOSTOCheckN.setName("btnPOSTOCheckN"); // NOI18N

        cbxVendorLoadingN.setName("cbxVendorLoadingN"); // NOI18N
        cbxVendorLoadingN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVendorLoadingNActionPerformed(evt);
            }
        });

        cbxVendorTransportN.setName("cbxVendorTransportN"); // NOI18N
        cbxVendorTransportN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxVendorTransportNActionPerformed(evt);
            }
        });

        lblWeightTicketNo.setText(resourceMap.getString("lblWeightTicketNo.text")); // NOI18N
        lblWeightTicketNo.setName("lblWeightTicketNo"); // NOI18N

        txtWeightTicketNo.setEditable(false);
        txtWeightTicketNo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txtWeightTicketNo.setName("txtWeightTicketNo"); // NOI18N

        lblCustomerN.setText(resourceMap.getString("lblCustomerN.text")); // NOI18N
        lblCustomerN.setName("lblCustomerN"); // NOI18N

        cbxCustomerN.setName("cbxCustomerN"); // NOI18N
        cbxCustomerN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCustomerNActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnROVRightLayout = new javax.swing.GroupLayout(pnROVRight);
        pnROVRight.setLayout(pnROVRightLayout);
        pnROVRightLayout.setHorizontalGroup(
            pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblBatchStock2N)
                    .addComponent(lblVendorLoadingN)
                    .addComponent(lblVendorTransportN)
                    .addComponent(lblWeightTicketNo)
                    .addComponent(lblMaterialTypeN)
                    .addComponent(lblPOSTONumN)
                    .addComponent(lblPONumN)
                    .addComponent(lblWeightN)
                    .addComponent(lblSONumN)
                    .addComponent(lblDONumN)
                    .addComponent(lblCustomerN)
                    .addComponent(lblSlocN)
                    .addComponent(lblSloc2N)
                    .addComponent(lblBatchStockN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxVendorTransportN, 0, 374, Short.MAX_VALUE)
                    .addComponent(cbxCustomerN, 0, 374, Short.MAX_VALUE)
                    .addComponent(txtPONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(txtPOSTONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(cbxMaterialTypeN, 0, 374, Short.MAX_VALUE)
                    .addComponent(txtWeightN, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(txtWeightTicketNo, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(txtSONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(txtDONumN, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                    .addComponent(cbxSlocN, 0, 374, Short.MAX_VALUE)
                    .addComponent(cbxSloc2N, 0, 374, Short.MAX_VALUE)
                    .addComponent(cbxBatchStockN, 0, 374, Short.MAX_VALUE)
                    .addComponent(cbxBatchStock2N, 0, 374, Short.MAX_VALUE)
                    .addComponent(cbxVendorLoadingN, 0, 374, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnPOCheckN)
                    .addComponent(btnPOSTOCheckN)
                    .addComponent(lblWeightUnitN)
                    .addComponent(btnSOCheckN)
                    .addComponent(btnDOCheckN))
                .addGap(37, 37, 37))
        );
        pnROVRightLayout.setVerticalGroup(
            pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblWeightTicketNo)
                    .addComponent(txtWeightTicketNo, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSONumN)
                    .addComponent(btnSOCheckN)
                    .addComponent(txtSONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDONumN)
                    .addComponent(btnDOCheckN)
                    .addComponent(txtDONumN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGap(8, 8, 8)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCustomerN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCustomerN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSlocN)
                    .addComponent(cbxSlocN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSloc2N)
                    .addComponent(cbxSloc2N, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatchStockN)
                    .addComponent(cbxBatchStockN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatchStock2N)
                    .addComponent(cbxBatchStock2N, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVendorLoadingN)
                    .addComponent(cbxVendorLoadingN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblVendorTransportN)
                    .addComponent(cbxVendorTransportN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        pnShowFilter.setName("pnShowFilter"); // NOI18N

        btnShowFilter.setText(resourceMap.getString("btnShowFilter.text")); // NOI18N
        btnShowFilter.setName("btnShowFilter"); // NOI18N
        btnShowFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnShowFilterLayout = new javax.swing.GroupLayout(pnShowFilter);
        pnShowFilter.setLayout(pnShowFilterLayout);
        pnShowFilterLayout.setHorizontalGroup(
            pnShowFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnShowFilterLayout.createSequentialGroup()
                .addContainerGap(1065, Short.MAX_VALUE)
                .addComponent(btnShowFilter))
        );
        pnShowFilterLayout.setVerticalGroup(
            pnShowFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnShowFilterLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(btnShowFilter))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnFilter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pnRegistrationOfVehicle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnROVRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(spnResult, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE)
                    .addComponent(pnPrintControl, javax.swing.GroupLayout.DEFAULT_SIZE, 1160, Short.MAX_VALUE)
                    .addComponent(pnShowFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnShowFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addComponent(spnResult, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnPrintControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(pnRegistrationOfVehicle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnROVRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold defaultstate="collapsed" desc="Old Code">
// </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Event methods">
    private void cbxHourToItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxHourToItemStateChanged
        if (cbxHourTo.getSelectedIndex() < cbxHourFrom.getSelectedIndex()) {
            cbxHourTo.setSelectedIndex(cbxHourFrom.getSelectedIndex());
        }
    }//GEN-LAST:event_cbxHourToItemStateChanged

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
    loadBatchStockModel(cbxSlocN, cbxBatchStockN, true);
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

private void cbxSloc2NActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSloc2NActionPerformed
    loadBatchStockModel(cbxSloc2N, cbxBatchStock2N, false);
}//GEN-LAST:event_cbxSloc2NActionPerformed

private void cbxBatchStock2NActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxBatchStock2NActionPerformed
    if (cbxBatchStock2N.getSelectedIndex() == -1) {
        return;
    }

    validateForm();

    BatchStock batchStock = (BatchStock) cbxBatchStock2N.getSelectedItem();
    if (newWeightTicket != null) {
        newWeightTicket.setRecvCharg(batchStock.getCharg());
    }
}//GEN-LAST:event_cbxBatchStock2NActionPerformed

private void txtPONumNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPONumNKeyReleased
    validateForm();
}//GEN-LAST:event_txtPONumNKeyReleased

private void txtPOSTONumNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPOSTONumNKeyReleased
    validateForm();
}//GEN-LAST:event_txtPOSTONumNKeyReleased

private void cbxCustomerNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCustomerNActionPerformed
    if (cbxCustomerN.getSelectedIndex() == -1) {
        return;
    }

    validateForm();
}//GEN-LAST:event_cbxCustomerNActionPerformed

private void txtTicketIdNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTicketIdNKeyReleased
    validateForm();
}//GEN-LAST:event_txtTicketIdNKeyReleased

private void txtWeightNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWeightNKeyReleased
    if (modeDetail == MODE_DETAIL.IN_PO_PURCHASE) {
        boolean isWeightValid = wtRegisValidation.validateLength(txtWeightN.getText(), lblWeightN, 1, 10);
        if (isWeightValid) {
            BigDecimal weight = new BigDecimal(txtWeightN.getText());

            if (numCheckWeight.subtract(weight).compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.quantityOver", numCheckWeight));

                lblWeightN.setForeground(Color.red);

                isValidWeight = false;
            } else {
                isValidWeight = true;
            }
        }
    }

    validateForm();
}//GEN-LAST:event_txtWeightNKeyReleased

private void txtSONumNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSONumNKeyReleased
    validateForm();
}//GEN-LAST:event_txtSONumNKeyReleased

private void cbxMaterialTypeNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMaterialTypeNActionPerformed
    if (cbxMaterialTypeN.getSelectedIndex() == -1) {
        return;
    }

    validateForm();

    if (newWeightTicket != null) {
        Object obj = cbxMaterialTypeN.getSelectedItem();
        if (obj instanceof MaterialInternal) {
            MaterialInternal materialInternal = (MaterialInternal) obj;
            newWeightTicket.setRecvMatnr(materialInternal.getMatnr());
        } else if (obj instanceof Material) {
            Material material = (Material) obj;
            newWeightTicket.setRecvMatnr(material.getMatnr());
        }
    }

    // load sloc
    boolean isInternal = modeDetail == MODE_DETAIL.IN_OTHER || modeDetail == MODE_DETAIL.OUT_OTHER;
    List<String> lgorts = weightTicketRegistarationController.getListLgortByMatnr(newWeightTicket.getRecvMatnr(), isInternal);
    loadSLoc(lgorts, null);

    // load batch stock
    loadBatchStockModel(cbxSlocN, cbxBatchStockN, true);
    loadBatchStockModel(cbxSloc2N, cbxBatchStock2N, false);
}//GEN-LAST:event_cbxMaterialTypeNActionPerformed

private void cbxVendorLoadingNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVendorLoadingNActionPerformed
    if (cbxVendorLoadingN.getSelectedItem() != null && !cbxVendorLoadingN.getSelectedItem().toString().equals("")) {
        Vendor vendor = (Vendor) cbxVendorLoadingN.getSelectedItem();
        //check validate vendor
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoNumber(newWeightTicket.getWeightTicketDetail().getEbeln());
        if (modeDetail != MODE_DETAIL.OUT_SLOC_SLOC) {
            if (newWeightTicket != null && newWeightTicket.getWeightTicketDetail().getMatnrRef() != null) {
                String vendorBocxep = "ZLCQ";
                String msgVendorCheck = "";
                if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                    msgVendorCheck = sapService.validateVendor(vendor.getLifnr(),
                            newWeightTicket.getWeightTicketDetail().getMatnrRef(),
                            vendorBocxep, purchaseOrder.getPurchaseOrderDetail().getPlant());
                }
                if (!msgVendorCheck.trim().isEmpty()) {
                    //display errror
                    JOptionPane.showMessageDialog(rootPane, msgVendorCheck);
                    lblVendorLoadingN.setForeground(Color.red);
                    isValidVendorLoad = false;
                } else {
                    isValidVendorLoad = true;
                    lblVendorLoadingN.setForeground(Color.black);
                    newWeightTicket.getWeightTicketDetail().setLoadVendor(vendor.getLifnr());
                }
            }
        }

        validateForm();
    } else {
        isValidVendorLoad = true;
    }
}//GEN-LAST:event_cbxVendorLoadingNActionPerformed

private void cbxVendorTransportNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVendorTransportNActionPerformed
    if (cbxVendorTransportN.getSelectedItem() != null && !cbxVendorTransportN.getSelectedItem().toString().equals("")) {
        Vendor vendor = (Vendor) cbxVendorTransportN.getSelectedItem();
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByPoNumber(newWeightTicket.getWeightTicketDetail().getEbeln());
        if (modeDetail != MODE_DETAIL.OUT_SLOC_SLOC) {
            //check validate vendor
            if (newWeightTicket != null && newWeightTicket.getWeightTicketDetail().getMatnrRef() != null) {
                String vendorVanchuyen = "ZIFQ";
                String msgVendorCheck = "";
                if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                    msgVendorCheck = sapService.validateVendor(vendor.getLifnr(),
                            newWeightTicket.getWeightTicketDetail().getMatnrRef(),
                            vendorVanchuyen, purchaseOrder.getPurchaseOrderDetail().getPlant());
                }
                if (msgVendorCheck != null && !msgVendorCheck.trim().isEmpty()) {
                    //display errror
                    JOptionPane.showMessageDialog(rootPane, msgVendorCheck);

                    lblVendorTransportN.setForeground(Color.red);
                    isValidVendorTransport = false;
                } else {
                    isValidVendorTransport = true;
                    lblVendorTransportN.setForeground(Color.black);
                    newWeightTicket.getWeightTicketDetail().setTransVendor(vendor.getLifnr());
                }
            }
        }

        validateForm();
    } else {
        isValidVendorTransport = true;
    }
}//GEN-LAST:event_cbxVendorTransportNActionPerformed

private void txtPlateNoNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlateNoNFocusLost
    String plateNo = txtPlateNoN.getText().trim();
    plateNo = plateNo.replace("-", "");
    plateNo = plateNo.replace(".", "");
    txtPlateNoN.setText(plateNo.toUpperCase());

    if (!plateNoValidDO.isEmpty() && !plateNo.contains(plateNoValidDO)) {
        lblPlateNoN.setForeground(Color.red);
        btnSave.setEnabled(false);

        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.plateNoNotResgiter", plateNo, plateNoValidDO));
    } else {
        validateForm();
    }
}//GEN-LAST:event_txtPlateNoNFocusLost

private void dpDateToPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dpDateToPropertyChange
    if (SDATE.equals(evt.getPropertyName())) {
        validateFilterForm();
    }
}//GEN-LAST:event_dpDateToPropertyChange

private void dpDateFromPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dpDateFromPropertyChange
    if (SDATE.equals(evt.getPropertyName())) {
        validateFilterForm();
    }
}//GEN-LAST:event_dpDateFromPropertyChange

private void txtDriverNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDriverNameKeyReleased
    validateFilterForm();
}//GEN-LAST:event_txtDriverNameKeyReleased

private void txtPlateNoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlateNoKeyReleased
    validateFilterForm();
}//GEN-LAST:event_txtPlateNoKeyReleased

private void txtCreatorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCreatorKeyReleased
    validateFilterForm();
}//GEN-LAST:event_txtCreatorKeyReleased

private void txtTrailerNoNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTrailerNoNKeyReleased
    validateForm();
}//GEN-LAST:event_txtTrailerNoNKeyReleased

private void txtSoNiemXaNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSoNiemXaNKeyReleased
    validateForm();
}//GEN-LAST:event_txtSoNiemXaNKeyReleased

private void txtProductionBatchNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProductionBatchNKeyReleased
    validateForm();
}//GEN-LAST:event_txtProductionBatchNKeyReleased

private void txtNoteNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoteNKeyReleased
    validateForm();
}//GEN-LAST:event_txtNoteNKeyReleased

private void cbxModeSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxModeSearchActionPerformed

}//GEN-LAST:event_cbxModeSearchActionPerformed

private void btnShowFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowFilterActionPerformed
    pnFilter.setVisible(true);
    pnShowFilter.setVisible(false);
}//GEN-LAST:event_btnShowFilterActionPerformed

private void btnHideFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHideFilterActionPerformed
    pnFilter.setVisible(false);
    pnShowFilter.setVisible(true);
}//GEN-LAST:event_btnHideFilterActionPerformed

    private void validateFilterForm() {
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverName.getText(), lblDriverName, 0, 70);
        boolean isPlateNoValid = wtRegisValidation.validateLength(txtPlateNo.getText(), lblPlateNo, 0, 20);
        boolean isCreatorValid = wtRegisValidation.validateLength(txtCreator.getText(), lblCreator, 0, 12);

        boolean isDateValid;
        try {
            dateFromToValidator.validate(dpDateFrom.getDate(), dpDateTo.getDate());

            lblDateFrom.setForeground(Color.black);
            lblDateTo.setForeground(Color.black);
            isDateValid = true;
        } catch (IllegalArgumentException ex) {
            lblDateFrom.setForeground(Color.red);
            lblDateTo.setForeground(Color.red);
            isDateValid = false;
        }

        btnFind.setEnabled(isDriverNameValid && isPlateNoValid && isCreatorValid && isDateValid);
    }

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
        String[] beforeDoNums = txtDONumN.getText().split("-");
        String[] doNums = Stream.of(beforeDoNums)
                .map(s -> StringUtil.paddingZero(s.trim(), 10)).distinct()
                .toArray(String[]::new);

        if (doNums.length != beforeDoNums.length) {
            JOptionPane.showMessageDialog(rootPane,
                    resourceMapMsg.getString("msg.duplicateDo"));
            return null;
        }

        txtDONumN.setText(String.join(" - ", doNums));

        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(txtPlateNoN.getText(), lblPlateNoN);
        if (!isPlateNoValid) {
            JOptionPane.showMessageDialog(rootPane,
                    resourceMapMsg.getString("msg.plzInputPlateNo"));
            return null;
        }

        return new CheckDOTask(WeighBridgeApp.getApplication());
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task checkSO() {
        String[] beforeSoNums = txtSONumN.getText().split("-");
        String[] soNums = Stream.of(beforeSoNums)
                .map(s -> StringUtil.paddingZero(s.trim(), 10)).distinct()
                .toArray(String[]::new);

        if (soNums.length != beforeSoNums.length) {
            JOptionPane.showMessageDialog(rootPane,
                    resourceMapMsg.getString("msg.duplicateSo"));
            return null;
        }

        txtSONumN.setText(String.join(" - ", soNums));

        return new CheckSOTask(WeighBridgeApp.getApplication());
    }

    List<DOCheckStructure> listDONumbers = new ArrayList<>();

    private class CheckSOTask extends org.jdesktop.application.Task<Object, Void> {

        CheckSOTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() throws Exception {
            String[] val = txtSONumN.getText().trim().split("-");
            String bsXe = txtPlateNoN.getText().trim();
            DOCheckStructure doNumber = new DOCheckStructure();
            String bsRomoc = txtTrailerNoN.getText().trim();

            if (val.length == listDONumbers.size()) {
                boolean hasChecked = listDONumbers.stream()
                        .allMatch(t -> Stream.of(val).anyMatch(s -> s.trim().equals(t.getVbelnSO())));

                if (hasChecked) {
                    return null;
                }
            }

            String doNum = "";
            if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                List<DOCheckStructure> doNumbers = sapService.getDONumber(val, bsXe, bsRomoc);

                if (doNumbers != null) {
                    listDONumbers.addAll(doNumbers);
                    for (int i = 0; i < doNumbers.size(); i++) {
                        doNumber = doNumbers.get(i);
                        if (!doNumber.getMessage().trim().isEmpty()) {
                            setMessage(doNumber.getMessage());
                            JOptionPane.showMessageDialog(rootPane, doNumber.getMessage());
                            String msg = "SO " + doNumber.getVbelnSO() + " sai, vui lòng nhập lại!";
                            setMessage(msg);
                            throw new Exception(msg);
                        } else {
                            if (doNum.isEmpty()) {
                                doNum = doNumber.getVbelnDO();
                            } else {
                                doNum += "-" + doNumber.getVbelnDO();
                            }
                        }
                    }
                }
            }

            txtDONumN.setText(doNum);
            return null;
        }

        @Override
        protected void failed(Throwable cause) {
            isValidSO = false;

            txtDONumN.setText(null);
            listDONumbers.clear();

            validateForm();

            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
            }
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());
        }

        @Override
        protected void succeeded(Object t) {
            isValidSO = true;
            validateForm();
        }
    }

    @Action(enabledProperty = "creatable")
    public void newRecord() {
        setFormEditable(true);
        setRbtEnabled(true);
        setCreatable(false);
        setClearable(true);
        txtWeightTicketNo.setText("");
        btnSave.setEnabled(false);

        // TODO new ui
        isEditMode = false;
        rbtInput.setEnabled(true);
        rbtOutput.setEnabled(true);
        cbxModeType.setEnabled(true);
        loadModeTypeModel(mode);
    }

    @Action(enabledProperty = "clearable")
    public void clearForm() {
        cleanData();
        setCreatable(true);
        setFormEditable(false);
        setRbtEnabled(false);
        setSaveNeeded(false);
        setClearable(false);

        // TODO new ui
        rbtInput.setEnabled(false);
        rbtOutput.setEnabled(false);
        cbxModeType.setEnabled(false);
        loadModeTypeModel(mode);
        disableAllInForm();
    }

    public void loadModeTypeModel(MODE mode) {
        if (mode == MODE.INPUT) {
            rbtInput.setSelected(true);
        } else {
            rbtOutput.setSelected(true);
        }

        if (this.modeDetail == null || this.mode != mode) {
            cbxModeType.setModel(weightTicketRegistarationController.getModeTypeModel(mode));
            this.mode = mode;
            this.modeDetail = ((WeighingMode) cbxModeType.getSelectedItem()).getModeDetail();
        }

        // TODO: new ui set enable input
        isValidDO = false;
        isValidPO = false;
        isValidPOSTO = false;
        isValidSO = false;
        disableAllInForm();
        prepareEditableForm(modeDetail);
    }

    private void disableAllInForm() {
        rbtInput.setForeground(Color.black);
        rbtOutput.setForeground(Color.black);
        txtTicketIdN.setEnabled(false);
        lblTicketIdN.setForeground(Color.black);
        txtWeightTickerRefN.setEnabled(false);
        lblWeightTickerRefN.setForeground(Color.black);
        txtRegisterIdN.setEnabled(false);
        lblRegisterIdN.setForeground(Color.black);
        txtDriverNameN.setEnabled(false);
        lblDriverNameN.setForeground(Color.black);
        txtCMNDN.setEnabled(false);
        lblCMNDN.setForeground(Color.black);
        txtPlateNoN.setEnabled(false);
        lblPlateNoN.setForeground(Color.black);
        txtTonnageN.setEnabled(false);
        lblTonnageN.setForeground(Color.black);
        txtTrailerNoN.setEnabled(false);
        lblTrailerNoN.setForeground(Color.black);
        txtSlingN.setEnabled(false);
        lblSlingN.setForeground(Color.black);
        txtPalletN.setEnabled(false);
        lblPalletN.setForeground(Color.black);
        txtSoNiemXaN.setEnabled(false);
        lblSoNiemXaN.setForeground(Color.black);
        txtProductionBatchN.setEnabled(false);
        lblProductionBatchN.setForeground(Color.black);
        txtNoteN.setEnabled(false);
        lblNoteN.setForeground(Color.black);
        txtDONumN.setEnabled(false);
        lblDONumN.setForeground(Color.black);
        btnDOCheckN.setEnabled(false);
        txtSONumN.setEnabled(false);
        lblSONumN.setForeground(Color.black);
        btnSOCheckN.setEnabled(false);
        txtPONumN.setEnabled(false);
        lblPONumN.setForeground(Color.black);
        btnPOCheckN.setEnabled(false);
        txtPOSTONumN.setEnabled(false);
        lblPOSTONumN.setForeground(Color.black);
        btnPOSTOCheckN.setEnabled(false);
        cbxMaterialTypeN.setEnabled(false);
        lblMaterialTypeN.setForeground(Color.black);
        txtWeightN.setEnabled(false);
        lblWeightN.setForeground(Color.black);
        cbxSlocN.setEnabled(false);
        lblSlocN.setForeground(Color.black);
        cbxSloc2N.setEnabled(false);
        lblSloc2N.setForeground(Color.black);
        cbxBatchStockN.setEnabled(false);
        lblBatchStockN.setForeground(Color.black);
        cbxBatchStock2N.setEnabled(false);
        lblBatchStock2N.setForeground(Color.black);
        cbxVendorLoadingN.setEnabled(false);
        lblVendorLoadingN.setForeground(Color.black);
        cbxVendorTransportN.setEnabled(false);
        lblVendorTransportN.setForeground(Color.black);
        cbxCustomerN.setEnabled(false);
        lblCustomerN.setForeground(Color.black);
    }

    private void prepareEditableForm(MODE_DETAIL modeDetail) {
        cleanData();
        initComboboxModel();

        lblPONumN.setText(resourceMapMsg.getString("lblPONumN.text"));
        lblPOSTONumN.setText(resourceMapMsg.getString("lblPOSTONumN.text"));
        lblSlocN.setText(resourceMapMsg.getString("lblSlocN.text"));
        lblBatchStockN.setText(resourceMapMsg.getString("lblBatchStockN.text"));

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

    private void showComponent(JComponent component, JLabel label, boolean isVisible, boolean isEditable) {
        label.setVisible(isVisible);
        component.setVisible(isVisible);

        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setEditable(isEditable);
            textField.setEnabled(isVisible);
        } else if (component instanceof JComboBox) {
            component.setEnabled(isEditable);
            Component editorComponent = ((JComboBox) component).getEditor().getEditorComponent();
            if (editorComponent instanceof JTextField) {
                ((JTextField) editorComponent).setEditable(false);
                ((JTextField) editorComponent).setEnabled(true);
            }
        }
    }

    private void showComponent(JComponent component, JLabel label, JComponent unit, boolean isVisible, boolean isEditable) {
        label.setVisible(isVisible);
        component.setVisible(isVisible);
        unit.setVisible(isVisible);

        if (component instanceof JTextField) {
            JTextField textField = (JTextField) component;
            textField.setEditable(isEditable);
            textField.setEnabled(isVisible);
        } else if (component instanceof JComboBox) {
            component.setEnabled(isEditable);
            Component editorComponent = ((JComboBox) component).getEditor().getEditorComponent();
            if (editorComponent instanceof JTextField) {
                ((JTextField) editorComponent).setEditable(false);
                ((JTextField) editorComponent).setEnabled(true);
            }
        }
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
        showComponent(cbxCustomerN, lblCustomerN, true, false);
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
        showComponent(cbxCustomerN, lblCustomerN, true, false);
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
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, true);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, false, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, false, false);
        showComponent(cbxCustomerN, lblCustomerN, true, true);

        cbxMaterialTypeN.setModel(weightTicketRegistarationController.getListMaterialInternal());
        cbxMaterialTypeN.setSelectedIndex(-1);

        cbxCustomerN.setModel(weightTicketRegistarationController.getCustomerModel());
        cbxCustomerN.setSelectedIndex(-1);
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
        showComponent(cbxCustomerN, lblCustomerN, true, false);
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

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, true);

        showComponent(cbxCustomerN, lblCustomerN, true, false);
    }

    private void prepareOutSlocSloc() {
        lblPONumN.setText(resourceMapMsg.getString("lblPoTrans"));
        lblPOSTONumN.setText(resourceMapMsg.getString("lblPostoLoad"));
        lblSlocN.setText(resourceMapMsg.getString("lblSlocOut"));
        lblBatchStockN.setText(resourceMapMsg.getString("lblBatchStockOut"));

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
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, true);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, true, true);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, true, true);

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, false);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, false);
        showComponent(cbxCustomerN, lblCustomerN, true, false);

        cbxMaterialTypeN.setModel(weightTicketRegistarationController.getListMaterial());
        cbxMaterialTypeN.setSelectedIndex(-1);
    }

    private void prepareOutPullStation() {
        lblPONumN.setText(resourceMapMsg.getString("lblPoOut"));
        lblPOSTONumN.setText(resourceMapMsg.getString("lblPostoIn"));

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

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, true);

        showComponent(cbxCustomerN, lblCustomerN, true, false);
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
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
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
        showComponent(cbxCustomerN, lblCustomerN, true, false);

        txtDONumN.setText("");
    }

    private void validateForm() {
        boolean isValid = false;
        switch (modeDetail) {
            case IN_PO_PURCHASE:
                isValid = validateInPoPurchase() && isValidPO && isValidWeight;
                break;
            case IN_WAREHOUSE_TRANSFER:
                isValid = validateInWarehouseTransfer() && isValidDO;
                break;
            case IN_OTHER:
                isValid = validateInOutOther();
                break;
            case OUT_SELL_ROAD:
                isValid = validateOutSellRoad() && isValidDO;
                break;
            case OUT_PLANT_PLANT:
                isValid = validateOutPlantPlant() && isValidPO && isValidVendorLoad && isValidVendorTransport;
                break;
            case OUT_SLOC_SLOC:
                isValid = validateOutSlocSloc() && isValidPO && (isValidPOSTO || txtPOSTONumN.getText().trim().isEmpty()) && isValidVendorTransport;
                break;
            case OUT_PULL_STATION:
                isValid = validateOutPullStation() && isValidPO && isValidVendorLoad && isValidVendorTransport;
                break;
            case OUT_SELL_WATERWAY:
                isValid = validateOutSellWateway() && isValidDO;
                break;
            case OUT_OTHER:
                isValid = validateInOutOther();
                break;
        }

        btnSave.setEnabled(isValid);
    }

    private boolean validateInPoPurchase() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

            Vendor transportVendor = (Vendor) cbxVendorTransportN.getSelectedItem();
            if (transportVendor != null) {
                if (!weightTicketRegistarationController.checkPlateNoInVendor(transportVendor.getLifnr(), plateNo)) {
                    isPlateNoValid = false;
                    lblPlateNoN.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rootPane, "msg.validateBSxe");
                }
            }
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        btnPOCheckN.setEnabled(isPOValid);
        if (!isValidPO) {
            lblPONumN.setForeground(Color.red);
        }

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid;
    }

    private boolean validateInWarehouseTransfer() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isDOValid = wtRegisValidation.validateDO(txtDONumN.getText(), lblDONumN);
        btnDOCheckN.setEnabled(isDOValid);
        if (!isValidDO) {
            lblDONumN.setForeground(Color.red);
        }

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        if (isEditMode) {
            boolean isSOValid = wtRegisValidation.validateDO(txtSONumN.getText(), lblSONumN);
            btnSOCheckN.setEnabled(isSOValid);
            if (!isValidSO) {
                lblSONumN.setForeground(Color.red);
            } else {
                btnDOCheckN.setEnabled(true);
            }
        }

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid;
    }

    private boolean validateInOutOther() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isWeightValid = wtRegisValidation.validateLength(txtWeightN.getText(), lblWeightN, 1, 10);

        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isMaterialTypeValid && isSlocValid && isWeightValid;
    }

    private boolean validateOutSellRoad() {
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isDOValid = wtRegisValidation.validateDO(txtDONumN.getText(), lblDONumN);
        btnDOCheckN.setEnabled(isDOValid);
        if (!isValidDO) {
            lblDONumN.setForeground(Color.red);
        }

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        if (isEditMode) {
            boolean isSOValid = wtRegisValidation.validateDO(txtSONumN.getText(), lblSONumN);
            btnSOCheckN.setEnabled(isSOValid);
            if (!isValidSO) {
                lblSONumN.setForeground(Color.red);
            } else {
                btnDOCheckN.setEnabled(true);
            }
        }

        return isRegisterIdValid && isDriverNameValid && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid;
    }

    private boolean validateOutPlantPlant() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNoWithDB(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

            Vendor transportVendor = (Vendor) cbxVendorTransportN.getSelectedItem();
            if (transportVendor != null) {
                if (!weightTicketRegistarationController.checkPlateNoInVendor(transportVendor.getLifnr(), plateNo)) {
                    isPlateNoValid = false;
                    lblPlateNoN.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.validateBSxe"));
                }
            }
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        btnPOCheckN.setEnabled(isPOValid);
        if (!isValidPO) {
            lblPONumN.setForeground(Color.red);
        }

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isVendorTransValid = wtRegisValidation.validateCbxSelected(cbxVendorTransportN.getSelectedIndex(), lblVendorTransportN);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid && isVendorTransValid;
    }

    private boolean validateOutSlocSloc() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

            Vendor transportVendor = (Vendor) cbxVendorTransportN.getSelectedItem();
            if (transportVendor != null) {
                if (!weightTicketRegistarationController.checkPlateNoInVendor(transportVendor.getLifnr(), plateNo)) {
                    isPlateNoValid = false;
                    lblPlateNoN.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.validateBSxe"));
                }
            }
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        btnPOCheckN.setEnabled(isPOValid);
        if (!isValidPO) {
            lblPONumN.setForeground(Color.red);
        }

        boolean isPOSTOValid = wtRegisValidation.validatePO(txtPOSTONumN.getText(), lblPOSTONumN);
        if (txtPOSTONumN.getText().trim().isEmpty()) {
            lblPOSTONumN.setForeground(Color.black);
        }

        btnPOSTOCheckN.setEnabled(isPOSTOValid);
        if (isPOSTOValid && !isValidPOSTO) {
            btnPOSTOCheckN.setForeground(Color.red);
        } else {
            btnPOSTOCheckN.setForeground(Color.black);
        }

        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isSloc2Valid = wtRegisValidation.validateCbxSelected(cbxSloc2N.getSelectedIndex(), lblSloc2N);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid && isSloc2Valid && isMaterialTypeValid;
    }

    private boolean validateOutPullStation() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNoWithDB(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

            Vendor transportVendor = (Vendor) cbxVendorTransportN.getSelectedItem();
            if (transportVendor != null) {
                if (!weightTicketRegistarationController.checkPlateNoInVendor(transportVendor.getLifnr(), plateNo)) {
                    isPlateNoValid = false;
                    lblPlateNoN.setForeground(Color.red);
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.validateBSxe"));
                }
            }
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        btnPOCheckN.setEnabled(isPOValid);
        if (!isValidPO) {
            lblPONumN.setForeground(Color.red);
        }

        boolean isPOSTOValid = wtRegisValidation.validatePO(txtPOSTONumN.getText(), lblPOSTONumN);
        btnPOSTOCheckN.setEnabled(isPOSTOValid);

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isVendorTransValid = wtRegisValidation.validateCbxSelected(cbxVendorTransportN.getSelectedIndex(), lblVendorTransportN);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid && isVendorTransValid;
    }

    private boolean validateOutSellWateway() {
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
        if (isPlateNoValid) {
            txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isSOValid = wtRegisValidation.validateDO(txtSONumN.getText(), lblSONumN);
        btnSOCheckN.setEnabled(isSOValid);
        if (!isValidSO) {
            lblSONumN.setForeground(Color.red);
        } else {
            btnDOCheckN.setEnabled(true);
        }

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        return isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid;
    }

    private void loadBatchStockModel(JComboBox slocComponent, JComboBox batchStockComponent, boolean isSloc) {
        if (slocComponent.getSelectedIndex() == -1) {
            return;
        }

        SLoc sloc = (SLoc) slocComponent.getSelectedItem();
        if (newWeightTicket != null) {
            if (isSloc) {
                newWeightTicket.setLgort(sloc.getLgort());
            } else {
                newWeightTicket.setRecvLgort(sloc.getLgort());
            }

            List<WeightTicketDetail> weightTicketDetails = newWeightTicket.getWeightTicketDetails();
            String[] arr_matnr;
            if (weightTicketDetails.size() > 0
                    && !(modeDetail == MODE_DETAIL.IN_OTHER || modeDetail == MODE_DETAIL.OUT_OTHER || modeDetail == MODE_DETAIL.OUT_SLOC_SLOC)) {
                arr_matnr = weightTicketDetails.stream().map(item -> item.getMatnrRef()).toArray(String[]::new);
            } else {
                arr_matnr = new String[]{newWeightTicket.getRecvMatnr()};
            }

            List<BatchStock> batchStocks = weightTicketRegistarationController.getBatchStocks(sloc, arr_matnr);
            batchStockComponent.setModel(weightTicketRegistarationController.getBatchStockModel(batchStocks));
            batchStockComponent.setSelectedIndex(batchStocks.size() > 0 ? 0 : -1);
        }

        validateForm();
    }

    @Action(enabledProperty = "saveNeeded")
    public Task saveRecord() {
        int answer = JOptionPane.showConfirmDialog(
                this.getRootPane(),
                resourceMapMsg.getString("msg.questtionSave"),
                JOptionPane.OPTIONS_PROPERTY,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

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

        private void setWeightTicketData() {
            for (int i = 0; i < weightTicketList.size(); i++) {
                WeightTicket item = weightTicketList.get(i);
                List<WeightTicketDetail> weightTicketDetails = item.getWeightTicketDetails();
                wtData[i][0] = item.getSeqDay();
                wtData[i][1] = item.getDriverName();
                wtData[i][2] = item.getDriverIdNo();
                wtData[i][3] = item.getPlateNo();
                wtData[i][4] = item.getTrailerId();
                wtData[i][5] = item.getRegType();
                String[] regItemDescriptions = weightTicketDetails.stream()
                        .map(t -> t.getRegItemDescription())
                        .filter(t -> t != null)
                        .toArray(String[]::new);
                wtData[i][6] = regItemDescriptions.length > 0 ? String.join(" - ", regItemDescriptions) : "";
                BigDecimal sumRegQty = BigDecimal.ZERO;
                for (WeightTicketDetail wt : weightTicketDetails) {
                    sumRegQty = sumRegQty.add(wt.getRegItemQuantity());
                }
                wtData[i][7] = sumRegQty;
                String[] doNums = weightTicketDetails.stream()
                        .map(t -> t.getDeliveryOrderNo())
                        .filter(t -> t != null)
                        .toArray(String[]::new);
                wtData[i][8] = doNums.length > 0 ? String.join(" - ", doNums) : "";
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
                StatusEnum status = (StatusEnum) cbxStatus.getSelectedItem();
                status = status != null ? status : StatusEnum.ALL;
                List<WeightTicket> result = weightTicketRegistarationController.findListWeightTicket(from, to,
                        txtCreator.getText().trim(), txtDriverName.getText().trim(), txtPlateNo.getText().trim(),
                        material.getMatnr(), status,
                        (ModeEnum) cbxModeSearch.getSelectedItem());

                result = filterHours(result, cbxHourFrom.getSelectedItem().toString(), cbxHourTo.getSelectedItem().toString());

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
        private List<String> matnrs = new ArrayList<>();
        private BigDecimal totalWeight = BigDecimal.ZERO;
        private Customer customer = null;
        private String strLgort = "";

        CheckDOTask(org.jdesktop.application.Application app) {
            super(app);
            newWeightTicket.setWeightTicketDetails(new ArrayList<>());
        }

        @Override
        protected Object doInBackground() throws Exception {
            String[] deliveryOrderNos = txtDONumN.getText().split("-");
            String[] salesOrderNos = txtSONumN.getText().split("-");
            String salesOrder = null;
            for (int index = 0; index < deliveryOrderNos.length; index++) {
                String deliveryOrderNo = deliveryOrderNos[index];
                if (salesOrderNos.length == deliveryOrderNos.length) {
                    salesOrder = salesOrderNos[index];
                }
                setStep(1, resourceMapMsg.getString("msg.checkDOInDB"));
                deliveryOrderNo = StringUtil.paddingZero(deliveryOrderNo.trim(), 10);
                OutboundDelivery outboundDelivery = weightTicketRegistarationController.findByDeliveryOrderNumber(deliveryOrderNo);

                // sync from SAP
                if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                    outboundDelivery = syncOutboundDelivery(deliveryOrderNo, outboundDelivery);

                    if (outboundDelivery != null) {
                        String kunnr = outboundDelivery.getKunnr();
                        if (kunnr != null && !kunnr.trim().isEmpty()) {
                            customer = syncCustomer(kunnr.trim());
                        }
                    }
                }

                // check exist DO
                if (outboundDelivery == null) {
                    throw new Exception(resourceMapMsg.getString("msg.dONotExitst", deliveryOrderNo));
                }

                // check register DO in WB
                WeightTicket weightTicket = weightTicketRegistarationController.findByDeliveryOrderNoScale(deliveryOrderNo);
                if (weightTicket != null) {
                    throw new Exception(resourceMapMsg.getString("msg.doExist", deliveryOrderNo, weightTicket.getId()));
                }

                // check status post SAP
                if (outboundDelivery.getVbelnNach() != null && !outboundDelivery.getVbelnNach().trim().isEmpty()) {
                    throw new Exception(resourceMapMsg.getString("msg.doPosted", deliveryOrderNo, outboundDelivery.getVbelnNach()));
                }

                //Check Delivery Plant with Configuration parameter.
                if ((!(outboundDelivery.getWerks()).equals(configuration.getWkPlant()))
                        && (!(outboundDelivery.getRecvPlant()).equals(configuration.getWkPlant()))) {
                    throw new Exception(resourceMapMsg.getString("msg.doIsDenied"));
                }

                // check mapping Plate No
                String plateNo = txtPlateNoN.getText().trim();
                String traid = outboundDelivery.getTraid().trim();
                traid = StringUtil.correctPlateNo(traid).toUpperCase();
                if ((traid.isEmpty()) || (!traid.isEmpty() && !traid.startsWith(plateNo))) {
                    throw new Exception(resourceMapMsg.getString("msg.plateNoNotMappingWithDO", plateNo));
                }

                // for check edit plateNo after check DO
                plateNoValidDO = traid.isEmpty() ? "" : plateNo;

                // check DO in used
//                if (isDOInUsed(deliveryOrderNo, outboundDelivery)) {
//                    throw new Exception(resourceMapMsg.getString("msg.typeDO", deliveryOrderNo, getMode(outboundDelivery)));
//                }
                // check customer
                if (index > 0) {
                    String deliveryOrderNoBefore = deliveryOrderNos[index - 1];
                    deliveryOrderNoBefore = StringUtil.paddingZero(deliveryOrderNoBefore.trim(), 10);
                    OutboundDelivery outboundDeliveryBefore = weightTicketRegistarationController.findByDeliveryOrderNumber(deliveryOrderNoBefore);
                    if (!outboundDelivery.getKunnr().equals(outboundDeliveryBefore.getKunnr())) {
                        throw new Exception(resourceMapMsg.getString("msg.customerNotTogether"));
                    }
                }

                // set DO data to Weight ticket
                updateWeightTicket(outboundDelivery, salesOrder);
                btnSave.setEnabled(true);
                setStep(4, null);
            }

            // check out together
            if (deliveryOrderNos.length > 1 && !checkMaterial(matnrs)) {
                throw new Exception(resourceMapMsg.getString("msg.materialNotTogether"));
            }

            return null;

        }

        private OutboundDelivery syncOutboundDelivery(String deliveryOrderNo, OutboundDelivery outboundDelivery) {
            try {
                setStep(2, resourceMapMsg.getString("checkDOInSap"));
                OutboundDelivery sapOutboundDelivery = sapService.getOutboundDelivery(deliveryOrderNo);

                setStep(3, resourceMapMsg.getString("msg.saveDataDOToDb"));
                return sapService.syncOutboundDelivery(sapOutboundDelivery, outboundDelivery, deliveryOrderNo);
            } catch (Exception ex) {
                return null;
            }
        }

        private Customer syncCustomer(String kunnr) {
            try {
                Customer dbCustomer = weightTicketRegistarationController.findByKunnr(kunnr);
                Customer sapCustomer = sapService.getCustomer(kunnr);
                return sapService.syncCustomer(sapCustomer, dbCustomer);
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

        private void updateWeightTicket(OutboundDelivery outboundDelivery, String salesOrder) {
            WeightTicketDetail weightTicketDetail = new WeightTicketDetail();
            weightTicketDetail.setItem(outboundDelivery.getDeliveryItem());
            weightTicketDetail.setMatnrRef(outboundDelivery.getMatnr());
            weightTicketDetail.setRegItemDescription(outboundDelivery.getArktx());
            weightTicketDetail.setUnit(outboundDelivery.getVrkme());
            weightTicketDetail.setKunnr(outboundDelivery.getKunnr());
            weightTicketDetail.setDeliveryOrderNo(outboundDelivery.getDeliveryOrderNo());
            weightTicketDetail.setRegItemQuantity(outboundDelivery.getLfimg());
            weightTicketDetail.setSoNumber(salesOrder);
            weightTicketDetail.setShipTo(outboundDelivery.getOutboundDeliveryDetail().getShipTo());
            newWeightTicket.addWeightTicketDetail(weightTicketDetail);
            newWeightTicket.setWeightTicketIdRef(outboundDelivery.getWtIdRef());

            List<OutboundDeliveryDetail> outboundDeliveryDetails = outboundDelivery.getOutboundDeliveryDetails();
            for (OutboundDeliveryDetail outboundDeliveryDetail : outboundDeliveryDetails) {
                if (!strMaterial.contains(outboundDeliveryDetail.getArktx())) {
                    strMaterial.add(outboundDeliveryDetail.getArktx());
                    matnrs.add(outboundDeliveryDetail.getMatnr());
                }
            }

            strLgort = outboundDelivery.getLgort();
            totalWeight = totalWeight.add(outboundDelivery.getLfimg());
        }

        private boolean checkMaterial(List<String> matnrs) {
            return materialGroupRepository.checkMaterialTogether(matnrs);
        }

        private boolean isDOInUsed(String deliveryOrderNo, OutboundDelivery outboundDelivery) {
            String wplant = configuration.getWkPlant();
            String sDoType = Constants.WTRegView.DO_TYPES;

            WeightTicket weightTicket = weightTicketRegistarationController.findByDeliveryOrderNo(deliveryOrderNo);
            String Lfart = outboundDelivery.getLfart();

            //if ((sDoType.contains(Lfart) && outboundDelivery.getWbstk() == 'C'
//               && outboundDelivery.getWerks().equalsIgnoreCase(wplant))
//                  || (weightTicket != null && weightTicket.isPosted())) {
            if ((outboundDelivery.getWbstk() == 'C'
                    && outboundDelivery.getWerks().equalsIgnoreCase(wplant))
                    || (weightTicket != null && weightTicket.isPosted())) {
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
            isValidDO = true;
            cbxMaterialTypeN.setSelectedItem(String.join(" - ", strMaterial));
            txtWeightN.setText(totalWeight.toString());

            if (customer != null) {
                cbxCustomerN.setModel(weightTicketRegistarationController.getCustomerModel());
                cbxCustomerN.setSelectedItem(customer);
            } else {
                cbxCustomerN.setSelectedIndex(-1);
            }

            validateForm();

            switch (modeDetail) {
                case IN_WAREHOUSE_TRANSFER:
                    txtWeightTickerRefN.setText(newWeightTicket.getWeightTicketIdRef());
            }

            // load sloc
            List<String> lgorts = weightTicketRegistarationController.getListLgortByMatnr(matnrs, false);
            loadSLoc(lgorts, strLgort);

            // load batch stock
            loadBatchStockModel(cbxSlocN, cbxBatchStockN, true);
        }

        @Override
        protected void failed(Throwable cause) {
            newWeightTicket.setWeightTicketDetails(new ArrayList<>());
            cbxMaterialTypeN.setSelectedItem("");
            txtWeightN.setText("0");
            cbxCustomerN.setSelectedIndex(-1);
            loadSLoc(null, null);

            isValidDO = false;
            // for check edit plateNo after check DO
            plateNoValidDO = "";
            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
            }
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());

            validateForm();
        }
    }

    private class SaveWeightTicketTask extends org.jdesktop.application.Task<Object, Void> {

        SaveWeightTicketTask(org.jdesktop.application.Application app) {
            super(app);
            setSaveNeeded(false);
            setClearable(false);
        }

        /*
         *  WYYMMXXXXX: Wplant(1) + YY + MM + XXXXX
         */
        private String getAutoGeneratedId(int seqByMonth) {
            String wPlantMap = StringUtil.isNotEmptyString(configuration.getWplantMap()) ? configuration.getWplantMap().substring(0, 1) : "0";
            String code = wPlantMap
                    + DateTimeFormatter.ofPattern("yyMMDD").format(LocalDateTime.now()).substring(0, 4)
                    + StringUtil.paddingZero(String.valueOf(seqByMonth), 5);
            return code;
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

            if (!isEditMode) {
                newWeightTicket.setId(getAutoGeneratedId(seqBMonth));
                newWeightTicket.setMandt(configuration.getSapClient());
                newWeightTicket.setWplant(configuration.getWkPlant());
                newWeightTicket.setSeqDay(seqBDay);
                newWeightTicket.setSeqMonth(seqBMonth);
                newWeightTicket.setCreatedTime(createdTime);
                newWeightTicket.setCreatedDate(now);
            } else {
                newWeightTicket.setUpdatedDate(now);
            }

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
            newWeightTicket.setSling(Integer.parseInt(txtSlingN.getText().trim()));
            newWeightTicket.setPallet(Integer.parseInt(txtPalletN.getText().trim()));
            newWeightTicket.setRecvPlant(configuration.getWkPlant());
            newWeightTicket.setSoNiemXa(txtSoNiemXaN.getText().trim());
            newWeightTicket.setBatch(txtProductionBatchN.getText().trim());
            newWeightTicket.setNote(txtNoteN.getText().trim());
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());

            // set for oubdel
            List<OutboundDeliveryDetail> deliveryDetails = null;
            if (!txtDONumN.getText().equals("")) {
                String[] do_list = txtDONumN.getText().split("-");
                for (int i = 0; i < do_list.length; i++) {
                    // save DO
                    String doNum = do_list[i].trim();
                    deliveryDetails = detailRepository.findByDeliveryOrderNo(doNum);
                    for (int j = 0; j < deliveryDetails.size(); j++) {
                        OutboundDeliveryDetail detail = deliveryDetails.get(j);
                        detail.setWtId(newWeightTicket.getId());
                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                        }
                        entityManager.merge(detail);
                        entityManager.getTransaction().commit();
                    }
                }
            }

            switch (modeDetail) {
                case IN_PO_PURCHASE:
                    updateDataForInPoPurchaseMode();
                    break;
                case IN_OTHER:
                    updateDataForOtherMode();
                    break;
                case OUT_SLOC_SLOC:
                    updateDataForOutSlocSloc();
                    break;
                case OUT_SELL_WATERWAY:
                    updateDataForOutSellWateway();
                    break;
                case OUT_PULL_STATION:
                    updateDataForPrepareOutPullStation();
                    break;
                case OUT_OTHER:
                    updateDataForOtherMode();
                    break;
            }

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

                if (!isEditMode) {
                    entityManager.persist(newWeightTicket);
                } else {
                    entityManager.merge(newWeightTicket);
                }

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

        public void updateDataForInPoPurchaseMode() {
            newWeightTicket.getWeightTicketDetail().setRegItemQuantity(new BigDecimal(txtWeightN.getText()));
        }

        public void updateDataForPrepareOutPullStation() {
            newWeightTicket.setMoveType("101");
            newWeightTicket.getWeightTicketDetail().setRegItemQuantity(new BigDecimal(txtWeightN.getText()));
        }

        public void updateDataForOutSellWateway() {
            if (WeighBridgeApp.getApplication().isOfflineMode()) {
                WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
                weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText()));
                weightTicketDetail.setSoNumber(txtSONumN.getText().trim());
            } else {
                if (listDONumbers != null) {
                    for (WeightTicketDetail weightTicketDetail : newWeightTicket.getWeightTicketDetails()) {
                        DOCheckStructure dOCheckStructure = listDONumbers.stream()
                                .filter(t -> t.getVbelnDO().equalsIgnoreCase(weightTicketDetail.getDeliveryOrderNo()))
                                .findFirst().get();

                        if (dOCheckStructure != null) {
                            weightTicketDetail.setSoNumber(dOCheckStructure.getVbelnSO());
                        }
                    }
                }
            }
        }

        public void updateDataForOtherMode() {
            WeightTicketDetail weightTicketDetail = new WeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());

            MaterialInternal material = (MaterialInternal) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText()));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }

            newWeightTicket.addWeightTicketDetail(weightTicketDetail);
        }

        public void updateDataForOutSlocSloc() {
            newWeightTicket.setMoveType("311");
            newWeightTicket.setMoveReas(null);
            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            Material material = (Material) cbxMaterialTypeN.getSelectedItem();

            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim()));
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

            clearForm();
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

    private void printWT(WeightTicket wt, boolean reprint) throws Exception {
        try {
            weightTicketRegistarationController.printRegWT(wt, reprint);
        } catch (Exception ex) {
            logger.error(null, ex);
            throw ex;
        }
    }

    private void cleanData() {
        newWeightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        isValidDO = false;
        isValidPO = false;
        isValidPOSTO = false;
        isValidSO = false;
        isValidWeight = false;
        isValidVendorLoad = false;
        isValidVendorTransport = false;
        plateNoValidDO = "";

        txtTicketIdN.setText("");
        txtWeightTickerRefN.setText("");
        txtRegisterIdN.setText("");
        txtDriverNameN.setText("");
        txtCMNDN.setText("");
        txtPlateNoN.setText("");
        txtTonnageN.setText("0");
        txtTrailerNoN.setText("");
        txtSlingN.setText("0");
        txtPalletN.setText("0");
        txtSoNiemXaN.setText("");
        txtProductionBatchN.setText("");
        txtNoteN.setText("");
        txtDONumN.setText("");
        txtPONumN.setText("");
        txtPOSTONumN.setText("");
        txtSONumN.setText("");
        cbxMaterialTypeN.setSelectedIndex(-1);
        txtWeightN.setText("0.000");
        cbxSlocN.setModel(new DefaultComboBoxModel());
        cbxSlocN.setSelectedIndex(-1);
        cbxSloc2N.setModel(new DefaultComboBoxModel());
        cbxSloc2N.setSelectedIndex(-1);
        cbxBatchStockN.setModel(new DefaultComboBoxModel());
        cbxBatchStockN.setSelectedIndex(-1);
        cbxBatchStock2N.setModel(new DefaultComboBoxModel());
        cbxBatchStock2N.setSelectedIndex(-1);
        cbxVendorLoadingN.setSelectedIndex(-1);
        cbxVendorTransportN.setSelectedIndex(-1);
        cbxCustomerN.setSelectedIndex(-1);

        disableAllInForm();
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
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDOCheckN;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnHideFilter;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnPOCheckN;
    private javax.swing.JButton btnPOSTOCheckN;
    private javax.swing.JButton btnReprint;
    private javax.swing.JButton btnSOCheckN;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnShowFilter;
    private javax.swing.JComboBox cbxBatchStock2N;
    private javax.swing.JComboBox cbxBatchStockN;
    private javax.swing.JComboBox cbxCustomerN;
    private javax.swing.JComboBox cbxHourFrom;
    private javax.swing.JComboBox cbxHourTo;
    private javax.swing.JComboBox cbxMaterialType;
    private javax.swing.JComboBox cbxMaterialTypeN;
    private javax.swing.JComboBox cbxModeSearch;
    private javax.swing.JComboBox cbxModeType;
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
    private javax.swing.JLabel lblCustomerN;
    private javax.swing.JLabel lblDONumN;
    private javax.swing.JLabel lblDateFrom;
    private javax.swing.JLabel lblDateTo;
    private javax.swing.JLabel lblDriverName;
    private javax.swing.JLabel lblDriverNameN;
    private javax.swing.JLabel lblHourFrom;
    private javax.swing.JLabel lblHourTo;
    private javax.swing.JLabel lblMaterialType;
    private javax.swing.JLabel lblMaterialTypeN;
    private javax.swing.JLabel lblMode;
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
    private javax.swing.JPanel pnPrintControl;
    private javax.swing.JPanel pnROVLeft;
    private javax.swing.JPanel pnROVRight;
    private javax.swing.JPanel pnRegistrationOfVehicle;
    private javax.swing.JPanel pnShowFilter;
    private javax.swing.JRadioButton rbtInput;
    private javax.swing.JRadioButton rbtOutput;
    private javax.swing.ButtonGroup rbtRegCatGroup;
    private javax.swing.JScrollPane spnResult;
    private org.jdesktop.swingx.JXTable tabResults;
    private javax.swing.JTextField txtCMNDN;
    private javax.swing.JTextField txtCreator;
    private javax.swing.JTextField txtDONumN;
    private javax.swing.JTextField txtDriverName;
    private javax.swing.JTextField txtDriverNameN;
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
    private javax.swing.JTextField txtTicketIdN;
    private javax.swing.JFormattedTextField txtTonnageN;
    private javax.swing.JTextField txtTrailerNoN;
    private javax.swing.JFormattedTextField txtWeightN;
    private javax.swing.JTextField txtWeightTickerRefN;
    private javax.swing.JTextField txtWeightTicketNo;
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

    @Action(block = Task.BlockingScope.ACTION)
    public Task checkPO() {
        String poNum = StringUtil.paddingZero(txtPONumN.getText().trim(), 10);
        txtPONumN.setText(poNum);

        return new CheckPOTask(Application.getInstance(WeighBridgeApp.class));
    }

    private class CheckPOTask extends Task<Object, Void> {

        private String strVendor = "";
        private String strMatnr = null;
        private BigDecimal totalWeight = BigDecimal.ZERO;
        private Customer customer = null;
        private String strLgort = "";

        CheckPOTask(Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() throws Exception {
            String poNum = txtPONumN.getText().trim();

            // get local PO
            setStep(1, resourceMapMsg.getString("msg.checkPOInDB"));
            purchaseOrderPO = purchaseOrderRepository.findByPoNumber(poNum);

            // sync from SAP
            if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                purchaseOrderPO = syncPurchaseOrder(poNum, purchaseOrderPO);

                if (purchaseOrderPO != null) {
                    String kunnr = purchaseOrderPO.getCustomer();
                    if (kunnr != null && !kunnr.trim().isEmpty()) {
                        customer = syncCustomer(kunnr.trim());
                    }
                }
            }
            
            // check compare PO with POSTO
            if ((!txtPOSTONumN.getText().trim().isEmpty()) && (purchaseOrderPOSTO != null) && (poNum.equals(purchaseOrderPOSTO.getPoNumber()))) {
                throw new Exception(resourceMapMsg.getString("msg.duplicatePo"));
            }

            // check exist PO
            if (purchaseOrderPO == null) {
                throw new Exception(resourceMapMsg.getString("msg.poNotExist", poNum));
            }

            //Check PO Plant with Configuration parameter.
            if ((modeDetail == MODE_DETAIL.IN_PO_PURCHASE || modeDetail == MODE_DETAIL.OUT_SLOC_SLOC)
                    && (!(purchaseOrderPO.getPurchaseOrderDetail().getPlant()).equals(configuration.getWkPlant()))) {
                throw new Exception(resourceMapMsg.getString("msg.poIsDenied", poNum));
            }
            if ((modeDetail == MODE_DETAIL.OUT_PLANT_PLANT
                    || modeDetail == MODE_DETAIL.OUT_PULL_STATION)
                    && (!(purchaseOrderPO.getSupplPlnt()).equals(configuration.getWkPlant()))) {
                throw new Exception(resourceMapMsg.getString("msg.poIsDenied", poNum));
            }

            updateWeightTicket(purchaseOrderPO);

            setStep(4, null);
            return null;
        }

        @Override
        protected void succeeded(Object t) {
            isValidPO = true;
            txtWeightN.setText(totalWeight.toString());
            Material temp = weightTicketRegistarationController.getMaterial(strMatnr);
            if (temp == null) {
                sapService.syncMaterial();
                temp = weightTicketRegistarationController.getMaterial(strMatnr);
            }

            if (temp == null && !strMatnr.isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.materialNotExist", strMatnr));
            }

            cbxMaterialTypeN.setSelectedItem(temp);

            // load sloc
            if (temp != null) {
                List<String> lgorts = weightTicketRegistarationController.getListLgortByMatnr(temp.getMatnr(), false);
                loadSLoc(lgorts, strLgort);
            }

            // load batch stock
            loadBatchStockModel(cbxSlocN, cbxBatchStockN, true);
            loadBatchStockModel(cbxSloc2N, cbxBatchStock2N, false);
            
            // load data vendor
            if (modeDetail == MODE_DETAIL.OUT_PLANT_PLANT
                    || modeDetail == MODE_DETAIL.OUT_PULL_STATION) {
                DefaultComboBoxModel vendorModel = weightTicketRegistarationController.getVendorModelByEkorg(purchaseOrderPO.getPurchaseOrderDetail().getPlant());
                DefaultComboBoxModel vendor2Model = (DefaultComboBoxModel) SerializationUtils.clone(vendorModel);
                cbxVendorLoadingN.setModel(vendorModel);
                cbxVendorLoadingN.setSelectedIndex(-1);
                cbxVendorTransportN.setModel(vendor2Model);
                cbxVendorTransportN.setSelectedIndex(-1);
            }

            if (customer != null) {
                cbxCustomerN.setModel(weightTicketRegistarationController.getCustomerModel());
                cbxCustomerN.setSelectedItem(customer);
            } else {
                cbxCustomerN.setSelectedIndex(-1);
            }

            switch (modeDetail) {
                case OUT_SLOC_SLOC:
                    cbxVendorTransportN.setSelectedItem(weightTicketRegistarationController.getVendor(strVendor));
                    break;
                case OUT_PLANT_PLANT:
                    String sloc = purchaseOrderPO.getPurchaseOrderDetail().getStgeLoc();
                    cbxSlocN.setSelectedItem(weightTicketRegistarationController.getSloc(sloc));
            }

            validateForm();
        }

        @Override
        protected void failed(Throwable cause) {
            newWeightTicket.setWeightTicketDetails(new ArrayList<>());
            cbxMaterialTypeN.setSelectedIndex(-1);
            cbxVendorTransportN.setSelectedIndex(-1);
            cbxCustomerN.setSelectedIndex(-1);
            loadSLoc(null, null);

            isValidPO = false;
            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
            }
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());

            validateForm();
        }

        private void setStep(int step, String msg) {
            if (StringUtil.isNotEmptyString(msg)) {
                setMessage(msg);
            }
            setProgress(step, 1, 4);
        }

        private PurchaseOrder syncPurchaseOrder(String poNum, PurchaseOrder purchaseOrder) {
            try {
                setStep(2, resourceMapMsg.getString("checkPOInSap"));
                PurchaseOrder sapPurchaseOrder = sapService.getPurchaseOrder(poNum);
                List<PurchaseOrderDetail> poItems = sapPurchaseOrder.getPurchaseOrderDetails();

                setStep(3, resourceMapMsg.getString("msg.saveDataPOToDb"));
                return sapService.syncPurchaseOrder(sapPurchaseOrder, purchaseOrder);
            } catch (Exception ex) {
                return null;
            }
        }

        private Customer syncCustomer(String kunnr) {
            try {
                Customer dbCustomer = weightTicketRegistarationController.findByKunnr(kunnr);
                Customer sapCustomer = sapService.getCustomer(kunnr);
                return sapService.syncCustomer(sapCustomer, dbCustomer);
            } catch (Exception ex) {
                return null;
            }
        }

        private void updateWeightTicket(PurchaseOrder purchaseOrder) {
            PurchaseOrderDetail purchaseOrderDetail = purchaseOrder.getPurchaseOrderDetail();
            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setEbeln(purchaseOrder.getPoNumber());
            weightTicketDetail.setItem(purchaseOrderDetail.getPoItem());
            weightTicketDetail.setRegItemDescription(purchaseOrderDetail.getShortText());
            weightTicketDetail.setRegItemQuantity(purchaseOrderDetail.getQuantity());
            weightTicketDetail.setMatnrRef(purchaseOrderDetail.getMaterial());
            weightTicketDetail.setKunnr(purchaseOrder.getCustomer());
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());

            strMatnr = purchaseOrderDetail.getMaterial();
            strLgort = purchaseOrderDetail.getStgeLoc();

            strVendor = purchaseOrder.getVendor();
            totalWeight = purchaseOrderDetail.getQuantity();

            switch (modeDetail) {
                case IN_PO_PURCHASE:
                    BigDecimal quantity = purchaseOrderDetail.getQuantity() != null ? purchaseOrderDetail.getQuantity() : BigDecimal.ZERO;
                    //BigDecimal tolerance = purchaseOrderDetail.getUnderDlvTol() != null ? purchaseOrderDetail.getUnderDlvTol() : BigDecimal.ZERO;
                    BigDecimal tolerance = purchaseOrderDetail.getOverDlvTol() != null ? purchaseOrderDetail.getOverDlvTol() : BigDecimal.ZERO;

                    numCheckWeight = quantity.add(
                            quantity.multiply(tolerance).divide(new BigDecimal(100))
                    ).subtract(weightTicketRegistarationController.getSumQuantityWithPoNo(purchaseOrder.getPoNumber()));

                    if (numCheckWeight.compareTo(BigDecimal.ZERO) < 0) {
                        numCheckWeight = BigDecimal.ZERO;
                    }

                    totalWeight = numCheckWeight;
                    isValidWeight = true;
                    break;
                case OUT_SLOC_SLOC:
                    newWeightTicket.getWeightTicketDetail().setTransVendor(purchaseOrder.getVendor());
                    break;
            }
        }
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task checkPOSTO() {
        String postoNum = StringUtil.paddingZero(txtPOSTONumN.getText().trim(), 10);
        txtPOSTONumN.setText(postoNum);

        return new CheckPOSTOTask(Application.getInstance(WeighBridgeApp.class));
    }

    private class CheckPOSTOTask extends Task<Object, Void> {

        private String strVendor = "";

        CheckPOSTOTask(Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() throws Exception {
            String postoNum = txtPOSTONumN.getText().trim();

            // check compare PO with POSTO
            if ((!txtPONumN.getText().trim().isEmpty()) && (purchaseOrderPO != null) && (postoNum.equals(purchaseOrderPO.getPoNumber()))) {
                throw new Exception(resourceMapMsg.getString("msg.duplicatePo"));
            }

            // get local POSTO
            setStep(1, resourceMapMsg.getString("msg.checkPOSTOInDB"));
            purchaseOrderPOSTO = purchaseOrderRepository.findByPoNumber(postoNum);

            // sync from SAP
            if (!WeighBridgeApp.getApplication().isOfflineMode()) {
                purchaseOrderPOSTO = syncPurchaseOrder(postoNum, purchaseOrderPOSTO);
            }

            // check exist PO
            if (purchaseOrderPOSTO == null) {
                throw new Exception(resourceMapMsg.getString("msg.postoNotExist", postoNum));
            }

            //Check PO Plant with Configuration parameter.
            if ((modeDetail == MODE_DETAIL.IN_PO_PURCHASE)
                    && (!(purchaseOrderPOSTO.getPurchaseOrderDetail().getPlant()).equals(configuration.getWkPlant()))) {
                throw new Exception(resourceMapMsg.getString("msg.postoIsDenied", postoNum));
            }

            //Check POSTO Plant with PO plant
            if (!purchaseOrderPOSTO.getPurchaseOrderDetail().getPlant().equals(configuration.getWkPlant())) {
                throw new Exception(resourceMapMsg.getString("msg.postoIsPlant", postoNum));
            }

            //Check matnr for PO-POSTO
            if ((modeDetail == MODE_DETAIL.OUT_PULL_STATION)
                    && (!purchaseOrderPOSTO.getPurchaseOrderDetail().getMaterial().equals(purchaseOrderPO.getPurchaseOrderDetail().getMaterial()))) {
                throw new Exception(resourceMapMsg.getString("msg.postoIsMatnr"));
            }

            updateWeightTicket(purchaseOrderPOSTO);

            setStep(4, null);
            return null;
        }

        @Override
        protected void succeeded(Object t) {
            isValidPOSTO = true;
            switch (modeDetail) {
                case OUT_SLOC_SLOC:
                    cbxVendorLoadingN.setSelectedItem(weightTicketRegistarationController.getVendor(strVendor));
                    break;
            }

            validateForm();
        }

        @Override
        protected void failed(Throwable cause) {
            isValidPOSTO = false;
            cbxVendorLoadingN.setSelectedIndex(-1);

            if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                cause = cause.getCause();
            }
            logger.error(null, cause);
            JOptionPane.showMessageDialog(rootPane, cause.getMessage());

            validateForm();
        }

        private void setStep(int step, String msg) {
            if (StringUtil.isNotEmptyString(msg)) {
                setMessage(msg);
            }
            setProgress(step, 1, 4);
        }

        private PurchaseOrder syncPurchaseOrder(String poNum, PurchaseOrder purchaseOrder) {
            try {
                setStep(2, resourceMapMsg.getString("checkPOSTOInSap"));
                PurchaseOrder sapPurchaseOrder = sapService.getPurchaseOrder(poNum);

                setStep(3, resourceMapMsg.getString("msg.saveDataPOSTOToDb"));
                return sapService.syncPurchaseOrder(sapPurchaseOrder, purchaseOrder);
            } catch (Exception ex) {
                return null;
            }
        }

        private void updateWeightTicket(PurchaseOrder purchaseOrder) {
            newWeightTicket.setPosto(purchaseOrder.getPoNumber());
            strVendor = purchaseOrder.getVendor();
            switch (modeDetail) {
                case OUT_SLOC_SLOC:
                    newWeightTicket.getWeightTicketDetail().setLoadVendor(purchaseOrder.getVendor());
                    break;
            }
        }
    }

    @Action(block = Task.BlockingScope.ACTION)
    public Task editOfflineRecord() {
        return new EditOfflineRecordTask(Application.getInstance(WeighBridgeApp.class));
    }

    private class EditOfflineRecordTask extends Task<Object, Void> {

        EditOfflineRecordTask(Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            newRecord();
            loadModeTypeModel(selectedWeightTicket.getRegType() == 'I' ? MODE.INPUT : MODE.OUTPUT);
            modeDetail = MODE_DETAIL.valueOf(selectedWeightTicket.getMode());
            cbxModeType.setSelectedItem(new WeighingMode(modeDetail, null));

            rbtInput.setEnabled(false);
            rbtOutput.setEnabled(false);
            cbxModeType.setEnabled(false);

            if (modeDetail == MODE_DETAIL.IN_WAREHOUSE_TRANSFER || modeDetail == MODE_DETAIL.OUT_SELL_ROAD) {
                showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, false);
                showComponent(txtSONumN, lblSONumN, btnSOCheckN, true, true);
            }

            newWeightTicket = selectedWeightTicket;
            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();

            txtTicketIdN.setText(newWeightTicket.getTicketId());
            txtWeightTickerRefN.setText(newWeightTicket.getWeightTicketIdRef());
            txtRegisterIdN.setText(newWeightTicket.getRegisteredNumber());
            txtDriverNameN.setText(newWeightTicket.getDriverName());
            txtCMNDN.setText(newWeightTicket.getDriverIdNo());
            txtPlateNoN.setText(newWeightTicket.getPlateNo());
            txtTonnageN.setText("0");
            txtTrailerNoN.setText(newWeightTicket.getTrailerId());
            txtSlingN.setText(Integer.toString(newWeightTicket.getSling()));
            txtPalletN.setText(Integer.toString(newWeightTicket.getPallet()));
            txtSoNiemXaN.setText(newWeightTicket.getSoNiemXa());
            txtProductionBatchN.setText(newWeightTicket.getBatch());
            txtNoteN.setText(newWeightTicket.getNote());
            txtDONumN.setText(weightTicketDetail.getDeliveryOrderNo());
            txtPONumN.setText(weightTicketDetail.getEbeln());
            txtPOSTONumN.setText(newWeightTicket.getPosto());
            txtSONumN.setText(weightTicketDetail.getSoNumber());
            cbxMaterialTypeN.setSelectedItem(weightTicketRegistarationController.getMaterial(weightTicketDetail.getMatnrRef()));
            txtWeightN.setText(weightTicketDetail.getRegItemQuantity().toString());

            // load sloc
            boolean isInternal = modeDetail == MODE_DETAIL.IN_OTHER || modeDetail == MODE_DETAIL.OUT_OTHER;
            List<String> lgorts = weightTicketRegistarationController.getListLgortByMatnr(weightTicketDetail.getMatnrRef(), isInternal);
            loadSLoc(lgorts, null);
            cbxSlocN.setSelectedItem(new SLoc(newWeightTicket.getLgort()));
            cbxSloc2N.setSelectedItem(new SLoc(newWeightTicket.getRecvLgort()));

            cbxBatchStockN.setModel(new DefaultComboBoxModel());
            cbxBatchStockN.setSelectedIndex(-1);
            cbxBatchStock2N.setModel(new DefaultComboBoxModel());
            cbxBatchStock2N.setSelectedIndex(-1);
            cbxVendorLoadingN.setSelectedItem(new Vendor(weightTicketDetail.getLoadVendor()));
            cbxVendorTransportN.setSelectedItem(new Vendor(weightTicketDetail.getTransVendor()));

            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            isEditMode = true;
            validateForm();
        }
    }

}
