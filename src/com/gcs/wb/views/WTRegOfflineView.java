/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.views;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE;
import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;
import com.gcs.wb.base.enums.ModeEnum;
import com.gcs.wb.base.enums.StatusEnum;
import com.gcs.wb.base.util.ExceptionUtil;
import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.base.validator.DateFromToValidator;
import com.gcs.wb.controller.WeightTicketRegistarationController;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.entity.*;
import com.gcs.wb.model.WeighingMode;
import com.gcs.wb.views.validations.WeightTicketRegistrationValidation;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import org.apache.commons.lang.SerializationUtils;
import org.jdesktop.application.Application;

public class WTRegOfflineView extends javax.swing.JInternalFrame {

    EntityManager entityManager = JPAConnector.getInstance();
    EntityTransaction entityTransaction = entityManager.getTransaction();
    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    WeightTicketRegistarationController weightTicketRegistarationController = new WeightTicketRegistarationController();
    public ResourceMap resourceMapMsg = Application.getInstance(WeighBridgeApp.class).getContext().getResourceMap(WTRegOfflineView.class);
    private static final Logger logger = Logger.getLogger(WTRegOfflineView.class);
    private final List<WeightTicket> weightTicketList;
    private boolean isValidVendorLoad = false;
    private boolean isValidVendorTransport = false;
    private boolean isValidSloc = false;
    private boolean isValidPlateNo = false;

    private boolean formValid;
    private WeightTicket newWeightTicket;
    private WeightTicket selectedWeightTicket;
    private boolean[] editable = null;
    Object[][] wtData = null;
    Object[] wtCols = Constants.WTRegView.WEIGHTTICKET_COLUMS;
    Class[] wtTypes = Constants.WTRegView.WEIGHTTICKET_TYPES;
    private DecimalFormat df = new DecimalFormat("#,##0.000");
    // TODO update ui
    private MODE mode = MODE.OUTPUT;
    private MODE_DETAIL modeDetail;
    private final DateFromToValidator dateFromToValidator = new DateFromToValidator();
    public static final String SDATE = "date";
    private WeightTicketRegistrationValidation wtRegisValidation;

    DefaultComboBoxModel materialModel = weightTicketRegistarationController.getListMaterial();
    DefaultComboBoxModel materialInternalModel = weightTicketRegistarationController.getListMaterialInternal();
    DefaultComboBoxModel vendorModel = weightTicketRegistarationController.getVendorModel();
    DefaultComboBoxModel vendor2Model = (DefaultComboBoxModel) SerializationUtils.clone(vendorModel);
    DefaultComboBoxModel vendorCustomerModel = weightTicketRegistarationController.getCusVendorModel();
    DefaultComboBoxModel customerModel = weightTicketRegistarationController.getCustomerModel();

    public WTRegOfflineView() {
        newWeightTicket = new WeightTicket();
        selectedWeightTicket = new WeightTicket();
        weightTicketList = new ArrayList<>();
        wtRegisValidation = new WeightTicketRegistrationValidation(rootPane, resourceMapMsg);
        initComponents();
        dpDateFrom.setFormats(Constants.Date.FORMAT);
        dpDateTo.setFormats(Constants.Date.FORMAT);
        pnShowFilter.setVisible(false);
        btnReprint.setEnabled(false);

        initComboboxModel();
        initComboboxRenderer();
        initTableEvent();
        cbxHourTo.setSelectedIndex(23);
        cbxModeSearch.setModel(new DefaultComboBoxModel<>(ModeEnum.values()));
        cbxStatus.setModel(new DefaultComboBoxModel<>(StatusEnum.values()));

        initDocumentListener(txtWeightN);
        initDocumentListener(txtSlingN);
        initDocumentListener(txtPalletN);

        btnFind.doClick();
    }

    private void initDocumentListener(JTextField jtext) {
        jtext.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateForm();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateForm();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateForm();
            }
        });
    }

    private void initTableEvent() {
        tabResults.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            try {
                selectedWeightTicket = weightTicketList.get(tabResults.convertRowIndexToModel(tabResults.getSelectedRow()));
                btnReprint.setEnabled(selectedWeightTicket != null);
            } catch (Exception ex) {
                btnReprint.setEnabled(false);
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
                    setText(vendor.getName1() + " " + vendor.getName2());
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
                    String name = customer.getName2();
                    if (!StringUtil.isEmptyString(customer.getName3())) {
                        name += " " + customer.getName3();
                    }
                    if (!StringUtil.isEmptyString(customer.getName4())) {
                        name += " " + customer.getName4();
                    }
                    setText(!StringUtil.isEmptyString(name) ? name : customer.getName1());
                    setToolTipText(customer.getKunnr());
                }

                if (value instanceof Vendor) {
                    Vendor vendor = (Vendor) value;
                    setText(vendor.getName1() + " " + vendor.getName2());
                    setToolTipText(vendor.getLifnr());
                }

                return this;
            }
        });

        cbxShipToN.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Customer) {
                    Customer customer = (Customer) value;
                    String name = customer.getName2();
                    if (!StringUtil.isEmptyString(customer.getName3())) {
                        name += " " + customer.getName3();
                    }
                    if (!StringUtil.isEmptyString(customer.getName4())) {
                        name += " " + customer.getName4();
                    }

                    setText(!StringUtil.isEmptyString(name) ? name : customer.getName1());
                    setToolTipText(customer.getKunnr());
                }

                return this;
            }
        });
    }

    private void initComboboxModel() {
        cbxCustomerN.setModel(customerModel);
        cbxCustomerN.setSelectedIndex(-1);
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
        lblSalanN = new javax.swing.JLabel();
        txtSalanN = new javax.swing.JTextField();
        lblLoadSourceN = new javax.swing.JLabel();
        txtLoadSourceN = new javax.swing.JTextField();
        pnControl = new javax.swing.JPanel();
        btnNew = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        pnROVRight = new javax.swing.JPanel();
        lblMaterialTypeN = new javax.swing.JLabel();
        lblWeightN = new javax.swing.JLabel();
        lblWeightUnitN = new javax.swing.JLabel();
        lblDONumN = new javax.swing.JLabel();
        lblSONumN = new javax.swing.JLabel();
        lblPONumN = new javax.swing.JLabel();
        lblPOSTONumN = new javax.swing.JLabel();
        lblVendorTransportN = new javax.swing.JLabel();
        cbxMaterialTypeN = new javax.swing.JComboBox();
        txtWeightN = new javax.swing.JFormattedTextField();
        txtDONumN = new javax.swing.JTextField();
        btnDOCheckN = new javax.swing.JButton();
        txtSONumN = new javax.swing.JTextField();
        btnSOCheckN = new javax.swing.JButton();
        txtPONumN = new javax.swing.JTextField();
        btnPOCheckN = new javax.swing.JButton();
        txtPOSTONumN = new javax.swing.JTextField();
        btnPOSTOCheckN = new javax.swing.JButton();
        lblWeightTicketNo = new javax.swing.JLabel();
        txtWeightTicketNo = new javax.swing.JTextField();
        lblCustomerN = new javax.swing.JLabel();
        cbxCustomerN = new javax.swing.JComboBox();
        lblShipToN = new javax.swing.JLabel();
        cbxShipToN = new javax.swing.JComboBox();
        lblSlocN = new javax.swing.JLabel();
        cbxSlocN = new javax.swing.JComboBox();
        lblSloc2N = new javax.swing.JLabel();
        cbxSloc2N = new javax.swing.JComboBox();
        lblBatchStockN = new javax.swing.JLabel();
        cbxBatchStockN = new javax.swing.JComboBox();
        lblBatchStock2N = new javax.swing.JLabel();
        cbxBatchStock2N = new javax.swing.JComboBox();
        cbxVendorLoadingN = new javax.swing.JComboBox();
        cbxVendorTransportN = new javax.swing.JComboBox();
        lblVendorLoadingN = new javax.swing.JLabel();
        pnShowFilter = new javax.swing.JPanel();
        btnShowFilter = new javax.swing.JButton();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getResourceMap(WTRegOfflineView.class);
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

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class).getContext().getActionMap(WTRegOfflineView.class, this);
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
                            .addComponent(cbxStatus, 0, 190, Short.MAX_VALUE)
                            .addComponent(txtPlateNo, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                            .addComponent(dpDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE))
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

        javax.swing.GroupLayout pnPrintControlLayout = new javax.swing.GroupLayout(pnPrintControl);
        pnPrintControl.setLayout(pnPrintControlLayout);
        pnPrintControlLayout.setHorizontalGroup(
            pnPrintControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnPrintControlLayout.createSequentialGroup()
                .addComponent(btnReprint)
                .addGap(923, 923, 923))
        );
        pnPrintControlLayout.setVerticalGroup(
            pnPrintControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnReprint)
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

        lblSalanN.setText(resourceMap.getString("lblSalanN.text")); // NOI18N
        lblSalanN.setName("lblSalanN"); // NOI18N

        txtSalanN.setName("txtSalanN"); // NOI18N
        txtSalanN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSalanNFocusLost(evt);
            }
        });
        txtSalanN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSalanNKeyReleased(evt);
            }
        });

        lblLoadSourceN.setText(resourceMap.getString("lblLoadSourceN.text")); // NOI18N
        lblLoadSourceN.setName("lblLoadSourceN"); // NOI18N

        txtLoadSourceN.setName("txtLoadSourceN"); // NOI18N
        txtLoadSourceN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLoadSourceNKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout pnROVLeftLayout = new javax.swing.GroupLayout(pnROVLeft);
        pnROVLeft.setLayout(pnROVLeftLayout);
        pnROVLeftLayout.setHorizontalGroup(
            pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNoteN)
                    .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnROVLeftLayout.createSequentialGroup()
                            .addGap(51, 51, 51)
                            .addComponent(lblPlateNoN))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnROVLeftLayout.createSequentialGroup()
                            .addComponent(rbtInput)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(rbtOutput)))
                    .addComponent(lblDriverNameN)
                    .addComponent(lblRegisterIdN)
                    .addComponent(lblWeightTickerRefN)
                    .addComponent(lblTicketIdN)
                    .addComponent(lblCMNDN)
                    .addComponent(lblSoNiemXaN)
                    .addComponent(lblSlingN)
                    .addComponent(lblProductionBatchN)
                    .addComponent(lblSalanN)
                    .addComponent(lblLoadSourceN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtProductionBatchN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtLoadSourceN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtSalanN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(cbxModeType, 0, 359, Short.MAX_VALUE)
                    .addComponent(txtTicketIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtCMNDN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtWeightTickerRefN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtDriverNameN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addComponent(txtRegisterIdN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
                    .addGroup(pnROVLeftLayout.createSequentialGroup()
                        .addComponent(txtPlateNoN, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTrailerNoN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTrailerNoN, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lblTonnageN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTonnageN, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE))
                    .addGroup(pnROVLeftLayout.createSequentialGroup()
                        .addComponent(txtSlingN, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addComponent(lblPalletN)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPalletN, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                    .addComponent(txtNoteN, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE))
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
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSalanN)
                    .addComponent(txtSalanN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSlingN)
                    .addComponent(txtSlingN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPalletN)
                    .addComponent(txtPalletN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoNiemXaN)
                    .addComponent(txtSoNiemXaN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblProductionBatchN)
                    .addComponent(txtProductionBatchN, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoadSourceN)
                    .addComponent(txtLoadSourceN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNoteN)
                    .addComponent(txtNoteN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addContainerGap(298, Short.MAX_VALUE))
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
                        .addComponent(pnControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(11, 11, 11))
                    .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                        .addComponent(pnROVLeft, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        pnRegistrationOfVehicleLayout.setVerticalGroup(
            pnRegistrationOfVehicleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnRegistrationOfVehicleLayout.createSequentialGroup()
                .addComponent(pnROVLeft, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(136, Short.MAX_VALUE))
        );

        pnROVRight.setName("pnROVRight"); // NOI18N

        lblMaterialTypeN.setText(resourceMap.getString("lblMaterialTypeN.text")); // NOI18N
        lblMaterialTypeN.setName("lblMaterialTypeN"); // NOI18N

        lblWeightN.setText(resourceMap.getString("lblWeightN.text")); // NOI18N
        lblWeightN.setName("lblWeightN"); // NOI18N

        lblWeightUnitN.setText(resourceMap.getString("lblWeightUnitN.text")); // NOI18N
        lblWeightUnitN.setName("lblWeightUnitN"); // NOI18N

        lblDONumN.setText(resourceMap.getString("lblDONumN.text")); // NOI18N
        lblDONumN.setName("lblDONumN"); // NOI18N

        lblSONumN.setText(resourceMap.getString("lblSONumN.text")); // NOI18N
        lblSONumN.setName("lblSONumN"); // NOI18N

        lblPONumN.setText(resourceMap.getString("lblPONumN.text")); // NOI18N
        lblPONumN.setName("lblPONumN"); // NOI18N

        lblPOSTONumN.setText(resourceMap.getString("lblPOSTONumN.text")); // NOI18N
        lblPOSTONumN.setName("lblPOSTONumN"); // NOI18N

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
        txtWeightN.setName("txtWeightN"); // NOI18N
        txtWeightN.setVerifyInputWhenFocusTarget(false);
        txtWeightN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWeightNFocusLost(evt);
            }
        });
        txtWeightN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtWeightNKeyReleased(evt);
            }
        });

        txtDONumN.setName("txtDONumN"); // NOI18N
        txtDONumN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDONumNFocusLost(evt);
            }
        });
        txtDONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtDONumNKeyReleased(evt);
            }
        });

        btnDOCheckN.setText(resourceMap.getString("btnDOCheckN.text")); // NOI18N
        btnDOCheckN.setName("btnDOCheckN"); // NOI18N

        txtSONumN.setName("txtSONumN"); // NOI18N
        txtSONumN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSONumNFocusLost(evt);
            }
        });
        txtSONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSONumNKeyReleased(evt);
            }
        });

        btnSOCheckN.setText(resourceMap.getString("btnSOCheckN.text")); // NOI18N
        btnSOCheckN.setName("btnSOCheckN"); // NOI18N

        txtPONumN.setName("txtPONumN"); // NOI18N
        txtPONumN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPONumNFocusLost(evt);
            }
        });
        txtPONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPONumNKeyReleased(evt);
            }
        });

        btnPOCheckN.setText(resourceMap.getString("btnPOCheckN.text")); // NOI18N
        btnPOCheckN.setName("btnPOCheckN"); // NOI18N

        txtPOSTONumN.setName("txtPOSTONumN"); // NOI18N
        txtPOSTONumN.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPOSTONumNFocusLost(evt);
            }
        });
        txtPOSTONumN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPOSTONumNKeyReleased(evt);
            }
        });

        btnPOSTOCheckN.setText(resourceMap.getString("btnPOSTOCheckN.text")); // NOI18N
        btnPOSTOCheckN.setName("btnPOSTOCheckN"); // NOI18N

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

        lblShipToN.setText(resourceMap.getString("lblShipToN.text")); // NOI18N
        lblShipToN.setName("lblShipToN"); // NOI18N

        cbxShipToN.setName("cbxShipToN"); // NOI18N
        cbxShipToN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxShipToNActionPerformed(evt);
            }
        });

        lblSlocN.setText(resourceMap.getString("lblSlocN.text")); // NOI18N
        lblSlocN.setName("lblSlocN"); // NOI18N

        cbxSlocN.setName("cbxSlocN"); // NOI18N
        cbxSlocN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSlocNActionPerformed(evt);
            }
        });

        lblSloc2N.setText(resourceMap.getString("lblSloc2N.text")); // NOI18N
        lblSloc2N.setName("lblSloc2N"); // NOI18N

        cbxSloc2N.setName("cbxSloc2N"); // NOI18N
        cbxSloc2N.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSloc2NActionPerformed(evt);
            }
        });

        lblBatchStockN.setText(resourceMap.getString("lblBatchStockN.text")); // NOI18N
        lblBatchStockN.setName("lblBatchStockN"); // NOI18N

        cbxBatchStockN.setName("cbxBatchStockN"); // NOI18N
        cbxBatchStockN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBatchStockNActionPerformed(evt);
            }
        });

        lblBatchStock2N.setText(resourceMap.getString("lblBatchStock2N.text")); // NOI18N
        lblBatchStock2N.setName("lblBatchStock2N"); // NOI18N

        cbxBatchStock2N.setName("cbxBatchStock2N"); // NOI18N
        cbxBatchStock2N.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxBatchStock2NActionPerformed(evt);
            }
        });

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

        lblVendorLoadingN.setText(resourceMap.getString("lblVendorLoadingN.text")); // NOI18N
        lblVendorLoadingN.setName("lblVendorLoadingN"); // NOI18N

        javax.swing.GroupLayout pnROVRightLayout = new javax.swing.GroupLayout(pnROVRight);
        pnROVRight.setLayout(pnROVRightLayout);
        pnROVRightLayout.setHorizontalGroup(
            pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnROVRightLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblVendorLoadingN)
                    .addComponent(lblWeightTicketNo)
                    .addComponent(lblMaterialTypeN)
                    .addComponent(lblPOSTONumN)
                    .addComponent(lblPONumN)
                    .addComponent(lblWeightN)
                    .addComponent(lblSONumN)
                    .addComponent(lblDONumN)
                    .addComponent(lblCustomerN)
                    .addComponent(lblShipToN)
                    .addComponent(lblSlocN)
                    .addComponent(lblSloc2N)
                    .addComponent(lblBatchStockN)
                    .addComponent(lblBatchStock2N)
                    .addComponent(lblVendorTransportN))
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
                    .addComponent(cbxShipToN, 0, 374, Short.MAX_VALUE)
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
                .addGap(45, 45, 45))
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
                    .addComponent(lblShipToN)
                    .addComponent(cbxShipToN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(cbxVendorLoadingN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorLoadingN))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnROVRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxVendorTransportN, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblVendorTransportN))
                .addContainerGap())
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
                .addContainerGap(1074, Short.MAX_VALUE)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnROVRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(spnResult, javax.swing.GroupLayout.DEFAULT_SIZE, 1169, Short.MAX_VALUE)
                    .addComponent(pnPrintControl, javax.swing.GroupLayout.DEFAULT_SIZE, 1169, Short.MAX_VALUE)
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
                .addComponent(spnResult, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnPrintControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnROVRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnRegistrationOfVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    loadBatchStockModel2N(cbxSloc2N, cbxBatchStock2N, false);
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

    if (modeDetail == MODE_DETAIL.OUT_SELL_ROAD || modeDetail == MODE_DETAIL.OUT_SELL_WATERWAY) {
        Customer customer = (Customer) cbxCustomerN.getSelectedItem();
        cbxShipToN.setModel(weightTicketRegistarationController.getShipToModel(customer.getKunnr()));
        cbxShipToN.setSelectedIndex(-1);
    }

    validateForm();
}//GEN-LAST:event_cbxCustomerNActionPerformed

private void txtTicketIdNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTicketIdNKeyReleased
    validateForm();
}//GEN-LAST:event_txtTicketIdNKeyReleased

private void txtWeightNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWeightNKeyReleased
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
    loadBatchStockModel2N(cbxSloc2N, cbxBatchStock2N, false);
}//GEN-LAST:event_cbxMaterialTypeNActionPerformed

private void cbxVendorLoadingNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVendorLoadingNActionPerformed
    if (cbxVendorLoadingN.getSelectedItem() != null && !cbxVendorLoadingN.getSelectedItem().toString().equals("")) {
        Vendor vendor = (Vendor) cbxVendorLoadingN.getSelectedItem();
        if (newWeightTicket != null && vendor != null) {
            isValidVendorLoad = true;
            lblVendorLoadingN.setForeground(Color.black);
            newWeightTicket.getWeightTicketDetail().setLoadVendor(vendor.getLifnr());
        }

        validateForm();
    } else {
        isValidVendorLoad = true;
    }
}//GEN-LAST:event_cbxVendorLoadingNActionPerformed

private void cbxVendorTransportNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxVendorTransportNActionPerformed
    if (cbxVendorTransportN.getSelectedItem() != null && !cbxVendorTransportN.getSelectedItem().toString().equals("")) {
        Vendor vendor = (Vendor) cbxVendorTransportN.getSelectedItem();
        if (newWeightTicket != null && vendor != null) {
            isValidVendorTransport = true;
            lblVendorTransportN.setForeground(Color.black);
            newWeightTicket.getWeightTicketDetail().setTransVendor(vendor.getLifnr());
        }

        checkPlateWithVendor();

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

    if (!checkPlateWithVendor()) {
        return;
    }

    txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

    validateForm();
}//GEN-LAST:event_txtPlateNoNFocusLost

    private boolean checkPlateWithVendor() throws HeadlessException {
        String plateNo = txtPlateNoN.getText().trim();
        isValidPlateNo = true;
        btnSave.setEnabled(false);
        if (modeDetail == MODE_DETAIL.OUT_PLANT_PLANT || modeDetail == MODE_DETAIL.OUT_SLOC_SLOC || modeDetail == MODE_DETAIL.OUT_PULL_STATION) {
            Vendor transportVendor = (Vendor) cbxVendorTransportN.getSelectedItem();

            boolean isPlateNoValid;
            if (modeDetail == MODE_DETAIL.OUT_SLOC_SLOC) {
                isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);
            } else {
                isPlateNoValid = wtRegisValidation.validatePlateNoWithDB(plateNo, lblPlateNoN);
            }

            if (!isPlateNoValid) {
                isValidPlateNo = false;
            } else {
                txtTonnageN.setText(weightTicketRegistarationController.loadVehicleLoading(plateNo).toString());

                if (transportVendor != null && !weightTicketRegistarationController.checkPlateNoInVendor(transportVendor.getLifnr(), plateNo)) {
                    lblPlateNoN.setForeground(Color.red);
                    btnSave.setEnabled(false);
                    JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.validateBSxe"));
                    isValidPlateNo = false;
                }
            }
        }

        return isValidPlateNo;
    }

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

private void txtSalanNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSalanNKeyReleased
    validateForm();
}//GEN-LAST:event_txtSalanNKeyReleased

private void txtSalanNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSalanNFocusLost
    String soSalan = txtSalanN.getText().trim();
    soSalan = soSalan.replace("-", "");
    soSalan = soSalan.replace(".", "");
    txtSalanN.setText(soSalan.toUpperCase());

    validateForm();
}//GEN-LAST:event_txtSalanNFocusLost

private void txtLoadSourceNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLoadSourceNKeyReleased
    validateForm();
}//GEN-LAST:event_txtLoadSourceNKeyReleased

private void txtDONumNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDONumNFocusLost
    String doNum = txtDONumN.getText().trim();
    if (!doNum.isEmpty()) {
        txtDONumN.setText(StringUtil.paddingZero(doNum, 10));
        validateForm();
    }
}//GEN-LAST:event_txtDONumNFocusLost

private void txtSONumNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSONumNFocusLost
    String soNum = txtSONumN.getText().trim();
    if (!soNum.isEmpty()) {
        txtSONumN.setText(StringUtil.paddingZero(soNum, 10));
        validateForm();
    }
}//GEN-LAST:event_txtSONumNFocusLost

private void txtPONumNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPONumNFocusLost
    String poNum = txtPONumN.getText().trim();
    if (!poNum.isEmpty()) {
        txtPONumN.setText(StringUtil.paddingZero(poNum, 10));
        validateForm();
    }
}//GEN-LAST:event_txtPONumNFocusLost

private void txtPOSTONumNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPOSTONumNFocusLost
    String postoNum = txtPOSTONumN.getText().trim();
    if (!postoNum.isEmpty()) {
        txtPOSTONumN.setText(StringUtil.paddingZero(postoNum, 10));
        validateForm();
    }
}//GEN-LAST:event_txtPOSTONumNFocusLost

private void txtWeightNFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWeightNFocusLost
    String weight = txtWeightN.getText().trim();
    double minVal = 0d;
    if (modeDetail != MODE_DETAIL.IN_OTHER && modeDetail != MODE_DETAIL.OUT_OTHER) {
        minVal = 0.001d;
    }

    if (!wtRegisValidation.validateWeighValue(weight, minVal)) {
        JOptionPane.showMessageDialog(rootPane, resourceMapMsg.getString("msg.lowerMinWeight", minVal));
    }
}//GEN-LAST:event_txtWeightNFocusLost

private void cbxShipToNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxShipToNActionPerformed
    validateForm();
}//GEN-LAST:event_cbxShipToNActionPerformed

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

    @Action(enabledProperty = "creatable")
    public void newRecord() {
        setFormEditable(true);
        setRbtEnabled(true);
        setCreatable(false);
        setClearable(true);
        txtWeightTicketNo.setText("");
        btnSave.setEnabled(false);

        rbtInput.setEnabled(true);
        rbtOutput.setEnabled(true);
        cbxModeType.setEnabled(true);
        loadModeTypeModel(mode);
    }

    @Action(enabledProperty = "clearable")
    public void clearForm() {
        loadModeTypeModel(mode);
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
    }

    public void loadModeTypeModel(MODE mode) {
        if (mode == MODE.INPUT) {
            rbtInput.setSelected(true);
        } else {
            rbtOutput.setSelected(true);
        }

        if (this.modeDetail == null || this.mode != mode) {
            DefaultComboBoxModel model = weightTicketRegistarationController.getModeTypeModel(mode);
            if (model.getSize() != 0) {
                cbxModeType.setModel(weightTicketRegistarationController.getModeTypeModel(mode));
                this.mode = mode;
                this.modeDetail = ((WeighingMode) cbxModeType.getSelectedItem()).getModeDetail();
            } else {
                this.modeDetail = null;
                btnNew.setEnabled(false);
            }
        }

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
        txtSalanN.setEnabled(false);
        lblSalanN.setForeground(Color.black);
        txtSlingN.setEnabled(false);
        lblSlingN.setForeground(Color.black);
        txtPalletN.setEnabled(false);
        lblPalletN.setForeground(Color.black);
        txtSoNiemXaN.setEnabled(false);
        lblSoNiemXaN.setForeground(Color.black);
        txtProductionBatchN.setEnabled(false);
        lblProductionBatchN.setForeground(Color.black);
        txtLoadSourceN.setEnabled(false);
        lblLoadSourceN.setForeground(Color.black);
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
        cbxShipToN.setEnabled(false);
        lblShipToN.setForeground(Color.black);
    }

    private void prepareEditableForm(MODE_DETAIL modeDetail) {
        cleanData();

        if (modeDetail == MODE_DETAIL.IN_OTHER || modeDetail == MODE_DETAIL.OUT_OTHER) {
            cbxMaterialTypeN.setModel(materialInternalModel);
            cbxMaterialTypeN.setSelectedIndex(-1);
        } else {
            cbxMaterialTypeN.setModel(materialModel);
            cbxMaterialTypeN.setSelectedIndex(-1);
        }

        if (modeDetail == MODE_DETAIL.IN_PO_PURCHASE) {
            cbxCustomerN.setModel(vendorCustomerModel);
            cbxCustomerN.setSelectedIndex(-1);
        } else {
            cbxCustomerN.setModel(customerModel);
            cbxCustomerN.setSelectedIndex(-1);
        }

        lblPONumN.setText(resourceMapMsg.getString("lblPONumN.text"));
        lblPOSTONumN.setText(resourceMapMsg.getString("lblPOSTONumN.text"));
        lblSlocN.setText(resourceMapMsg.getString("lblSlocN.text"));
        lblBatchStockN.setText(resourceMapMsg.getString("lblBatchStockN.text"));
        lblPlateNoN.setText(resourceMapMsg.getString("lblPlateNoN.text"));

        if (modeDetail == null) {
            return;
        }

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
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
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
        showComponent(cbxShipToN, lblShipToN, false, false);
    }

    private void prepareInWarehouseTransfer() {
        lblPlateNoN.setText(resourceMapMsg.getString("lblVehicleNo"));
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, true, true);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, true);
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
        showComponent(cbxShipToN, lblShipToN, false, false);
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
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
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
        showComponent(cbxShipToN, lblShipToN, false, false);

        txtWeightN.setText("0.000");
        txtWeightN.setValue(0.000d);
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
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, true);
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
        showComponent(cbxShipToN, lblShipToN, true, true);
    }

    private void prepareOutPlantPlant() {
        lblPlateNoN.setText(resourceMapMsg.getString("lblVehicleNo"));
        showComponent(txtTicketIdN, lblTicketIdN, true, true);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, true, true);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, false, false);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, true);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxCustomerN, lblCustomerN, true, true);
        showComponent(cbxShipToN, lblShipToN, false, false);

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, true);
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
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, false, false);
        showComponent(txtPalletN, lblPalletN, false, false);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
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
        showComponent(cbxCustomerN, lblCustomerN, true, true);
        showComponent(cbxShipToN, lblShipToN, false, false);

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, true);
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
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, false, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, false, false);
        showComponent(txtPONumN, lblPONumN, btnPOCheckN, true, true);
        showComponent(txtPOSTONumN, lblPOSTONumN, btnPOSTOCheckN, true, true);
        showComponent(cbxMaterialTypeN, lblMaterialTypeN, true, true);
        showComponent(txtWeightN, lblWeightN, lblWeightUnitN, true, true);
        showComponent(cbxSlocN, lblSlocN, true, true);
        showComponent(cbxSloc2N, lblSloc2N, false, false);
        showComponent(cbxBatchStockN, lblBatchStockN, true, true);
        showComponent(cbxBatchStock2N, lblBatchStock2N, false, false);
        showComponent(cbxCustomerN, lblCustomerN, true, true);
        showComponent(cbxShipToN, lblShipToN, false, false);

        boolean isShowPOV = WeighBridgeApp.getApplication().getSapSetting().getCheckPov();
        showComponent(cbxVendorLoadingN, lblVendorLoadingN, isShowPOV, true);
        showComponent(cbxVendorTransportN, lblVendorTransportN, isShowPOV, true);
    }

    private void prepareOutSellWateway() {
        lblPlateNoN.setText(resourceMapMsg.getString("lblPlateNoWater"));

        showComponent(txtTicketIdN, lblTicketIdN, false, false);
        showComponent(txtWeightTickerRefN, lblWeightTickerRefN, false, false);
        showComponent(txtRegisterIdN, lblRegisterIdN, true, true);
        showComponent(txtDriverNameN, lblDriverNameN, true, true);
        showComponent(txtCMNDN, lblCMNDN, true, true);
        showComponent(txtPlateNoN, lblPlateNoN, true, true);
        showComponent(txtTonnageN, lblTonnageN, lblTonngageUnitN, true, false);
        showComponent(txtTrailerNoN, lblTrailerNoN, true, true);
        showComponent(txtSalanN, lblSalanN, true, true);
        showComponent(txtSlingN, lblSlingN, true, true);
        showComponent(txtPalletN, lblPalletN, true, true);
        showComponent(txtSoNiemXaN, lblSoNiemXaN, true, true);
        showComponent(txtProductionBatchN, lblProductionBatchN, true, true);
        showComponent(txtLoadSourceN, lblLoadSourceN, false, false);
        showComponent(txtNoteN, lblNoteN, true, true);
        showComponent(txtDONumN, lblDONumN, btnDOCheckN, true, false);
        showComponent(txtSONumN, lblSONumN, btnSOCheckN, true, true);
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
        showComponent(cbxShipToN, lblShipToN, true, true);
    }

    private void validateForm() {
        boolean isValid = false;
        if (modeDetail == null) {
            return;
        }

        switch (modeDetail) {
            case IN_PO_PURCHASE:
                isValid = validateInPoPurchase();
                break;
            case IN_WAREHOUSE_TRANSFER:
                isValid = validateInWarehouseTransfer();
                break;
            case IN_OTHER:
                isValid = validateInOutOther();
                break;
            case OUT_SELL_ROAD:
                isValid = validateOutSellRoad();
                break;
            case OUT_PLANT_PLANT:
                isValid = validateOutPlantPlant() && isValidVendorLoad && isValidVendorTransport && isValidPlateNo;
                break;
            case OUT_SLOC_SLOC:
                isValid = validateOutSlocSloc() && isValidVendorTransport && isValidPlateNo && isValidSloc;
                break;
            case OUT_PULL_STATION:
                isValid = validateOutPullStation() && isValidVendorLoad && isValidVendorTransport && isValidPlateNo;
                break;
            case OUT_SELL_WATERWAY:
                isValid = validateOutSellWateway();
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

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid && isWeightValid
                && isCMNDBLValid && isPlateNoValid && isSalanValid && isCustomerValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isWeightValid
                && isNoteValid && isSlocValid && isPOValid && isMaterialTypeValid;
    }

    private boolean validateInWarehouseTransfer() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validateVehicle(plateNo, lblPlateNoN);

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);
        boolean isLoadSourceValid = wtRegisValidation.validateLength(txtLoadSourceN.getText(), lblLoadSourceN, 0, 128);

        boolean isDOValid = wtRegisValidation.validateSingleSODO(txtDONumN.getText(), lblDONumN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid && isDOValid
                && isCMNDBLValid && isPlateNoValid && isSalanValid && isMaterialTypeValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isWeightValid
                && isNoteValid && isSlocValid && isLoadSourceValid && isCustomerValid;
    }

    private boolean validateInOutOther() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0d);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        boolean isSlingValid = wtRegisValidation.validateIntegerValue(txtSlingN.getText(), lblSlingN);
        boolean isPalletValid = wtRegisValidation.validateIntegerValue(txtPalletN.getText(), lblPalletN);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isPlateNoValid && isSalanValid && isSlingValid && isPalletValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isMaterialTypeValid && isSlocValid && isWeightValid;
    }

    private boolean validateOutSellRoad() {
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNo(plateNo, lblPlateNoN);

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isDOValid = wtRegisValidation.validateSingleSODO(txtDONumN.getText(), lblDONumN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        boolean isSlingValid = wtRegisValidation.validateIntegerValue(txtSlingN.getText(), lblSlingN);
        boolean isPalletValid = wtRegisValidation.validateIntegerValue(txtPalletN.getText(), lblPalletN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isShipToValid = wtRegisValidation.validateCbxSelected(cbxShipToN.getSelectedIndex(), lblShipToN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isRegisterIdValid && isDriverNameValid && isCMNDBLValid && isPlateNoValid && isDOValid && isWeightValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isMaterialTypeValid && isShipToValid
                && isNoteValid && isSlocValid && isSalanValid && isSlingValid && isPalletValid && isCustomerValid;
    }

    private boolean validateOutPlantPlant() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        if (plateNo.isEmpty()) {
            lblPlateNoN.setForeground(Color.red);
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);
        boolean isLoadSourceValid = wtRegisValidation.validateLength(txtLoadSourceN.getText(), lblLoadSourceN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isVendorTransValid = wtRegisValidation.validateCbxSelected(cbxVendorTransportN.getSelectedIndex(), lblVendorTransportN);
        if (!isValidVendorTransport) {
            lblVendorTransportN.setForeground(Color.red);
        }

        boolean isSlingValid = wtRegisValidation.validateIntegerValue(txtSlingN.getText(), lblSlingN);
        boolean isPalletValid = wtRegisValidation.validateIntegerValue(txtPalletN.getText(), lblPalletN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid && isPOValid && isCustomerValid
                && isCMNDBLValid && isSalanValid && isLoadSourceValid && isSlingValid && isPalletValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isWeightValid
                && isNoteValid && isSlocValid && isVendorTransValid && isMaterialTypeValid;
    }

    private boolean validateOutSlocSloc() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        if (plateNo.isEmpty()) {
            lblPlateNoN.setForeground(Color.red);
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        boolean isPOSTOValid = wtRegisValidation.validatePO(txtPOSTONumN.getText(), lblPOSTONumN);
        if (txtPOSTONumN.getText().trim().isEmpty()) {
            lblPOSTONumN.setForeground(Color.black);
            isPOSTOValid = true;
        }

        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN) && isValidSloc;
        boolean isSloc2Valid = wtRegisValidation.validateCbxSelected(cbxSloc2N.getSelectedIndex(), lblSloc2N) && isValidSloc;
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid
                && isCMNDBLValid && isSalanValid && isPOValid && isPOSTOValid && isWeightValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid
                && isNoteValid && isSlocValid && isSloc2Valid && isMaterialTypeValid;
    }

    private boolean validateOutPullStation() {
        boolean isTicketIdValid = wtRegisValidation.validateLength(txtTicketIdN.getText(), lblTicketIdN, 0, 20);
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        if (plateNo.isEmpty()) {
            lblPlateNoN.setForeground(Color.red);
        }

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isPOValid = wtRegisValidation.validatePO(txtPONumN.getText(), lblPONumN);
        boolean isPOSTOValid = wtRegisValidation.validatePO(txtPOSTONumN.getText(), lblPOSTONumN);

        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);
        boolean isVendorTransValid = wtRegisValidation.validateCbxSelected(cbxVendorTransportN.getSelectedIndex(), lblVendorTransportN);
        if (!isValidVendorTransport) {
            lblVendorTransportN.setForeground(Color.red);
        }

        boolean isSlingValid = wtRegisValidation.validateIntegerValue(txtSlingN.getText(), lblSlingN);
        boolean isPalletValid = wtRegisValidation.validateIntegerValue(txtPalletN.getText(), lblPalletN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isTicketIdValid && isRegisterIdValid && isDriverNameValid && isPOValid && isPOSTOValid
                && isCMNDBLValid && isSalanValid && isSlingValid && isPalletValid && isCustomerValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isWeightValid
                && isNoteValid && isSlocValid && isVendorTransValid && isMaterialTypeValid;
    }

    private boolean validateOutSellWateway() {
        boolean isRegisterIdValid = wtRegisValidation.validateLength(txtRegisterIdN.getText(), lblRegisterIdN, 0, 50);
        boolean isDriverNameValid = wtRegisValidation.validateLength(txtDriverNameN.getText(), lblDriverNameN, 1, 70);
        boolean isCMNDBLValid = wtRegisValidation.validateLength(txtCMNDN.getText(), lblCMNDN, 1, 25);

        String plateNo = txtPlateNoN.getText().trim();
        boolean isPlateNoValid = wtRegisValidation.validatePlateNoWater(plateNo, lblPlateNoN);

        boolean isTrailerNoValid = wtRegisValidation.validateLength(txtTrailerNoN.getText(), lblTrailerNoN, 0, 10);
        String salan = txtSalanN.getText().trim();
        boolean isSalanValid = salan.isEmpty() || wtRegisValidation.validatePlateNoWater(salan, lblSalanN);
        boolean isSoNiemXaValid = wtRegisValidation.validateLength(txtSoNiemXaN.getText(), lblSoNiemXaN, 0, 60);
        boolean isProductionBatchValid = wtRegisValidation.validateLength(txtProductionBatchN.getText(), lblProductionBatchN, 0, 128);
        boolean isNoteValid = wtRegisValidation.validateLength(txtNoteN.getText(), lblNoteN, 0, 128);

        boolean isSOValid = wtRegisValidation.validateSingleSODO(txtSONumN.getText(), lblSONumN);
        boolean isSlocValid = wtRegisValidation.validateCbxSelected(cbxSlocN.getSelectedIndex(), lblSlocN);

        boolean isSlingValid = wtRegisValidation.validateIntegerValue(txtSlingN.getText(), lblSlingN);
        boolean isPalletValid = wtRegisValidation.validateIntegerValue(txtPalletN.getText(), lblPalletN);
        boolean isMaterialTypeValid = wtRegisValidation.validateCbxSelected(cbxMaterialTypeN.getSelectedIndex(), lblMaterialTypeN);
        boolean isCustomerValid = wtRegisValidation.validateCbxSelected(cbxCustomerN.getSelectedIndex(), lblCustomerN);
        boolean isShipToValid = wtRegisValidation.validateCbxSelected(cbxShipToN.getSelectedIndex(), lblShipToN);
        boolean isWeightValid = wtRegisValidation.validateWeighField(txtWeightN.getText(), lblWeightN, 1, 10, 0.001d);

        return isRegisterIdValid && isDriverNameValid && isSOValid && isMaterialTypeValid
                && isCMNDBLValid && isPlateNoValid && isSalanValid && isCustomerValid
                && isTrailerNoValid && isSoNiemXaValid && isProductionBatchValid && isShipToValid
                && isNoteValid && isSlocValid && isSlingValid && isPalletValid && isWeightValid;
    }

    private void loadBatchStockModel(JComboBox slocComponent, JComboBox batchStockComponent, boolean isSloc) {
        batchStockComponent.setModel(new DefaultComboBoxModel());
        if (slocComponent.getSelectedIndex() == -1) {
            return;
        }

        //lblSloc2N.setBackground(Color.black);
        SLoc sloc = (SLoc) slocComponent.getSelectedItem();
        if (modeDetail == MODE_DETAIL.OUT_SLOC_SLOC
                && cbxSloc2N.getSelectedIndex() != -1) {
            SLoc sloc2N = (SLoc) cbxSloc2N.getSelectedItem();
            if (sloc.getLgort().equals(sloc2N.getLgort())) {
                JOptionPane.showMessageDialog(rootPane,
                        resourceMapMsg.getString("msg.slocDuplicate"),
                        "Warning", JOptionPane.WARNING_MESSAGE);
                isValidSloc = false;
                cbxBatchStockN.setSelectedIndex(-1);
                lblSlocN.setForeground(Color.red);
                btnSave.setEnabled(false);
                return;
            }
        }
        isValidSloc = true;
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

    private void loadBatchStockModel2N(JComboBox slocComponent, JComboBox batchStockComponent, boolean isSloc) {
        if (slocComponent.getSelectedIndex() == -1) {
            batchStockComponent.setModel(new DefaultComboBoxModel());
            return;
        }

        isValidSloc = true;
        SLoc sloc = (SLoc) slocComponent.getSelectedItem();
        if (modeDetail == MODE_DETAIL.OUT_SLOC_SLOC
                && cbxSlocN.getSelectedIndex() != -1) {
            SLoc sloc2N = (SLoc) cbxSlocN.getSelectedItem();
            if (sloc.getLgort().equals(sloc2N.getLgort())) {
                JOptionPane.showMessageDialog(rootPane,
                        resourceMapMsg.getString("msg.slocDuplicate"),
                        "Warning", JOptionPane.WARNING_MESSAGE);
                isValidSloc = false;
                cbxBatchStock2N.setSelectedIndex(-1);
                lblSloc2N.setForeground(Color.red);
                btnSave.setEnabled(false);
                return;
            }
        }

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

                List<String> regItemDescriptions = new ArrayList<>();
                for (WeightTicketDetail weightTicketDetail : weightTicketDetails) {
                    String description = weightTicketDetail.getRegItemDescription();
                    if (description != null && !description.isEmpty()) {
                        regItemDescriptions.add(description);
                    }
                }

                wtData[i][6] = regItemDescriptions.size() > 0 ? String.join(" - ", regItemDescriptions) : "";

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
        protected Object doInBackground() throws Exception {
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

            return null;
        }

        @Override
        protected void succeeded(Object result) {
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
            if (!ExceptionUtil.isDatabaseDisconnectedException(cause)) {
                JOptionPane.showMessageDialog(rootPane, cause.getMessage());
            }
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

    private class SaveWeightTicketTask extends org.jdesktop.application.Task<Object, Void> {

        SaveWeightTicketTask(org.jdesktop.application.Application app) {
            super(app);
            setSaveNeeded(false);
            setClearable(false);
        }

        /*
         *  WYYMMXXXXX: Wplant(1) + YY + MM + XXXXX
         */
        private String getAutoGeneratedId(int seqByMonth, Date now) {
            String wPlantMap = StringUtil.isNotEmptyString(configuration.getWplantMap()) ? configuration.getWplantMap().substring(0, 1) : "0";
            SimpleDateFormat formatter = new SimpleDateFormat();
            formatter.applyPattern("yyMM");

            return wPlantMap
                    + formatter.format(now)
                    + StringUtil.paddingZero(String.valueOf(seqByMonth), 5);
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

            newWeightTicket.setId(getAutoGeneratedId(seqBMonth, now));
            newWeightTicket.setMandt(configuration.getSapClient());
            newWeightTicket.setWplant(configuration.getWkPlant());
            newWeightTicket.setSeqDay(seqBDay);
            newWeightTicket.setSeqMonth(seqBMonth);
            newWeightTicket.setCreatedTime(createdTime);
            newWeightTicket.setCreatedDate(now);
            newWeightTicket.setCreatedDatetime(now);

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
            newWeightTicket.setRecvPlant(configuration.getWkPlant());
            newWeightTicket.setSoNiemXa(txtSoNiemXaN.getText().trim());
            newWeightTicket.setBatch(txtProductionBatchN.getText().trim());
            newWeightTicket.setNote(txtNoteN.getText().trim());
            newWeightTicket.setChargEnh(txtSalanN.getText().trim());

            switch (modeDetail) {
                case IN_PO_PURCHASE:
                    updateDataForInPoPurchaseMode();
                    break;
                case IN_WAREHOUSE_TRANSFER:
                    updateDataForInWarehourseTransfer();
                    break;
                case IN_OTHER:
                    updateDataForOtherMode();
                    break;
                case OUT_SELL_ROAD:
                    updateDataForOutSellRoad();
                    break;
                case OUT_PLANT_PLANT:
                    updateDataForOutPlantPlant();
                    break;
                case OUT_SLOC_SLOC:
                    updateDataForOutSlocSloc();
                    break;
                case OUT_PULL_STATION:
                    updateDataForPrepareOutPullStation();
                    break;
                case OUT_SELL_WATERWAY:
                    updateDataForOutSellWateway();
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
                    weightTicketDetail.setCreatedDatetime(now);
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

            return null;
        }

        public void updateDataForInPoPurchaseMode() {
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setEbeln(txtPONumN.getText().trim());

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));

            Vendor customer = (Vendor) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getLifnr());
            }
        }

        public void updateDataForInWarehourseTransfer() {
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());
            newWeightTicket.setLoadSource(txtLoadSourceN.getText().trim());
            newWeightTicket.setWeightTicketIdRef(txtWeightTickerRefN.getText().trim());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setDeliveryOrderNo(txtDONumN.getText().trim());

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }
        }

        public void updateDataForOtherMode() {
            Number sling = (Number) txtSlingN.getValue();
            newWeightTicket.setSling(sling.intValue());
            Number pallet = (Number) txtPalletN.getValue();
            newWeightTicket.setPallet(pallet.intValue());
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());

            MaterialInternal material = (MaterialInternal) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            String val = txtWeightN.getText().trim().replace(",", "");
            weightTicketDetail.setRegItemQuantity(new BigDecimal(val));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }
        }

        public void updateDataForOutSellRoad() {
            Number sling = (Number) txtSlingN.getValue();
            newWeightTicket.setSling(sling.intValue());
            Number pallet = (Number) txtPalletN.getValue();
            newWeightTicket.setPallet(pallet.intValue());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setDeliveryOrderNo(txtDONumN.getText().trim());

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }

            Customer shipTo = (Customer) cbxShipToN.getSelectedItem();
            if (shipTo != null) {
                weightTicketDetail.setShipTo(shipTo.getKunnr());
            }
        }

        public void updateDataForOutPlantPlant() {
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());
            newWeightTicket.setLoadSource(txtLoadSourceN.getText().trim());
            Number sling = (Number) txtSlingN.getValue();
            newWeightTicket.setSling(sling.intValue());
            Number pallet = (Number) txtPalletN.getValue();
            newWeightTicket.setPallet(pallet.intValue());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setEbeln(txtPONumN.getText().trim());

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }
        }

        public void updateDataForOutSlocSloc() {
            newWeightTicket.setMoveType("311");
            newWeightTicket.setMoveReas(null);
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            Material material = (Material) cbxMaterialTypeN.getSelectedItem();

            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));
            weightTicketDetail.setEbeln(txtPONumN.getText().trim());
            newWeightTicket.setPosto(txtPOSTONumN.getText().trim());

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }
        }

        public void updateDataForPrepareOutPullStation() {
            newWeightTicket.setMoveType("101");
            newWeightTicket.setTicketId(txtTicketIdN.getText().trim());
            Number sling = (Number) txtSlingN.getValue();
            newWeightTicket.setSling(sling.intValue());
            Number pallet = (Number) txtPalletN.getValue();
            newWeightTicket.setPallet(pallet.intValue());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setEbeln(txtPONumN.getText().trim());
            newWeightTicket.setPosto(txtPOSTONumN.getText().trim());

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }
        }

        public void updateDataForOutSellWateway() {
            Number sling = (Number) txtSlingN.getValue();
            newWeightTicket.setSling(sling.intValue());
            Number pallet = (Number) txtPalletN.getValue();
            newWeightTicket.setPallet(pallet.intValue());

            WeightTicketDetail weightTicketDetail = newWeightTicket.getWeightTicketDetail();

            Material material = (Material) cbxMaterialTypeN.getSelectedItem();
            weightTicketDetail.setMatnrRef(material.getMatnr());
            weightTicketDetail.setRegItemDescription(material.getMaktx());
            weightTicketDetail.setRegItemQuantity(new BigDecimal(txtWeightN.getText().trim().replace(",", "")));
            weightTicketDetail.setUnit(weightTicketRegistarationController.getUnit().getWeightTicketUnit());
            weightTicketDetail.setSoNumber(txtSONumN.getText().trim());

            Customer customer = (Customer) cbxCustomerN.getSelectedItem();
            if (customer != null) {
                weightTicketDetail.setKunnr(customer.getKunnr());
            }

            Customer shipTo = (Customer) cbxShipToN.getSelectedItem();
            if (shipTo != null) {
                weightTicketDetail.setShipTo(shipTo.getKunnr());
            }
        }

        @Override
        protected void succeeded(Object t) {
            btnFind.doClick();
        }

        @Override
        protected void failed(Throwable cause) {
            logger.error(cause);
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
        isValidVendorLoad = false;
        isValidVendorTransport = false;
        isValidPlateNo = false;

        txtTicketIdN.setText("");
        txtWeightTickerRefN.setText("");
        txtRegisterIdN.setText("");
        txtDriverNameN.setText("");
        txtCMNDN.setText("");
        txtPlateNoN.setText("");
        txtTonnageN.setText("0");
        txtTrailerNoN.setText("");
        txtSalanN.setText("");
        txtSlingN.setText("0");
        txtSlingN.setValue(0);
        txtPalletN.setText("0");
        txtPalletN.setValue(0);
        txtSoNiemXaN.setText("");
        txtProductionBatchN.setText("");
        txtLoadSourceN.setText("");
        txtNoteN.setText("");
        txtDONumN.setText("");
        txtPONumN.setText("");
        txtPOSTONumN.setText("");
        txtSONumN.setText("");
        cbxMaterialTypeN.setSelectedIndex(-1);
        txtWeightN.setText("0.001");
        txtWeightN.setValue(0.001d);
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
        cbxShipToN.setModel(new DefaultComboBoxModel());
        cbxShipToN.setSelectedIndex(-1);

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
    private javax.swing.JComboBox cbxShipToN;
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
    private javax.swing.JLabel lblLoadSourceN;
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
    private javax.swing.JLabel lblSalanN;
    private javax.swing.JLabel lblShipToN;
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
    private javax.swing.JTextField txtLoadSourceN;
    private javax.swing.JTextField txtNoteN;
    private javax.swing.JTextField txtPONumN;
    private javax.swing.JTextField txtPOSTONumN;
    private javax.swing.JFormattedTextField txtPalletN;
    private javax.swing.JTextField txtPlateNo;
    private javax.swing.JTextField txtPlateNoN;
    private javax.swing.JTextField txtProductionBatchN;
    private javax.swing.JTextField txtRegisterIdN;
    private javax.swing.JTextField txtSONumN;
    private javax.swing.JTextField txtSalanN;
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
}
