package org.example.editor;

import org.example.document.HtmlTreeVisitor;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.example.tools.treeprinter.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditorFactory {
    private final SpellCheckerService spellChecker;
    private final FileParserService fileParser;
    private final HtmlTreeVisitor visitor;
    private final CommandHistory commandHistory;

    @Autowired
    public EditorFactory(SpellCheckerService checker, FileParserService fileParser, HtmlTreeVisitor visitor, CommandHistory commandHistory) {
        this.spellChecker = checker;
        this.fileParser = fileParser;
        this.visitor = visitor;
        this.commandHistory = commandHistory;
    }

    public HtmlEditor createEditor(String filePath, Boolean showId) {
        return new HtmlEditor(spellChecker, fileParser, filePath, showId, visitor, commandHistory);
    }
}