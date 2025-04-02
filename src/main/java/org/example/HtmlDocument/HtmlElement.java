package org.example.HtmlDocument;

import org.example.visitor.NodeVisitor;

import java.util.List;
import java.util.Map;

public class HtmlElement extends HtmlNode {
    private String tagName;
    private String id;
    private String textContent;
    private List<HtmlNode> children;

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);

        for (HtmlNode child : children) {
            child.accept(visitor);
        }
    }
}