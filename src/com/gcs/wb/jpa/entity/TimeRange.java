/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.jpa.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author admin
 */
@Entity
@Table(name = "time_range")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TimeRange.findByMandtWBID", query = "SELECT t FROM TimeRange t WHERE t.timeRangePK.mandt = :mandt AND t.timeRangePK.wbId = :wbId")
})
public class TimeRange implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TimeRangePK timeRangePK;
    @Column(name = "TIME_FROM")
    private String timeFrom;
    @Column(name = "TIME_TO")
    private String timeTo;
    @Column(name = "IP")
    private String ip;
    @Column(name = "PCName")
    private String pCName;
    @Column(name = "ModDate")
    private String modDate;
    @Column(name = "ModBy")
    private String modBy;
    @Column(name = "oldTimeFrom")
    private String oldTimeFrom;
    @Column(name = "oldTimeTo")
    private String oldTimeTo;
    @Column(name = "segment1")
    private String segment1;
    @Column(name = "segment2")
    private String segment2;
    @Column(name = "segment3")
    private String segment3;

    public TimeRange() {
    }

    public TimeRange(TimeRangePK timeRangePK) {
        this.timeRangePK = timeRangePK;
    }

    public TimeRange(String mandt, String wbId) {
        this.timeRangePK = new TimeRangePK(mandt, wbId);
    }

    public TimeRangePK getTimeRangePK() {
        return timeRangePK;
    }

    public void setTimeRangePK(TimeRangePK timeRangePK) {
        this.timeRangePK = timeRangePK;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPCName() {
        return pCName;
    }

    public void setPCName(String pCName) {
        this.pCName = pCName;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }

    public String getOldTimeFrom() {
        return oldTimeFrom;
    }

    public void setOldTimeFrom(String oldTimeFrom) {
        this.oldTimeFrom = oldTimeFrom;
    }

    public String getOldTimeTo() {
        return oldTimeTo;
    }

    public void setOldTimeTo(String oldTimeTo) {
        this.oldTimeTo = oldTimeTo;
    }

    public String getSegment1() {
        return segment1;
    }

    public void setSegment1(String segment1) {
        this.segment1 = segment1;
    }

    public String getSegment2() {
        return segment2;
    }

    public void setSegment2(String segment2) {
        this.segment2 = segment2;
    }

    public String getSegment3() {
        return segment3;
    }

    public void setSegment3(String segment3) {
        this.segment3 = segment3;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeRangePK != null ? timeRangePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeRange)) {
            return false;
        }
        TimeRange other = (TimeRange) object;
        if ((this.timeRangePK == null && other.timeRangePK != null) || (this.timeRangePK != null && !this.timeRangePK.equals(other.timeRangePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gcs.wb.jpa.entity.TimeRange[ timeRangePK=" + timeRangePK + " ]";
    }
}
