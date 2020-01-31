/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.service;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.service.SAPService;
import com.gcs.wb.jpa.JPAConnector;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.OutboundDelivery;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.repositorys.CustomerRepository;
import com.gcs.wb.jpa.repositorys.VendorRepository;
import com.gcs.wb.model.AppConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class WeightTicketRegistarationService {

    WeightTicketJpaController conWTicket = new WeightTicketJpaController();
    EntityManager entityManager = JPAConnector.getInstance();
    CustomerRepository customerRepository = new CustomerRepository();
    VendorRepository vendorRepository = new VendorRepository();
    SAPService sAPService = new SAPService();

    public List<WeightTicket> listWeightTicketsDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo, JComboBox cbxType, JComboBox cbxTimeFrom, JComboBox cbxTimeTo, JTextField txtNguoitao, JRadioButton rbtDissolved, JRadioButton rbtPosted, JRadioButton rbtStateAll, JTextField txtTaixe, JTextField txtBienSo) throws Exception {
        AppConfig config = WeighBridgeApp.getApplication().getConfig();
        int days = (int) ((dpTo.getDate().getTime() - dpFrom.getDate().getTime()) / (1000 * 60 * 60 * 24));
        Date Now = new Date();
        int days2 = (int) ((Now.getTime() - dpTo.getDate().getTime()) / (1000 * 60 * 60 * 24));

        Object[] select = cbxType.getSelectedObjects();
        com.gcs.wb.jpa.entity.Material selecttext = (com.gcs.wb.jpa.entity.Material) select[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fFrom = format.format(dpFrom.getDate());
        String fTo = format.format(dpTo.getDate());
        System.out.println("dpFrom : " + fFrom + " to " + fTo);

        List<WeightTicket> result = conWTicket.findByDateFull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMatnr(), txtBienSo.getText().trim());
        List<WeightTicket> data = conWTicket.findByDateFull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMatnr(), txtBienSo.getText().trim());
        result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
        if (selecttext.getMaktx().equals("Linh tinh")) {
            data = conWTicket.findByDateNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
            result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByDateDissolvedNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByDatePostedNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByDateAllNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            }
        } else if (selecttext.getMaktx().equals("Tất cả")) {
            data = conWTicket.findByDateNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
            result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByDateDissolvedNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByDatePostedNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByDateAllNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            }
        } else {
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByDateDissolved(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMatnr(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByDatePosted(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMatnr(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByDateAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMatnr(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            }
        }
        return result;
    }

    private List<WeightTicket> filterHours(List<WeightTicket> data, String timefrom, String timeto) {
        List<WeightTicket> result = new ArrayList<WeightTicket>();
        int n1 = Integer.parseInt(timefrom);
        double n2 = Integer.parseInt(timeto) + 0.99;
        if (!data.isEmpty()) {
            for (int i = 0; i < data.size(); i++) {
                WeightTicket item = null;
                item = data.get(i);

                String ct = item.getCreatedTime();
                Character c0 = ct.charAt(0);
                Character c1 = ct.charAt(1);
                String ct1 = c0.toString().concat(c1.toString());
                int cTime = Integer.parseInt(ct1);
                if (cTime >= n1 && cTime <= n2) {
                    if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
                        result.add(item);
                    } else {
                        //filter sloc
                        String[] sloc_item = WeighBridgeApp.getApplication().getSloc().split("-");
                        if (sloc_item.length > 0) {
                            for (int j = 0; j < sloc_item.length; j++) {
                                String current_sloc = sloc_item[j].toString().trim();
                                if (item.getLgort() == null) {
                                    result.add(item);
                                    break;
                                } else if (item.getLgort().equals(current_sloc)) {
                                    result.add(item);
                                }
                            }
                        }

                    }

                }
            }
        }
        return result;
    }

    public Object[][] handleWtData(String getMode, Object[][] wtData, List<WeightTicket> weightTicketList, AppConfig config, Object[] wtCols, String sVendor) {
        for (int i = 0; i < weightTicketList.size(); i++) {
            WeightTicket item = weightTicketList.get(i);
            if (!item.getMandt().equalsIgnoreCase(config.getsClient()) && !item.getWplant().equalsIgnoreCase(config.getwPlant().toString())) {
                continue;
            }
            wtData[i][0] = item.getSeqDay();
            wtData[i][1] = item.getDriverName();
            wtData[i][2] = item.getDriverIdNo();
            wtData[i][3] = item.getPlateNo();
            wtData[i][4] = item.getTrailerId();
            wtData[i][5] = item.getRegType();
            wtData[i][6] = item.getRegItemDescription();
            wtData[i][7] = item.getRegItemQuantity();
            wtData[i][8] = item.getDeliveryOrderNo();
            wtData[i][9] = item.getCreator();
            wtData[i][10] = item.getSeqMonth();
            wtData[i][11] = item.getCreatedDate();
            String hh;
            String mm;
            String ss;
            hh = item.getCreatedTime().substring(0, 2);
            mm = item.getCreatedTime().substring(2, 4);
            ss = item.getCreatedTime().substring(4);
            wtData[i][12] = hh + ":" + mm + ":" + ss;
            wtData[i][13] = item.isDissolved();
            if (item.isPosted()) {
                wtData[i][14] = true;
            } else {
                wtData[i][14] = false;
            }
        }
        return wtData;
    }
    
    public void hanldeCheckDOOutbDel(OutboundDelivery sapOutb, Customer customer, Customer kunag, Vendor lifnr, Customer sapKunnr, Customer sapKunag, Vendor sapLifnr) {
        if (sapOutb != null) {
            if (sapOutb.getKunnr() != null && !sapOutb.getKunnr().trim().isEmpty()) {
                customer = customerRepository.findByKunnr(sapOutb.getKunnr());
                sapKunnr = sAPService.getCustomer(sapOutb.getKunnr());
            }
            if (sapOutb.getKunag() != null && !sapOutb.getKunag().trim().isEmpty()) {
                kunag = customerRepository.findByKunnr(sapOutb.getKunag());
                sapKunag = sAPService.getCustomer(sapOutb.getKunag());
            }
            if (sapOutb.getLifnr() != null && !sapOutb.getLifnr().trim().isEmpty()) {
                lifnr = vendorRepository.findByLifnr(sapOutb.getLifnr());
                sapLifnr = sAPService.getVendor(sapOutb.getLifnr());
                // abbr = sapLifnr.getVendorPK().getLifnr();
            }

        }
    }
}
