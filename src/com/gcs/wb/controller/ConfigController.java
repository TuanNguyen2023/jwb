/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.ConfigService;
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
public class ConfigController {

    private ConfigService configService = new ConfigService();
    
    public DefaultComboBoxModel getPortModel() {
        return configService.getPortModel();
    }

    public boolean validateForm(JTextField txtDBHost, JTextField txtDBName,
            JTextField txtDBUsr,JPasswordField txtDBPwd, JTextField txtHost,JTextField txtPlant,
            JLabel lblDBHost, JLabel lblDBName,
            JLabel lblDBUsr, JLabel lblDBPwd,
            JLabel lblHost,JLabel lblSId, JLabel lblDClient,JLabel lblPort1,
            JLabel lblPort2,JLabel lblSpeed1, JLabel lblSpeed2,JLabel lblPlant,
            JFormattedTextField txfSNo, JFormattedTextField txfDClient,
            JComboBox cbxPort1,JComboBox cbxPort2,JComboBox cbxSpeed1,JComboBox cbxSpeed2) {
        return configService.validateForm(txtDBHost, txtDBName, txtDBUsr, txtDBPwd, txtHost, txtPlant, lblDBHost, lblDBName, lblDBUsr, lblDBPwd,
                lblHost, lblSId, lblDClient, lblPort1, lblPort2, lblSpeed1, lblSpeed2, lblPlant, txfSNo, txfDClient,
                cbxPort1, cbxPort2, cbxSpeed1, cbxSpeed2);
    }
    
    public AppConfig objMapping(AppConfig config, JTextField txtWBID, JTextField txtDBHost, JTextField txtDBName, JTextField txtDBUsr, JPasswordField txtDBPwd
            ,JTextField txtHost, JTextField txtRString, JFormattedTextField txfSNo, JFormattedTextField txfDClient,
            JComboBox cbxPort1, JComboBox cbxPort2, JComboBox cbxSpeed1, JComboBox cbxSpeed2,
            JComboBox cbxDataBits1, JComboBox cbxDataBits2, JComboBox cbxStopBits1
            ,JComboBox cbxPControl1, JCheckBox chbMettler1, JComboBox cbxStopBits2, JComboBox cbxPControl2, JCheckBox chbMettler2, JTextField txtPlant){
    return configService.objMapping(config, txtWBID, txtDBHost, txtDBName, txtDBUsr, txtDBPwd, txtHost, txtRString, txfSNo, txfDClient, cbxPort1, cbxPort2,
            cbxSpeed1, cbxSpeed2, cbxDataBits1, cbxDataBits2, cbxStopBits1, cbxPControl1, chbMettler1, cbxStopBits2, cbxPControl2, chbMettler2, txtPlant);
    }
    
    public void objBinding(AppConfig config, JTextField txtWBID, JTextField txtDBHost, JTextField txtDBName, JTextField txtDBUsr, JPasswordField txtDBPwd
            ,JTextField txtHost, JTextField txtRString, JFormattedTextField txfSNo, JFormattedTextField txfDClient,
            JComboBox cbxPort1, JComboBox cbxPort2, JComboBox cbxSpeed1, JComboBox cbxSpeed2,
            JComboBox cbxDataBits1, JComboBox cbxDataBits2, JComboBox cbxStopBits1
            ,JComboBox cbxPControl1, JCheckBox chbMettler1, JComboBox cbxStopBits2, JComboBox cbxPControl2, JCheckBox chbMettler2, JTextField txtPlant){
        configService.objBinding(config, txtWBID, txtDBHost, txtDBName, txtDBUsr, txtDBPwd, txtHost, txtRString, txfSNo, txfDClient, cbxPort1, cbxPort2, cbxSpeed1,
                cbxSpeed2, cbxDataBits1, cbxDataBits2, cbxStopBits1, cbxPControl1, chbMettler1, cbxStopBits2, cbxPControl2, chbMettler2, txtPlant);
    }
    
    public DefaultComboBoxModel getSpeedModel() {
        return configService.getPortModel();
    }
}
