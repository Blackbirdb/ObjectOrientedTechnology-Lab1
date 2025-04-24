package org.example.tools.spellchecker;

import org.languagetool.rules.RuleMatch;

import java.util.List;

public interface SpellChecker {
    boolean hasErrors(String text);
    List<RuleMatch> checkText(String text);
}
