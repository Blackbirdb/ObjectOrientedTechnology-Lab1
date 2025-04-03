package org.example.service;

import org.example.document.HtmlDocument;
import org.example.visitor.SpellCheckVisitor;

public class SpellChecker {
    private final SpellCheckVisitor visitor = new SpellCheckVisitor();
    private final HtmlDocument document;

    public SpellChecker(HtmlDocument document) {
        this.document = document;
    }

    public boolean hasErrors() {
        document.accept(visitor);
        return visitor.hasErrors();
    }
}
