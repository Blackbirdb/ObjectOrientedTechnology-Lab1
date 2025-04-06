package org.example.document;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class HtmlElement extends HtmlNode {
    private final String tagName;
    @Setter
    private String id;
    private final List<HtmlNode> children;

    public HtmlElement(String tagName, String id, HtmlElement parent) {
        this.tagName = tagName;
        this.id = id;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<").append(tagName);
        if (id != null) {
            sb.append(" id=\"").append(id).append("\"");
        }
        sb.append(">\n");
        for (HtmlNode child : children) {
            if (child instanceof HtmlTextNode) {
                sb.append("text: ").append(((HtmlTextNode) child).getText()).append("\n");
            }
            else if (child instanceof HtmlElement) {
                sb.append("child: ").append(((HtmlElement)child).getTagName()).append("\n");
            }
        }
        sb.append("</").append(tagName).append(">\n");
        return sb.toString();
    }

    @Override
    public void accept(HtmlVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Get the text content of the first child node if it is a text node.
     * @return String: text context of the given node or null
     */
    public String getTextContent() {
        if (!children.isEmpty() && children.getFirst() instanceof HtmlTextNode) {
            return ((HtmlTextNode) children.getFirst()).getText();
        }
        return null;
    }

    /**
     * Set the text content of the first child node if it is a text node.
     * @param textContent: String
     */
    public void setTextContent(String textContent) {
        if (!children.isEmpty() && children.getFirst() instanceof HtmlTextNode) {
            if (textContent == null) {textContent = "";}
            ((HtmlTextNode) children.getFirst()).setText(textContent);
        } else {
            if (textContent != null) {
                HtmlTextNode textNode = new HtmlTextNode(textContent, this);
                children.addFirst(textNode);
            }
        }
    }

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

    public int getChildIndex(HtmlNode child) {
        assert child.getParent() == this;
        return children.indexOf(child);
    }
}