package org.example.document;

import org.example.visitor.HtmlVisitor;

import java.util.List;

public abstract class HtmlNode {
    public abstract void accept(HtmlVisitor visitor);
    public abstract HtmlElement getParent();
    public boolean isLastChild() {
        HtmlElement parent = getParent();
        if (parent == null) {
            return true;
        }
        List<HtmlNode> siblings = parent.getChildren();
        return siblings.indexOf(this) == siblings.size() - 1;
    }

    public boolean parentIsLastChild() {
        HtmlElement parent = getParent();
        if (parent == null) {
            return true;
        }
        return parent.isLastChild();
    }
}