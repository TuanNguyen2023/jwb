/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.controller;

import com.gcs.wb.base.enums.ParityEnum;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.model.AppConfig;
import com.gcs.wb.service.ConfigService;

/**
 *
 * @author THANGPT
 */
public class ConfigController {

    private final ConfigService configService = new ConfigService();

    public Configuration getConfiguration() {
        return configService.getConfiguration();
    }

    public ParityEnum getPlControl(AppConfig config, int getBPC) {

        ParityEnum p1Control = null;

        switch (getBPC) {
            case 0:
                p1Control = ParityEnum.NONE;
                break;
            case 1:
                p1Control = ParityEnum.ODD;
                break;
            case 2:
                p1Control = ParityEnum.EVEN;
                break;
            case 3:
                p1Control = ParityEnum.MARK;
                break;
            case 4:
                p1Control = ParityEnum.SPACE;
                break;
        }
        return p1Control;

    }
}
