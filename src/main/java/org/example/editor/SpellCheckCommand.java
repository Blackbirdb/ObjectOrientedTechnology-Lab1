package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.service.SpellChecker;

public class SpellCheckCommand implements IrrevocableCommand{
    private HtmlDocument document;

    public SpellCheckCommand(HtmlDocument document) {
        this.document = document;
    }

    public void execute() {
        SpellChecker.printErrorMap(document);
    }
}
