package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.htmlparser.FileParserService;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.JsoupFileParser;

public class SaveFileCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final String filePath;
    private final FileParserService parser;

    public SaveFileCommand(HtmlDocument document, String filePath, FileParserService parser) {
        this.document = document;
        this.filePath = filePath;
        this.parser = parser;
    }

    public void execute() {
        parser.saveHtmlDocumentToFile(document, filePath);
    }
}
