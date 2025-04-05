package org.example.editor.commands;

import org.example.document.HtmlDocument;
import org.example.tools.htmltreeprinter.HtmlTreePrinter;

public class PrintTreeCommand implements IrrevocableCommand {
    private final HtmlDocument document;
    private final boolean showId;

    public PrintTreeCommand(HtmlDocument document, boolean showId) {
        this.document = document;
        this.showId = showId;
    }

    public void execute() {
        HtmlTreePrinter.print(document.getRoot(), showId);
    }
}
