/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi.outbdlv;

import com.gcs.wb.bapi.goodsmvt.structure.GoodsMvtWeightTicketStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbpokStructure;
import com.gcs.wb.bapi.outbdlv.structure.VbkokStructure;
import com.gcs.wb.bapi.outbdlv.constants.WsDeliveryUpdateConstants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibersap.annotations.Bapi;
import org.hibersap.annotations.Export;
import org.hibersap.annotations.Import;
import org.hibersap.annotations.Parameter;
import org.hibersap.annotations.ParameterType;
import org.hibersap.annotations.Table;
import org.hibersap.annotations.ThrowExceptionOnError;
import org.hibersap.bapi.BapiRet2;

/**
 *
 * @author Tran-Vu
 */
@Bapi(WsDeliveryUpdateConstants.BAPI_NAME)
@ThrowExceptionOnError(returnStructure = "TABLE/RETURN")
public class WsDeliveryUpdateBapi implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="Import Parameters">
    /**Delivery Header*/
    @Import
    @Parameter(value = WsDeliveryUpdateConstants.VBKOK_WA, type = ParameterType.STRUCTURE)
    private VbkokStructure _vbkok_wa;
    /**Delivery Number*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.DELIVERY)
    private String _delivery;
    /**Process Order ID*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_PROCORDID)
    private String _proc_ord_id;
    /**Qty. in confirmation*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_YIELD)
    private BigDecimal _yield;
    /**Act.1 value*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_CONF_ACTIVITY1)
    private BigDecimal _act1;
    /**Act.2 value*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_CONF_ACTIVITY2)
    private BigDecimal _act2;
    /**Act.3 value*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_CONF_ACTIVITY3)
    private BigDecimal _act3;
    /**Act.4 value*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.PP_CONF_ACTIVITY4)
    private BigDecimal _act4;
    /**Update Picking flag*/
    @Import
    @Parameter(WsDeliveryUpdateConstants.UPDATE_PICKING)
    private String _update_picking;

    @Import
    @Parameter(value = WsDeliveryUpdateConstants.I_WEIGHTTICKET, type = ParameterType.STRUCTURE)
    private GoodsMvtWeightTicketStructure _weightticket;

    @Import
    @Parameter(WsDeliveryUpdateConstants.IV_WBID_NOSAVE)
    private String _iv_wbid_nosave;

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Export Parameters">
    /**Confirmation number*/
    @Export
    @Parameter(value = WsDeliveryUpdateConstants.CONF_NO, type = ParameterType.SIMPLE)
    private String _conf_no;
    /**Confirmation Counter*/
    @Export
    @Parameter(value = WsDeliveryUpdateConstants.CONF_CNT, type = ParameterType.SIMPLE)
    private String _conf_cnt;
    /**Material Document number*/
    @Export
    @Parameter(value = WsDeliveryUpdateConstants.MATERIALDOCUMENT, type = ParameterType.SIMPLE)
    private String _mat_doc;
    @Export
    @Parameter(value = WsDeliveryUpdateConstants.MATDOCUMENTYEAR, type = ParameterType.SIMPLE)
    private String _doc_year;
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Table Parameters">
    /**Delivery Item*/
    @Table
    @Parameter(value = WsDeliveryUpdateConstants.VBPOK_TAB, type = ParameterType.STRUCTURE)
    private List<VbpokStructure> _vbpok_tab;
    @Table
    @Parameter(value = WsDeliveryUpdateConstants.RETURN, type = ParameterType.STRUCTURE)
    private List<BapiRet2> _return;
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Class Constructor">

    public WsDeliveryUpdateBapi() {
        _vbkok_wa = new VbkokStructure();
        _vbpok_tab = new ArrayList<VbpokStructure>();
    }
    // </editor-fold>
    // <editor-fold defaultstate="expanded" desc="Field Getter/Setter">

    /**
     * Delivery Header
     * @param vbkok_wa the _vbkok_wa to set
     */
    public void setVbkok_wa(VbkokStructure vbkok_wa) {
        this._vbkok_wa = vbkok_wa;
    }

    /**
     * Delivery Number
     * @param delivery the _delivery to set
     */
    public void setDelivery(String delivery) {
        this._delivery = delivery;
    }

    /**
     * Process Order ID
     * @param proc_ord_id the _proc_ord_id to set
     */
    public void setProc_ord_id(String proc_ord_id) {
        this._proc_ord_id = proc_ord_id;
    }

    /**
     * Qty. in confirmation
     * @param yield the _yield to set
     */
    public void setYield(BigDecimal yield) {
        this._yield = yield;
    }

    /**
     * Act.1 value
     * @param act1 the _act1 to set
     */
    public void setAct1(BigDecimal act1) {
        this._act1 = act1;
    }

    /**
     * Act.2 value
     * @param act2 the _act2 to set
     */
    public void setAct2(BigDecimal act2) {
        this._act2 = act2;
    }

    /**
     * Act.3 value
     * @param act3 the _act3 to set
     */
    public void setAct3(BigDecimal act3) {
        this._act3 = act3;
    }

    /**
     * Act.4 value
     * @param act4 the _act4 to set
     */
    public void setAct4(BigDecimal act4) {
        this._act4 = act4;
    }

    /**
     * Update Picking flag
     * @param update_picking the _update_picking to set
     */
    public void setUpdate_picking(String update_picking) {
        this._update_picking = update_picking;
    }

    /**
     * Confirmation number
     * @return the _conf_no
     */
    public String getConf_no() {
        return _conf_no;
    }

    /**
     * Confirmation Counter
     * @return the _conf_cnt
     */
    public String getConf_cnt() {
        return _conf_cnt;
    }

    /**
     * Material Document number
     * @return the _mat_doc
     */
    public String getMat_doc() {
        return _mat_doc;
    }

    /**
     * @return the _doc_year
     */
    public String getDoc_year() {
        return _doc_year;
    }

    /**
     * Delivery Item
     * @param vbpok_tab the _vbpok_tab to set
     */
    public void setVbpok_tab(List<VbpokStructure> vbpok_tab) {
        this._vbpok_tab = vbpok_tab;
    }

    /**
     * @return the _return
     */
    public List<BapiRet2> getReturn() {
        return _return;
    }
    // </editor-fold>

    public GoodsMvtWeightTicketStructure getWeightticket() {
        return _weightticket;
    }

    public void setWeightticket(GoodsMvtWeightTicketStructure _weightticket) {
        this._weightticket = _weightticket;
    }

    public void setIvWbidNosave(String ivWbidNosave) {
        this._iv_wbid_nosave = ivWbidNosave;
    }
    
    public List<String> getReturnMessage() {
        List<String> errorMsgs = new ArrayList<>();
        if (_return == null || _return.isEmpty()) {
            return errorMsgs;
        }
        List<String> errors = _return.stream().map(bapiRet2 -> {
            if (bapiRet2.getType() != 'E') {
                return null;
            }

            if (bapiRet2.getMessage() != null && !bapiRet2.getMessage().trim().isEmpty()) {
                return bapiRet2.getMessage().trim();
            }

            String msg = "";
            if (bapiRet2.getMessageV1() != null && !bapiRet2.getMessageV1().trim().isEmpty()) {
                msg += bapiRet2.getMessageV1() + " ";
            }
            if (bapiRet2.getMessageV2() != null && !bapiRet2.getMessageV2().trim().isEmpty()) {
                msg += bapiRet2.getMessageV2() + " ";
            }
            if (bapiRet2.getMessageV3() != null && !bapiRet2.getMessageV3().trim().isEmpty()) {
                msg += bapiRet2.getMessageV3() + " ";
            }
            if (bapiRet2.getMessageV4() != null && !bapiRet2.getMessageV4().trim().isEmpty()) {
                msg += bapiRet2.getMessageV4() + " ";
            }
            return msg;
        }).filter(t -> t != null).collect(Collectors.toList());
        
        List<String> normals = _return.stream().map(bapiRet2 -> {
            if (bapiRet2.getType() == 'E') {
                return null;
            }
            if (bapiRet2.getMessage() != null && !bapiRet2.getMessage().trim().isEmpty()) {
                return bapiRet2.getMessage().trim();
            }
            String msg = "";
            if (bapiRet2.getMessageV1() != null && !bapiRet2.getMessageV1().trim().isEmpty()) {
                msg += bapiRet2.getMessageV1() + " ";
            }
            if (bapiRet2.getMessageV2() != null && !bapiRet2.getMessageV2().trim().isEmpty()) {
                msg += bapiRet2.getMessageV2() + " ";
            }
            if (bapiRet2.getMessageV3() != null && !bapiRet2.getMessageV3().trim().isEmpty()) {
                msg += bapiRet2.getMessageV3() + " ";
            }
            if (bapiRet2.getMessageV4() != null && !bapiRet2.getMessageV4().trim().isEmpty()) {
                msg += bapiRet2.getMessageV4() + " ";
            }
            return msg;
        }).filter(t -> t != null).collect(Collectors.toList());
        
        return errors.size()> 0 ? errors : normals;
    }

    @Override
    public String toString() {
        return "WsDeliveryUpdateBapi{" + "_vbkok_wa=" + _vbkok_wa + ", _delivery=" + _delivery + ", _proc_ord_id=" + _proc_ord_id + ", _yield=" + _yield + ", _act1=" + _act1 + ", _act2=" + _act2 + ", _act3=" + _act3 + ", _act4=" + _act4 + ", _update_picking=" + _update_picking + ", _weightticket=" + _weightticket + ", _iv_wbid_nosave=" + _iv_wbid_nosave + '}';
    }

}
