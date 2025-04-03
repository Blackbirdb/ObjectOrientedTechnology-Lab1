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

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public HtmlElement getParent() {
        return parent;
    }

    /**
     * Insert a new child before the specified reference child.
     * @param newChild
     * @param refChild
     */
    public void insertBefore(HtmlElement newChild, HtmlElement refChild) {
        assert refChild.getParent() == this;

        int index = children.indexOf(refChild);
        children.add(index, newChild);
        newChild.setParent(this);
    }

    public void insertAtLast(HtmlNode newChild) {
        children.add(newChild);
    }

    public void insertAtIndex(int index, HtmlNode newChild) {
        if (index < 0 || index > children.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + children.size());
        }
        children.add(index, newChild);
    }

    public void removeChild(HtmlNode child) {
        children.remove(child);
    }

    public void setParent(HtmlElement parent) {
        this.parent = parent;
    }

    public int getChildIndex(HtmlNode child) {
        return children.indexOf(child);
    }
}