package com.gcs.wb.base.comboboxfilter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Function;

public class FilterEditor<T> extends BasicComboBoxEditor {
    private JTextField filterText = new JTextField(76);
    private String text = "";
    boolean editing;
    private Function<T, String> displayTextFunction;
    private Consumer<Boolean> editingChangeListener;
    private Object selected;

    FilterEditor(Function<T, String> displayTextFunction,
                 Consumer<Boolean> editingChangeListener) {
        this.displayTextFunction = displayTextFunction;
        this.editingChangeListener = editingChangeListener;
        this.filterText.setDisabledTextColor(Color.black);
    }

    public void addChar(char c) {
        text += c;
        if (!editing) {
            enableEditingMode();
        }
    }

    public void removeCharAtEnd() {
        if ("".equals(text)) {
            text = filterText.getText();
        }
        if (text.length() > 0) {
            text = text.substring(0, text.length() - 1);
            if (!editing) {
                enableEditingMode();
            }
        }
    }
    public void enableEditingMode() {
        editing = true;
        filterText.setFont(filterText.getFont().deriveFont(Font.PLAIN));
        editingChangeListener.accept(true);
    }

    public void reset() {
        if (editing) {
            filterText.setFont(UIManager.getFont("ComboBox.font"));
            filterText.setForeground(UIManager.getColor("Label.foreground"));
            text = "";
            editing = false;
            editingChangeListener.accept(false);
        }
        filterText.setCaretPosition(0);
    }

    @Override
    public Component getEditorComponent() {
        return filterText;
    }

    public JTextField getFilterText() {
        return filterText;
    }

    @Override
    public void setItem(Object anObject) {
        if (!editing) {
            T t = (T) anObject;
            String text = displayTextFunction.apply(t);
            filterText.setText(displayTextFunction.apply(t));
        }
        this.selected = anObject;
    }

    @Override
    public Object getItem() {
        return selected;
    }

    @Override
    public void selectAll() {
    }

    @Override
    public void addActionListener(ActionListener l) {
    }

    @Override
    public void removeActionListener(ActionListener l) {
    }

    public boolean isEditing() {
        return editing;
    }

    public String getText() {
        return text;
    }
}