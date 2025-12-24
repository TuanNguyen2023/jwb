/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.base.util;

import com.gcs.wb.base.constant.Constants;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author thanghl
 */
public class DateUtil {

    public static TableCellRenderer getTableCellRendererForDate() {
        return getTableCellRendererForDate(false);
    }

    public static TableCellRenderer getTableCellRendererForDate(boolean isDateTime) {
        return new DefaultTableCellRenderer() {
            SimpleDateFormat f = new SimpleDateFormat(isDateTime ? Constants.Date.FORMAT_DATETIME : Constants.Date.FORMAT);

            @Override
            public Component getTableCellRendererComponent(JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                if (value instanceof Date) {
                    value = f.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };
    }
}
