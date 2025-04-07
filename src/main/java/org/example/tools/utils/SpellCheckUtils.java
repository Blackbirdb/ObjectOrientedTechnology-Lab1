package org.example.tools.utils;

import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;

import java.io.IOException;
import java.util.List;

public class SpellCheckUtils {
    private static final JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode("en-GB"));

    public static boolean hasErrors(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        try {
            return !langTool.check(text).isEmpty();
        } catch (IOException e) {
            throw new RuntimeException("Spell check failed", e);
        }
    }

    public static List<RuleMatch> checkText(String text) {
        try {
            return langTool.check(text);

        } catch (IOException e) {
            throw new RuntimeException("LanguageTool check failed", e);
        }
    }

}
