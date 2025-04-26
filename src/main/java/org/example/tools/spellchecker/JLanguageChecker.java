package org.example.tools.spellchecker;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;
import org.example.editor.commands.SpellCheckCommand;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


@Component
public class JLanguageChecker implements SpellChecker {
    private final JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));

    @Override
    public boolean hasErrors(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            return !langTool.check(text).isEmpty();
        } catch (IOException e) {
            throw new RuntimeException("Spell check failed", e);
        }
    }

    @Override
    public List<RuleMatch> checkText(String text) {
        try {
            return langTool.check(text);

        } catch (IOException e) {
            throw new RuntimeException("LanguageTool check failed", e);
        }
    }

    @Override
    public void checkSpelling(HtmlDocument document) {
        StringBuilder builder = new StringBuilder();

        for (HtmlElement element : document.getIdToElementMap().values()) {
            if (hasErrors(element.getTextContent())) {
                String id = element.getId();
                List<RuleMatch> errors = checkText(element.getTextContent());

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
