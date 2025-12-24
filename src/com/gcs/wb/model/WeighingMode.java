package com.gcs.wb.model;

import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;
import java.util.Objects;

/**
 *
 * @author thanghl.qx
 */
public class WeighingMode {

    private MODE_DETAIL modeDetail;
    private String title;

    public WeighingMode(MODE_DETAIL modeDetail) {
        this.modeDetail = modeDetail;
    }

    public WeighingMode(MODE_DETAIL modeDetail, String title) {
        this.modeDetail = modeDetail;
        this.title = title;
    }

    public MODE_DETAIL getModeDetail() {
        return modeDetail;
    }

    public void setModeDetail(MODE_DETAIL modeDetail) {
        this.modeDetail = modeDetail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.modeDetail);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WeighingMode other = (WeighingMode) obj;
        if (this.modeDetail != other.modeDetail) {
            return false;
        }
        return true;
    }

}
