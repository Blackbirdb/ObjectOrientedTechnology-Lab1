package org.example.document;

import lombok.Getter;
import lombok.Setter;
import org.example.visitor.HtmlVisitor;

import java.util.List;

@Setter
@Getter
public abstract class HtmlNode {
    protected HtmlElement parent;

    public abstract void accept(HtmlVisitor visitor);

    public boolean isLastChild() {
        HtmlElement parent = getParent();
        if (parent == null) {
            return true;
        }
        List<HtmlNode> siblings = parent.getChildren();
        if (siblings == null || siblings.isEmpty()) {
            throw new IllegalStateException("Parent has no children");
        }
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}