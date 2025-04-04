package org.example.document;

import org.example.visitor.HtmlVisitor;

public class HtmlTextNode extends HtmlNode {
    private String text;

    public HtmlTextNode(String text, HtmlElement parent) {
        this.text = text;
        this.parent = parent;
    }

    @Override
    public void accept(HtmlVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}