    package org.example.editor.commands;

    import org.example.document.HtmlDocument;
    import org.example.document.HtmlTreeVisitor;

    public class PrintTreeCommand implements IrrevocableCommand {
        private final HtmlDocument document;
        private final HtmlTreeVisitor visitor;

        public PrintTreeCommand(HtmlDocument document, boolean showId) {
            this.document = document;
            this.visitor = new HtmlTreeVisitor(showId);
        }

        public void execute() {
            if (document.getRoot() == null) {
                System.out.println("Document is empty.");
                return;
            }

            document.getRoot().accept(visitor);
            System.out.println(visitor.getOutput());
        }
    }
