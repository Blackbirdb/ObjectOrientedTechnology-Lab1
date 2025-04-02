package org.example.document;

import org.example.visitor.NodeVisitor;

public class HtmlTextNode extends HtmlNode {
    private String text;

    public HtmlTextNode(String text) {
        this.text = text;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }
}