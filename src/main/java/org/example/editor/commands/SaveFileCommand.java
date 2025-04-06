package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.htmlparser.HtmlFileParser;
import org.example.tools.htmlparser.HtmlParserUtils;

public class SaveFileCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final String filePath;
    private final HtmlFileParser parser = new HtmlFileParser();

    public SaveFileCommand(HtmlDocument document, String filePath) {
        this.document = document;
        this.filePath = filePath;
    }

    public void execute() {
        parser.saveHtmlDocumentToFile(document, filePath);
    }
}
