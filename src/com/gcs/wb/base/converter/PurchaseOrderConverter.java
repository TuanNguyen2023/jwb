/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.converter;

import com.gcs.wb.WeighBridgeApp;
import com.gcs.wb.bapi.helper.PoGetDetailBapi;
import com.gcs.wb.bapi.helper.structure.PoGetDetailHeaderStructure;
import com.gcs.wb.bapi.helper.structure.PoGetDetailItemStructure;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.jpa.entity.PurchaseOrder;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class PurchaseOrderConverter extends AbstractThrowableParamConverter<PoGetDetailBapi, PurchaseOrder, Exception> {

    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    
    @Override
    public PurchaseOrder convertHasParameter(PoGetDetailBapi from, String val) throws Exception {
        String item_num = null;
        String item_num_free = null;
        boolean flag_free = true;
        boolean flag = true;
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        PurchaseOrder result;
        PoGetDetailHeaderStructure header = from.getPoHeader();
        List<PoGetDetailItemStructure> items = from.getPoItems();
        if (items.size() == 2 && !items.get(0).getMATERIAL().equalsIgnoreCase(items.get(1).getMATERIAL())) {
            throw new Exception("Không hỗ trợ P.O số: " + val + "!");
        }

        result = new PurchaseOrder(configuration.getSapClient(), val);
        result.setDocType(header.getDOC_TYPE());
        result.setDeleteInd(header.getDELETE_IND() == null || header.getDELETE_IND().trim().isEmpty() ? ' ' : header.getDELETE_IND().charAt(0));
        result.setStatus(header.getSTATUS() == null || header.getSTATUS().trim().isEmpty() ? ' ' : header.getSTATUS().charAt(0));
        result.setCreatDate(header.getCREAT_DATE());
        result.setVendor(header.getVENDOR());
        result.setSupplVend(header.getSUPPL_VEND());
        result.setCustomer(header.getCUSTOMER());
        result.setSupplPlnt(header.getSUPPL_PLNT());
        result.setPoRelInd(header.getPO_REL_IND() == null || header.getPO_REL_IND().trim().isEmpty() ? ' ' : header.getPO_REL_IND().charAt(0));
        result.setRelStatus(header.getREL_STATUS());
        for (int i = 0; i < items.size(); i++) {
            PoGetDetailItemStructure item = items.get(i);
            if (item.getFREE_ITEM() == null || item.getFREE_ITEM().trim().isEmpty()) {
                if (flag == true) {
                    item_num = item.getPO_ITEM();
                    flag = false;
                }
                result.setIDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
                result.setShortText(item.getSHORT_TEXT());
                result.setMaterial(item.getMATERIAL());
                result.setPlant(item.getPLANT());
                result.setStgeLoc(item.getSTGE_LOC());
                result.setVendMat(item.getVEND_MAT());
                item_qty = item_qty.add(item.getQUANTITY());
                result.setPoUnit(item.getPO_UNIT());
                result.setPoUnitIso(item.getPO_UNIT_ISO());
                result.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                result.setOverDlvTol(item.getOVER_DLV_TOL());
                result.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                result.setValType(item.getVAL_TYPE());
                result.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                result.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                result.setItemCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                result.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                result.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                result.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                result.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            } else {
                if (flag_free == true) {
                    item_num_free = item.getPO_ITEM();
                    flag_free = false;
                }
                result.setIfDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
                item_qty_free = item_qty_free.add(item.getQUANTITY());
                result.setItemFreeCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                result.setShortText(item.getSHORT_TEXT());
                result.setMaterial(item.getMATERIAL());
                result.setPlant(item.getPLANT());
                result.setStgeLoc(item.getSTGE_LOC());
                result.setVendMat(item.getVEND_MAT());
                result.setPoUnit(item.getPO_UNIT());
                result.setPoUnitIso(item.getPO_UNIT_ISO());
                result.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                result.setOverDlvTol(item.getOVER_DLV_TOL());
                result.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                result.setValType(item.getVAL_TYPE());
                result.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                result.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                result.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                result.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                result.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                result.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            }
        }
        result.setPoItem(item_num);
        result.setPoItemFree(item_num_free);
        result.setQuantity(item_qty);
        result.setQuantityFree(item_qty_free);
        //neu chi co free goods
        if (result.getPoItem() == null) {
            if (result.getPoItemFree() != null) {
                result.setPoItem(result.getPoItemFree());
                result.setQuantity(result.getQuantityFree());
                result.setPoItemFree(null);
                result.setQuantityFree(null);
            }
        }
        return result;
    }
    @Override
    public PurchaseOrder convertsHasParameter(PoGetDetailBapi from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
