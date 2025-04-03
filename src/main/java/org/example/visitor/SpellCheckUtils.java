package org.example.visitor;

import org.example.document.HtmlDocument;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpellCheckUtils {
    private final JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));

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

    public List<String> getSuggestions(String text) {
        try {
            return langTool.check(text).stream()
                    .flatMap(match -> match.getSuggestedReplacements().stream())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

}
