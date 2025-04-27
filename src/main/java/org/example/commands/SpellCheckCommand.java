package org.example.commands;

import org.example.document.HtmlDocument;
import org.example.tools.spellchecker.SpellCheckerService;

public class SpellCheckCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final SpellCheckerService spellChecker;

    public SpellCheckCommand(HtmlDocument document, SpellCheckerService spellChecker) {
        this.document = document;
        this.spellChecker = spellChecker;
    }

    public void execute() {
        spellChecker.checkSpelling(document);
    }

}