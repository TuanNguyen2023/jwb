package com.gcs.wb.base.comboboxfilter;

import static com.gcs.wb.views.WTRegView.getCustomDisplayText;
import static com.gcs.wb.views.WTRegView.getCustomToolTip;
import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class CustomComboRenderer extends DefaultListCellRenderer {

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
        
        String name = getCustomDisplayText(value);
        setText(HtmlHighlighter.highlightText(name, highlightTextSupplier.get()));
        
        String tooltip = getCustomToolTip(value);
        if ("".equals(tooltip)) {
            tooltip = name;
        }
        setToolTipText(tooltip);
        this.setForeground(defaultForeground);
        return this;
    }
}
