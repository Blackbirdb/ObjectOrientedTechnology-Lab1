    package org.example.editor.commands;

    import org.example.document.HtmlDocument;
    import org.example.tools.htmltreeprinter.HtmlTreePrinter;

    public class PrintTreeCommand implements IrrevocableCommand {
        private final HtmlDocument document;
        private final boolean showId;
        private final HtmlTreePrinter printer;

        public PrintTreeCommand(HtmlDocument document, boolean showId) {
            this.document = document;
            this.showId = showId;
            this.printer = new HtmlTreePrinter();
        }

        public PrintTreeCommand(HtmlDocument document, boolean showId, HtmlTreePrinter printer) {
            this.document = document;
            this.showId = showId;
            this.printer = printer;
        }

        public void execute() {
            printer.print(document.getRoot(), showId);
        }
    }
