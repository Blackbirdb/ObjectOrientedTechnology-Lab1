package org.example.visitor;

import org.example.document.HtmlElement;
import org.example.document.HtmlTextNode;

public interface HtmlVisitor {
    void visit(HtmlElement element);
    void visit(HtmlTextNode textNode);
}