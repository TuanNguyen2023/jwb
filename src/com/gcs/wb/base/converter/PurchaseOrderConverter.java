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
import com.gcs.wb.jpa.entity.PurchaseOrderDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author THANGPT
 */
public class PurchaseOrderConverter extends AbstractThrowableParamConverter<PoGetDetailBapi, PurchaseOrder, Exception> {

    Configuration configuration = WeighBridgeApp.getApplication().getConfig().getConfiguration();
    
    @Override
    public PurchaseOrder convertHasParameter(PoGetDetailBapi from, String poNum) throws Exception {
        BigDecimal item_qty = BigDecimal.ZERO;
        BigDecimal item_qty_free = BigDecimal.ZERO;
        PoGetDetailHeaderStructure header = from.getPoHeader();
        List<PoGetDetailItemStructure> items = from.getPoItems();
        if (items.size() == 2 && !items.get(0).getMATERIAL().equalsIgnoreCase(items.get(1).getMATERIAL())) {
            throw new Exception("Không hỗ trợ P.O số: " + poNum + "!");
        }

        PurchaseOrder result = new PurchaseOrder(configuration.getSapClient(), poNum);
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
            PurchaseOrderDetail purchaseOrderDetail = new PurchaseOrderDetail();
            
            if (item.getFREE_ITEM() == null || item.getFREE_ITEM().trim().isEmpty()) {
                purchaseOrderDetail.setPoItem(item.getPO_ITEM());
                purchaseOrderDetail.setIDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
                purchaseOrderDetail.setShortText(item.getSHORT_TEXT());
                purchaseOrderDetail.setMaterial(item.getMATERIAL());
                purchaseOrderDetail.setPlant(item.getPLANT());
                purchaseOrderDetail.setStgeLoc(item.getSTGE_LOC());
                purchaseOrderDetail.setVendMat(item.getVEND_MAT());
                purchaseOrderDetail.setQuantity(item_qty.add(item.getQUANTITY()));
                purchaseOrderDetail.setPoUnit(item.getPO_UNIT());
                purchaseOrderDetail.setPoUnitIso(item.getPO_UNIT_ISO());
                purchaseOrderDetail.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                purchaseOrderDetail.setOverDlvTol(item.getOVER_DLV_TOL());
                purchaseOrderDetail.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                purchaseOrderDetail.setValType(item.getVAL_TYPE());
                purchaseOrderDetail.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                purchaseOrderDetail.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                purchaseOrderDetail.setItemCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                purchaseOrderDetail.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                purchaseOrderDetail.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                purchaseOrderDetail.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                purchaseOrderDetail.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            } else {
                purchaseOrderDetail.setPoItemFree(item.getPO_ITEM());
                purchaseOrderDetail.setIfDeleteInd(item.getDELETE_IND() == null || item.getDELETE_IND().trim().isEmpty() ? ' ' : item.getDELETE_IND().charAt(0));
                purchaseOrderDetail.setQuantityFree(item_qty_free.add(item.getQUANTITY()));
                purchaseOrderDetail.setItemFreeCat(item.getITEM_CAT() == null || item.getITEM_CAT().trim().isEmpty() ? ' ' : item.getITEM_CAT().charAt(0));
                purchaseOrderDetail.setShortText(item.getSHORT_TEXT());
                purchaseOrderDetail.setMaterial(item.getMATERIAL());
                purchaseOrderDetail.setPlant(item.getPLANT());
                purchaseOrderDetail.setStgeLoc(item.getSTGE_LOC());
                purchaseOrderDetail.setVendMat(item.getVEND_MAT());
                purchaseOrderDetail.setPoUnit(item.getPO_UNIT());
                purchaseOrderDetail.setPoUnitIso(item.getPO_UNIT_ISO());
                purchaseOrderDetail.setQualInsp(item.getQUAL_INSP() == null || item.getQUAL_INSP().trim().isEmpty() ? ' ' : item.getQUAL_INSP().charAt(0));
                purchaseOrderDetail.setOverDlvTol(item.getOVER_DLV_TOL());
                purchaseOrderDetail.setUnlimitedDlv(item.getUNLIMITED_DLV() == null || item.getUNLIMITED_DLV().trim().isEmpty() ? ' ' : item.getUNLIMITED_DLV().charAt(0));
                purchaseOrderDetail.setValType(item.getVAL_TYPE());
                purchaseOrderDetail.setNoMoreGr(item.getNO_MORE_GR() == null || item.getNO_MORE_GR().trim().isEmpty() ? ' ' : item.getNO_MORE_GR().charAt(0));
                purchaseOrderDetail.setFinalInv(item.getFINAL_INV() == null || item.getFINAL_INV().trim().isEmpty() ? ' ' : item.getFINAL_INV().charAt(0));
                purchaseOrderDetail.setGrInd(item.getGR_IND() == null || item.getGR_IND().trim().isEmpty() ? ' ' : item.getGR_IND().charAt(0));
                purchaseOrderDetail.setGrNonVal(item.getGR_NON_VAL() == null || item.getGR_NON_VAL().trim().isEmpty() ? ' ' : item.getGR_NON_VAL().charAt(0));
                purchaseOrderDetail.setDelivCompl(item.getDELIV_COMPL() == null || item.getDELIV_COMPL().trim().isEmpty() ? ' ' : item.getDELIV_COMPL().charAt(0));
                purchaseOrderDetail.setPartDeliv(item.getPART_DELIV() == null || item.getPART_DELIV().trim().isEmpty() ? ' ' : item.getPART_DELIV().charAt(0));
            }

            //neu chi co free goods
            if (purchaseOrderDetail.getPoItem() == null) {
                if (purchaseOrderDetail.getPoItemFree() != null) {
                    purchaseOrderDetail.setPoItem(purchaseOrderDetail.getPoItemFree());
                    purchaseOrderDetail.setQuantity(purchaseOrderDetail.getQuantityFree());
                    purchaseOrderDetail.setPoItemFree(null);
                    purchaseOrderDetail.setQuantityFree(null);
                }
            }

            result.addPurchaseOrderDetail(purchaseOrderDetail);
        }

        return result;
    }

    @Override
    public PurchaseOrder convertsHasParameter(PoGetDetailBapi from, String val, boolean refresh) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
