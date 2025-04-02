package org.example.document;

import org.example.visitor.NodeVisitor;

public class HtmlTextNode extends HtmlNode {
    private String text;
    private HtmlElement parent;

    public HtmlTextNode(String text, HtmlElement parent) {
        this.text = text;
        this.parent = parent;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public HtmlElement getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }
}