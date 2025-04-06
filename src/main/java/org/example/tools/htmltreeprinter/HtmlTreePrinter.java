package org.example.tools.htmltreeprinter;

import org.example.document.HtmlDocument;
import org.example.document.HtmlElement;

public class HtmlTreePrinter {

    public void print(HtmlElement root, boolean showId) {
        TreePrintVisitor visitor = new TreePrintVisitor(showId);
        root.accept(visitor);
        System.out.println(visitor.getTreeOutput());
    }

    public String getTreeOutput(HtmlElement root, boolean showId) {
        TreePrintVisitor visitor = new TreePrintVisitor();
        root.accept(visitor);
        return visitor.getTreeOutput();
    }
}