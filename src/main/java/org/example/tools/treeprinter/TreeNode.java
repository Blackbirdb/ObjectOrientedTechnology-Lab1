package org.example.tools.treeprinter;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public abstract class TreeNode {
    protected InnerTreeNode parent;

    public abstract void accept(Visitor visitor);

    public boolean isLastChild() {
        InnerTreeNode parent = getParent();
        if (parent == null) {
            return true;
        }
        List<TreeNode> siblings = parent.getChildren();
        if (siblings == null || siblings.isEmpty()) {
            throw new IllegalStateException("Parent has no children");
        }
        return siblings.indexOf(this) == siblings.size() - 1;
    }
}