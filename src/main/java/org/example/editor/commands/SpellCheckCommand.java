package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.spellcheck.SpellChecker;

public class SpellCheckCommand implements IrrevocableCommand{
    private final HtmlDocument document;
    private final SpellChecker spellChecker;

    public SpellCheckCommand(HtmlDocument document) {
        this.document = document;
        spellChecker = new SpellChecker();
    }

    public SpellCheckCommand(HtmlDocument document, SpellChecker spellChecker) {
        this.document = document;
        this.spellChecker = spellChecker;
    }

    public void execute() {
        spellChecker.printErrorMap(document.getRoot());
    }
}
