package org.example.command;

import org.example.document.HtmlDocument;
import org.example.service.HtmlFileParser;
import org.example.service.TreePrinter;

import java.io.IOException;

public class PrintTreeCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final boolean showId;

    public PrintTreeCommand(HtmlDocument document, boolean showId) {
        this.document = document;
        this.showId = showId;
    }

    public void execute() throws IOException {
        TreePrinter.print(document, showId);
    }
}
