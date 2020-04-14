package com.gcs.wb.base.comboboxfilter;

import com.gcs.wb.base.util.StringUtil;
import com.gcs.wb.jpa.entity.Customer;
import com.gcs.wb.jpa.entity.Vendor;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class CustomComboRenderer extends DefaultListCellRenderer {

    public static final Color background = Color.WHITE;
    private static final Color defaultBackground = (Color) UIManager.get("List.background");
    private static final Color defaultForeground = (Color) UIManager.get("List.foreground");
    private Supplier<String> highlightTextSupplier;

    public CustomComboRenderer(Supplier<String> highlightTextSupplier) {
        this.highlightTextSupplier = highlightTextSupplier;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Customer) {
            Customer customer = (Customer) value;
            String name = customer.getName2();
            if (!StringUtil.isEmptyString(customer.getName3())) {
                name += " " + customer.getName3();
            }
            if (!StringUtil.isEmptyString(customer.getName4())) {
                name += " " + customer.getName4();
            }
            name = HtmlHighlighter.highlightText(name, highlightTextSupplier.get());
            setText(name);
            setToolTipText(customer.getKunnr());
        }

        if (value instanceof Vendor) {
            Vendor vendor = (Vendor) value;
            String name = vendor.getName1() + " " + vendor.getName2();
            name = HtmlHighlighter.highlightText(name, highlightTextSupplier.get());
            setText(name);
            setToolTipText(vendor.getLifnr());
        }
        if (!isSelected) {
            this.setBackground(index % 2 == 0 ? background : defaultBackground);
        }
        this.setForeground(defaultForeground);
        return this;
    }
}
