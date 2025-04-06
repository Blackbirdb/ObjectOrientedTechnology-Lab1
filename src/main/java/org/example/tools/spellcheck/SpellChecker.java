package org.example.tools.spellcheck;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.languagetool.rules.RuleMatch;

import java.util.List;
import java.util.Map;

public class SpellChecker {

    public Map<String, List<RuleMatch>> getErrorMap(HtmlElement root) {
        SpellCheckVisitor visitor = new SpellCheckVisitor();
        root.accept(visitor);
        return visitor.getErrorMap();
    }

    public String getErrorMapAsString(HtmlElement root) {
        StringBuilder builder = new StringBuilder();
        Map<String, List<RuleMatch>> errorMap = getErrorMap(root);
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

    public void printErrorMap(HtmlElement root) {
        String errors = getErrorMapAsString(root);
        if (!errors.isEmpty()) {
            System.out.println("Errors found:");
            System.out.println(errors);
        }
        else {
            System.out.println("No errors found.");
        }
    }
}
