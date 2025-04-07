package org.example.tools.htmltreeprinter;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class HtmlTreePrinter {

    private final TreePrintVisitor visitor;

    public HtmlTreePrinter() {
        this.visitor = new TreePrintVisitor();
    }

    public HtmlTreePrinter(TreePrintVisitor visitor) {
        this.visitor = visitor;
    }

    public void print(HtmlElement root, boolean showId) {
        visitor.setShowId(showId);
        root.accept(visitor);
        System.out.println(visitor.getTreeOutput());
    }

}