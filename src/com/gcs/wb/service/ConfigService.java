/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.fazecast.jSerialComm.SerialPort;
import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.base.serials.SerialHelper;
import java.util.HashSet;

import com.gcs.wb.base.enums.ParityEnum;
import com.gcs.wb.base.util.Base64_Utils;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.model.AppConfig;
import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author THANGPT
 */
public class ConfigService {

    public DefaultComboBoxModel getPortModel() {
        DefaultComboBoxModel result = new DefaultComboBoxModel();
        HashSet<SerialPort> ports = SerialHelper.getAvailableSerialPorts();
        for (SerialPort port : ports) {
            result.addElement(port.getSystemPortName());
        }
        return result;
    }

    public boolean validateForm(JTextField txtDBHost, JTextField txtDBName,
            JTextField txtDBUsr, JPasswordField txtDBPwd, JTextField txtHost, JTextField txtPlant,
            JLabel lblDBHost, JLabel lblDBName,
            JLabel lblDBUsr, JLabel lblDBPwd,
            JLabel lblHost, JLabel lblSId, JLabel lblDClient, JLabel lblPort1,
            JLabel lblPort2, JLabel lblSpeed1, JLabel lblSpeed2, JLabel lblPlant,
            JFormattedTextField txfSNo, JFormattedTextField txfDClient,
            JComboBox cbxPort1, JComboBox cbxPort2, JComboBox cbxSpeed1, JComboBox cbxSpeed2) {
        boolean //DB Checks
                bDBHost = false, bDBName = false, bDBUsr = false, bDBPwd = false,
                //SAP Checks
                bSHost = false, bSNo = false, bSClient = false,
                //Bridge 1 Checks,
                bCOM1 = false, bSpeed1 = false,
                //Bridge 1 Checks,
                bCOM2 = false, bSpeed2 = false,
                bWPlant = false;

        bDBHost = !(txtDBHost.getText().trim().length() == 0);
        if (bDBHost) {
            lblDBHost.setForeground(Color.BLACK);
        } else {
            lblDBHost.setForeground(Color.RED);
        }

        bDBName = !(txtDBName.getText().trim().length() == 0);
        if (bDBName) {
            lblDBName.setForeground(Color.BLACK);
        } else {
            lblDBName.setForeground(Color.RED);
        }

        bDBUsr = !(txtDBUsr.getText().trim().length() == 0);
        if (bDBUsr) {
            lblDBUsr.setForeground(Color.BLACK);
        } else {
            lblDBUsr.setForeground(Color.RED);
        }

        bDBPwd = !((new String(txtDBPwd.getPassword())).trim().length() == 0);
        if (bDBPwd) {
            lblDBPwd.setForeground(Color.BLACK);
        } else {
            lblDBPwd.setForeground(Color.RED);
        }

        bSHost = !(txtHost.getText().trim().length() == 0);
        if (bSHost) {// && checkString(txtHost.getText().trim())) {
            lblHost.setForeground(Color.black);
        } else {
            lblHost.setForeground(Color.red);
        }

        bSNo = !(txfSNo.getText().trim().length() == 0);
        if (bSNo) {
            lblSId.setForeground(Color.black);
        } else {
            lblSId.setForeground(Color.red);
        }

        bSClient = !(txfDClient.getText().trim().length() == 0);
        if (bSClient) {
            lblDClient.setForeground(Color.black);
        } else {
            lblDClient.setForeground(Color.red);
        }

        bCOM1 = !(cbxPort1.getSelectedItem() instanceof String && (cbxPort1.getSelectedItem() == null || ((String) cbxPort1.getSelectedItem()).isEmpty()));
        if (bCOM1) {
            lblPort1.setForeground(Color.black);
        } else {
            lblPort1.setForeground(Color.red);
        }

        bCOM2 = !(cbxPort2.getSelectedItem() instanceof String && (cbxPort2.getSelectedItem() == null || ((String) cbxPort2.getSelectedItem()).isEmpty()));
        if (bCOM2) {
            lblPort2.setForeground(Color.black);
        } else {
            lblPort2.setForeground(Color.red);
        }

        bSpeed1 = !(cbxSpeed1.getSelectedItem() == null);
        if (bSpeed1) {
            lblSpeed1.setForeground(Color.black);
        } else {
            lblSpeed1.setForeground(Color.red);
        }

        bSpeed2 = !(cbxSpeed2.getSelectedItem() == null);
        if (bSpeed2) {
            lblSpeed2.setForeground(Color.black);
        } else {
            lblSpeed2.setForeground(Color.red);
        }

        bWPlant = !(txtPlant.getText().trim().length() == 0);
        if (bWPlant) {
            lblPlant.setForeground(Color.black);
        } else {
            lblPlant.setForeground(Color.red);
        }

        boolean bB1 = bCOM1 && bSpeed1, bB2 = bCOM2 && bSpeed2;

        return (bDBHost && bDBName && bDBUsr && bDBPwd
                && bSHost && bSNo && bSClient
                && (bB1 || bB2)
                && bWPlant);
    }

    public AppConfig objMapping(AppConfig config, JTextField txtWBID, JTextField txtDBHost, JTextField txtDBName
            , JTextField txtDBUsr, JPasswordField txtDBPwd, JTextField txtHost, JTextField txtRString, JFormattedTextField txfSNo
            , JFormattedTextField txfDClient,JComboBox cbxPort1, JComboBox cbxPort2, JComboBox cbxSpeed1, JComboBox cbxSpeed2,
            JComboBox cbxDataBits1, JComboBox cbxDataBits2, JComboBox cbxStopBits1, JComboBox cbxPControl1, JCheckBox chbMettler1
            , JComboBox cbxStopBits2, JComboBox cbxPControl2, JCheckBox chbMettler2, JTextField txtPlant) {
        if (config == null) {
            config = new AppConfig();
            config.setConfiguration(new Configuration());
        }
        Configuration configuration = config.getConfiguration();
        
        String wbId = txtWBID.getText();
        wbId = wbId == null || wbId.trim().length() == 0 ? "" : wbId.trim();
        String dbHost = txtDBHost.getText();
        dbHost = dbHost == null || dbHost.trim().length() == 0 ? "" : dbHost.trim();
        String dbName = txtDBName.getText();
        dbName = dbName == null || dbName.trim().length() == 0 ? "" : dbName.trim();
        String dbUsr = txtDBUsr.getText();
        dbUsr = dbUsr == null || dbUsr.trim().length() == 0 ? "" : dbUsr.trim();
        String dbPwd = new String(txtDBPwd.getPassword());
        dbPwd = dbPwd == null || dbPwd.trim().length() == 0 ? "" : dbPwd.trim();
        configuration.setWbId(Base64_Utils.decodeNTimes(wbId));
        config.setDbHost(Base64_Utils.decodeNTimes(dbHost));
        config.setDbName(Base64_Utils.decodeNTimes(dbName));
        config.setDbUsername(Base64_Utils.decodeNTimes(dbUsr));
        config.setDbPassword(Base64_Utils.decodeNTimes(dbPwd));

        String sHost = txtHost.getText();
        sHost = sHost == null || sHost.trim().length() == 0 ? "" : sHost;
        String sRoute = txtRString.getText();
        sRoute = sRoute == null || sRoute.trim().length() == 0 ? "" : sRoute;
        String sNo = txfSNo.getValue() == null || txfSNo.getValue().toString() == null
                || txfSNo.getValue().toString().trim().length() == 0 ? "" : txfSNo.getValue().toString().trim();
        String sDClient = txfDClient.getValue() == null || txfDClient.getValue().toString() == null
                || txfDClient.getValue().toString().trim().length() == 0 ? "" : txfDClient.getValue().toString().trim();
        configuration.setSapHost(Base64_Utils.decodeNTimes(sHost));
        configuration.setSapRouteString(Base64_Utils.decodeNTimes(sRoute));
        configuration.setSapSystemNumber(Base64_Utils.decodeNTimes(sNo));
        configuration.setSapClient(Base64_Utils.decodeNTimes(sDClient));

        Object port1 = cbxPort1.getSelectedItem();
        port1 = port1 == null || port1.toString().trim().length() == 0 ? null : port1.toString().trim();
        Integer speed1 = 0;
        if (port1 == null) {
            speed1 = null;
        } else if (cbxSpeed1.getSelectedItem() instanceof Short) {
            speed1 = ((Short) cbxSpeed1.getSelectedItem()).intValue();
        } else if (cbxSpeed1.getSelectedItem() instanceof Integer) {
            speed1 = (Integer) cbxSpeed1.getSelectedItem();
        } else if (cbxSpeed1.getSelectedItem() instanceof String) {
            try {
                speed1 = Integer.valueOf(String.valueOf(cbxSpeed1.getSelectedItem()));
            } catch (NumberFormatException ex) {
                speed1 = null;
            }
        } else if (cbxSpeed1.getSelectedItem() == null) {
            speed1 = Integer.valueOf(String.valueOf(cbxSpeed1.getEditor().getItem()));
        }
        configuration.setWb1Port((String) port1);
        configuration.setWb1BaudRate(speed1);
        configuration.setWb1DataBit(port1 == null ? null : (Short) cbxDataBits1.getSelectedItem());
        Float sbit1 = 0f;
        sbit1 = (Float) cbxStopBits1.getSelectedItem();
        sbit1 = (sbit1 == 1.5f ? sbit1 * 2f : sbit1);
        configuration.setWb1StopBit(port1 == null ? null : new BigDecimal(sbit1));
        configuration.setWb1ParityControl(port1 == null ? null : (new Integer(((ParityEnum) cbxPControl1.getSelectedItem()).ordinal())).shortValue());
        configuration.setWb1Mettler(port1 == null ? null : chbMettler1.isSelected());

        Object port2 = cbxPort2.getSelectedItem();
        port2 = port2 == null || port2.toString().trim().length() == 0 ? null : port2.toString().trim();
        Integer speed2 = 0;
        if (port2 == null) {
            speed2 = null;
        } else if (cbxSpeed2.getSelectedItem() instanceof Short) {
            speed2 = ((Short) cbxSpeed2.getSelectedItem()).intValue();
        } else if (cbxSpeed2.getSelectedItem() instanceof Integer) {
            speed2 = (Integer) cbxSpeed2.getSelectedItem();
        } else if (cbxSpeed2.getSelectedItem() instanceof String) {
            try {
                speed2 = Integer.valueOf(String.valueOf(cbxSpeed2.getSelectedItem()));
            } catch (NumberFormatException ex) {
                speed2 = null;
            }
        } else if (cbxSpeed2.getSelectedItem() == null) {
            speed2 = Integer.valueOf(String.valueOf(cbxSpeed2.getEditor().getItem()));
        }
        configuration.setWb2Port((String) port2);
        configuration.setWb2BaudRate(speed2);
        configuration.setWb2DataBit(port2 == null ? null : (Short) cbxDataBits2.getSelectedItem());
        Float sbit2 = 0f;
        sbit2 = (Float) cbxStopBits2.getSelectedItem();
        sbit2 = (sbit2 == 1.5f ? sbit2 * 2f : sbit2);
        configuration.setWb2StopBit(port2 == null ? null : new BigDecimal(sbit2));
        configuration.setWb2ParityControl(port2 == null ? null : (new Integer(((ParityEnum) cbxPControl2.getSelectedItem()).ordinal())).shortValue());
        configuration.setWb2Mettler(port2 == null ? null : chbMettler2.isSelected());

        String wPlant = txtPlant.getText();
        wPlant = wPlant == null || wPlant.trim().isEmpty() ? "" : wPlant;
        configuration.setWkPlant(Base64_Utils.decodeNTimes(wPlant));

        return config;
    }

    public void objBinding(AppConfig config, JTextField txtWBID, JTextField txtDBHost, JTextField txtDBName, JTextField txtDBUsr, JPasswordField txtDBPwd
            ,JTextField txtHost, JTextField txtRString, JFormattedTextField txfSNo, JFormattedTextField txfDClient,
            JComboBox cbxPort1, JComboBox cbxPort2, JComboBox cbxSpeed1, JComboBox cbxSpeed2,
            JComboBox cbxDataBits1, JComboBox cbxDataBits2, JComboBox cbxStopBits1
            ,JComboBox cbxPControl1, JCheckBox chbMettler1, JComboBox cbxStopBits2, JComboBox cbxPControl2, JCheckBox chbMettler2, JTextField txtPlant){
        Configuration configuration = config.getConfiguration();
        txtWBID.setText(Base64_Utils.encodeNTimes(configuration.getWbId()));
        txtDBHost.setText(Base64_Utils.encodeNTimes(config.getDbHost()));
        txtDBName.setText(Base64_Utils.encodeNTimes(config.getDbName()));
        txtDBUsr.setText(Base64_Utils.encodeNTimes(config.getDbUsername()));
        txtDBPwd.setText(Base64_Utils.encodeNTimes(config.getDbPassword()));

        txtHost.setText(Base64_Utils.encodeNTimes(configuration.getSapHost()));
        txfSNo.setValue(Base64_Utils.encodeNTimes(configuration.getSapSystemNumber()));
        txtRString.setText(Base64_Utils.encodeNTimes(configuration.getSapRouteString()));
        txfDClient.setValue(Base64_Utils.encodeNTimes(configuration.getSapClient()));
        txtPlant.setText(Base64_Utils.encodeNTimes(configuration.getWkPlant()));

        DefaultComboBoxModel port1Model = getPortModel();
        String port1 = configuration.getWb1Port();
        if (port1Model.getIndexOf(port1) < 0) {
            port1Model.addElement(port1);
            cbxPort1.setModel(port1Model);
        }
        cbxPort1.setSelectedItem(port1);

        DefaultComboBoxModel speed1Model = getSpeedModel();
        Integer speed1 = configuration.getWb1BaudRate();
        if (speed1Model.getIndexOf(speed1) < 0) {
            speed1Model.addElement(speed1);
            cbxSpeed1.setModel(speed1Model);
        }
        cbxSpeed1.setSelectedItem(speed1);

        int dbit1 = configuration.getWb1DataBit();
        cbxDataBits1.setSelectedItem(dbit1);

        Float sbit1 = configuration.getWb1StopBit().floatValue();
        if (sbit1 != null && sbit1 == 3f) {
            sbit1 = sbit1 / 2f;
        }
        cbxStopBits1.setSelectedItem(sbit1);

        ParityEnum p1Control = null;

        switch (configuration.getWb1ParityControl()) {
            case 0:
                p1Control = ParityEnum.NONE;
                break;
            case 1:
                p1Control = ParityEnum.ODD;
                break;
            case 2:
                p1Control = ParityEnum.EVEN;
                break;
            case 3:
                p1Control = ParityEnum.MARK;
                break;
            case 4:
                p1Control = ParityEnum.SPACE;
                break;
        }
        cbxPControl1.setSelectedItem(p1Control);
        chbMettler1.setSelected(configuration.getWb1Mettler()== null ? false : configuration.getWb1Mettler());

        DefaultComboBoxModel port2Model = getPortModel();
        String port2 = configuration.getWb2Port();
        if (port2Model.getIndexOf(port2) < 0) {
            port2Model.addElement(port2);
            cbxPort2.setModel(port2Model);
        }
        cbxPort2.setSelectedItem(port2);

        DefaultComboBoxModel speed2Model = getSpeedModel();
        Integer speed2 = configuration.getWb2BaudRate();
        if (speed2Model.getIndexOf(speed2) < 0) {
            speed2Model.addElement(speed2);
            cbxSpeed2.setModel(speed2Model);
        }
        cbxSpeed2.setSelectedItem(speed2);

        int dbit2 = configuration.getWb2DataBit();
        cbxDataBits2.setSelectedItem(dbit2);

        Float sbit2 = configuration.getWb2StopBit().floatValue();
        if (sbit2 != null && sbit2 == 3f) {
            sbit2 = sbit2 / 2f;
        }
        cbxStopBits2.setSelectedItem(sbit2);

        ParityEnum p2Control = null;
        switch (configuration.getWb2ParityControl()) {
            case 0:
                p2Control = ParityEnum.NONE;
                break;
            case 1:
                p2Control = ParityEnum.ODD;
                break;
            case 2:
                p2Control = ParityEnum.EVEN;
                break;
            case 3:
                p2Control = ParityEnum.MARK;
                break;
            case 4:
                p2Control = ParityEnum.SPACE;
                break;
        }
        cbxPControl2.setSelectedItem(p2Control);
        chbMettler2.setSelected(configuration.getWb2Mettler()== null ? false : configuration.getWb2Mettler());
    }
    
    public DefaultComboBoxModel getSpeedModel() {
        return new DefaultComboBoxModel(new Integer[]{110, 300, 600, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200});
    }
}
