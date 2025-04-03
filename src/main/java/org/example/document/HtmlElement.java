package org.example.document;

import org.example.visitor.HtmlVisitor;

import java.util.ArrayList;
import java.util.List;

public class HtmlElement extends HtmlNode {
    private String tagName;
    private String id;
    private List<HtmlNode> children;
    private HtmlElement parent;

    public HtmlElement(String tagName, String id, HtmlElement parent) {
        this.tagName = tagName;
        this.id = id;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public void accept(HtmlVisitor visitor) {
        visitor.visit(this);
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


    /**
     * Get the text content of the first child node if it is a text node.
     * @return String: text context of the given node
     */
    public String getTextContent() {
        if (!children.isEmpty() && children.getFirst() instanceof HtmlTextNode) {
            return ((HtmlTextNode) children.getFirst()).getText();
        }
        return "";
    }

    /**
     * Set the text content of the first child node if it is a text node.
     * @param textContent: String
     */
    public void setTextContent(String textContent) {
        if (!children.isEmpty() && children.getFirst() instanceof HtmlTextNode) {
            ((HtmlTextNode) children.getFirst()).setText(textContent);
        } else {
            HtmlTextNode textNode = new HtmlTextNode(textContent, this);
            children.addFirst(textNode);
        }
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