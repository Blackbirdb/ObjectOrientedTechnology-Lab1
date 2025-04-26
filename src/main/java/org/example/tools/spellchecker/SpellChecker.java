package org.example.tools.spellchecker;

import org.example.document.HtmlDocument;
import org.languagetool.rules.RuleMatch;

import java.util.List;

public interface SpellChecker {
    boolean hasErrors(String text);
    List<RuleMatch> checkText(String text);
    void checkSpelling(HtmlDocument document);
}
