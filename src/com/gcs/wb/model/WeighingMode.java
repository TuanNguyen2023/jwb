package com.gcs.wb.model;

import com.gcs.wb.base.constant.Constants.WeighingProcess.MODE_DETAIL;

/**
 *
 * @author thanghl.qx
 */
public class WeighingMode {

    private MODE_DETAIL modeDetail;
    private String title;

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
}
