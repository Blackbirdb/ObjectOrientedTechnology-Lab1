package org.example.document;

import org.example.visitor.NodeVisitor;

import java.util.List;

public class HtmlElement extends HtmlNode {
    private final String tagName;
    private final String id;
    private final String textContent;
    private final List<HtmlNode> children;

    public HtmlElement(String tagName, String id, String textContent, List<HtmlNode> children) {
        this.tagName = tagName;
        this.id = id;
        this.textContent = textContent;
        this.children = children;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);

        for (HtmlNode child : children) {
            child.accept(visitor);
        }
    }

    public String getId() {
        return id;
    }

    public List<HtmlNode> getChildren() {
        return children;
    }

    public void addChild(HtmlNode childNode) {
        this.children.add(childNode);
    }

    public String getTextContent() {
        return textContent;
    }

    public String getTagName() {
        return tagName;
    }
}