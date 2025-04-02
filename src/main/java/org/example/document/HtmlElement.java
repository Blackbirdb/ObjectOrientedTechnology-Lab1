package org.example.document;

import org.example.visitor.NodeVisitor;

import java.util.List;

public class HtmlElement extends HtmlNode {
    private final String tagName;
    private final String id;
    private final String textContent;
    private final List<HtmlNode> children;
    private final HtmlElement parent;

    public HtmlElement(String tagName, String id, String textContent, List<HtmlNode> children, HtmlElement parent) {
        this.tagName = tagName;
        this.id = id;
        this.textContent = textContent;
        this.children = children;
        this.parent = parent;
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

    @Override
    public HtmlElement getParent() {
        return parent;
    }

    public void insertBefore(HtmlNode newChild, HtmlNode refChild) {
        if (refChild.getParent() != this) {
            throw new IllegalArgumentException("Reference child is not a child of this element");
        }

        int index = children.indexOf(refChild);
        children.add(index, newChild);
    }

    public void removeChild(HtmlNode child) {
        children.remove(child);
    }
}