package org.example.tools.spellchecker;


import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpellCheckerService {
    private final SpellChecker spellChecker;

    @Autowired
    public SpellCheckerService(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    public boolean hasErrors(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return !spellChecker.checkText(text).isEmpty();
    }

    public void checkSpelling(HtmlDocument document) {
        StringBuilder builder = new StringBuilder();

        for (HtmlElement element : document.getIdToElementMap().values()) {
            if (hasErrors(element.getTextContent())) {
                String id = element.getId();
                List<RuleMatch> errors = spellChecker.checkText(element.getTextContent());

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
