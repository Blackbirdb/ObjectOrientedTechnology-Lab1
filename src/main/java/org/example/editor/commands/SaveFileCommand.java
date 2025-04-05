package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.htmlparser.HtmlFileParser;

public class SaveFileCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final String filePath;

    public SaveFileCommand(HtmlDocument document, String filePath) {
        this.document = document;
        this.filePath = filePath;
    }

    public void execute() {
        HtmlFileParser.saveHtmlDocumentToFile(document, filePath);
    }

}
