package org.example.tools.spellchecker;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class SpellChecker {
    private final HtmlDocument document;
    private final SpellCheckUtils spellCheckUtils;

    public SpellChecker(HtmlDocument document) {
        this.document = document;
        this.spellCheckUtils = new SpellCheckUtils();
    }

    public SpellChecker(HtmlDocument document, SpellCheckUtils spellCheckUtils) {
        this.document = document;
        this.spellCheckUtils = spellCheckUtils;
    }

    public void checkSpelling() {
        StringBuilder builder = new StringBuilder();

        for (HtmlElement element : document.getIdToElementMap().values()) {
            if (spellCheckUtils.hasErrors(element.getTextContent())) {
                String id = element.getId();
                List<RuleMatch> errors = spellCheckUtils.checkText(element.getTextContent());

                builder.append("ElementId: ").append(id).append("\n");
                for (RuleMatch error : errors) {
                    builder.append(" - ").append(error.getMessage()).append(" (")
                            .append(error.getFromPos()).append(":").append(error.getToPos()).append(")\n");
                }
                builder.append("\n");
            }
        }

        String errors = builder.toString();

        if (!errors.isEmpty()) {
            System.out.println("Errors found:");
            System.out.println(errors);
        } else {
            System.out.println("No errors found.");
        }
    }
}
