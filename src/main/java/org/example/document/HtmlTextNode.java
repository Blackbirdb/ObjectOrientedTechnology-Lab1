package org.example.document;

import org.example.visitor.HtmlVisitor;

public class HtmlTextNode extends HtmlNode {
    private String text;
    private HtmlElement parent;

    public HtmlTextNode(String text, HtmlElement parent) {
        this.text = text;
        this.parent = parent;
    }

    @Override
    public void accept(HtmlVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public HtmlElement getParent() {
        return parent;
    }

    public void setParent(HtmlElement parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}