package org.example.service;

import org.example.document.HtmlDocument;
import org.example.visitor.TreePrintVisitor;

public class TreePrinter {
    private final HtmlDocument document;
    private final TreePrintVisitor visitor = new TreePrintVisitor();

    public TreePrinter(HtmlDocument document) {
        this.document = document;
    }

    public String print() {
        document.accept(visitor);
        return visitor.getTreeOutput();
    }
}