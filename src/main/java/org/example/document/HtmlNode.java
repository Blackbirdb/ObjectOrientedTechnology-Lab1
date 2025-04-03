package org.example.document;

import org.example.visitor.HtmlVisitor;

public abstract class HtmlNode {
    public abstract void accept(HtmlVisitor visitor);
    public abstract HtmlElement getParent();
}