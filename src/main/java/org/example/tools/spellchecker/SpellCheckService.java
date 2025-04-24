package org.example.tools.spellchecker;

import lombok.Setter;
import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.languagetool.rules.RuleMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpellCheckService {
    @Setter private HtmlDocument document;
    @Autowired private SpellChecker spellChecker;

//    @Autowired
//    public SpellCheckService(SpellChecker spellChecker) {
//        this.spellChecker = spellChecker;
//    }

    public void checkSpelling() {
        StringBuilder builder = new StringBuilder();

        for (HtmlElement element : document.getIdToElementMap().values()) {
            if (spellChecker.hasErrors(element.getTextContent())) {
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
