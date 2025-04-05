package org.example.document;

public interface HtmlVisitor {
    void visit(HtmlElement element);
    void visit(HtmlTextNode textNode);
}