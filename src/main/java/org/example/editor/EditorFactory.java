package org.example.editor;

import org.example.document.HtmlDocument;
import org.example.document.HtmlTreeVisitor;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;
import org.example.tools.spellchecker.SpellChecker;
import org.example.tools.spellchecker.SpellCheckerService;
import org.example.tools.treeprinter.Visitor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditorFactory {
    private final SpellCheckerService spellChecker;
    private final FileParserService fileParser;
    private final HtmlTreeVisitor visitor;
    private final ObjectProvider<CommandHistory> commandHistoryObjectProvider;
    private final ObjectProvider<HtmlDocument> htmlDocumentObjectProvider;

    @Autowired
    public EditorFactory(SpellCheckerService checker, FileParserService fileParser, HtmlTreeVisitor visitor,
                         ObjectProvider<CommandHistory> commandHistoryObjectProvider,
                         ObjectProvider<HtmlDocument> htmlDocumentObjectProvider) {
        this.spellChecker = checker;
        this.fileParser = fileParser;
        this.visitor = visitor;
        this.commandHistoryObjectProvider = commandHistoryObjectProvider;
        this.htmlDocumentObjectProvider = htmlDocumentObjectProvider;
    }

    public HtmlEditor createEditor(String filePath, Boolean showId) {
        HtmlDocument document = htmlDocumentObjectProvider.getIfAvailable();
        fileParser.readHtmlFromFile(filePath, document);
        CommandHistory commandHistory = commandHistoryObjectProvider.getIfAvailable();
        return new HtmlEditor(spellChecker, fileParser, filePath, showId, visitor, commandHistory, document);
    }
}