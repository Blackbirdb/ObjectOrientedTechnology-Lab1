package org.example.document;

import org.example.visitor.NodeVisitor;

import java.util.List;

public class HtmlElement extends HtmlNode {
    private String tagName;
    private String id;
    private String textContent;
    private List<HtmlNode> children;
    private HtmlElement parent;

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

    public void setId(String id) {
        this.id = id;
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
        assert refChild.getParent() == this;

        int index = children.indexOf(refChild);
        children.add(index, newChild);
    }

    public void insertAtLast(HtmlNode newChild) {
        children.add(newChild);
    }

    public void removeChild(HtmlNode child) {
        children.remove(child);
    }
}