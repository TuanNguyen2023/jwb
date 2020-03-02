/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import com.gcs.wb.base.constant.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author thanghl
 */
@Entity
@Table(name = "tbl_configuration")
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c"),
    @NamedQuery(name = "Configuration.findByWbId", query = "SELECT c FROM Configuration c WHERE c.wbId = :wbId")
})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "sap_host")
    private String sapHost;
    @Column(name = "sap_gw_host")
    private String sapGwHost;
    @Column(name = "sap_route_string")
    private String sapRouteString;
    @Column(name = "sap_system_number")
    private String sapSystemNumber;
    @Column(name = "sap_client")
    private String sapClient;

    @Column(name = "wk_plant")
    private String wkPlant;
    @Column(name = "wb_id") // ma cau can
    private String wbId;

    @Column(name = "wb1_port")
    private String wb1Port;
    @Column(name = "wb1_baud_rate")
    private Integer wb1BaudRate;
    @Column(name = "wb1_data_bit")
    private Integer wb1DataBit;
    @Column(name = "wb1_stop_bit")
    private BigDecimal wb1StopBit;
    @Column(name = "wb1_parity_control")
    private Integer wb1ParityControl;
    @Column(name = "wb1_mettler")
    private Boolean wb1Mettler;

    @Column(name = "wb2_port")
    private String wb2Port;
    @Column(name = "wb2_baud_rate")
    private Integer wb2BaudRate;
    @Column(name = "wb2_data_bit")
    private Integer wb2DataBit;
    @Column(name = "wb2_stop_bit")
    private BigDecimal wb2StopBit;
    @Column(name = "wb2_parity_control")
    private Integer wb2ParityControl;
    @Column(name = "wb2_mettler")
    private Boolean wb2Mettler;

    @Column(name = "weight_limit")
    private int weightLimit = Constants.Configuration.WEIGHT_LIMIT;
    @Column(name = "mode_normal")
    private boolean modeNormal = Constants.Configuration.MODE_NORMAL;
    @Column(name = "rpt_id")
    private String rptId = Constants.Configuration.RPT_ID;

    @Column(name = "created_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date createdDate = new Date();
    @Column(name = "updated_date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date updatedDate;

    @Column(name = "wb1_delay")
    private int wb1Delay;
    @Column(name = "wb2_delay")
    private int wb2Delay;
    @Column(name = "wb1_step")
    private int wb1Step;
    @Column(name = "wb2_step")
    private int wb2Step;
    @Column(name = "wb1_mettler_param")
    private String wb1MettlerParam;
    @Column(name = "wb2_mettler_param")
    private String wb2MettlerParam;
    @Column(name = "wplant_map")
    private String wplantMap = Constants.Configuration.WPLANT_MAP;
    @Column(name = "tolerance")
    private BigDecimal tolerance = Constants.Configuration.TOLERANCE;

    @Column(name = "wb1_auto_signal")
    private Boolean wb1AutoSignal;
    @Column(name = "wb2_auto_signal")
    private Boolean wb2AutoSignal;
    @Column(name = "sap_user")
    private String sapUser;
    @Column(name = "sap_pass")
    private String sapPass;

    public Configuration() {
    }

    public String getWplantMap() {
        return wplantMap;
    }

    public void setWplantMap(String wplantMap) {
        this.wplantMap = wplantMap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSapHost() {
        return sapHost;
    }

    public void setSapHost(String sapHost) {
        this.sapHost = sapHost;
    }

    public String getSapGwHost() {
        return sapGwHost;
    }

    public void setSapGwHost(String sapGwHost) {
        this.sapGwHost = sapGwHost;
    }

    public String getSapRouteString() {
        return sapRouteString;
    }

    public void setSapRouteString(String sapRouteString) {
        this.sapRouteString = sapRouteString;
    }

    public String getSapSystemNumber() {
        return sapSystemNumber;
    }

    public void setSapSystemNumber(String sapSystemNumber) {
        this.sapSystemNumber = sapSystemNumber;
    }

    public String getSapClient() {
        return sapClient;
    }

    public void setSapClient(String sapClient) {
        this.sapClient = sapClient;
    }

    public String getWkPlant() {
        return wkPlant;
    }

    public void setWkPlant(String wkPlant) {
        this.wkPlant = wkPlant;
    }

    public String getWbId() {
        return wbId;
    }

    public void setWbId(String wbId) {
        this.wbId = wbId;
    }

    public String getWb1Port() {
        return wb1Port;
    }

    public void setWb1Port(String wb1Port) {
        this.wb1Port = wb1Port;
    }

    public Integer getWb1BaudRate() {
        return wb1BaudRate;
    }

    public void setWb1BaudRate(int wb1BaudRate) {
        this.wb1BaudRate = wb1BaudRate;
    }

    public Integer getWb1DataBit() {
        return wb1DataBit;
    }

    public void setWb1DataBit(int wb1DataBit) {
        this.wb1DataBit = wb1DataBit;
    }

    public BigDecimal getWb1StopBit() {
        return wb1StopBit;
    }

    public void setWb1StopBit(BigDecimal wb1StopBit) {
        this.wb1StopBit = wb1StopBit;
    }

    public Integer getWb1ParityControl() {
        return wb1ParityControl;
    }

    public void setWb1ParityControl(int wb1ParityControl) {
        this.wb1ParityControl = wb1ParityControl;
    }

    public Boolean getWb1Mettler() {
        return wb1Mettler;
    }

    public void setWb1Mettler(boolean wb1Mettler) {
        this.wb1Mettler = wb1Mettler;
    }

    public String getWb2Port() {
        return wb2Port;
    }

    public void setWb2Port(String wb2Port) {
        this.wb2Port = wb2Port;
    }

    public Integer getWb2BaudRate() {
        return wb2BaudRate;
    }

    public void setWb2BaudRate(int wb2BaudRate) {
        this.wb2BaudRate = wb2BaudRate;
    }

    public Integer getWb2DataBit() {
        return wb2DataBit;
    }

    public void setWb2DataBit(int wb2DataBit) {
        this.wb2DataBit = wb2DataBit;
    }

    public BigDecimal getWb2StopBit() {
        return wb2StopBit;
    }

    public void setWb2StopBit(BigDecimal wb2StopBit) {
        this.wb2StopBit = wb2StopBit;
    }

    public Integer getWb2ParityControl() {
        return wb2ParityControl;
    }

    public void setWb2ParityControl(int wb2ParityControl) {
        this.wb2ParityControl = wb2ParityControl;
    }

    public Boolean getWb2Mettler() {
        return wb2Mettler;
    }

    public void setWb2Mettler(boolean wb2Mettler) {
        this.wb2Mettler = wb2Mettler;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public boolean isModeNormal() {
        return modeNormal;
    }

    public void setModeNormal(boolean modeNormal) {
        this.modeNormal = modeNormal;
    }

    public String getRptId() {
        return rptId;
    }

    public void setRptId(String rptId) {
        this.rptId = rptId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getWb1Delay() {
        return wb1Delay;
    }

    public void setWb1Delay(int wb1Delay) {
        this.wb1Delay = wb1Delay;
    }

    public int getWb2Delay() {
        return wb2Delay;
    }

    public void setWb2Delay(int wb2Delay) {
        this.wb2Delay = wb2Delay;
    }

    public int getWb1Step() {
        return wb1Step;
    }

    public void setWb1Step(int wb1Step) {
        this.wb1Step = wb1Step;
    }

    public int getWb2Step() {
        return wb2Step;
    }

    public void setWb2Step(int wb2Step) {
        this.wb2Step = wb2Step;
    }

    public String getWb1MettlerParam() {
        return wb1MettlerParam;
    }

    public void setWb1MettlerParam(String wb1MettlerParam) {
        this.wb1MettlerParam = wb1MettlerParam;
    }

    public String getWb2MettlerParam() {
        return wb2MettlerParam;
    }

    public void setWb2MettlerParam(String wb2MettlerParam) {
        this.wb2MettlerParam = wb2MettlerParam;
    }

    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = tolerance;
    }

    public Boolean getWb1AutoSignal() {
        return wb1AutoSignal;
    }

    public void setWb1AutoSignal(Boolean wb1AutoSignal) {
        this.wb1AutoSignal = wb1AutoSignal;
    }

    public Boolean getWb2AutoSignal() {
        return wb2AutoSignal;
    }

    public void setWb2AutoSignal(Boolean wb2AutoSignal) {
        this.wb2AutoSignal = wb2AutoSignal;
    }

    public String getSapUser() {
        return sapUser;
    }

    public void setSapUser(String sapUser) {
        this.sapUser = sapUser;
    }

    public String getSapPass() {
        return sapPass;
    }

    public void setSapPass(String sapPass) {
        this.sapPass = sapPass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Configuration configuration = (Configuration) o;
        return id == configuration.getId();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.sapHost);
        hash = 97 * hash + Objects.hashCode(this.sapGwHost);
        hash = 97 * hash + Objects.hashCode(this.sapRouteString);
        hash = 97 * hash + Objects.hashCode(this.sapSystemNumber);
        hash = 97 * hash + Objects.hashCode(this.sapClient);
        hash = 97 * hash + Objects.hashCode(this.wkPlant);
        hash = 97 * hash + Objects.hashCode(this.wbId);
        hash = 97 * hash + Objects.hashCode(this.wb1Port);
        hash = 97 * hash + Objects.hashCode(this.wb1BaudRate);
        hash = 97 * hash + Objects.hashCode(this.wb1DataBit);
        hash = 97 * hash + Objects.hashCode(this.wb1StopBit);
        hash = 97 * hash + Objects.hashCode(this.wb1ParityControl);
        hash = 97 * hash + Objects.hashCode(this.wb1Mettler);
        hash = 97 * hash + Objects.hashCode(this.wb2Port);
        hash = 97 * hash + Objects.hashCode(this.wb2BaudRate);
        hash = 97 * hash + Objects.hashCode(this.wb2DataBit);
        hash = 97 * hash + Objects.hashCode(this.wb2StopBit);
        hash = 97 * hash + Objects.hashCode(this.wb2ParityControl);
        hash = 97 * hash + Objects.hashCode(this.wb2Mettler);
        hash = 97 * hash + this.weightLimit;
        hash = 97 * hash + (this.modeNormal ? 1 : 0);
        hash = 97 * hash + Objects.hashCode(this.rptId);
        hash = 97 * hash + Objects.hashCode(this.createdDate);
        hash = 97 * hash + Objects.hashCode(this.updatedDate);
        hash = 97 * hash + Objects.hashCode(this.wb1Delay);
        hash = 97 * hash + Objects.hashCode(this.wb2Delay);
        hash = 97 * hash + Objects.hashCode(this.wb1Step);
        hash = 97 * hash + Objects.hashCode(this.wb2Step);
        hash = 97 * hash + Objects.hashCode(this.wb1MettlerParam);
        hash = 97 * hash + Objects.hashCode(this.wb2MettlerParam);

        return hash;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.Configuration[id=" + id + "]";
    }
}
