package org.example.service;

import org.example.document.HtmlDocument;
import org.example.visitor.TreePrintVisitor;

public class TreePrinter {
    private final HtmlDocument document;
    private final TreePrintVisitor visitor = new TreePrintVisitor();

    public TreePrinter(HtmlDocument document) {
        this.document = document;
    }

    public void print() {
        document.accept(visitor);
        System.out.println(visitor.getTreeOutput());
    }

    public String getTreeOutput() {
        document.accept(visitor);
        return visitor.getTreeOutput();
    }
}