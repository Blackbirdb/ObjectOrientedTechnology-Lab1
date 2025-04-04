package org.example.service;

import org.example.document.HtmlDocument;
import org.example.document.HtmlNode;
import org.example.visitor.SpellCheckVisitor;
import org.languagetool.rules.RuleMatch;

import java.util.List;
import java.util.Map;

public class SpellChecker {
    private final SpellCheckVisitor visitor;
    private final HtmlDocument document;

    public SpellChecker(HtmlDocument document) {
        this.document = document;
        this.visitor = new SpellCheckVisitor();
    }

    public SpellChecker(HtmlDocument document, SpellCheckVisitor visitor) {
        this.document = document;
        this.visitor = visitor;
    }

    public boolean hasErrors() {
        document.accept(visitor);
        return visitor.hasErrors();
    }

    public Map<String, List<RuleMatch>> getErrorMap() {
        document.accept(visitor);
        return visitor.getErrorMap();
    }

    public String getErrorMapAsString() {
        StringBuilder builder = new StringBuilder();
        Map<String, List<RuleMatch>> errorMap = getErrorMap();
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

    public void printErrorMap() {
        String errors = getErrorMapAsString();
        System.out.println(errors);
    }
}
