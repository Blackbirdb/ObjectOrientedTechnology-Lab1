package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.spellcheck.SpellChecker;

public class SpellCheckCommand implements IrrevocableCommand{
    private HtmlDocument document;

    public SpellCheckCommand(HtmlDocument document) {
        this.document = document;
    }

    public void execute() {
        SpellChecker.printErrorMap(document);
    }
}
