package org.example.service;

import org.example.document.HtmlDocument;
import org.example.visitor.TreePrintVisitor;

public class TreePrinter {

    public static void print(HtmlDocument document, boolean showId) {
        TreePrintVisitor visitor = new TreePrintVisitor(showId);
        document.accept(visitor);
        System.out.println(visitor.getTreeOutput());
    }

    public static String getTreeOutput(HtmlDocument document, boolean showId) {
        TreePrintVisitor visitor = new TreePrintVisitor();
        document.accept(visitor);
        return visitor.getTreeOutput();
    }
}