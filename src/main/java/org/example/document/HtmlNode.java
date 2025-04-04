package org.example.document;

import org.example.visitor.HtmlVisitor;

import java.util.List;

public abstract class HtmlNode {
    protected HtmlElement parent;

    public abstract void accept(HtmlVisitor visitor);
    public HtmlElement getParent(){
        return parent;
    };
    public void setParent(HtmlElement parent){ this.parent = parent; }

    public boolean isLastChild() {
        HtmlElement parent = getParent();
        if (parent == null) {
            return true;
        }
        List<HtmlNode> siblings = parent.getChildren();
        if (siblings == null || siblings.isEmpty()) {
            throw new IllegalStateException("Parent has no children");
        }
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}