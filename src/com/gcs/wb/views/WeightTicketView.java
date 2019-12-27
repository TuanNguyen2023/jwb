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
 import com.gcs.wb.utils.Base64_Utils; 
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.SAPErrorTransform;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtCodeStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtHeaderStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemDoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtItemPoStructure;
import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.helper.MatAvailableBapi;
import com.gcs.wb.bapi.helper.MvtGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.BatchStocksStructure;
import com.gcs.wb.bapi.helper.MvtReasonsGetListBapi;
import com.gcs.wb.bapi.helper.structure.MvtReasonsGetListStructure;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.bapi.helper.structure.MatAvailableStructure;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.DORevertBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.outbdlv.structure.OutbDeliveryCreateStoStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import com.gcs.wb.jpa.JpaProperties;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.OutbDetailsV2;
import com.gcs.wb.jpa.entity.BatchStocks;
import com.gcs.wb.jpa.entity.BatchStocksPK;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import com.gcs.wb.jpa.entity.Material;
import com.gcs.wb.jpa.entity.MaterialPK;
import com.gcs.wb.jpa.entity.Movement;
import com.gcs.wb.jpa.entity.MovementPK;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.OutbDelPK;
import com.gcs.wb.jpa.entity.PurOrder;
import com.gcs.wb.jpa.entity.PurOrderPK;
import com.gcs.wb.jpa.entity.Reason;
import com.gcs.wb.jpa.entity.ReasonPK;
import com.gcs.wb.jpa.entity.SAPSetting;
import com.gcs.wb.jpa.entity.SLoc;
import com.gcs.wb.jpa.entity.SLocPK;
import com.gcs.wb.jpa.entity.TimeRange;
import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.jpa.entity.Vehicle;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.VendorPK;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.utils.RegexFormatter;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.JCoException;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.log4j.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.hibersap.HibersapException;
import org.hibersap.SapException;
import org.hibersap.session.Session;
import org.hibersap.util.DateUtil;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import javax.persistence.EntityManager;
import com.gcs.wb.jpa.entity.Variant;
import com.gcs.wb.jpa.entity.VariantPK;
import com.gcs.wb.utils.Conversion_Exit;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.math.BigInteger;
// import java.util.Locale;
import javax.persistence.Query;
import java.sql.Timestamp;
// import java.util.Set;
import java.awt.datatransfer.StringSelection; 
import java.util.*; 

/*
 *
 * @author Tran-Vu
 */
public class WeightTicketView extends javax.swing.JInternalFrame {

    private AppConfig config = null;
    private final SAPSetting sapSetting;
    private final User login;
    private String last_value = null;
    private String current_value = null;
    public HashMap hmMsg = new HashMap();
   
    public WeightTicketView() {
        initComponents();
        cbxMaterial.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Material) {
                    Material material = (Material)value;
                    setText(material.getMaktx());
                    //                    setText(sloc.getLgobe().concat(" - ").concat(sloc.getSLocPK().getLgort()));
                }
                return this;
            }
        });
        
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
        rbtMvt311.setVisible(false);
        rbtMb1b.setVisible(false);


        //>> Tuanna modified 02.02.2013  for Set manual input qty in wheighing   
        boolean flag = true ; // tram can , true -- giao nhan  file   
        boolean flagAdmin = true ;  // false normal , true -> SCM admin 
        // replace by 
        txfCurScale.setEditable(flag);
        txtInTime.setEditable(flagAdmin);
       txtOutTime.setEditable(flagAdmin);

  
        sapSetting = WeighBridgeApp.getApplication().getSapSetting();
        login = WeighBridgeApp.getApplication().getLogin();
//        WeighBridgeView.txt_status.setText(sapSetting.getName1() + " - User:" + login.getFullName());
        
        entityManager.clear();
        chkDissolved.setEnabled(false);
        chkDissolved.setVisible(false);
        lblReason.setVisible(false);
        cbxReason.setVisible(false);
                
        
        rbtMb1b.setVisible(false);
        rbtMvt311.setVisible(false);
        lblComplete.setVisible(false);
        cbxCompleted.setVisible(false);
//        if (WeighBridgeApp.getApplication().isOfflineMode() || rbtMisc.isSelected()) {
        rbtMisc.setForeground(Color.red);
        WeightTicketJpaController conWTicket = new WeightTicketJpaController(entityManager);
        Query q = (Query) entityManager.createNativeQuery("select * from Customer where MANDT = ?"); 
        q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getsClient());
        List kunnr = q.getResultList();
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        result.addElement("");
        for (Object obj : kunnr) {
            Object[] wt = (Object[]) obj;
            CustomerPK custPK = new CustomerPK();
            Customer cust = new Customer();
            custPK.setMandt(WeighBridgeApp.getApplication().getConfig().getsClient());
            custPK.setKunnr(wt[1].toString());
            cust.setCustomerPK(custPK);
            cust.setName1(wt[2].toString());
            cust.setName2(wt[3].toString());
            if (result.getIndexOf(cust) < 0) {
                result.addElement(cust);
            }
        }
        //tuanna modi 140415
        
        try 
        {
         q = (Query) entityManager.createNativeQuery("call pGetDev2 (? )" ); 
         q.setParameter(1, WeighBridgeApp.getApplication().getConfig().getWbId());
         List sdev  = q.getResultList();
         
         for ( Object obj : sdev )
         {
             Object[] wt = (Object[]) obj; 
             //hld18
             txfCurScale.setEditable((( Integer.parseInt(Base64_Utils.decodeNTimes (  wt[0].toString())) ==1 )? true:false));
             txtInTime.setEditable((( Integer.parseInt( Base64_Utils.decodeNTimes(wt[1].toString())) ==1 )? true:false));
             txtOutTime.setEditable((( Integer.parseInt(  Base64_Utils.decodeNTimes (wt[2].toString())) ==1 )? true:false));        
             break; 
             
         }
        }catch
                (Exception e) {}
        
         // txtInTime.setEditable(flagAdmin);
        // txtOutTime.setEditable(flagAdmin);
        
        
        
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

        TypedQuery tq = entityManager.createNamedQuery("TimeRange.findByMandtWBID", TimeRange.class);
        tq.setParameter("mandt", WeighBridgeApp.getApplication().getConfig().getsClient());
        tq.setParameter("wbId", WeighBridgeApp.getApplication().getConfig().getWbId());
        TimeRange t = null;
        try {
            t = (TimeRange) tq.getSingleResult();
            timeFrom = 0 + Integer.parseInt(t.getTimeFrom() != null ? (t.getTimeFrom().trim()) : "0");
            timeTo = Integer.parseInt(t.getTimeTo() != null ? (t.getTimeTo().trim()) : "0");
        } catch (Exception e) {
            timeFrom = 0;
            timeTo = 0;
        }
        
        // cấu hình cho cầu cân hiển thị PO và vendor
        if((sapSetting.getCheckPov()) != null && (sapSetting.getCheckPov()) == true)
        {
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

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        grbType = new javax.swing.ButtonGroup();
        grbBridge = new javax.swing.ButtonGroup();
        grbCat = new javax.swing.ButtonGroup();
        entityManager = WeighBridgeApp.getApplication().getEm();
        weightTicket = new com.gcs.wb.jpa.entity.WeightTicket();
        purOrder = new com.gcs.wb.jpa.entity.PurOrder();
        outbDel = new com.gcs.wb.jpa.entity.OutbDel();
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
        cbxMaterial = new javax.swing.JComboBox();
        cbxVendorLoading = new javax.swing.JComboBox();
        cbxVendorTransport = new javax.swing.JComboBox();
        txtPoPosto = new javax.swing.JTextField();
        chkInternal = new javax.swing.JCheckBox();
        lblMaterial = new javax.swing.JLabel();
        lblPoPosto = new javax.swing.JLabel();
        lblVendorLoading = new javax.swing.JLabel();
        lblVendorTransport = new javax.swing.JLabel();

        entityManager.clear();

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
                        .addComponent(txtWTNum, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
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
                            .addComponent(rbtMvt311, javax.swing.GroupLayout.DEFAULT_SIZE, 362, Short.MAX_VALUE))
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
                .addContainerGap(10, Short.MAX_VALUE))
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
                .addContainerGap(16, Short.MAX_VALUE))
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
                    .addComponent(txfGoodsQty, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(txfOutQty, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                    .addComponent(txfInQty, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE))
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
                            .addComponent(txtOutTime, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                            .addComponent(txtInTime, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
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

        cbxSLoc.setModel(SAP2Local.getSlocModel(config, entityManager));
        cbxSLoc.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof SLoc) {
                    SLoc sloc = (SLoc)value;
                    setText(sloc.getSLocPK().getLgort().concat(" - ").concat(sloc.getLgobe()));
                    //                    setText(sloc.getLgobe().concat(" - ").concat(sloc.getSLocPK().getLgort()));
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

        cbxMaterial.setModel(getMaterialList());
        cbxMaterial.setName("cbxMaterial"); // NOI18N
        cbxMaterial.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMaterialItemStateChanged(evt);
            }
        });

        cbxVendorLoading.setModel(SAP2Local.getVendorList(config, entityManager));
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

        cbxVendorTransport.setModel(SAP2Local.getVendorList(config, entityManager));
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

        chkInternal.setAction(actionMap.get("selectInternal")); // NOI18N
        chkInternal.setHideActionText(true);
        chkInternal.setName("chkInternal"); // NOI18N
        chkInternal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkInternalItemStateChanged(evt);
            }
        });

        lblMaterial.setText(resourceMap.getString("lblMaterial.text")); // NOI18N
        lblMaterial.setName("lblMaterial"); // NOI18N

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
                    .addComponent(lblMaterial)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(lblReason)
                        .addGap(4, 4, 4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWTicketLayout.createSequentialGroup()
                        .addComponent(chkDissolved)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 285, Short.MAX_VALUE)
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
                                    .addComponent(cbxReason, 0, 206, Short.MAX_VALUE)
                                    .addComponent(txtGRText, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                    .addComponent(cbxSLoc, javax.swing.GroupLayout.Alignment.TRAILING, 0, 206, Short.MAX_VALUE)
                                    .addComponent(txtLicPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                    .addComponent(txtDName, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                                    .addComponent(txtTrailerPlate, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
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
                            .addGroup(pnWTicketLayout.createSequentialGroup()
                                .addComponent(cbxMaterial, 0, 206, Short.MAX_VALUE)
                                .addGap(21, 21, 21)
                                .addComponent(lbKunnr)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnWTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPoPosto, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addGroup(pnWTicketLayout.createSequentialGroup()
                                .addComponent(rbtInward)
                                .addGap(18, 18, 18)
                                .addComponent(rbtOutward)
                                .addGap(18, 18, 18)
                                .addComponent(chkInternal))
                            .addComponent(txtRegItem, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDelNum, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(cbxBatch, 0, 399, Short.MAX_VALUE)
                            .addComponent(txtCementDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(cbxCompleted, 0, 399, Short.MAX_VALUE)
                            .addComponent(txtMatnr, javax.swing.GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
                            .addComponent(cbxKunnr, 0, 399, Short.MAX_VALUE)
                            .addComponent(cbxVendorLoading, javax.swing.GroupLayout.Alignment.TRAILING, 0, 399, Short.MAX_VALUE)
                            .addComponent(cbxVendorTransport, javax.swing.GroupLayout.Alignment.TRAILING, 0, 399, Short.MAX_VALUE))))
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
                    .addComponent(rbtOutward)
                    .addComponent(chkInternal))
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
                    .addComponent(cbxKunnr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaterial))
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
        lblMaterial.getAccessibleContext().setAccessibleDescription(resourceMap.getString("lblMat.AccessibleContext.accessibleDescription")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnScaleData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(pnWTFilter, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
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
                .addContainerGap(57, Short.MAX_VALUE))
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
         WeightTicketJpaController con  = new WeightTicketJpaController(entityManager);
        try {
            config = con.getDev(WeighBridgeApp.getApplication().getConfig().getWbId());
            
            
         btnAccept.setEnabled(WeighBridgeApp.getApplication().connectWB(
                    config.getB1Port(),  //string
                    config.getB1Speed(), //int 
                    config.getB1DBits(), //short 
                    config.getB1SBits().shortValue(), //short 
                    config.getB1PC(),   //short 
                    config.getB1Mettler(),
                    txfCurScale)); 
         
          setSaveNeeded(isValidated());
            
        } catch (Exception ex) {           
            
            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
                  
            
     

    }//GEN-LAST:event_rbtBridge1ActionPerformed

    private void rbtBridge2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtBridge2ActionPerformed
    
        /*config = WeighBridgeApp.getApplication().getConfig();
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
        }
                  */
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
        weightTicket.setLgort(selSloc.getSLocPK().getLgort());
        if (selSloc != null && (txtMatnr.getText() != null && !txtMatnr.getText().trim().isEmpty())) {
            lblSLoc.setForeground(Color.black);
            TypedQuery<BatchStocks> tBatchQ = entityManager.createQuery("SELECT b FROM BatchStocks b WHERE b.batchStocksPK.mandt = :mandt AND b.batchStocksPK.werks = :werks AND b.batchStocksPK.lgort = :lgort  AND b.batchStocksPK.matnr = :matnr ", BatchStocks.class);
            //   TypedQuery<BatchStocks> tBatchQ = entityManager.createQuery("SELECT b FROM BatchStocks b WHERE b.batchStocksPK.mandt = :mandt AND b.batchStocksPK.werks = :werks AND b.batchStocksPK.lgort = :lgort  AND b.batchStocksPK.matnr = :matnr and b.batchStocksPK.charg not like '%-%'", BatchStocks.class);

            tBatchQ.setParameter("mandt", config.getsClient());
            tBatchQ.setParameter("werks", config.getwPlant());
            tBatchQ.setParameter("lgort", selSloc.getSLocPK().getLgort());
            tBatchQ.setParameter("matnr", txtMatnr.getText());
            List<BatchStocks> batchs = tBatchQ.getResultList();
            List<BatchStocksStructure> bBatchStocks = SAP2Local.getBatchStocks(selSloc.getSLocPK().getLgort(), weightTicket.getMatnrRef());
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            for (BatchStocksStructure b : bBatchStocks) {
                BatchStocks bs = new BatchStocks(new BatchStocksPK(config.getsClient(), config.getwPlant(), b.getLgort(), b.getMatnr(), b.getCharg()));
                bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));
                if (batchs.indexOf(bs) == -1) {
                    entityManager.persist(bs);
                } else {
                    entityManager.merge(bs);
                }
                //   if (batchs.indexOf(bs) == -1 && b.getLgort().equalsIgnoreCase(weightTicket.getLgort())) {
                String sfix = "-";
                if (batchs.indexOf(bs) == -1 && b.getLgort().equalsIgnoreCase(weightTicket.getLgort())) {

                    batchs.add(bs);
                }
            }
            entityManager.getTransaction().commit();
            entityManager.clear();

            DefaultComboBoxModel result = new DefaultComboBoxModel();
            for (BatchStocks b : batchs) {
                //if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty() )
                if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                    // Fillter BATCH not contain "-" by Tuanna -10.01.2013 
                    if (WeighBridgeApp.getApplication().getConfig().getwPlant().indexOf("1311") >= 0) {
                        result.addElement(b.getBatchStocksPK().getCharg());
                    } else if (b.getBatchStocksPK().getCharg().indexOf("-") < 0) {
                        result.addElement(b.getBatchStocksPK().getCharg());
                    }
                }
            }
            cbxBatch.setModel(result);
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
         //    weightTicket.setText("ZQT");
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
            if (weightTicket != null && (weightTicket.getRegItemText() != null && !weightTicket.getRegItemText().trim().isEmpty())) {
                txtRegItem.setText(weightTicket.getRegItemText());
            } else if (purOrder != null) {
                txtRegItem.setText(purOrder.getShortText());
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
//        if (rbtPO.isEnabled() && rbtMisc.isEnabled() && rbtMb1b.isEnabled() && grbType.getSelection() != null) {
//            rbtPO.setForeground(Color.black);
//            rbtMisc.setForeground(Color.black);
//            rbtMb1b.setForeground(Color.black);
//        }
//        setSaveNeeded(isValidated());
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
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicket.setRecvPo(null);
            weightTicket.setRecvMatnr(null);
            weightTicket.setMatnrRef(null);
//            weightTicket.setRegItemText(null);
            weightTicket.setUnit(null);
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
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicket.setRecvMatnr(null);
            weightTicket.setMatnrRef(null);
//            weightTicket.setRegItemText(null);
            weightTicket.setUnit(null);
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
        weightTicket.setPosted(0);
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        entityManager.merge(weightTicket);
        entityManager.getTransaction().commit();
        entityManager.clear();
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
            weightTicket.setKunnr(cust.getCustomerPK().getKunnr());
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.merge(weightTicket);
            entityManager.getTransaction().commit();
        }
    }
}//GEN-LAST:event_cbxKunnrItemStateChanged

private void txtOutTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtOutTimeKeyReleased
// TODO add your handling code here:
    if (txtOutTime.getText().length() == 19) {
        String[] time = txtOutTime.getText().split(" ");
        String[] date = time[0].split("/");
        String[] hour = time[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        Date stime = cal.getTime();
        System.out.println(stime);
        weightTicket.setSTime(stime);
    }
}//GEN-LAST:event_txtOutTimeKeyReleased

private void txtInTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInTimeKeyReleased
// TODO add your handling code here:
    if (txtInTime.getText().length() == 19) {
        String[] time = txtInTime.getText().split(" ");
        String[] date = time[0].split("/");
        String[] hour = time[1].split(":");
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]), Integer.parseInt(hour[0]), Integer.parseInt(hour[1]), Integer.parseInt(hour[2]));
        Date stime = cal.getTime();
        System.out.println(stime);
        weightTicket.setFTime(stime);
    }
}//GEN-LAST:event_txtInTimeKeyReleased

private void txtWTNumFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWTNumFocusGained
// Tuanna add -- for paste ID from Clipboard if data is ticket  --28.11.2012

    String result = "";
    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    //odd: the Object param of getContents is not currently used
    Transferable contents = clipboard.getContents(null);
    boolean hasTransferableText =
            (contents != null)
            && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
    if (hasTransferableText) {
        try {
            result = (String) contents.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException ex) {
            //highly unlikely since we are using a standard DataFlavor
            System.out.println(ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println(ex);
            //  ex.printStackTrace();
        }
    }
    if (result.length() == 13) {
        txtWTNum.setText(result);
    }

    //end Add.

}//GEN-LAST:event_txtWTNumFocusGained

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    //tuanna 
    StringSelection stringSelection = new StringSelection("");
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents( stringSelection, null);    
        txtWTNum.selectAll();
        txtWTNum.setText("");
        txtWTNum.requestFocusInWindow();
  
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        if(txtWTNum.getText() != ""  )
        {
            
        }
       
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txtWTNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtWTNumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtWTNumActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveActionPerformed

private void cbxMaterialItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMaterialItemStateChanged
// TODO add your handling code here:
}//GEN-LAST:event_cbxMaterialItemStateChanged

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

private void chkInternalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkInternalItemStateChanged
// TODO add your handling code here:
}//GEN-LAST:event_chkInternalItemStateChanged

    @Action
    public void showMB1BOption() {
        if (weightTicket == null) {
            return;
        }
        if (rbtMb1b.isSelected() && rbtInward.isSelected()) {
            grbType.clearSelection();
            return;
        }
        if (rbtMb1b.isSelected() && weightTicket.getRegCategory() == 'O') {
            RecvSlocView rsview = new RecvSlocView(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), weightTicket.getRecvPo());
            rsview.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
            WeighBridgeApp.getApplication().show(rsview);
            if (!rsview.isShowing()) {
                if (rsview.getRecSloc() != null) {
                    weightTicket.setRecvLgort(rsview.getRecSloc().getSLocPK().getLgort());
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
                weightTicket.setRecvMatnr(setting.getMatnrClinker());
                weightTicket.setMatnrRef(setting.getMatnrClinker());
                weightTicket.setRegItemText("Clinker gia công");
                weightTicket.setUnit("TO");
                weightTicket.setMoveType("313");
                weightTicket.setMoveReas("0003");
                rsview.dispose();
                rsview = null;
                txtRegItem.setText(weightTicket.getRegItemText());
            }
        }
        txtMatnr.setText(weightTicket.getRecvMatnr());
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
        if (rbtMvt311.isSelected() && rbtInward.isSelected()) {
            grbType.clearSelection();
            return;
        }
        txtRegItem.setText(weightTicket.getRegItemText());
        if (rbtMvt311.isSelected() && weightTicket.getRegCategory() == 'O') {
            Mvt311View mvt311View = new Mvt311View(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getRecvLgort(), null);
            mvt311View.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
            WeighBridgeApp.getApplication().show(mvt311View);
            if (!mvt311View.isShowing()) {
                if (mvt311View.getRecSloc() != null) {
                    weightTicket.setRecvLgort(mvt311View.getRecSloc().getSLocPK().getLgort());
                } else {
                    weightTicket.setRecvLgort(null);
                }
                if (mvt311View.getSelMaterial() != null) {
                    material = mvt311View.getSelMaterial();
                    weightTicket.setRecvMatnr(material.getMaterialPK().getMatnr());
                    weightTicket.setMatnrRef(material.getMaterialPK().getMatnr());
                    weightTicket.setRegItemText(material.getMaktx());
                } else {
                    material = null;
                    weightTicket.setRecvMatnr(null);
                    weightTicket.setMatnrRef(null);
//                    weightTicket.setRegItemText(null);
                }
                weightTicket.setRecvPlant(config.getwPlant());
                weightTicket.setRecvCharg(weightTicket.getCharg());
                weightTicket.setUnit("TO");
                weightTicket.setMoveType("311");
                weightTicket.setMoveReas(null);
                mvt311View.dispose();
                mvt311View = null;
                txtRegItem.setText(weightTicket.getRegItemText());
            }
        }
        txtMatnr.setText(weightTicket.getRecvMatnr());
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
        
        if ( rbtPO.isEnabled())
        {
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
            txtRegItem.setText(weightTicket.getRegItemText());
            weightTicket.setEbeln(null);
            weightTicket.setItem(null);
            weightTicket.setRecvLgort(null);
            weightTicket.setRecvPlant(null);
            weightTicket.setRecvCharg(null);
            weightTicket.setRecvPo(null);
            weightTicket.setRecvMatnr(null);
            weightTicket.setMatnrRef(null);
            weightTicket.setUnit(null);
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
            JOptionPane.showMessageDialog(rootPane, "Xin đợi cho cân ổn định");
            return null;
        }
               
     
       //tuanna add 20/11/2013 check position signal 
        boolean fCheckSignal= false ; 
        // Tuanna 10062014 
        boolean fOk = false ; 
        if (fCheckSignal==true )
        {
        String data = "";
         Query q = (Query) entityManager.createNativeQuery("SELECT COUNT(cvalue) AS icount FROM jweighbridge.signal WHERE cvalue LIKE '02' AND wbid LIKE '1411TC'",String.class );
         List<String> lst =  q.getResultList();
         JOptionPane.showMessageDialog(rootPane,lst.get(0).toString()) ;
        }
        
        if (weightTicket == null || txfCurScale.getValue() == null || ((Number) txfCurScale.getValue()).intValue() == 0) {
            return null;
        }
        if (!rbtMisc.isSelected() && weightTicket.getMatnrRef() == null) {
            return null;
        }
        if (!isStage1() && !isStage2()) {
            return null;
        }
        if (rbtPO.isSelected()) {
            if (purOrder.getDocType().equals("UB")) {
                WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
                Material m = null;
                try {
                    m = con.CheckPOSTO(purOrder.getMaterial());
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (m != null) {
                    if (m.getCheckPosto() == null) {
                        return new AcceptScaleTask(WeighBridgeApp.getApplication());
                    } else if (m.getCheckPosto() != null || !m.getCheckPosto().equals("")) {
                        if (!purOrder.getValType().equals("TRANSIT")) {
                            JOptionPane.showMessageDialog(rootPane, "Valuation Type khác 'TRANSIT', vui lòng kiểm tra lại!!");
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
        wt_ID = txtWTNum.getText().trim();
        String kq = "" ; 
        
        
        ///tuanna 02/10/2015 
       String sql = "call pgetWT_Reg ( ?)" ;                                 
                               Query q = (Query) entityManager.createNativeQuery(sql);
                               q.setParameter(1,txt); 
                               List wts = q.getResultList();         
                        
                         try {
                          kq = wts.get(0).toString();                           
                         
                        }catch(Throwable cause ){                                  
                              //    klmax  = -1 ;                                   
                        }
                         
                      if (kq.length() >0 )
                      {
                          txt = kq.trim(); 
                      }
        
        //end
                      
    
           

        
        // tuanna 09092015 
        chkDissolved.setEnabled(false);
        
        if (isEnteredValidWTNum()) {
            String sID = null;
            String sSeq = null;
            sID = txt.substring(0, 10);
            sSeq = txt.substring(10);
            sID.trim();
            sSeq.trim();

            return new ReadWTTask(WeighBridgeApp.getApplication(), sID, sSeq);
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
            //if (weightTicket.getDissolved() == null)
            //    {
            //      weightTicket.setDissolved(false);
            //  }
            if ((weightTicket.getDissolved() == null) || (weightTicket.getDissolved() == false)) { //+20100112#01 Phieu bi huy khong dc in lai
                return new ReprintWTTask(WeighBridgeApp.getApplication());
            } else { //+20100112#01 Phieu bi huy khong dc in lai
                JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), "Phiếu cân đã bị hủy!");
                return null;
            } //+20100112#01 Phieu bi huy khong dc in lai
        }
    }

    @Action(enabledProperty = "saveNeeded")
    public Task saveWT() throws Exception {
        //{+20101203#01 Cheat code, adjust date to posting - be deleted
        //weightTicket.setSScale(new BigDecimal(((Number) txfOutQty.getValue()).doubleValue()));
        // Date now = Calendar.getInstance().getTime();
        // final long ONE_DAY_MILLISCONDS = now.getTime() - (long) 25 * 60 * 60 * 1000 * 30;
        // Date lastMonth = new Date( ONE_DAY_MILLISCONDS );
        // weightTicket.setSTime(lastMonth);
        //}+20101203#01
         WeightTicketJpaController cmes  = new WeightTicketJpaController(entityManager); 
         String msg ="";
         
        boolean valid = isValidated();
        if (weightTicket != null) {
            if (weightTicket.getPosted() == -1 && chkDissolved.isSelected()) {
                valid = true;
            }
            //20121203 HOANGVV : check KL Dang Ky = KL Thuc te cho xi mang
            OutbDel outdel_tmp = null;
            WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
            List<OutbDetailsV2> details_list = new ArrayList<OutbDetailsV2>();
            OutbDetailsV2 item = null;
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

                    ximang_tmp = mat_tmp.getBag();
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    details_list = con.findByMandtDelivNumb(outdel_tmp.getOutbDelPK().getDelivNumb());
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (int j = 0; j < details_list.size(); j++) {
                    item = details_list.get(j);
                    if (item.getLfimg().doubleValue() != item.getLfimgOri().doubleValue()) {
                        if (ximang_tmp) {
                            flag_tmp = false;
                        }
                    }
                }
            }
            if (!flag_tmp) {
            //    JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(),cmes.getMsg("3") ); 
                String Mes ="Điểm nhận hàng trên phiếu không hợp lệ, vui lòng kiểm tra, hoặc liên hệ DVKH để được hỗ trợ. " ; 
            //    Mes = cmes.getMsg("3") ; 
                 JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(),Mes.toString()  ); 
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
                msg = "Lỗi thiếu thông tin ghi chú ! Vui lòng nhập thông tin ghi chú "; 
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
                msg  = " Có muốn lưu phiếu cân không ? "; 
            } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
            }
            Integer lv_return = JOptionPane.showConfirmDialog(WeighBridgeApp.getApplication().getMainFrame(),msg , "Question", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            if (lv_return == JOptionPane.YES_OPTION) {
                //
                if (outbDel != null && outbDel.getMatnr() != null
                        && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrPcb40())
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
                            Variant vari = new Variant();
                            VariantPK variPK = new VariantPK();
                            EntityManager entityManager = java.beans.Beans.isDesignTime() ? null : WeighBridgeApp.getApplication().getEm();
                            variPK.setMandt(config.getsClient().toString());
                            variPK.setWPlant(config.getwPlant().toString());
                            variPK.setParam("PROCESS_ORDER_CF");
                            vari.setVariantPK(variPK);
                            vari = entityManager.find(Variant.class, vari.getVariantPK());
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
                                ProcOrdView procoView = new ProcOrdView(WeighBridgeApp.getApplication().getMainFrame(), weightTicket.getPpProcord());
                                procoView.setLocationRelativeTo(WeighBridgeApp.getApplication().getMainFrame());
                                WeighBridgeApp.getApplication().show(procoView);
                                if (!procoView.isShowing()) {
                                    if (procoView.getProcOrd() != null) {
                                        weightTicket.setPpProcord(procoView.getProcOrd());
                                    } else {
                                        weightTicket.setPpProcord(null);
                                    }
                                    procoView.dispose();
                                    procoView = null;
                                    if (weightTicket.getPpProcord() == null) {
                                        JOptionPane.showMessageDialog(WeighBridgeApp.getApplication().getMainFrame(), "Cần nhập Process Order để thực hiện thao tác xuất XM PCB40 !!!");
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
            boolean valid = text.matches("\\d{13}");  // 
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
        List<Material> materials = null;
        
        //get data from DB
        TypedQuery<Material> tMaterial = entityManager.createQuery("SELECT m FROM Material m WHERE m.materialPK.wplant = :wplant order by m.materialPK.matnr asc", Material.class);
        tMaterial.setParameter("wplant", config.getwPlant());
        materials = tMaterial.getResultList();
        //get data from sap and sync DB
        //List<Material> mMaterials = SAP2Local.getLookUpMaterialsList(config.getwPlant());
        List<Material> mMaterials = SAP2Local.getMaterialsList();
        if (!entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().begin();
        }
        for (Material mSap : mMaterials) {
            if (materials.indexOf(mSap) == -1) {
                entityManager.persist(mSap);
            } else {
                entityManager.merge(mSap);
            }
        }
        entityManager.getTransaction().commit();
        entityManager.clear();
        materials = tMaterial.getResultList();
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        for (Material m : materials) {
            result.addElement(m);
            }
        return result;
    }

    private class ReadWTTask extends Task<Object, Void> {

        String id;
        Integer seq;
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

        ReadWTTask(Application app, String id, String seq) {
            super(app);
            this.id = id;
            this.seq = Integer.valueOf(seq);
            setReprintable(false);
            grbType.clearSelection();
            grbCat.clearSelection();
            config = WeighBridgeApp.getApplication().getConfig();
            
            
        }

        @Override
        protected Object doInBackground() {
            entityManager.clear();
            weightTicket = entityManager.find(WeightTicket.class, new WeightTicketPK(config.getsClient(), config.getwPlant(), id, seq));
            entityManager.clear();
            if (weightTicket == null) {
                failed(new Exception("Không có phiếu cân số: " + txtWTNum.getText()));
            } else {
                if (weightTicket.getRegCategory() == 'I') {
                    rbtInward.setSelected(true);
                } else {

                    rbtOutward.setSelected(true);
                }
                // get Niem Xa 
                String SoNiemXa =""; 
                String Posto =""; 
                String sql = "call pgetWT_NiemXa ( ?)" ;                                 
                               Query q = (Query) entityManager.createNativeQuery(sql);
                               q.setParameter(1,txtWTNum.getText().toString()); 
                               List wts = q.getResultList();         
                        
                         try {
                          SoNiemXa = wts.get(0).toString();                           
                          txtCementDesc.setText(SoNiemXa);
                          
                        }catch(Throwable cause ){                                  
                              //    klmax  = -1 ;                                   
                        }
                         
                          try{
                      sql = "call pvc_getTicketIndex ( ? ) " ;                                 
                               Query qx = (Query) entityManager.createNativeQuery(sql);
                               qx.setParameter(1,txtWTNum.getText().toString()); 
                               List wtxxx = qx.getResultList();         
                        
                         try {
                             for (Object obj : wtxxx)
                                    {
                                      // wt[0] == null; 
                                       Object[] wt = (Object[]) obj;
                                      //  txtDName.setText(wt[4].toString());
                                         Posto = wt[2].toString().trim(); 
                                         txtPONum.setText(Posto); 
                                         weightTicket.setAbbr(wt[3].toString().trim());
                                          rbtPO.setSelected(true);
                                    }
                          
                        }catch(Throwable cause ){                                  
                              //    klmax  = -1 ;                                   
                        }
                        
                         
                         
                 } catch(Throwable cause ){ 
                                        
                 }
                    
                
                // <editor-fold defaultstate="collapsed" desc="Determine state of Weight Ticket">
                setStage1(false);
                setStage2(false);
                if (weightTicket.getFScale() == null && weightTicket.getSScale() == null) {
                    setStage1(true);
                } else if (weightTicket.getFScale() != null && weightTicket.getSScale() == null) {
                    setStage2(true);
                }
                OutbDel od = null; //HLD18++
                // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc="Load D.O/P.O details">
                if ((weightTicket.getDelivNumb() == null || weightTicket.getDelivNumb().trim().isEmpty())
                        || ((weightTicket.getPosted() == 0 || weightTicket.getPosted() == -1) && (weightTicket.getEbeln() != null && !weightTicket.getEbeln().trim().isEmpty()))
                        || (weightTicket.getDelivNumb() == null && weightTicket.getEbeln() == null)) {
                    setWithoutDO(true);
                } else {
                   // OutbDel od = null; //HLD18--
                    List<OutbDetailsV2> odt = null;
                    WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
                    try {
                        od = con.findByMandtOutDel(weightTicket.getDelivNumb());
                    } catch (Exception ex) {
//                        java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (od == null && weightTicket.getEbeln() != null) {
                        od = SAP2Local.getOutboundDelivery(weightTicket.getDelivNumb(), false);
                        if (od != null) {
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            entityManager.persist(od);
                            entityManager.getTransaction().commit();
                        }
                        try {
                            odt = con.findByMandtDelivNumb(weightTicket.getDelivNumb());
                        } catch (Exception ex) {
//                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if ((odt != null) && (odt.size() > 0)) {
                            OutbDetailsV2 item = odt.get(0);
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
                   && od == null     //HLD18
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
                    String[] do_list = weightTicket.getDelivNumb().split("-");
                    WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
                    List<OutbDetailsV2> details_list = new ArrayList<OutbDetailsV2>();
                    OutbDetailsV2 item = null;
                    outbDel_list.clear();
                    outDetails_lits.clear();
                    for (int i = 0; i < do_list.length; i++) {
                        try {
                            //outbDel = entityManager.find(OutbDel.class, new OutbDelPK(config.getsClient(), do_list[i]));
                            outbDel = con.findByMandtOutDel(do_list[i]);
                        } catch (Exception ex) {
                            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (!WeighBridgeApp.getApplication().isOfflineMode()){ //HLD18++offline
                        OutbDel sapOutbDel = SAP2Local.getOutboundDelivery(do_list[i], false);
                        if (sapOutbDel != null && outbDel == null) {
                            if (!entityManager.getTransaction().isActive()) {
                                entityManager.getTransaction().begin();
                            }
                            entityManager.persist(sapOutbDel);
                            entityManager.getTransaction().commit();
                            entityManager.clear();
                        } else if (sapOutbDel != null && outbDel != null) {
                            sapOutbDel.setPosted(outbDel.getPosted());
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
                            weightTicket.setKunnr(null);
                            if ((weightTicket.getKunnr() == null || weightTicket.getKunnr().isEmpty())
                                    && outbDel.getKunnr() != null && !outbDel.getKunnr().isEmpty()) {
                                weightTicket.setKunnr(outbDel.getKunnr());
                            }
                            weightTicket.setRegItemText(outbDel.getArktx());
                            weightTicket.setMatnrRef(outbDel.getMatnr());
                            weightTicket.setItem(outbDel.getDelivItem());
                            weightTicket.setUnit(outbDel.getVrkme());
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
                if (weightTicket.getEbeln() != null && !weightTicket.getEbeln().trim().isEmpty()) {
             
                    purOrder = entityManager.find(PurOrder.class, new PurOrderPK(config.getsClient(), weightTicket.getEbeln()));
                    txtPONum.setText(weightTicket.getEbeln());
                    setValidPONum(true);
                    rbtPO.setSelected(true);
                    setSubContract(false);
                    if (rbtOutward.isSelected() && purOrder.getItemCat() == '3' && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())) {
                        setSubContract(true);
                    }
                } else {
                    if ( Posto.equals("")){  
                        // Tuanna 2018 08015
                        txtPONum.setText(null);
                        rbtPO.setSelected(false);                        
                    }
                    else
                    {
                         rbtPO.setSelected(true);  
                         rbtMisc.setSelected (false ) ; 
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
                } else if (isWithoutDO() && (weightTicket.getEbeln() == null || weightTicket.getEbeln().trim().isEmpty())) {
                    rbtMisc.setSelected(true);
                } else if (!isWithoutDO() && WeighBridgeApp.getApplication().isOfflineMode()) //HLD18++
                {
                    rbtMisc.setSelected(true);
                }    
                if (weightTicket.getRegCategory() == 'I'
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
                    smatref = weightTicket.getMatnrRef();
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
                txtRegItem.setText(weightTicket.getRegItemText());
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
                txtDName.setText(weightTicket.getTenTaiXe());
                txtCMNDBL.setText(weightTicket.getCmndBl());
                txtLicPlate.setValue(weightTicket.getSoXe());
                txtTrailerPlate.setValue(weightTicket.getSoRomooc());

                //20120522_ setEnabled for "combo box Khach hang" depends on Offline(of Weight Ticket, radio Offline) and Posted status
                //Posted: -1-Post hong, 0-Chua Post, 1-Post vo SAP ok, 2-Post ok nhung Offline
                cbxKunnr.setSelectedItem(null);
                cbxKunnr.setSelectedIndex(-1);
                if (weightTicket.getKunnr() != null && !weightTicket.getKunnr().trim().isEmpty()) {
                    entityManager.clear();
                    Customer cust = entityManager.find(Customer.class, new CustomerPK(config.getsClient(), weightTicket.getKunnr()));
                    cbxKunnr.setSelectedItem(cust);
                }
                if ((WeighBridgeApp.getApplication().isOfflineMode()
                        && (weightTicket.getPosted() == 0 || weightTicket.getPosted() == -1))
                        || (!WeighBridgeApp.getApplication().isOfflineMode()
                        && (rbtMisc.isSelected())
                        && (weightTicket.getPosted() == 0 || weightTicket.getPosted() == -1))) {
                    if (weightTicket.getRegCategory() == 'O' && weightTicket.getEbeln() == null) {
                        cbxKunnr.setEnabled(true); // 2471
                    }
                } else {
                    // logic cho nay => no mac dinh offline khi usr can xuat clinker cho POSTO
                    //  cbxKunnr.setEnabled(false);
                }
                if (weightTicket.getLgort() != null && !weightTicket.getLgort().trim().isEmpty()) {
                    entityManager.clear();
                    SLoc sloc = entityManager.find(SLoc.class, new SLocPK(config.getsClient(), config.getwPlant(), weightTicket.getLgort()));
                    cbxSLoc.setSelectedItem(sloc);
                } else if (outbDel != null && outbDel.getLgort() != null && !outbDel.getLgort().trim().isEmpty()) {
                    SLoc sloc = entityManager.find(SLoc.class, new SLocPK(config.getsClient(), config.getwPlant(), outbDel.getLgort()));
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

                txtDelNum.setText(weightTicket.getDelivNumb());
                txtCementDesc.setText(SoNiemXa);
                if (weightTicket.getNoMoreGr() != null && weightTicket.getNoMoreGr() == '2') {
                    cbxCompleted.setSelectedIndex(1);
                } else {
                    cbxCompleted.setSelectedIndex(0);
                }
                chkDissolved.setSelected(weightTicket.getDissolved() == null ? false : weightTicket.getDissolved());
                if (chkDissolved.isSelected() || (!isStage1() && !isStage2() && weightTicket.getPosted() == 1)) {
                    chkDissolved.setEnabled(false);
                } else {
                    chkDissolved.setEnabled(true);
                }
//                setDissolved(chkDissolved.isSelected());
                if (isDissolved() || (!isStage1() && !isStage2() && (weightTicket.getPosted() == 1 || weightTicket.getPosted() == 2))) {
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

                if (isDissolved() || (!isStage1() && isStage2()) || (!isStage1() && !isStage2() && (weightTicket.getPosted() == 1 || weightTicket.getPosted() == 2))) {
                    setReprintable(true);
                }
                // <editor-fold defaultstate="collapsed" desc="bind batch">
                if (cbxSLoc.getSelectedIndex() > -1
                        && ((weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty())
                        || (!isWithoutDO() && outbDel != null && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()))
                        && weightTicket.getMatnrRef() != null && !weightTicket.getMatnrRef().trim().isEmpty()) {
                    String lgort = ((SLoc) cbxSLoc.getSelectedItem()).getSLocPK().getLgort();
                    BatchStocks batch = null;
                    if (weightTicket.getCharg() != null && !weightTicket.getCharg().trim().isEmpty()) {
                        batch = entityManager.find(BatchStocks.class, new BatchStocksPK(config.getsClient(), config.getwPlant(), lgort, weightTicket.getMatnrRef(), weightTicket.getCharg()));
                    } else if (!isWithoutDO() && outbDel.getCharg() != null && !outbDel.getCharg().trim().isEmpty()) {
                        batch = entityManager.find(BatchStocks.class, new BatchStocksPK(config.getsClient(), config.getwPlant(), lgort, weightTicket.getMatnrRef(), outbDel.getCharg()));
                    }
                    if (cbxBatch.getModel().getSize() == 0) {
                        TypedQuery<BatchStocks> tBatchQ = entityManager.createQuery("SELECT b FROM BatchStocks b WHERE b.batchStocksPK.mandt = :mandt AND b.batchStocksPK.werks = :werks AND b.batchStocksPK.lgort = :lgort  AND b.batchStocksPK.matnr = :matnr", BatchStocks.class);
                        tBatchQ.setParameter("mandt", config.getsClient());
                        tBatchQ.setParameter("werks", config.getwPlant());
                        tBatchQ.setParameter("lgort", lgort);
                        tBatchQ.setParameter("matnr", weightTicket.getMatnrRef());
                        List<BatchStocks> batchs = tBatchQ.getResultList();
                        List<BatchStocksStructure> bBatchStocks = SAP2Local.getBatchStocks(lgort, weightTicket.getMatnrRef());
                        if (!entityManager.getTransaction().isActive()) {
                            entityManager.getTransaction().begin();
                        }
                        for (BatchStocksStructure b : bBatchStocks) {
                            BatchStocks bs = new BatchStocks(new BatchStocksPK(config.getsClient(), config.getwPlant(), b.getLgort(), b.getMatnr(), b.getCharg()));
                            bs.setLvorm(b.getLvorm() == null || b.getLvorm().trim().isEmpty() ? ' ' : b.getLvorm().charAt(0));
                            if (batchs.indexOf(bs) == -1) {
                                entityManager.persist(bs);
                            } else {
                                entityManager.merge(bs);
                            }
                            if (batchs.indexOf(bs) == -1 && b.getLgort().equalsIgnoreCase(weightTicket.getLgort())) {
                                batchs.add(bs);
                            }
                        }
                        entityManager.getTransaction().commit();
                        entityManager.clear();
                        DefaultComboBoxModel result = new DefaultComboBoxModel();
                        for (BatchStocks b : batchs) {
                            if (b.getLvorm() == null || b.getLvorm().toString().trim().isEmpty()) {
                                result.addElement(b.getBatchStocksPK().getCharg());
                            }
                        }
                        cbxBatch.setModel(result);
                    }
                    if (batch != null) {
                        cbxBatch.setSelectedItem(batch.getBatchStocksPK().getCharg());
                    } else if (weightTicket.getCharg() != null && cbxBatch.isEditable()) {
                        cbxBatch.setSelectedItem(weightTicket.getCharg());
                    } else {
                        cbxBatch.setSelectedIndex(-1);
                    }
                }
                
                // cấu hình cho cầu cân hiển thị PO và vendor
                if(sapSetting.getCheckPov() != null && sapSetting.getCheckPov() == true)
                {
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
                
                // check display select for Vat tu from SAP to DB
                Material matDB = entityManager.find(Material.class, new MaterialPK(config.getsClient(), config.getwPlant(), weightTicket.getMatnr()));
                
                // CHECK sap
//                Material matSap = SAP2Local.getMaterialDetail(weightTicket.getMatnr());
//                if(matSap != null)
//                {
//                    if(matDB != null && (!matSap.getMaktx().equalsIgnoreCase(matDB.getMaktx())))
//                    {
//                        entityManager.merge(matSap);
//                        matDB = matSap;
//                    }
//                    if(matDB == null)
//                    {
//                        entityManager.persist(matSap);
//                        matDB = matSap;
//                    }
//                }
                if(matDB != null)
                {
                   cbxMaterial.setSelectedItem(matDB); 
                } else
                {
                    cbxMaterial.setSelectedIndex(-1);
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
                if (weightTicket.getPosted() == 0 || weightTicket.getPosted() == -1) {
                    chkDissolved.setEnabled(true);
                } else {
                    chkDissolved.setEnabled(false);
                }
//                if (weightTicket.getPosted() == -1 && weightTicket.getDissolved() == null) {
//                    setDissolved(chkDissolved.isSelected());
//                    setSaveNeeded(true);
//                }
                if (weightTicket.getDissolved() != null) {
                    if (weightTicket.getDissolved() == true) {
                        setSaveNeeded(false);
                    }
                }
                if (weightTicket.getPosted() == -1) {
                    btnPostAgain.setEnabled(true);
                } else {
                    btnPostAgain.setEnabled(false);
                }
            }
        }
    }

    private class ReadPOTask extends Task<Object, Void> {

        String poNum;
        PurOrder sapPurOrder = null;
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
            setMessage("Đang đọc P.O từ CSDL ...");
            setProgress(0, 0, 3);
            // Tuanna >> 14.06.13 
            cbxKunnr.setSelectedIndex(-1);
            cbxKunnr.setEnabled(false);

            // << end comment.

            purOrder = entityManager.find(PurOrder.class, new PurOrderPK(config.getsClient(), poNum));
            setMessage("Tìm dữ liệu của P.O trong SAP ...");
            setProgress(1, 0, 3);

            try {
                sapPurOrder = SAP2Local.getPurchaseOrder(poNum);
            } catch (Exception ex) {
                failed(ex);
            }
            if (sapPurOrder != null) {
                if (sapPurOrder.getVendor() != null && !sapPurOrder.getVendor().trim().isEmpty()) {
                    vendor = entityManager.find(
                            Vendor.class,
                            new VendorPK(config.getsClient(), sapPurOrder.getVendor()));
                    sapVendor = SAP2Local.getVendor(sapPurOrder.getVendor());
                }
                if (sapPurOrder.getSupplVend() != null && !sapPurOrder.getSupplVend().trim().isEmpty()) {
                    supVendor = entityManager.find(
                            Vendor.class,
                            new VendorPK(config.getsClient(), sapPurOrder.getSupplVend()));
                    sapSupVendor = SAP2Local.getVendor(sapPurOrder.getSupplVend());
                }
                if (sapPurOrder.getCustomer() != null && !sapPurOrder.getCustomer().trim().isEmpty()) {
                    customer = entityManager.find(
                            Customer.class,
                            new CustomerPK(config.getsClient(), sapPurOrder.getCustomer()));
                    sapCustomer = SAP2Local.getCustomer(sapPurOrder.getCustomer());
                }
            }
            
            
            setMessage("Lưu dữ liệu P.O vào CSDL ...");
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
                purOrder = entityManager.find(PurOrder.class, sapPurOrder.getPurOrderPK());
                entityManager.refresh(purOrder);
                entityManager.clear();
                setValidPONum(true);
            } else {
                purOrder = null;
                setValidPONum(false);
            }
            if (isValidPONum()) {
                setSubContract(false);
                if ((rbtInward.isSelected() && purOrder.getPlant().equalsIgnoreCase(config.getwPlant()))
                        || (rbtOutward.isSelected() && purOrder.getSupplPlnt().equalsIgnoreCase(config.getwPlant()))) {
                    setValidPONum(true);
                } else if (rbtOutward.isSelected() && purOrder.getItemCat() == '3' && purOrder.getMaterial().equalsIgnoreCase(setting.getMatnrPcb40())) {
                    purOrder.setMaterial(setting.getMatnrClinker());
                    purOrder.setShortText("Clinker gia công");
                    purOrder.setPoUnit("TO");
                    setValidPONum(true);
                    setSubContract(true);
                } else {
                    //{+20101206#01
                    setValidPONum(true);
                    //}+20101206#01
                    //{-20101206#01
                    //setValidPONum(false);
                    //String mode = rbtInward.isSelected() ? "nhập" : "xuất";
                    //String msg = "PO \" " + this.poNum + " \" không được phép sử dụng để " + mode + " hàng tại Plant \" " + config.getwPlant() + "\"";
                    //failed(new Exception(msg));
                    //}-20101206#01
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
            if (isValidPONum()) {
                setSaveNeeded(isValidated());
                txtRegItem.setText(purOrder.getShortText());
                txtMatnr.setText(purOrder.getMaterial());
                weightTicket.setEbeln(purOrder.getPurOrderPK().getPoNumber());
                weightTicket.setItem(purOrder.getPoItem());
                weightTicket.setRegItemText(purOrder.getShortText());
                weightTicket.setMatnrRef(purOrder.getMaterial());
//                weightTicket.setUnit(purOrder.getPoUnit());
                weightTicket.setUnit("TON");
            } else {
                txtRegItem.setText(null);
                txtMatnr.setText(null);
                weightTicket.setEbeln(null);
                weightTicket.setItem(null);
                weightTicket.setMatnrRef(null);
                weightTicket.setUnit(null);
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


            // End add   
            btnSave.setEnabled(false);
            if (((isStage2() || (!isStage1() && !isStage2())) && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false))
                    || (!isStage1() && !isStage2() && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false)
                    && (weightTicket != null && (weightTicket.getPosted() == 0)))) {
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
            OutbDel outbDel = null;
            List<String> completedDO = new ArrayList<String>();
            if (rbtPO.isSelected()) {
                if (((isStage2() || (!isStage1() && !isStage2())) && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false))
                        || (!isStage1() && !isStage2() && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false)
                        && (weightTicket != null && weightTicket.getPosted() == 0))) {
                    if (rbtInward.isSelected()) {
                        if (weightTicket.getDelivNumb() == null) {
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
                            } else if (weightTicket.getDelivNumb() != null) {
                                // tuanna 16.06.13
                                //  cbxKunnr.setSelectedIndex(-1);
                                objBapi = getPgmVl02nBapi(weightTicket, outbDel);

                            } else {
                                // tuanna 16.06.13
                                //cbxKunnr.setSelectedIndex(-1);
                                objBapi = getDoCreate2PGI(weightTicket, outbDel);
                            }
                        }
                    }
                    if (WeighBridgeApp.getApplication().isOfflineMode() == false) {
                        if (objBapi != null) {
                            try {
                                sapSession.execute(objBapi);
                                if (objBapi instanceof DOCreate2PGIBapi) {
                                    weightTicket.setDelivNumb(((DOCreate2PGIBapi) objBapi).getDelivery());
                                    weightTicket.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                    weightTicket.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                    try {
                                        bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP (DOCreate2PGIBapi line >>> 2880) ";
                                    }
                                }
                                if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                    weightTicket.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                    weightTicket.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                    try {
                                        bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "No message returned when call SAP ( GoodsMvtPoCreateBapi line >>2889 ) ";
                                    }
                                }
                                if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                    weightTicket.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                    weightTicket.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));

                                    try {
                                        bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = " No message returned when call SAP ( GoodsMvtDoCreateBapi line 2899 )";
                                    }
                                }
                                if (objBapi instanceof WsDeliveryUpdateBapi) {
                                    weightTicket.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                    weightTicket.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));
                                    if (weightTicket.getPpProcord() != null && weightTicket.getPpProcord().length() == 12) {
                                        weightTicket.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
                                        weightTicket.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
                                    }

                                    try {
                                        bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                    } catch (Exception Ex) {
                                        bapi_message = "ERROR:2944- No message returned error ";
                                    }

                                }
                                if (weightTicket.getMatDoc() == null || weightTicket.getMatDoc().equals("")) {
                                    revertCompletedDO(completedDO, null, null);
                                    weightTicket.setPosted(-1);
                                    if (bapi_message == "") {
                                        bapi_message = "Error in BAPI function";
                                    }
                                    JOptionPane.showMessageDialog(rootPane, bapi_message);
                                    completed = false;
                                    entityManager.clear();
                                } else if ((weightTicket.getMatDoc() != null) && (!weightTicket.getMatDoc().equals(""))) {
                                    weightTicket.setPosted(1);
                                    completedDO.add(weightTicket.getDelivNumb());
                                }

                                // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
                                if (outbDel != null) {
                                    OutbDel sapOutb = SAP2Local.getOutboundDelivery(outbDel.getOutbDelPK().getDelivNumb(), false);
                                    Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                    Vendor lifnr = null, sapLifnr = null;
                                    if (sapOutb != null) {
                                        if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                            kunnr = entityManager.find(
                                                    Customer.class,
                                                    new CustomerPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getKunnr()));
                                            sapKunnr = SAP2Local.getCustomer(sapOutb.getKunnr());
                                        }
                                        if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                            kunag = entityManager.find(
                                                    Customer.class,
                                                    new CustomerPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getKunag()));
                                            sapKunag = SAP2Local.getCustomer(sapOutb.getKunag());
                                        }
                                        if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                            lifnr = entityManager.find(
                                                    Vendor.class,
                                                    new VendorPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getLifnr()));
                                            sapLifnr = SAP2Local.getVendor(sapOutb.getLifnr());
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
                                    if (sapKunag != null && kunag == null && !sapKunnr.getCustomerPK().getKunnr().equalsIgnoreCase(sapKunag.getCustomerPK().getKunnr())) {
                                        entityManager.persist(sapKunag);
                                    } else if (sapKunag != null && kunag != null) {
                                        entityManager.merge(sapKunag);
                                    } else if (sapKunag == null && kunag != null && !sapKunnr.getCustomerPK().getKunnr().equalsIgnoreCase(sapKunag.getCustomerPK().getKunnr())) {
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

                                weightTicket.setPosted(-1);
                                failed(ex);
                                completed = false;
                                entityManager.clear();
                            }
                        } else if (rbtMisc.isSelected() || objBapi == null) {
                            weightTicket.setPosted(2);
                            weightTicket.setUnit("TON");
                        }
                    } else {
                        bapi_message = "Đang post phiếu ở chế độ OFFLINE 3024";
                        weightTicket.setPosted(2);
                        weightTicket.setUnit("TON");
                    }
                }
                /*} else if (weightTicket.getDelivNumb() != null || !weightTicket.getDelivNumb().equals("")) {*/
                // 20120521: update check PO-STO.
            } else {
                for (int i = 0; i < outbDel_list.size(); i++) {
                    outbDel = outbDel_list.get(i);
                    if (((isStage2() || (!isStage1() && !isStage2())) && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false))
                            || (!isStage1() && !isStage2() && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false)
                            && (weightTicket != null && weightTicket.getPosted() == 0))) {
                        if (rbtInward.isSelected()) {
                            if (weightTicket.getDelivNumb() == null) {
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
                                } else if (weightTicket.getDelivNumb() != null) {
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

                                    OutbDetailsV2 details_item = null;

                                    if (objBapi instanceof DOCreate2PGIBapi) {
                                        weightTicket.setDelivNumb(((DOCreate2PGIBapi) objBapi).getDelivery());
                                        weightTicket.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                        weightTicket.setDocYear(Integer.valueOf(((DOCreate2PGIBapi) objBapi).getDocYear()));
                                        try {
                                            bapi_message = ((DOCreate2PGIBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = "No message returned  >> ERR 3048 ";
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            //if (details_item.getDelivNumb().equals(outbDel.getOutbDelPK().getDelivNumb())) {
                                            if (details_item.getOutbDetailsV2PK().getDelivNumb().equals(outbDel.getOutbDelPK().getDelivNumb())) {
                                                details_item.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((DOCreate2PGIBapi) objBapi).getDocYear());
                                                outbDel.setMatDoc(((DOCreate2PGIBapi) objBapi).getMatDoc());
                                            }
                                            if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted("-1");
                                                outbDel.setPosted("-1");
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted("1");
                                                outbDel.setPosted("1");
                                            }

                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((DOCreate2PGIBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted("-1");
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted("1");
                                        }
                                    }
                                    if (objBapi instanceof GoodsMvtPoCreateBapi) {
                                        weightTicket.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                        weightTicket.setDocYear(Integer.valueOf(((GoodsMvtPoCreateBapi) objBapi).getMatYear()));
                                        try {
                                            bapi_message = ((GoodsMvtPoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = "No message returned  >> ERROR 3086 ";
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getOutbDetailsV2PK().getDelivNumb().equals(outbDel.getOutbDelPK().getDelivNumb())) {
                                                details_item.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((GoodsMvtPoCreateBapi) objBapi).getMatYear());
                                                outbDel.setMatDoc(((GoodsMvtPoCreateBapi) objBapi).getMatDoc());
                                            }
                                            if (((GoodsMvtPoCreateBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted("-1");
                                                outbDel.setPosted("-1");
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted("1");
                                                outbDel.setPosted("1");
                                            }
                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((GoodsMvtPoCreateBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted("-1");

                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted("1");
                                        }
                                    }
                                    if (objBapi instanceof GoodsMvtDoCreateBapi) {
                                        weightTicket.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                        weightTicket.setDocYear(Integer.valueOf(((GoodsMvtDoCreateBapi) objBapi).getMatYear()));
                                        try {
                                            bapi_message = ((GoodsMvtDoCreateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = "No message returned ERROR 3123 ";
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getOutbDetailsV2PK().getDelivNumb().equals(outbDel.getOutbDelPK().getDelivNumb())) {
                                                details_item.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                                details_item.setDocYear(((GoodsMvtDoCreateBapi) objBapi).getMatYear());
                                                outbDel.setMatDoc(((GoodsMvtDoCreateBapi) objBapi).getMatDoc());
                                            }
                                            if (((GoodsMvtDoCreateBapi) objBapi).getMatDoc() == null) {
                                                details_item.setPosted("-1");
                                                outbDel.setPosted("-1");
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted("1");
                                                outbDel.setPosted("1");
                                            }
                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((GoodsMvtDoCreateBapi) objBapi).getMatDoc() == null) {
                                            outbDel.setPosted("-1");
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted("1");
                                        }
                                    }
                                    if (objBapi instanceof WsDeliveryUpdateBapi) {
                                        weightTicket.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                        weightTicket.setDocYear(Integer.valueOf(((WsDeliveryUpdateBapi) objBapi).getDoc_year()));

                                        try {
                                            bapi_message = ((WsDeliveryUpdateBapi) objBapi).getReturn().get(0).getMessage().toString();
                                        } catch (Exception Ex) {
                                            bapi_message = "No message returned ERROR 3160";
                                        }
                                        if (weightTicket.getPpProcord() != null && weightTicket.getPpProcord().length() == 12) {
                                            weightTicket.setPpProcordcnf(((WsDeliveryUpdateBapi) objBapi).getConf_no());
                                            weightTicket.setPpProcordcnfcnt(((WsDeliveryUpdateBapi) objBapi).getConf_cnt());
                                        }
                                        for (int k = 0; k < outDetails_lits.size(); k++) {
                                            details_item = outDetails_lits.get(k);
                                            if (details_item.getOutbDetailsV2PK().getDelivNumb().equals(outbDel.getOutbDelPK().getDelivNumb())) {
                                                details_item.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                                details_item.setDocYear(((WsDeliveryUpdateBapi) objBapi).getDoc_year());
                                                outbDel.setMatDoc(((WsDeliveryUpdateBapi) objBapi).getMat_doc());
                                            }
                                            if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                                details_item.setPosted("-1");
                                                outbDel.setPosted("-1");
                                                flag_fail = true;
                                            } else {
                                                details_item.setPosted("1");
                                                outbDel.setPosted("1");
                                            }

                                            if (!entityManager.getTransaction().isActive()) {
                                                entityManager.getTransaction().begin();
                                            }
                                            entityManager.merge(outbDel);
                                            entityManager.merge(details_item);
                                            entityManager.getTransaction().commit();
                                        }
                                        if (((WsDeliveryUpdateBapi) objBapi).getMat_doc() == null) {
                                            outbDel.setPosted("-1");
                                            flag_fail = true;
                                        } else {
                                            outbDel.setPosted("1");
                                        }
                                    }

                                    if (flag_fail || weightTicket.getMatDoc() == null || weightTicket.getMatDoc().equals("")) {
                                        revertCompletedDO(completedDO, outDetails_lits, outbDel_list);
                                        weightTicket.setPosted(-1);
                                        if (bapi_message == "") {
                                            bapi_message = "Error in BAPI function";
                                        }
                                        JOptionPane.showMessageDialog(rootPane, bapi_message);
                                        completed = false;
                                        entityManager.clear();
                                    } else if (!flag_fail) {
                                        weightTicket.setPosted(1);
                                        completedDO.add(weightTicket.getDelivNumb());
                                    }

                                    // <editor-fold defaultstate="collapsed" desc="Update D.O from SAP to DB">
                                    if (outbDel != null) {
                                        OutbDel sapOutb = SAP2Local.getOutboundDelivery(outbDel.getOutbDelPK().getDelivNumb(), false);
                                        Customer kunnr = null, sapKunnr = null, kunag = null, sapKunag = null;
                                        Vendor lifnr = null, sapLifnr = null;
                                        if (sapOutb != null) {
                                            if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                                                kunnr = entityManager.find(
                                                        Customer.class,
                                                        new CustomerPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getKunnr()));
                                                sapKunnr = SAP2Local.getCustomer(sapOutb.getKunnr());
                                            }
                                            if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                                                kunag = entityManager.find(
                                                        Customer.class,
                                                        new CustomerPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getKunag()));
                                                sapKunag = SAP2Local.getCustomer(sapOutb.getKunag());
                                            }
                                            if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                                                lifnr = entityManager.find(
                                                        Vendor.class,
                                                        new VendorPK(WeighBridgeApp.getApplication().getConfig().getsClient(), sapOutb.getLifnr()));
                                                sapLifnr = SAP2Local.getVendor(sapOutb.getLifnr());
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
                                        if (sapKunag != null && kunag == null && !sapKunnr.getCustomerPK().getKunnr().equalsIgnoreCase(sapKunag.getCustomerPK().getKunnr())) {
                                            entityManager.persist(sapKunag);
                                        } else if (sapKunag != null && kunag != null) {
                                            entityManager.merge(sapKunag);
                                        } else if (sapKunag == null && kunag != null && !sapKunnr.getCustomerPK().getKunnr().equalsIgnoreCase(sapKunag.getCustomerPK().getKunnr())) {
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
                                        sapOutb.setPosted(outbDel.getPosted());
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
                                            outbDel.setPosted("-1");
                                        }
                                    }
                                    weightTicket.setPosted(-1);
                                    failed(e);
                                    completed = false;
                                    entityManager.clear();
                                }

                            } else if (rbtMisc.isSelected() || objBapi == null) {
                                weightTicket.setPosted(2);
                                weightTicket.setUnit("TON");
                            }
                        } else {
                            weightTicket.setPosted(2);
                            weightTicket.setUnit("TON");
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
                weightTicket.setPosted(2);
                weightTicket.setUnit("TON");
            }
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.merge(weightTicket);
            entityManager.getTransaction().commit();
            entityManager.clear();
            if (completed) {
                if (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false || weightTicket.getPosted() == 1 || weightTicket.getPosted() == 2) { //+20100111#01 khong in neu huy phieu
                    setMessage("Đang in phiếu cân...");
                    // tuanna >>17.06.2013                 
                    //      Query qUpdate = (Query) entityManager.createNativeQuery("call pSet_KhachHang1311() ");

                    // << end 
                    printWT(weightTicket, false);
                    
                    //Tuanna >> 
                    /*
                    if (weightTicket.getRegCategory() == 'O' )
                    {
                        if(weightTicket.getSTime() == null && weightTicket.getFTime() != null )
                        {
                            if (weightTicket.getMoveType().toString() == "601" || weightTicket.getMoveType().toString() == "921"  ) 
                                // updating somthing for report . 
                                
                                 true = true ; 
                            
                            }
                            
                    }
                    */
                }
            }
            btnSave.setEnabled(false);
            //       cbxKunnr.setSelectedIndex(-1); 

            // >> tuanna fix 140613 
            //  cbxKunnr.setEnabled(true);
            return null;  // return your result
        }

        protected void revertCompletedDO(List<String> completedDOs, List<OutbDetailsV2> OutbDetailsV2, List<OutbDel> outbDels) {
            // revert completed DOs
            DORevertBapi bapi = null;
            for (String item : completedDOs) {
                bapi = new DORevertBapi(item);
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

                OutbDel od_temp = new OutbDel(new OutbDelPK(weightTicket.getWeightTicketPK().getMandt(), item));
                GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, od_temp, outDetails_lits);
                
                bapi.setWeightticket(stWT);
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert                
                try {
                    sapSession.execute(bapi);
                } catch (Exception ex) {
                    //do nothing
                }
            }

            // active entityManager
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            // revert posted_status of DO in OutbDetailsV2
            if (OutbDetailsV2 != null) {
                for (OutbDetailsV2 outbDetail : OutbDetailsV2) {
                    outbDetail.setPosted("-1");
                    entityManager.merge(outbDetail);
                }
            }
            // revert posted_status of DO in outbDel
            if (outbDels != null) {
                for (OutbDel outbD : outbDels) {
                    outbD.setPosted("-1");
                    entityManager.merge(outbD);
                }
            }

            //commit transaction
            entityManager.getTransaction().commit();

        }

        @Override
        protected void failed(Throwable cause) {
            completed = false;
            if ((isStage2() && (weightTicket.getDissolved() == null || weightTicket.getDissolved() == false))
                    || (weightTicket != null && weightTicket.getPosted() == 0)) {
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
                    weightTicket.setPosted(-1);
                }
            } else {
                if (cause instanceof HibersapException && cause.getCause() instanceof JCoException) {
                    cause = cause.getCause();
                }
                logger.error(null, cause);
                JOptionPane.showMessageDialog(rootPane, cause != null ? cause.getMessage() : "Null Pointer Exception");
                weightTicket.setPosted(-1);
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
                setMessage("Đang kiểm tra tồn kho trong SAP...");
                Double remaining = CheckMatStock(weightTicket.getMatnrRef(), config.getwPlant(), weightTicket.getLgort(), weightTicket.getCharg());
                if (weightTicket.getGQty().doubleValue() > remaining) {
                    JOptionPane.showMessageDialog(rootPane, "K.L xuất lớn hơn K.L tồn kho!!!");
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
//            Date now = Calendar.getInstance().getTime();
            //get server time
            Date now = null;
            
            formatter.applyPattern(WeighBridgeApp.DATE_TIME_DISPLAY_FORMAT);//      
            WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
            try {
                Object rs = con.get_server_time();
                
                    if (rs instanceof Timestamp){
                     Timestamp time1 = (Timestamp)rs;
                          now = new Date(time1.getTime());
                     }else{
                       now = (Date)rs;
                      }
                    
                 } catch (Exception ex) {
                java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
            }
                     
// Move up 11/09/2012
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
                    if (weightTicket.getRegCategory() == 'O') {
                        JOptionPane.showMessageDialog(rootPane, "K.L nhập lớn hơn K.L xuất!");
                        txfOutQty.setValue(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        return null;
                    }
                } else {
                    result = dOut - dIn;
                    if (weightTicket.getRegCategory() == 'I') {
                        JOptionPane.showMessageDialog(rootPane, "K.L xuất lớn hơn K.L nhập!");
                        txfOutQty.setValue(null);
                        txtOutTime.setText(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        return null;
                    }
                }
                result = result / 1000d;
                if (result == 0) {
                    JOptionPane.showMessageDialog(rootPane, "Khối lượng hàng không hợp lệ!");
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
//                    qty = main_qty + free_qty;
//                    qty = Double.parseDouble(total_qty_goods.add(total_qty_free).toString());
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
                if ((outbDel == null || !bCompare)
                        && rbtPO.isSelected()
                        && purOrder != null) {
                    //                    
                    Material m = null;
                    try {
                        m = con.CheckPOSTO(purOrder.getMaterial());
                        bag = m.getBag();
                        btype = m.getXimang() != null && m.getXimang().equals("X") ? true : false;
                    } catch (Exception ex) {
                        java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    bCompare = (bag || btype);
                    if (bCompare && weightTicket.getRegItemQty() != null) {
                        qty = weightTicket.getRegItemQty().doubleValue();
                    }
                    //
                }
                if (bCompare) // end add
                /*
                if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF"))
                // Tuanna add 05/03/2013 for checking cement bag  
                || outbDel.getArktx().toUpperCase().indexOf("BAO")>=0         ) 
                 * 
                 */ {
                    Variant vari = new Variant();
                    VariantPK variPK = new VariantPK();
                    EntityManager entityManager = java.beans.Beans.isDesignTime() ? null : WeighBridgeApp.getApplication().getEm();
                    AppConfig lconfig = WeighBridgeApp.getApplication().getConfig();
                    variPK.setMandt(lconfig.getsClient().toString());
                    variPK.setWPlant(lconfig.getwPlant().toString());
                    variPK.setParam((outbDel != null && outbDel.getMatnr() != null) ? outbDel.getMatnr().toString() : "");
                    if (variPK.getParam().equals("") && purOrder != null
                            && purOrder.getMaterial() != null && !purOrder.getMaterial().isEmpty()) {
                        variPK.setParam(purOrder.getMaterial());
                    }
                    vari.setVariantPK(variPK);
                    vari = entityManager.find(Variant.class, vari.getVariantPK());
//                    if (!vari.getValue().equals("") || !vari.getValue1().equals("")) {
                      double valu = 0;
                        double valu1 = 0;
                        
                    if (vari != null) {
                      
                        if (!vari.getValue().equals("")) {
                            valu = Double.parseDouble(vari.getValue());
                        }
                        if (!vari.getValue1().equals("")) {
                            valu1 = Double.parseDouble(vari.getValue1());
                        }
//                    double upper = qty + (qty * (config.getB1Port() != null ? WeighBridgeApp.getApplication().getSapSetting().getWb1Tol().doubleValue() : config.getB2Port() != null ? WeighBridgeApp.getApplication().getSapSetting().getWb2Tol().doubleValue() : 0d));
//                    double lower = qty - (qty * (config.getB1Port() != null ? WeighBridgeApp.getApplication().getSapSetting().getWb1Tol().doubleValue() : config.getB2Port() != null ? WeighBridgeApp.getApplication().getSapSetting().getWb2Tol().doubleValue() : 0d));
                        double upper = qty + (qty * valu1) / 100;
                        double lower = qty - (qty * valu) / 100;
                                                
                        //   Query qLoading = (Query) entityManager.createNativeQuery("SELECT COUNT(cvalue) AS icount FROM jweighbridge.signal WHERE cvalue LIKE '02' AND wbid LIKE '1411TC'",String.class );
                        // Modified by Tuanna at TAFICO JSC 
                        // Purposed : Warning Maximum overloading qty. ( 30/10/2014 ) 
                         float klmax = 0 ;  
                         float ilock ; 
                         boolean block  = false ; 
                         
                        String sql ="";                         
                              sql = "call pGetMaxL ( ?,?,?)" ;                                 
                              Query q = (Query) entityManager.createNativeQuery(sql);
                              q.setParameter(1,config.getwPlant().toString().trim());
                              q.setParameter(2,config.getWbId().toString());
                              q.setParameter(3,weightTicket.getSoXe().toString().trim());
                             
                         List wts = q.getResultList();          
                         sql = "call pGetMaxLLock ( ?,?,?)" ;    
                         
                              Query q2 = (Query) entityManager.createNativeQuery(sql);
                             
                              q2.setParameter(1,config.getwPlant().toString().trim());
                              q2.setParameter(2,config.getWbId().toString());
                              q2.setParameter(3,weightTicket.getSoXe().toString().trim());
                        List wts2 = q2.getResultList();      
                        // JOptionPane.showMessageDialog(rootPane, wts.get(0).toString()) ; 
                        try {
                          klmax = Float.parseFloat(wts.get(0).toString()); 
                          ilock  = Float.parseFloat(wts2.get(0).toString()); 
                        }catch(Throwable cause ){ 
    
                                  block  = false ;  
                                  klmax  = -1 ; 
                                  
                        }
                         int ask  ;
                         boolean isKTC = true ; 
                         
                        if (block ==  false &&  klmax  == -1 && isKTC == false  ) 
                        {
                          //check weight 
                            String msg  = ""; 
                            msg = con.getMsg("6"); 
                        //    JOptionPane.showMessageDialog(rootPane,  msg ); //variant mesage --> Tuanna 
                            
                            ask = JOptionPane.showConfirmDialog(rootPane,msg
                                , "Thông báo ", JOptionPane.YES_NO_OPTION); 
                              if (ask  == JOptionPane.NO_OPTION)
                              {
                                 txfOutQty.setValue(null);
                                 txtOutTime.setText(null);
                                 txfGoodsQty.setValue(null);
                                 weightTicket.setGQty(null);
                                 btnAccept.setEnabled(false);
                                 // Tuanna add 14.01.2013 - Check logic when input value not match require condition  
                                 btnOScaleReset.setEnabled(true);
                                 return null;
                              }
                    
                        }
                        else 
                        {
                         if(result > klmax  &&  klmax >0   )  
                         {
                              JOptionPane.showMessageDialog(rootPane, "IF 111114:  Lưu ý ! Trọng lượng hàng = "  + result + "   vượt quá trọng tải cho phép  ( Max = " + klmax +" )") ; 
                           if(block )
                              {
                               txfOutQty.setValue(null);
                                txtOutTime.setText(null);
                                txfGoodsQty.setValue(null);
                                 weightTicket.setGQty(null);
                                 btnAccept.setEnabled(false);
                            // Tuanna add 14.01.2013 - Check logic when input value not match require condition  
                                btnOScaleReset.setEnabled(true);
                                 return null;
                              }
                                  
                          }  
                        }
                                                
                        boolean a , b ; 
                        a = b = true ;
                        
                        if ((lower <= result && result <= upper ) ) {
                            txfGoodsQty.setValue(result);
                            weightTicket.setGQty(new BigDecimal(result));
                        } else {
                            String msg  = ""; 
                            try{
                            msg = con.getMsg("5"); 
                            }catch(Exception ex){
                            msg = "Khối lượng ngoài giới hạn cho phép";
                            }
                            JOptionPane.showMessageDialog(rootPane,  msg ); //variant mesage --> Tuanna 
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
                        weightTicket.setGQty(new BigDecimal(result));
                    }
//                } else if (outbDel != null && ( outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") ) && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrXmxa())) {
//                    if (result <= qty) {
//                        txfGoodsQty.setValue(result);
//                        weightTicket.setGQty(new BigDecimal(result));
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Khối lượng vượt quá mức cho phép lấy!!!");
//                        txfGoodsQty.setValue(null);
//                        weightTicket.setGQty(null);
//                    }
                } else if (isSubContract() && weightTicket.getLgort() != null && weightTicket.getCharg() != null) {
                    setMessage("Đang kiểm tra tồn kho trong SAP..."); // checking stock --tuanna
                    Double remaining = CheckMatStock(weightTicket.getMatnrRef(), config.getwPlant(), weightTicket.getLgort(), weightTicket.getCharg());
                    if (result <= remaining) {
                        txfGoodsQty.setValue(result);
                        weightTicket.setGQty(new BigDecimal(result));
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "K.L xuất lớn hơn K.L tồn kho!!!");
                        txfOutQty.setValue(null);
                        txfGoodsQty.setValue(null);
                        weightTicket.setGQty(null);
                        btnOScaleReset.setEnabled(true);
                    }
                } else {
                    txfGoodsQty.setValue(result);
                    weightTicket.setGQty(new BigDecimal(result));
                }
            }
// End             
            if (isStage1()) {
                txfInQty.setValue(txfCurScale.getValue());
                txtInTime.setText(formatter.format(now));
                if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                    weightTicket.setFCreator(WeighBridgeApp.getApplication().getLogin().getUserPK().getId());
                } else {
                    weightTicket.setFCreator(WeighBridgeApp.getApplication().getCurrent_user());
                }
                weightTicket.setFScale(new BigDecimal(((Number) txfInQty.getValue()).doubleValue()));
                weightTicket.setFTime(now);
                lblIScale.setForeground(Color.black);

                //xu ly dua fscale vao details
                // 20120522_ fix logic_code in for_loop
                OutbDetailsV2 item = null;
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
                    weightTicket.setSCreator(WeighBridgeApp.getApplication().getLogin().getUserPK().getId());
                } else {
                    weightTicket.setSCreator(WeighBridgeApp.getApplication().getCurrent_user());
                }
                weightTicket.setSScale(new BigDecimal(((Number) txfOutQty.getValue()).doubleValue()));
                weightTicket.setSTime(now);
                lblOScale.setForeground(Color.black);

                //xu ly dua sscale vao details
                OutbDetailsV2 item = null;
                double remain = ((Number) txfCurScale.getValue()).doubleValue() - ((Number) txfInQty.getValue()).doubleValue();
                if (remain < 0) {
                    remain = remain * 1;
                }
                remain = remain / 1000;
                // 20120522_ fix logic_code in for_loop
                if (outDetails_lits.size() > 1) {
                    for (int i = 0; i < outDetails_lits.size(); i++) {
                        item = outDetails_lits.get(i);
                        // 20120522_ fix logic_code in dividing weight for promote_or_not_promote
                        /*if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                        if (item.getLfimg().compareTo(remain_qty_goods) == -1) {
                        item.setGoodsQty(item.getLfimg());
                        item.setOutScale(BigDecimal.valueOf(item.getInScale().doubleValue()+ item.getLfimg().doubleValue()));
                        remain_qty_goods = remain_qty_goods.subtract(item.getLfimg());
                        } else {
                        item.setGoodsQty(remain_qty_goods);
                        item.setOutScale(BigDecimal.valueOf(item.getInScale().doubleValue()+ item.getLfimg().doubleValue()));
                        }
                        } else {
                        item.setGoodsQty(item.getLfimg());
                        item.setOutScale(BigDecimal.valueOf(item.getInScale().doubleValue()+ item.getLfimg().doubleValue()));
                        }*/
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

// Move up 11/09/2012
            return null;  // return your result
        }

        @Override
        protected void finished() {
            if (txfCurScale.isEditable()) {
                weightTicket.setManual("X");
            } else {
                weightTicket.setManual(null);
            }
            setSaveNeeded(isValidated());
        }
    }

    private Double CheckMatStock(String matnr, String plant, String sloc, String batch) {
        Double remaining = 0d;

        MatAvailableBapi bapi = new MatAvailableBapi();
        bapi.setMaterial(matnr);
        bapi.setPlant(plant);
        bapi.setSloc(sloc);
        bapi.setBatch(batch);
        bapi.setUnit("TO");
        try {
            WeighBridgeApp.getApplication().getSAPSession().execute(bapi);
            List<MatAvailableStructure> stocks = bapi.getWmdvex();
            if (!stocks.isEmpty() && stocks.get(0).getCom_qty() != null) {
                remaining = stocks.get(0).getCom_qty().doubleValue();
            }
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

    private Object getGrDoMigoBapi(WeightTicket wt, OutbDel outbDel) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getOutbDelPK().getDelivNumb();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += wt.getSoRomooc();
        }
        
        GoodsMvtDoCreateBapi bapi = new GoodsMvtDoCreateBapi();
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();
        String tempWTID = weightTicket.getWeightTicketPK().getId();
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert
        
        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, 
                (outbDel != null && outbDel.getOutbDelPK() != null
                && outbDel.getOutbDelPK().getDelivNumb() != null 
                && !outbDel.getOutbDelPK().getDelivNumb().isEmpty() ? outbDel : null),
                outDetails_lits);
        bapi.setWeightticket(stWT); 
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert
        header.setDocDate(DateUtil.stripTime(wt.getSTime()));

        // header.setPstngDate(DateUtil.stripTime(wt.getSTime())); >> by Tuanna 11-06-2013 

        //  Tuanna modified 11.06.2013 -- for date -1 
// begin <<

        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        header.setPstngDate(DateUtil.stripTime(stime));
        // >> end of modified      

        if (outbDel == null) {
            header.setRefDocNo(wt.getDelivNumb());
        } else {
            header.setRefDocNo(doNum);
        }
        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getCmndBl());
        if (outbDel == null) {
            header.setHeaderText(wt.getDelivNumb());
        } else {
            header.setHeaderText(doNum);
        }

        GoodsMvtItemDoStructure tab_wa = new GoodsMvtItemDoStructure();
        List<GoodsMvtItemDoStructure> tab = new ArrayList<GoodsMvtItemDoStructure>();

        //get do details for current do
        OutbDetailsV2 item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getOutbDetailsV2PK().getDelivNumb().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);
        if (outbDel == null) {
            tab_wa.setDeliv_numb(wt.getDelivNumb());
            tab_wa.setDeliv_numb_to_search(wt.getDelivNumb());
        } else {
            tab_wa.setDeliv_numb(doNum);
            tab_wa.setDeliv_numb_to_search(doNum);
        }
        tab_wa.setDeliv_item(wt.getItem());
        tab_wa.setDeliv_item_to_search(wt.getItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        if (outbDel == null) {
            tab_wa.setEntry_qnt(wt.getGQty());
        } else {
            tab_wa.setEntry_qnt(kl_total);
        }
        tab_wa.setEntry_uom(wt.getUnit());
//        tab_wa.setEntry_uom_iso(wt.getUnit());
        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    private Object getGrPoMigoBapi(WeightTicket wt) {
        config = WeighBridgeApp.getApplication().getConfig();
        //  TODO: Sum QTY of wt GR posted on this plant, then calculate the remaining Qty of Main Item and free Item if Existed
        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += wt.getSoRomooc();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi();
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();

        header.setDocDate(DateUtil.stripTime(wt.getSTime()));
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null);
        bapi.setWeightticket(stWT);
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert
        //Begin modify lui ngay 12.06.2013 >> Tuanna at Tafico JSC
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;

        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }
        // End >> 


        header.setPstngDate(DateUtil.stripTime(stime));

        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getCmndBl());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        tab_wa.setPo_number(wt.getEbeln());
        tab_wa.setPo_item(wt.getItem());
        tab_wa.setMove_type("101");
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setGr_rcpt(wt.getSCreator());
        tab_wa.setEntry_qnt(wt.getGQty());
        tab_wa.setEntry_uom(wt.getUnit());
//        tab_wa.setEntry_uom_iso(wt.getUnit());

        if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
            tab_wa.setNo_more_gr("X");
        } else {
            tab_wa.setNo_more_gr(null);
        }
        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        if (purOrder != null && purOrder.getPoItemFree() != null) {
            
//            tab_wa.setPo_number(wt.getEbeln());
//            tab_wa.setPo_item(purOrder.getPoItemFree());
//            tab_wa.setMove_type("101");
//            tab_wa.setPlant(config.getwPlant());
//            tab_wa.setStge_loc(wt.getLgort());
//            tab_wa.setBatch(wt.getCharg());
//            tab_wa.setGr_rcpt(wt.getSCreator());
//            tab_wa.setEntry_qnt(wt.getGQty());
//            tab_wa.setEntry_uom(wt.getUnit());
//            tab_wa.setEntry_uom_iso(wt.getUnit());
//
//            if (wt.getNoMoreGr() != null && wt.getNoMoreGr() == '2') {
//                tab_wa.setNo_more_gr("X");
//            } else {
//                tab_wa.setNo_more_gr(null);
//            }
//            tab_wa.setMove_reas(wt.getMoveReas());
//            tab_wa.setItem_text(wt.getText());
//            tab.add(tab_wa);
        }

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    private Object getGi541MigoBapi(WeightTicket wt) {
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += wt.getSoRomooc();
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi(new GoodsMvtCodeStructure("04"));
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null);
        bapi.setWeightticket(stWT);
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert        
        //modify lui ngay 11/06/2013 >>Tuanna at TFC JSC
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        // << End of modified.
        header.setDocDate(DateUtil.stripTime(stime));
        header.setPstngDate(DateUtil.stripTime(stime));
//        header.setDocDate(DateUtil.stripTime(wt.getSTime()));
//        header.setPstngDate(DateUtil.stripTime(wt.getSTime()));
        header.setBillOfLading(plateCombine);
        header.setGrGiSlipNo(wt.getCmndBl());

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();



        tab_wa.setMaterial(wt.getMatnrRef());
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setMove_type("541");
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());
//        tab_wa.setEntry_uom_iso(wt.getUnit());

        tab_wa.setEntry_uom(wt.getUnit());
//        tab_wa.setGr_rcpt(wt.getSCreator());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setVendor(purOrder.getVendor());

        tab_wa.setNo_more_gr(null);

        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    private Object getGiMB1BBapi(WeightTicket wt) {
        config = WeighBridgeApp.getApplication().getConfig();
        String vendorNo = null;
        String headertxt = null;
        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += wt.getSoRomooc();


        }
        if (setting.getCheckTalp().booleanValue()) {
            Vehicle vehicle = entityManager.find(Vehicle.class, wt.getSoXe());
            vendorNo = vehicle.getTaAbbr();


            if (vendorNo.startsWith(
                    "00") && vendorNo.length() == 10) {
                vendorNo = vendorNo.substring(2);
            }
        }
        GoodsMvtPoCreateBapi bapi = new GoodsMvtPoCreateBapi(new GoodsMvtCodeStructure("04"));
        GoodsMvtHeaderStructure header = new GoodsMvtHeaderStructure();
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, null, null);
        bapi.setWeightticket(stWT);
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert        
        //modify lui ngay
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }
        header.setDocDate(DateUtil.stripTime(stime));
        header.setPstngDate(DateUtil.stripTime(stime));
//        header.setDocDate(DateUtil.stripTime(wt.getSTime()));
//        header.setPstngDate(DateUtil.stripTime(wt.getSTime()));
        header.setRefDocNo(weightTicket.getRecvPo());
        header.setGrGiSlipNo(wt.getCmndBl());
        if (vendorNo != null) {
            headertxt = plateCombine + "|" + vendorNo;
        } else {
            headertxt = plateCombine;
        }

        header.setHeaderText(headertxt);

        List<GoodsMvtItemPoStructure> tab = new ArrayList<GoodsMvtItemPoStructure>();
        GoodsMvtItemPoStructure tab_wa = new GoodsMvtItemPoStructure();

        tab_wa.setMaterial(wt.getMatnrRef());
        tab_wa.setPlant(config.getwPlant());
        tab_wa.setStge_loc(wt.getLgort());
        tab_wa.setBatch(wt.getCharg());
        tab_wa.setMove_type(wt.getMoveType());
        tab_wa.setMvt_ind(null);
        tab_wa.setEntry_qnt(wt.getGQty());
//        tab_wa.setEntry_uom_iso(wt.getUnit());
        tab_wa.setEntry_uom(wt.getUnit());

        tab_wa.setMoveMat(wt.getRecvMatnr());
        tab_wa.setMovePlant(wt.getRecvPlant());
        tab_wa.setMoveSLoc(wt.getRecvLgort());
        tab_wa.setMoveBatch(wt.getRecvCharg());

        tab_wa.setMove_reas(wt.getMoveReas());
        tab_wa.setItem_text(wt.getText());
        tab.add(tab_wa);

        bapi.setHeader(header);
        bapi.setItems(tab);
        return bapi;
    }

    private Object getDoCreate2PGI(WeightTicket wt, OutbDel outbDel) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getOutbDelPK().getDelivNumb();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        DOCreate2PGIBapi bapi = new DOCreate2PGIBapi();

        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += "|" + wt.getSoRomooc();
        }
        if (setting.getCheckTalp().booleanValue()) {
            Vehicle vehicle = entityManager.find(Vehicle.class, wt.getSoXe());

            if (vehicle != null) {
                
                bapi.setIdParnr(vehicle.getTaAbbr());
                
            }
        }
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(wt.getDelivNumb());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));

        wa.setKomue("X");
        wa.setWabuc("X");
    

        //modify lui ngay
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;

        /*
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
        cal.add(Calendar.DATE, -1);
        stime = cal.getTime();
        } else {
        stime = wt.getSTime();
        }
         * 
         */
        // Change by Tuanna at TFC JSC 03062013 

        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
//        wa.setWadat_ist(DateUtil.stripTime(wt.getSTime()));
//        wa.setWadat(DateUtil.stripTime(wt.getSTime()));
//        wa.setWauhr(DateUtil.stripDate(wt.getSTime()));
//        wa.setLfdat(DateUtil.stripTime(wt.getSTime()));
//        wa.setLfuhr(DateUtil.stripDate(wt.getSTime()));
        wa.setTraty("0004");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getTenTaiXe());
        bapi.setVbkok_wa(wa);

        //get do details for current do
        OutbDetailsV2 item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getOutbDetailsV2PK().getDelivNumb().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);

        List<OutbDeliveryCreateStoStructure> _StockTransItems = new ArrayList<OutbDeliveryCreateStoStructure>();
        if (wt.getRegCategory() == 'O'
                && wt.getRegItemText() != null
                && wt.getRegItemText().toLowerCase().indexOf("bao") >= 0
                && wt.getMatnrRef() != null) {
//                && wt.getMatnrRef().indexOf("1011304") >= 0) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), wt.getRegItemQty(), wt.getUnit()));
        } else if (outbDel == null) {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), wt.getGQty(), wt.getUnit()));
        } else {
            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), kl_total, wt.getUnit()));
        }
//        if (outbDel == null) {
//            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), wt.getGQty(), wt.getUnit()));
//          
//            
//        } else {
//            _StockTransItems.add(new OutbDeliveryCreateStoStructure(wt.getEbeln(), wt.getItem(), kl_total, wt.getUnit()));
//           /// theo a cho nay minh check dk la xi mang = X and bag =1 thi gan 
//        }
        bapi.setStockTransItems(_StockTransItems);
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits);
        bapi.setWeightticket(stWT);
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert
        List<VbpokStructure> tab = new ArrayList<VbpokStructure>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(wt.getDelivNumb());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(wt.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(wt.getMatnrRef());
        tab_wa.setWerks(config.getwPlant());
        tab_wa.setLgort(wt.getLgort());
        tab_wa.setCharg(wt.getCharg());
        tab_wa.setLianp("X");
        if (outbDel == null) {
//            if( wt.getRegCategory() == 'O' 
//               && wt.getRegItemText().toLowerCase().indexOf("bao") >=0
//                 && wt.getMatnrRef().indexOf("1011304") >=0  ) 
//            { 
//             //   int i = 1; 
//                 tab_wa.setPikmg(kl_total);
//                 tab_wa.setLfimg(kl_total);
//             }else 
//            {
            tab_wa.setPikmg(wt.getGQty());
            tab_wa.setLfimg(wt.getGQty());
//            }
        } else {
            tab_wa.setPikmg(kl_total);
            tab_wa.setLfimg(kl_total);
        }
        tab_wa.setVrkme(wt.getUnit());
        tab_wa.setMeins(wt.getUnit());
        tab.add(tab_wa);
        bapi.setVbpok_tab(tab);

        return bapi;
    }

    private Object getPgmVl02nBapi(WeightTicket wt, OutbDel outbDel) {
        String doNum = null;
        if (outbDel != null) {
            doNum = outbDel.getOutbDelPK().getDelivNumb();
        }
        config = WeighBridgeApp.getApplication().getConfig();
        String plateCombine = wt.getSoXe();
        if (wt.getSoRomooc() != null && !wt.getSoRomooc().trim().isEmpty()) {
            plateCombine += "|" + wt.getSoRomooc();
        }
        WsDeliveryUpdateBapi bapi = new WsDeliveryUpdateBapi();
        if (outbDel == null) {
            bapi.setDelivery(wt.getDelivNumb());
        } else {
            bapi.setDelivery(doNum);
        }
        bapi.setUpdate_picking("X");

        //get do details for current do
        OutbDetailsV2 item = null;
        BigDecimal kl = BigDecimal.ZERO;
        BigDecimal kl_km = BigDecimal.ZERO;
        BigDecimal kl_total = BigDecimal.ZERO;
        for (int i = 0; i < outDetails_lits.size(); i++) {
            item = outDetails_lits.get(i);
            if (item.getOutbDetailsV2PK().getDelivNumb().contains(doNum)) {
                if (item.getFreeItem() == null || item.getFreeItem().equals("")) {
                    kl = kl.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                } else {
                    kl_km = kl_km.add((item.getGoodsQty() != null) ? item.getGoodsQty() : BigDecimal.ZERO);
                }
            }
        }
        kl_total = kl.add(kl_km);

        if (wt.getPpProcord() != null && wt.getPpProcord().length() == 12) {
            bapi.setProc_ord_id(wt.getPpProcord());
            if (outbDel == null) {
                bapi.setYield(wt.getGQty());
                bapi.setAct1(setting.getBact1Val().multiply(wt.getGQty()));
                bapi.setAct2(setting.getBact2Val().multiply(wt.getGQty()));
                bapi.setAct3(setting.getBact3Val().multiply(wt.getGQty()));
                bapi.setAct4(setting.getBact4Val().multiply(wt.getGQty()));
            } else {
                bapi.setYield(kl_total);
                bapi.setAct1(setting.getBact1Val().multiply(kl_total));
                bapi.setAct2(setting.getBact2Val().multiply(kl_total));
                bapi.setAct3(setting.getBact3Val().multiply(kl_total));
                bapi.setAct4(setting.getBact4Val().multiply(kl_total));
            }
        }
        //{+20101203#01 set confirm quantity = goods qty on scale
        bapi.setYield(kl_total);
        //}+20101203#01
        VbkokStructure wa = new VbkokStructure();
        if (outbDel == null) {
            wa.setVbeln_vl(wt.getDelivNumb());
        } else {
            wa.setVbeln_vl(doNum);
        }
        wa.setKodat(DateUtil.stripTime(wt.getFTime()));
        wa.setKouhr(DateUtil.stripDate(wt.getFTime()));
        wa.setKomue("X");
        wa.setWabuc("X");

        //modify lui ngay
        Calendar cal = Calendar.getInstance();
        cal.setTime(wt.getSTime());
        Date stime = null;
        
        /*
        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1) && rbtOutward.isSelected()) {
        cal.add(Calendar.DATE, -1);
        stime = cal.getTime();
        } else {
        stime = wt.getSTime();
        }
         * 
         */
        // change by Tuanna 03062013

        if (timeFrom <= cal.get(Calendar.HOUR_OF_DAY) && cal.get(Calendar.HOUR_OF_DAY) <= (timeTo - 1)) {
            cal.add(Calendar.DATE, -1);
            stime = cal.getTime();
        } else {
            stime = wt.getSTime();
        }

        wa.setWadat_ist(DateUtil.stripTime(stime));
        wa.setWadat(DateUtil.stripTime(stime));
        wa.setWauhr(DateUtil.stripDate(stime));
        wa.setLfdat(DateUtil.stripTime(stime));
        wa.setLfuhr(DateUtil.stripDate(stime));
//        wa.setWadat_ist(DateUtil.stripTime(wt.getSTime()));
//        wa.setWadat(DateUtil.stripTime(wt.getSTime()));
//        wa.setWauhr(DateUtil.stripDate(wt.getSTime()));
//        wa.setLfdat(DateUtil.stripTime(wt.getSTime()));
//        wa.setLfuhr(DateUtil.stripDate(wt.getSTime()));
        wa.setTraty("0004");
        wa.setTraid(plateCombine);
        wa.setLifex(wt.getTenTaiXe());
        bapi.setVbkok_wa(wa);

        List<VbpokStructure> tab = new ArrayList<VbpokStructure>();
        VbpokStructure tab_wa = new VbpokStructure();
        if (outbDel == null) {
            tab_wa.setVbeln_vl(wt.getDelivNumb());
        } else {
            tab_wa.setVbeln_vl(doNum);
        }
        tab_wa.setPosnr_vl(wt.getItem());
        tab_wa.setVbeln(tab_wa.getVbeln_vl());
        tab_wa.setPosnn(tab_wa.getPosnr_vl());
        tab_wa.setMatnr(wt.getMatnrRef());
        tab_wa.setWerks(config.getwPlant());
        tab_wa.setLgort(wt.getLgort());
        tab_wa.setCharg(wt.getCharg());
        tab_wa.setLianp("X");
        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF")) && wt.getMatnrRef().equalsIgnoreCase(setting.getMatnrPcb40())) {
            tab_wa.setPikmg(outbDel.getLfimg());
            tab_wa.setLfimg(outbDel.getLfimg());
        } else {
            /*{-20101203#01 comment
            tab_wa.setPikmg(wt.getGQty());
            tab_wa.setLfimg(wt.getGQty());
            }-20101203#01 comment */
            //{+20101203#01 freeGoods qty
            BigDecimal qty = new BigDecimal(0);
            BigDecimal qtyfree = new BigDecimal(0);
            try {
                qtyfree = (BigDecimal) outbDel.getFreeQty();
            } catch (Exception ex) {
                qtyfree = new BigDecimal(0);
            }
            if (qtyfree == null) {
                qtyfree = new BigDecimal(0);
            }
            qty = wt.getGQty().subtract(qtyfree);
            tab_wa.setPikmg(kl);
            tab_wa.setLfimg(kl);
            //}+20101203#01 freeGoods qty
        }
//      {[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - begin of insert

        GoodsMvtWeightTicketStructure stWT = fillWTStructure(weightTicket, outbDel, outDetails_lits);
        stWT.setGQTY(tab_wa.getLfimg());
        bapi.setWeightticket(stWT);
        
//      }[M001-DungDang-28/06/2013] - Fixing bug post double GoodsMovement - end of insert        
        tab_wa.setVrkme(wt.getUnit());
        tab_wa.setMeins(outbDel.getMeins());
        tab.add(tab_wa);
        //{+20101203#01 add free goods item
        VbpokStructure tab_wa_f = new VbpokStructure();

        //tab_wa_f.setMatnr(wt.getMatnrRef());
        tab_wa_f.setWerks(config.getwPlant());
        tab_wa_f.setLgort(wt.getLgort());
        tab_wa_f.setCharg(wt.getCharg());
        tab_wa_f.setLianp("X");
        tab_wa_f.setVrkme(wt.getUnit());
        tab_wa_f.setMeins(outbDel.getMeins());

        if (outbDel.getDelivItemFree() != null && (outbDel.getDelivItemFree() == null ? "" != null : !outbDel.getDelivItemFree().equals(""))) {
            if (outbDel == null) {
                tab_wa_f.setVbeln_vl(wt.getDelivNumb());
            } else {
                tab_wa_f.setVbeln_vl(doNum);
            }
            tab_wa_f.setMatnr(outbDel.getMatnrFree());
            tab_wa_f.setPosnr_vl(outbDel.getDelivItemFree());
            tab_wa_f.setVbeln(tab_wa_f.getVbeln_vl());
            tab_wa_f.setPosnn(tab_wa_f.getPosnr_vl());
            //if (outbDel != null && ( outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") ) && wt.getMatnrRef().equalsIgnoreCase(setting.getMatnrPcb40())) {
            tab_wa_f.setPikmg(outbDel.getFreeQty());
            tab_wa_f.setLfimg(outbDel.getFreeQty());
            //} else {
            //    tab_wa.setPikmg(wt.getGQty());
            //    tab_wa.setLfimg(wt.getGQty());
            //}
            tab.add(tab_wa_f);
        }
        //}+20101203#01 add free goods item

        bapi.setVbpok_tab(tab);
        return bapi;
    }

    private DefaultComboBoxModel getReasonModel() {
        config = WeighBridgeApp.getApplication().getConfig();
        Movement mvt = null;

        try {
            TypedQuery<Movement> tMvtQ = entityManager.createNamedQuery("Movement.findByMandtBwartSpras", Movement.class);
            tMvtQ.setParameter(
                    "mandt", config.getsClient());
            tMvtQ.setParameter(
                    "bwart", "101");
            tMvtQ.setParameter(
                    "spras", WeighBridgeApp.getApplication().getLogin().getLanguP().toString());
            mvt = tMvtQ.getSingleResult();
        } catch (NoResultException ex) {
            MvtGetDetailBapi bMvt = new MvtGetDetailBapi(config.getsClient(), "101");
            WeighBridgeApp.getApplication().getSAPSession().execute(bMvt);
            mvt = new Movement(new MovementPK(config.getsClient(), bMvt.getItem().getBwart()), bMvt.getItem().getSpras());
            mvt.setBtext(bMvt.getItem().getBtext());
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            entityManager.persist(mvt);
            entityManager.getTransaction().commit();
            entityManager.clear();


        }

        TypedQuery<Reason> tReasonQ = entityManager.createNamedQuery("Reason.findByMandtBwart", Reason.class);
        tReasonQ.setParameter(
                "mandt", config.getsClient());
        tReasonQ.setParameter(
                "bwart", mvt.getMovementPK().getBwart());
        List<Reason> reasons = tReasonQ.getResultList();


        if (reasons.isEmpty()) {
            MvtReasonsGetListBapi bReason = new MvtReasonsGetListBapi(config.getsClient(), "101");
            WeighBridgeApp.getApplication().getSAPSession().execute(bReason);
            List<MvtReasonsGetListStructure> brReasons = bReason.getTdMvtsReasons();
            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }
            for (MvtReasonsGetListStructure r : brReasons) {
                Reason reason = new Reason(r.getMandt(), r.getBwart(), r.getGrund());
                reason.setGrtxt(r.getGrtxt());
                entityManager.persist(reason);
                reasons.add(reason);
            }
            entityManager.getTransaction().commit();
            entityManager.clear();
        }


        return new DefaultComboBoxModel(reasons.toArray());
    }

    private void printWT(WeightTicket wt, boolean reprint) {
        config = WeighBridgeApp.getApplication().getConfig();
//      JasperReport jasperReport;
        OutbDel item = null;
        OutbDel outbDel = null;
        ximang = null;
        Boolean bag_tmp = false;
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        WeightTicketJpaController con = new WeightTicketJpaController(entityManager);
        Material m = null;
        try {
            m = con.CheckPOSTO(wt.getMatnrRef());
        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(WeightTicketView.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (m != null) {
            if (m.getXimang() == null) {
                ximang = null;
            } else if (m.getXimang() != null || !m.getXimang().equals("")) {
                ximang = "X";
            }
            bag_tmp = m.getBag();
        }
        try {
            Map<String, Object> map = new HashMap<String, Object>();
//            jasperReport = JasperCompileManager.compileReport("./rpt/WeightTicket.jrxml");
            Long bags = null;
            if (outbDel_list == null || outbDel_list.isEmpty() || rbtMisc.isSelected() || rbtPO.isSelected()) {
                // can posto xi mang 
                map.put("P_MANDT", WeighBridgeApp.getApplication().getConfig().getsClient());
                map.put("P_WPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
                map.put("P_ID", wt.getWeightTicketPK().getId());
                map.put("P_DAYSEQ", wt.getWeightTicketPK().getSeqByDay());
                map.put("P_REPRINT", reprint);
                map.put("P_ADDRESS", config.getRptId());
                if (bag_tmp && (wt.getDissolved() == null || !wt.getDissolved())) {
                    Double tmp;
                    // Double tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                    /*
                    if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013                           
                    {
                        tmp =  wt.getGQty() != null ? (((wt.getGQty().doubleValue()) * 1000d) / 40d) : 0;
                    } else {
                        tmp = wt.getGQty() != null ?(((wt.getGQty().doubleValue()) * 1000d) / 50d) : 0;
                    }
                     * */
                     
                     if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013                           
                        {
                            tmp = ((wt.getRegItemQty().doubleValue()) * 1000d) / 40d;
                        } else {
                            tmp = ((wt.getRegItemQty().doubleValue()) * 1000d) / 50d;
                        }
                     
//                    if ((tmp == null || tmp == 0) && wt.getFScale() != null) {
//                        if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013                           
//                        {
//                            tmp = ((wt.getFScale().doubleValue())) / 40d;
//                        } else {
//                            tmp = ((wt.getFScale().doubleValue())) / 50d;
//                        }
//                    }
                    if ((tmp == null || tmp == 0) && wt.getRegItemQty() != null) {
                        if (wt.getMatnrRef() != null && wt.getMatnrRef().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K 27.04.2013                           
                        {
                            tmp = ((wt.getRegItemQty().doubleValue()) * 1000d) / 40d;
                        } else {
                            tmp = ((wt.getRegItemQty().doubleValue()) * 1000d) / 50d;
                        }
                    }
                    bags = Math.round(tmp);
                    if (bags != null) {
                        map.put("P_PCB40BAG", bags);
                    }
                }
//                map.put("P_DEL_NUM", outbDel.getOutbDelPK().getDelivNumb());
                String reportName1 = "";
                if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                    reportName1 = "./rpt/rptBT/WeightTicket.jasper";
                } else {
                    reportName1 = "./rpt/rptPQ/WeightTicket.jasper";
                }
                Class.forName((String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_DRIVER));
                Connection jdbcCon = DriverManager.getConnection(
                        (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_URL),
                        (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_USER),
                        (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_PASSWORD));
                JasperPrint jasperPrint = JasperFillManager.fillReport(reportName1, map, jdbcCon);
                JasperViewer jv = new JasperViewer(jasperPrint, false);
                jv.setVisible(true);

            } else {
                for (int i = 0; i < outbDel_list.size(); i++) {
                    item = outbDel_list.get(i);
                    OutbDetailsV2 out_detail = null;
                    BigDecimal sscale = BigDecimal.ZERO;
                    BigDecimal gqty = BigDecimal.ZERO;
                    BigDecimal lfimg_ori = BigDecimal.ZERO;
                    for (int k = 0; k < outDetails_lits.size(); k++) {
                        out_detail = outDetails_lits.get(k);
                        if (out_detail.getOutbDetailsV2PK().getDelivNumb().contains(item.getOutbDelPK().getDelivNumb())) {
                            if (out_detail.getLfimgOri() != null) {
                                lfimg_ori = lfimg_ori.add(out_detail.getLfimgOri());
                            }
//                            if (out_detail.getOutScale() != null && out_detail.getGoodsQty() != null) {
//                                sscale = sscale.add(out_detail.getOutScale());
//                                gqty = gqty.add(out_detail.getGoodsQty());
//                            }
                        }
                    }
                    map.put("P_PAGE", "Trang ".concat(String.valueOf(i + 1).concat("/").concat(String.valueOf(outbDel_list.size()))));
                    map.put("P_TOTAL_QTY_ORI", String.valueOf(lfimg_ori));
                    if (wt.getFScale() != null) {
                        outbDel = item;
                    }
                    if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF")) && wt.getMatnrRef().equalsIgnoreCase(setting.getMatnrPcb40())) {
                        //Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
//                    Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
//                    bags = tmp.longValue();
                    }
                    map.put("P_MANDT", WeighBridgeApp.getApplication().getConfig().getsClient());
                    map.put("P_WPlant", WeighBridgeApp.getApplication().getConfig().getwPlant());
                    map.put("P_ID", wt.getWeightTicketPK().getId());
                    map.put("P_DAYSEQ", wt.getWeightTicketPK().getSeqByDay());
                    map.put("P_REPRINT", reprint);
                    map.put("P_ADDRESS", config.getRptId());
                    map.put("P_DEL_NUM", outbDel.getOutbDelPK().getDelivNumb());
                    //map.put("P_SSCALE", sscale); //comment out - hauld
                    if (wt.getSScale() != null) {
                        BigDecimal n_1000 = new BigDecimal(1000);
                        sscale = wt.getSScale().divide(n_1000);
                    }
                    if (wt.getGQty() != null) {
                        gqty = wt.getGQty();
                    }
                    if (reprint && wt.getPosted() == 0) {
                        sscale = BigDecimal.ZERO;
                        gqty = BigDecimal.ZERO;
                    }

                    map.put("P_SSCALE", sscale);
                    map.put("P_GQTY", gqty);
                    BigDecimal n_0 = new BigDecimal(0);
                    map.put("P_STAGE1", isStage1());

                    //map.put("P_STAGE2", isStage2());
                    //if(!isStage2()){
                    if (wt.getFScale() != null && wt.getSScale() != null && wt.getSScale() != n_0) {
                        map.put("P_STAGE2", true);
                    } else {
                        map.put("P_STAGE2", false);
                    }
                    //}                        

                    if (outbDel.getFreeQty() != null) {
                        map.put("P_TOTAL_QTY", String.valueOf(outbDel.getLfimg().add(outbDel.getFreeQty())));
                        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") || outbDel.getLfart().equalsIgnoreCase("NL")) && bag_tmp) {
                            Double tmp;
                            // Double tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                            if (outbDel.getMatnr().equalsIgnoreCase("000000101130400008")) // Tuanna - crazy lắm lun hix ai chơi hardcode  for bag 40K 27.04.2013
                            //  Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 40d;
                            {
                                tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 40d;
                            } else //  Double tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
                            {
                                tmp = ((outbDel.getLfimg().doubleValue() + outbDel.getFreeQty().doubleValue()) * 1000d) / 50d;
                            }
                            bags = Math.round(tmp);
                        }
                    } else {
                        map.put("P_TOTAL_QTY", String.valueOf(outbDel.getLfimg()));
                        Double tmp;
                        if (outbDel != null && (outbDel.getLfart().equalsIgnoreCase("LF") || outbDel.getLfart().equalsIgnoreCase("ZTLF") || outbDel.getLfart().equalsIgnoreCase("NL")) && bag_tmp) {
                            if (outbDel.getMatnr().equalsIgnoreCase("000000101130400008")) // Tuanna - for bag 40K  27.04.2013
                            {
                                tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 40d;
                            } else {
                                tmp = (outbDel.getLfimg().doubleValue() * 1000d) / 50d;
                            }

                            bags = Math.round(tmp);
                        }

                    }

                    if (bags != null) {
                        map.put("P_PCB40BAG", bags);
                    }
                    if (outbDel.getMatDoc() != null) {
                        map.put("P_MAT_DOC", outbDel.getMatDoc());
                    }

                    String reportName = null;
                    String path = "";
                    if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                        path = "./rpt/rptBT/";  // ->> DO cai nay ne e
                    } else {
                        path = "./rpt/rptPQ/";
                    }
                    if (rbtMisc.isSelected() || rbtPO.isSelected()) {
                        reportName = path.concat("WeightTicket.jasper");
                    } else {
                        reportName = path.concat("WeightTicket_NEW.jasper");
                    }
                    Class.forName((String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_DRIVER));
                    Connection jdbcCon = DriverManager.getConnection(
                            (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_URL),
                            (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_USER),
                            (String) JpaProperties.getProperties().get(PersistenceUnitProperties.JDBC_PASSWORD));
                    JasperPrint jasperPrint = JasperFillManager.fillReport(reportName, map, jdbcCon);
                    JasperViewer jv = new JasperViewer(jasperPrint, false);
                    jv.setVisible(true);
                }
            }

        } catch (Exception ex) {
            logger.error(null, ex);
            JOptionPane.showMessageDialog(rootPane, ex);
        }
    }

    private void getSAPMatData(DocumentEvent e) {
        if (weightTicket == null) {
            return;
        }
        try {
            String val = e.getDocument().getText(0, e.getDocument().getLength()).trim();
            if (!val.isEmpty()) {
                material = SAP2Local.getMaterialDetail(val);
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

            if (rbtMvt311.isSelected() && (weightTicket.getRecvLgort() == null || weightTicket.getRecvMatnr() == null)) {
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
//        if(cbxSLoc.getSelectedIndex() >= -1){
//        if(weightTicket.getLgort() == null){
//        cbxSLoc.setEnabled(true);
//        }
//        }
        txtGRText.setEnabled(true);
        if (outbDel != null) {
            if (isStage2() && outbDel.getMatnr() != null
                    && outbDel.getMatnr().equalsIgnoreCase(setting.getMatnrXmxa()) && (txtCementDesc.getText().trim() == null || txtCementDesc.getText().trim().equals(""))) {
                bNiemXa = false;
            }
            if (bNiemXa) {
                lblCementDesc.setForeground(Color.black);
            } else {
                lblCementDesc.setForeground(Color.red);
            }
        }
        result = (bMisc || bPO || bMB1B || bMvt311) && bScale && bSLoc && bBatch && bNiemXa && (isStage1() || isStage2() || (!isStage1() && !isStage2() && weightTicket != null && (weightTicket.getPosted() == 0)));
        return result;
    }
    public GoodsMvtWeightTicketStructure fillWTStructure(WeightTicket wt,
            OutbDel od, List<OutbDetailsV2> od_v2_list) {
        GoodsMvtWeightTicketStructure stWT = null;
        if (wt == null || wt.getWeightTicketPK() == null) {
            return stWT;
        }
        String tempWTID = wt.getWeightTicketPK().getId().trim();        
        tempWTID = tempWTID.concat(Conversion_Exit.Conv_output_num(String.valueOf(weightTicket.getWeightTicketPK().getSeqByDay()),3));
        stWT = new GoodsMvtWeightTicketStructure(weightTicket.getWeightTicketPK().getWPlant(),
                        weightTicket.getWbId(),
                        tempWTID);
                
//          outb_details_v2
        OutbDetailsV2 od_v2 = null;
        if (od != null && od_v2_list != null) {
            for (OutbDetailsV2 od_v2_tmp: od_v2_list) {
                if (od_v2_tmp.getOutbDetailsV2PK().getDelivNumb() != null 
                        && od_v2_tmp.getOutbDetailsV2PK().getDelivNumb().equals(od.getOutbDelPK().getDelivNumb())) {
                    od_v2 = od_v2_tmp;
                    break;
                }
            }
//            od_v2 = od_v2_list.get(0);                    
        }                
        
        stWT.setCAT_TYPE(String.valueOf(wt.getRegCategory()));
//        stWT.setCREATE_DATE(new Date());
//        stWT.setCREATE_TIME(null);
//        stWT.setClient(title);
//        stWT.setDOC_YEAR(wt_ID);
        stWT.setDO_WT(wt.getDelivNumb());
        stWT.setDO_NUMBER(od != null && od.getOutbDelPK() != null 
                && od.getOutbDelPK().getDelivNumb() != null ? 
                od.getOutbDelPK().getDelivNumb() : "");
        stWT.setDRIVERID(wt.getCmndBl());
        stWT.setDRIVERN(wt.getTenTaiXe());
        stWT.setFDATE(wt.getFTime());
        stWT.setFSCALE(wt.getFScale());
        stWT.setFTIME(wt.getFTime());
        stWT.setGQTY_WT(wt.getGQty());
        stWT.setGQTY(wt.getGQty());
        stWT.setKUNNR(wt.getKunnr());
        stWT.setLIFNR(wt.getAbbr());
//        stWT.setMATDOC(title);
        stWT.setMATID(wt.getMatnrRef());
        stWT.setMATNAME(wt.getRegItemText());
        stWT.setMVT_TYPE(wt.getMoveType());
        if ((stWT.getMVT_TYPE() == null || stWT.getMVT_TYPE().isEmpty()) 
            && od != null && od.getBwart() != null) {
            stWT.setMVT_TYPE(od.getBwart());                    
        }
        stWT.setNTEXT(wt.getText());
        stWT.setPO_NUMBER(wt.getEbeln());
        stWT.setREGQTY_WT(wt.getRegItemQty());
        stWT.setREGQTY(od != null ? od.getLfimg() : BigDecimal.ZERO);
        stWT.setSALEDT(od_v2 != null ? od_v2.getBzirk() : "");
        stWT.setSDATE(wt.getSTime());
        stWT.setSHIPTYPE(od != null ? od.getTraty() : "");
        stWT.setSLOC(wt.getLgort());
        stWT.setSSCALE(wt.getSScale());
        stWT.setSTIME(wt.getSTime());
        stWT.setTRANSID(wt.getSoXe());
        stWT.setUNIT(wt.getUnit());
        stWT.setUSERNAME(wt.getSCreator());
        stWT.setVTYPE(od != null ? od.getBwtar() : "");
        stWT.setBatch(od != null ? od.getCharg(): "");
        stWT.setLfart(od != null ? od.getLfart() : "");
        
        return stWT;
    }

    @Action
    public Task selectInternal() {
        return new SelectInternalTask(org.jdesktop.application.Application.getInstance(com.gcs.wb.WeighBridgeApp.class));
    }

    private class SelectInternalTask extends org.jdesktop.application.Task<Object, Void> {
        SelectInternalTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to SelectInternalTask fields, here.
            super(app);
        }
        @Override protected Object doInBackground() {
            if(chkInternal.isSelected()) {
                rbtInward.setEnabled(true);
                rbtOutward.setEnabled(true);
            } else {
                rbtInward.setEnabled(false);
                rbtOutward.setEnabled(false);
            }
            return null;  // return your result
        }
        @Override protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
        }
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
    private javax.swing.JComboBox cbxMaterial;
    private javax.swing.JComboBox cbxReason;
    private javax.swing.JComboBox cbxSLoc;
    private javax.swing.JComboBox cbxVendorLoading;
    private javax.swing.JComboBox cbxVendorTransport;
    private javax.swing.JCheckBox chkDissolved;
    private javax.swing.JCheckBox chkInternal;
    private javax.persistence.EntityManager entityManager;
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
    private javax.swing.JLabel lblMaterial;
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
    private com.gcs.wb.jpa.entity.OutbDel outbDel;
    private javax.swing.JPanel pnCurScale;
    private javax.swing.JPanel pnCurScaleData;
    private javax.swing.JPanel pnScaleData;
    private javax.swing.JPanel pnWTFilter;
    private javax.swing.JPanel pnWTicket;
    private com.gcs.wb.jpa.entity.PurOrder purOrder;
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
    private List<OutbDel> outbDel_list = new ArrayList<OutbDel>();
    private List<OutbDetailsV2> outDetails_lits = new ArrayList<OutbDetailsV2>();
    private String wt_ID = null;
    private boolean flag_fail = false;
    private int timeFrom;
    private int timeTo;
    private String ximang = null;
    // </editor-fold>
}
