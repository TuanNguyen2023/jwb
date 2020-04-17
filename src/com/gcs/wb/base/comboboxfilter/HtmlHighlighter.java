package com.gcs.wb.base.comboboxfilter;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class HtmlHighlighter {

    private static final String HighLightTemplate = "<span style='background:yellow;'>$1</span>";

    public static String highlightText(String text, String textToHighlight) {
        if (textToHighlight.length() == 0) {
            return text;
        }
        String lowerText = text.toLowerCase();
        lowerText = removeAccent(lowerText);
        String realText = "";
        textToHighlight = removeAccent(textToHighlight);
        if (lowerText.contains(textToHighlight.toLowerCase())) {
            int index = lowerText.indexOf(textToHighlight.toLowerCase());
            realText = text.substring(index, index + textToHighlight.length());
        }
        try {
            text = text.replaceAll("(?i)(" + Pattern.quote(realText) + ")", HighLightTemplate);
        } catch (Exception e) {
            return text;
        }
        return "<html>" + text + "</html>";
    }

    public static String removeAccent(String s) {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
    
}
