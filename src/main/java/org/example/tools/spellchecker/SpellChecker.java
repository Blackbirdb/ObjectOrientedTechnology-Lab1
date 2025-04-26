package org.example.tools.spellchecker;

import org.languagetool.rules.RuleMatch;

import java.util.List;

public interface SpellChecker {
    List<RuleMatch> checkText(String text);
}
