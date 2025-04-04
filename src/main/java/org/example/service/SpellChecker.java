package org.example.service;

import org.example.document.HtmlDocument;
import org.example.visitor.SpellCheckVisitor;
import org.languagetool.rules.RuleMatch;

import java.util.List;
import java.util.Map;

public class SpellChecker {

    public static Map<String, List<RuleMatch>> getErrorMap(HtmlDocument document) {
        SpellCheckVisitor visitor = new SpellCheckVisitor();
        document.accept(visitor);
        return visitor.getErrorMap();
    }

    public static String getErrorMapAsString(HtmlDocument document) {
        StringBuilder builder = new StringBuilder();
        Map<String, List<RuleMatch>> errorMap = getErrorMap(document);
        for (Map.Entry<String, List<RuleMatch>> entry : errorMap.entrySet()) {
            String errorTagId = entry.getKey();
            List<RuleMatch> errors = entry.getValue();
            builder.append("ElementId: ").append(errorTagId).append("\n");
            for (RuleMatch error : errors) {
                builder.append(" - ").append(error.getMessage()).append(" (")
                        .append(error.getFromPos()).append(":").append(error.getToPos()).append(")\n");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static void printErrorMap(HtmlDocument document) {
        String errors = getErrorMapAsString(document);
        if (!errors.isEmpty()) {
            System.out.println("Errors found:");
            System.out.println(errors);
        }
        else {
            System.out.println("No errors found.");
        }
    }
}
