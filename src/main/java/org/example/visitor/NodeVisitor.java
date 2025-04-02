package org.example.visitor;

import org.example.HtmlDocument.HtmlElement;
import org.example.HtmlDocument.HtmlTextNode;

public interface NodeVisitor {
    void visit(HtmlElement element);
    void visit(HtmlTextNode textNode);
}