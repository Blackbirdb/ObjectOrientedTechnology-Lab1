package org.example.HtmlDocument;

import org.example.visitor.NodeVisitor;

public class HtmlTextNode extends HtmlNode {
    private String text;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}