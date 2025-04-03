package org.example.visitor;

import org.example.document.HtmlElement;

public interface HtmlVisitor {
    void visit(HtmlElement element);
}