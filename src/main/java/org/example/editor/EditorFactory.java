package org.example.editor;

import org.example.document.HtmlTreeVisitor;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.treeprinter.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditorFactory {
    private final SpellChecker spellChecker;
    private final HtmlFileParser fileParser;
    private final HtmlTreeVisitor visitor;

    @Autowired
    public EditorFactory(SpellChecker checker, HtmlFileParser fileParser, HtmlTreeVisitor visitor) {
        this.spellChecker = checker;
        this.fileParser = fileParser;
        this.visitor = visitor;
    }

    public HtmlEditor createEditor(String filePath, Boolean showId) {
        return new HtmlEditor(spellChecker, fileParser, filePath, showId, visitor);
    }
}