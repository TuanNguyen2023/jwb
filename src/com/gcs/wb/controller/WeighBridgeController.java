/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.jpa.entity.User;
import com.gcs.wb.service.WeighBridgeService;
import javax.swing.JLabel;

/**
 *
 * @author THANGPT
 */
public class WeighBridgeController {

    private WeighBridgeService weighBridgeService = new WeighBridgeService();

    public void setStatus(User login, JLabel txt_status) {
        weighBridgeService.setStatus(login, txt_status);
    }
}
