/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.SAP2Local;
import com.gcs.wb.base.constant.Constants;
import com.gcs.wb.jpa.controller.WeightTicketJpaController;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.CustomerPK;
import com.gcs.wb.jpa.entity.OutbDel;
import com.gcs.wb.jpa.entity.Vendor;
import com.gcs.wb.jpa.entity.VendorPK;
import com.gcs.wb.jpa.entity.WeightTicket;
import com.gcs.wb.jpa.entity.WeightTicketPK;
import com.gcs.wb.model.AppConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author THANGPT
 */
public class WTRegController {

    WeightTicketJpaController conWTicket = new WeightTicketJpaController();

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

        List<WeightTicket> result = conWTicket.findByMandtWPlantDateFull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMaterialPK().getMatnr(), txtBienSo.getText().trim());
        List<WeightTicket> data = conWTicket.findByMandtWPlantDateFull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMaterialPK().getMatnr(), txtBienSo.getText().trim());
        result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
        if (selecttext.getMaktx().equals("Linh tinh")) {
            data = conWTicket.findByMandtWPlantDateNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
            result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByMandtWPlantDateDissolvedNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByMandtWPlantDatePostedNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByMandtWPlantDateAllNull(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            }
        } else if (selecttext.getMaktx().equals("Tất cả")) {
            data = conWTicket.findByMandtWPlantDateNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
            result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByMandtWPlantDateDissolvedNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByMandtWPlantDatePostedNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByMandtWPlantDateAllNullAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            }
        } else {
            if (rbtDissolved.isSelected()) {
                data = conWTicket.findByMandtWPlantDateDissolved(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMaterialPK().getMatnr(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtPosted.isSelected()) {
                data = conWTicket.findByMandtWPlantDatePosted(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMaterialPK().getMatnr(), txtBienSo.getText().trim());
                result = filterHours(data, cbxTimeFrom.getSelectedItem().toString(), cbxTimeTo.getSelectedItem().toString());
            } else if (rbtStateAll.isSelected()) {
                data = conWTicket.findByMandtWPlantDateAll(fFrom, fTo, txtNguoitao.getText().trim(), txtTaixe.getText().trim(), selecttext.getMaterialPK().getMatnr(), txtBienSo.getText().trim());
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

                String ct = item.getCreateTime();
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

    public Object[][] handleWtData(String getMode, Object[][] wtData, Object[][] wtRptData, List<WeightTicket> weightTicketList, AppConfig config, Object[] wtCols, Object[] wtRptCols, String sVendor) {
        for (int i = 0; i < weightTicketList.size(); i++) {
            WeightTicket item = weightTicketList.get(i);
            if (getMode.equalsIgnoreCase(Constants.WTRegView.MODE_REG)) {
                WeightTicketPK pk = item.getWeightTicketPK();
                if (!pk.getMandt().equalsIgnoreCase(config.getsClient()) && !pk.getWPlant().equalsIgnoreCase(config.getwPlant().toString())) {
                    continue;
                }
                wtData[i][0] = pk.getSeqByDay();
                wtData[i][1] = item.getTenTaiXe();
                wtData[i][2] = item.getCmndBl();
                wtData[i][3] = item.getSoXe();
                wtData[i][4] = item.getSoRomooc();
                wtData[i][5] = item.getRegCategory();
                wtData[i][6] = item.getRegItemText();
                wtData[i][7] = item.getRegItemQty();
                wtData[i][8] = item.getDelivNumb();
                wtData[i][9] = item.getCreator();
                wtData[i][10] = item.getSeqByMonth();
                wtData[i][11] = item.getCreateDate();
                String hh;
                String mm;
                String ss;
                hh = item.getCreateTime().substring(0, 2);
                mm = item.getCreateTime().substring(2, 4);
                ss = item.getCreateTime().substring(4);
                wtData[i][12] = hh + ":" + mm + ":" + ss;
                wtData[i][13] = item.getDissolved();
                if (item.getPosted() == 1) {
                    wtData[i][14] = true;
                } else {
                    wtData[i][14] = false;
                }

            } else if (getMode.equalsIgnoreCase(Constants.WTRegView.MODE_RPT)) {
                WeightTicketPK pk = item.getWeightTicketPK();
                if (!pk.getMandt().equalsIgnoreCase(config.getsClient()) && !pk.getWPlant().equalsIgnoreCase(config.getwPlant().toString())) {
                    continue;
                }
                String hh;
                String mm;
                String ss;
                hh = item.getCreateTime().substring(0, 2);
                mm = item.getCreateTime().substring(2, 4);
                ss = item.getCreateTime().substring(4);
                Calendar create_date = Calendar.getInstance();
                create_date.setTime(item.getCreateDate());
                create_date.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hh));
                create_date.set(Calendar.MINUTE, Integer.valueOf(mm));
                create_date.set(Calendar.SECOND, Integer.valueOf(ss));
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String ct = df.format(create_date.getTime());
                wtRptData[i][0] = i + 1;// item.getSeqByMonth();
                wtRptData[i][1] = item.getWeightTicketPK().getSeqByDay();
                wtRptData[i][2] = item.getTenTaiXe();
                wtRptData[i][3] = item.getCmndBl();
                wtRptData[i][4] = item.getSoXe();
                wtRptData[i][5] = item.getSoRomooc();
                wtRptData[i][6] = item.getCreator();
                wtRptData[i][7] = ct;
                wtRptData[i][8] = item.getRegCategory();
                wtRptData[i][9] = item.getRegItemText();
                if (item.getFTime() != null) {
                    Calendar cf = Calendar.getInstance();
                    cf.setTime(item.getFTime());
                    String ft = df.format(cf.getTime());
                    wtRptData[i][10] = ft;//item.getFTime();
                } else {
                    wtRptData[i][10] = item.getFTime();
                }
                wtRptData[i][11] = item.getFScale() == null ? item.getFScale() : item.getFScale().doubleValue() / 1000d;
                if (item.getSTime() != null) {
                    Calendar sf = Calendar.getInstance();
                    sf.setTime(item.getSTime());
                    String st = df.format(sf.getTime());
                    wtRptData[i][12] = st;//item.getSTime();
                } else {
                    wtRptData[i][12] = item.getSTime();
                }
                wtRptData[i][13] = item.getSScale() == null ? item.getSScale() : item.getSScale().doubleValue() / 1000d;
                wtRptData[i][14] = item.getGQty();
                wtRptData[i][15] = item.getDelivNumb();
                wtRptData[i][16] = item.getMatDoc();
                wtRptData[i][17] = item.getDissolved();
                if (item.getPosted() == 1) {
                    wtRptData[i][18] = true;
                } else {
                    wtRptData[i][18] = false;
                }
                wtRptData[i][20] = item.getEbeln();
                wtRptData[i][19] = sVendor;
            }
        }
        if (getMode.equalsIgnoreCase(Constants.WTRegView.MODE_REG)) {
            return wtData;
        } else if (getMode.equalsIgnoreCase(Constants.WTRegView.MODE_RPT)) {
            return wtRptData;
        }
        return null;
    }

    public String printReportDoInBackground(JXDatePicker dpFrom, JXDatePicker dpTo) {

        String reportName = null;
        if (WeighBridgeApp.getApplication().getConfig().getModeNormal()) {
            reportName = "./rpt/rptBT/WTList.jasper";
        } else {
            reportName = "./rpt/rptPQ/WTList.jasper";
        }
        return reportName;
    }

    public Map<String, Object> getPrintReport(JXDatePicker dpFrom, JXDatePicker dpTo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String kFrom = format.format(dpFrom.getDate());
        String kTo = format.format(dpTo.getDate());
        System.out.println("dpFrom : " + kFrom + " to " + kTo);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("P_PNAME_RPT", WeighBridgeApp.getApplication().getSapSetting().getNameRpt());
        params.put("P_PADDRESS", WeighBridgeApp.getApplication().getSapSetting().getAddress());
        params.put("P_PPHONE", WeighBridgeApp.getApplication().getSapSetting().getPhone());
        params.put("P_PFAX", WeighBridgeApp.getApplication().getSapSetting().getFax());
        params.put("P_FROM", kFrom);
        params.put("P_TO", kTo);
        return params;
    }

    public void hanldeCheckDOOutbDel(EntityManager entityManager, OutbDel sapOutb, Customer kunnr, Customer kunag, Vendor lifnr, Customer sapKunnr, Customer sapKunag, Vendor sapLifnr) {
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
                // abbr = sapLifnr.getVendorPK().getLifnr();
            }

        }
    }
}
