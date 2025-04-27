package org.example.commands;

import org.example.document.HtmlDocument;
import org.example.tools.htmlparser.FileParserService;

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
