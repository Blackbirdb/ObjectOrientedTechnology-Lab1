package org.example.tools.spellchecker;

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
    public List<RuleMatch> checkText(String text) {
        try {
            return langTool.check(text);

        } catch (IOException e) {
            throw new RuntimeException("LanguageTool check failed", e);
        }
    }

}
