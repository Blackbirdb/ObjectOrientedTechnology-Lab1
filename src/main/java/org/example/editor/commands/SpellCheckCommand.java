package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.tools.utils.SpellChecker;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public class SpellCheckCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final SpellChecker spellChecker;

    public SpellCheckCommand(HtmlDocument document) {
        this.document = document;
        this.spellChecker = new SpellChecker();
    }

    public SpellCheckCommand(HtmlDocument document, SpellChecker spellChecker) {
        this.document = document;
        this.spellChecker = spellChecker;
    }

    public void execute() {
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